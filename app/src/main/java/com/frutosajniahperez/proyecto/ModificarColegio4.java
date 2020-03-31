package com.frutosajniahperez.proyecto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Set;

public class ModificarColegio4 extends AppCompatActivity implements Dialogo_profe.ResultadoDialogoProfe, Dialogo_aula.ResultadoDialogoAula, Dialogo_eliminar_aula.ResultadoDialogoEliminarAula, Dialogo_eliminar_profe.ResultadoDialogoEliminarProfe, Dialogo_modificar_profe.ResultadoDialogoModificarProfe {

    Colegio cole;
    TextView txtIdColeMod, txtPassColeMod;
    ImageView btnRegresar;
    Button btnAñadirAulaMod, btnAñadirProfeMod, btnEliminarAulaMod, btnEliminarProfeMod, btnBuscarCole, btnModificaProfe;
    FloatingActionButton btnConfirmarCambios;
    Context context;
    ArrayList<String> listadoAulas, listadoProfes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_colegio);

        context = this;
        listadoAulas = new ArrayList<>();
        listadoProfes = new ArrayList<>();

        txtIdColeMod = findViewById(R.id.txtIdColeMod);
        txtPassColeMod = findViewById(R.id.txtPassColeMod);
        btnAñadirAulaMod = findViewById(R.id.btnAñadirAulaMod);
        btnAñadirProfeMod = findViewById(R.id.btnAñadirProfeMod);
        btnEliminarAulaMod = findViewById(R.id.btnEliminarAulaMod);
        btnEliminarProfeMod = findViewById(R.id.btnEliminarProfeMod);
        btnModificaProfe = findViewById(R.id.btnModificaProfe);
        btnBuscarCole = findViewById(R.id.btnBuscarCole);
        btnConfirmarCambios = findViewById(R.id.btnConfimarCambio);
        btnRegresar = findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModificarColegio4.this, PantallaInicio3.class));
            }
        });

        btnBuscarCole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (comprobarID(txtIdColeMod.getText().toString())) {
                    //Crea la conexión con Firebase
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    //Creamos la referencia a un documento con el ID recibido del usuario
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
                                    btnBuscarCole.setEnabled(false);
                                } else {
                                    Toast.makeText(ModificarColegio4.this, "Código secreto incorrecto", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ModificarColegio4.this, "El código tiene que contener 8 caracteres", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ModificarColegio4.this, "No se encuentra el colegio", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(ModificarColegio4.this, "El ID tiene que tener 8 números", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Inicia el dialogo para añadir aula
        btnAñadirAulaMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_aula(context, ModificarColegio4.this);
            }
        });

        //Carga un arrayadapter de los ID de las aulas y se pasa al dialogo para cargar su spinner
        btnAñadirProfeMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_profe(context, ModificarColegio4.this, cargarListados("aulas"));
            }
        });

        //Carga un arrayadapter de los ID de las aulas y se pasa al dialogo para cargar su spinner
        btnEliminarAulaMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_eliminar_aula(context, ModificarColegio4.this, cargarListados("aulas"));
            }
        });

        //Carga un arrayadapter de los ID de las profesores y se pasa al dialogo para cargar su spinner
        btnEliminarProfeMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_eliminar_profe(context, ModificarColegio4.this, cargarListados("profes"));
            }
        });

        btnModificaProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_modificar_profe(context, ModificarColegio4.this, cargarListados("profes"), cargarListados("aulas"));
            }
        });

        btnConfirmarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se obtiene la referencia a la base de datos y se actualiza con los cambios realizados
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                DocumentReference docRef = database.collection("Colegios").document(txtIdColeMod.getText().toString());
                docRef.set(cole).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ModificarColegio4.this, "El colegio se ha modificado con éxito", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ModificarColegio4.this, "Fallo la modificación del colegio", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    //Comprueba si el ID tiene longitud 8 y son todos números
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

    //Crea los listados necesarios para los spinners de los dialogos
    public ArrayAdapter<String> cargarListados(String listado){

        ArrayAdapter<String> adapter = null;

        if (listado.equals("aulas")) {
            Set<String> aulas = cole.getAulas().keySet();
            listadoAulas = new ArrayList<>(aulas);
            adapter = new ArrayAdapter<String>(ModificarColegio4.this, android.R.layout.simple_spinner_item, listadoAulas);


        } else if (listado.equals("profes")){
            Set<String> profes = cole.getProfesorado().keySet();
            listadoProfes = new ArrayList<>(profes);
            adapter = new ArrayAdapter<>(ModificarColegio4.this, android.R.layout.simple_spinner_item, listadoProfes);

        }
        return adapter;
    }

    //Crea un aula con el ID resultado recibido del dialogo
    @Override
    public void ResultadoDialogoAula(String idAula) {

        if (cole.getAulas().containsKey(idAula)){
            Toast.makeText(ModificarColegio4.this, "Aula ya existe", Toast.LENGTH_LONG).show();
        } else{
            Aula aula = new Aula();
            aula.setIdAula(idAula);
            cole.getAulas().put(idAula, aula);
            Toast.makeText(ModificarColegio4.this, "Aula creada", Toast.LENGTH_LONG).show();
        }

    }

    //Crea un profesor con el ID del profesor y del aula recibidos del dialogo
    @Override
    public void ResultadoDialogoProfe(String idProfe, String idAula) {

        if(cole.getProfesorado().containsKey(idProfe)){
            Toast.makeText(ModificarColegio4.this, "Profesor ya existe", Toast.LENGTH_LONG).show();
        }else{
            Profesor profe = new Profesor();
            profe.setIdProfesor(idProfe);
            profe.setAula(cole.getAulas().get(idAula));
            cole.getProfesorado().put(idProfe, profe);
            Toast.makeText(ModificarColegio4.this, "Profesor creado", Toast.LENGTH_LONG).show();
        }


    }

    //Elimina el aula con el ID recibido del dialogo
    @Override
    public void ResultadoDialogoEliminarAula(String idAula) {
        cole.getAulas().remove(idAula);
    }

    //Elimina el profesor con el ID recibido del dialogo
    @Override
    public void ResultadoDialogoEliminarProfe(String idProfe) {
        cole.getProfesorado().remove(idProfe);
    }

    //Modifica el aula del profesor con el ID recibido del dialogo
    @Override
    public void ResultadoDialogoModificarProfe(String idProfe, String idAula) {
        cole.getProfesorado().get(idProfe).setAula(cole.getAulas().get(idAula));
    }
}
