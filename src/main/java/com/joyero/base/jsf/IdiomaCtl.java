package com.joyero.base.jsf;

import lombok.Getter;
import lombok.Setter;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class IdiomaCtl {

    @Getter
    @Setter
    private Locale locale;

    public IdiomaCtl() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    public List<Locale> getSupportedLocales() {
        List<Locale> locales = new ArrayList<>();
        for (Iterator<Locale> it = FacesContext.getCurrentInstance().getApplication().getSupportedLocales(); it.hasNext(); ) {
            locales.add(it.next());
        }
        return locales;
    }

    public void localeChangeListener(ValueChangeEvent changeEvent) {
        locale = ((Locale) changeEvent.getNewValue());
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
}
