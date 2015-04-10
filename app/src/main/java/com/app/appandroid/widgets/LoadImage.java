package com.app.appandroid.widgets;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.appandroid.MainActivity;

import java.io.InputStream;
import java.net.URL;

public class LoadImage extends AsyncTask<String, String, Bitmap> {

    Context mContext;
    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;

    public LoadImage(Context context,ProgressDialog dial,ImageView imagen) {
        mContext = context;
        pDialog = dial;
        img = imagen;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.setMessage("Cargando Imagen...");
        pDialog.show();
    }
    protected Bitmap doInBackground(String... args) {
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    protected void onPostExecute(Bitmap image) {
        if(image != null){
            img.setImageBitmap(image);
            pDialog.dismiss();
        }else{
            pDialog.dismiss();
            Toast.makeText(mContext.getApplicationContext(), "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
        }
    }
}






