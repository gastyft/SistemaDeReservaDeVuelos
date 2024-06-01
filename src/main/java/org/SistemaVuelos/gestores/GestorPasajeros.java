package org.SistemaVuelos.gestores;

public class GestorPasajeros {


//VER SI PUEDO AGREGAR PAIS AL PASAJERO
    String pais = "Estados Unidos";
    char primeraLetra = pais.charAt(0);
    public static boolean contieneSoloLetras(String str) {
        // Expresión regular para verificar si la cadena contiene solo letras
        // La expresión regular ^[a-zA-Z]*$ coincide con una cadena que contiene solo letras (mayúsculas y minúsculas)
        return str.matches("^[a-zA-Z]*$");
    }
}
