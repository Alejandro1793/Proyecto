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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ModificarColegio extends AppCompatActivity implements Dialogo_profe.ResultadoDialogoProfe, Dialogo_aula.ResultadoDialogoAula, Dialogo_eliminar_aula.ResultadoDialogoEliminarAula {

    Colegio cole;
    TextView txtIdColeMod, txtPassColeMod;
    Button btnAñadirAulaMod, btnAñadirProfeMod, btnEliminarAulaMod, btnEliminarProfeMod, btnBuscarCole, btnModificaProfe;
    Context context;
    ArrayList<String> listadoAulas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_colegio);

        context = this;
        listadoAulas = new ArrayList<>();

        txtIdColeMod = findViewById(R.id.txtIdColeMod);
        txtPassColeMod = findViewById(R.id.txtPassColeMod);
        btnAñadirAulaMod = findViewById(R.id.btnAñadirAulaMod);
        btnAñadirProfeMod = findViewById(R.id.btnAñadirProfeMod);
        btnEliminarAulaMod = findViewById(R.id.btnEliminarAulaMod);
        btnEliminarProfeMod = findViewById(R.id.btnEliminarProfeMod);
        btnModificaProfe = findViewById(R.id.btnModificaProfe);
        btnBuscarCole = findViewById(R.id.btnBuscarCole);


        btnBuscarCole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarID(txtIdColeMod.getText().toString())) {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("Colegios").document(txtIdColeMod.getText().toString());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            cole = documentSnapshot.toObject(Colegio.class);
                            if (comprobarCodigo(cole.getCodigoSecreto())) {
                                if (cole.getCodigoSecreto().equals(txtPassColeMod.getText().toString())) {
                                    btnAñadirAulaMod.setEnabled(true);
                                    btnAñadirProfeMod.setEnabled(true);
                                    btnEliminarAulaMod.setEnabled(true);
                                    btnEliminarProfeMod.setEnabled(true);
                                    btnModificaProfe.setEnabled(true);
                                    txtIdColeMod.setEnabled(false);
                                    txtPassColeMod.setEnabled(false);
                                } else {
                                    Toast.makeText(ModificarColegio.this, "Código secreto incorrecto", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ModificarColegio.this, "El código tiene que contener 8 caracteres", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ModificarColegio.this, "No se encuentra el colegio", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(ModificarColegio.this, "El ID tiene que tener 8 números", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnAñadirAulaMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_aula(context, ModificarColegio.this);
            }
        });

        btnAñadirProfeMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Aula aula : cole.getAulas()) {
                    listadoAulas.add(aula.getIdAula());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModificarColegio.this, android.R.layout.simple_spinner_item, listadoAulas);

                new Dialogo_profe(context, ModificarColegio.this, adapter);
            }
        });

        btnEliminarAulaMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Aula aula : cole.getAulas()) {
                    listadoAulas.add(aula.getIdAula());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModificarColegio.this, android.R.layout.simple_spinner_item, listadoAulas);

                new Dialogo_eliminar_aula(context, ModificarColegio.this, adapter);
            }
        });

    }

    public boolean comprobarID(String id){

        if (id.length() == 8){
            return TextUtils.isDigitsOnly(id);
        } else {
            return false;
        }
    }

    public boolean comprobarCodigo(String codigo){

        return (codigo.length() == 8);
    }


    @Override
    public void ResultadoDialogoAula(String idAula) {
        Aula aula = new Aula();
        aula.setIdAula(idAula);
        cole.getAulas().add(aula);
        Toast.makeText(ModificarColegio.this, "Aula creada", Toast.LENGTH_LONG).show();
    }

    @Override
    public void ResultadoDialogoProfe(String idProfe, String idAula) {

        Profesor profe = new Profesor();
        profe.setIdProfesor(idProfe);
        for (int i = 0; i < cole.getAulas().size(); i++){
            if (cole.getAulas().get(i).getIdAula().equals(idAula)){
                profe.setAula(cole.getAulas().get(i));
                break;
            }
        }
        cole.getProfesorado().add(profe);
        Toast.makeText(ModificarColegio.this, "Profesor creado", Toast.LENGTH_LONG).show();

    }

    @Override
    public void ResultadoDialogoEliminarAula(String idAula) {

        for (int i = 0; i < cole.getAulas().size(); i++){
            if (cole.getAulas().get(i).getIdAula().equals(idAula)){
                cole.getAulas().remove(i);
                break;
            }
        }
    }
}
