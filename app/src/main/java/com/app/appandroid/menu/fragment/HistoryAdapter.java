package com.app.appandroid.menu.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.appandroid.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HistoryAdapter extends BaseAdapter {

    //private String QUERY_LIST_HISTORY = "http://estilosapp.apphb.com/Estilos.svc/ObtenerListaReservaUsuario/";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mHistoryArray;
    String mIdUsuario;

    public HistoryAdapter(Context context, String idUsuario) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mHistoryArray = new JSONArray();
        mIdUsuario = idUsuario;
    }

    @Override
    public int getCount() {
        return mHistoryArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return mHistoryArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        // your particular dataset uses String IDs
        // but you have to put something in this method
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.row_history, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.textFecha = (TextView) convertView.findViewById(R.id.textFecha);
            holder.textNombre = (TextView) convertView.findViewById(R.id.textNombre);
            holder.textDireccion = (TextView) convertView.findViewById(R.id.textDireccion);
            holder.textTelefono = (TextView) convertView.findViewById(R.id.textTelefono);
            holder.textHora = (TextView) convertView.findViewById(R.id.textHora);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }
        // More code after this

        // Get the current book's data in JSON form
        JSONObject jsonObject = (JSONObject) getItem(position);
        // Grab the title and author from the JSON
        String idEstablecimiento = "";
        String noEstablecimiento = "";
        String fechahora = "";
        String fecha= "";
        String hora = "";

        if (jsonObject.has("idEstablecimiento")) {
            idEstablecimiento = jsonObject.optString("idEstablecimiento");
        }
        if (jsonObject.has("noEstablecimiento")) {
            noEstablecimiento = jsonObject.optString("noEstablecimiento");
        }
        if (jsonObject.has("hora")) {
            fechahora = jsonObject.optString("hora");
        }

        // Send these Strings to the TextViews for display
        if(fechahora.length()!=0) {
            fecha = fechahora.substring(0,10);
            hora = fechahora.substring(11,16);
            holder.textFecha.setText(fecha);
            holder.textHora.setText("Hora: "+hora);
        }
        else {
            holder.textFecha.setText(fechahora);
            holder.textHora.setText("Hora: "+fechahora);
        }
        holder.textNombre.setText("Establecimiento: "+noEstablecimiento);
        holder.textDireccion.setText("Direccion: "+idEstablecimiento);
        //holder.textTelefono.setText("Telf: "+telefono);

        return convertView;
    }

    // this is used so you only ever have to do
    // inflation and finding by ID once ever per View
    private static class ViewHolder {
        public TextView textFecha;
        public TextView textNombre;
        public TextView textDireccion;
        public TextView textTelefono;
        public TextView textHora;
    }

    public void updateData(JSONArray jsonArray) {
        // update the adapter's dataset
        mHistoryArray = jsonArray;
        notifyDataSetChanged();
    }

}