package com.app.appandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class RegisterActivity extends Activity {

    private EditText editTextName;
    private EditText editTextMail;
    private EditText editTextPass1;
    private EditText editTextPass2;

    private Button buttonRegistrar;

    ProgressDialog mDialog;

    private String QUERY_REGISTRATION = "http://estilosapp.apphb.com/Estilos.svc/RegistrarUsuario/?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextMail = (EditText) findViewById(R.id.editTextMail);
        editTextPass1 = (EditText) findViewById(R.id.editTextPass1);
        editTextPass2 = (EditText) findViewById(R.id.editTextPass2);

        editTextName.setText("Carlos Martel");
        editTextMail.setText("cmartel@gmail.com");
        editTextPass1.setText("1234");
        editTextPass2.setText("1234");

        buttonRegistrar = (Button) findViewById(R.id.buttonRegistrar);
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarUsuario(editTextName.getText().toString(),
                        editTextMail.getText().toString(),
                        editTextPass1.getText().toString(),
                        editTextPass2.getText().toString());
            }
        });

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Registrando datos");
        mDialog.setCancelable(true);
    }

    private void RegistrarUsuario(String nombre, String correo, String pass1, String pass2) {

        if (nombre.equals("") || correo.equals("") || pass1.equals("") || pass2.equals("")) {
            Toast.makeText(this, "Debe llenar todos los campos!", Toast.LENGTH_LONG).show();
        }
        else
            if (!pass1.equals(pass2)) {
                Toast.makeText(this, "Password no coinciden!", Toast.LENGTH_LONG).show();
            }
            else {
                String urlString = "nombre="+nombre+"&correo="+correo+"&pass="+pass1;

                // Create a client to perform networking
                AsyncHttpClient client = new AsyncHttpClient();

                Toast.makeText(this, QUERY_REGISTRATION + urlString, Toast.LENGTH_LONG).show();

                // Show ProgressDialog to inform user that a task in the background is occurring
                mDialog.show();
                // Have the client get a JSONArray of data nd define how to respond
                client.get(QUERY_REGISTRATION + urlString, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        mDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                        NavUtils.navigateUpFromSameTask(RegisterActivity.this);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        mDialog.dismiss();

                        editTextName.setText("");
                        editTextMail.setText("");
                        editTextPass1.setText("");
                        editTextPass2.setText("");
                        Toast.makeText(getApplicationContext(), error.optString("Mensaje").toString()+"!", Toast.LENGTH_LONG).show();
                    }
                });
            }
    }

}