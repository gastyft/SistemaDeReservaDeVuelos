package org.SistemaVuelos.model;

import org.SistemaVuelos.enums.TipoAsiento;

import java.util.Objects;

public class Reserva {


    private final String id;
    private static int autoId = 8000;
    private Pasajero pasajeroReserva;
    private Vuelo vueloReserva;
    private TipoAsiento tipoAsientoPasajero;

    public Reserva(String idR, Pasajero pasajeroReserva, Vuelo vueloReserva, TipoAsiento tipoAsientoPasajero,String id) {
        this.id = idR+autoId++;
        this.pasajeroReserva = pasajeroReserva;
        this.vueloReserva = vueloReserva;
        this.tipoAsientoPasajero = tipoAsientoPasajero;
    }

    public String getId() {
        return id;
    }

    public static int getAutoId() {
        return autoId;
    }

    public static void setAutoId(int autoId) {
        Reserva.autoId = autoId;
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
                "\n Vuelo de la Reserva: " + vueloReserva +
                "\n Tipo de asiento del Pasajero: " + tipoAsientoPasajero;
    }
}
