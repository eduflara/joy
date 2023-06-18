package com.joyero.seguridad.servicio;

import com.joyero.base.jsf.JsfUtils;
import com.joyero.seguridad.rest.usuario.Usuario;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alejandro on 16/08/2016.
 */
@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
//        setDefaultTargetUrl("/pages/index.xhtml");
        super.onAuthenticationSuccess(request, response, authentication);

        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
        String ip = webAuthenticationDetails.getRemoteAddress();
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String sessionId = webAuthenticationDetails.getSessionId();
        String password = request.getParameter("password");

        String plainCreds = usuario.getUsername() + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        JsfUtils.putToSession("btoa", base64Creds);

        //pagina de redireccion
        setDefaultTargetUrl("/pages/index.xhtml");

//        RegistroAcceso registroAcceso = new RegistroAcceso(ip, usuario, sessionId);
//        servicioSeguridad.guardarRegistroAcceso(registroAcceso);
    }
}
