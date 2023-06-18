package com.joyero.seguridad.rest.registroAcceso;

import com.joyero.base.jsf.TableModelEntidad;
import com.joyero.base.rest.ApiRest;

public class RegistroAccesoTbl extends TableModelEntidad<RegistroAcceso, Long> {

    public RegistroAccesoTbl(ApiRest<RegistroAcceso, Long> apiRest) {
        super(apiRest);
    }
}
