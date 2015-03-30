package com.app.appandroid.menu.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.appandroid.R;
import com.app.appandroid.model.Service;
import com.app.appandroid.model.Stylist;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailFragment extends Fragment {

    View rootview;
    private JSONObject establecimiento;

    private TextView textViewName;
    private TextView textViewDesc;
    private TextView textViewDir;
    private TextView textViewOperationH;
    private TextView textViewPhone;

    private Spinner spinnerServices;
    private ArrayAdapter sevicesAdapter;
    private Spinner spinnerStylists;
    private ArrayAdapter stylistsAdapter;

    private static String URI_SERVICES = "http://estilosapp.apphb.com/Estilos.svc/ObtenerListaServicioEstablecimiento/";
    private static String URI_STYLISTS = "http://estilosapp.apphb.com/Estilos.svc/ObtenerListaEstilistaEstablecimiento/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_detail, container, false);
        initializeElements();
        return rootview;
    }

    private void initializeElements() {
        textViewName = (TextView)rootview.findViewById(R.id.textViewName);
        textViewDesc = (TextView)rootview.findViewById(R.id.textViewDesc);
        textViewDir = (TextView)rootview.findViewById(R.id.textViewDir);
        textViewOperationH = (TextView)rootview.findViewById(R.id.textViewOperationH);
        textViewPhone = (TextView)rootview.findViewById(R.id.textViewPhone);
        try {
            textViewName.setText(establecimiento.getString("noEstablecimiento"));
            textViewDesc.setText(establecimiento.getString("desEstablecimiento"));
            textViewDir.setText(establecimiento.getString("direccion"));
            textViewOperationH.setText(establecimiento.getString("horario"));
            textViewPhone.setText("Teléfono:  "+establecimiento.getString("telefono"));
            initializeSpinnerService(establecimiento.getInt("idEstablecimiento"));
            initializeSpinnerStylists(establecimiento.getInt("idEstablecimiento"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private  void initializeSpinnerService(int idEstablecimiento){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URI_SERVICES+idEstablecimiento, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response){
                try {
                    Service[] serviceArray= new Service[response.length()];
                    for (int i=0; i < response.length(); i++) {
                        Service service = new Service();
                        JSONObject jsonService= response.getJSONObject(i);
                        service.setId(jsonService.getInt("idServicio"));
                        service.setName(jsonService.getString("noServicio"));
                        serviceArray[i]= service;
                    }
                    spinnerServices =(Spinner) rootview.findViewById(R.id.spinnerService);
                    sevicesAdapter = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,serviceArray);
                    spinnerServices.setAdapter(sevicesAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Log.d("DetailFragment",error.toString());
            }
        });
    }

    private  void initializeSpinnerStylists(int idEstablecimiento){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URI_STYLISTS+idEstablecimiento, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response){
                try {
                    Stylist[] stylistArray= new Stylist[response.length()];
                    for (int i=0; i < response.length(); i++) {
                        Stylist stylist = new Stylist();
                        JSONObject jsonService= response.getJSONObject(i);
                        stylist.setId(jsonService.getInt("idEstilista"));
                        stylist.setName(jsonService.getString("noEstilista"));
                        stylistArray[i]= stylist;
                    }
                    spinnerStylists =(Spinner) rootview.findViewById(R.id.spinnerStylist);
                    stylistsAdapter = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,stylistArray);
                    spinnerStylists.setAdapter(stylistsAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Log.d("DetailFragment",error.toString());
            }
        });
    }

    public void setEstablecimiento(JSONObject establecimiento) {
        this.establecimiento = establecimiento;
    }
}