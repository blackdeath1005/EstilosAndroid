package com.app.appandroid.menu.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.appandroid.R;
import com.app.appandroid.model.Service;
import com.app.appandroid.model.Stylist;
import com.app.appandroid.widgets.DatePickerDialogFragment;
import com.app.appandroid.widgets.TimePickerDialogFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DetailFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    View rootview;

    public ImageView imageNoFavorite;
    public ImageView imageFavorite;
    private TextView textViewName;
    private TextView textViewDesc;
    private TextView textViewDir;
    private TextView textViewOperationH;
    private TextView textViewPhone;
    private TextView textViewDate;
    private TextView textViewTime;
    private Button buttonReservar;

    private Spinner spinnerServices;
    private ArrayAdapter sevicesAdapter;
    private Spinner spinnerStylists;
    private ArrayAdapter stylistsAdapter;
    String idUsuario;
    int idEstablecimiento = 0;
    String noEstablecimiento = "";
    String desEstablecimiento = "";
    String direccion = "";
    String telefono = "";
    String horario = "";

    private static String IMAGE_URL_BASE = "http://bellezaperu.com/images/clientes/166/amarige_am01.jpg";

    private static String URI_SERVICES = "http://estilosapp.apphb.com/Estilos.svc/ObtenerListaServicioEstablecimiento/";
    private static String URI_STYLISTS = "http://estilosapp.apphb.com/Estilos.svc/ObtenerListaEstilistaEstablecimiento/";

    private String QUERY_RESERVATION = "http://estilosapp.apphb.com/Estilos.svc/RegistrarReserva/?";
    private String QUERY_FAVORITE = "http://estilosapp.apphb.com/Estilos.svc/AgregrarFavorito/?";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_detail, container, false);

        idUsuario = this.getArguments().getString("idUsuario");
        idEstablecimiento = Integer.parseInt(this.getArguments().getString("idEstablecimiento"));
        noEstablecimiento = this.getArguments().getString("noEstablecimiento");
        desEstablecimiento = this.getArguments().getString("desEstablecimiento");
        direccion = this.getArguments().getString("direccion");
        telefono = this.getArguments().getString("telefono");
        horario = this.getArguments().getString("horario");

        initializeElements();
        return rootview;
    }

    private void initializeElements() {

        ImageView imageEstablecimiento = (ImageView) rootview.findViewById(R.id.imageViewPortrait);
        Picasso.with(rootview.getContext()).load(IMAGE_URL_BASE).placeholder(R.mipmap.ic_launcher).into(imageEstablecimiento);

        imageNoFavorite = (ImageView) rootview.findViewById(R.id.imageNoFavorite);
        imageNoFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarFavorito();
            }
        });

        imageFavorite = (ImageView) rootview.findViewById(R.id.imageFavorite);

        textViewName = (TextView)rootview.findViewById(R.id.textViewName);
        textViewDesc = (TextView)rootview.findViewById(R.id.textViewDesc);
        textViewDir = (TextView)rootview.findViewById(R.id.textViewDir);
        textViewOperationH = (TextView)rootview.findViewById(R.id.textViewOperationH);
        textViewPhone = (TextView)rootview.findViewById(R.id.textViewPhone);
        textViewDate = (TextView)rootview.findViewById(R.id.textViewDate);
        textViewDate.setText(DATE_FORMATTER.format(new Date()));
        textViewDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DatePickerDialogFragment newFragment = new DatePickerDialogFragment();
                newFragment.setOnDateSetListener(DetailFragment.this);
                newFragment.show(ft, "date_dialog");
            }
        });
        textViewTime = (TextView)rootview.findViewById(R.id.textViewTime);
        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                TimePickerDialogFragment newFragment = new TimePickerDialogFragment();
                newFragment.setOnTimeSetListener(DetailFragment.this);
                newFragment.show(ft,"time_dialog");
            }
        });
        textViewName.setText(noEstablecimiento);
        textViewDesc.setText(desEstablecimiento);
        textViewDir.setText(direccion);
        textViewOperationH.setText(horario);
        textViewPhone.setText("Teléfono:  "+telefono);
        initializeSpinnerService(idEstablecimiento);
        initializeSpinnerStylists(idEstablecimiento);

        buttonReservar = (Button)rootview.findViewById(R.id.buttonReservar);
        buttonReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservarEstablecimiento();
            }
        });
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

    private void ReservarEstablecimiento() {

        String hh = textViewTime.getText().toString();

        if(hh.length()!=0) {

            Service servicio = (Service) spinnerServices.getSelectedItem();
            String codServicio = servicio.getId() + "";

            Stylist estilista = (Stylist) spinnerStylists.getSelectedItem();
            String codEstilista = estilista.getId() + "";

            String codEstablecimiento = idEstablecimiento + "";
            String codUsuario = idUsuario;

            String fecha = textViewDate.getText().toString();
            String dia = fecha.substring(0, 2);
            String mes = fecha.substring(3, 5);
            String anno = fecha.substring(6, 10);
            String fechaSQL = anno + "-" + mes + "-" + dia;
            String hora = textViewTime.getText().toString() + ":00";

            String urlString = "codUsuario=" + codUsuario + "&codEstablecimiento=" + codEstablecimiento + "&codEstilista=" + codEstilista + "&codServicio=" + codServicio + "&hora=" + fechaSQL + "%20" + hora;

            // Create a client to perform networking
            AsyncHttpClient client = new AsyncHttpClient();

            Toast.makeText(rootview.getContext(), QUERY_RESERVATION + urlString, Toast.LENGTH_LONG).show();

            // Have the client get a JSONArray of data nd define how to respond
            client.get(QUERY_RESERVATION + urlString, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(JSONObject jsonObject) {

                    Toast.makeText(rootview.getContext().getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                    //LoginCorrecto(jsonObject);
                }

                @Override
                public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                    Toast.makeText(rootview.getContext().getApplicationContext(), error.optString("Mensaje").toString() + "!", Toast.LENGTH_LONG).show();
                }
            });

        }
        else {
            Toast.makeText(rootview.getContext(),"Seleccione Hora de Reserva!", Toast.LENGTH_LONG).show();
        }
    }

    private void AgregarFavorito() {

        String codEstablecimiento = idEstablecimiento+"";
        String codUsuario = idUsuario;

        String urlString = "codUsuario="+codUsuario+"&codEstablecimiento="+codEstablecimiento;

        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();

        Toast.makeText(rootview.getContext(), QUERY_FAVORITE + urlString, Toast.LENGTH_LONG).show();

        // Have the client get a JSONArray of data nd define how to respond
        client.get(QUERY_FAVORITE + urlString, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {

                Toast.makeText(rootview.getContext().getApplicationContext(), "Establecimiento hecho favorito!", Toast.LENGTH_LONG).show();
                imageNoFavorite.setVisibility(View.GONE);
                imageFavorite.setVisibility(View.VISIBLE);
                //LoginCorrecto(jsonObject);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                Toast.makeText(rootview.getContext().getApplicationContext(), error.optString("Mensaje").toString()+"!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        textViewDate.setText(DATE_FORMATTER.format(calendar.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hourToShow = hourOfDay<10 ? "0"+hourOfDay : hourOfDay+"";
        textViewTime.setText(hourToShow+":00");
    }
}