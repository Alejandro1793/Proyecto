package com.frutosajniahperez.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

public class IniciarSesion2a extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore database;
    private final static String TAG = "Estado";
    EditText txtCorreo, txtContraseña;
    Button btnIniciarSesion;
    ImageView btnRegresar;
    Usuario usuario;
    DocumentReference docUsuario;
    String uid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion_2a);


        txtCorreo = findViewById(R.id.txtCorreo);
        txtContraseña = findViewById(R.id.txtContrasenia);

        btnIniciarSesion = findViewById(R.id.btnInicioSesion);
        btnRegresar = findViewById(R.id.btnRegresar);

        mAuth = FirebaseAuth.getInstance();

        //Iniciar sesion
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String email, password;
                email = txtCorreo.getText().toString();
                password = txtContraseña.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(IniciarSesion2a.this, "El usuario no puede estar vacío", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(IniciarSesion2a.this, "La contraseña no puede estar vacía", Toast.LENGTH_LONG).show();
                    return;
                }



                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(IniciarSesion2a.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(IniciarSesion2a.this, "Sesión Iniciada",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    uid = FirebaseAuth.getInstance().getUid();
                                    database = FirebaseFirestore.getInstance();
                                    docUsuario = database.collection("users").document(uid);
                                    updateUI(user);
                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(IniciarSesion2a.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IniciarSesion2a.this, Principal.class));
                finish();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.signOut();
    }

    //DEPENDERÁ DE LOS ROLES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //PARA DEPENDER DE LOS ROLES PRIMERO TIENE QUE FUNCIONAR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void updateUI(FirebaseUser user){
        if (user != null){

           docUsuario.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    usuario = documentSnapshot.toObject(Usuario.class);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(IniciarSesion2a.this, "Fallo", Toast.LENGTH_LONG).show();
                }
            });


           //Obtenemos el colegio para comprobar que terminó el proceso de registro. Si no es el caso, se envía de nuevo a registro de colegio.
           DocumentReference docColegio = database.collection("Colegios").document(usuario.getIdColegio());
           docColegio.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Intent intent = null;
                        DocumentSnapshot documento = task.getResult();
                        if (documento.exists()) {
                            if (usuario.getRoles().isAdmin()){
                                intent = new Intent(IniciarSesion2a.this, ModificarColegio4.class);
                            } else if (usuario.getRoles().isProfe()){
                                intent = new Intent(IniciarSesion2a.this, ModificarColegio4.class);
                            } else if (usuario.getRoles().isAlumno()){
                                intent = new Intent(IniciarSesion2a.this, ModificarColegio4.class);
                            }
                            intent.putExtra("user", usuario);
                        } else {
                            Toast.makeText(IniciarSesion2a.this, "Aún no has registrado tu colegio", Toast.LENGTH_LONG).show();
                            intent = new Intent(IniciarSesion2a.this, RegistroColegio.class);
                            intent.putExtra("idcole", usuario.getIdColegio());
                        }
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}
