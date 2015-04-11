package com.app.appandroid.menu.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
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

public class FavoritesFragment extends Fragment {

    private String QUERY_LIST_FAVORITES = "http://estilosapp.apphb.com/Estilos.svc/ObtenerListaFavoritoUsuario/";

    View rootview;
    ListView favoritesListView;
    FavoritesAdapter favoritesAdapter;

    String idUsuario;
    String idEstablecimiento = "";
    String noEstablecimiento = "";
    String desEstablecimiento = "";
    String direccion = "";
    String telefono = "";
    String horario = "";
    String imagen = "";
    String imagen2 = "";
    String imagen3 = "";

    ProgressDialog mDialog;
    ProgressDialog mDialog2;
    ProgressDialog mDialog3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_favorites, container, false);

        idUsuario = this.getArguments().getString("idUsuario");

        mDialog2 = new ProgressDialog(rootview.getContext());
        mDialog2.setMessage("Eliminando Favorito...");
        mDialog3 = new ProgressDialog(rootview.getContext());
        mDialog3.setMessage("Buscando Favoritos...");

        favoritesListView = (ListView) rootview.findViewById(R.id.listviewFavoritos);
        favoritesAdapter = new FavoritesAdapter(rootview.getContext(),idUsuario,mDialog2,mDialog3);
        // Set the ListView to use the ArrayAdapter
        favoritesListView.setAdapter(favoritesAdapter);
        favoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                StartFragmentDetail(position);
            }
        });

        mDialog = new ProgressDialog(rootview.getContext());
        mDialog.setMessage("Buscando Favoritos...");
        mDialog.setCancelable(true);

        ListFavorites(idUsuario);

        return rootview;
    }

    private void ListFavorites(String id) {

        String urlString = id;

        AsyncHttpClient client = new AsyncHttpClient();
        mDialog.show();

        client.get(QUERY_LIST_FAVORITES+urlString, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONArray jsonArray) {
                mDialog.dismiss();
                favoritesAdapter.updateData(jsonArray);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                mDialog.dismiss();
                Toast.makeText(rootview.getContext().getApplicationContext(), error.optString("Mensaje").toString()+"!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void StartFragmentDetail(int position) {

        JSONObject jsonObject = (JSONObject) favoritesAdapter.getItem(position);
        String idFavorito = jsonObject.optString("idFavorito");
        idEstablecimiento = jsonObject.optString("idEstablecimiento");
        noEstablecimiento = jsonObject.optString("noEstablecimiento");
        desEstablecimiento = jsonObject.optString("desEstablecimiento");
        direccion = jsonObject.optString("direccion");
        telefono = jsonObject.optString("telefono");
        horario = jsonObject.optString("horario");
        imagen = jsonObject.optString("imagen");
        imagen2 = jsonObject.optString("imagen2");
        imagen3 = jsonObject.optString("imagen3");

        DetailFragment detailFragment = new DetailFragment();

        Bundle argsUsuario = new Bundle();
        argsUsuario.putString("idUsuario", idUsuario);
        argsUsuario.putString("idEstablecimiento", idEstablecimiento);
        argsUsuario.putString("noEstablecimiento", noEstablecimiento);
        argsUsuario.putString("desEstablecimiento", desEstablecimiento);
        argsUsuario.putString("direccion", direccion);
        argsUsuario.putString("telefono", telefono);
        argsUsuario.putString("horario", horario);
        argsUsuario.putString("imagen", imagen);
        argsUsuario.putString("imagen2", imagen2);
        argsUsuario.putString("imagen3", imagen3);

        CharSequence tituloReservar;
        tituloReservar = getString(R.string.title_section_reservation);
        detailFragment.setArguments(argsUsuario);
        ((MainActivity)getActivity()).changeFragment(detailFragment);
        ((MainActivity)getActivity()).restoreActionBar(tituloReservar);

    }

}

