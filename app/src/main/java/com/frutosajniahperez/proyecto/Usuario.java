package com.frutosajniahperez.proyecto;

public class Usuario {

    private String email;
    private String uid;
    private String contraseña;

    public Usuario() {
    }

    public Usuario(String email, String uid, String contraseña) {
        this.email = email;
        this.uid = uid;
        this.contraseña = contraseña;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
