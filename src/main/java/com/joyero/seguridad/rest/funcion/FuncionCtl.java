package com.joyero.seguridad.rest.funcion;

import com.joyero.base.jsf.Controlador;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@Scope(value = "session")
public class FuncionCtl extends Controlador<Funcion, Long> {


    @Override
    public void nuevo() {
        entidad = new Funcion();
    }

    @Override
    public void onLoad() {
        super.onLoad();


    }

    @Override
    @PostConstruct
    public void iniciarTbl() {
        tablemodel = new FuncionTbl(apiRest);
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
