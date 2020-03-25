package com.frutosajniahperez.proyecto;

import java.util.ArrayList;

public class Colegio {

    private String idColegio;
    private ArrayList<Alumno> alumnado;
    private ArrayList<Profesor> profesorado;
    private ArrayList<Aula> aulas;
    private ArrayList<Libro> biblioteca;

    public Colegio() {
    }

    public Colegio(String idColegio, ArrayList<Alumno> alumnado, ArrayList<Profesor> profesorado, ArrayList<Aula> aulas, ArrayList<Libro> biblioteca) {
        this.idColegio = idColegio;
        this.alumnado = alumnado;
        this.profesorado = profesorado;
        this.aulas = aulas;
        this.biblioteca = biblioteca;
    }

    public String getIdColegio() {
        return idColegio;
    }

    public void setIdColegio(String idColegio) {
        this.idColegio = idColegio;
    }

    public ArrayList<Alumno> getAlumnado() {
        return alumnado;
    }

    public void setAlumnado(ArrayList<Alumno> alumnado) {
        this.alumnado = alumnado;
    }

    public ArrayList<Profesor> getProfesorado() {
        return profesorado;
    }

    public void setProfesorado(ArrayList<Profesor> profesorado) {
        this.profesorado = profesorado;
    }

    public ArrayList<Aula> getAulas() {
        return aulas;
    }

    public void setAulas(ArrayList<Aula> aulas) {
        this.aulas = aulas;
    }

    public ArrayList<Libro> getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(ArrayList<Libro> biblioteca) {
        this.biblioteca = biblioteca;
    }
}
