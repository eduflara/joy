package com.joyero.seguridad.rest.grupo;

import com.joyero.base.rest.ApiRest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GrupoRest extends ApiRest<Grupo, Long> {

    @Override
    public String getUrlBase() {
        return getUrlRest() + "seg/grupo";
    }


    public Grupo get(String nombre) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBase64Creds());

        Map<String, String> params = new HashMap<>();
        params.put("nombre", nombre);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Grupo> request = new HttpEntity<>(headers);

        ResponseEntity<Grupo> response = restTemplate.exchange(getUrlBase() + "/nombre/" + nombre, HttpMethod.GET, request, type);
        Grupo entidad = response.getBody();

//        E entidad = restTemplate.getForObject(getUrlId(), type, params);

        return entidad;

    }

    public List<Grupo> findByUsuario(Long idUsuario, String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        // headers.add("Authorization", "Basic " + getBase64Creds());

        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        headers.add("Authorization", "Basic " + base64Creds);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<Grupo>> request = new HttpEntity<>(headers);
        ResponseEntity<List<Grupo>> response = restTemplate.exchange(getUrlBase() + "/usuario/" + idUsuario, HttpMethod.GET, request, getListType());
        List<Grupo> result = response.getBody();
        return result;
    }

    public List<Grupo> findByUsuario(Long idUsuario) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBase64Creds());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<Grupo>> request = new HttpEntity<>(headers);
        ResponseEntity<List<Grupo>> response = restTemplate.exchange(getUrlBase() + "/usuario/" + idUsuario, HttpMethod.GET, request, getListType());
        List<Grupo> result = response.getBody();
        return result;
    }

    @Override
    public ParameterizedTypeReference getListType() {
        return new ParameterizedTypeReference<List<Grupo>>() {
        };
    }
}
