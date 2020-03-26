package com.frutosajniahperez.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RegistroColegio extends AppCompatActivity implements Dialogo_aula.ResultadoDialogoAula, Dialogo_profe.ResultadoDialogoProfe {

    Button btnGenerarCodigo, btnAceptarCodigo, btnRegistroAula, btnRegistroProfe;
    TextView txtCodigoGenerado, txtIdCole;
    Colegio cole;
    ArrayList<Aula> aulas;
    ArrayList<String> listadoAulas;
    ArrayList<Profesor> profesorado;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_colegio);

        btnGenerarCodigo = findViewById(R.id.btnGenerarCodigo);
        txtCodigoGenerado = findViewById(R.id.txtCodigoGenerado);
        txtIdCole = findViewById(R.id.txtIdCole);
        btnAceptarCodigo = findViewById(R.id.btnAceptarCodigo);
        btnRegistroAula = findViewById(R.id.btnRegitroAula);
        btnRegistroProfe = findViewById(R.id.btnRegistroProfe);

        cole = new Colegio();
        aulas = new ArrayList<Aula>();
        profesorado = new ArrayList<Profesor>();
        listadoAulas = new ArrayList<String>();
        context = this;

        cole.setAulas(aulas);
        cole.setProfesorado(profesorado);

        btnGenerarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarCodigo(txtIdCole.getText().toString())){
                    txtCodigoGenerado.setText(cole.getCodigoSecreto());
                    btnRegistroAula.setEnabled(true);
                    txtIdCole.setEnabled(false);
                    btnGenerarCodigo.setEnabled(false);
                } else {
                    Toast.makeText(RegistroColegio.this, "El ID tiene que tener 8 números", Toast.LENGTH_LONG).show();
                    txtIdCole.setText("");
                }
            }
        });

        btnAceptarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cole.setIdColegio(txtIdCole.getText().toString());
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

        btnRegistroAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_aula(context, RegistroColegio.this);
            }
        });

        btnRegistroProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Aula aula : aulas) {
                    listadoAulas.add(aula.getIdAula());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistroColegio.this, android.R.layout.simple_spinner_item, listadoAulas);

                new Dialogo_profe(context, RegistroColegio.this, adapter);
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

    @Override
    public void ResultadoDialogoAula(String idAula) {
        Aula aula = new Aula();
        aula.setIdAula(idAula);
        aulas.add(aula);
        Toast.makeText(RegistroColegio.this, "Aula creada", Toast.LENGTH_LONG).show();
        if (aulas.size() == 1){
            btnAceptarCodigo.setEnabled(true);
        }
        if (aulas.size() > profesorado.size()){
            btnRegistroProfe.setEnabled(true);
        }

    }

    @Override
    public void ResultadoDialogoProfe(String idProfe, String idAula) {
        Profesor profe = new Profesor();
        profe.setIdProfesor(idProfe);
        for (int i = 0; i < aulas.size(); i++){
            if (aulas.get(i).getIdAula().equals(idAula)){
                profe.setAula(aulas.get(i));
                break;
            }
        }
        profesorado.add(profe);
    }
}
