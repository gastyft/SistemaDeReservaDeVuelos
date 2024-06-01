package org.SistemaVuelos.gestores;

import org.SistemaVuelos.enums.Destinos;
import org.SistemaVuelos.enums.Estado;
import org.SistemaVuelos.enums.TipoDeVuelo;
import org.SistemaVuelos.model.Vuelo;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class GestorVuelos {
    GestorCRUD<Vuelo> gestorCRUD = new GestorCRUD<Vuelo>();
    Scanner scanner = new Scanner(System.in);

    public void listaDestinos() { //muestro los destinos posibles de los vuelos
        System.out.println("Lista de destinos disponibles:");
        for (Destinos destino : Destinos.values()) {
            System.out.println(destino.getNombre());
        }
    }

    //AGREGAR
    public LocalDateTime cargaHorarioSalida() {

        LocalDateTime horarioSalida = null;
        int hora = 0;
        int min = 0;
        int anio = 0;
        int mes = 0;
        int dia = 0;
        boolean fechaValida = false;
        while (!fechaValida) {
            try {
                System.out.println("Ingrese horario de salida ");
                System.out.println("Ingrese hora");
                hora = scanner.nextInt();
                System.out.println("Ingrese minutos");
                min = scanner.nextInt();
                System.out.println("Ingrese año");
                anio = scanner.nextInt();
                System.out.println("Ingrese mes");
                mes = scanner.nextInt();
                System.out.println("Ingrese día");
                dia = scanner.nextInt();
                // Validar la fecha ingresada
                horarioSalida = LocalDateTime.of(anio, mes, dia, hora, min);

                // Verificar si la fecha es igual o mayor que la fecha actual
                if (!horarioSalida.toLocalDate().isBefore(LocalDate.now())) {
                    fechaValida = true;
                } else {
                    System.out.println("La fecha ingresada debe ser igual o mayor que la fecha de hoy.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número válido.");
                scanner.next(); // Limpiar el buffer del scanner para evitar un bucle infinito
            } catch (DateTimeException e) {
                System.out.println("La fecha y hora ingresadas no son válidas. Por favor, inténtelo de nuevo.");
            }
        }
        return horarioSalida;
    }
 /*   public TipoDeVuelo cargaTipoVuelo() { //Sub funcion que ahorro por agregar en el enum Destinos el tipo de vuelo

        System.out.println("Seleccione el tipo de vuelo:");
        for (TipoDeVuelo tipo : TipoDeVuelo.values()) {
            System.out.println((tipo.ordinal() + 1) + ". " + tipo.name());
        }
        int opcionSeleccionada;
        do {
            System.out.println("Ingrese el número correspondiente al tipo de vuelo:");
            opcionSeleccionada = scanner.nextInt();
        } while (opcionSeleccionada < 1 || opcionSeleccionada > TipoDeVuelo.values().length);
        TipoDeVuelo tipoDeVuelo = TipoDeVuelo.values()[opcionSeleccionada - 1];
        return tipoDeVuelo;
    }
    */


    public Destinos cargaDestino() { //Todo EN EL ENUM DESTINO LE AGREGO EL TIPO SI ES NACIONAL O INTERNACIONAL
        //Todo PARA AHORRAR CODIGO Y OBTENERLO MAS LIMPIO
        listaDestinos();
        boolean cargo = false;
        Destinos destino = null;
        while (!cargo) {
            System.out.println("Ingrese el nombre del destino de la lista: ");
            Scanner scanner = new Scanner(System.in);
            String nombreDestino = scanner.nextLine().toUpperCase();
            // Buscar el destino correspondiente al nombre ingresado

            for (Destinos d : Destinos.values()) {
                if (d.getNombre().equalsIgnoreCase(nombreDestino)) {
                    destino = d;
                    break;
                }
            }
            if (destino != null) {
                cargo = true;
                System.out.println("Has seleccionado el destino: " + destino.getNombre());
                // Aquí puedes hacer algo más con el destino seleccionado, como agregarlo a un vuelo
            } else {
                System.out.println("El destino ingresado no está en la lista de destinos disponibles.");
            }
        }
        return destino;
    }

    public void agregarVuelo() {//TODO FUNCION AGREGAR CON SUBFUNCIONES PARA DESCOMPRIMIR Y QUE NO QUEDE UN METODO MUY EXTENSO
        String tipoID = "D";
        try {
            Destinos destino = cargaDestino();
            TipoDeVuelo tipoDeVuelo = destino.getTipo();

            if (tipoDeVuelo != null) {  //SEGUN TIPO DE VUELO SERA SU DENOMINACION DE ID DE VUELO
                // SI ES NACIONAL SE PASA N O SI ES INTERNACIONAL SE PASARA I Y POR DEFECTO D en case de que falle
                if (tipoDeVuelo.equals(TipoDeVuelo.NACIONAL)) {
                    // Si es igual a NACIONAL
                    tipoID = "N";
                } else if (tipoDeVuelo.equals(TipoDeVuelo.INTERNACIONAL)) {
                    // Si es igual a INTERNACIONAL
                    tipoID = "I";
                }
                Vuelo vuelo = new Vuelo(destino, cargaHorarioSalida(), Estado.PROGRAMADO, destino.getTipo(), tipoID);
                gestorCRUD.agregar(vuelo, vuelo.getId()); //Agrego en gestor generico
            } else System.out.println("ERROR carga vuelo");
        } catch (Exception e) {
            System.out.println("Error en agregar el vuelo: " + e.getMessage());
        }
    }

    public void mostrarVuelos() { //Muestra todos los vuelos
        try {
            gestorCRUD.mostrar();
        } catch (NullPointerException e) {
            System.out.println("Error al mostrar");
        }
    }

    public void buscarPorDestinos(String destinoABuscar) { //Busca por destino
        boolean encontrado = false;
        //Todo llamo Map.entry y creo dos variables vuelo y destino para no perder la generacidad en GestorCRUD
        for (Map.Entry<String, Vuelo> entry : gestorCRUD.getTreeMap().entrySet()) {
            Destinos destino = entry.getValue().getDestino(); // entry.getValue() es el objeto Vuelo
            if (destino != null && destino.getNombre().equalsIgnoreCase(destinoABuscar)) {
                System.out.println(entry.getValue());
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("No se encontraron vuelos para el destino: " + destinoABuscar);
        }
    }

    public Vuelo buscarUnVuelo(String numVuelo){ //busca y devuelve un vuelo
        Vuelo vuelo=null;
        try {
            vuelo = gestorCRUD.getTreeMap().get(numVuelo);
            if (vuelo != null) {
                System.out.println("Vuelo encontrado");
                System.out.println(vuelo); // Imprimir detalles del vuelo
            } else {
                System.out.println("No se encontró ningún vuelo con el número: " + numVuelo);
            }
        } catch (NullPointerException e) {
            System.out.println("El TreeMap no está inicializado o está vacío.");
        } catch (Exception e) {
            System.out.println("Error en la búsqueda: " + e.getMessage());
        }
        return vuelo;
    }

public void eliminar(String numVuelo){
    mostrarVuelos();
    Vuelo vueloAEliminar =buscarUnVuelo(numVuelo); // Vuelo vueloAEliminar = gestorCRUD.getTreeMap().get(numVuelo);
    if(vueloAEliminar!= null) {
        gestorCRUD.eliminar(vueloAEliminar, vueloAEliminar.getId()); // Su funcion muestra si se borro o no exitosamente
    }
    else System.out.println("No se pudo eliminar el objeto");
}

public void cambiarEstado(){


}
}
