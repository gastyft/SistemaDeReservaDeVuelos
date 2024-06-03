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

import java.util.List;
import java.util.Scanner;
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

    public TipoAsiento cargaTipoAsiento() {
        boolean cargo = false;
        TipoAsiento tipoAsiento = null;
        while (!cargo) {
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
        try {
            gestorPasajeros.mostrarPasajeros();

            System.out.println("Ingrese pasaporte de pasajero a reservar");
            Pasajero pasajero = gestorPasajeros.buscarUnPasajeroID();
            if (pasajero == null) {
                throw new PasajeroNoEncontradoException("Pasajero no encontrado");
            }
            gestorVuelos.mostrarVuelos();
            System.out.println("Ingrese vuelo a reservar");
            Vuelo vuelo = gestorVuelos.buscarUnVuelo();
            if (vuelo == null) {
                throw new VueloNoEncontradoException("Vuelo no encontrado");
            }

            // Validar asientos disponibles
            if (vuelo.isDisponible()) {
                System.out.println("\n\nHay Asientos Economicos " + vuelo.getCantAsientosE());
                System.out.println("Hay Asientos Negocios " + vuelo.getCantAsientosNeg());
                System.out.println("Hay Asientos Primera " + vuelo.getCantAsientosPri());
                // Disminuir la disponibilidad del vuelo segun tipo de asiento

                System.out.println("Elija que tipo de asiento quiere");
                TipoAsiento tipoAsiento = cargaTipoAsiento();
                if (tipoAsiento != null) {

                    tipoAsientoDisponible(tipoAsiento, vuelo);


                    if (vuelo.getCantAsientosE() == 0 && vuelo.getCantAsientosNeg() == 0 && vuelo.getCantAsientosPri() == 0)
                        vuelo.setDisponible(false);
                } else {
                    throw new ReservaNoEncontradaException("Error en tipo de asiento");
                }


                Reserva reservaNueva = new Reserva(pasajero, vuelo, tipoAsiento);
                gestorCRUD.agregar(reservaNueva, reservaNueva.getId());
                System.out.println("Reserva agregada");
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

    public void mostrarReservas() {
        try {
            gestorCRUD.mostrar();
        } catch (NullPointerException e) {
            System.out.println("Error al mostrar");
        }
    }

    public List<Reserva> buscarReservasPorVuelo() throws VueloNoEncontradoException, ReservaNoEncontradaException {
        Vuelo vueloABuscar = gestorVuelos.buscarUnVuelo();
        if (vueloABuscar == null) {
            throw new VueloNoEncontradoException("Vuelo no encontrado");
        }

        List<Reserva> reservasFiltradas = gestorCRUD.getTreeMap().values().stream()
                .filter(reserva -> reserva.getVueloReserva().getId().equals(vueloABuscar.getId()))
                .toList();
        if (reservasFiltradas.isEmpty()) {
            throw new ReservaNoEncontradaException("No se encontraron reservas asociadas a ese vuelo");
        } else {
            reservasFiltradas.forEach(System.out::println);
        }

        return reservasFiltradas;
    }

    public List<Reserva> buscarReservasPorPasajero() throws PasajeroNoEncontradoException, ReservaNoEncontradaException {
        Pasajero pasajeroABuscar = gestorPasajeros.buscarUnPasajeroID();
        if (pasajeroABuscar == null) {
            throw new PasajeroNoEncontradoException("Pasajero no encontrado");
        }
        List<Reserva> reservasFiltradas = gestorCRUD.getTreeMap().values().stream()
                .filter(reserva -> reserva.getPasajeroReserva().getId().equals(pasajeroABuscar.getId()))
                .toList();
        if (reservasFiltradas.isEmpty()) {
            throw new ReservaNoEncontradaException("No se encontraron reservas asociadas a ese vuelo");
        } else {
            reservasFiltradas.forEach(System.out::println);
        }
        return reservasFiltradas;
    }

    public Reserva buscarUnaReservaPorID() throws ReservaNoEncontradaException { //busca y devuelve un pasajero
        Reserva reserva = null;
        System.out.println("Ingrese Id de la reserva");
        String idABuscar = scanner.nextLine().toUpperCase();
        try {
            reserva = gestorCRUD.getTreeMap().get(idABuscar);
            if (reserva != null) {
                System.out.println("Reserva encontrada");
                System.out.println(reserva); // Imprimir detalles del pasajero
            } else {
                throw new ReservaNoEncontradaException("No se encontró ningún pasajero con el pasaporte: " + idABuscar);
            }
        } catch (NullPointerException e) {
            System.out.println("El TreeMap no está inicializado o está vacío.");
        } catch (Exception e) {
            System.out.println("Error en la búsqueda: " + e.getMessage());
        }
        return reserva;
    }

    public void modificarReserva() throws PasajeroNoEncontradoException, ReservaNoEncontradaException, VueloNoEncontradoException, AsientoNoDisponibleException {
        System.out.println("Modificar una reserva");
        TipoAsiento tipoAsiento = null;
        try {

            Reserva reserva = buscarUnaReservaPorID();
            if (reserva == null)
                throw new ReservaNoEncontradaException("No se encontro la reserva buscada para modificar");

            Pasajero pasajero = gestorPasajeros.buscarUnPasajeroID();
            if (pasajero == null)
                throw new PasajeroNoEncontradoException("No se encontro el pasajero para modificar");

            Vuelo vuelo = gestorVuelos.buscarUnVuelo();
            if (vuelo == null)
                throw new VueloNoEncontradoException("No se encontro el vuelo buscado para modificar");

            if (vuelo.isDisponible()) {
                System.out.println("\n\nHay Asientos Economicos " + vuelo.getCantAsientosE());
                System.out.println("Hay Asientos Negocios " + vuelo.getCantAsientosNeg());
                System.out.println("Hay Asientos Primera " + vuelo.getCantAsientosPri());
                // Disminuir la disponibilidad del vuelo segun tipo de asiento

                System.out.println("Elija que tipo de asiento quiere");
                tipoAsiento = cargaTipoAsiento();
                if (tipoAsiento != null) {
                    tipoAsientoDisponible(tipoAsiento, vuelo);
                    if (vuelo.getCantAsientosE() == 0 && vuelo.getCantAsientosNeg() == 0 && vuelo.getCantAsientosPri() == 0)
                        vuelo.setDisponible(false);
                } else {
                    throw new AsientoNoDisponibleException("Error en tipo de asiento");
                }
            } else throw new AsientoNoDisponibleException("No hay asientos disponibles");
            System.out.println("Quiere cancelar la reserva? Ingrese 1 para si, cualquier tecla para no");
            Scanner scanner1 = new Scanner(System.in);
            String can = scanner1.nextLine();
            if (can.equals("1")) cancelarReserva(reserva);
            reserva.setPasajeroReserva(pasajero);
            reserva.setVueloReserva(vuelo);
            reserva.setTipoAsientoPasajero(tipoAsiento);
            System.out.println("Reserva modificada\n" + reserva);
        } catch (Exception e) {
            System.out.println("Error al modificar una reserva");
        }
    }

    public void cancelarReserva(Reserva reserva) throws ReservaNoEncontradaException {
        try {
            if (reserva == null) throw new ReservaNoEncontradaException(" ");
            if (!reserva.isActivo()) {
                System.out.println("Esta reserva ya se encontraba dada de baja");
            } else {
                reserva.setActivo(false);
                // sumar el asiento que va a quedar disponible
                if (reserva.getTipoAsientoPasajero().equals(TipoAsiento.ECONOMICA)) //Sumo mas 1 a asientos economica del vuelo asociado
                    reserva.getVueloReserva().setCantAsientosE(reserva.getVueloReserva().getCantAsientosE() + 1);
                if (reserva.getTipoAsientoPasajero().equals(TipoAsiento.NEGOCIOS)) //Sumo mas 1 a asientos negocios del vuelo asociado
                    reserva.getVueloReserva().setCantAsientosNeg(reserva.getVueloReserva().getCantAsientosNeg() + 1);
                if (reserva.getTipoAsientoPasajero().equals(TipoAsiento.PRIMERA))//Sumo mas 1 a asientos primera del vuelo asociado
                    reserva.getVueloReserva().setCantAsientosPri(reserva.getVueloReserva().getCantAsientosPri() + 1);
                System.out.println("Reserva cancelada");
            }
        } catch (Exception e) {
            System.out.println("No se encontro la reserva");
        }
    }

    public void darDeAltaReservaCancelada() throws ReservaNoEncontradaException, AsientoNoDisponibleException {
        try {
            Reserva reserva = buscarUnaReservaPorID();
            if (reserva == null) throw new ReservaNoEncontradaException("No se encontro la reserva solicitada");
            if (reserva.getVueloReserva().isDisponible()) {
                if (reserva.getTipoAsientoPasajero() != null) {
                    try {
                        tipoAsientoDisponible(reserva.getTipoAsientoPasajero(), reserva.getVueloReserva());
                    } catch (AsientoNoDisponibleException e) {
                        System.out.println("El tipo de asiento reservado anteriormente no está disponible. Se asignará otro tipo de asiento si está disponible.");
                        // Puedes agregar aquí la lógica para asignar otro tipo de asiento disponible
                        TipoAsiento nuevoTipoAsiento = seleccionarOtroTipoAsientoDisponible(reserva.getVueloReserva());

                        // Verifica si se encontró otro tipo de asiento disponible
                        if (nuevoTipoAsiento != null) {
                            try {
                                // Intenta asignar el nuevo tipo de asiento disponible
                                tipoAsientoDisponible(nuevoTipoAsiento, reserva.getVueloReserva());
                                System.out.println("Se ha asignado un nuevo tipo de asiento: " + nuevoTipoAsiento);
                                reserva.setTipoAsientoPasajero(nuevoTipoAsiento);
                            } catch (AsientoNoDisponibleException ex) {

                                System.out.println("No hay más tipos de asientos disponibles.");
                            }
                        } else {
                           throw  new AsientoNoDisponibleException("No hay más tipos de asientos disponibles.");
                        }
                    }
                    if (reserva.getVueloReserva().getCantAsientosE() == 0 && reserva.getVueloReserva().getCantAsientosNeg() == 0 && reserva.getVueloReserva().getCantAsientosPri() == 0)
                        reserva.getVueloReserva().setDisponible(false);
                } else {
                    throw new AsientoNoDisponibleException("Error en tipo de asiento");
                }
            } else throw new AsientoNoDisponibleException("No hay asientos disponibles");
            reserva.setActivo(true);

        } catch (Exception e) {
            System.out.println("Error en dar de alta");
        }
    }
    private TipoAsiento seleccionarOtroTipoAsientoDisponible(Vuelo vuelo) {
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

    public void eliminar()throws ReservaNoEncontradaException {
        mostrarReservas();
        Reserva reservaAEliminar = buscarUnaReservaPorID();
        if (reservaAEliminar != null) {
            gestorCRUD.eliminar(reservaAEliminar, reservaAEliminar.getId()); // Su funcion muestra si se borro o no exitosamente
        } else throw  new ReservaNoEncontradaException("No se pudo eliminar la reserva");
    }



    }



}