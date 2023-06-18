package com.joyero.base.util.exception;

/**
 * Created by alejandro on 13/09/2016.
 */
public class ResultadoErrorException extends ResultadoException {

    private ResultadoException resultadoException;

    public ResultadoErrorException(ResultadoException resultadoException) {
        this.resultadoException = resultadoException;
        this.errores = resultadoException.getErrores();
        this.avisos = resultadoException.getAvisos();
    }

    public ResultadoException getResultadoException() {
        return resultadoException;
    }

    public void setResultadoException(ResultadoException resultadoException) {
        this.resultadoException = resultadoException;
    }

}
