package com.joyero.seguridad.controlador;


import com.joyero.seguridad.rest.funcionAsignada.FuncionAsignada;
import com.joyero.seguridad.rest.funcionAsignada.FuncionAsignadaRest;
import com.joyero.seguridad.rest.grupo.Grupo;
import com.joyero.seguridad.rest.grupo.GrupoRest;
import com.joyero.seguridad.rest.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.*;

/**
 * @author Alejandro
 */
@Controller
@Scope("session")
public class ControladorSesion {
    /* Servicios Rest inyectados */
    @Autowired
    private GrupoRest grupoRest;

    private FuncionAsignadaRest funcionAsignadaRest;

    /* Atributos del controlador */
    @NotEmpty
    @Getter
    @Setter
    private String username;

    @NotEmpty
    @Getter
    @Setter
    private String password;

    @Setter
    private Usuario usuario;

    @Setter
    private Locale currentLocale;

    private List<String> funcionesPermitidas = new LinkedList<>();

    /* Métodos get/set */
    public UserDetails getUsuario() {
        if (usuario == null
                && getAuthentication() != null
                && getAuthentication().getPrincipal() != null) {
            usuario = (Usuario) getAuthentication().getPrincipal();
        }
        return usuario;
    }

    public Usuario getUsuarioActual() {
        return (Usuario) getUsuario();
    }

    public Locale getCurrentLocale() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        Locale browserLocale = externalContext.getRequestLocale();
        Locale appLocale = facesContext.getApplication().getDefaultLocale();
        Locale viewLocale = null;
        if (facesContext.getViewRoot() != null) {
            viewLocale = facesContext.getViewRoot().getLocale();
        }
        //si no se ha definido el idioma se utilizará el del navegador
        //pero si el usuario ha seleccionado uno en la pantalla se login se usará ese.
        if (currentLocale == null) {
            currentLocale = browserLocale;
        }
        if (usuario != null) {
            currentLocale = usuario.getLocale();
        }

        if (facesContext.getViewRoot() != null) {
            facesContext.getViewRoot().setLocale(currentLocale);
        }
        return currentLocale;
    }

    public String getCurrentLocaleStr() {
        String language = getCurrentLocale().getLanguage();
        String[] temp = language.split("_");
        return temp[0];
    }

    public List<String> getFuncionesPermitidas() {
        if (funcionesPermitidas.isEmpty() && getUsuario() != null) {
            for (GrantedAuthority authority : getUsuario().getAuthorities()) {
                Grupo grupo = grupoRest.get(authority.getAuthority());
                if (grupo.getFuncionesAsginadas() != null) {
                    for (FuncionAsignada funcionesAsginada : grupo.getFuncionesAsginadas()) {
                        String idFuncion = funcionesAsginada.getFuncion() != null ? Long.toString(funcionesAsginada.getFuncion().getId()) : "";
                        funcionesPermitidas.add(idFuncion);
                    }
                }
            }
        }
        return funcionesPermitidas;
    }

    /* Métodos propios del controlador */

    public String getNombreUsuario() {
        if (getAuthentication() != null
                && getAuthentication().getPrincipal() != null) {
            return ((UserDetails) getAuthentication().getPrincipal()).getUsername();
        } else {
            return null;
        }
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getNombreCompleto() {
        if (getAuthentication() != null
                && getAuthentication().getPrincipal() != null) {
            return ((Usuario) getAuthentication().getPrincipal()).getNombreCompleto();
        } else {
            return null;
        }
    }

    public String getDireccionIp() {
        if (getAuthentication() != null
                && getAuthentication().getPrincipal() != null) {
            return ((WebAuthenticationDetails) getAuthentication().getDetails()).getRemoteAddress();
        } else {
            return null;
        }
    }

    public String getSessionId() {
        if (getAuthentication() != null
                && getAuthentication().getPrincipal() != null) {
            return ((WebAuthenticationDetails) getAuthentication().getDetails()).getSessionId();
        } else {
            return null;
        }
    }

    public boolean isPermitido(String pagina, String modo) {
        if (getUsuario() != null) {
            for (GrantedAuthority authority : getUsuario().getAuthorities()) {
                Grupo grupo = grupoRest.get(authority.getAuthority());
                if (grupo.getFuncionesAsginadas() != null) {
                    for (FuncionAsignada funcionesAsginada : grupo.getFuncionesAsginadas()) {
                        if (funcionesAsginada.getFuncion().getIdPagina().equals(pagina)) {
                            switch (modo) {
                                case "A":
                                    return BooleanUtils.isTrue(funcionesAsginada.getAlta());
                                case "B":
                                    return BooleanUtils.isTrue(funcionesAsginada.getBaja());
                                case "M":
                                    return BooleanUtils.isTrue(funcionesAsginada.getModificar());
                                case "C":
                                    return BooleanUtils.isTrue(funcionesAsginada.getConsultar());
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public void invalidarSesion() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public boolean isPermitidoAcceso(String pagina) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return false;
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return false;
        }

        if (getUsuario() != null) {
            for (GrantedAuthority authority : getUsuario().getAuthorities()) {
                Grupo grupo = grupoRest.get(authority.getAuthority());
                if (grupo.getFuncionesAsginadas() != null) {
                    for (FuncionAsignada funcionesAsginada : grupo.getFuncionesAsginadas()) {
                        if (funcionesAsginada.getFuncion().getIdPagina().equals(pagina)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void guardarAcceso(String pagina) {
//        RegistroAcceso registroAcceso = new RegistroAcceso(getDireccionIp(), (com.sumainfo.seguridad.model.Usuario) getUsuario());
//        registroAcceso.setSessionId(getSessionId());
//        registroAcceso.setUrl(pagina);
//        servicioSeguridad.guardarRegistroAcceso(registroAcceso);
    }

    public String doLogout() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/logout");
            dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception exception) {
            //logger.fatal(exception);
        }
        return null;
    }

    public String doLogin() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check");
            dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception exception) {
            String mensajeError = "Error grave en el sistema.";
            String mensajeErrorDetalles = "Error grave en el sistema.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, mensajeError, mensajeErrorDetalles));
        }
        return null;
    }

    public List<Locale> getSupportedLocales() {
        List<Locale> locales = new ArrayList<>();
        for (Iterator<Locale> it = FacesContext.getCurrentInstance().getApplication().getSupportedLocales(); it.hasNext(); ) {
            locales.add(it.next());
        }
        return locales;
    }

    public String cambiarIdioma(String idioma) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        Locale locale = new Locale(idioma);
        currentLocale = locale;

        UIViewRoot uiViewRoot = FacesContext.getCurrentInstance().getViewRoot();
        uiViewRoot.setLocale(locale);

        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();

        Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        if (usuario != null) {
            usuario.setLocale(locale);
        }
        String referrer = externalContext.getRequestHeaderMap().get("referer");
        try {
            externalContext.redirect(referrer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
