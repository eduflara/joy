package com.joyero.app.contrato;

import com.joyero.app.Mercado;
import com.joyero.app.TipoContrato;
import com.joyero.app.cliente.Cliente;
import com.joyero.app.cliente.ClienteRest;
import com.joyero.app.lote.LoteCtl;
import com.joyero.base.jasperreports.JasperReportClientRest;
import com.joyero.base.jsf.Controlador;
import com.joyero.seguridad.controlador.ControladorSesion;
import com.joyero.seguridad.rest.grupo.Grupo;
import com.joyero.seguridad.rest.grupo.GrupoRest;
import com.joyero.seguridad.rest.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@Scope(value = "session")
public class ContratoCtl extends Controlador<Contrato, Long> {

    private Long idCliente;

    private String codigoMercado;
    private String codigoTipoContrato;

    @Autowired
    private ControladorSesion controladorSesion;

    @Getter
    @Setter
    private List<Contrato> contratosSeleccionados;

    @Autowired
    private ClienteRest clienteRest;

    @Autowired
    private LoteCtl loteCtl;

    //endregion
    //region Implementar y sobrescribir métodos del controlador base
    @PostConstruct
    @Override
    public void iniciarTbl() {
        tablemodel = new ContratoTbl(apiRest);
        tablemodel.getFiltros().put("fechaCierre", "null");
        nuevo();
    }

    @Override
    public String verFormulario() {
        return null;
    }

    @Override
    public String verListado() {
        return null;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void nuevo() {
        entidad = new Contrato();
    }

    @Override
    public void postCargarSeleccion() {
    }

    @Override
    public void preGuardar() {
        if (idCliente != null) {
            this.entidad.setClienteId(idCliente);
        }
        if (codigoMercado != null) {
            this.entidad.setMercado(Mercado.getMercado(codigoMercado));
        }
        if (codigoTipoContrato != null) {
            this.entidad.setTipoContrato(TipoContrato.getTipoContrato(codigoTipoContrato));
        }
    }

    @Override
    public void postGuardar() {
    }

    @Override
    public void preEliminar() {
    }

    @Override
    public void postEliminar() {
    }

    public List<Contrato> getContratosSeleccionados() {
        return contratosSeleccionados;
    }

    public void setContratosSeleccionados(List<Contrato> contratosSeleccionados) {
        this.contratosSeleccionados = contratosSeleccionados;
        this.loteCtl.setIdContrato(contratosSeleccionados.get(0).getId());
    }

    public List<Cliente> getClientes(){
        return clienteRest.getCollection();
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public List<Mercado> dameMercados() {
        return Mercado.getMercados();
    }

    public String getCodigoMercado() {
        return codigoMercado;
    }

    public void setCodigoMercado(String codigoMercado) {
        this.codigoMercado = codigoMercado;
    }

    public String getCodigoTipoContrato() {
        return codigoTipoContrato;
    }

    public void setCodigoTipoContrato(String codigoTipoContrato) {
        this.codigoTipoContrato = codigoTipoContrato;
    }

    public List<TipoContrato> dameTiposContrato() {
        return TipoContrato.getTipos();
    }

//endregion
}
