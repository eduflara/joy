package com.joyero.base.jsf;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alejandro on 13/05/2015.
 */
public class MensajeErrorUsuario {

    private String msgId;
    private String clientId;
    private String baseName;
    private List<Object> args;
    private Exception exception;

    public MensajeErrorUsuario() {
        args = new LinkedList<>();
    }

    public MensajeErrorUsuario(String clientId, String msgId, String baseName) {
        this.msgId = msgId;
        this.clientId = clientId;
        this.baseName = baseName;
        args = new LinkedList<>();
    }

    public MensajeErrorUsuario(String msgId, String baseName) {
        this.msgId = msgId;
        this.clientId = null;
        this.baseName = baseName;
        args = new LinkedList<>();
    }

    public MensajeErrorUsuario(String msgId, String baseName, Exception exception) {
        this.msgId = msgId;
        this.clientId = null;
        this.baseName = baseName;
        this.exception = exception;
        args = new LinkedList<>();
    }

    public MensajeErrorUsuario(String msgId, String baseName, Exception exception, List<Object> args) {
        this.msgId = msgId;
        this.clientId = null;
        this.baseName = baseName;
        this.exception = exception;
        this.args = args;
    }

    public MensajeErrorUsuario(String msgId, String baseName, List<Object> args) {
        this.msgId = msgId;
        this.clientId = null;
        this.baseName = baseName;
        this.args = args;
    }


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public boolean addArg(Object arg) {
        if (args == null) {
            args = new LinkedList<>();
        }
        return args.add(arg);
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
