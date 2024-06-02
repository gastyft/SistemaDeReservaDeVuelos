package org.SistemaVuelos.gestores;

import org.SistemaVuelos.exceptions.PasajeroNoEncontradoException;
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
    this.gestorPasajeros= gestorPasajeros;
    this.gestorVuelos= gestorVuelos;
    }
    public void agregar() throws VueloNoEncontradoException, PasajeroNoEncontradoException {
        try {
            System.out.println("Ingrese vuelo a reservar"); //Ver como adicionar la disponibilidad
            Vuelo vuelo = gestorVuelos.buscarUnVuelo();
            //Validar asientos disponibles
            //Si hay, elegir uno y bajarle -1  a su disponibilidad

            System.out.println("Ingrese pasaporte del pasajero");
            Pasajero pasajeroReserva = gestorPasajeros.buscarUnPasajeroID();
        }catch(Exception e){
            throw new RuntimeException("Error en agregar");
        }

    }
}
