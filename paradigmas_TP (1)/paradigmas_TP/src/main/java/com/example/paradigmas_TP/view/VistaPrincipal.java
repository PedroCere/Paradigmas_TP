package com.example.paradigmas_TP.view;

import com.example.paradigmas_TP.controller.LibroController;
import com.example.paradigmas_TP.model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaPrincipal extends JFrame {
    private JTextField txtTitulo, txtAutor, txtGenero, txtAño;
    private JButton btnAgregarLibro, btnFiltrarDisponible, btnFiltrarPrestado;
    private JButton btnEliminarLibro, btnMarcarPrestado;
    private JTable tableLibros;
    private LibroController libroController;
    private DefaultTableModel tableModel;

    ImageIcon img = new ImageIcon("C:\\Repo-remoto5\\Paradigmas_TP\\paradigmas_TP (1)\\paradigmas_TP\\src\\main\\java\\com\\example\\paradigmas_TP\\view\\library.png");

    public VistaPrincipal() {
        libroController = new LibroController();
        setTitle("Biblioteca");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(img.getImage());



        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        panel.add(txtTitulo);

        panel.add(new JLabel("Autor:"));
        txtAutor = new JTextField();
        panel.add(txtAutor);

        panel.add(new JLabel("Género:"));
        txtGenero = new JTextField();
        panel.add(txtGenero);

        panel.add(new JLabel("Año:"));
        txtAño = new JTextField();
        panel.add(txtAño);

        btnAgregarLibro = new JButton("Agregar Libro");
        btnAgregarLibro.addActionListener(e -> agregarLibro());
        panel.add(btnAgregarLibro);

        btnFiltrarDisponible = new JButton("Mostrar Disponibles");
        btnFiltrarDisponible.addActionListener(e -> mostrarLibros("disponible"));
        panel.add(btnFiltrarDisponible);

        btnFiltrarPrestado = new JButton("Mostrar Prestados");
        btnFiltrarPrestado.addActionListener(e -> mostrarLibros("prestado"));
        panel.add(btnFiltrarPrestado);

        add(panel, BorderLayout.NORTH);

        // Configuración de la tabla de libros
        tableModel = new DefaultTableModel(new String[]{"ID", "Título", "Autor", "Género", "Año", "Estado"}, 0);
        tableLibros = new JTable(tableModel);
        add(new JScrollPane(tableLibros), BorderLayout.CENTER);

        // Panel inferior para botones adicionales
        JPanel panelBotones = new JPanel();
        btnEliminarLibro = new JButton("Eliminar Libro");
        btnEliminarLibro.addActionListener(e -> eliminarLibro());
        btnMarcarPrestado = new JButton("Marcar como Prestado");
        btnMarcarPrestado.addActionListener(e -> marcarLibroComoPrestado());

        panelBotones.add(btnEliminarLibro);
        panelBotones.add(btnMarcarPrestado);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
        mostrarLibros(""); // Mostrar todos los libros al iniciar
    }

    private void agregarLibro() {
        try {
            Libro libro = new Libro();
            libro.setTitulo(txtTitulo.getText());
            libro.setAutor(txtAutor.getText());
            libro.setGenero(txtGenero.getText());
            libro.setAño(Integer.parseInt(txtAño.getText()));
            libroController.registrarLibro(libro);

            // Limpiar campos y actualizar la tabla
            txtTitulo.setText("");
            txtAutor.setText("");
            txtGenero.setText("");
            txtAño.setText("");
            mostrarLibros(""); // Refrescar lista de libros
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un año válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarLibros(String estado) {
        List<Libro> libros = estado.isEmpty() ? libroController.obtenerTodosLosLibros() : libroController.obtenerLibrosPorEstado(estado);

        // Limpiar el modelo de la tabla
        tableModel.setRowCount(0);

        // Llenar el modelo de la tabla con los datos de los libros
        for (Libro libro : libros) {
            Object[] rowData = {
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getGenero(),
                    libro.getAño(),
                    libro.getEstado()
            };
            tableModel.addRow(rowData);
        }
    }

    private void eliminarLibro() {
        int filaSeleccionada = tableLibros.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idLibro = (int) tableModel.getValueAt(filaSeleccionada, 0);
            libroController.eliminarLibro(idLibro);
            mostrarLibros(""); // Refrescar lista de libros
            JOptionPane.showMessageDialog(this, "Libro eliminado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar.");
        }
    }

    private void marcarLibroComoPrestado() {
        int filaSeleccionada = tableLibros.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idLibro = (int) tableModel.getValueAt(filaSeleccionada, 0);
            Libro libro = libroController.obtenerTodosLosLibros().stream().filter(l -> l.getId() == idLibro).findFirst().orElse(null);
            if (libro != null) {
                libro.setEstado("prestado");
                libroController.actualizarLibro(libro);
                mostrarLibros(""); // Refrescar lista de libros
                JOptionPane.showMessageDialog(this, "Libro marcado como prestado.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para marcar como prestado.");
        }
    }




}
