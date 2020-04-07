package com.frutosajniahperez.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Registro2 extends AppCompatActivity {

    Button btnRegistroProfe, btnRegistroAdmin;
    ImageView btregresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);

        btnRegistroAdmin = findViewById(R.id.btnISAdmin);
        btnRegistroProfe = findViewById(R.id.btnISProfe);
        btregresar = findViewById(R.id.btnRegresar);

        btnRegistroProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro2.this, InicioSesionExitoso.class));
            }
        });

        btnRegistroAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro2.this, RegistroAdministrador2b.class));
            }
        });

        btregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro2.this, Principal.class));
            }
        });

    }
}
