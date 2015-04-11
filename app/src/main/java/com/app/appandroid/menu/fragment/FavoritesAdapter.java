package com.app.appandroid.menu.fragment;

import android.app.ProgressDialog;
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

public class FavoritesAdapter extends BaseAdapter {

    private String QUERY_DELETE_FAVORITES = "http://estilosapp.apphb.com/Estilos.svc/EliminarFavorito/";
    private String QUERY_LIST_FAVORITES = "http://estilosapp.apphb.com/Estilos.svc/ObtenerListaFavoritoUsuario/";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mFavoritesArray;
    String mIdUsuario;
    ProgressDialog mDialog;
    ProgressDialog mDialog2;

    public FavoritesAdapter(Context context, String idUsuario, ProgressDialog dial, ProgressDialog dial2) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mFavoritesArray = new JSONArray();
        mIdUsuario = idUsuario;
        mDialog = dial;
        mDialog2 = dial2;
    }

    @Override
    public int getCount() {
        return mFavoritesArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
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
            holder.imageDelete = (ImageView) convertView.findViewById(R.id.imageDelete);
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
        // Grab the title and author from the JSON
        String idFavorito = "";
        String noEstablecimiento = "";
        String direccion = "";
        String telefono = "";
        String horario = "";

        if (jsonObject.has("idFavorito")) {
            idFavorito = jsonObject.optString("idFavorito","");
        }
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

        holder.imageDelete.setTag(idFavorito);
        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarFavorito(v.getTag().toString());
            }
        });

        return convertView;
    }

    // this is used so you only ever have to do
    // inflation and finding by ID once ever per View
    private static class ViewHolder {
        public TextView textNombre;
        public ImageView imageDelete;
        public TextView textDireccion;
        public TextView textTelefono;
        public TextView textHorarioTitulo;
        public TextView textHorario;
    }

    public void updateData(JSONArray jsonArray) {
        // update the adapter's dataset
        mFavoritesArray = jsonArray;
        notifyDataSetChanged();
    }

    private void EliminarFavorito(String id) {

        String urlString = id;

        AsyncHttpClient client = new AsyncHttpClient();
        mDialog.show();

        client.get(QUERY_DELETE_FAVORITES+urlString, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject error) {
                mDialog.dismiss();
                Toast.makeText(mContext.getApplicationContext(), "Error en la eliminacion!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject jsonObject) {
                mDialog.dismiss();
                Toast.makeText(mContext.getApplicationContext(), jsonObject.optString("Mensaje").toString()+"!", Toast.LENGTH_LONG).show();
                ListFavorites(mIdUsuario);
            }
        });

    }

    private void ListFavorites(String id) {

        String urlString = id;

        AsyncHttpClient client = new AsyncHttpClient();
        mDialog2.show();

        client.get(QUERY_LIST_FAVORITES+urlString, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONArray jsonArray) {
                mDialog2.dismiss();
                updateData(jsonArray);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                mDialog2.dismiss();
                Toast.makeText(mContext.getApplicationContext(), error.optString("Mensaje").toString()+"!", Toast.LENGTH_LONG).show();
            }
        });

    }

}