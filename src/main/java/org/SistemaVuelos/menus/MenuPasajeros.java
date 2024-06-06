package org.SistemaVuelos.menus;

import org.SistemaVuelos.exceptions.PasajeroNoEncontradoException;
import org.SistemaVuelos.gestores.GestorPasajeros;
import org.SistemaVuelos.model.Pasajero;

import java.util.Scanner;

public class MenuPasajeros { //Todo llamadas a las funciones de carga

    GestorPasajeros gestorPasajeros = new GestorPasajeros();

    public void menuPasajeros() {

    // un string de las opciones del menu
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
        do { // do while con opc en String
            System.out.println(menu); //Imprime menu
            opc = scanner.nextLine().toUpperCase();
            switch (opc) {
                case "1" -> { //Agregar pasajero
                    try {
                        gestorPasajeros.agregarPasajero();
                    } catch (PasajeroNoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "2" -> gestorPasajeros.mostrarPasajeros(); //Muestra pasajeros
                case "3" -> { //muestra y busca pasajeros
                    gestorPasajeros.mostrarPasajeros();
                    try {
                        gestorPasajeros.buscarPorNombre();
                    } catch (PasajeroNoEncontradoException e) {
                        System.out.println("Pasajero no encontrado");
                    }
                }
                case "4" -> { //Busca un pasajero por id e imprime con Swing
                    try {
                       Pasajero pasajero= gestorPasajeros.buscarUnPasajeroID();
                       if(pasajero!=null)
                        gestorPasajeros.imprimirPantallaDetallesPasajero(pasajero,"B");
                    } catch (PasajeroNoEncontradoException e) {
                        System.out.println("Pasajero no encontrado");
                    }
                }

                case "5" -> { //Modifica un pasajero e imprime con Swing
                    try {
                    Pasajero pasajero =   gestorPasajeros.modificar();
                    if(pasajero!=null)
                        gestorPasajeros.imprimirPantallaDetallesPasajero(pasajero,"1");
                    } catch (PasajeroNoEncontradoException e) {
                        System.out.println("Pasajero no encontrado");
                    }
                }

                case "6" -> { //Elimina un pasajero e imprime con Swing
                    try {
                       Pasajero pasajero = gestorPasajeros.eliminar();
                       if(pasajero!=null)
                        gestorPasajeros.imprimirEliminarPasajero(pasajero);
                    } catch (PasajeroNoEncontradoException e) {
                        System.out.println("Pasajero no encontrado");
                    }
                }
                case "ESC" -> System.out.println("ESCRIBIO ESC");
                default -> System.out.println("Opcion incorrecta. Elija nuevamente");
            }

        } while (!opc.equalsIgnoreCase("Esc")); //Se sale al tipear ESC con equalsIgnoreCase
        System.out.println("Salio de la Gestion de Pasajeros");
    }
}
