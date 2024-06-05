package org.SistemaVuelos.gestores;

import org.SistemaVuelos.enums.Destinos;
import org.SistemaVuelos.exceptions.PasajeroNoEncontradoException;
import org.SistemaVuelos.menus.MenuPrincipal;
import org.SistemaVuelos.model.Pasajero;
import org.SistemaVuelos.model.Vuelo;

import javax.swing.*;
import java.awt.*;
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

    public void agregarPasajero() throws PasajeroNoEncontradoException{

        try {
            System.out.println("Ingrese nombre completo");
            Pasajero pasajeroNuevo = new Pasajero(scanner.nextLine());
            gestorCRUD.agregar(pasajeroNuevo, pasajeroNuevo.getId());
            System.out.println("Pasajero agregado con exito");
            imprimirPantallaDetallesPasajero(pasajeroNuevo,"0");
        } catch (Exception e) {
            throw new PasajeroNoEncontradoException("ERROR EN CARGAR PASAJERO");
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
                imprimirPantallaDetallesPasajero(entry.getValue(),"B");
            }
        }
        if (!encontrado) {
            throw new PasajeroNoEncontradoException("No se encontraron pasajeros con nombres: " + pasajeroABuscar);
        }
    }

    public Pasajero buscarUnPasajeroID() throws PasajeroNoEncontradoException { //busca y devuelve un pasajero
        Pasajero pasajero = null;
        System.out.println("Ingrese Id del pasajero");
        String pasaporte = scanner.nextLine().toUpperCase();
        try {
            pasajero = gestorCRUD.getTreeMap().get(pasaporte);
            if (pasajero != null) {
                System.out.println("pasajero encontrado");
                System.out.println(pasajero); // Imprimir detalles del pasajero
                imprimirPantallaDetallesPasajero(pasajero,"B");
            } else {
               throw  new PasajeroNoEncontradoException("No se encontró ningún pasajero con el pasaporte: " + pasaporte);
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

    public Pasajero modificar()throws PasajeroNoEncontradoException {
        try {
            Pasajero pasajeroAModificar = buscarUnPasajeroID();
            if(pasajeroAModificar==null)
                throw new PasajeroNoEncontradoException("No se encontro el pasajero");
            System.out.println("Ingrese nuevo nombre completo");
            pasajeroAModificar.setNombreCompleto(scanner.nextLine());
            return pasajeroAModificar;
        } catch (Exception e) {
            throw new PasajeroNoEncontradoException("No se pudo modificar.");
        }
    }

    public Pasajero eliminar() throws PasajeroNoEncontradoException{
        mostrarPasajeros();
        Pasajero pasajeroAEliminar = buscarUnPasajeroID(); //
        if (pasajeroAEliminar != null) {
            gestorCRUD.eliminar(pasajeroAEliminar, pasajeroAEliminar.getId()); // Su funcion muestra si se borro o no exitosamente
            return pasajeroAEliminar;
        } else throw  new PasajeroNoEncontradoException("No se pudo eliminar el objeto");
    }

    public void imprimirEliminarPasajero(Pasajero pasajero) {
        SwingUtilities.invokeLater(() -> {
            String message = "El pasajero ha sido eliminado \uD83D\uDE22"; // Emoji de carita triste
            JOptionPane.showMessageDialog(null, message, "Eliminado", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    public  void imprimirPantallaDetallesPasajero(Pasajero pasajero,String i) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Detalles del Pasajero");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 200);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 1)); // Una columna y filas automáticas
            JLabel label1;
            if(i.equals("1"))
             label1 = new JLabel("ACTUALIZACION");
            else if(i.equals("0"))label1 =new JLabel("PASAJERO AGREGADO");
            else label1 = new JLabel("PASAJERO ENCONTRADO");
            label1.setBackground(Color.RED); // Cambia el color de fondo a rojo
            label1.setOpaque(true); // Esto es necesario para que el color de fondo sea visible
            panel.add(label1);

            JLabel label = new JLabel("Detalles del Pasajero:");
            label.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(label);

            panel.add(new JLabel("ID: " + pasajero.getId()));
            panel.add(new JLabel("Nombre completo: " + pasajero.getNombreCompleto()));

            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}


