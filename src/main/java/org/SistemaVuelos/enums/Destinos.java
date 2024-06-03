package org.SistemaVuelos.enums;

public enum Destinos {

    MAR_DEL_PLATA("Mar del Plata", TipoDeVuelo.NACIONAL),
    BUENOS_AIRES("Buenos Aires", TipoDeVuelo.NACIONAL),
    MENDOZA("Mendoza", TipoDeVuelo.NACIONAL),
    PARIS("Paris", TipoDeVuelo.INTERNACIONAL),
    LONDON("Londres", TipoDeVuelo.INTERNACIONAL),
    NEW_YORK("Nueva York", TipoDeVuelo.INTERNACIONAL),
    TOKYO("Tokio", TipoDeVuelo.INTERNACIONAL),
    SYDNEY("Sydney", TipoDeVuelo.INTERNACIONAL);

    private final String nombre;
    private final TipoDeVuelo tipo;

    Destinos(String nombre, TipoDeVuelo tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoDeVuelo getTipo() {
        return tipo;
    }
}
