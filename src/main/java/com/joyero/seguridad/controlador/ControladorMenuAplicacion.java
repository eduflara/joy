package com.joyero.seguridad.controlador;

import com.joyero.base.jsf.BarraNavegacionControlador;
import com.joyero.seguridad.rest.funcion.Funcion;
import com.joyero.seguridad.rest.funcion.FuncionRest;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.model.menu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Alejandro
 */
@Controller
@Scope("session")
public class ControladorMenuAplicacion implements Serializable {

    private final MenuModel menuModel;
    private final MenuModel menuModelSesion;
    private final ControladorSesion controladorSesion;
    // private final IdiomaCtl idiomaCtl;

    @Value("${seguridad.modo}")
    private int modoSeguridad;
    @Value("${app.ficheroIdioma}")
    private String ficheroIdioma;
    private ResourceBundle txt;

    private String path;

    @Autowired
    private FuncionRest funcionRest;

    @Autowired
    public ControladorMenuAplicacion(
            ControladorSesion controladorSesion) {

        this.controladorSesion = controladorSesion;
        //this.idiomaCtl = idiomaCtl;
        menuModel = new DefaultMenuModel();
        menuModelSesion = new DefaultMenuModel();
    }

    public ControladorMenuAplicacion() {
        this.menuModel = null;
        this.menuModelSesion = null;
        //this.idiomaCtl = null;
        this.controladorSesion = null;
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public MenuModel getMenuModelSesion() {
        return menuModelSesion;
    }

    @PostConstruct
    public void postConstruct() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        path = externalContext.getRequestScheme() + "://" + externalContext.getRequestServerName() + ":" + externalContext.getRequestServerPort();
        txt = ResourceBundle.getBundle(ficheroIdioma, controladorSesion.getCurrentLocale());


        cargarMenu();

        if (modoSeguridad == 1) {
            DefaultMenuItem menuItem = new DefaultMenuItem(getNombreFuncion("cambiarContrasena"), "ui-icon-key", "#");
            menuItem.setOnclick("PF('cambiarContrasenaDlg').show()");
            menuModelSesion.addElement(menuItem);
            //cambiar contraseña
            menuItem = new DefaultMenuItem(getNombreFuncion("cambiarIdioma"), "ui-icon-flag", "#");
            menuItem.setOnclick("PF('cambiarIdioma').show()");
            menuModelSesion.addElement(menuItem);
            //buzon

            String url = getAppContext() + "pages/spc/buzonMi.xhtml" + "?" + BarraNavegacionControlador.NAV_REINICIAR + "=s&" + BarraNavegacionControlador.VIEW_ID + "=buzon";
            menuItem = new DefaultMenuItem(getNombreFuncion("buzon"), "fa fa-envelope", url);
            menuModelSesion.addElement(menuItem);
            //salir
            menuItem = new DefaultMenuItem(getNombreFuncion("salir"), "fa fa-sign-out", "/logout");
            menuModelSesion.addElement(menuItem);
        } else if (modoSeguridad == 2) {
            DefaultMenuItem menuItem = new DefaultMenuItem("Salir", "fa fa-sign-out", "/j_spring_security_logout");
            menuModelSesion.addElement(menuItem);
        }
    }

    public void cargarMenu() {
        cargarMenu(controladorSesion.getCurrentLocale().getLanguage());
    }

    public void cargarMenu(String idioma) {
        menuModel.getElements().clear();
        txt = ResourceBundle.getBundle(ficheroIdioma, new Locale(idioma));
        List<Funcion> funciones = funcionRest.cargarFuncionesSuperiores();

        for (Funcion funcion : funciones) {
            addItem(funcion, null);
        }
    }

    public void addItem(Funcion funcion, DefaultSubMenu menuFuncionSuperior) {
        List<Funcion> funciones = funcionRest.cargarFuncionesInferiores(funcion.getId());
        funcion.setFuncionesInferiores(funciones);

        if (menuFuncionSuperior == null) {
            if (funcion.getFuncionesInferiores() != null
                    && !funcion.getFuncionesInferiores().isEmpty()) {
                addSubMenu(funcion);
            } else {
                addMenuItem(funcion);
            }
        } else {
            if (funcion.getFuncionesInferiores() != null
                    && !funcion.getFuncionesInferiores().isEmpty()) {

                boolean funcionesInferioresVisibles = false;
                for (Funcion funcionesInferior : funcion.getFuncionesInferiores()) {
                    funcionesInferioresVisibles = funcionesInferioresVisibles || funcionesInferior.getApareceMenu();
                }
                if (funcionesInferioresVisibles) {
                    addSubMenu(funcion, menuFuncionSuperior);
                } else {
                    addMenuItem(funcion, menuFuncionSuperior);
                }
            } else if (BooleanUtils.isTrue(funcion.getApareceMenu())) {
                addMenuItem(funcion, menuFuncionSuperior);
            }
        }
    }

