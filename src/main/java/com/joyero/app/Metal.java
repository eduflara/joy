package com.joyero.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Metal {
    BLANCO("blanco", "Blanco");

    private String codigo;
    private String descripcion;

    Metal(String codigo, String descripcion) {
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

    public static List<Metal> dameMetales() {
        return new ArrayList<>(Arrays.asList(Metal.values()));
    }

}