package com.joyero.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum TipoContrato {
    TASACION("tasacion", "Tasacion");

    private String codigo;
    private String descripcion;

    TipoContrato(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public static List<TipoContrato> getTipos(){
        return new ArrayList<>(Arrays.asList(TipoContrato.values()));
    }

    public static TipoContrato getTipoContrato(String codigo) {
        for (TipoContrato aux:getTipos()) {
            if (aux.getCodigo().equals(codigo)) {
                return aux;
            }
        }
        return null;
    }
}