    private void addSubMenu(Funcion funcion) {
        DefaultSubMenu subMenu = new DefaultSubMenu(getNombreFuncion(funcion.getIdPagina()), funcion.getIcono());
        for (Funcion funcionAux : funcion.getFuncionesInferiores()) {
            addItem(funcionAux, subMenu);
        }
        boolean rendered = false;
        for (MenuElement menuElement : subMenu.getElements()) {
            rendered = rendered || menuElement.isRendered();
        }
        subMenu.setRendered(rendered);
        menuModel.addElement(subMenu);
    }

    private void addSubMenu(Funcion funcion, DefaultSubMenu menuFuncionSuperior) {
        DefaultSubMenu subMenu = new DefaultSubMenu(getNombreFuncion(funcion.getIdPagina()), funcion.getIcono());
        for (Funcion funcionAux : funcion.getFuncionesInferiores()) {
            addItem(funcionAux, subMenu);
        }
        boolean rendered = false;
        for (MenuElement menuElement : subMenu.getElements()) {
            rendered = rendered || menuElement.isRendered();
        }
        subMenu.setRendered(rendered);
        menuFuncionSuperior.getElements().add(subMenu);
    }

    private void addMenuItem(Funcion funcion) {
        DefaultMenuItem menuItem = new DefaultMenuItem(getNombreFuncion(funcion.getIdPagina()), funcion.getIcono());
        if (funcion.getUrl() != null) {
            String url;
            if (funcion.getUrl().contains("?")) {
                url = getAppContext() + funcion.getUrl() + "&" + BarraNavegacionControlador.NAV_REINICIAR + "=s&" + BarraNavegacionControlador.NAV_LIMPIAR_FILTROS + "=s&" + BarraNavegacionControlador.VIEW_ID + "=" + funcion.getIdPagina();
            } else {
                url = getAppContext() + funcion.getUrl() + "?" + BarraNavegacionControlador.NAV_REINICIAR + "=s&" + BarraNavegacionControlador.NAV_LIMPIAR_FILTROS + "=s&" + BarraNavegacionControlador.VIEW_ID + "=" + funcion.getIdPagina();
            }
            if (!url.startsWith("http")) {
                url = path.concat(url);
            }
            menuItem.setUrl(url);
        }
        if (controladorSesion.getFuncionesPermitidas() == null
                || !controladorSesion.getFuncionesPermitidas().contains(funcion.getId() + "")) {
            menuItem.setRendered(false);
        }
        menuModel.addElement(menuItem);
    }

    private void addMenuItem(Funcion funcion, DefaultSubMenu menuFuncionSuperior) {
        DefaultMenuItem menuItem = new DefaultMenuItem(getNombreFuncion(funcion.getIdPagina()), funcion.getIcono());
        if (funcion.getUrl() != null) {
            String url = "";
            if (funcion.getUrl().contains("?")) {
                url = getAppContext() + funcion.getUrl() + "&" + BarraNavegacionControlador.NAV_REINICIAR + "=s&" + BarraNavegacionControlador.NAV_LIMPIAR_FILTROS + "=s&" + BarraNavegacionControlador.VIEW_ID + "=" + funcion.getIdPagina();
            } else {
                url = getAppContext() + funcion.getUrl() + "?" + BarraNavegacionControlador.NAV_REINICIAR + "=s&" + BarraNavegacionControlador.NAV_LIMPIAR_FILTROS + "=s&" + BarraNavegacionControlador.VIEW_ID + "=" + funcion.getIdPagina();
            }
            if (!url.startsWith("http")) {
                url = path.concat(url);
            }
            menuItem.setUrl(url);
        }
        if (controladorSesion.getFuncionesPermitidas() == null
                || !controladorSesion.getFuncionesPermitidas().contains(funcion.getId() + "")) {
            menuItem.setRendered(false);
        }
        menuFuncionSuperior.getElements().add(menuItem);
    }

    public String getNombreFuncion(String etiqueta) {
        String valor;
        try {
            if (etiqueta != null) {
                valor = txt.getString("menu." + etiqueta);
            } else {
                valor = txt.getString("menu.sinDefinir");
            }
        } catch (java.util.MissingResourceException ex) {
            valor = txt.getString("menu.sinDefinir");
        }
        return valor;
    }

    private String getAppContext() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String appContext = ec.getApplicationContextPath();
        if (!appContext.endsWith("/")) {
            appContext = appContext.concat("/");
        }
        return appContext;
    }


}
