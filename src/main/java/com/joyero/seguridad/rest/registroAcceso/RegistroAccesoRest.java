package com.joyero.seguridad.rest.registroAcceso;

import com.joyero.base.rest.ApiRest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistroAccesoRest extends ApiRest<RegistroAcceso, Long> {

    @Override
    public String getUrlBase() {
        return getUrlRest() + "seg/registroAcceso";
    }

    @Override
    public ParameterizedTypeReference getListType() {
        return new ParameterizedTypeReference<List<RegistroAcceso>>() {
        };
    }
}
