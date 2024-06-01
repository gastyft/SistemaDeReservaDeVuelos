package org.SistemaVuelos;

import org.SistemaVuelos.gestores.GestorVuelos;

public class Main {
    public static void main(String[] args) {


        GestorVuelos gestorVuelos = new GestorVuelos();

        gestorVuelos.agregarVuelo();
        gestorVuelos.agregarVuelo();

        gestorVuelos.mostrarVuelos();
        gestorVuelos.buscarPorDestinos("mar del plata");
    }
}