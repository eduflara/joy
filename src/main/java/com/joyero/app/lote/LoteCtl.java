package com.joyero.app.lote;

import com.joyero.app.*;
import com.joyero.app.cliente.Cliente;
import com.joyero.app.cliente.ClienteRest;
import com.joyero.base.jsf.Controlador;
import com.joyero.seguridad.controlador.ControladorSesion;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@Scope(value = "session")
public class LoteCtl extends Controlador<Lote, Long> {

    private Long idContrato;

    private String codigoObjeto;
    private String codigoGema;
    private String codigoMetal;

    @Autowired
    private ControladorSesion controladorSesion;

    @Getter
    @Setter
    private List<Lote> lotesSeleccionados;

    @Autowired
    private LoteRest loteRest;

    //endregion
    //region Implementar y sobrescribir métodos del controlador base
    @PostConstruct
    @Override
    public void iniciarTbl() {
        tablemodel = new LoteTbl(apiRest);
        tablemodel.getFiltros().put("idContrato", idContrato);
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
        entidad = new Lote();
    }

    @Override
    public void postCargarSeleccion() {
    }

    @Override
    public void preGuardar() {
        /*
        if (idCliente != null) {
            this.entidad.setClienteId(idCliente);
        }
        if (codigoMercado != null) {
            this.entidad.setMercado(Mercado.getMercado(codigoMercado));
        }
        if (codigoTipoContrato != null) {
            this.entidad.setTipoContrato(TipoContrato.getTipoContrato(codigoTipoContrato));
        }
         */
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

    public List<Lote> getLotesSeleccionados() {
        return lotesSeleccionados;
    }

    public void setLotesSeleccionados(List<Lote> lotesSeleccionados) {
        this.lotesSeleccionados = lotesSeleccionados;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public LoteRest getLoteRest() {
        return loteRest;
    }

    public void setLoteRest(LoteRest loteRest) {
        this.loteRest = loteRest;
    }

    public String getCodigoObjeto() {
        return codigoObjeto;
    }

    public void setCodigoObjeto(String codigoObjeto) {
        this.codigoObjeto = codigoObjeto;
    }

    public List<Objeto> dameObjetos() {
        return Objeto.dameObjetos();
    }

    public String getCodigoGema() {
        return codigoGema;
    }

    public void setCodigoGema(String codigoGema) {
        this.codigoGema = codigoGema;
    }

    public String getCodigoMetal() {
        return codigoMetal;
    }

    public void setCodigoMetal(String codigoMetal) {
        this.codigoMetal = codigoMetal;
    }

    public List<Gema> dameGemas() {
        return Gema.dameGemas();
    }

    public List<Metal> dameMetales() {
        return Metal.dameMetales();
    }

    //endregion
}
