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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

public class IniciarSesion2a extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore database;
    private final static String TAG = "Estado";
    TextView txtCorreo, txtContraseña;
    Button btnIniciarSesion;
    ImageView btnRegresar;
    Usuario usuario;

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
            public void onClick(View v) {
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
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(IniciarSesion2a.this, "Sesión Iniciada",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
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
                startActivity(new Intent(IniciarSesion2a.this, Registro2.class));
                finish();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.signOut();
        //updateUI(currentUser);
    }

    //DEPENDERÁ DE LOS ROLES!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //PARA DEPENDER DE LOS ROLES PRIMERO TIENE QUE FUNCIONAR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public void updateUI(FirebaseUser user){
        if (user != null){
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            database = FirebaseFirestore.getInstance();
            DocumentReference docUsuario = database.collection("users").document(uid);
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

            Intent intent = new Intent(IniciarSesion2a.this, ModificarColegio4.class);
            intent.putExtra("idcole", usuario.getIdColegio());
            startActivity(intent);
            finish();
        }
    }
}
