package frontend;

import backend.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private JLabel labelResumen;
    private JButton botonCargar;
    private JButton botonIniciar;
    private List<Item> items;

    public VentanaPrincipal() {
        setTitle("Sistema de Pruebas - Taxonomía de Bloom");
        setSize(600, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        //Título
        JLabel titulo = new JLabel("Cargar archivo de items", SwingConstants.CENTER);
        titulo.setFont(new Font("tahoma", Font.BOLD, 30));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        //Panel central con descripcion y botones
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JLabel instrucciones = new JLabel("Seleccione un archivo con preguntas para comenzar la evaluación");
        instrucciones.setFont(new Font("tahoma", Font.PLAIN, 14));
        instrucciones.setAlignmentX(Component.CENTER_ALIGNMENT);

        botonCargar = new JButton("Cargar archivo de items");
        botonCargar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonCargar.setFont(new Font("tahoma", Font.BOLD, 14));
        botonCargar.addActionListener(e -> cargarArchivo());

        labelResumen = new JLabel("Archivo no cargdo");
        labelResumen.setFont(new Font("tahoma", Font.PLAIN, 12));
        labelResumen.setAlignmentX(Component.CENTER_ALIGNMENT);;

        botonIniciar = new JButton("Iniciar prueba");
        botonIniciar.setEnabled(false);
        botonIniciar.setFont(new Font("tahoma", Font.BOLD, 14));
        botonIniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonIniciar.addActionListener(e -> {
            VentanaPregunta vp = new VentanaPregunta(items);
            vp.setVisible(true);
            this.dispose();
        });

        panelCentral.add(instrucciones);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCentral.add(botonCargar);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCentral.add(labelResumen);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCentral.add(botonIniciar);

        add(titulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    private void cargarArchivo() {
        JFileChooser elegido = new JFileChooser();
        int resultado = elegido.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = elegido.getSelectedFile();
            try {
                items = CargadorDeArchivo.cargarDesdeArchivo(archivo.getAbsolutePath());
                int totalTiempo = items.stream().mapToInt(Item::getTiempoEstimado).sum();
                labelResumen.setText(" Ítems cargados: " + items.size() + " | Tiempo estimado total: " + totalTiempo + " segundos");
                botonIniciar.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
