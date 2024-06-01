package org.SistemaVuelos.gestores;

public interface CRUD<T> {
     void agregar(T objeto,String idObj);

    public void modificar(String idModificar,T obj);
    public void eliminar(T obj, String idAEliminar);
    public T buscar(String idABuscar); //Ver como buscar. por igualdad de atr o hash
    public void mostrar();


}
