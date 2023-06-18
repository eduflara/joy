package com.joyero.seguridad.rest.funcion;

import com.joyero.base.jsf.TableModelEntidad;
import com.joyero.base.rest.ApiRest;

public class FuncionTbl extends TableModelEntidad<Funcion, Long> {

    public FuncionTbl(ApiRest<Funcion, Long> apiRest) {
        super(apiRest);
    }
}
