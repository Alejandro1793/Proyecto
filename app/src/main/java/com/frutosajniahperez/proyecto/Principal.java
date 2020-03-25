package com.frutosajniahperez.proyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Principal extends AppCompatActivity {
    Button btnRegistro, btnInicioSesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        btnRegistro = (findViewById(R.id.btnRegistro));
        btnInicioSesion = (findViewById(R.id.btnInicioSesion));
        //Pantalla para iniciar Sesion

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, MainActivity.class));
            }
        });


        //Pantalla para registrar usuario
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Principal.this, RegistroNuevo.class));
            }
        });
    }




}
