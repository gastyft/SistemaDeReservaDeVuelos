package org.SistemaVuelos.model;

import org.SistemaVuelos.enums.TipoAsiento;

import java.util.Objects;

public class Reserva {
    private final String id;
    private static int autoId = 8000;
    private Pasajero pasajeroReserva;
    private Vuelo vueloReserva;
    private TipoAsiento tipoAsientoPasajero;
    private boolean activo;

    public Reserva(Pasajero pasajeroReserva, Vuelo vueloReserva, TipoAsiento tipoAsientoPasajero) {
        this.id = "RR"+autoId++;
        this.pasajeroReserva = pasajeroReserva;
        this.vueloReserva = vueloReserva;
        this.tipoAsientoPasajero = tipoAsientoPasajero;
        this.activo =true;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public Pasajero getPasajeroReserva() {
        return pasajeroReserva;
    }

    public void setPasajeroReserva(Pasajero pasajeroReserva) {
        this.pasajeroReserva = pasajeroReserva;
    }

    public Vuelo getVueloReserva() {
        return vueloReserva;
    }

    public void setVueloReserva(Vuelo vueloReserva) {
        this.vueloReserva = vueloReserva;
    }

    public TipoAsiento getTipoAsientoPasajero() {
        return tipoAsientoPasajero;
    }

    public void setTipoAsientoPasajero(TipoAsiento tipoAsientoPasajero) {
        this.tipoAsientoPasajero = tipoAsientoPasajero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva reserva)) return false;
        return Objects.equals(getId(), reserva.getId()) && Objects.equals(getPasajeroReserva(), reserva.getPasajeroReserva()) && Objects.equals(getVueloReserva(), reserva.getVueloReserva()) && getTipoAsientoPasajero() == reserva.getTipoAsientoPasajero();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPasajeroReserva(), getVueloReserva(), getTipoAsientoPasajero());
    }

    @Override
    public String toString() {
        return "\n----------------------------------\n" +
                "\nId: " + id +
                "\nPasajero de la reserva: " + pasajeroReserva +
                "\nVuelo de la Reserva: " + vueloReserva +
                "\nTipo de asiento del Pasajero: " + tipoAsientoPasajero+
                "\nLa reserva esta "+(activo ? "Activa" : "Dada de baja");
    }


}
