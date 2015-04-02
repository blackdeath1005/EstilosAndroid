package com.app.appandroid.menu.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.appandroid.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.json.JSONArray;

public class HistoryFragment extends Fragment {

    private String QUERY_LIST_HISTORY = "http://estilosapp.apphb.com/Estilos.svc/ObtenerListaReservaUsuario/";

    View rootview;
    ListView historyListView;
    HistoryAdapter historyAdapter;
    String idUsuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_history, container, false);

        idUsuario = this.getArguments().getString("idUsuario");

        historyListView = (ListView) rootview.findViewById(R.id.listviewHistory);
        historyAdapter = new HistoryAdapter(rootview.getContext(),idUsuario);
        // Set the ListView to use the ArrayAdapter
        historyListView.setAdapter(historyAdapter);

        ListHistory(idUsuario);

        return rootview;
    }

    private void ListHistory(String id) {

        String urlString = id;

        AsyncHttpClient client = new AsyncHttpClient();

        Toast.makeText(rootview.getContext(), QUERY_LIST_HISTORY+urlString, Toast.LENGTH_LONG).show();

        client.get(QUERY_LIST_HISTORY+urlString, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONArray jsonArray) {
                historyAdapter.updateData(jsonArray);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(rootview.getContext().getApplicationContext(), error.optString("Mensaje").toString()+"!", Toast.LENGTH_LONG).show();
            }
        });

    }

}