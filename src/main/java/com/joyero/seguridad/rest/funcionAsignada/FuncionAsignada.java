package com.joyero.seguridad.rest.funcionAsignada;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joyero.base.rest.Entidad;
import com.joyero.seguridad.rest.funcion.Funcion;
import com.joyero.seguridad.rest.grupo.Grupo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuncionAsignada implements Entidad {

    @EqualsAndHashCode.Exclude
    private Long id;

    private Funcion funcion;

    private Long idFuncion;

    private Grupo grupo;

    private Long idGrupo;

    @EqualsAndHashCode.Exclude
    private Boolean alta;

    @EqualsAndHashCode.Exclude
    private Boolean baja;

    @EqualsAndHashCode.Exclude
    private Boolean modificar;

    @EqualsAndHashCode.Exclude
    private Boolean consultar;
}
