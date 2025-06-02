package backend;

import java.io.*;
import java.util.*;

public class CargadorDeArchivo {

    public static List<Item> cargarDesdeArchivo(String ruta) throws IOException {
        List<Item> items = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(ruta));
        String linea;
        while ((linea = reader.readLine()) != null) {
            String[] partes = linea.split(";");
            if (partes.length < 6) {
                System.out.println("Linea ignorada por formato incorrecto: " + linea);
                continue;
            }
            String pregunta = partes[0];
            String[] opciones = partes[1].split(",");
            String respuesta = partes[2];
            String nivel = partes[3];
            Item.Tipo tipo = partes[4].equalsIgnoreCase("VF") ? Item.Tipo.VF : Item.Tipo.OpcionMultiple;
            int tiempo = Integer.parseInt(partes[5]);
            items.add(new Item(pregunta, opciones, respuesta, nivel, tipo, tiempo));
        }
        reader.close();
        return items;
    }
}
