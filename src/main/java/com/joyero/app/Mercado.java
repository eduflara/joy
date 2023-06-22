package com.joyero.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Mercado {
    MERCADOSECUNDARIO("mercadosecundario", "Mercado Secundario");

    private String codigo;
    private String descripcion;

    Mercado(String codigo, String descripcion) {
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

    public static List<Mercado> getMercados(){
        return new ArrayList<>(Arrays.asList(Mercado.values()));
    }

    public static Mercado getMercado(String codigo) {
        for (Mercado aux:getMercados()) {
            if (aux.getCodigo().equals(codigo)) {
                return aux;
            }
        }
        return null;
    }
}