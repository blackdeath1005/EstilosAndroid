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
            holder.textServicio = (TextView) convertView.findViewById(R.id.textServicio);

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
        String noEstablecimiento = "";
        String noServicio = "";
        String fechahora = "";
        String fecha= "";

        if (jsonObject.has("noEstablecimiento")) {
            noEstablecimiento = jsonObject.optString("noEstablecimiento");
        }
        if (jsonObject.has("noServicio")) {
            noServicio = jsonObject.optString("noServicio");
        }
        if (jsonObject.has("hora")) {
            fechahora = jsonObject.optString("hora");
        }

        // Send these Strings to the TextViews for display
        if(fechahora.length()!=0) {
            fecha = fechahora.substring(0,10);
            holder.textFecha.setText(fecha);
        }
        else {
            holder.textFecha.setText(fechahora);
        }
        holder.textNombre.setText("Establecimiento: "+noEstablecimiento);
        holder.textServicio.setText("Servicio: "+noServicio);

        return convertView;
    }

    // this is used so you only ever have to do
    // inflation and finding by ID once ever per View
    private static class ViewHolder {
        public TextView textFecha;
        public TextView textNombre;
        public TextView textServicio;
    }

    public void updateData(JSONArray jsonArray) {
        // update the adapter's dataset
        mHistoryArray = jsonArray;
        notifyDataSetChanged();
    }

}