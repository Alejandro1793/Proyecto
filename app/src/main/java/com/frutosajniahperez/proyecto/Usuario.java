package com.frutosajniahperez.proyecto;

import java.io.Serializable;
import java.util.HashMap;

public class Usuario implements Serializable {

    private String email;
    private String uid;
    private String contraseña;
    private String idColegio;
    private Roles roles;

    public Usuario() {
    }

    public Usuario(String email, String uid, String contraseña, String idColegio) {
        this.email = email;
        this.uid = uid;
        this.contraseña = contraseña;
        this.idColegio = idColegio;
        this.roles = new Roles();
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

    public String getIdColegio() {
        return idColegio;
    }

    public void setIdColegio(String idColegio) {
        this.idColegio = idColegio;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }
}
