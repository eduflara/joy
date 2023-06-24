package com.joyero.app.lote;

import com.joyero.base.rest.ApiRest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoteRest extends ApiRest<Lote, Long> {

    @Override
    public String getUrlBase() {
        return getUrlRest() + "lote";
    }


    @Override
    public ParameterizedTypeReference getListType() {
        return new ParameterizedTypeReference<List<Lote>>() {
        };
    }

}