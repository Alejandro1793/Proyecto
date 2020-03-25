package com.frutosajniahperez.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroColegio extends AppCompatActivity {

    Button btnGenerarCodigo, btnAceptarCodigo;
    TextView txtCodigoGenerado, txtIdCole, txtPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_colegio);

        btnGenerarCodigo = findViewById(R.id.btnGenerarCodigo);
        txtCodigoGenerado = findViewById(R.id.txtCodigoGenerado);
        txtIdCole = findViewById(R.id.txtIdCole);
        btnAceptarCodigo = findViewById(R.id.btnAceptarCodigo);


        btnGenerarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCodigo(txtIdCole.getText().toString())){
                    txtCodigoGenerado.setText(GeneradorContraseña.getPassword());
                } else {
                    Toast.makeText(RegistroColegio.this, "El ID tiene que tener 8 números", Toast.LENGTH_LONG).show();
                    txtIdCole.setText("");
                }
            }
        });

        btnAceptarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> listado = new HashMap<>();
                Colegio cole = new Colegio();
                cole.setIdColegio(txtIdCole.getText().toString());
                listado.put(txtIdCole.getText().toString(), cole);
                // Write a message to the database
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection("Colegios").document(txtIdCole.getText().toString()).set(cole).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistroColegio.this, "El colegio se ha registrado con éxito", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroColegio.this, "Fallo en el registro del colegio", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public boolean comprobarCodigo(String codigo){

        if (codigo.length() == 8){
            return TextUtils.isDigitsOnly(codigo);
        } else {

            return false;
        }

    }
}
