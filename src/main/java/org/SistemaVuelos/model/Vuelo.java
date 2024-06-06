package org.SistemaVuelos.model;

import org.SistemaVuelos.enums.Destinos;
import org.SistemaVuelos.enums.Estado;
import org.SistemaVuelos.enums.TipoDeVuelo;

import java.time.LocalDateTime;
import java.util.Objects;

public class Vuelo {
    private  String id;
    private  static int autoId=3000;
    private Destinos destino;
    private LocalDateTime horarioSalida;
    private Estado estadoDeVuelo;
    private TipoDeVuelo tipoVuelo;
    private String caracterParaId;

    //Para las cantidades de asientos por vuelos deberia ser un tipo de avion unico
    //pero deberia variar segun Tipo de avion donde  tiene su propia capacidad (Seria un nuevo enum)
    private int cantAsientosE= 2; //60
    private int cantAsientosNeg = 1; //30
    private int cantAsientosPri= 1; //20
    private boolean disponible;

    public Vuelo(Destinos destino, LocalDateTime horarioSalida, Estado estadoDeVuelo, TipoDeVuelo tipoVuelo, String id) {
        this.destino = destino;
        this.horarioSalida = horarioSalida;
        this.estadoDeVuelo = estadoDeVuelo;
        this.tipoVuelo = tipoVuelo;
        this.caracterParaId=id;
        this.id = caracterParaId+autoId++; //PASA SEGUN TIPO DE VUELO SU DENOMINACION MAS UN AUTOINCREMENTAL CUALQUIERA
    this.disponible=true;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setCaracterParaId(String caracterParaId) {
        this.caracterParaId = caracterParaId;
    }

    public int getCantAsientosE() {
        return cantAsientosE;
    }

    public void setCantAsientosE(int cantAsientosE) {
        this.cantAsientosE = cantAsientosE;
    }

    public int getCantAsientosNeg() {
        return cantAsientosNeg;
    }

    public void setCantAsientosNeg(int cantAsientosNeg) {
        this.cantAsientosNeg = cantAsientosNeg;
    }

    public int getCantAsientosPri() {
        return cantAsientosPri;
    }

    public void setCantAsientosPri(int cantAsientosPri) {
        this.cantAsientosPri = cantAsientosPri;
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

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
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
                "\nTipo de vuelo: " + tipoVuelo+
                "\nCantidad asientos Economica disponibles "+cantAsientosE+
                "\nCantidad asientos Negocios disponibles "+cantAsientosNeg+
                "\nCantidad asientos Primera disponibles "+cantAsientosPri;
    }

}
