package com.joyero.seguridad.rest.sistema;

import com.joyero.base.jsf.TableModelEntidad;
import com.joyero.base.rest.ApiRest;

public class SistemaTbl extends TableModelEntidad<Sistema, Long> {
    public SistemaTbl(ApiRest<Sistema, Long> apiRest) {
        super(apiRest);
    }
}
