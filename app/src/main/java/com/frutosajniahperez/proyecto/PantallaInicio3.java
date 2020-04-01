package com.frutosajniahperez.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class PantallaInicio3 extends AppCompatActivity {

    Button btnCrearCole, btnEntrarProfe, btnModificaCole;
    Usuario usuario;
    final FirebaseFirestore database = FirebaseFirestore.getInstance();
    final String mAuth = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio_3);

        btnCrearCole = findViewById(R.id.btnCrearColegio);
        btnModificaCole = findViewById(R.id.btnModificarColegio);

        DocumentReference docUsuario = database.collection("users").document(mAuth);
        docUsuario.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario = documentSnapshot.toObject(Usuario.class);
            }
        });

        DocumentReference docRef = database.collection("Colegios").document(usuario.getIdColegio());
        //Se intenta obtener el documento de la base de datos para saber si ya existe
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                btnCrearCole.setEnabled(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnCrearCole.setEnabled(true);
            }
        });

        btnCrearCole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PantallaInicio3.this, RegistroColegio.class));
                finish();
            }
        });

        btnModificaCole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PantallaInicio3.this, ModificarColegio4.class));
                finish();
            }
        });

    }

}
