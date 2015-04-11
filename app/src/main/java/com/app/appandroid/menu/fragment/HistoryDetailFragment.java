package com.app.appandroid.menu.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.appandroid.MainActivity;
import com.app.appandroid.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class HistoryDetailFragment extends Fragment {

    private String QUERY_ESTABLECIMIENTO = "http://estilosapp.apphb.com/Estilos.svc/ObtenerEstablecimiento/";
    private String QUERY_DELETE_RESERVATION = "http://estilosapp.apphb.com/Estilos.svc/CancelarReserva/";

    View rootview;

    private TextView textNombre;
    private TextView textDescripcion;
    private TextView textDireccion;
    private TextView textTelefono;
    private TextView textHorarioTitulo;
    private TextView textHorario;
    private TextView textDetalle;
    private ImageView imageCancel;
    private TextView textServicio;
    private TextView textEstilista;
    private TextView textFecha;
    private TextView textHora;

    String idUsuario = "";
    String idReserva = "";
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

    ProgressDialog mDialog;
    ProgressDialog mDialog2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_history_detail, container, false);

        idUsuario = this.getArguments().getString("idUsuario");
        idReserva = this.getArguments().getString("idReserva");
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
        imageCancel = (ImageView) rootview.findViewById(R.id.imageCancel);
        textServicio = (TextView)rootview.findViewById(R.id.textServicio);
        textEstilista = (TextView)rootview.findViewById(R.id.textEstilista);
        textFecha = (TextView)rootview.findViewById(R.id.textFecha);
        textHora = (TextView)rootview.findViewById(R.id.textHora);

        mDialog = new ProgressDialog(rootview.getContext());
        mDialog.setMessage("Leyendo Reserva...");
        mDialog.setCancelable(true);

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

        imageCancel.setTag(idReserva);
        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarReserva(v.getTag().toString());
            }
        });

        mDialog2 = new ProgressDialog(rootview.getContext());
        mDialog2.setMessage("Cancelando Reserva...");
        mDialog2.setCancelable(true);

        return rootview;
    }

    private void datosEstablecimiento(String id) {

        String urlString = id;

        AsyncHttpClient client = new AsyncHttpClient();
        mDialog.show();

        client.get(QUERY_ESTABLECIMIENTO+urlString, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                mDialog.dismiss();

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
                mDialog.dismiss();
                Toast.makeText(rootview.getContext().getApplicationContext(), error.optString("Mensaje").toString()+"!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void EliminarReserva(String id) {

        String urlString = id;

        AsyncHttpClient client = new AsyncHttpClient();
        mDialog2.show();

        client.get(QUERY_DELETE_RESERVATION+urlString, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject error) {
                mDialog2.dismiss();
                Toast.makeText(rootview.getContext().getApplicationContext(), "Error en la eliminacion!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject jsonObject) {
                mDialog2.dismiss();
                Toast.makeText(rootview.getContext().getApplicationContext(), jsonObject.optString("Mensaje").toString()+"!", Toast.LENGTH_LONG).show();
                StartFragmentHistory();
            }
        });

    }

    private void StartFragmentHistory() {

        HistoryFragment historyFragment = new HistoryFragment();

        Bundle argsUsuario = new Bundle();
        argsUsuario.putString("idUsuario", idUsuario);

        CharSequence tituloHistory;
        tituloHistory = getString(R.string.title_section_history);
        historyFragment.setArguments(argsUsuario);
        ((MainActivity)getActivity()).changeFragment(historyFragment);
        ((MainActivity)getActivity()).restoreActionBar(tituloHistory);

    }

}
