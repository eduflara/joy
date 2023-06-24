package com.joyero.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Gema {
    BLANCO("blanco", "Blanco");

    private String codigo;
    private String descripcion;

    Gema(String codigo, String descripcion) {
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

    public static List<Gema> dameGemas() {
        return new ArrayList<>(Arrays.asList(Gema.values()));
    }

}