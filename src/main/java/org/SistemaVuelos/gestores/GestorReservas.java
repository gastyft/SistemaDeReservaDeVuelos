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

import java.util.Scanner;

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
public void tipoAsientoDisponible(TipoAsiento elegido,Vuelo vuelo) throws AsientoNoDisponibleException,ReservaNoEncontradaException{
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

            gestorVuelos.mostrarVuelos();
            System.out.println("Ingrese vuelo a reservar");
            Vuelo vuelo = gestorVuelos.buscarUnVuelo();


            // Validar asientos disponibles
            if (vuelo.isDisponible()) {
                System.out.println("\n\nHay Asientos Economicos "+vuelo.getCantAsientosE());
                System.out.println("Hay Asientos Negocios "+vuelo.getCantAsientosNeg());
                System.out.println("Hay Asientos Primera "+vuelo.getCantAsientosPri());
                // Disminuir la disponibilidad del vuelo segun tipo de asiento

                    System.out.println("Elija que tipo de asiento quiere");
                    TipoAsiento tipoAsiento = cargaTipoAsiento();
                    if (tipoAsiento != null) {

                         tipoAsientoDisponible(tipoAsiento,vuelo);


                        if(vuelo.getCantAsientosE()==0 && vuelo.getCantAsientosNeg()==0 && vuelo.getCantAsientosPri()==0)
                            vuelo.setDisponible(false);
                    } else {
                        throw new ReservaNoEncontradaException("Error en tipo de asiento");
                    }


                Reserva reservaNueva = new Reserva(pasajero,vuelo,tipoAsiento);
                gestorCRUD.agregar(reservaNueva,reservaNueva.getId());
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

    public void mostrarReservas()
    {
        try {
            gestorCRUD.mostrar();
        } catch (NullPointerException e) {
            System.out.println("Error al mostrar");
        }
    }

}
