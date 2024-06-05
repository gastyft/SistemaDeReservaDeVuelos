package org.SistemaVuelos.gestores;


import org.SistemaVuelos.model.Pasajero;
import org.SistemaVuelos.model.Vuelo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class GestorCRUD<T> implements CRUD<T> {
    //Gestor de funcionalidad principal sin carga de datos por usuario

    Map<String, T> treeMap;

    public GestorCRUD() {
        treeMap = new TreeMap<>();
    } //Inicializo el treeMap

    public void agregar(T objeto, String idObj) { //Agrega un objeto al treeMap
        if (objeto != null && idObj != null) {
            treeMap.put(idObj, objeto);
        } else {
            System.out.println("No se pudo agregar el elemento: clave o valor nulos");
        }
    }

    public T buscar(String idABuscar) { //busca un objeto del treeMap
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

    public void modificar(String idModificar, T obj) { //Modifica un objeto del treeMap pasando su clave y su valor
        if (idModificar != null && obj != null) {
            treeMap.put(idModificar, obj);
        } else System.out.println("Error al modificar");
    }

    public void modificarId(String idAntiguo, String nuevoId) { //Solo lo uso en Vuelo Si se modifica el destino de
        //nacional a internacional o viceversa para asignarle la nueva letra
        if (treeMap.containsKey(idAntiguo)) {
            // Extraer el objeto del TreeMap con el ID antiguo
            T objetoModificado = treeMap.remove(idAntiguo);
            // Insertar el objeto con el nuevo ID en el TreeMap
            treeMap.put(nuevoId, objetoModificado);
        } else {
            System.out.println("No se encontró ningún objeto con el ID " + idAntiguo);
        }
    }

    @Override
    public void eliminar(T obj, String idAEliminar) { //Elimina un objeto del treeMap pasando su clave y su valor
        if (obj != null && idAEliminar != null) {
            boolean borrado = treeMap.remove(idAEliminar, obj);
            if (!borrado) {
                System.out.println("Error al eliminar: no se encontró el par clave-valor");
            } else System.out.println("Eliminado con exito");
        } else System.out.println("Error al eliminar");
    }

    @Override
    public void mostrar() { //Muestra todos los objetos del treemap
        //Con System.out y con Swing
        if (treeMap.isEmpty()) {
            System.out.println("El TreeMap está vacío.");
        } else {
            treeMap.forEach((key, value) -> System.out.println(value));
            imprimirTreeMapEnSwing((TreeMap<String, T>) treeMap); //Llamada al metodo para imprimir un treeMap con Swing
        }
    }

    public Map<String, T> getTreeMap() {
        return treeMap;
    }
    //Para obtener el treeMap y poder recorrerlo en las clases para hacer busquedas


    public void imprimirTreeMapEnSwing(TreeMap<String, T> treeMap1) { //Metodo de muestreo por Swing

        SwingUtilities.invokeLater(() -> {
            // Determinar el nombre de la clase del primer valor en el TreeMap
            String frameTitle = "TreeMap";
            if (!treeMap1.isEmpty()) {
                T firstValue = treeMap1.firstEntry().getValue();
                if (firstValue != null) {
                    frameTitle = firstValue.getClass().getSimpleName();
                }
            }
            JFrame frame = new JFrame("LISTA DE "+frameTitle.toUpperCase()+"S");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 400);

            DefaultTableModel tableModel = new DefaultTableModel();

            // Obtén el primer valor para determinar las columnas
            if (!treeMap1.isEmpty()) {
                T firstValue = treeMap1.entrySet().iterator().next().getValue();
                if (firstValue != null) {
                    for (Field field : firstValue.getClass().getDeclaredFields()) {
                        // Añade todas las columnas excepto autoId
                        if (!field.getName().equals("autoId") && !field.getName().equals("caracterParaId")) {
                            tableModel.addColumn(field.getName());
                        }
                    }
                }
            }

            // Llena la tabla
            for (Map.Entry<String, T> entry : treeMap1.entrySet()) {
                Object[] rowData = new Object[tableModel.getColumnCount()];
                T value = entry.getValue();
                if (value != null) {
                    int i = 0;
                    for (Field field : value.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        try {
                            // Omitir autoId y caracterParaId
                            if (!field.getName().equals("autoId") && !field.getName().equals("caracterParaId")) {
                                if (field.getName().equals("pasajeroReserva")) {
                                    // Obtener el campo id del objeto Pasajero
                                    Pasajero pasajero = (Pasajero) field.get(value);
                                    if (pasajero != null) {
                                        Field pasajeroIdField = pasajero.getClass().getDeclaredField("id");
                                        pasajeroIdField.setAccessible(true);
                                        rowData[i++] = pasajeroIdField.get(pasajero);
                                    }
                                } else if (field.getName().equals("vueloReserva")) {
                                    // Obtener el campo id del objeto Vuelo
                                    Vuelo vuelo = (Vuelo) field.get(value);
                                    if (vuelo != null) {
                                        Field vueloIdField = vuelo.getClass().getDeclaredField("id");
                                        vueloIdField.setAccessible(true);
                                        rowData[i++] = vueloIdField.get(vuelo);
                                    }
                                } else {
                                    rowData[i++] = field.get(value);
                                }
                            }
                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    }
                }
                tableModel.addRow(rowData);
            }

            JTable table = new JTable(tableModel);
            table.setGridColor(Color.LIGHT_GRAY); // Cambia el color de las líneas de la tabla
            table.setFont(new Font("Arial", Font.PLAIN, 12)); // Cambia la fuente de la tabla

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Elimina el borde predeterminado del JScrollPane

            // Personaliza los encabezados de columna
            JTableHeader header = table.getTableHeader();
            header.setBackground(Color.DARK_GRAY); // Cambia el color de fondo de los encabezados de columna
            header.setForeground(Color.WHITE); // Cambia el color del texto de los encabezados de columna
            header.setFont(new Font("Arial", Font.BOLD, 14)); // Cambia la fuente de los encabezados de columna

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}








