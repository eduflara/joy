package com.joyero.seguridad.servicio;


import com.joyero.seguridad.rest.usuario.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alejandro on 16/08/2016.
 */
@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
            String ip = webAuthenticationDetails.getRemoteAddress();
            Usuario usuario = (Usuario) authentication.getPrincipal();
            String sessionId = webAuthenticationDetails.getSessionId();

//            RegistroAcceso registroAcceso = new RegistroAcceso(ip, usuario, sessionId);
//            registroAcceso.setCierreSesion(true);
//            servicioSeguridad.guardarRegistroAcceso(registroAcceso);
        }
//        setDefaultTargetUrl("/pages/index.xhtml");
        super.onLogoutSuccess(request, response, authentication);
    }
}
