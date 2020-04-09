package com.frutosajniahperez.proyecto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ModificarColegio4 extends AppCompatActivity implements Dialogo_profe.ResultadoDialogoProfe, Dialogo_aula.ResultadoDialogoAula, Dialogo_eliminar_aula.ResultadoDialogoEliminarAula, Dialogo_eliminar_profe.ResultadoDialogoEliminarProfe, Dialogo_modificar_profe.ResultadoDialogoModificarProfe {

    TextView txtIdColeMod, lbEliminarProfe,lbEliminarAula,lbAnadirAula,lbAnadirProfe,lbModificarProfe;
    FloatingActionButton fab_eliminarProfe, fab_eliminarAula, fab_modificarProfe, fab_añadirAula,  fab_añadirProfe, fab_opciones,btnCerrarSesion;
    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    Context context;
    Boolean exito = true;
    HashMap<String, Aula> aulas;
    HashMap<String, Profesor> profesorado;
    ArrayList<String> listadoAulas, listadoProfes;
    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseFirestore database = FirebaseFirestore.getInstance();

    boolean menuAbierto =  false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_colegio);

        context = this;
        listadoAulas = new ArrayList<>();
        listadoProfes = new ArrayList<>();
        final String idCole = getIntent().getStringExtra("idcole");
        final Colegio cole = (Colegio) getIntent().getSerializableExtra("colegio");


        txtIdColeMod = findViewById(R.id.txtIdColeMod);
        fab_añadirAula = findViewById(R.id.fab_añadirAula);
        fab_añadirProfe = findViewById(R.id.fab_añadirProfe);
        fab_eliminarAula = findViewById(R.id.fab_eliminarAula);
        fab_eliminarProfe = findViewById(R.id.fab_EliminarProfe);
        fab_modificarProfe = findViewById(R.id.fab_ModificarProfe);
        fab_opciones = findViewById(R.id.fab_opciones);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        lbEliminarProfe = findViewById((R.id.lbEliminarProfe));
        lbEliminarAula = findViewById(R.id.lbEliminarAula);
        lbAnadirProfe = findViewById(R.id.lbAnadirProfe);
        lbAnadirAula = findViewById(R.id.lbAnadirAula);
        lbModificarProfe = findViewById(R.id.lbModificarProfe);


        //El setAlpha y el setTranslation es para la animación del boton flotante
        fab_añadirAula.setAlpha(0f);
        fab_añadirProfe.setAlpha(0f);
        fab_eliminarAula.setAlpha(0f);
        fab_eliminarProfe.setAlpha(0f);
        fab_modificarProfe.setAlpha(0f);
        lbEliminarProfe.setAlpha(0f);
        lbEliminarAula.setAlpha(0f);
        lbAnadirProfe.setAlpha(0f);
        lbAnadirAula.setAlpha(0f);
        lbModificarProfe.setAlpha(0f);

        fab_añadirAula.setTranslationY(translationY);
        fab_añadirProfe.setTranslationY(translationY);
        fab_eliminarAula.setTranslationY(translationY);
        fab_eliminarProfe.setTranslationY(translationY);
        fab_modificarProfe.setTranslationY(translationY);
        lbEliminarProfe.setTranslationY(translationY);
        lbEliminarAula.setTranslationY(translationY);
        lbAnadirProfe.setTranslationY(translationY);
        lbAnadirAula.setTranslationY(translationY);
        lbModificarProfe.setTranslationY(translationY);


        txtIdColeMod.setText(idCole);

        //
        //NOS FALTA CARGAR EL COLEGIO DESDE LA BASE DE DATOS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cole.setIdColegio(idCole);
                cole.setAulas(aulas);
                cole.setProfesorado(profesorado);

                final DocumentReference docColegio = database.collection("Colegios").document(idCole);
                docColegio.set(cole).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ModificarColegio4.this, "Colegio modificado correctamente", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ModificarColegio4.this, "Fallo en la modificación del colegio", Toast.LENGTH_LONG).show();
                        exito = false;
                    }
                });

                if (exito) {
                    CerrarSesion.cerrarSesion(mAuth);
                    startActivity(new Intent(ModificarColegio4.this, Principal.class));
                }
            }
        });



        //Inicia el dialogo para añadir aula
        fab_añadirAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_aula(context, ModificarColegio4.this);
            }
        });

        //Carga un arrayadapter de los ID de las aulas y se pasa al dialogo para cargar su spinner
        fab_añadirProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_profe(context, ModificarColegio4.this, cargarListados("aulas"));
            }
        });

        //Carga un arrayadapter de los ID de las aulas y se pasa al dialogo para cargar su spinner
        fab_eliminarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_eliminar_aula(context, ModificarColegio4.this, cargarListados("aulas"));
            }
        });

        //Carga un arrayadapter de los ID de las profesores y se pasa al dialogo para cargar su spinner
        fab_eliminarProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_eliminar_profe(context, ModificarColegio4.this, cargarListados("profes"));
            }
        });

        fab_modificarProfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialogo_modificar_profe(context, ModificarColegio4.this, cargarListados("profes"), cargarListados("aulas"));
            }
        });

        //Esto es para que cuando se pulse el boton flotante muestre o no el menu desplegado
        fab_opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.fab_opciones:
                        if(menuAbierto){
                            cierraMenu();
                        }else{
                            abreMenu();
                        }
                }
            }
        });

    }

    public void abreMenu(){
        menuAbierto=!menuAbierto;

        fab_opciones.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        fab_añadirAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_añadirProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_modificarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

        lbEliminarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbEliminarAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirAula.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        lbModificarProfe.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

    }
    public void cierraMenu(){
        menuAbierto=!menuAbierto;

        fab_opciones.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fab_añadirAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_añadirProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_eliminarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fab_modificarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();

        lbEliminarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbEliminarAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbAnadirAula.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        lbModificarProfe.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    //Crea los listados necesarios para los spinners de los dialogos
    public ArrayAdapter<String> cargarListados(String listado){

        ArrayAdapter<String> adapter = null;

        if (listado.equals("aulas")) {
            Set<String> setAulas = aulas.keySet();
            listadoAulas = new ArrayList<>(setAulas);
            adapter = new ArrayAdapter<String>(ModificarColegio4.this, android.R.layout.simple_spinner_item, listadoAulas);


        } else if (listado.equals("profes")){
            Set<String> profes = profesorado.keySet();
            listadoProfes = new ArrayList<>(profes);
            adapter = new ArrayAdapter<>(ModificarColegio4.this, android.R.layout.simple_spinner_item, listadoProfes);

        }
        return adapter;
    }

    //Crea un aula con el ID resultado recibido del dialogo
    @Override
    public void ResultadoDialogoAula(String idAula) {

        if (aulas.containsKey(idAula)){
            Toast.makeText(ModificarColegio4.this, "Aula ya existe", Toast.LENGTH_LONG).show();
        } else{
            Aula aula = new Aula();
            aula.setIdAula(idAula);
            aulas.put(idAula, aula);
            Toast.makeText(ModificarColegio4.this, "Aula creada", Toast.LENGTH_LONG).show();
        }

    }

    //Crea un profesor con el ID del profesor y del aula recibidos del dialogo
    @Override
    public void ResultadoDialogoProfe(String idProfe, String idAula) {

        if(profesorado.containsKey(idProfe)){
            Toast.makeText(ModificarColegio4.this, "Profesor ya existe", Toast.LENGTH_LONG).show();
        }else{
            Profesor profe = new Profesor();
            profe.setIdProfesor(idProfe);
            profe.setAula(aulas.get(idAula));
            profesorado.put(idProfe, profe);
            Toast.makeText(ModificarColegio4.this, "Profesor creado", Toast.LENGTH_LONG).show();
        }


    }

    //Elimina el aula con el ID recibido del dialogo
    @Override
    public void ResultadoDialogoEliminarAula(String idAula) {
        aulas.remove(idAula);
    }

    //Elimina el profesor con el ID recibido del dialogo
    @Override
    public void ResultadoDialogoEliminarProfe(String idProfe) {
        profesorado.remove(idProfe);
    }

    //Modifica el aula del profesor con el ID recibido del dialogo
    @Override
    public void ResultadoDialogoModificarProfe(String idProfe, String idAula) {
        profesorado.get(idProfe).setAula(aulas.get(idAula));
    }
}
