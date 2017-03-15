package com.br.cdr.mercadobarato.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.br.cdr.mercadobarato.Manifest;
import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.SuperMarketWrapper;
import com.br.cdr.mercadobarato.util.GooglePlacesJsonParser;
import com.br.cdr.mercadobarato.util.Utils;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.*;
import static com.google.zxing.client.android.HttpHelper.ContentType.JSON;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private BootstrapButton mCheckin;
    private Location mLocation;
    private CrystalSeekbar mRangebar;
    private int mRange;
    private List<SuperMarketWrapper> mMarketList;
    private Map<LatLng, SuperMarketWrapper> mSuperMarkertWrapperMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // get seekbar from view
        mRangebar = (CrystalSeekbar) view.findViewById(R.id.rangeSeekbar1);
        // get min and max text view
        final TextView tvRange = (TextView) view.findViewById(R.id.textMin1);


        mCheckin = (BootstrapButton) view.findViewById(R.id.btn_checkin);

        mRangebar.setMaxValue(25);
        mRangebar.setMinValue(1);
        mRangebar.setMinStartValue(getPreferredDistance()).apply();

        // set listener
        mRangebar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                tvRange.setText(String.valueOf(value));

            }
        });

        // set final value listener
        mRangebar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                Log.d("CRS=>", String.valueOf(value));
                mRange = (value.intValue() * 1000);
                setPreferredDistance(mRange);
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Alguma permissão foi negada, agora é com você :-)
                Utils.alertAndFinish(getActivity());
                return;
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        } else {

            if (!mMap.isMyLocationEnabled())
                mMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            mLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (mLocation == null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String provider = lm.getBestProvider(criteria, true);
                mLocation = lm.getLastKnownLocation(provider);
            }

            mCheckin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    searchLocation(v, mLocation);
                }
            });
            
        }
    }

    public void searchLocation(View v, Location myLocation) {
        if (myLocation != null) {
            LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            String url = getResources().getString(R.string.google_places_url) +
                    myLocation.getLatitude() + "%2C" + myLocation.getLongitude() + "&types=grocery_or_supermarket&radius=" + mRange + "&key=" +
                    getResources().getString(R.string.google_places_key);

            AsyncHttpClient client = new AsyncHttpClient();

            client.get(url,
                    new JsonHttpResponseHandler() {
                        /**
                         * verifica se a requisição obteve sucesso ou falha, em caso de sucesso
                         * a listRestultsActiviy é chamada com os objetos obtidos no JSON em formato
                         * de String
                         *
                         * @param jsonObject
                         */
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                            mMarketList = GooglePlacesJsonParser.parse(jsonObject.toString());
                            Marker marker;
                            mSuperMarkertWrapperMap = new HashMap<LatLng, SuperMarketWrapper>();
                            if (mMarketList != null) {
                                for (SuperMarketWrapper superMarket : mMarketList) {
                                    marker = mMap.addMarker(new MarkerOptions()
                                            .title(superMarket.getName())
                                            .snippet(superMarket.getAddress())
                                            .position(new LatLng(
                                                    superMarket.getLat(),
                                                    superMarket.getLng()
                                            ))
                                    );
                                    mSuperMarkertWrapperMap.put(marker.getPosition(), superMarket);
                                }

                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        Double mDistance;
                                        Location markerLocation = new Location("");
                                        markerLocation.setLatitude(marker.getPosition().latitude);
                                        markerLocation.setLongitude(marker.getPosition().longitude);

                                        mDistance = distanceBetween(mLocation, markerLocation);
                                        if (mDistance <= 5000) {

                                            LatLng latLng = new LatLng(marker.getPosition().latitude,
                                                    marker.getPosition().longitude);
                                            SuperMarketWrapper wrapper = mSuperMarkertWrapperMap.get(latLng);
                                            Intent it = new Intent(getActivity(), SuperMarketCheckedInActivity.class);
                                            it.putExtra("superMarkerWrapper", wrapper);
                                            getActivity().startActivity(it);

                                           /* Toast.makeText(getActivity(), marker.getTitle(),
                                                    Toast.LENGTH_LONG).show();*/

                                        } else {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.checkInFailed),
                                                    Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.placesNotFound), Toast.LENGTH_LONG).show();
                            Log.e("RJGXM", statusCode + " " + throwable.getMessage());

                        }
                    });

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14), 1500, null);
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.locationNotFound), Toast.LENGTH_LONG).show();
        }
    }


    double distanceBetween(Location l1, Location l2) {

        double lat1 = l1.getLatitude();
        double lon1 = l1.getLongitude();
        double lat2 = l2.getLatitude();
        double lon2 = l2.getLongitude();
        double R = 6371; // km
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        lat1 = lat1 * Math.PI / 180;
        lat2 = lat2 * Math.PI / 180;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c * 1000;
        return d;
    }

    public void setPreferredDistance(int preferredDistance){
        SharedPreferences sharedPref = getActivity()
                                       .getSharedPreferences(getString(R.string.app_preferences),
                                                             Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.preferred_distance), preferredDistance);
        editor.commit();
    }

    public int getPreferredDistance(){
        SharedPreferences sharedPref = getActivity()
                .getSharedPreferences(getString(R.string.app_preferences),
                        Context.MODE_PRIVATE);
        int preferredDistance = sharedPref.getInt(getString(R.string.preferred_distance), 1000)/1000;
        return preferredDistance ;
    }
}






