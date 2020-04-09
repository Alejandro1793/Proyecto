package com.frutosajniahperez.proyecto;

import java.io.Serializable;

public class Roles implements Serializable {

    private boolean admin;
    private boolean profe;
    private boolean alumno;

    public Roles() {
        this.admin = false;
        this.profe = false;
        this.alumno = false;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        if (!this.alumno){
            this.admin = admin;
        }
    }

    public boolean isProfe() {
        return profe;
    }

    public void setProfe(boolean profe) {
        if (!this.alumno){
            this.profe = profe;
        }
    }

    public boolean isAlumno() {
        return alumno;
    }

    public void setAlumno(boolean alumno) {
        if (alumno){
            this.admin = false;
            this.profe = false;
        }
        this.alumno = alumno;
    }
}
