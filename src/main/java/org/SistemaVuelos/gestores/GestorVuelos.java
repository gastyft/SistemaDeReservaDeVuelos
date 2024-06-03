package org.SistemaVuelos.gestores;

import org.SistemaVuelos.enums.Destinos;
import org.SistemaVuelos.enums.Estado;
import org.SistemaVuelos.enums.TipoDeVuelo;
import org.SistemaVuelos.exceptions.VueloNoEncontradoException;
import org.SistemaVuelos.menus.MenuVuelos;
import org.SistemaVuelos.model.Vuelo;

import javax.swing.*;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.*;
import java.util.List;

public class GestorVuelos { //GESTOR DE CARGA DE DATOS PARA VUELOS
    GestorCRUD<Vuelo> gestorCRUD = new GestorCRUD<Vuelo>();
    Scanner scanner = new Scanner(System.in);

    public void listaDestinos() { //muestro los destinos posibles de los vuelos
        System.out.println("Lista de destinos disponibles:");
        for (Destinos destino : Destinos.values()) {
            System.out.println(destino.getNombre());
        }
    }

    //AGREGAR
    public LocalDateTime cargaHorarioSalida() {

        LocalDateTime horarioSalida = null;
        int hora = 0;
        int min = 0;
        int anio = 0;
        int mes = 0;
        int dia = 0;

        boolean fechaValida = false;
        while (!fechaValida) {
            try {
                System.out.println("Ingrese horario de salida ");
                System.out.println("Ingrese hora");
                hora = scanner.nextInt();
                System.out.println("Ingrese minutos");
                min = scanner.nextInt();
                System.out.println("Ingrese año");
                anio = scanner.nextInt();
                System.out.println("Ingrese mes");
                mes = scanner.nextInt();
                System.out.println("Ingrese día");
                dia = scanner.nextInt();

                scanner.nextLine(); //LIMPIO BUFFER porque tuve errores con el scanner luego de cargar un int. Asi se me soluciona
                // Validar la fecha ingresada
                horarioSalida = LocalDateTime.of(anio, mes, dia, hora, min);

                // Verificar si la fecha es igual o mayor que la fecha actual
                if (!horarioSalida.toLocalDate().isBefore(LocalDate.now())) {
                    fechaValida = true;
                } else {
                    System.out.println("La fecha ingresada debe ser igual o mayor que la fecha de hoy.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número válido.");
                scanner.next(); // Limpiar el buffer del scanner para evitar un bucle infinito
            } catch (DateTimeException e) {
                System.out.println("La fecha y hora ingresadas no son válidas. Por favor, inténtelo de nuevo.");
            }
        }
        return horarioSalida;
    }
 /*   public TipoDeVuelo cargaTipoVuelo() { //Sub funcion que ahorro por agregar en el enum Destinos el tipo de vuelo

        System.out.println("Seleccione el tipo de vuelo:");
        for (TipoDeVuelo tipo : TipoDeVuelo.values()) {
            System.out.println((tipo.ordinal() + 1) + ". " + tipo.name());
        }
        int opcionSeleccionada;
        do {
            System.out.println("Ingrese el número correspondiente al tipo de vuelo:");
            opcionSeleccionada = scanner.nextInt();
        } while (opcionSeleccionada < 1 || opcionSeleccionada > TipoDeVuelo.values().length);
        TipoDeVuelo tipoDeVuelo = TipoDeVuelo.values()[opcionSeleccionada - 1];
        return tipoDeVuelo;
    }
    */


    public Destinos cargaDestino() { //Todo EN EL ENUM DESTINO LE AGREGO EL TIPO SI ES NACIONAL O INTERNACIONAL
        //Todo PARA AHORRAR CODIGO Y OBTENERLO MAS LIMPIO
        listaDestinos();
        boolean cargo = false;
        Destinos destino = null;
        // Scanner scanner1 = new Scanner( System.in);
        while (!cargo) {
            System.out.println("Ingrese el nombre del destino de la lista: ");
            String nombreDestino = scanner.nextLine().trim().toUpperCase();
            // Buscar el destino correspondiente al nombre ingresado
            for (Destinos d : Destinos.values()) {
                if (d.getNombre().equalsIgnoreCase(nombreDestino)) {
                    destino = d;
                    break;
                }
            }
            if (destino != null) {
                cargo = true;
                System.out.println("Has seleccionado el destino: " + destino.getNombre());
            } else {
                System.out.println("El destino ingresado no está en la lista de destinos disponibles.");
            }
        }
        return destino;
    }

    public void agregarVuelo() throws VueloNoEncontradoException {//TODO FUNCION AGREGAR CON SUBFUNCIONES PARA DESCOMPRIMIR Y QUE NO QUEDE UN METODO MUY EXTENSO
        String tipoID = "D";
        try {
            Destinos destino = cargaDestino();
            TipoDeVuelo tipoDeVuelo = destino.getTipo();

            if (tipoDeVuelo != null) {  //SEGUN TIPO DE VUELO SERA SU DENOMINACION DE ID DE VUELO
                // SI ES NACIONAL SE PASA N O SI ES INTERNACIONAL SE PASARA I Y POR DEFECTO D en case de que falle
                if (tipoDeVuelo.equals(TipoDeVuelo.NACIONAL)) {
                    // Si es igual a NACIONAL
                    tipoID = "N";
                } else if (tipoDeVuelo.equals(TipoDeVuelo.INTERNACIONAL)) {
                    // Si es igual a INTERNACIONAL
                    tipoID = "I";
                }
                Vuelo vuelo = new Vuelo(destino, cargaHorarioSalida(), Estado.PROGRAMADO, destino.getTipo(), tipoID);
                gestorCRUD.agregar(vuelo, vuelo.getId()); //Agrego en gestor generico
                System.out.println("Vuelo creado con exito");
            } else throw new VueloNoEncontradoException("ERROR al agregar un nuevo vuelo");
        } catch (Exception e) {
            System.out.println("Error en agregar el vuelo: " + e.getMessage());
        }
    }

    public void mostrarVuelos() { //Muestra todos los vuelos
        try {
            gestorCRUD.mostrar();
        } catch (NullPointerException e) {
            System.out.println("Error al mostrar");
        }
    }

    public List<Vuelo> buscarPorDestinos() throws VueloNoEncontradoException { //Busca por destino
        boolean encontrado = false;
        List<Vuelo> vueloList = new ArrayList<>();
        System.out.println("Ingrese destino a buscar");
        // Scanner scanner1 =new Scanner(System.in);
        String destinoABuscar = scanner.nextLine(); //No uso upperCase porque uso equalsIgnoreCase
        //Todo llamo Map.entry y creo dos variables vuelo y destino para no perder la generacidad en GestorCRUD
        for (Map.Entry<String, Vuelo> entry : gestorCRUD.getTreeMap().entrySet()) {
            // entry.getValue() es el objeto Vuelo leido del treeMap
            Destinos destino = entry.getValue().getDestino();

            if (destino != null && destino.getNombre().equalsIgnoreCase(destinoABuscar)) {
                vueloList.add(entry.getValue());
                System.out.println(entry.getValue());
                encontrado = true;

            }
        }
        if (!encontrado) {
            throw new VueloNoEncontradoException("No se encontraron vuelos para el destino: " + destinoABuscar);
        }
        return vueloList;
    }

    public Vuelo buscarUnVuelo() throws VueloNoEncontradoException { //busca y devuelve un vuelo
        Vuelo vuelo = null; //TODO VER PORQUE NO ANDA BUSCAR
        System.out.println("Ingrese Id del vuelo");
        // Scanner scanner1 =new Scanner(System.in);
        // TODO SOLUCION COLOCAR UN scanner.nextLine() debajo de la ultima carga de un scanner.nextInt()
        // scanner.nextLine(); //Tuve que crear un nuevo scanner localmente porque no funcionaba
        //me capturaba un dato vacio y saltaba a la exeption
        //Una suposicion mia es que como en agregarVuelo llamo al scanner instanciado de manera global
        // repetidas veces entonces me debe quedar "tildado" o "sin limpiar"
        //porque en el Gestor Pasajeros realizo la misma logica y funciona perfectamente para busqueda
        String num = scanner.nextLine().trim().toUpperCase();
        try {
            vuelo = gestorCRUD.getTreeMap().get(num);
            if (vuelo != null) {
                System.out.println("Vuelo encontrado");
                System.out.println(vuelo); // Imprimir detalles del vuelo

            } else { //tira una nueva excepcion en el momento si no se encontro el vuelo
                throw new VueloNoEncontradoException("No se encontró ningún vuelo con el número: " + num);
            }
        } catch (NullPointerException e) {
            System.out.println("El TreeMap no está inicializado o está vacío.");
        } catch (Exception e) {
            System.out.println("Error en la búsqueda: " + e.getMessage());
        }
        return vuelo;
    }


    public void eliminar() throws VueloNoEncontradoException { //elimina un vuelo
        // TODO PREGUNTAR por si hay reservas activas o no puedo eliminar
        mostrarVuelos();
        Vuelo vueloAEliminar = buscarUnVuelo(); // Vuelo vueloAEliminar = gestorCRUD.getTreeMap().get(numVuelo);
        if (vueloAEliminar != null) {
            gestorCRUD.eliminar(vueloAEliminar, vueloAEliminar.getId()); // Su funcion muestra si se borro o no exitosamente
        } else throw new VueloNoEncontradoException("No se pudo eliminar el Vuelo");
    }

    public Estado cambiarEstado() {
        boolean cargo = false;
        Estado estado = null;
        // Scanner scanner3 =new Scanner(System.in);
        while (!cargo) {


            // Buscar el destino correspondiente al nombre ingresado
            for (Estado estMuestra : Estado.values()) {
                System.out.println(estMuestra);
            }
            System.out.println("Ingrese nuevo estado");
            String nuevoEstado = scanner.nextLine().trim().toUpperCase();
            for (Estado est : Estado.values()) {
                if (est.name().equals(nuevoEstado)) {
                    estado = est;
                    break;
                }
            }
            if (estado != null) {
                cargo = true;
                System.out.println("Has seleccionado el estado: " + estado);
            } else {
                System.out.println("El destino ingresado no está en la lista de destinos disponibles.");
            }
        }
        return estado;
    }

    public Vuelo modificar() throws VueloNoEncontradoException { //Modificar
        //Reutilizo funciones de agregar
        try {
            System.out.println("Modificar un vuelo");
            Vuelo vuelo = buscarUnVuelo();
            System.out.println("Carga de destino nuevo");
            Destinos destino = cargaDestino();
            vuelo.setDestino(destino);
            vuelo.setTipoVuelo(destino.getTipo());
            System.out.println("Cambiar estado");
            vuelo.setEstadoDeVuelo(cambiarEstado());
            System.out.println("Cargar horario salida");
            vuelo.setHorarioSalida(cargaHorarioSalida());
            gestorCRUD.modificar(vuelo.getId(), vuelo);
            return vuelo;
        } catch (Exception e) {
            throw new VueloNoEncontradoException("No se pudo modificar el vuelo");
        }
    }

    public void imprimirPantallaDetallesVuelo(Vuelo vuelo) { //Imprime con Swing una actualizacion sobre un vuelo
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Detalles del Vuelo");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 300);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 1)); // Una columna y filas automáticas

            JLabel label1 = new JLabel("ACTUALIZACION");
            label1.setBackground(Color.RED); // Cambia el color de fondo a rojo
            label1.setOpaque(true); // Esto es necesario para que el color de fondo sea visible
            panel.add(label1);

            JLabel label = new JLabel("Detalles del Vuelo:");
            label.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(label);

            panel.add(new JLabel("ID: " + vuelo.getId()));
            panel.add(new JLabel("Destino: " + vuelo.getDestino()));
            panel.add(new JLabel("Horario de salida: " + vuelo.getHorarioSalida()));
            panel.add(new JLabel("Estado de vuelo: " + vuelo.getEstadoDeVuelo()));
            panel.add(new JLabel("Tipo de vuelo: " + vuelo.getTipoVuelo()));
            //         panel.add(new JLabel("Asientos Económicos disponibles: " + vuelo.getAsientosEconomicos()));
            //      panel.add(new JLabel("Asientos de Negocios disponibles: " + vuelo.getAsientosNegocios()));
            //   panel.add(new JLabel("Asientos de Primera disponibles: " + vuelo.getAsientosPrimera()));

            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}



