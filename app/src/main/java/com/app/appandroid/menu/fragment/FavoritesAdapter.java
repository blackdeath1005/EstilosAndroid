package com.app.appandroid.menu.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.app.appandroid.R;

public class FavoritesAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mFavoritesArray;

    public FavoritesAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mFavoritesArray = new JSONArray();
        Toast.makeText(mContext.getApplicationContext(), "Favorites adapter", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getCount() {
        Toast.makeText(mContext.getApplicationContext(),"getCount "+ mFavoritesArray.length()+"", Toast.LENGTH_LONG).show();
        return mFavoritesArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        Toast.makeText(mContext.getApplicationContext(), "getItem "+mFavoritesArray.optJSONObject(position).toString(), Toast.LENGTH_LONG).show();
        return mFavoritesArray.optJSONObject(position);
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
            convertView = mInflater.inflate(R.layout.row_favorites, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.textNombre = (TextView) convertView.findViewById(R.id.textNombre);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.imageButton);
            holder.textDireccion = (TextView) convertView.findViewById(R.id.textDireccion);
            holder.textTelefono = (TextView) convertView.findViewById(R.id.textTelefono);
            holder.textHorarioTitulo = (TextView) convertView.findViewById(R.id.textHorarioTitulo);
            holder.textHorario = (TextView) convertView.findViewById(R.id.textHorario);

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
        Toast.makeText(mContext.getApplicationContext(), "getView "+position, Toast.LENGTH_LONG).show();
        Toast.makeText(mContext.getApplicationContext(), jsonObject.optString("noEstablecimiento").toString(), Toast.LENGTH_LONG).show();
        // Grab the title and author from the JSON
        String noEstablecimiento = "";
        String direccion = "";
        String telefono = "";
        String horario = "";

        if (jsonObject.has("noEstablecimiento")) {
            noEstablecimiento = jsonObject.optString("noEstablecimiento");
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
        // Send these Strings to the TextViews for display
        holder.textNombre.setText(noEstablecimiento);
        holder.textDireccion.setText(direccion);
        holder.textTelefono.setText("Telf: "+telefono);
        holder.textHorarioTitulo.setText("Horario de Atencion:");
        holder.textHorario.setText("L-V "+horario);

        return convertView;
    }

    // this is used so you only ever have to do
    // inflation and finding by ID once ever per View
    private static class ViewHolder {
        public TextView textNombre;
        public ImageButton imageButton;
        public TextView textDireccion;
        public TextView textTelefono;
        public TextView textHorarioTitulo;
        public TextView textHorario;
    }

    public void updateData(JSONArray jsonArray) {
        // update the adapter's dataset
        mFavoritesArray = jsonArray;
        Toast.makeText(mContext.getApplicationContext(), "ADAPTER", Toast.LENGTH_LONG).show();
        notifyDataSetChanged();
    }

}