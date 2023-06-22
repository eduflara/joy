package com.joyero.base.jsf;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:c:/sumainfo/config/joy.properties")
public class Config {

    @Autowired
    @Getter
    private Environment environment;

    public String getUrlRest() {
        String urlRest = environment.getProperty("urlRest");
        return urlRest;
    }


}
