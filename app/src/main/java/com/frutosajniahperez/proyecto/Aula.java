package com.frutosajniahperez.proyecto;

import java.util.Dictionary;

public class Aula {

    private String idAula;
    private Dictionary<String, Libro> libreria;
    private Dictionary<String, Alumno> listadoAlumnos;

    public Aula() {}

    public Aula(String idAula, Dictionary<String, Libro> libreria, Dictionary<String, Alumno> listadoAlumnos) {
        this.idAula = idAula;
        this.libreria = libreria;
        this.listadoAlumnos = listadoAlumnos;
    }

    public String getIdAula() {
        return idAula;
    }

    public void setIdAula(String idAula) {
        this.idAula = idAula;
    }

    public Dictionary<String, Libro> getLibreria() {
        return libreria;
    }

    public void setLibreria(Dictionary<String, Libro> libreria) {
        this.libreria = libreria;
    }

    public Dictionary<String, Alumno> getListadoAlumnos() {
        return listadoAlumnos;
    }

    public void setListadoAlumnos(Dictionary<String, Alumno> listadoAlumnos) {
        this.listadoAlumnos = listadoAlumnos;
    }
}
