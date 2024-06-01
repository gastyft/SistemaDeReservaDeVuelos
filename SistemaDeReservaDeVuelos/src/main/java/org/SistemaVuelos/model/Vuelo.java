package org.SistemaVuelos.model;

import org.SistemaVuelos.enums.Destinos;
import org.SistemaVuelos.enums.Estado;
import org.SistemaVuelos.enums.TipoDeVuelo;

import java.time.LocalDateTime;
import java.util.Objects;

public class Vuelo {
    private final String id;
    private  static int autoId=3000;
    private Destinos destino;
    private LocalDateTime horarioSalida;
    private Estado estadoDeVuelo;
    private TipoDeVuelo tipoVuelo;

    public Vuelo(Destinos destino, LocalDateTime horarioSalida, Estado estadoDeVuelo, TipoDeVuelo tipoVuelo, String id) {
        this.destino = destino;
        this.horarioSalida = horarioSalida;
        this.estadoDeVuelo = estadoDeVuelo;
        this.tipoVuelo = tipoVuelo;
        this.id = id+autoId++; //PASA SEGUN TIPO DE VUELO SU DENOMINACION MAS UN AUTOINCREMENTAL CUALQUIERA
    }

    public String getId() {
        return id;
    }

    public Destinos getDestino() {
        return destino;
    }

    public void setDestino(Destinos destino) {
        this.destino = destino;
    }

    public LocalDateTime getHorarioSalida() {
        return horarioSalida;
    }

    public void setHorarioSalida(LocalDateTime horarioSalida) {
        this.horarioSalida = horarioSalida;
    }

    public Estado getEstadoDeVuelo() {
        return estadoDeVuelo;
    }

    public void setEstadoDeVuelo(Estado estadoDeVuelo) {
        this.estadoDeVuelo = estadoDeVuelo;
    }

    public TipoDeVuelo getTipoVuelo() {
        return tipoVuelo;
    }

    public void setTipoVuelo(TipoDeVuelo tipoVuelo) {
        this.tipoVuelo = tipoVuelo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vuelo vuelos)) return false;
        return Objects.equals(getId(), vuelos.getId()) && Objects.equals(getDestino(), vuelos.getDestino()) && Objects.equals(getHorarioSalida(), vuelos.getHorarioSalida()) && getEstadoDeVuelo() == vuelos.getEstadoDeVuelo() && getTipoVuelo() == vuelos.getTipoVuelo();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDestino(), getHorarioSalida(), getEstadoDeVuelo(), getTipoVuelo());
    }

    @Override
    public String toString() {
        return "\n----------------------------\n" +
                "\nId: "+id +
                "\nDestino: " + destino.getNombre() +
                "\nHorario de salida: " + horarioSalida +
                "\nEstado de vuelo: " + estadoDeVuelo +
                "\nTipo de vuelo: " + tipoVuelo;
    }
}
