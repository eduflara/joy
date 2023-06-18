package com.joyero.seguridad.rest.sistema;

import com.joyero.base.jsf.Controlador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@Scope(value = "session")
public class SistemaCtl extends Controlador<Sistema, Long> {

    @Autowired
    private SistemaRest rest;

    @Override
    public void nuevo() {
        entidad = new Sistema();
    }

    @Override
    public void onLoad() {
        super.onLoad();


    }

    @Override
    @PostConstruct
    public void iniciarTbl() {
        tablemodel = new SistemaTbl(rest);
    }

    @Override
    public void postCargarSeleccion() {

    }


    @Override
    public String verFormulario() {
        return null;
    }

    @Override
    public String verListado() {
        return null;
    }
}
