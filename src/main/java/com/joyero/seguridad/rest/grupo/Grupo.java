package com.joyero.seguridad.rest.grupo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joyero.base.rest.Entidad;
import com.joyero.seguridad.rest.funcionAsignada.FuncionAsignada;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Grupo implements Entidad {


    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String descripcion;

    private Integer tiempoSesion;

    private Boolean root;

    private Set<FuncionAsignada> funcionesAsginadas;
}
