package com.app.appandroid.menu.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.app.appandroid.R;

public class DetailFragment extends Fragment {

    View rootview;
    ArrayAdapter<String> sevicesAdapter;
    String[] services ={"Servicio 1","Servicio 2","Servicio 3","Servicio 4","Servicio 5"};
    Spinner spinnerServices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_detail, container, false);

        spinnerServices =(Spinner) rootview.findViewById(R.id.spinnerService);
        sevicesAdapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,services);
        spinnerServices.setAdapter(sevicesAdapter);

        return rootview;
    }

}