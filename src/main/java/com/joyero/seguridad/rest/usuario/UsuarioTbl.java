package com.joyero.seguridad.rest.usuario;

import com.joyero.base.jsf.TableModelEntidad;
import com.joyero.base.rest.ApiRest;

public class UsuarioTbl extends TableModelEntidad<Usuario, Long> {
    public UsuarioTbl(ApiRest<Usuario, Long> apiRest) {
        super(apiRest);
    }
}
