package com.joyero.seguridad.rest.usuario;

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
public class UsuarioRest extends ApiRest<Usuario, Long> {

    @Override
    public String getUrlBase() {
        return getUrlRest() + "seg/usuario";
    }


    public Usuario findByUsername(String username, String password) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        //NO SE PUEDE USAR TODAVIA PORQUE NO SE HA COMPLETADO EL LOGIN
        //JsfUtils.putToSession("btoa", base64Creds);
        //JsfUtils.getFromSession("btoa");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Usuario> request = new HttpEntity<>(headers);

        ResponseEntity<Usuario> response = restTemplate.exchange(getUrlBase() + "/username/" + username, HttpMethod.GET, request, type);
        Usuario usuario = response.getBody();

        return usuario;
    }


    public Boolean login(String username) {
        HttpHeaders headers = new HttpHeaders();

        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Boolean> request = new HttpEntity<>(headers);

        String url = getUrlRest().replace("app", "login") + username;
        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, request, Boolean.class);
        Boolean autorizado = response.getBody();

//        E entidad = restTemplate.getForObject(getUrlId(), type, params);

        return autorizado;
    }

    @Override
    public ParameterizedTypeReference getListType() {
        return new ParameterizedTypeReference<List<Usuario>>() {
        };
    }

    public Usuario findByIdBuzon(Long idBuzon) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic ");

        Map<String, String> params = new HashMap<>();

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Usuario> request = new HttpEntity<>(headers);

        ResponseEntity<Usuario> response = restTemplate.exchange(getUrlBase() + "/idBuzon/" + idBuzon, HttpMethod.GET, request, type);
        Usuario usuario = response.getBody();

        return usuario;
    }
}
