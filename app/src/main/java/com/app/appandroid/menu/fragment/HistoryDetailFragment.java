package com.app.appandroid.menu.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.appandroid.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class HistoryDetailFragment extends Fragment {

    private String QUERY_ESTABLECIMIENTO = "http://estilosapp.apphb.com/Estilos.svc/ObtenerEstablecimiento/";

    View rootview;

    private TextView textNombre;
    private TextView textDescripcion;
    private TextView textDireccion;
    private TextView textTelefono;
    private TextView textHorarioTitulo;
    private TextView textHorario;
    private TextView textDetalle;
    private TextView textServicio;
    private TextView textEstilista;
    private TextView textFecha;
    private TextView textHora;

    String idUsuario = "";
    String idEstablecimiento = "";
    String noEstablecimiento = "";
    String noEstilista = "";
    String noServicio = "";
    String fechahora = "";
    String fecha = "";
    String hora = "";

    String desEstablecimiento = "";
    String direccion = "";
    String telefono = "";
    String horario = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_history_detail, container, false);

        idUsuario = this.getArguments().getString("idUsuario");
        idEstablecimiento = this.getArguments().getString("idEstablecimiento");
        noEstablecimiento = this.getArguments().getString("noEstablecimiento");
        noEstilista = this.getArguments().getString("noEstilista");
        noServicio = this.getArguments().getString("noServicio");
        fechahora = this.getArguments().getString("hora");

        textNombre = (TextView)rootview.findViewById(R.id.textNombre);
        textDescripcion = (TextView)rootview.findViewById(R.id.textDescripcion);
        textDireccion = (TextView)rootview.findViewById(R.id.textDireccion);
        textTelefono = (TextView)rootview.findViewById(R.id.textTelefono);
        textHorarioTitulo = (TextView)rootview.findViewById(R.id.textHorarioTitulo);
        textHorario = (TextView)rootview.findViewById(R.id.textHorario);

        textDetalle = (TextView)rootview.findViewById(R.id.textDetalle);
        textServicio = (TextView)rootview.findViewById(R.id.textServicio);
        textEstilista = (TextView)rootview.findViewById(R.id.textEstilista);
        textFecha = (TextView)rootview.findViewById(R.id.textFecha);
        textHora = (TextView)rootview.findViewById(R.id.textHora);

        datosEstablecimiento(idEstablecimiento);

        textNombre.setText(noEstablecimiento);

        textServicio.setText("Servicio: "+noServicio);
        textEstilista.setText("Estilista: "+noEstilista);

        if(fechahora.length()!=0) {
            fecha = fechahora.substring(0,10);
            hora = fechahora.substring(11,16);
            textFecha.setText("Fecha: "+fecha);
            textHora.setText("Hora: "+hora);
        }
        else {
            textFecha.setText("Fecha: "+fechahora);
            textHora.setText("Hora: "+fechahora);
        }

        return rootview;
    }

    private void datosEstablecimiento(String id) {

        String urlString = id;

        AsyncHttpClient client = new AsyncHttpClient();

        Toast.makeText(rootview.getContext(), QUERY_ESTABLECIMIENTO+urlString, Toast.LENGTH_LONG).show();

        client.get(QUERY_ESTABLECIMIENTO+urlString, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                Toast.makeText(rootview.getContext().getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                if (jsonObject.has("desEstablecimiento")) {
                    desEstablecimiento = jsonObject.optString("desEstablecimiento","");
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

                textDescripcion.setText(desEstablecimiento);
                textDireccion.setText("Direccion: "+direccion);
                textTelefono.setText("Telf: "+telefono);
                textHorario.setText("L-V "+horario);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(rootview.getContext().getApplicationContext(), error.optString("Mensaje").toString()+"!", Toast.LENGTH_LONG).show();
            }
        });

    }

}
