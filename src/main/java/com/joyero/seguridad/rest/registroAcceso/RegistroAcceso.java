package com.joyero.seguridad.rest.registroAcceso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joyero.base.rest.Entidad;
import com.joyero.seguridad.rest.usuario.Usuario;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RegistroAcceso implements Entidad {

    private Long id;

    private Date fecha;

    private String ip;

    private Usuario usuario;

    private Long idUsuario;

    private Boolean cierreSesion;

    private String url;

    private String sessionId;

}
