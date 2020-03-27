package com.frutosajniahperez.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroNuevo extends AppCompatActivity {

    private static final String TAG = " ";
    Button btnGenerar, btnAceptarDatos;
    TextView txtPassGenerada, txtEmail;
    FirebaseAuth mAuth;
    ImageView btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_nuevo_2b);

        btnGenerar = findViewById(R.id.btnGenerar);
        btnRegresar = findViewById(R.id.btnRegresar);
        txtPassGenerada = findViewById(R.id.txtPassGenerada);
        txtEmail = findViewById(R.id.txtEmail);
        btnAceptarDatos = findViewById(R.id.btnAceptarDatos);

        mAuth = FirebaseAuth.getInstance();

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comprobarEmail(txtEmail.getText().toString())){
                    txtPassGenerada.setText(GeneradorContraseña.getPassword());
                    txtEmail.setEnabled(false);
                    btnGenerar.setEnabled(false);
                } else {
                    Toast.makeText(RegistroNuevo.this, "Email incorrecto", Toast.LENGTH_LONG).show();
                    txtEmail.setText("");
                }
            }
        });

        btnAceptarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassGenerada.getText().toString())
                        .addOnCompleteListener(RegistroNuevo.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(RegistroNuevo.this, "Usuario creado con éxito.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegistroNuevo.this, "Authentication failed.",
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
                startActivity(new Intent(RegistroNuevo.this, Principal.class));
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
            Intent intent = new Intent(RegistroNuevo.this, PantallaInicio.class);
            startActivity(intent);
            finish();
        }
    }


}
