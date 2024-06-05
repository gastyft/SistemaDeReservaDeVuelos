package org.SistemaVuelos;


import org.SistemaVuelos.menus.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
//TODO El codigo esta basado en que las cargas de datos no se realizan con SWING por eso las cargas se encuentran en
        //TODO los gestores de las clases.
        MenuPrincipal menuPrincipal = new MenuPrincipal();

        menuPrincipal.iniciar(); //Inicio del programa
    }
}