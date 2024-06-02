package org.SistemaVuelos.gestores;

import org.SistemaVuelos.enums.Destinos;
import org.SistemaVuelos.exceptions.PasajeroNoEncontradoException;
import org.SistemaVuelos.menus.MenuPrincipal;
import org.SistemaVuelos.model.Pasajero;
import org.SistemaVuelos.model.Vuelo;

import java.util.Map;
import java.util.Scanner;

public class GestorPasajeros { //GESTOR DE CARGA DE DATOS


    /* String pais = "Estados Unidos";  //Si se quiere personalizar pasaportes por paises
     char primeraLetra = pais.charAt(0);
     public static boolean contieneSoloLetras(String str) {
         // Expresión regular para verificar si la cadena contiene solo letras
         // La expresión regular ^[a-zA-Z]*$ coincide con una cadena que contiene solo letras (mayúsculas y minúsculas)
         return str.matches("^[a-zA-Z]*$");
     }
     */
    Scanner scanner = new Scanner(System.in);
    GestorCRUD<Pasajero> gestorCRUD = new GestorCRUD<Pasajero>();

    public void agregarPasajero() {

        try {
            System.out.println("Ingrese nombre completo");
            Pasajero pasajeroNuevo = new Pasajero(scanner.nextLine());
            gestorCRUD.agregar(pasajeroNuevo, pasajeroNuevo.getId());
        } catch (Exception e) {
            throw new RuntimeException("ERROR EN CARGAR PASAJERO");
        }
    }

    public void buscarPorNombre() throws PasajeroNoEncontradoException {
        //Busca por nombre
        boolean encontrado = false;
        System.out.println("Ingrese nombre de pasajero a buscar");
        String pasajeroABuscar = scanner.nextLine(); //No uso upperCase porque uso equalsIgnoreCase
        //Todo llamo Map.entry y creo dos variables pasajero y destino para no perder la generacidad en GestorCRUD
        for (Map.Entry<String, Pasajero> entry : gestorCRUD.getTreeMap().entrySet()) {
            // entry.getValue() es el objeto Pasajero leido del treeMap
            Pasajero pasajero = entry.getValue();
            if (pasajero != null && pasajero.getNombreCompleto().equalsIgnoreCase(pasajeroABuscar)) {
                System.out.println(entry.getValue());
            }
        }
        if (!encontrado) {
            throw new PasajeroNoEncontradoException("No se encontraron pasajeros con nombres: " + pasajeroABuscar);
        }
    }

    public Pasajero buscarUnPasajeroID() { //busca y devuelve un pasajero
        Pasajero pasajero = null;
        System.out.println("Ingrese Id del pasajero");
        String pasaporte = scanner.nextLine();
        try {
            pasajero = gestorCRUD.getTreeMap().get(pasaporte);
            if (pasajero != null) {
                System.out.println("pasajero encontrado");
                System.out.println(pasajero); // Imprimir detalles del pasajero
            } else {
                System.out.println("No se encontró ningún pasajero con el pasaporte: " + pasaporte);
            }
        } catch (NullPointerException e) {
            System.out.println("El TreeMap no está inicializado o está vacío.");
        } catch (Exception e) {
            System.out.println("Error en la búsqueda: " + e.getMessage());
        }
        return pasajero;
    }

    public void mostrarPasajeros() { //Muestra todos los pasajeros
        try {
            gestorCRUD.mostrar();
        } catch (NullPointerException e) {
            System.out.println("Error al mostrar");
        }
    }

    public void modificar() {
        try {
            Pasajero pasajeroAModificar = buscarUnPasajeroID();
            System.out.println("Ingrese nuevo nombre completo");
            pasajeroAModificar.setNombreCompleto(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Error al modificar");
        }
    }

    public void eliminar() {
        mostrarPasajeros();
        Pasajero pasajeroAEliminar = buscarUnPasajeroID(); //
        if (pasajeroAEliminar != null) {
            gestorCRUD.eliminar(pasajeroAEliminar, pasajeroAEliminar.getId()); // Su funcion muestra si se borro o no exitosamente
        } else System.out.println("No se pudo eliminar el objeto");
    }

}


