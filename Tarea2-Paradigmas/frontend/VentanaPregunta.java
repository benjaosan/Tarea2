package frontend;

import backend.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class VentanaPregunta extends JFrame {

    private List<Item> items;
    private int actual = 0;
    private JLabel labelPregunta;
    private JLabel labelNivel;
    private JLabel labelProgreso;
    private JRadioButton[] opciones;
    private ButtonGroup grupo;
    private String[] respuestasUsuario;

    private JButton botonAnterior;
    private JButton botonSiguiente;
    private boolean respuestasFinalizadas = false;

    public VentanaPregunta(List<Item> items) {
        this.items = items;
        this.respuestasUsuario = new String[items.size()];

        setTitle("Evaluación - Taxonomía de Bloom");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
        labelProgreso = new JLabel("", SwingConstants.CENTER);
        labelNivel = new JLabel("", SwingConstants.CENTER);
        labelProgreso.setFont(new Font("tahoma", Font.BOLD, 16));
        labelNivel.setFont(new Font("tahoma", Font.PLAIN, 14));
        panelSuperior.add(labelProgreso);
        panelSuperior.add(labelNivel);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        labelPregunta = new JLabel();
        labelPregunta.setFont(new Font("tahoma", Font.PLAIN, 14));
        labelPregunta.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelPregunta.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        grupo = new ButtonGroup();
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setAlignmentX(Component.LEFT_ALIGNMENT);

        opciones = new JRadioButton[5];
        for (int i = 0; i < 5; i++) {
            opciones[i] = new JRadioButton();
            opciones[i].setFont(new Font("tahoma", Font.PLAIN, 13));
            opciones[i].setBorder(BorderFactory.createEmptyBorder(0, 10, 30, 0));
            grupo.add(opciones[i]);
            panelOpciones.add(opciones[i]);
        }

        panelCentral.add(labelPregunta);
        panelCentral.add(panelOpciones);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botonAnterior = new JButton("Anterior");
        botonSiguiente = new JButton("Siguiente");

        botonAnterior.addActionListener(e -> irAAnterior());
        botonSiguiente.addActionListener(e -> irASiguiente());

        panelInferior.add(botonAnterior);
        panelInferior.add(botonSiguiente);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        mostrarPregunta();
    }

    private void mostrarPregunta() {
        grupo.clearSelection();
        Item item = items.get(actual);

        labelPregunta.setText("Pregunta " + (actual + 1) + ": " + item.getPregunta());
        labelNivel.setText("Nivel de Bloom: " + item.getNivel());
        labelProgreso.setText("Pregunta " + (actual + 1) + " de " + items.size());

        String[] ops = item.getOpciones();
        for (int i = 0; i < 5; i++) {
            if (i < ops.length) {
                opciones[i].setText(ops[i]);
                opciones[i].setVisible(true);
                opciones[i].setEnabled(!respuestasFinalizadas);
                opciones[i].setSelected(ops[i].equals(respuestasUsuario[actual]));
            } else {
                opciones[i].setVisible(false);
            }
        }

        botonAnterior.setEnabled(actual > 0 && !respuestasFinalizadas);
        botonSiguiente.setText(actual == items.size() - 1 ? "Enviar respuestas" : "Siguiente");
        botonSiguiente.setEnabled(true);
    }

    private void guardarRespuesta() {
        if (respuestasFinalizadas) return;
        for (JRadioButton opcion : opciones) {
            if (opcion.isVisible() && opcion.isSelected()) {
                respuestasUsuario[actual] = opcion.getText();
                break;
            }
        }
    }

    private void irAAnterior() {
        guardarRespuesta();
        if (actual > 0) {
            actual--;
            mostrarPregunta();
        }
    }

    private void irASiguiente() {
        guardarRespuesta();
        if (actual < items.size() - 1) {
            actual++;
            mostrarPregunta();
        } else {
            respuestasFinalizadas = true; // Lo cambia a true en caso de que sea la ultima pregunta
            mostrarResumen();
            mostrarPregunta(); // Refresca la pregunta y la muestra nuevamente pero con los controles descativados
        }
    }

    private void mostrarResumen() {
        Map<String, Integer> totalPorTaxonomia = new HashMap<>();
        Map<String, Integer> correctasPorTaxonomia = new HashMap<>();
        Map<Item.Tipo, Integer> totalPorTipo = new HashMap<>();
        Map<Item.Tipo, Integer> correctasPorTipo = new HashMap<>();

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            String tax = item.getNivel();
            Item.Tipo tipo = item.getTipo();
            String respuesta = item.getRespuestaCorrecta();

            totalPorTaxonomia.put(tax, totalPorTaxonomia.getOrDefault(tax, 0) + 1);
            totalPorTipo.put(tipo, totalPorTipo.getOrDefault(tipo, 0) + 1);

            if (respuesta.equalsIgnoreCase(respuestasUsuario[i])) {
                correctasPorTaxonomia.put(tax, correctasPorTaxonomia.getOrDefault(tax, 0) + 1);
                correctasPorTipo.put(tipo, correctasPorTipo.getOrDefault(tipo, 0) + 1);
            }
        }

        StringBuilder resumen = new StringBuilder();
        resumen.append("\n--- RESULTADOS POR TAXONOMIA ---\n");
        for (String tax : totalPorTaxonomia.keySet()) {
            int total = totalPorTaxonomia.get(tax);
            int correctas = correctasPorTaxonomia.getOrDefault(tax, 0);
            double porcentaje = (double) correctas / total * 100;
            resumen.append(tax).append(": ").append(String.format("%.2f", porcentaje)).append("%\n");
        }

        resumen.append("\n---RESULTADOS POR TIPO DE ÍTEM ---\n");
        for (Item.Tipo tipo : totalPorTipo.keySet()) {
            int total = totalPorTipo.get(tipo);
            int correctas = correctasPorTipo.getOrDefault(tipo, 0);
            double porcentaje = (double) correctas / total * 100;
            resumen.append(tipo.name()).append(": ").append(String.format("%.2f", porcentaje)).append("%\n");
        }

        JOptionPane.showMessageDialog(this, resumen.toString(), "Resumen de Resultados", JOptionPane.INFORMATION_MESSAGE);
    }
}
