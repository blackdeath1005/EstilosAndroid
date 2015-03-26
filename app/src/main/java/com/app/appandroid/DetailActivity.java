package com.app.appandroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.TextView;

public class DetailActivity  extends ActionBarActivity {

    TextView textId;
    TextView textNombre;
    TextView textCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_detail);

        // Enable the "Up" button for more navigation options
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 13. unpack the coverID from its trip inside your Intent
        String idUsuario = this.getIntent().getExtras().getString("idUsuario");
        String noUsuario = this.getIntent().getExtras().getString("noUsuario");
        String correoUsuario = this.getIntent().getExtras().getString("correoUsuario");

        textId = (TextView) findViewById(R.id.txtIdUsuario);
        textNombre = (TextView) findViewById(R.id.txtNombreUsuario);
        textCorreo = (TextView) findViewById(R.id.txtCorreoUsuario);

        textId.setText(idUsuario);
        textNombre.setText(noUsuario);
        textCorreo.setText(correoUsuario);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}

