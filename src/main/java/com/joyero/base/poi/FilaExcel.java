package com.joyero.base.poi;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eduardo
 */
public class FilaExcel {

    private int numeroFila;

    private Map<Integer, Object> columnas;

    public FilaExcel(int numeroFila) {
        this.numeroFila = numeroFila;
        columnas = new HashMap<>();
    }

    public void putValue(int numeroColumna, Object valor) {
        columnas.put(numeroColumna, valor);
    }

    public String getValue(int numeroColumna) {
        if (columnas.get(numeroColumna) != null) {
            return columnas.get(numeroColumna).toString();
        }
        return "";
    }

}
