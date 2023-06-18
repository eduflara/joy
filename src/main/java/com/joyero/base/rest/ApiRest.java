package com.joyero.base.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.joyero.base.jsf.Config;
import com.joyero.base.jsf.JsfUtils;
import com.joyero.base.jsf.MensajeErrorUsuario;
import com.joyero.base.util.exception.ResultadoErrorException;
import com.joyero.base.util.exception.ResultadoException;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class ApiRest<E extends Entidad, ID extends Serializable> {

    @SuppressWarnings("unchecked")
    protected Class<E> type = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Autowired
    protected Config config;

    /**
     * Devuelve el path de la entidad
     *
     * @return
     */
    public abstract String getUrlBase();

    public abstract ParameterizedTypeReference getListType();

    /**
     * Devuelve la ruta del servidor donde se encuentran los servicios REST
     *
     * @return
     */
    public String getUrlRest() {
        return config.getUrlRest();
    }

    /**
     * Devuelve la ruta del servicio REST para ejecutar una consulta paginada
     *
     * @return
     */
    public String getUrlFind() {
        return getUrlBase() + "/find";
    }

    /**
     * @return
     */
    public String getUrlFindAll() {
        return getUrlBase() + "/all";
    }

    /**
     * Devuelve la ruta del servicio REST para ejecutar un conteo
     *
     * @return
     */
    public String getUrlCount() {
        return getUrlBase() + "/count";
    }

    /**
     * @return
     */
    public String getUrlId() {
        return getUrlBase() + "/{id}";
    }

    public String getUrlId(Serializable id) {
        return getUrlBase() + "/" + id.toString();
    }

    /**
     * Recupera una entidad con el id especificado
     *
     * @param id
     * @return
     */
    public E get(Serializable id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBase64Creds());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<E> request = new HttpEntity<>(headers);

        ResponseEntity<E> response = restTemplate.exchange(getUrlId(id), HttpMethod.GET, request, type);
        return response.getBody();
    }

    /**
     * Envía un petición de crear una nueva entidad al servidor REST
     *
     * @return
     */
    public E save(E entidad) throws ResultadoException {
        ResultadoException resultadoException = new ResultadoException();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + getBase64Creds());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<E> request = new HttpEntity<>(entidad, headers);
            ResponseEntity<E> response = restTemplate.postForEntity(getUrlBase(), request, type);
            entidad = response.getBody();
            return entidad;
        } catch (HttpClientErrorException ex) {
            String response = ex.getResponseBodyAsString();
            ResponseErrorApi error = null;
            MensajeErrorUsuario mensajeErrorUsuario = new MensajeErrorUsuario("error.guardar", "com.joyero.i18n.validationMessages", ex);
            resultadoException.addError(mensajeErrorUsuario);

            try {
                ObjectMapper mapper = new ObjectMapper();
                error = mapper.readValue(response, ResponseErrorApi.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (error.getErrors() != null) {
                for (ErrorApi errorApi : error.getErrors()) {
                    MensajeErrorUsuario mensaje = new MensajeErrorUsuario(errorApi.getErrorMessage(), null, ex);
                    resultadoException.addError(mensaje);
                }
            } else {
                MensajeErrorUsuario mensaje = new MensajeErrorUsuario(error.getMessage(), null, ex);
                resultadoException.addError(mensaje);
            }
            throw new ResultadoErrorException(resultadoException);
        }
    }

    /**
     * idComposicion
     * Envía un petición de actualización de datos al servidor REST
     *
     * @return
     */
    public E update(E entidad) {
        ResultadoException resultadoException = new ResultadoException();
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + getBase64Creds());

            HttpEntity<E> request = new HttpEntity<>(entidad, headers);
            restTemplate.exchange(getUrlBase(), HttpMethod.PUT, request, Void.class);

            return get(entidad.getId());

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            String response = ex.getResponseBodyAsString();
            ResponseErrorApi error = null;
            MensajeErrorUsuario mensajeErrorUsuario = new MensajeErrorUsuario("error.actualizar", "com.joyero.i18n.validationMessages", ex);
            resultadoException.addError(mensajeErrorUsuario);

            try {
                ObjectMapper mapper = new ObjectMapper();
                error = mapper.readValue(response, ResponseErrorApi.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (error.getErrors() != null) {

                for (ErrorApi errorApi : error.getErrors()) {
                    MensajeErrorUsuario mensaje = new MensajeErrorUsuario(errorApi.getErrorMessage(), null, ex);
                    resultadoException.addError(mensaje);
                }
            } else {
                MensajeErrorUsuario mensaje = new MensajeErrorUsuario(error.getMessage(), null, ex);
                resultadoException.addError(mensaje);

            }
            throw new ResultadoErrorException(resultadoException);
        }
    }

    /**
     * Elimina una entidad
     *
     * @param entidad
     */
    public void delete(E entidad) {
        ResultadoException resultadoException = new ResultadoException();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + getBase64Creds());

//            Map<String, String> params = new HashMap<>();
//            params.put("id", "" + entidad.getId());

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<ObjectNode> request = new HttpEntity<>(headers);

            Long id = (Long) entidad.getId();

            ResponseEntity<ObjectNode> response = restTemplate.exchange(getUrlId(id), HttpMethod.DELETE, request, ObjectNode.class);
            ObjectNode e = response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            String response = ex.getResponseBodyAsString();
            ResponseErrorApi error = null;
            MensajeErrorUsuario mensajeErrorUsuario = new MensajeErrorUsuario("error.eliminar", "com.joyero.i18n.validationMessages", ex);
            resultadoException.addError(mensajeErrorUsuario);

            try {
                ObjectMapper mapper = new ObjectMapper();
                error = mapper.readValue(response, ResponseErrorApi.class);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (error.getErrors() != null) {
                for (ErrorApi errorApi : error.getErrors()) {
                    MensajeErrorUsuario mensaje = new MensajeErrorUsuario(errorApi.getErrorMessage(), null, ex);
                    resultadoException.addError(mensaje);
                }
            } else {
                MensajeErrorUsuario mensaje = new MensajeErrorUsuario(error.getMessage(), type.getPackage().getName(), ex);
                resultadoException.addError(mensaje);
            }
            throw new ResultadoErrorException(resultadoException);
        }
    }

    /**
     * Recupera TODOS los elementos
     *
     * @return
     */
    public List<E> getCollection() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBase64Creds());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<E>> request = new HttpEntity<>(headers);
        ResponseEntity<List<E>> response = restTemplate.exchange(getUrlFindAll(), HttpMethod.GET, request, getListType());
        return response.getBody();
    }

    /**
     * @param sortField Campo por el que se van a ordenar los elementos
     * @param sortOrder Orden ascendente o descente
     * @param filters   Map con los filtros que se van a aplicar en la búsqueda
     * @return
     */
    public List<E> getCollection(String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBase64Creds());

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUrlFind());
        generateUriFilter(filters, builder);

        if (StringUtils.isNotEmpty(sortField)) {

            StringBuilder sorter = new StringBuilder(sortField);
            if (SortOrder.DESCENDING.equals(sortOrder)) {
                sorter.append(",desc");
            } else {
                sorter.append(",asc");
            }

            builder.queryParam("sort", sorter.toString());
        }

        String uriString = builder.build(false).toUriString();

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<E>> request = new HttpEntity<>(headers);
        ResponseEntity<List<E>> response = restTemplate.exchange(uriString, HttpMethod.GET, request, getListType());
        return response.getBody();
    }

    /**
     * @param first     Elemnto a partir de cual se va a recuperar
     * @param pageSize  Cantidad total de elementos a recuperar
     * @param sortField Campo por el que se van a ordenar los elementos
     * @param sortOrder Orden ascendente o descente
     * @param filters   Map con los filtros que se van a aplicar en la búsqueda
     * @return
     */
    public List<E> getCollection(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        return getCollection(first, pageSize, sortField, sortOrder, filters, "");
    }

    /**
     * @param first     Elemnto a partir de cual se va a recuperar
     * @param pageSize  Cantidad total de elementos a recuperar
     * @param sortField Campo por el que se van a ordenar los elementos
     * @param sortOrder Orden ascendente o descente
     * @param filters   Map con los filtros que se van a aplicar en la búsqueda
     * @param url
     * @return
     */
    public List<E> getCollection(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBase64Creds());

        UriComponentsBuilder builder = null;
        if (StringUtils.isNotEmpty(url)) {
            builder = UriComponentsBuilder.fromUriString(getUrlBase() + url);
        } else {
            builder = UriComponentsBuilder.fromUriString(getUrlFind());
        }

        //TODO: QueryDSL sólo admite 1000 registros máximos en las consultas
        // con all forzamos a hacer una consulta unpaged
        if(pageSize > 1000) {
            builder.queryParam("all", 1);
        } else {
            builder.queryParam("page", first / pageSize + "");
            builder.queryParam("size", pageSize + "");
        }

        generateUriFilter(filters, builder);

        if (StringUtils.isNotEmpty(sortField)) {

            StringBuilder sorter = new StringBuilder(sortField);
            if (SortOrder.DESCENDING.equals(sortOrder)) {
                sorter.append(",desc");
            } else {
                sorter.append(",asc");
            }

            builder.queryParam("sort", sorter.toString());
        }

        String uriString = builder.build(false).toUriString();

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<List<E>> request = new HttpEntity<>(headers);
        ResponseEntity<List<E>> response = restTemplate.exchange(uriString, HttpMethod.GET, request, getListType());
        return response.getBody();
    }

    /**
     * @param filters
     * @return
     */
    public long getCount(Map<String, Object> filters) {
        return getCount(filters, "");
    }

    /**
     * @param filters
     * @param url
     * @return
     */
    public long getCount(Map<String, Object> filters, String url) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUrlCount() + url);
        generateUriFilter(filters, builder);

        String uriString = builder.build(false).toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + getBase64Creds());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Integer> request = new HttpEntity<>(headers);
        ResponseEntity<Integer> response = restTemplate.exchange(uriString, HttpMethod.GET, request, Integer.class);

        return response.getBody();

    }

    /**
     * @param filters Map con los filtros que se van a aplicar en la búsqueda
     * @param builder Builder para componer la URI
     */
    protected void generateUriFilter(Map<String, Object> filters, UriComponentsBuilder builder) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat dtf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            if (entry.getValue() instanceof Date) {
                Date date = (Date) entry.getValue();

                if (date.getHours() > 0 || date.getMinutes() > 0) {
                    builder.queryParam(entry.getKey(), dtf.format(entry.getValue()));
                } else {
                    builder.queryParam(entry.getKey(), df.format(entry.getValue()));
                }
            } else if (entry.getValue() instanceof Collection) {
                List<Object> values = (List<Object>) entry.getValue();
                boolean isDate = values.get(0) instanceof Date;

                for (Object value : values) {
                    if (isDate) {
                        value = df.format((Date) value);
                    }
                    builder.queryParam(entry.getKey(), value);
                }

            } else {
                builder.queryParam(entry.getKey(), entry.getValue());
            }

        }
    }
//        for (String key : filters.keySet()) {
//            if (filters.get(key) instanceof Date) {
//                Date date = (Date) filters.get(key);
//                boolean datetime = date.getHours() > 0 || date.getMinutes() > 0;
//                if (datetime) {
//                    builder.queryParam(key, dtf.format(filters.get(key)));
//                } else {
//                    builder.queryParam(key, df.format(filters.get(key)));
//                }
//
//            } else if (filters.get(key) instanceof Collection) {
//                List<Object> values = (List<Object>) filters.get(key);
//                Object firstValue = values.get(0);
//                boolean isDate = firstValue instanceof Date;
//
//                for (Object value : values) {
//                    if (isDate) {
//                        value = df.format((Date) value);
//                    }
//                    builder.queryParam(key, value);
//                }
//            } else {
//                builder.queryParam(key, filters.get(key));
//            }
//        }
//    }

    public String getBase64Creds() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        String base64Creds = (String) sessionMap.get("btoa");

        base64Creds = (String) JsfUtils.getFromSession("btoa");

        return base64Creds;
    }
}
