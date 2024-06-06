package org.SistemaVuelos.menus;

import org.SistemaVuelos.exceptions.AsientoNoDisponibleException;
import org.SistemaVuelos.exceptions.PasajeroNoEncontradoException;
import org.SistemaVuelos.exceptions.ReservaNoEncontradaException;
import org.SistemaVuelos.exceptions.VueloNoEncontradoException;
import org.SistemaVuelos.gestores.GestorPasajeros;
import org.SistemaVuelos.gestores.GestorReservas;
import org.SistemaVuelos.gestores.GestorVuelos;
import org.SistemaVuelos.model.Reserva;


import java.util.Scanner;

public class MenuReservas { //Todo Llamadas a las funciones de carga

    GestorReservas gestorReservas;

    public MenuReservas(GestorPasajeros gestorPasajeros, GestorVuelos gestorVuelos) {
        this.gestorReservas = new GestorReservas(gestorPasajeros, gestorVuelos); //Inicializo el gestor reservas
        //Trayendo el gestor pasajeros y gestor vuelos que cada uno tiene sus respectivos treeMap necesarios para las reservas
    }


    public void menuReservas() {
        //String de menu de opciones
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
        String opc ;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println(menu);
            opc = scanner.nextLine().toUpperCase();
            switch (opc) {
                case "1" -> {
                    try { //Agregar reservas
                        gestorReservas.agregarReserva();
                    } catch (ReservaNoEncontradaException | PasajeroNoEncontradoException |
                             VueloNoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "2" -> gestorReservas.mostrarReservas(); //Muestreo de reservas
                case "3" -> {
                    gestorReservas.mostrarReservas(); //Muestreo de reservas y busquedas por vuelos
                    try {
                        gestorReservas.buscarReservasPorVuelo();
                    } catch (ReservaNoEncontradaException | VueloNoEncontradoException e) {
                        System.out.println(" ");
                    }
                }
                case "4" -> {
                    try { //Busqueda de una reserva por ID e imprime con Swing
                        gestorReservas.mostrarReservas();
                        Reserva reserva = gestorReservas.buscarUnaReservaPorID();
                        if(reserva !=null) {
                            gestorReservas.imprimirPantallaDetallesReserva(reserva, "B");
                        }
                    } catch (ReservaNoEncontradaException e) {
                        System.out.println("Reserva no encontrado");
                    }
                }

                case "5" -> {
                    try { //Busqueda de reservas por pasajero
                      gestorReservas.buscarReservasPorPasajero();
                    } catch (ReservaNoEncontradaException | PasajeroNoEncontradoException e) {
                        System.out.println(" ");
                    }
                }

                case "6" -> {
                    try { //Muestreo de reservas y modificar una reserva
                        gestorReservas.mostrarReservas();
                        gestorReservas.modificarReserva();
                    } catch (PasajeroNoEncontradoException | ReservaNoEncontradaException | VueloNoEncontradoException |
                             AsientoNoDisponibleException e) {
                        System.out.println(" ");
                    }
                }
                case "7" -> {
                    try { //Elimina una reserva e imprime cartelito de eliminacion
                        Reserva reserva = gestorReservas.eliminar();
                        if(reserva!=null) {
                            gestorReservas.imprimirEliminarReserva(reserva);
                        }
                    } catch (ReservaNoEncontradaException e) {
                        System.out.println("Reserva no encontrada. No se pudo eliminar");
                    }
                }
                case "8" -> {
                    try { // Busca una reserva por Id y cancela la reserva
                        gestorReservas.mostrarReservas();
                        Reserva reserva = gestorReservas.buscarUnaReservaPorID();
                        if (reserva == null) throw new ReservaNoEncontradaException(" ");
                        gestorReservas.cancelarReserva(reserva);

                    } catch (ReservaNoEncontradaException e) {
                        System.out.println(" ");
                    }

                }
                case "9" -> {
                    try { //Da de alta una reserva cancelada
                      gestorReservas.darDeAltaReservaCancelada();

                    } catch (ReservaNoEncontradaException | AsientoNoDisponibleException e) {
                        System.out.println(" ");
                    }

                }
                case "ESC" -> System.out.println("ESCRIBIO ESC");
                default -> System.out.println("Opcion incorrecta. Elija nuevamente");
            }

        } while (!opc.equalsIgnoreCase("Esc")); //sale del menu reservas

        System.out.println("Salio de la Gestion de Reservas");
    }
}
