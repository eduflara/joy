package com.joyero.seguridad.rest.funcionAsignada;

import com.joyero.base.rest.ApiRest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class FuncionAsignadaRest extends ApiRest<FuncionAsignada, Long> {

    @Override
    public String getUrlBase() {
        return getUrlRest() + "seg/funcionAsignada";
    }

    public List<FuncionAsignada> findByGrupo(Long idGrupo, String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        //headers.add("Authorization", "Basic " + getBase64Creds());
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        headers.add("Authorization", "Basic " + base64Creds);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<FuncionAsignada>> request = new HttpEntity<>(headers);
        ResponseEntity<List<FuncionAsignada>> response = restTemplate.exchange(getUrlBase() + "/grupo/" + idGrupo, HttpMethod.GET, request, getListType());
        List<FuncionAsignada> result = response.getBody();
        return result;
    }

    public List<FuncionAsignada> findByGrupo(Long idGrupo) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBase64Creds());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<FuncionAsignada>> request = new HttpEntity<>(headers);
        ResponseEntity<List<FuncionAsignada>> response = restTemplate.exchange(getUrlBase() + "/grupo/" + idGrupo, HttpMethod.GET, request, getListType());
        List<FuncionAsignada> result = response.getBody();
        return result;
    }


    @Override
    public ParameterizedTypeReference getListType() {
        return new ParameterizedTypeReference<List<FuncionAsignada>>() {
        };
    }
}
