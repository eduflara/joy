package com.joyero.base.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class JsfUtils {

    private static final String DEFAULT_BUNDLE_NAME = "messages";

    public static String getMessage(String msgId) {
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(DEFAULT_BUNDLE_NAME, locale);
        return bundle.getString(msgId);
    }

    public static String getRequestParameter(String name) {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    public static void renderResponse() {
        FacesContext.getCurrentInstance().renderResponse();
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
        String msg = formatMessage(msgId, args);
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
        String msg = formatMessage(msgId, args);
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
        String msg = formatMessage(msgId, args);
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    /**
     * Add error message to current context filtering the exception cause
     *
     * @param clientId client id (control id) to which message is associated
     *                 (can be null)
     * @param exp      the exception
     * @param args     optional arguments for message
     */
    public static void addErrorMessage(String clientId, Exception exp, Object... args) {
        String msgId = "error_genericException";

        String msg = formatMessage(msgId, args);
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    /**
     * Format a resource message given some parameters. The current JSF locale
     * is used.
     *
     * @param msgId id of message in resource file
     * @param args  arguments of the message
     * @return the formatted string
     */
    public static String formatMessage(String msgId, Object... args) {
        String bundleName = FacesContext.getCurrentInstance().getApplication().getMessageBundle();
        if (bundleName == null) {
            bundleName = DEFAULT_BUNDLE_NAME;
        }
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
        try {
            String msg = bundle.getString(msgId);
            return MessageFormat.format(msg, args);
        } catch (MissingResourceException e) {
            return "MISSING: " + msgId + " :MISSING";
        }
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
     * Remove an object from session
     *
     * @param name name of object to remove
     */
    public static void removeFromSession(String name) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(name);
    }

    public static void putToSession(String name, Object value) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put(name, value);

        String sesion = context.getSessionId(true);
    }

    public static Object getFromSession(String name) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().get(name);

        String sesion = context.getSessionId(true);

        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(name);
    }
}
