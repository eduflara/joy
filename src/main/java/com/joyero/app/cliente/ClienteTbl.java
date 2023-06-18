package com.joyero.app.cliente;

import com.joyero.base.jsf.TableModelEntidad;
import com.joyero.base.rest.ApiRest;

public class ClienteTbl extends TableModelEntidad<Cliente, Long> {

    public ClienteTbl(ApiRest<Cliente, Long> apiRest) {
        super(apiRest);
    }

}