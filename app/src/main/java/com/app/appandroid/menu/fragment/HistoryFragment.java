package com.app.appandroid.menu.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.appandroid.MainActivity;
import com.app.appandroid.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.json.JSONArray;

public class HistoryFragment extends Fragment {

    private String QUERY_LIST_HISTORY = "http://estilosapp.apphb.com/Estilos.svc/ObtenerListaReservaUsuarioDESC/";

    View rootview;
    ListView historyListView;
    HistoryAdapter historyAdapter;

    String idUsuario;
    String idReserva = "";
    String idEstablecimiento = "";
    String noEstablecimiento = "";
    String noEstilista = "";
    String noServicio = "";
    String hora = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_history, container, false);

        idUsuario = this.getArguments().getString("idUsuario");

        historyListView = (ListView) rootview.findViewById(R.id.listviewHistory);
        historyAdapter = new HistoryAdapter(rootview.getContext(),idUsuario);
        // Set the ListView to use the ArrayAdapter
        historyListView.setAdapter(historyAdapter);
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                StartHistorytDetail(position);
            }
        });


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

    private void StartHistorytDetail(int position) {

        JSONObject jsonObject = (JSONObject) historyAdapter.getItem(position);
        idReserva = jsonObject.optString("idReserva");
        idEstablecimiento = jsonObject.optString("idEstablecimiento");
        noEstablecimiento = jsonObject.optString("noEstablecimiento");
        noEstilista = jsonObject.optString("noEstilista");
        noServicio = jsonObject.optString("noServicio");
        hora = jsonObject.optString("hora");

        HistoryDetailFragment historydetailFragment = new HistoryDetailFragment();

        Bundle argsUsuario = new Bundle();
        argsUsuario.putString("idUsuario", idUsuario);
        argsUsuario.putString("idReserva", idReserva);
        argsUsuario.putString("idEstablecimiento", idEstablecimiento);
        argsUsuario.putString("noEstablecimiento", noEstablecimiento);
        argsUsuario.putString("noEstilista", noEstilista);
        argsUsuario.putString("noServicio", noServicio);
        argsUsuario.putString("hora", hora);

        CharSequence tituloReservar;
        tituloReservar = getString(R.string.title_section_history_detail);
        historydetailFragment.setArguments(argsUsuario);
        ((MainActivity)getActivity()).changeFragment(historydetailFragment);
        ((MainActivity)getActivity()).restoreActionBar(tituloReservar);

    }

}