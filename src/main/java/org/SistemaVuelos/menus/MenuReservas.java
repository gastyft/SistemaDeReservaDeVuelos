package org.SistemaVuelos.menus;

import org.SistemaVuelos.exceptions.AsientoNoDisponibleException;
import org.SistemaVuelos.exceptions.PasajeroNoEncontradoException;
import org.SistemaVuelos.exceptions.ReservaNoEncontradaException;
import org.SistemaVuelos.exceptions.VueloNoEncontradoException;
import org.SistemaVuelos.gestores.GestorPasajeros;
import org.SistemaVuelos.gestores.GestorReservas;
import org.SistemaVuelos.gestores.GestorVuelos;
import org.SistemaVuelos.model.Reserva;
import java.util.List;
import java.util.Scanner;

public class MenuReservas { //Todo Llamadas a las funciones de carga

    GestorReservas gestorReservas;

    public MenuReservas(GestorPasajeros gestorPasajeros, GestorVuelos gestorVuelos) {
        this.gestorReservas = new GestorReservas(gestorPasajeros, gestorVuelos);
    }


    public void menuReservas() {

        String menu = """
                ---------------------MENU RESERVAS-------------------
                1-Agregar Reserva
                2-Mostrar Reservas
                3-Buscar reservas por vuelo
                4-Buscar reservas por ID de reserva
                5-Buscar reservas por pasajero
                6-Modificar una reserva
                7-Eliminar por ID
                8-Cancelar una reserva
                9-Dar de alta una reserva cancelada
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
                    } catch (ReservaNoEncontradaException | PasajeroNoEncontradoException |
                             VueloNoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "2" -> gestorReservas.mostrarReservas();
                case "3" -> {
                    gestorReservas.mostrarReservas();
                    try {
                        List<Reserva> busquedaReservasPorVuelo = gestorReservas.buscarReservasPorVuelo();
                    } catch (ReservaNoEncontradaException | VueloNoEncontradoException e) {
                        System.out.println(" ");
                    }
                }
                case "4" -> {
                    try {
                        gestorReservas.mostrarReservas();
                        gestorReservas.buscarUnaReservaPorID();
                    } catch (ReservaNoEncontradaException e) {
                        System.out.println("Reserva no encontrado");
                    }
                }

                case "5" -> {
                    try {
                        List<Reserva> reservasPorPasajeroList = gestorReservas.buscarReservasPorPasajero();
                    } catch (ReservaNoEncontradaException | PasajeroNoEncontradoException e) {
                        System.out.println(" ");
                    }
                }

                case "6" -> {
                    try {
                        gestorReservas.mostrarReservas();
                        gestorReservas.modificarReserva();
                    } catch (PasajeroNoEncontradoException | ReservaNoEncontradaException | VueloNoEncontradoException |
                             AsientoNoDisponibleException e) {
                        System.out.println(" ");
                    }
                }
             /*   case "7" -> {
                    try {


                    } catch () {

                    }

                } */
                case "8" -> {
                    try {
                        Reserva reserva= gestorReservas.buscarUnaReservaPorID();
                        if(reserva ==null) throw new ReservaNoEncontradaException(" ");
            gestorReservas.cancelarReserva(reserva);

                    } catch (ReservaNoEncontradaException e) {
                        System.out.println(" ");
                    }

                }
         case "9" -> {
                    try {
                    gestorReservas.darDeAltaReservaCancelada();

                    } catch (ReservaNoEncontradaException |AsientoNoDisponibleException e) {
                        System.out.println(" ");
                    }

                }
                case "ESC" -> System.out.println("ESCRIBIO ESC");
                default -> System.out.println("Opcion incorrecta. Elija nuevamente");
            }

        } while (!opc.equalsIgnoreCase("Esc"));

        System.out.println("Salio de la Gestion de Vuelos");
    }
}
