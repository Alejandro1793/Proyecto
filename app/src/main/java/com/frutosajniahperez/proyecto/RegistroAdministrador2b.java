package com.frutosajniahperez.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroAdministrador2b extends AppCompatActivity {

    Button btnGenerar, btnAceptarDatos;
    TextView txtPassGenerada, txtEmail, txtRegistroCole;
    FirebaseAuth mAuth;
    ImageView btnRegresar;
    Colegio cole;
    Boolean existe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_administrador_2b);

        btnGenerar = findViewById(R.id.btnGenerar);
        btnRegresar = findViewById(R.id.btnRegresar);
        txtPassGenerada = findViewById(R.id.txtPassGenerada);
        txtEmail = findViewById(R.id.txtEmail);
        btnAceptarDatos = findViewById(R.id.btnAceptarDatos);
        txtRegistroCole = findViewById(R.id.txtRegistroCole);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();


        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarEmail(txtEmail.getText().toString())){
                    txtPassGenerada.setText(GeneradorContraseña.getPassword());
                    txtEmail.setEnabled(false);
                    btnGenerar.setEnabled(false);
                    btnAceptarDatos.setEnabled(true);
                } else {
                    Toast.makeText(RegistroAdministrador2b.this, "Email incorrecto", Toast.LENGTH_LONG).show();
                    txtEmail.setText("");
                }
            }
        });

        btnAceptarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if (comprobarID(txtRegistroCole.getText().toString())){
                    final DocumentReference docColegio = database.collection("Colegios").document(txtRegistroCole.getText().toString());
                    docColegio.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            cole = documentSnapshot.toObject(Colegio.class);
                            if (cole != null){
                                existe = true;
                            }
                        }
                    });
                    if (!existe){
                        mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassGenerada.getText().toString())
                            .addOnCompleteListener(RegistroAdministrador2b.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registro del usuario realizado con éxito
                                        Toast.makeText(RegistroAdministrador2b.this, "Usuario creado con éxito.",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Usuario usuario = new Usuario(txtEmail.getText().toString(), user.getUid(), txtPassGenerada.getText().toString(), txtRegistroCole.getText().toString());
                                        usuario.getRoles().setAdmin(true);
                                        database.collection("users").document(user.getUid()).set(usuario);
                                        updateUI(user);
                                    } else {
                                        Toast.makeText(RegistroAdministrador2b.this, "Error al registrar el usuario",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                    } else {
                        Toast.makeText(RegistroAdministrador2b.this, "Fallo en el registro. El colegio ya existe", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegistroAdministrador2b.this, "El ID tiene que tener 8 números", Toast.LENGTH_LONG).show();
                    txtRegistroCole.setText("");
                }

            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroAdministrador2b.this, Principal.class));
                finish();
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

    public void updateUI(FirebaseUser user){
        if (user != null){
            Intent intent = new Intent(RegistroAdministrador2b.this, RegistroColegio.class);
            intent.putExtra("idcole", txtRegistroCole.getText().toString());
            startActivity(intent);
            finish();
        }
    }

    public boolean comprobarID(String codigo){

        if (codigo.length() == 8){
            return TextUtils.isDigitsOnly(codigo);
        } else {
            return false;
        }

    }


}
