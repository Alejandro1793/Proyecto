package com.frutosajniahperez.proyecto;

import com.google.firebase.auth.FirebaseAuth;

public class CerrarSesion {

    public static void cerrarSesion(FirebaseAuth mAuth){
        mAuth.signOut();
    }
}
