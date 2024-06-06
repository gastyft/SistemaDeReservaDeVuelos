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


    //String menu de opciones
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
            {try { // Agrega un nuevo vuelo
                gestorVuelos.agregarVuelo();
            }catch (VueloNoEncontradoException e){
                System.out.println(e.getMessage());
            }
            }
            case "2" -> gestorVuelos.mostrarVuelos(); //muestreo de vuelos
            case "3" -> { //Muestra destinos del enum y busca vuelos por destinos
                gestorVuelos.listaDestinos();
                try {
                    List<Vuelo> vueloList =gestorVuelos.buscarPorDestinos();
                } catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "4" -> { //Muestreo de vuelos y busca un vuelo por ID e imprime con Swing
                gestorVuelos.mostrarVuelos();
                try {
                    Vuelo vuelo= gestorVuelos.buscarUnVuelo();
                    if(vuelo!= null)
                    gestorVuelos.imprimirPantallaDetallesVuelo(vuelo,"B");
                } catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "5" -> {
                try { //Muestreo de vuelos y modificacion
                    gestorVuelos.mostrarVuelos();
                   Vuelo vuelo =  gestorVuelos.modificar();
                   if(vuelo!=null)
                    gestorVuelos.imprimirPantallaDetallesVuelo(vuelo,"1");
                }catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "6" -> {
                try { // Elimina un vuelo validando que no hay reservas para ese vuelo e imprime con swing
                   Vuelo vuelo= gestorVuelos.eliminar(gestorReservas);
                    if(vuelo!=null)
                    gestorVuelos.imprimirEliminarVuelo(vuelo);
                } catch (VueloNoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
            }
            case "7"->{
              try{ //Busca un vuelo por Id y cambia el estado e imprime la actualizacion con Swing
                  Vuelo vuelo = gestorVuelos.buscarUnVuelo();
                  if(vuelo ==null) throw new VueloNoEncontradoException("");
               Estado estado = gestorVuelos.cambiarEstado();
               if(vuelo != null && estado!= null){
                   vuelo.setEstadoDeVuelo(estado);
                   gestorVuelos.imprimirPantallaDetallesVuelo(vuelo,"1");
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

