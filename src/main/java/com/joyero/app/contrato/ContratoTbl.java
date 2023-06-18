package com.joyero.app.contrato;

import com.joyero.app.cliente.Cliente;
import com.joyero.base.jsf.TableModelEntidad;
import com.joyero.base.rest.ApiRest;

public class ContratoTbl extends TableModelEntidad<Contrato, Long> {

    public ContratoTbl(ApiRest<Contrato, Long> apiRest) {
        super(apiRest);
    }

}