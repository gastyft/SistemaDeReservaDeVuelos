package org.SistemaVuelos.menus;

import org.SistemaVuelos.exceptions.PasajeroNoEncontradoException;
import org.SistemaVuelos.gestores.GestorPasajeros;
import org.SistemaVuelos.model.Pasajero;

import java.util.Scanner;

public class MenuPasajeros { //Todo llamadas a las funciones de carga

    GestorPasajeros gestorPasajeros = new GestorPasajeros();

    public void menuPasajeros() {


        String menu = """
                ---------------------MENU PASAJEROS-------------------
                1-Agregar Pasajero
                2-Mostrar Todos
                3-Buscar pasajero por nombre
                4-Buscar por ID
                5-Modificar un pasajero
                6-Eliminar por ID
                ESCRIBA "ESC" PARA VOLVER AL MENU PRINCIPAL
                """;
        String opc = "Esc";
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println(menu);
            opc = scanner.nextLine().toUpperCase();
            switch (opc) {
                case "1" -> {
                    try {
                        gestorPasajeros.agregarPasajero();
                    } catch (PasajeroNoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "2" -> gestorPasajeros.mostrarPasajeros();
                case "3" -> {
                    gestorPasajeros.mostrarPasajeros();
                    try {
                        gestorPasajeros.buscarPorNombre();
                    } catch (PasajeroNoEncontradoException e) {
                        System.out.println("Pasajero no encontrado");
                    }
                }
                case "4" -> {
                    try {
                       Pasajero pasajero= gestorPasajeros.buscarUnPasajeroID();
                        gestorPasajeros.imprimirPantallaDetallesPasajero(pasajero,"B");
                    } catch (PasajeroNoEncontradoException e) {
                        System.out.println("Pasajero no encontrado");
                    }
                }

                case "5" -> {
                    try {
                    Pasajero pasajero =   gestorPasajeros.modificar();
                        gestorPasajeros.imprimirPantallaDetallesPasajero(pasajero,"1");
                    } catch (PasajeroNoEncontradoException e) {
                        System.out.println("Pasajero no encontrado");
                    }
                }

                case "6" -> {
                    try {
                       Pasajero pasajero = gestorPasajeros.eliminar();
                        gestorPasajeros.imprimirEliminarPasajero(pasajero);
                    } catch (PasajeroNoEncontradoException e) {
                        System.out.println("Pasajero no encontrado");
                    }
                }
                case "ESC" -> System.out.println("ESCRIBIO ESC");
                default -> System.out.println("Opcion incorrecta. Elija nuevamente");
            }

        } while (!opc.equalsIgnoreCase("Esc"));
        System.out.println("Salio de la Gestion de Pasajeros");
    }
}
