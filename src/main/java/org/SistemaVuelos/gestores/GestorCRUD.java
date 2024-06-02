package org.SistemaVuelos.gestores;

import java.util.Map;
import java.util.TreeMap;

public class GestorCRUD<T> implements CRUD<T> {
    //Gestor de funcionalidad principal sin carga de datos por usuario

    Map<String, T> treeMap;

    public GestorCRUD() {
        treeMap = new TreeMap<>();
    }

    public void agregar(T objeto, String idObj) {
        if (objeto != null && idObj != null) {
            treeMap.put(idObj, objeto);
        } else {
            System.out.println("No se pudo agregar el elemento: clave o valor nulos");
        }
    }

    public T buscar(String idABuscar) {
        if (idABuscar != null) {
            T obj = treeMap.get(idABuscar);
            if (obj == null) {
                System.out.println("No se encontró el objeto");
            }
            return obj;
        } else {
            System.out.println("La clave de búsqueda es nula");
            return null;
        }
    }

    public void modificar(String idModificar, T obj) {
        if (idModificar != null && obj != null) {
            treeMap.put(idModificar, obj);
        } else System.out.println("Error al modificar");
    }


    @Override
    public void eliminar(T obj, String idAEliminar) {
        if (obj != null && idAEliminar != null) {
            boolean borrado = treeMap.remove(idAEliminar, obj);
            if (!borrado) {
                System.out.println("Error al eliminar: no se encontró el par clave-valor");
            }
            else System.out.println("Eliminado con exito");
        } else System.out.println("Error al eliminar");
    }

    @Override
    public void mostrar() {
        if (treeMap.isEmpty()) {
            System.out.println("El TreeMap está vacío.");
        } else {
            treeMap.forEach((key, value) -> System.out.println(value));
        }
    }

    public Map<String, T> getTreeMap() {
        return treeMap;
    }
}



