package com.app.appandroid.menu.fragment;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.app.appandroid.MainActivity;
import com.app.appandroid.R;
import com.app.appandroid.map.LocationProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapFragment extends Fragment implements LocationProvider.LocationCallback, GoogleMap.OnInfoWindowClickListener {

    View rootView;
    MapView mMapView;
    private GoogleMap googleMap;
    private LocationProvider mLocationProvider;

    private static String URI_ESTABLECIMIENTOS = "http://estilosapp.apphb.com/Estilos.svc/ObtenerListaEstablecimiento/";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationProvider = new LocationProvider(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.map_fragment, container, false);
        loadMap(savedInstanceState);
        return rootView;
    }

    private void loadMap(Bundle savedInstanceState) {
        if(mMapView == null){
            mMapView = (MapView) rootView.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume();// needed to get the map to display immediately
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            googleMap = mMapView.getMap();
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnInfoWindowClickListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mLocationProvider.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        mLocationProvider.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationProvider.disconnect();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void handleFirstLocation(Location location) {
        /*CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));*/
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URI_ESTABLECIMIENTOS, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response){
                try {    Log.d("MapFragment",response.toString());
                    response.length();
                    for (int i=0; i < response.length(); i++) {
                        JSONObject jsonEstablecimiento = response.getJSONObject(i);
                        double latitude = Double.parseDouble(jsonEstablecimiento.getString("latitud"));
                        double longitude = Double.parseDouble(jsonEstablecimiento.getString("longitud"));
                        MarkerOptions marker = new MarkerOptions().position(
                                new LatLng(latitude, longitude)).title(jsonEstablecimiento.getString("noEstablecimiento"));
                        googleMap.addMarker(marker);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Log.d("MapFragment",error.toString());
            }
        });
    }

    @Override
    public void handleNewLocation(Location location) {
        /*double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("Hello Maps");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));*/
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        ((MainActivity)getActivity()).changeFragment(new DetailFragment());
    }
}
