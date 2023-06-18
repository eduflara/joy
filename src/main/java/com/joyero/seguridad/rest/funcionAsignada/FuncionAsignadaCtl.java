package com.joyero.seguridad.rest.funcionAsignada;


import com.joyero.base.jsf.Controlador;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "session")
public class FuncionAsignadaCtl extends Controlador<FuncionAsignada, Long> {


    @Override
    public void nuevo() {
        entidad = new FuncionAsignada();
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
