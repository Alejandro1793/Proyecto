package com.frutosajniahperez.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class PantallaInicio extends AppCompatActivity {

    Button btnCrearCole, btnEntrarProfe, btnModificaCole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio_3);

        btnCrearCole = findViewById(R.id.btnCrearColegio);
        btnModificaCole = findViewById(R.id.btnModificarColegio);

        btnCrearCole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PantallaInicio.this, RegistroColegio.class));
            }
        });

        btnModificaCole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PantallaInicio.this, ModificarColegio.class));
            }
        });

    }

}
