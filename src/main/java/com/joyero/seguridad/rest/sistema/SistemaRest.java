package com.joyero.seguridad.rest.sistema;

import com.joyero.base.rest.ApiRest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SistemaRest extends ApiRest<Sistema, Long> {

    @Override
    public String getUrlBase() {
        return getUrlRest() + "seg/sistema";
    }

    @Override
    public ParameterizedTypeReference getListType() {
        return new ParameterizedTypeReference<List<Sistema>>() {
        };
    }
}
