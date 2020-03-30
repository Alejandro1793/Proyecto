package com.frutosajniahperez.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IniciarSesion2 extends AppCompatActivity {

    Button btnISProfe, btnISAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion2);

        btnISAdmin = findViewById(R.id.btnISAdmin);
        btnISProfe = findViewById(R.id.btnISProfe);

        btnISProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IniciarSesion2.this, InicioSesionExitoso.class));
            }
        });

        btnISAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IniciarSesion2.this, IniciarSesion.class));
            }
        });
    }
}
