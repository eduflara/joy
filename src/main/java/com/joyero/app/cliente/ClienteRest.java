package com.joyero.app.cliente;

import com.joyero.base.rest.ApiRest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteRest extends ApiRest<Cliente, Long> {

    @Override
    public String getUrlBase() {
        return getUrlRest() + "cliente";
    }


    @Override
    public ParameterizedTypeReference getListType() {
        return new ParameterizedTypeReference<List<Cliente>>() {
        };
    }

}