package com.joyero.app.cliente;

import com.joyero.base.jsf.Controlador;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@Scope(value = "session")
public class ClienteCtl extends Controlador<Cliente, Long> {

    //region Clientes REST

    //endregion

    //region Atributos del controlador

    //endregion

    //region Métodos propios del controlador

    //endregion

    //region Implementar y sobrescribir métodos del controlador base
    @PostConstruct
    @Override
    public void iniciarTbl() {
        tablemodel = new ClienteTbl(apiRest);
    }

    @Override
    public String verFormulario() {
        return null;
    }

    @Override
    public String verListado() {
        return null;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void nuevo() {
        entidad = new Cliente();
    }

    @Override
    public void postCargarSeleccion() {
    }

    @Override
    public void preGuardar() {
        System.out.println(entidad);
    }

    @Override
    public void guardar() {
        super.guardar();
    }

    @Override
    public void postGuardar() {
    }

    @Override
    public void preEliminar() {
    }

    @Override
    public void postEliminar() {
    }
    //endregion

}