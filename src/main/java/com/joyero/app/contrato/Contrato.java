package com.joyero.app.contrato;

import com.joyero.app.Mercado;
import com.joyero.app.TipoContrato;
import com.joyero.app.cliente.Cliente;
import com.joyero.base.rest.Entidad;
import lombok.Data;

import java.util.Date;

@Data
public class Contrato implements Entidad {

    private Long id;
    private String codigo;
    private String lugartasacion;
    private TipoContrato tipoContrato;
    private Mercado mercado;
    private Date fechaAlta;
    private Cliente cliente;
    private Long clienteId;
    private String tasador;
    private String personafirma;
    private Date fechaCierre;
}
