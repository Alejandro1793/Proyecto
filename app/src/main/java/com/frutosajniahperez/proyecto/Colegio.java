package com.frutosajniahperez.proyecto;

import java.util.HashMap;
import java.util.Hashtable;


public class Colegio {

    private String idColegio;
    private String codigoSecreto;
    private HashMap<String, Alumno> alumnado;
    private HashMap<String, Profesor> profesorado;
    private HashMap<String, Aula> aulas;

    public Colegio() {
        codigoSecreto = GeneradorContraseña.getPassword();
    }

    public Colegio(String idColegio, HashMap<String, Alumno> alumnado, HashMap<String, Profesor> profesorado, HashMap<String, Aula> aulas) {
        this.idColegio = idColegio;
        codigoSecreto = GeneradorContraseña.getPassword();
        this.alumnado = alumnado;
        this.profesorado = profesorado;
        this.aulas = aulas;
    }

    public String getIdColegio() {
        return idColegio;
    }

    public void setIdColegio(String idColegio) {
        this.idColegio = idColegio;
    }

    public HashMap<String, Alumno> getAlumnado() {
        return alumnado;
    }

    public void setAlumnado(HashMap<String, Alumno> alumnado) {
        this.alumnado = alumnado;
    }

    public HashMap<String, Profesor> getProfesorado() {
        return profesorado;
    }

    public void setProfesorado(HashMap<String, Profesor> profesorado) {
        this.profesorado = profesorado;
    }

    public HashMap<String, Aula> getAulas() {
        return aulas;
    }

    public void setAulas(HashMap<String, Aula> aulas) {
        this.aulas = aulas;
    }

    public String getCodigoSecreto() {
        return codigoSecreto;
    }

    public void setCodigoSecreto(String codigoSecreto) {
        this.codigoSecreto = codigoSecreto;
    }
}
