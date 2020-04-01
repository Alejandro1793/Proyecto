package com.frutosajniahperez.proyecto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class RegistroColegio extends AppCompatActivity implements Dialogo_aula.ResultadoDialogoAula, Dialogo_profe.ResultadoDialogoProfe {

    private static final String TAG = "Error";
    Button  btnAceptarCole, btnRegistroAula, btnRegistroProfe, btnAtras;
    TextView txtCodigoGenerado;
    HashMap<String, Aula> aulas;
    ArrayList<String> listadoAulas;
    HashMap<String, Profesor> profesorado;
    Context context;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_colegio);

        txtCodigoGenerado = findViewById(R.id.txtCodigoGenerado);
        txtCodigoGenerado.setText(GeneradorContraseña.getPassword());
        btnAceptarCole = findViewById(R.id.btnAceptarCole);
        btnRegistroAula = findViewById(R.id.btnRegitroAula);
        btnRegistroProfe = findViewById(R.id.btnRegistroProfe);
        btnAtras = findViewById(R.id.btnAtras);
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        final String mAuth = FirebaseAuth.getInstance().getCurrentUser().getUid();

        aulas = new HashMap<>();
        profesorado = new HashMap<>();
        context = this;

        DocumentReference docUsuario = database.collection("users").document(mAuth);
        docUsuario.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario = documentSnapshot.toObject(Usuario.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroColegio.this, "Fallo en la autentificación del usuario", Toast.LENGTH_LONG).show();
            }
        });



        btnRegistroAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_aula(context, RegistroColegio.this);
            }
        });

        btnRegistroProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se obtienen los ID de las aulas y se convierten en un ArrayAdapter que se pasan al dialogo
                Set<String> idAulas = aulas.keySet();
                listadoAulas = new ArrayList<>(idAulas);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistroColegio.this, android.R.layout.simple_spinner_item, listadoAulas);
                new Dialogo_profe(context, RegistroColegio.this, adapter);
            }
        });

        btnAceptarCole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtiene la referencia del documento con el ID que ha ingresado el usuario
                CollectionReference docColegio = database.collection("users").document(mAuth).collection("Colegio");
                DocumentReference docAulas = docColegio.document("Aulas");
                DocumentReference docProfes = docColegio.document("Profesores");
                //Se carga el colegio creado en la base de datos
                docAulas.set(aulas).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistroColegio.this, "Las aulas se han registrado con éxito", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroColegio.this, "Fallo en el registro de las aulas", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Error adding document", e);
                    }
                });

                docProfes.set(profesorado).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistroColegio.this, "Los profesores se han registrado con éxito", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroColegio.this, "Fallo en el registro de los profesores", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Error adding document", e);
                    }
                });

            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroColegio.this, PantallaInicio3.class));
            }
        });


    }

    public boolean comprobarID(String codigo){

        if (codigo.length() == 8){
            return TextUtils.isDigitsOnly(codigo);
        } else {
            return false;
        }

    }

    //Comprueba si el aula ya existe y si no, se introduce en la colección
    @Override
    public void ResultadoDialogoAula(String idAula) {

        if (aulas.containsKey(idAula)){
            Toast.makeText(RegistroColegio.this, "Este aula ya existe", Toast.LENGTH_LONG).show();
        } else {
            Aula aula = new Aula();
            aula.setIdAula(idAula);
            aulas.put(idAula, aula);
            Toast.makeText(RegistroColegio.this, "Aula creada", Toast.LENGTH_LONG).show();

            if (aulas.size() == 1) {
                btnAceptarCole.setEnabled(true);
            }

            btnRegistroProfe.setEnabled(true);
        }

    }

    //Comprueba si el profesor ya existe y si no, se introduce en la colección
    @Override
    public void ResultadoDialogoProfe(String idProfe, String idAula) {

        if (profesorado.containsKey(idProfe)){
            Toast.makeText(RegistroColegio.this, "Este profesor ya existe", Toast.LENGTH_LONG).show();
        } else {
            Profesor profe = new Profesor();
            profe.setIdProfesor(idProfe);
            profe.setAula(aulas.get(idAula));
            profesorado.put(idProfe, profe);
            Toast.makeText(RegistroColegio.this, "Profesor creado", Toast.LENGTH_LONG).show();

            if (aulas.size() <= profesorado.size()) {
                btnRegistroProfe.setEnabled(false);
            }
        }
    }
}
