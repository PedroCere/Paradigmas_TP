package com.example.paradigmas_TP.controller;


import com.example.paradigmas_TP.dao.LibroDAO;
import com.example.paradigmas_TP.model.Libro;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LibroController {
    private LibroDAO libroDAO;

    public LibroController() {
        libroDAO = new LibroDAO();
    }


    public void registrarLibro(Libro libro) {
        libro.setEstado("disponible");  // Inicialmente el libro está disponible
        libroDAO.insertarLibro(libro);
    }


    public List<Libro> obtenerTodosLosLibros() {
        return libroDAO.obtenerTodosLosLibros();
    }


    public List<Libro> obtenerLibrosPorEstado(String estado) {
        return libroDAO.obtenerLibrosPorEstado(estado);
    }

    public void actualizarLibro(Libro libro) {
        libroDAO.actualizarLibro(libro);
    }

    public void eliminarLibro(int idLibro) {
        libroDAO.eliminarLibro(idLibro);
    }


}