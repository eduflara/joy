package com.joyero.app.contrato;

import com.joyero.app.cliente.Cliente;
import com.joyero.base.rest.ApiRest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContratoRest extends ApiRest<Contrato, Long> {

    @Override
    public String getUrlBase() {
        return getUrlRest() + "contrato";
    }


    @Override
    public ParameterizedTypeReference getListType() {
        return new ParameterizedTypeReference<List<Contrato>>() {
        };
    }

}