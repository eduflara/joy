package com.joyero.app.cliente;

import com.joyero.base.rest.Entidad;
import lombok.Data;

@Data
public class Cliente implements Entidad {

    private Long id;
    private String codigo;
    private String nombre;
    private String apellidos;
    private String telefono;
}
