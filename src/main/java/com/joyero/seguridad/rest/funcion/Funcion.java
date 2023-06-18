package com.joyero.seguridad.rest.funcion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joyero.base.rest.Entidad;
import com.joyero.seguridad.rest.sistema.Sistema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Funcion implements Entidad {

    private Long id;

    private String icono;

    private String url;

    private String idPagina;

    private Boolean apareceMenu;

    private Integer orden;

    private Sistema sistema;
    private Long idSistema;

    @EqualsAndHashCode.Exclude
    private Funcion funcionSuperior;

    private Long idFuncionSuperior;

    @EqualsAndHashCode.Exclude
    private List<Funcion> funcionesInferiores;

}
