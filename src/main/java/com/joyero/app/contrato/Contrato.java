package com.joyero.app.contrato;

import com.joyero.app.cliente.Cliente;
import com.joyero.base.rest.Entidad;
import lombok.Data;

import java.util.Date;

@Data
public class Contrato implements Entidad {

    private Long id;
    private String codigo;
    private String lugartasacion;
    private String tipocontrato;
    private String mercado;
    private Date fechaAlta;
    private Cliente cliente;
    private String tasador;
    private String personafirma;
    private Date fechaCierre;
}
