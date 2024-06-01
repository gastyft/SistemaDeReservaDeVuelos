package org.SistemaVuelos.model;

import java.util.Objects;

public class Pasajero {

    private  final String id;
    private  static int autoId=4500000;

    private String nombreCompleto;

    public Pasajero(String nombreCompleto) {
        this.id ="PP"+autoId++;
        this.nombreCompleto = nombreCompleto;
    }

    public String getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pasajero pasajeros)) return false;
        return getId() == pasajeros.getId() && Objects.equals(getNombreCompleto(), pasajeros.getNombreCompleto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNombreCompleto());
    }

    @Override
    public String toString() {
        return "\n----------------------------------------\n" +
                "\nId: " + id +
                "\nNombre completo='" + nombreCompleto;
    }
}
