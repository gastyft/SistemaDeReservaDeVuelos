package org.SistemaVuelos.menus;

import org.SistemaVuelos.exceptions.PasajeroNoEncontradoException;
import org.SistemaVuelos.exceptions.ReservaNoEncontradaException;
import org.SistemaVuelos.exceptions.VueloNoEncontradoException;
import org.SistemaVuelos.gestores.GestorCRUD;
import org.SistemaVuelos.gestores.GestorPasajeros;
import org.SistemaVuelos.gestores.GestorReservas;
import org.SistemaVuelos.gestores.GestorVuelos;
import org.SistemaVuelos.model.Reserva;

import java.util.Scanner;

public class MenuReservas { //Todo llamadas a las funciones de carga



public void menuReservas(GestorPasajeros gestorPasajeros,GestorVuelos gestorVuelos){
    GestorReservas gestorReservas= new GestorReservas(gestorPasajeros,gestorVuelos);


    String menu = """
                ---------------------MENU RESERVAS-------------------
                1-Agregar Reserva
                2-Mostrar Reservas
                3-Buscar reservas por vuelo
                4-Buscar reservas por ID de reserva
                5-Buscar reservas por pasajero
                6-Modificar una reserva
                7-Eliminar por ID
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
                    gestorReservas.agregarReserva();
                } catch (ReservaNoEncontradaException | PasajeroNoEncontradoException |VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "2" -> gestorReservas.mostrarReservas();
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
                    gestorPasajeros.buscarUnPasajeroID();
                } catch (PasajeroNoEncontradoException e) {
                    System.out.println("Pasajero no encontrado");
                }
            }

            case "5" -> {
                try {
                    gestorPasajeros.modificar();
                } catch (PasajeroNoEncontradoException e) {
                    System.out.println("Pasajero no encontrado");
                }
            }

            case "6" -> {
                try {
                    gestorPasajeros.eliminar();
                } catch (PasajeroNoEncontradoException e) {
                    System.out.println("Pasajero no encontrado");
                }
            }
            case "ESC" -> System.out.println("ESCRIBIO ESC");
            default -> System.out.println("Opcion incorrecta. Elija nuevamente");
        }

    } while (!opc.equalsIgnoreCase("Esc"));

    System.out.println("Salio de la Gestion de Vuelos");
}
}
