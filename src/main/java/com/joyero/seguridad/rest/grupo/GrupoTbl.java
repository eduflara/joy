package com.joyero.seguridad.rest.grupo;

import com.joyero.base.jsf.TableModelEntidad;
import com.joyero.base.rest.ApiRest;

public class GrupoTbl extends TableModelEntidad<Grupo, Long> {
    public GrupoTbl(ApiRest<Grupo, Long> apiRest) {
        super(apiRest);
    }
}
