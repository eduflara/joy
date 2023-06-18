package com.joyero.seguridad.rest.sistema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joyero.base.rest.Entidad;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Sistema implements Entidad {

    private Long id;

    private String nombre;

    private String url;
}
