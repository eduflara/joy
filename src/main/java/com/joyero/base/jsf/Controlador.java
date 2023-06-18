package com.joyero.base.jsf;

import com.joyero.base.rest.ApiRest;
import com.joyero.base.rest.Entidad;
import com.joyero.base.util.exception.ResultadoException;
import com.joyero.seguridad.controlador.ControladorSesion;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponentsBuilder;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author Alejandro
 */
public abstract class Controlador<E extends Entidad, ID extends Serializable> implements Serializable {

    protected static final Logger logger = LogManager.getLogger(Logger.class.getName());
    protected static final String NAVEGACION_URL_PAGES = "/pages/";
    protected static final String NAVEGACION_REDIRECCION = "?faces-redirect=true";
    protected static final String NAV_REDIRECCION = "faces-redirect";
    protected static final String MODO_ALTA = "A";
    protected static final String MODO_BAJA = "B";
    protected static final String MODO_MODIFICAR = "M";
    protected static final String MODO_CONSULTAR = "C";
    protected static final String DEFAULT_BUNDLE_NAME = "com.joyero.i18n.comun";

    @Getter
    private static final String ICON_FOLDER = "fa fa-file";

    @Getter
    @Setter
    protected E entidad;

    @Getter
    @Setter
    protected boolean modoDepuracion;

    @Getter
    protected BarraNavegacionControlador barraNavegacionControlador;

    @Getter
    protected ControladorSesion sesionControlador;
    protected TableModelEntidad<E, ID> tablemodel;

    @Autowired(required = false)
    protected ApiRest<E, ID> apiRest;

    public Controlador() {
        nuevo();
    }

    /* Métodos abstractos */

    /**
     * Inicia los objetos necesarios para dar de alta un nuevo registro.
     */
    public abstract void nuevo();

    /**
     * Se ejecuta siempre al cargar la página JSF
     */
    public void onLoad() {
        String limpiarFiltros = JsfUtils.getRequestParameter(BarraNavegacionControlador.NAV_LIMPIAR_FILTROS);
        if ("s".equalsIgnoreCase(limpiarFiltros)) {
            tablemodel.limpiarFiltrosTableModel();
            nuevo();
        }
    }

    public abstract void iniciarTbl();

    public abstract String verFormulario();

    public abstract String verListado();

