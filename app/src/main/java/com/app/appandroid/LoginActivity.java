package com.app.appandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class LoginActivity extends Activity {

    private EditText editTextMail;
    private EditText editTextPass;
    private Button buttonLogin;
    private Button buttonRegistrar;

    ProgressDialog mDialog;

    private String QUERY_AUTENTICACION = "http://estilosapp.apphb.com/Estilos.svc/AutenticarUsuario/?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextMail = (EditText) findViewById(R.id.editTextMail);
        editTextPass = (EditText) findViewById(R.id.editTextPass);

        editTextMail.setText("blackdeath1005@gmail.com");
        editTextPass.setText("1234");

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutenticarUsuario(editTextMail.getText().toString(), editTextPass.getText().toString());
            }
        });

        buttonRegistrar = (Button) findViewById(R.id.buttonRegistrar);
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarUsuario();
            }
        });

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Validando datos");
        mDialog.setCancelable(true);
    }

    private void RegistrarUsuario() {

    }

    private void AutenticarUsuario(String correo, String pass) {

        String urlString = "correo="+correo+"&pass="+pass;

        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();

        Toast.makeText(this, QUERY_AUTENTICACION + urlString, Toast.LENGTH_LONG).show();

        // Show ProgressDialog to inform user that a task in the background is occurring
        mDialog.show();
        // Have the client get a JSONArray of data nd define how to respond
        client.get(QUERY_AUTENTICACION + urlString, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject jsonObject) {
                mDialog.dismiss();

                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                LoginCorrecto(jsonObject);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                mDialog.dismiss();

                editTextMail.setText("");
                editTextPass.setText("");
                Toast.makeText(getApplicationContext(), error.optString("Mensaje").toString()+"!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void LoginCorrecto(JSONObject jsonObject) {

        String idUsuario = jsonObject.optString("idUsuario");
        String noUsuario = jsonObject.optString("noUsuario");
        String correoUsuario = jsonObject.optString("correoUsuario");

        // create an Intent to take you over to a new DetailActivity
        Intent intentMap = new Intent(LoginActivity.this,MainActivity.class);
        // pack away the data into your Intent before you head out
        intentMap.putExtra("idUsuario", idUsuario);
        intentMap.putExtra("noUsuario", noUsuario);
        intentMap.putExtra("correoUsuario", correoUsuario);

        // start the next Activity using your prepared Intent
        startActivity(intentMap);

    }

}
