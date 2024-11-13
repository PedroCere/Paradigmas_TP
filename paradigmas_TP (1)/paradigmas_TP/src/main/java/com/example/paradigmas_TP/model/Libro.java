package com.example.paradigmas_TP.model;


public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String genero;
    private int año;
    private String estado;

    // Constructor que inicializa todos los campos excepto el ID
    public Libro(String titulo, String autor, String genero, int año, String estado) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.año = año;
        this.estado = estado;
    }

    // Constructor vacío por si necesitas una instancia vacía
    public Libro() {}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}