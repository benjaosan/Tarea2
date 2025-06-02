package backend;

public class Item {
    public enum Tipo {
        OpcionMultiple, VF
    }

    private String pregunta;
    private String[] opciones;
    private String respuestaCorrecta;
    private String nivel;
    private Tipo tipo;
    private int tiempoEstimado;

    public Item(String pregunta, String[] opciones, String respuestaCorrecta, String nivel, Tipo tipo, int tiempoEstimado) {
        this.pregunta = pregunta;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
        this.nivel = nivel;
        this.tipo = tipo;
        this.tiempoEstimado = tiempoEstimado;
    }

    public String getPregunta() { return pregunta; }
    public String[] getOpciones() { return opciones; }
    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public String getNivel() { return nivel; }
    public Tipo getTipo() { return tipo; }
    public int getTiempoEstimado() { return tiempoEstimado; }
}
