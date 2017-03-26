package com.br.cdr.mercadobarato.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.model.SuperMarketWrapper;
import com.br.cdr.mercadobarato.util.GooglePlacesJsonParser;
import com.br.cdr.mercadobarato.util.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.LOCATION_SERVICE;
import static com.br.cdr.mercadobarato.util.Utils.distanceBetween;
import static com.br.cdr.mercadobarato.util.Utils.getPreferredDistance;

public class ComparePriceFormFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private List<SuperMarketWrapper> mMarketList;
    private HashMap<LatLng, SuperMarketWrapper> mSuperMarkertWrapperMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_compare_price_form, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        // Verifica se o GPS está ativo
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Caso não esteja ativo abre um novo diálogo com as configurações para
        // realizar se ativamento
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }


        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this) //Be aware of state of the connection
                .addOnConnectionFailedListener(this) //Be aware of failures
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();


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

    public void pararConexaoComGoogleApi() {
        //Verificando se está conectado para então cancelar a conexão!
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            Log.i("latitude", "" + lastLocation.getLatitude());
            Log.i("longitude", "" + lastLocation.getLongitude());
            searchGrocerys(lastLocation);


        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        pararConexaoComGoogleApi();
    }


    public void searchGrocerys(final Location lastLocation) {

        double latitude = lastLocation.getLatitude();
        double longitude = lastLocation.getLongitude();
        int mRange = Utils.getPreferredDistance(getActivity()) * 1000;
        LatLng userLocation = new LatLng(latitude, longitude);
        String url = getResources().getString(R.string.google_places_url) +
                latitude + "%2C" + longitude + "&types=grocery_or_supermarket&radius=" + mRange + "&key=" +
                getResources().getString(R.string.google_places_key);

        Log.i("urlMAPS", url);

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
                        Log.i("markets", jsonObject.toString());
                        Marker marker;
                        mSuperMarkertWrapperMap = new HashMap<>();
                        if (mMarketList != null) {
                            for (SuperMarketWrapper superMarket : mMarketList) {
//                                mSuperMarkertWrapperMap.put(marker.getPosition(), superMarket);
                                Log.i("marketId", superMarket.getID());

                            }

                            Double mDistance;
//                            Location markerLocation = new Location("");
//                            markerLocation.setLatitude(marker.getPosition().latitude);
//                            markerLocation.setLongitude(marker.getPosition().longitude);

//                            mDistance = distanceBetween(lastLocation, markerLocation);
//                            if (mDistance <= 5000) {

//                                LatLng latLng = new LatLng(marker.getPosition().latitude,
//                                        marker.getPosition().longitude);
//                                SuperMarketWrapper wrapper = mSuperMarkertWrapperMap.get(latLng);
//                                Intent it = new Intent(getActivity(), SuperMarketCheckedInActivity.class);
//                                it.putExtra("superMarkerWrapper", wrapper);
//                                getActivity().startActivity(it);

                                           /* Toast.makeText(getActivity(), marker.getTitle(),
                                                    Toast.LENGTH_LONG).show();*/

//                            } else {
//                                Toast.makeText(getActivity(), getResources().getString(R.string.checkInFailed),
//                                        Toast.LENGTH_LONG).show();
//
//                            }

                        }
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.placesNotFound), Toast.LENGTH_LONG).show();
                        Log.e("RJGXM", statusCode + " " + throwable.getMessage());

                    }
                });


    }

}
