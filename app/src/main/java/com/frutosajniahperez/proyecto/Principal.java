package com.frutosajniahperez.proyecto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Principal extends AppCompatActivity {
    Button btnRegistro, btnInicioSesion;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_principal1);
        btnRegistro = (findViewById(R.id.btnRegistro));
        btnInicioSesion = (findViewById(R.id.btnInicioSesion));

        //Pantalla para iniciar Sesion

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, IniciarSesion2.class));
            }
        });


        //Pantalla para registrar usuario
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, RegistroAdministrador2b.class));
            }
        });
    }




}
