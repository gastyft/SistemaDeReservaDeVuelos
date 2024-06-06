package org.SistemaVuelos.gestores;


import org.SistemaVuelos.exceptions.PasajeroNoEncontradoException;
import org.SistemaVuelos.model.Pasajero;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Scanner;

public class GestorPasajeros { //GESTOR DE CARGA DE DATOS


    /* String pais = "Estados Unidos";  //Si se quiere personalizar pasaportes por paises
     char primeraLetra = pais.charAt(0);
     public  boolean contieneSoloLetras(String str) {
         // Expresión regular para verificar si la cadena contiene solo letras
         // La expresión regular ^[a-zA-Z]*$ coincide con una cadena que contiene solo letras (mayúsculas y minúsculas)
         return str.matches("^[a-zA-Z]*$");
     }
     */
    Scanner scanner = new Scanner(System.in);
    GestorCRUD<Pasajero> gestorCRUD = new GestorCRUD<>();

    public void agregarPasajero() throws PasajeroNoEncontradoException{//Agrega un pasajero

        try {
            System.out.println("Ingrese nombre completo");
            String nombreCompleto=scanner.nextLine();
            if (!nombreCompleto.matches("[a-zA-Z\\s]+")) { //Valida que el nombre tenga solo letras
                System.out.println("Error: El nombre completo solo puede contener letras y espacios.");
               throw new PasajeroNoEncontradoException("El nombre completo solo puede contener letras y espacios");
                // Termina el método si la entrada no es válida
            }
            Pasajero pasajeroNuevo = new Pasajero(nombreCompleto);
            gestorCRUD.agregar(pasajeroNuevo, pasajeroNuevo.getId()); //LLamo a agregar del gestorCRUD generico
            System.out.println("Pasajero agregado con exito");
            imprimirPantallaDetallesPasajero(pasajeroNuevo,"0"); //Imprimo con Swing
        } catch (Exception e) {
            throw new PasajeroNoEncontradoException("ERROR EN CARGAR PASAJERO");
        }
    }

    public void buscarPorNombre() throws PasajeroNoEncontradoException { //Busqueda por nombre
        //Busca por nombre
        boolean encontrado = false;
        System.out.println("Ingrese nombre de pasajero a buscar");
        String pasajeroABuscar = scanner.nextLine();
        for (Map.Entry<String, Pasajero> entry : gestorCRUD.getTreeMap().entrySet()) {
            // entry.getValue() es el objeto Pasajero leido del treeMap
            Pasajero pasajero = entry.getValue(); //Se puede pasar entry.getValue() Para ahorrar una variable
            if (pasajero != null && pasajero.getNombreCompleto().equalsIgnoreCase(pasajeroABuscar)) {
                //Si hay un pasajero y su nombre es igual al scanneado entonces lo muestro con system.out y Swing
                System.out.println(entry.getValue());
                imprimirPantallaDetallesPasajero(entry.getValue(),"B");
            }
        }
        if (!encontrado) {
            throw new PasajeroNoEncontradoException(STR."No se encontraron pasajeros con nombres: \{pasajeroABuscar}");
        }
    }

    public Pasajero buscarUnPasajeroID() throws PasajeroNoEncontradoException { //busca y devuelve un pasajero
        Pasajero pasajero = null;
        System.out.println("Ingrese Id del pasajero");
        String pasaporte = scanner.nextLine().toUpperCase();
        try {
            pasajero = gestorCRUD.getTreeMap().get(pasaporte);//Si encuentra el pasaporte que es el Id entonces
            //entra al if y lo devuelve e imprime
            if (pasajero != null) {
                System.out.println("pasajero encontrado");
                System.out.println(pasajero); // Imprimir detalles del pasajero
                imprimirPantallaDetallesPasajero(pasajero,"B"); //Muestreo con Swing
            } else {
               throw  new PasajeroNoEncontradoException(STR."No se encontró ningún pasajero con el pasaporte: \{pasaporte}");
            }
        } catch (NullPointerException e) {
            System.out.println("El TreeMap no está inicializado o está vacío.");
        } catch (Exception e) {
            System.out.println(STR."Error en la búsqueda: \{e.getMessage()}");
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

    public Pasajero modificar()throws PasajeroNoEncontradoException {//Modifica un pasajero
        try {
            Pasajero pasajeroAModificar = buscarUnPasajeroID(); //Primero lo busco llamando al metodo buscar por ID
            if(pasajeroAModificar==null)
                throw new PasajeroNoEncontradoException("No se encontro el pasajero");
            System.out.println("Ingrese nuevo nombre completo"); //Si lo encuentra entonces pido nuevo valor de nombre
            String nombreCompleto=scanner.nextLine();
            if (!nombreCompleto.matches("[a-zA-Z\\s]+")) { //Valido que tenga letras y numeros
                System.out.println("Error: El nombre completo solo puede contener letras y espacios.");
              throw new PasajeroNoEncontradoException("El nombre solo puede contener letras y espacios");// Terminar el método si la entrada no es válida
            }
            pasajeroAModificar.setNombreCompleto(nombreCompleto); //Seteo nombre y lo devuelvo modificado
            return pasajeroAModificar;
        } catch (Exception e) {
            throw new PasajeroNoEncontradoException("No se pudo modificar.");
        }
    }

    public Pasajero eliminar() throws PasajeroNoEncontradoException{
        mostrarPasajeros(); //Muestro pasajeros para que se vean los id para luego buscar y eliminar
        Pasajero pasajeroAEliminar = buscarUnPasajeroID(); //LLamo metodo buscar por ID
        if (pasajeroAEliminar != null) { //Si el metodo anterior lo encuentra entonces entra al if
            gestorCRUD.eliminar(pasajeroAEliminar, pasajeroAEliminar.getId()); // Su funcion muestra si se borro o no exitosamente
          //LLamo a eliminar del gestorCRUD. Muestro en el menu Pasajeros
            return pasajeroAEliminar;
        } else throw  new PasajeroNoEncontradoException("No se pudo eliminar el objeto");
    }

    public void imprimirEliminarPasajero(Pasajero pasajero) { //Cartelito para cuando elimine
        SwingUtilities.invokeLater(() -> {
            String message = STR."El pasajero \{pasajero.getNombreCompleto()} ha sido eliminado \uD83D\uDE22"; // Emoji de carita triste
            JOptionPane.showMessageDialog(null, message, "Eliminado", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    public  void imprimirPantallaDetallesPasajero(Pasajero pasajero,String i) {
        // Imprime el detalle de un pasajero con Swing
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

            panel.add(new JLabel(STR."ID: \{pasajero.getId()}"));
            panel.add(new JLabel(STR."Nombre completo: \{pasajero.getNombreCompleto()}"));

            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}


