package com.joyero.base.jsf;

import com.joyero.base.rest.Entidad;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.Serializable;

/**
 * @author alejandro
 */
public class Conversor implements Converter {


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        Entidad objeto = null;
        try {
            if (StringUtils.isNotBlank(string) && string.contains("|")) {
                String[] partes = string.split("|");
                Class clazz = Class.forName(partes[0]);
                Serializable id = partes[1];
                // servicio.
            }
        } catch (Exception ex) {
            Controlador.addErrorMessage(uic.getClientId(), ex);
        }
        return objeto;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o != null && o instanceof Entidad) {
            Entidad entidad = (Entidad) o;
            return entidad.getClass() + "|" + entidad.getId();
        } else {
            return "";
        }
    }

}
