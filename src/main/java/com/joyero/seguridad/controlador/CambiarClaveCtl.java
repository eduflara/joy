package com.joyero.seguridad.controlador;

import com.joyero.base.jsf.Controlador;
import com.joyero.base.jsf.JsfUtils;
import com.joyero.seguridad.rest.usuario.Usuario;
import com.joyero.seguridad.rest.usuario.UsuarioRest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;

@Controller
@Scope("session")
public class CambiarClaveCtl {
    private ControladorSesion controladorSesion;
    private String nombreCompleto;
    private String password;
    private UsuarioRest servicioSeguridad;

    @Autowired
    public void ControladorCambiarClave(ControladorSesion controladorSesion,
                                        UsuarioRest servicioSeguridad) {
        this.controladorSesion = controladorSesion;
        this.servicioSeguridad = servicioSeguridad;
    }


    public void cambiarClave() {
        Usuario usuario = (Usuario) controladorSesion.getUsuario();
        MessageDigestPasswordEncoder messageDigestPasswordEncoder = new MessageDigestPasswordEncoder("sha");
        usuario.setPassword(messageDigestPasswordEncoder.encode(password));
        servicioSeguridad.save(usuario);

        //Actualizar el token de seguridad
        String plainCreds = usuario.getUsername() + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        JsfUtils.putToSession("btoa", base64Creds);

        Controlador.hideDialog("cambiarContrasenaDlg");
        Controlador.showMessageDialog("info.contrasenaCambiada");
    }

    public Usuario getUsuario() {
        return (Usuario) controladorSesion.getUsuario();
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
