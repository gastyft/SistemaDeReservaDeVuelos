package org.SistemaVuelos.gestores;

import org.SistemaVuelos.enums.Destinos;
import org.SistemaVuelos.enums.TipoAsiento;
import org.SistemaVuelos.exceptions.AsientoNoDisponibleException;
import org.SistemaVuelos.exceptions.PasajeroNoEncontradoException;
import org.SistemaVuelos.exceptions.ReservaNoEncontradaException;
import org.SistemaVuelos.exceptions.VueloNoEncontradoException;
import org.SistemaVuelos.model.Pasajero;
import org.SistemaVuelos.model.Reserva;
import org.SistemaVuelos.model.Vuelo;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class GestorReservas { //GESTOR DE CARGA DE DATOS

    GestorCRUD<Reserva> gestorCRUD = new GestorCRUD<Reserva>();
    Scanner scanner = new Scanner(System.in);
    GestorVuelos gestorVuelos;
    GestorPasajeros gestorPasajeros;

    public GestorReservas(GestorPasajeros gestorPasajeros, GestorVuelos gestorVuelos) {
        this.gestorPasajeros = gestorPasajeros;
        this.gestorVuelos = gestorVuelos;
    }

    public TipoAsiento cargaTipoAsiento() { //Cargo un asiento validando que este en el enum
        boolean cargo = false;
        TipoAsiento tipoAsiento = null;
        while (!cargo) { // Uso while teniendo en cuenta que no realizo cargas con SWING
            System.out.println("\nECONOMICA\nNEGOCIOS\nPRIMERA");
            System.out.println("Ingrese el nombre del tipo de asiento de la lista: ");
            Scanner scanner = new Scanner(System.in);
            String nombretipoAsiento = scanner.nextLine().toUpperCase();
            // Buscar el tipoAsiento correspondiente al nombre ingresado
            for (TipoAsiento t : TipoAsiento.values()) {
                if (t.name().equalsIgnoreCase(nombretipoAsiento)) {
                    tipoAsiento = t;
                    break;
                }
            }
            if (tipoAsiento != null) {
                cargo = true;
                System.out.println("Has seleccionado el tipo de asiento: " + tipoAsiento.name());
            } else {
                System.out.println("El tipo de asiento ingresado no está en la lista de tipo de asientos disponibles.");
            }
        }
        return tipoAsiento;
    }

    public void tipoAsientoDisponible(TipoAsiento elegido, Vuelo vuelo) throws AsientoNoDisponibleException, ReservaNoEncontradaException {
        //Verifico si hay asientos disponibles de cada tipo sino lanzo excepcion de no hay lugar
        //      boolean disp= true;
        switch (elegido) {
            case ECONOMICA:
                if (vuelo.getCantAsientosE() > 0) {
                    vuelo.setCantAsientosE(vuelo.getCantAsientosE() - 1);
                } else {
                    throw new AsientoNoDisponibleException("El tipo de asiento Económico está lleno");
                }
                break;
            case NEGOCIOS:
                if (vuelo.getCantAsientosNeg() > 0) {
                    vuelo.setCantAsientosNeg(vuelo.getCantAsientosNeg() - 1);
                } else {
                    throw new AsientoNoDisponibleException("El tipo de asiento Negocios está lleno");
                }
                break;
            case PRIMERA:
                if (vuelo.getCantAsientosPri() > 0) {
                    vuelo.setCantAsientosPri(vuelo.getCantAsientosPri() - 1);
                } else {
                    throw new AsientoNoDisponibleException("El tipo de asiento Primera está lleno");
                }
                break;
            default:
                throw new ReservaNoEncontradaException("Tipo de asiento inválido");
        }
        //  return disp;
    }

    public void agregarReserva() throws VueloNoEncontradoException, PasajeroNoEncontradoException, ReservaNoEncontradaException {
        //Agregar Reserva
        try {
            gestorPasajeros.mostrarPasajeros(); //Muestreo de pasajeros para ver el Id(Pasaporte) a cargar

            System.out.println("Ingrese pasaporte de pasajero a reservar");
            Pasajero pasajero = gestorPasajeros.buscarUnPasajeroID();//Metodo buscar por id de gestor Pasajeros
            if (pasajero == null) { //Si es null lanza excepcion
                throw new PasajeroNoEncontradoException("Pasajero no encontrado");
            }
            gestorVuelos.mostrarVuelos(); //Se muestran los vuelos
            System.out.println("Ingrese vuelo a reservar");
            Vuelo vuelo = gestorVuelos.buscarUnVuelo(); //Se llama al metodo buscar un vuelo(es por ID) de gestor vuelos
            if (vuelo == null) { // si el metodo anterior de busqueda devuelve null entonces lanza excepcion
                throw new VueloNoEncontradoException("Vuelo no encontrado");
            }

            // Validar asientos disponibles
            if (vuelo.isDisponible()) { //Si el vuelo tiene asientos de alguna clase disponible muestra
                //cuales tienen asientos libres y cuales tienen 0
                System.out.println("\n\nHay Asientos Economicos " + vuelo.getCantAsientosE());
                System.out.println("Hay Asientos Negocios " + vuelo.getCantAsientosNeg());
                System.out.println("Hay Asientos Primera " + vuelo.getCantAsientosPri());
                // Disminuir la disponibilidad del vuelo segun tipo de asiento

                System.out.println("Elija que tipo de asiento quiere");
                TipoAsiento tipoAsiento = cargaTipoAsiento();
                if (tipoAsiento != null) {
                    //Se corrobora que el asiento este disponible y se resta uno a su disponibilidad de ese asiento elegido
                    tipoAsientoDisponible(tipoAsiento, vuelo);

                    //Caso de que no queden asientos disponibles entonces se setea la disponibilidad de asientos a false
                    if (vuelo.getCantAsientosE() == 0 && vuelo.getCantAsientosNeg() == 0 && vuelo.getCantAsientosPri() == 0)
                        vuelo.setDisponible(false);
                } else { //Si el tipo de asiento devuelto es null, lanza excepcion
                    throw new ReservaNoEncontradaException("Error en tipo de asiento");
                }

                //Se instancia una reserva nueva con los objetos obtenidos anteriormente
                Reserva reservaNueva = new Reserva(pasajero, vuelo, tipoAsiento);
                gestorCRUD.agregar(reservaNueva, reservaNueva.getId()); //Se agrega al treeMap
                System.out.println("Reserva agregada");
                imprimirPantallaDetallesReserva(reservaNueva, "0");//Imprime con Swing la nueva reserva agregada
            } else {
                throw new ReservaNoEncontradaException("No hay asientos disponibles en este vuelo");
            }
        } catch (VueloNoEncontradoException e) {
            throw new VueloNoEncontradoException("Vuelo no encontrado: " + e.getMessage());
        } catch (PasajeroNoEncontradoException e) {
            throw new PasajeroNoEncontradoException("Pasajero no encontrado: " + e.getMessage());
        } catch (Exception e) {
            throw new ReservaNoEncontradaException("Error en agregar Reserva: " + e.getMessage());
        }
    }

    public void mostrarReservas() { //Muestra reservas
        try {
            gestorCRUD.mostrar(); //Llamada al metodo mostrar del gestorCRUD
        } catch (NullPointerException e) {
            System.out.println("Error al mostrar");
        }
    }

    public List<Reserva> buscarReservasPorVuelo() throws VueloNoEncontradoException, ReservaNoEncontradaException {
        //Busqueda de reservas por vuelo que devuelve una listas
        Vuelo vueloABuscar = gestorVuelos.buscarUnVuelo();
        if (vueloABuscar == null) {
            throw new VueloNoEncontradoException("Vuelo no encontrado");
        }
        TreeMap<String, Reserva> reservaTreeMap = new TreeMap<>();
        List<Reserva> reservasFiltradas = gestorCRUD.getTreeMap().values().stream()
                .filter(reserva -> reserva.getVueloReserva().getId().equals(vueloABuscar.getId()))
                .toList(); //Obtencion de la lista de reservas filtradas por vuelos

        if (reservasFiltradas.isEmpty()) { //Si esta vacia lanza excepcion
            throw new ReservaNoEncontradaException("No se encontraron reservas asociadas a ese vuelo");
        } else {
            for (Reserva reserva : reservasFiltradas) { //Para imprimir con Swing lo agrego a un treemap
                reservaTreeMap.put(reserva.getId(), reserva);
            }
            reservasFiltradas.forEach(System.out::println); //Imprimo la lista en consola
            gestorCRUD.imprimirTreeMapEnSwing(reservaTreeMap);//Imprimo el treemap con swing llamandolo del gestorCRUD
        }

        return reservasFiltradas; //Devuelvo lista
    }

    public List<Reserva> buscarReservasPorPasajero() throws PasajeroNoEncontradoException, ReservaNoEncontradaException {
        //Busqueda de reservas por pasajero
        Pasajero pasajeroABuscar = gestorPasajeros.buscarUnPasajeroID();//Llamada a buscar por id de gestor pasajeros
        if (pasajeroABuscar == null) { //Si es null lo que devuelve el metodo entonces lanza excepcion
            throw new PasajeroNoEncontradoException("Pasajero no encontrado");
        }
        TreeMap<String, Reserva> reservaTreeMap = new TreeMap<>(); //Crea treemap para mostrar por Swing
        List<Reserva> reservasFiltradas = gestorCRUD.getTreeMap().values().stream()
                .filter(reserva -> reserva.getPasajeroReserva().getId().equals(pasajeroABuscar.getId()))
                .toList(); //Filtra lista de reservas por pasajeros
        if (reservasFiltradas.isEmpty()) { //Si esta vacia la lista lanza excepcion
            throw new ReservaNoEncontradaException("No se encontraron reservas asociadas a ese vuelo");
        } else {
            for (Reserva reserva : reservasFiltradas) { //Para mostrar con Swing
                reservaTreeMap.put(reserva.getId(), reserva);
            }
            reservasFiltradas.forEach(System.out::println); // muestreo en consola
            gestorCRUD.imprimirTreeMapEnSwing(reservaTreeMap); //Llamada a imprimir con Swing
        }

        return reservasFiltradas;
    }


    public Reserva buscarUnaReservaPorID() throws ReservaNoEncontradaException { //busca y devuelve un pasajero
        //Busqueda de reserva por ID
        Reserva reserva = null;
        System.out.println("Ingrese Id de la reserva");
        String idABuscar = scanner.nextLine().toUpperCase();
        try {
            reserva = gestorCRUD.getTreeMap().get(idABuscar); //Busqueda por clave ID unico del treeMap
            if (reserva != null) { //Si se encuentra se muestra
                System.out.println("Reserva encontrada");
                System.out.println(reserva); // Imprimir detalles del pasajero
            } else {//Excepcion si es null
                throw new ReservaNoEncontradaException("No se encontró ningún pasajero con el pasaporte: " + idABuscar);
            }
        } catch (NullPointerException e) {
            System.out.println("El TreeMap no está inicializado o está vacío.");
        } catch (Exception e) {
            System.out.println("Error en la búsqueda: " + e.getMessage());
        }
        return reserva; //Se devuelve si se hallo
    }

    public void modificarReserva() throws PasajeroNoEncontradoException, ReservaNoEncontradaException, VueloNoEncontradoException, AsientoNoDisponibleException {
        //Modificar una reserva
        System.out.println("Modificar una reserva");
        TipoAsiento tipoAsiento = null;
        try {
            Reserva reserva = buscarUnaReservaPorID();//Busqueda de reserva por ID
            if (reserva == null) //Si es null lanza excepcion
                throw new ReservaNoEncontradaException("No se encontro la reserva buscada para modificar");
            //Si halla reserva se sigue por pasajero
            Pasajero pasajero = gestorPasajeros.buscarUnPasajeroID(); //Se busca pasajero por id desde gestor Pasajeros
            if (pasajero == null)//Si es null lanza excepcion
                throw new PasajeroNoEncontradoException("No se encontro el pasajero para modificar");
            //Si no es null se sigue por vuelo
            Vuelo vuelo = gestorVuelos.buscarUnVuelo();//Busca un vuelo desde gestor de vuelos
            if (vuelo == null) //Si es null lanza excepcion
                throw new VueloNoEncontradoException("No se encontro el vuelo buscado para modificar");

            if (vuelo.isDisponible()) { //Se muestran los asientos disponibles si se quiere cambiar el tipo de asiento
                System.out.println("\n\nHay Asientos Economicos " + vuelo.getCantAsientosE());
                System.out.println("Hay Asientos Negocios " + vuelo.getCantAsientosNeg());
                System.out.println("Hay Asientos Primera " + vuelo.getCantAsientosPri());
                // Por como lo ejecuto Sumo uno a la disponibilidad del asiento segun tipo de asiento
                //Porque abajo vuelvo a pedir un nuevo asiento y vuelvo a restar la disponibilidad
                //Entonces asi asi recupero el asiento por mas que elija el mismo
                switch (reserva.getTipoAsientoPasajero()) {
                    case ECONOMICA:
                        vuelo.setCantAsientosE(vuelo.getCantAsientosE() + 1);
                        break;
                    case NEGOCIOS:
                        vuelo.setCantAsientosNeg(vuelo.getCantAsientosNeg() + 1);
                        break;
                    case PRIMERA:
                        vuelo.setCantAsientosPri(vuelo.getCantAsientosPri() + 1);
                        break;
                    default:
                        System.out.println("Tipo de asiento inválido.");
                        break;
                }
                System.out.println("Elija que tipo de asiento quiere");
                tipoAsiento = cargaTipoAsiento(); //Se carga un nuevo asiento
                if (tipoAsiento != null) {  //Si hay asiento se disminuye su cantidad en -1 segun el tipo de asiento que elija
                    tipoAsientoDisponible(tipoAsiento, vuelo);
                    //Si la disponibilidad de todos los asientos llega a 0 entonces se setea Disponible en false
                    if (vuelo.getCantAsientosE() == 0 && vuelo.getCantAsientosNeg() == 0 && vuelo.getCantAsientosPri() == 0)
                        vuelo.setDisponible(false);
                } else {
                    throw new AsientoNoDisponibleException("Error en tipo de asiento");
                }
            } else throw new AsientoNoDisponibleException("No hay asientos disponibles");
            //Dentro de la modificacion de la reserva se puede cancelar tambien
            System.out.println("Quiere cancelar la reserva? Ingrese 1 para si, cualquier tecla para no");
            Scanner scanner1 = new Scanner(System.in);
            String can = scanner1.nextLine();
            if (can.equals("1")) cancelarReserva(reserva); //Llamo a un metodo cancelar
            reserva.setPasajeroReserva(pasajero); //Seteo cambios anteriores
            reserva.setVueloReserva(vuelo); //Seteo cambios anteriores
            reserva.setTipoAsientoPasajero(tipoAsiento); //Seteo cambios anteriores
            System.out.println("Reserva modificada\n" + reserva); //Muestreo por consola
            imprimirPantallaDetallesReserva(reserva, "1"); //muestreo por Swing
        } catch (Exception e) {
            System.out.println("Error al modificar una reserva");
        }
    }

    public void cancelarReserva(Reserva reserva) throws ReservaNoEncontradaException {
        //Cancela una reserva que se pasa por parametro
        try {
            //Si es null se lanza excepcion
            if (reserva == null) throw new ReservaNoEncontradaException(" ");
            if (!reserva.isActivo()) { //Si estaba en false (Ya estaba cancelada) entonces se lanza excepcion
                throw new ReservaNoEncontradaException("Esta reserva ya se encontraba dada de baja");
            } else { //sino se setea la reserva activa a false
                reserva.setActivo(false);
                // sumar el asiento que va a quedar disponible
                if (reserva.getTipoAsientoPasajero().equals(TipoAsiento.ECONOMICA)) //Sumo mas 1 a asientos economica del vuelo asociado
                    reserva.getVueloReserva().setCantAsientosE(reserva.getVueloReserva().getCantAsientosE() + 1);
                if (reserva.getTipoAsientoPasajero().equals(TipoAsiento.NEGOCIOS)) //Sumo mas 1 a asientos negocios del vuelo asociado
                    reserva.getVueloReserva().setCantAsientosNeg(reserva.getVueloReserva().getCantAsientosNeg() + 1);
                if (reserva.getTipoAsientoPasajero().equals(TipoAsiento.PRIMERA))//Sumo mas 1 a asientos primera del vuelo asociado
                    reserva.getVueloReserva().setCantAsientosPri(reserva.getVueloReserva().getCantAsientosPri() + 1);
                System.out.println("Reserva cancelada");
                imprimirCancelarReserva(reserva);//Muestreo de cancelacion por SWING
            }
        } catch (Exception e) {
            System.out.println("No se encontro la reserva");
        }
    }

    public void darDeAltaReservaCancelada() throws ReservaNoEncontradaException, AsientoNoDisponibleException {
        //Da de alta una reserva que fue cancelada
        try {
            Reserva reserva = buscarUnaReservaPorID(); //Busqueda por id
            //Si el metodo devuelve null se lanza excepcion
            if (reserva == null) throw new ReservaNoEncontradaException("No se encontro la reserva solicitada");
            //Si hay asientos disponibles se puede asignar uno
            if (reserva.getVueloReserva().isDisponible()) {
                if (reserva.getTipoAsientoPasajero() != null) {
                    try { //Se valida que el tipo de asiento reservado cancelado este disponible
                        tipoAsientoDisponible(reserva.getTipoAsientoPasajero(), reserva.getVueloReserva());
                    } catch (
                            AsientoNoDisponibleException e) {//Si no se puede es que ya no hay mas lugares con ese asiento
                        //Si el asiento estaba lleno entonces dentro del catch se carga un nuevo asiento si es que hay disponibles
                        System.out.println("El tipo de asiento reservado anteriormente no está disponible. Se asignará otro tipo de asiento si está disponible.");
                        TipoAsiento nuevoTipoAsiento = seleccionarOtroTipoAsientoDisponible(reserva.getVueloReserva());
                        //Si hay otro asiento disponible se asigna automaticamente
                        // Verifica si se encontró otro tipo de asiento disponible
                        if (nuevoTipoAsiento != null) {
                            try {
                                //Se resta la disponibilidad de ese nuevo asiento o si habia
                                //lugar en el asiento original entonces
                                tipoAsientoDisponible(nuevoTipoAsiento, reserva.getVueloReserva());
                                System.out.println("Se ha asignado un nuevo tipo de asiento: " + nuevoTipoAsiento);
                                reserva.setTipoAsientoPasajero(nuevoTipoAsiento);
                            } catch (AsientoNoDisponibleException ex) {

                                System.out.println("No hay más tipos de asientos disponibles.");
                            }
                        } else { //Si estan todos ocupados de todos los tipos se lanza excepcion
                            throw new AsientoNoDisponibleException("No hay más tipos de asientos disponibles.");
                        }
                    } //Si se asigna un asiento y caso de que justo era el ultimo se setea la disponibilidad de asientos a false
                    if (reserva.getVueloReserva().getCantAsientosE() == 0 && reserva.getVueloReserva().getCantAsientosNeg() == 0 && reserva.getVueloReserva().getCantAsientosPri() == 0)
                        reserva.getVueloReserva().setDisponible(false);
                } else {
                    throw new AsientoNoDisponibleException("Error en tipo de asiento");
                }
            } else throw new AsientoNoDisponibleException("No hay asientos disponibles");
            reserva.setActivo(true); //Si se pudo cargar el asiento bien se setea la reserva a activa nuevamente
            imprimirActivarReservaCancelada(reserva);//Se imprime actualizacion con Swing
        } catch (Exception e) {
            System.out.println("Error en dar de alta");
        }
    }

    private TipoAsiento seleccionarOtroTipoAsientoDisponible(Vuelo vuelo) {
        //Si el asiento se encuentra lleno entonces se asigna uno nuevo al siguiente que este disponible
        if (vuelo.getCantAsientosE() > 0) {
            return TipoAsiento.ECONOMICA;
        } else if (vuelo.getCantAsientosNeg() > 0) {
            return TipoAsiento.NEGOCIOS;
        } else if (vuelo.getCantAsientosPri() > 0) {
            return TipoAsiento.PRIMERA;
        } else {
            return null; // No hay más tipos de asientos disponibles
        }
    }

    public Reserva eliminar() throws ReservaNoEncontradaException {//Elimina una reserva
        mostrarReservas();//Muestra reservas para ver el id que se vaya a querer eliminar
        Reserva reservaAEliminar = buscarUnaReservaPorID(); //Se busca con el id
        if (reservaAEliminar != null) { //Si se encuentra la reserva se elimina del treeMap
            gestorCRUD.eliminar(reservaAEliminar, reservaAEliminar.getId()); // Su funcion muestra si se borro o no exitosamente
            return reservaAEliminar; //Devuelvo para mostrar desde el menu
        } else throw new ReservaNoEncontradaException("No se pudo eliminar la reserva");//Si no se encuentra la reserva, lanza excepcion
    }


    public void imprimirPantallaDetallesReserva(Reserva reserva, String i) {
        //Muestreo de detalle de reserva con Swing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Detalles de la Reserva");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 300);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0, 1)); // Una columna y filas automáticas
            JLabel label1;
            if (i.equals("1"))
                label1 = new JLabel("ACTUALIZACION"); //Caso actualizacion de la reserva
            else if (i.equals("0"))
                label1 = new JLabel("RESERVA AGREGADA"); //Caso agregar nueva reserva
            else label1 = new JLabel("RESERVA ENCONTRADA"); //Caso en busqueda
            label1.setBackground(Color.RED); // Cambia el color de fondo a rojo
            label1.setOpaque(true); // Esto es necesario para que el color de fondo sea visible
            panel.add(label1);

            JLabel label = new JLabel("Detalles de la Reserva:");
            label.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(label);

            panel.add(new JLabel("ID: " + reserva.getId()));
            panel.add(new JLabel("Pasajero: " + reserva.getPasajeroReserva().getNombreCompleto()));
            panel.add(new JLabel("Vuelo: " + reserva.getVueloReserva().getId()));
            panel.add(new JLabel("Destino del vuelo: " + reserva.getVueloReserva().getDestino()));
            panel.add(new JLabel("Horario de salida: " + reserva.getVueloReserva().getHorarioSalida()));
            panel.add(new JLabel("Estado del vuelo: " + reserva.getVueloReserva().getEstadoDeVuelo()));
            panel.add(new JLabel("Tipo de vuelo: " + reserva.getVueloReserva().getTipoVuelo()));
            panel.add(new JLabel("Tipo de asiento: " + reserva.getTipoAsientoPasajero()));
            panel.add(new JLabel("Estado de la reserva: " + (reserva.isActivo() ? "Activa" : "Dada de baja")));
                            //Ternario para ver si la reserva esta activa o cancelada
            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

    }

    public void imprimirEliminarReserva(Reserva reserva) {
        //Cartelito de eliminacion
        SwingUtilities.invokeLater(() -> {
            String message = "La reserva " + reserva.getId() + " ha sido eliminada \u2705"; // Emoji de marca de verificación
            JOptionPane.showMessageDialog(null, message, "Eliminado", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public void imprimirCancelarReserva(Reserva reserva) {
        //Cartelito de eliminacion
        SwingUtilities.invokeLater(() -> {
            String message = "La reserva " + reserva.getId() + " ha sido cancelada \uD83D\uDEAB"; // Emoji de prohibición
            JOptionPane.showMessageDialog(null, message, "Cancelada", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public void imprimirActivarReservaCancelada(Reserva reserva) {
        //Cartelito de reactivacion de reserva
        SwingUtilities.invokeLater(() -> {
            String message = "La reserva " + reserva.getId() + " ha sido reactivada \uD83D\uDCAF"; // Emoji de 100 puntos
            JOptionPane.showMessageDialog(null, message, "Reactivada", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}