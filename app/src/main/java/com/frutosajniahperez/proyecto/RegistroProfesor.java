package com.frutosajniahperez.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroProfesor extends AppCompatActivity {

    Button btnGenerar, btnAceptarDatos;
    TextView txtPassGenerada;
    EditText txtEmail, txtIdProfeRegistro;
    FirebaseAuth mAuth;
    ImageView btnRegresar;
    Spinner spIdColegios;
    Colegio cole;
    Boolean existe = false;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_profesor);

        btnGenerar = findViewById(R.id.btnGenerar);
        btnRegresar = findViewById(R.id.btnRegresar);
        txtPassGenerada = findViewById(R.id.txtPassGenerada);
        txtEmail = findViewById(R.id.txtEmail);
        txtIdProfeRegistro = findViewById(R.id.txtIdProfeRegistro);
        btnAceptarDatos = findViewById(R.id.btnAceptarDatos);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        spIdColegios = findViewById(R.id.spIdColegios);

        spIdColegios.setAdapter(cargarListados());

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarEmail(txtEmail.getText().toString())){
                    txtPassGenerada.setText(GeneradorContraseña.getPassword());
                    txtEmail.setEnabled(false);
                    btnGenerar.setEnabled(false);
                    btnAceptarDatos.setEnabled(true);
                } else {
                    Toast.makeText(RegistroProfesor.this, "Email incorrecto", Toast.LENGTH_LONG).show();
                    txtEmail.setText("");
                }
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroProfesor.this, Principal.class));
                finish();
            }
        });

        btnAceptarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if (comprobarID(txtIdProfeRegistro.getText().toString())){
                    final DocumentReference docColegio = database.collection("Colegios").document(spIdColegios.getSelectedItem().toString());
                        docColegio.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                cole = documentSnapshot.toObject(Colegio.class);
                                if (cole != null){
                                    existe = true;
                                }
                            }
                        });

                    if (existe){
                        mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassGenerada.getText().toString())
                                .addOnCompleteListener(RegistroProfesor.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Registro del usuario realizado con éxito
                                            Toast.makeText(RegistroProfesor.this, "Usuario creado con éxito.",
                                                    Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Usuario usuario = new Usuario(txtEmail.getText().toString(), user.getUid(), txtPassGenerada.getText().toString(), spIdColegios.getSelectedItem().toString());
                                            usuario.getRoles().setProfe(true);
                                            database.collection("users").document(user.getUid()).set(usuario);
                                            updateUI(user);
                                        } else {
                                            Toast.makeText(RegistroProfesor.this, "Error al registrar el usuario",
                                                    Toast.LENGTH_SHORT).show();
                                            updateUI(null);
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(RegistroProfesor.this, "Fallo en el registro. El colegio no existe", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegistroProfesor.this, "El ID tiene que tener 9 números", Toast.LENGTH_LONG).show();
                    txtIdProfeRegistro.setText("");
                }

            }
        });
    }

    public boolean comprobarEmail(String email){

        // Patrón para validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher(email);

        return mather.find();
    }

    public boolean comprobarID(String codigo){

        if (codigo.length() == 9){
            return TextUtils.isDigitsOnly(codigo);
        } else {
            return false;
        }

    }

    public void updateUI(FirebaseUser user){
        if (user != null){


        }
    }

    public ArrayAdapter<String> cargarListados(){

        ArrayAdapter<String> adapter;
        final ArrayList<String> listadoColegio = new ArrayList<>();

        CollectionReference docColegio = database.collection("Colegios");
        docColegio.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        cole = document.toObject(Colegio.class);
                        listadoColegio.add(cole.getIdColegio());
                    }
                } else {
                        Toast.makeText(RegistroProfesor.this, "Todavía no se ha registrado ningún colegio", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegistroProfesor.this, Principal.class));
                }
            }
        });

        adapter = new ArrayAdapter<>(RegistroProfesor.this, android.R.layout.simple_spinner_item, listadoColegio);
        return adapter;
    }

}
