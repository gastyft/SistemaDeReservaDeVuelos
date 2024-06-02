package org.SistemaVuelos.menus;


import org.SistemaVuelos.exceptions.VueloNoEncontradoException;
import org.SistemaVuelos.gestores.GestorVuelos;

import java.util.Scanner;

public class MenuVuelos { //Todo llamadas a las funciones de carga

    GestorVuelos gestorVuelos = new GestorVuelos();

    public void menuVuelos() {



    String menu = """
            ---------------------MENU VUELOS-------------------
            1-Agregar
            2-Mostrar Todos
            3-Buscar por destino
            4-Buscar por ID
            5-Modificar
            6-Eliminar por ID
            ESCRIBA "ESC" PARA VOLVER AL MENU PRINCIPAL
            """;
    String opc = "Esc";
    Scanner scanner = new Scanner(System.in);
    do {
        System.out.println(menu);
        opc = scanner.nextLine().toUpperCase();
        switch (opc) {
            case "1" -> gestorVuelos.agregarVuelo();
            case "2" -> gestorVuelos.mostrarVuelos();
            case "3" -> {
                gestorVuelos.listaDestinos();
                try {
                    gestorVuelos.buscarPorDestinos();
                } catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "4" -> {
                try {
                    gestorVuelos.buscarUnVuelo();
                } catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "5" -> {
                try {
                    gestorVuelos.modificar();
                }catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "6" -> {
                try {
                    gestorVuelos.eliminar();
                } catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "ESC" -> System.out.println("ESCRIBIO ESC");
            default -> System.out.println("Opcion incorrecta. Elija nuevamente");
        }

    } while (!opc.equalsIgnoreCase("Esc"));

    System.out.println("Salio de la Gestion de Vuelos");
}
    }

