package com.joyero.app.lote;

import com.joyero.app.*;
import com.joyero.app.cliente.Cliente;
import com.joyero.app.contrato.Contrato;
import com.joyero.base.rest.Entidad;
import lombok.Data;

import java.util.Date;

@Data
public class Lote implements Entidad {

    private Long id;
    private Objeto objeto;
    private Contrato contrato;
    private Long idContrato;
    private String descripcion;
    private String observaciones;
    private Gema gema;
    private Metal metal;
    private Double cantidad;
    private Double peso;
}
