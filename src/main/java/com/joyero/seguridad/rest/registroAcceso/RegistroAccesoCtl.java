package com.joyero.seguridad.rest.registroAcceso;

import com.joyero.base.jsf.Controlador;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "session")
public class RegistroAccesoCtl extends Controlador<RegistroAcceso, Long> {

    @Override
    public void nuevo() {
        entidad = new RegistroAcceso();
    }

    @Override
    public void onLoad() {
        super.onLoad();


    }

    @Override
    public void iniciarTbl() {

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