    public final String getIdPagina() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getRequestParameterMap().get(BarraNavegacionControlador.VIEW_ID);
    }

    public TableModelEntidad<E, ID> getTablemodel() {
        return tablemodel;
    }

    public boolean isModificarRegistro() {
        if (entidad != null) {
            return entidad.getId() != null;
        }
        return false;
    }

    public void guardar() {
        guardar(true);
    }

    public final void guardar(boolean reiniciarEntidad) {
        if (apiRest != null) {
            try {
                preGuardar();
                if (entidad.getId() == null) {
                    entidad = apiRest.save(entidad);
                } else {
                    entidad = apiRest.update(entidad);
                }

                postGuardar();
                addInfoMessage(null, "info.guardado");
                if (reiniciarEntidad) {
                    nuevo();
                }
            } catch (ResultadoException ex) {
                mostrarErrores(ex);
            }
        } else {
            addErrorMessage(null, "fatal.servicio", "com.joyero.i18n.validationMessages");
        }
    }

    public void preGuardar() {
    }

    public void postGuardar() {
    }

    public void eliminar() {
        eliminar(true);
    }

    protected final void eliminar(boolean reiniciarEntidad) {
        //TODO if(isPermitidoBaja())
        if (apiRest != null) {
            try {
                preEliminar();
                apiRest.delete(entidad);
                postEliminar();
                addInfoMessage(null, "info.eliminado");
                if (reiniciarEntidad) {
                    nuevo();
                }
            } catch (ResultadoException ex) {
                mostrarErrores(ex);
            }
        } else {
            addErrorMessage(null, "fatal.servicio", "com.joyero.i18n.validationMessages");
        }
    }

    public void preEliminar() {
    }

    public void postEliminar() {
    }

    public final void cargarSeleccion() {
        //seleccionar registro de la tabla
//        entidad = getTablemodel().getSeleccion();
        //forzar la carga con una peticion rest
        entidad = apiRest.get(getTablemodel().getSeleccion().getId());
        postCargarSeleccion();
    }

    public abstract void postCargarSeleccion();

    @Autowired
    public void setBarraNavegacionControlador(BarraNavegacionControlador barraNavegacionControlador) {
        this.barraNavegacionControlador = barraNavegacionControlador;
    }

    public Locale getIdioma() {
        return sesionControlador.getCurrentLocale();
    }

    public String getIdiomaStr() {
        String language = sesionControlador.getCurrentLocale().getLanguage();
//        String[] temp = languge.split("_");
//        return temp[0];

//        return language.substring(0, language.indexOf('_'));
        return language;
    }

    public String getIdiomaStrShort() {
        String language = sesionControlador.getCurrentLocale().getLanguage();
        String[] temp = language.split("_");
        return temp[0];

        //return language.substring(0, language.indexOf('_'));

    }

    @Autowired
    public void setSesionControlador(ControladorSesion sesionControlador) {
        this.sesionControlador = sesionControlador;
    }

    public boolean isPermitidoAcceso() {
        return sesionControlador.isPermitidoAcceso(getIdPagina());
    }

    public boolean isPermitidoAlta() {
        return sesionControlador.isPermitido(getIdPagina(), MODO_ALTA);
    }

    public boolean isPermitidoBaja() {
        return sesionControlador.isPermitido(getIdPagina(), MODO_BAJA);
    }

    public boolean isPermitidoModificar() {
        return sesionControlador.isPermitido(getIdPagina(), MODO_MODIFICAR);
    }

    public boolean isPermitidoGuardar() {
        if (entidad != null && entidad.getId() != null) {
            return isPermitidoModificar();
        }
        return isPermitidoAlta();
    }

    public boolean isPermitidoConsultar() {
        return sesionControlador.isPermitido(getIdPagina(), MODO_CONSULTAR);
    }

    public boolean isNoPermitidoAlta() {
        return !isPermitidoAlta();
    }

    public boolean isNoPermitidoBaja() {
        return !isPermitidoBaja();
    }

    public boolean isNoPermitidoModificar() {
        return !isPermitidoModificar();
    }

    public boolean isNoPermitidoGuardar() {
        return !isPermitidoGuardar();
    }

    public boolean isNoPermitidoConsultar() {
        return !isPermitidoConsultar();
    }

    public boolean isNoPermitidoAcceso() {
        return !isPermitidoAcceso();
    }


    public void mostrarErrores(ResultadoException ex) {
        for (MensajeErrorUsuario mensajeErrorUsuario : ex.getErrores()) {
            addErrorMessage(mensajeErrorUsuario.getClientId(), mensajeErrorUsuario.getMsgId(), mensajeErrorUsuario.getBaseName(), mensajeErrorUsuario.getArgs().toArray());
        }
        for (MensajeErrorUsuario mensajeAvisoUsuario : ex.getAvisos()) {
            addWarningMessage(mensajeAvisoUsuario.getClientId(), mensajeAvisoUsuario.getMsgId(), mensajeAvisoUsuario.getBaseName(), mensajeAvisoUsuario.getArgs().toArray());
        }
    }

    public void comprobarAcceso() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean isAjax = context.getPartialViewContext().isAjaxRequest();

        if (!isAjax) {
            Map<String, String> requestParams = context.getExternalContext().getRequestParameterMap();

            if (isNoPermitidoAcceso()) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

                try {
                    ec.redirect(ec.getRequestContextPath() + "/pages/access.xhtml");
                } catch (IOException ex) {
                    logger.fatal(ex);
                }
            }
        }
    }

    public String redirigir(String idPagina, String pagina) {
        return redirigir(idPagina, pagina, Collections.emptyMap(), false);
    }

    public String redirigir(String idPagina, String pagina, Map<String, String> params) {
        return redirigir(idPagina, pagina, params, false);
    }

    protected String redirigir(String idPagina, String pagina, Map<String, String> params, boolean ajax) {
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        FacesContext facesContext = FacesContext.getCurrentInstance();
//        String requesContextPath = facesContext.getExternalContext().getRequestContextPath();
//        String requestPath = facesContext.getExternalContext().getRequestServletPath();
        String applicationPath = facesContext.getExternalContext().getApplicationContextPath();

        StringBuilder url = new StringBuilder();

        if (ajax) {
            url.append(applicationPath);
        }

        if (!pagina.contains(NAVEGACION_URL_PAGES)) {
            url.append(NAVEGACION_URL_PAGES);
        }

        url.append(pagina);


        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url.toString());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriBuilder.queryParam(entry.getKey(), entry.getValue());
        }

        uriBuilder.queryParam(BarraNavegacionControlador.VIEW_ID, idPagina);
        barraNavegacionControlador.addElementoBarraNavegacion(idPagina, uriBuilder.toUriString());

        uriBuilder.queryParam(NAV_REDIRECCION, "true");
        uriBuilder.queryParam(BarraNavegacionControlador.NAV_LIMPIAR_FILTROS, "s");
        return uriBuilder.toUriString();
    }

    public void redirigirAjax(String idPagina, String pagina) {
        redirigirAjax(idPagina, pagina, Collections.emptyMap());
    }

    public void redirigirAjax(String idPagina, String pagina, Map<String, String> params) {
        String url = redirigir(idPagina, pagina, params, true);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    /* Métodos estáticos */

    public static String getMessage(String msgId, String baseName) {
        return getMessage(msgId, baseName, FacesContext.getCurrentInstance().getViewRoot().getLocale());
    }

    public static String getMessage(String msgId, String baseName, Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
            return bundle.getString(msgId);
        } catch (MissingResourceException e) {
            return "MISSING[" + msgId + "]";
        }
    }

    /**
     * Format a resource message given some parameters. The current JSF locale
     * is used.
     *
     * @param msgId    id of message in resource file
     * @param baseName
     * @param args     arguments of the message
     * @return the formatted string
     */
    public static String formatMessage(String msgId, String baseName, Object... args) {
        String bundleName = FacesContext.getCurrentInstance().getApplication().getMessageBundle();
        if (baseName == null && bundleName == null) {
            bundleName = DEFAULT_BUNDLE_NAME;
        } else if (baseName != null) {
            bundleName = baseName;
        }
        return MessageFormat.format(getMessage(msgId, bundleName), args);

    }

    /**
     * Add informational message to current context. The same message is used as
     * summary and detail.
     *
     * @param clientId client id (control id) to which message is associated
     *                 (can be null)
     * @param args     optional arguments for message
     * @param msgId    message id in resources file
     */
    public static void addInfoMessage(String clientId, String msgId, Object... args) {
        String msg = formatMessage(msgId, null, args);
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    /**
     * Add informational message to current context. The same message is used as
     * summary and detail.
     *
     * @param clientId client id (control id) to which message is associated
     *                 (can be null)
     * @param args     optional arguments for message
     * @param baseName
     * @param msgId    message id in resources file
     */
    public static void addInfoMessage(String clientId, String msgId, String baseName, Object... args) {
        String msg = formatMessage(msgId, baseName, args);
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    /**
     * Add warning message to current context. The same message is used as
     * summary and detail.
     *
     * @param clientId client id (control id) to which message is associated
     *                 (can be null)
     * @param args     optional arguments for message
     * @param msgId    message id in resources file
     */
    public static void addWarningMessage(String clientId, String msgId, Object... args) {
        String msg = formatMessage(msgId, null, args);
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null));
    }

    /**
     * Add warning message to current context. The same message is used as
     * summary and detail.
     *
     * @param clientId client id (control id) to which message is associated
     *                 (can be null)
     * @param args     optional arguments for message
     * @param baseName
     * @param msgId    message id in resources file
     */
    public static void addWarningMessage(String clientId, String msgId, String baseName, Object... args) {
        String msg = formatMessage(msgId, baseName, args);
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null));
    }

    /**
     * Add error message to current context. The same message is used as summary
     * and detail.
     *
     * @param clientId client id (control id) to which message is associated
     *                 (can be null)
     * @param args     optional arguments for message
     * @param msgId    message id in resources file
     */
    public static void addErrorMessage(String clientId, String msgId, Object... args) {
        String msg = formatMessage(msgId, null, args);
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    /**
     * Add error message to current context. The same message is used as summary
     * and detail.
     *
     * @param clientId client id (control id) to which message is associated
     *                 (can be null)
     * @param args     optional arguments for message
     * @param baseName
     * @param msgId    message id in resources file
     */
    public static void addErrorMessage(String clientId, String msgId, String baseName, Object... args) {
        String msg = msgId;
        if (baseName != null) {
            msg = formatMessage(msgId, baseName, args);
        }
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    /**
     * Add fatal error message to current context filtering the exception cause
     *
     * @param clientId client id (control id) to which message is associated
     *                 (can be null)
     * @param exp      the exception
     * @param args     optional arguments for message
     */
    public static void addErrorMessage(String clientId, Exception exp, Object... args) {
        String msgId = "error_genericException";
        String msg = formatMessage(msgId, null, args);
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, null));
    }

    public static String getRequestParameter(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    public static void renderResponse() {
        FacesContext.getCurrentInstance().renderResponse();
    }

    /**
     * Get a component given its id
     *
     * @param id component id according to JSF specification
     * @return the requested component
     */
    public static UIComponent getComponent(String id) {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final UIViewRoot root = facesContext.getViewRoot();
        return root.findComponent(id);
    }

    /**
     * Get a managed bean by name. This method looks first for beans defined
     * under JSF and, if that fails, looks under Spring's context (if that is
     * configured in faces-config.xml file).
     *
     * @param name bean name
     * @return the managed bean associated to the name
     */
    public static Object getBean(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(name);
    }

    public static Object setBean(String name, Object object) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(name, object);
    }

    /**
     * Remove an object from session
     *
     * @param name name of object to remove
     */
    public static void removeFromSession(String name) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(name);
    }

    public static void showDialog(String widgetVar) {
        StringBuilder stb = new StringBuilder();
        stb.append("PF('");
        stb.append(widgetVar);
        stb.append("').show();");
        PrimeFaces.current().executeScript(stb.toString());
    }

    public static void hideDialog(String widgetVar) {
        StringBuilder stb = new StringBuilder();
        stb.append("PF('");
        stb.append(widgetVar);
        stb.append("').hide();");
        PrimeFaces.current().executeScript(stb.toString());
    }

    public static void showMessageDialog(String messageId, Object... args) {
        String message = formatMessage(messageId, null, args);
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, message);
        PrimeFaces.current().dialog().showMessageDynamic(facesMessage);
    }
}
