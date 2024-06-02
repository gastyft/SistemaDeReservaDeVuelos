package org.SistemaVuelos.menus;

import org.SistemaVuelos.exceptions.PasajeroNoEncontradoException;
import org.SistemaVuelos.gestores.GestorPasajeros;

import java.util.Scanner;

public class MenuPasajeros {

    GestorPasajeros gestorPasajeros = new GestorPasajeros();
public void menuPasajeros(){


        String menu = """
                ---------------------MENU VUELOS-------------------
                1-Agregar
                2-Mostrar Todos
                3-Buscar pasajero por nombre
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
                case "1" -> gestorPasajeros.agregarPasajero();
                case "2" -> gestorPasajeros.mostrarPasajeros();
                case "3" -> {
                     gestorPasajeros.mostrarPasajeros();
                     try {
                         gestorPasajeros.buscarPorNombre();
                     }
                     catch (PasajeroNoEncontradoException e){
                         System.out.println("Pasajero no encontrado");
                     }
                     }
                case "4" -> gestorPasajeros.buscarUnPasajeroID();
                case "5" -> gestorPasajeros.modificar();
                case "6" -> gestorPasajeros.eliminar();
                case "ESC" -> System.out.println("ESCRIBIO ESC");
                default -> System.out.println("Opcion incorrecta. Elija nuevamente");
            }

        } while (!opc.equalsIgnoreCase("Esc"));

        System.out.println("Salio de la Gestion de Vuelos");
    }
}
