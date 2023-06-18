package com.joyero.base.util.exception;

import com.joyero.base.jsf.MensajeErrorUsuario;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alejandro on 02/09/2016.
 */
public class ResultadoException extends RuntimeException {

    protected List<MensajeErrorUsuario> errores;
    protected List<MensajeErrorUsuario> avisos;
    protected long tiempoEjecucion;

    public ResultadoException() {
        errores = new LinkedList<>();
        avisos = new LinkedList<>();
    }

    public List<MensajeErrorUsuario> getErrores() {
        return errores;
    }

    public void setErrores(List<MensajeErrorUsuario> errores) {
        this.errores = errores;
    }

    public boolean addError(MensajeErrorUsuario mensajeErrorUsuario) {
        if (errores == null) {
            errores = new LinkedList<>();
        }
        return errores.add(mensajeErrorUsuario);
    }

    public List<MensajeErrorUsuario> getAvisos() {
        return avisos;
    }

    public void setAvisos(List<MensajeErrorUsuario> avisos) {
        this.avisos = avisos;
    }

    public boolean addAviso(MensajeErrorUsuario mensajeErrorUsuario) {
        if (avisos == null) {
            avisos = new LinkedList<>();
        }
        return avisos.add(mensajeErrorUsuario);
    }

    public long getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(long tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public boolean debeArrojarse(boolean ignoraAvisos) {
        if (ignoraAvisos) {
            return !errores.isEmpty();
        } else {
            return !errores.isEmpty() || !avisos.isEmpty();
        }
    }

    public boolean debeArrojarse() {
        return this.debeArrojarse(false);
    }
}
