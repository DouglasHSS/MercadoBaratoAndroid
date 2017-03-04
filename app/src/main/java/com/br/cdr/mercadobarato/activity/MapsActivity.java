package com.br.cdr.mercadobarato.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.SuperMarketWrapper;
import com.br.cdr.mercadobarato.util.GooglePlacesJsonParser;
import com.br.cdr.mercadobarato.util.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private BootstrapButton mCheckin;
    private int mRange;
    private List<SuperMarketWrapper> mMarketList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_maps, container, false);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mCheckin = (BootstrapButton) view.findViewById(R.id.btn_checkin);
        mCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openFragment(HomeActivity.class, getFragmentManager());

//                Intent intent = new Intent(MapsActivity.this, HomeActivity.class);
//                startActivity(intent);
            }
        });


        return view;
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

        if (checkPermission()) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            if (!mMap.isMyLocationEnabled()) {
                mMap.setMyLocationEnabled(true);
            }

            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String provider = lm.getBestProvider(criteria, true);
                myLocation = lm.getLastKnownLocation(provider);
            }

            if (myLocation != null) {
                LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mRange = 1000;
                String url = getResources().getString(R.string.google_places_url) +
                        myLocation.getLatitude() + "%2C" +
                        myLocation.getLongitude() +
                        "&types=grocery_or_supermarket&radius=" +
                        mRange + "&key=" +
                        getResources().getString(R.string.google_places_key);

                AsyncHttpClient client = new AsyncHttpClient();

                geResultMaps(url, client);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14), 1500, null);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.locationNotFound), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void geResultMaps(String url, AsyncHttpClient client) {
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
                    public void onSuccess(JSONObject jsonObject) {
                        mMarketList = GooglePlacesJsonParser.parse(jsonObject.toString());
                        if (mMarketList != null) {
                            for (SuperMarketWrapper superMarket : mMarketList) {
                                mMap.addMarker(new MarkerOptions()
                                        .title(superMarket.getName())
                                        .snippet(superMarket.getAddress())
                                        .position(new LatLng(
                                                superMarket.getLat(),
                                                superMarket.getLng()
                                        ))
                                );
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.placesNotFound), Toast.LENGTH_LONG).show();
                        Log.e("RJGXM", statusCode + " " + throwable.getMessage());

                    }
                });
    }

    private boolean checkPermission() {
        return (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

}





