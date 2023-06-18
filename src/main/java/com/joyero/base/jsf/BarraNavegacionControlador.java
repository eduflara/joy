package com.joyero.base.jsf;

import com.joyero.seguridad.controlador.ControladorSesion;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

/**
 * @author Alejandro
 */
@Controller
@Scope("session")
public class BarraNavegacionControlador implements Serializable {

    public static final String NAV_REDIRECCION = "faces-redirect";
    public static final String NAV_REINICIAR = "rn";
    public static final String NAV_LIMPIAR_FILTROS = "lf";
    public static final String VIEW_ID = "viewId";

    private ControladorSesion controladorSesion;
    //private IdiomaCtl idiomaCtl;
    private final MenuModel menuModel;
    private final LinkedList<String> listaUrls;
    private final Map<String, MenuItem> menuItemUrl;
    private ResourceBundle txt;

    @Value("${app.ficheroIdioma}")
    private String ficheroIdioma;


    public BarraNavegacionControlador() {
        menuModel = new DefaultMenuModel();
        listaUrls = new LinkedList<>();
        menuItemUrl = new HashMap<>();
        txt = null;
    }

    @Autowired
    public BarraNavegacionControlador(ControladorSesion controladorSesion) {
        menuModel = new DefaultMenuModel();
        listaUrls = new LinkedList<>();
        menuItemUrl = new HashMap<>();
        this.controladorSesion = controladorSesion;
    }

    private String getIndexUrl() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String applicationPath = facesContext.getExternalContext().getApplicationContextPath();
        String url = applicationPath.concat("/pages/index.xhtml");
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
        uriBuilder.queryParam(VIEW_ID, "inicio");
        url = uriBuilder.toUriString();
        return url;
    }

    public MenuModel getMenuModel() {
        if (menuModel.getElements().isEmpty()) {
            addElementoBarraNavegacion("inicio", getIndexUrl());
        }
        return menuModel;
    }

    public void reiniciarBarraNavegacion() {
        while (!listaUrls.getLast().equals(getIndexUrl())) {
            menuModel.getElements().remove(menuItemUrl.remove(listaUrls.removeLast()));
        }
    }

    public void addElementoBarraNavegacion(String idPagina, String url) {
        if (url.contains(VIEW_ID)) {

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            String applicationPath = externalContext.getApplicationContextPath();

            Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
            List<String> keyParams = new LinkedList<>(params.keySet());
            Collections.sort(keyParams);

            if (!url.startsWith(applicationPath)) {
                url = applicationPath + url;
            }

            if (!url.contains(VIEW_ID)) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
                if (params.get(VIEW_ID) != null) {
                    builder.queryParam(VIEW_ID, params.get(VIEW_ID));
                } else if (idPagina != null) {
                    builder.queryParam(VIEW_ID, idPagina);
                }
                url = builder.toUriString();
            }

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
            UriComponents uriComponents = uriBuilder.build();
            MultiValueMap<String, String> uriQueryParams = uriComponents.getQueryParams();
            List<String> uriQueryParamKeys = new LinkedList<>(uriQueryParams.keySet());

            Collections.sort(uriQueryParamKeys);
            String path = uriComponents.getPath();

            uriBuilder = UriComponentsBuilder.fromUriString(path);
            for (String uriParam : uriQueryParamKeys) {
                List<String> uriParamValue = new LinkedList<>(uriQueryParams.get(uriParam));
                Collections.sort(uriParamValue);
                for (String s : uriParamValue) {
                    uriBuilder.queryParam(uriParam, s);
                }

            }

            url = uriBuilder.toUriString();
            System.out.println("URL" + url);


            if (idPagina != null && url != null && !listaUrls.contains(url)) {
                DefaultMenuItem menuItem = new DefaultMenuItem();
                menuItem.setValue(getNombreElemento(idPagina));
                menuItem.setUrl(url);

                listaUrls.add(url);
                menuItemUrl.put(url, menuItem);
                menuModel.addElement(menuItem);

                controladorSesion.guardarAcceso(idPagina);
            }

        }
    }

    public void comprobarNavegacion() {

        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            Map<String, String> params = new HashMap<>();
            params.putAll(externalContext.getRequestParameterMap());

            String requestPath = externalContext.getRequestServletPath();
            String applicationPath = externalContext.getApplicationContextPath();
            String viewId = facesContext.getViewRoot().getViewId();

            String paramReiniciarNav = params.get(NAV_REINICIAR);
            String paramAddNavEtiqueta = params.get(VIEW_ID);
            String url = applicationPath.concat(viewId);

            if (paramReiniciarNav != null) {
                reiniciarBarraNavegacion();
                params.remove(NAV_REINICIAR);
            }
            params.remove(NAV_REDIRECCION);
            params.remove(NAV_LIMPIAR_FILTROS);

            List<String> keyParams = new ArrayList<>(params.keySet());
            Collections.sort(keyParams);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
            for (String key : keyParams) {
                uriBuilder.queryParam(key, params.get(key));
            }
            url = uriBuilder.toUriString();

            addElementoBarraNavegacion(params.get(VIEW_ID), url);

            if (listaUrls.contains(url)) {
                while (!listaUrls.getLast().equals(url)) {
                    menuModel.getElements().remove(menuItemUrl.remove(listaUrls.removeLast()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getNombreElemento(String etiqueta) {
        try {
            txt = ResourceBundle.getBundle(ficheroIdioma, controladorSesion.getCurrentLocale());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
