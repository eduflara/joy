package com.joyero.app.lote;

import com.joyero.base.jsf.TableModelEntidad;
import com.joyero.base.rest.ApiRest;

public class LoteTbl extends TableModelEntidad<Lote, Long> {

    public LoteTbl(ApiRest<Lote, Long> apiRest) {
        super(apiRest);
    }

}