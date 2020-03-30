package com.frutosajniahperez.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class IniciarSesion2 extends AppCompatActivity {

    Button btnISProfe, btnISAdmin;
    ImageView btregresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion2);

        btnISAdmin = findViewById(R.id.btnISAdmin);
        btnISProfe = findViewById(R.id.btnISProfe);
        btregresar = findViewById(R.id.btnRegresar);

        btnISProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IniciarSesion2.this, InicioSesionExitoso.class));
            }
        });

        btnISAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IniciarSesion2.this, IniciarSesion2a.class));
            }
        });

        btregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IniciarSesion2.this, Principal.class));
            }
        });

    }
}
