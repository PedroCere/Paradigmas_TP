package com.example.paradigmas_TP.view;

import com.example.paradigmas_TP.controller.LibroController;
import com.example.paradigmas_TP.model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaPrincipal extends JFrame {
    private JTextField txtTitulo, txtAutor, txtGenero, txtAño;
    private JTextField txtBuscar;
    private JComboBox<String> comboCriterio;
    private JButton btnAgregarLibro, btnFiltrarDisponible, btnFiltrarPrestado, btnBuscar;
    private JButton btnEliminarLibro, btnMarcarPrestado, btnMarcarDisponible;
    private JTable tableLibros;
    private DefaultTableModel tableModel;
    private LibroController libroController;

    public VistaPrincipal() {
        libroController = new LibroController();
        setTitle("Sistema de Biblioteca");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
        mostrarLibros("");  // Cargar todos los libros al iniciar la vista
    }

    private void initUI() {
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        txtBuscar = new JTextField(20);
        comboCriterio = new JComboBox<>(new String[]{"titulo", "autor", "genero"});
        btnBuscar = new JButton("Buscar");

        btnBuscar.addActionListener(e -> buscarLibros());

        panelBusqueda.add(new JLabel("Buscar por:"));
        panelBusqueda.add(comboCriterio);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        add(panelBusqueda, BorderLayout.NORTH);

        // Panel para el registro de libros
        JPanel panelRegistro = new JPanel(new GridLayout(0, 2));
        panelRegistro.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        panelRegistro.add(txtTitulo);

        panelRegistro.add(new JLabel("Autor:"));
        txtAutor = new JTextField();
        panelRegistro.add(txtAutor);

        panelRegistro.add(new JLabel("Género:"));
        txtGenero = new JTextField();
        panelRegistro.add(txtGenero);

        panelRegistro.add(new JLabel("Año:"));
        txtAño = new JTextField();
        panelRegistro.add(txtAño);

        btnAgregarLibro = new JButton("Agregar Libro");
        btnAgregarLibro.addActionListener(e -> agregarLibro());
        panelRegistro.add(btnAgregarLibro);

        btnFiltrarDisponible = new JButton("Mostrar Disponibles");
        btnFiltrarDisponible.addActionListener(e -> mostrarLibros("disponible"));
        panelRegistro.add(btnFiltrarDisponible);

        btnFiltrarPrestado = new JButton("Mostrar Prestados");
        btnFiltrarPrestado.addActionListener(e -> mostrarLibros("prestado"));
        panelRegistro.add(btnFiltrarPrestado);

        add(panelRegistro, BorderLayout.WEST);

        // Tabla de libros
        tableModel = new DefaultTableModel(new String[] {"ID", "Título", "Autor", "Género", "Año", "Estado"}, 0);
        tableLibros = new JTable(tableModel);
        add(new JScrollPane(tableLibros), BorderLayout.CENTER);

        // Panel para botones adicionales
        JPanel panelBotones = new JPanel();
        btnEliminarLibro = new JButton("Eliminar Libro");
        btnEliminarLibro.addActionListener(e -> eliminarLibroSeleccionado());
        panelBotones.add(btnEliminarLibro);

        btnMarcarPrestado = new JButton("Marcar como Prestado");
        btnMarcarPrestado.addActionListener(e -> marcarLibroComoPrestado());
        panelBotones.add(btnMarcarPrestado);

        btnMarcarDisponible = new JButton("Marcar como Disponible");
        btnMarcarDisponible.addActionListener(e -> marcarLibroComoDisponible());
        panelBotones.add(btnMarcarDisponible);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarLibro() {
        try {
            // Validar campos obligatorios
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String genero = txtGenero.getText().trim();
            String añoTexto = txtAño.getText().trim();

            if (titulo.isEmpty() || autor.isEmpty() || genero.isEmpty() || añoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar que el año es un número válido
            int año;
            try {
                año = Integer.parseInt(añoTexto);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un año válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear el objeto Libro y registrar
            Libro libro = new Libro();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setGenero(genero);
            libro.setAño(año);
            libroController.registrarLibro(libro);

            // Limpiar campos y actualizar tabla
            txtTitulo.setText("");
            txtAutor.setText("");
            txtGenero.setText("");
            txtAño.setText("");
            mostrarLibros("");  // Mostrar todos los libros después de agregar uno nuevo
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrió un error al agregar el libro.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarLibros(String estado) {
        List<Libro> libros;
        if (estado.isEmpty()) {
            libros = libroController.obtenerTodosLosLibros();
        } else {
            libros = libroController.obtenerLibrosPorEstado(estado);
        }

        tableModel.setRowCount(0);
        for (Libro libro : libros) {
            tableModel.addRow(new Object[] {libro.getId(), libro.getTitulo(), libro.getAutor(), libro.getGenero(), libro.getAño(), libro.getEstado()});
        }
    }

    private void eliminarLibroSeleccionado() {
        int selectedRow = tableLibros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idLibro = (int) tableModel.getValueAt(selectedRow, 0);
        libroController.eliminarLibro(idLibro);
        mostrarLibros("");
    }

    private void marcarLibroComoPrestado() {
        int selectedRow = tableLibros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para marcar como prestado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idLibro = (int) tableModel.getValueAt(selectedRow, 0);
        Libro libro = libroController.obtenerLibroPorId(idLibro);
        libro.setEstado("prestado");
        libroController.actualizarLibro(libro);
        mostrarLibros("");
    }

    private void marcarLibroComoDisponible() {
        int selectedRow = tableLibros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para marcar como disponible.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idLibro = (int) tableModel.getValueAt(selectedRow, 0);
        Libro libro = libroController.obtenerLibroPorId(idLibro);
        libro.setEstado("disponible");
        libroController.actualizarLibro(libro);
        mostrarLibros("");
    }

    private void buscarLibros() {
        String criterioSeleccionado = comboCriterio.getSelectedItem().toString().toLowerCase();
        String valorBusqueda = txtBuscar.getText().trim();

        if (valorBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un valor para buscar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Libro> libros = libroController.buscarLibros(criterioSeleccionado, valorBusqueda);
        tableModel.setRowCount(0);

        for (Libro libro : libros) {
            tableModel.addRow(new Object[] {libro.getId(), libro.getTitulo(), libro.getAutor(), libro.getGenero(), libro.getAño(), libro.getEstado()});
        }
    }
}
