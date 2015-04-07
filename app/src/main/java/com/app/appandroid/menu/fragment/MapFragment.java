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

import java.text.DecimalFormat;

public class MapFragment extends Fragment implements LocationProvider.LocationCallback, GoogleMap.OnInfoWindowClickListener {

    View rootView;
    MapView mMapView;
    private GoogleMap googleMap;
    private LocationProvider mLocationProvider;

    String idUsuario;
    String idEstablecimiento = "";
    String noEstablecimiento = "";
    String desEstablecimiento = "";
    String direccion = "";
    String telefono = "";
    String horario = "";

    private static String URI_ESTABLECIMIENTOS = "http://estilosapp.apphb.com/Estilos.svc/ObtenerListaEstablecimiento/";
    private static String URI_ESTABLECIMIENTO = "http://estilosapp.apphb.com/Estilos.svc/BuscarEstablecimiento/?";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationProvider = new LocationProvider(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.map_fragment, container, false);

        idUsuario = this.getArguments().getString("idUsuario");

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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        AsyncHttpClient client = new AsyncHttpClient();
        Toast.makeText(rootView.getContext(), URI_ESTABLECIMIENTOS, Toast.LENGTH_LONG).show();
        client.get(URI_ESTABLECIMIENTOS, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response){
                Toast.makeText(rootView.getContext().getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
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
        LatLng position = marker.getPosition();
        double latitude = position.latitude;
        double longitude = position.longitude;
        String latitudParam = "latitud="+String.format("%.7f", latitude);
        String longitudeParam = "longitud="+String.format("%.7f", longitude);
        latitudParam = latitudParam.replace(",",".");
        longitudeParam = longitudeParam.replace(",",".");

        AsyncHttpClient client = new AsyncHttpClient();

        Toast.makeText(rootView.getContext(), URI_ESTABLECIMIENTO+latitudParam+"&"+longitudeParam, Toast.LENGTH_LONG).show();
        client.get(URI_ESTABLECIMIENTO+latitudParam+"&"+longitudeParam, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                Toast.makeText(rootView.getContext().getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                DetailFragment detailFragment = new DetailFragment();
                if (jsonObject.has("idEstablecimiento")) {
                    idEstablecimiento = jsonObject.optString("idEstablecimiento","");
                }
                if (jsonObject.has("noEstablecimiento")) {
                    noEstablecimiento = jsonObject.optString("noEstablecimiento");
                }
                if (jsonObject.has("desEstablecimiento")) {
                    desEstablecimiento = jsonObject.optString("desEstablecimiento");
                }
                if (jsonObject.has("direccion")) {
                    direccion = jsonObject.optString("direccion");
                }
                if (jsonObject.has("telefono")) {
                    telefono = jsonObject.optString("telefono");
                }
                if (jsonObject.has("horario")) {
                    horario = jsonObject.optString("horario");
                }
                Bundle argsUsuario = new Bundle();
                argsUsuario.putString("idUsuario", idUsuario);
                argsUsuario.putString("idEstablecimiento", idEstablecimiento);
                argsUsuario.putString("noEstablecimiento", noEstablecimiento);
                argsUsuario.putString("desEstablecimiento", desEstablecimiento);
                argsUsuario.putString("direccion", direccion);
                argsUsuario.putString("telefono", telefono);
                argsUsuario.putString("horario", horario);
                detailFragment.setArguments(argsUsuario);
                ((MainActivity)getActivity()).changeFragment(detailFragment);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Log.d("MapFragment",error.toString());
            }
        });
    }
}
