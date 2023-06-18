package com.joyero.seguridad.rest.funcion;

import com.joyero.base.rest.ApiRest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class FuncionRest extends ApiRest<Funcion, Long> {

    @Override
    public String getUrlBase() {
        return getUrlRest() + "seg/funcion";
    }


    public List<Funcion> cargarFuncionesSuperiores() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBase64Creds());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<Funcion>> request = new HttpEntity<>(headers);
        ResponseEntity<List<Funcion>> response = restTemplate.exchange(getUrlBase() + "/superiores", HttpMethod.GET, request, getListType());
        return response.getBody();
    }

    public List<Funcion> cargarFuncionesInferiores(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBase64Creds());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<Funcion>> request = new HttpEntity<>(headers);
        ResponseEntity<List<Funcion>> response = restTemplate.exchange(getUrlBase() + "/inferiores/" + id, HttpMethod.GET, request, getListType());
        return response.getBody();
    }

    @Override
    public ParameterizedTypeReference getListType() {
        return new ParameterizedTypeReference<List<Funcion>>() {
        };
    }
}
