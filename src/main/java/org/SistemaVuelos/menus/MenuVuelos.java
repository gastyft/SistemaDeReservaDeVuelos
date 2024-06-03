package org.SistemaVuelos.menus;


import org.SistemaVuelos.enums.Estado;
import org.SistemaVuelos.exceptions.VueloNoEncontradoException;
import org.SistemaVuelos.gestores.GestorReservas;
import org.SistemaVuelos.gestores.GestorVuelos;
import org.SistemaVuelos.model.Vuelo;

import java.util.List;
import java.util.Scanner;

public class MenuVuelos { //Todo llamadas a las funciones de carga

    GestorVuelos gestorVuelos = new GestorVuelos();

    public void menuVuelos(GestorReservas gestorReservas) {



    String menu = """
            ---------------------MENU VUELOS-------------------
            1-Agregar Vuelo
            2-Mostrar Todos
            3-Buscar por destino
            4-Buscar por ID
            5-Modificar
            6-Eliminar por ID
            7-Cambiar estado a un vuelo
            ESCRIBA "ESC" PARA VOLVER AL MENU PRINCIPAL
            """;
    String opc = "Esc";
    Scanner scanner = new Scanner(System.in);
    do {
        System.out.println(menu);
        opc = scanner.nextLine().toUpperCase();
        switch (opc) {
            case "1" ->
            {try {
                gestorVuelos.agregarVuelo();
            }catch (VueloNoEncontradoException e){
                System.out.println(e.getMessage());
            }
            }
            case "2" -> gestorVuelos.mostrarVuelos();
            case "3" -> {
                gestorVuelos.listaDestinos();
                try {
                    List<Vuelo> vueloList =gestorVuelos.buscarPorDestinos();
                } catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "4" -> {
                gestorVuelos.mostrarVuelos();
                try {
                    Vuelo vuelo= gestorVuelos.buscarUnVuelo();
                } catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "5" -> {
                try {
                    gestorVuelos.mostrarVuelos();
                   Vuelo vuelo =  gestorVuelos.modificar();
                    gestorVuelos.imprimirPantallaDetallesVuelo(vuelo);
                }catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "6" -> {
                try {
                    gestorVuelos.eliminar(gestorReservas);
                } catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "7"->{
              try{
                  Vuelo vuelo = gestorVuelos.buscarUnVuelo();
                  if(vuelo ==null) throw new VueloNoEncontradoException("");
               Estado estado = gestorVuelos.cambiarEstado();
               gestorVuelos.imprimirPantallaDetallesVuelo(vuelo);
               if(vuelo != null && estado!= null){
                   vuelo.setEstadoDeVuelo(estado);
               }
              }catch (VueloNoEncontradoException e){
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

