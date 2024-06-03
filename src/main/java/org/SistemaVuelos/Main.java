package org.SistemaVuelos;

import org.SistemaVuelos.gestores.GestorReservas;
import org.SistemaVuelos.gestores.GestorVuelos;
import org.SistemaVuelos.menus.MenuPrincipal;

public class Main {
    public static void main(String[] args) {

        MenuPrincipal menuPrincipal = new MenuPrincipal();

        menuPrincipal.iniciar();
    }
}