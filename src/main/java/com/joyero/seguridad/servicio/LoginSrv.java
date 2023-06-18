package com.joyero.seguridad.servicio;


import com.joyero.seguridad.rest.funcionAsignada.FuncionAsignada;
import com.joyero.seguridad.rest.funcionAsignada.FuncionAsignadaRest;
import com.joyero.seguridad.rest.grupo.Grupo;
import com.joyero.seguridad.rest.grupo.GrupoRest;
import com.joyero.seguridad.rest.usuario.Usuario;
import com.joyero.seguridad.rest.usuario.UsuarioRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by alejandro on 22/07/2016.
 */
@Service
public class LoginSrv implements UserDetailsService {

    @Autowired
    private UsuarioRest usuarioRest;

    @Autowired
    private GrupoRest grupoRest;

    @Autowired
    private FuncionAsignadaRest funcionAsignadaRest;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = null;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String password = request.getParameter("password");
        Locale locale = new Locale(request.getParameter("locale"));

        try {
            boolean login = usuarioRest.login(username);
            if (login) {
                usuario = usuarioRest.findByUsername(username, password);
                List<Grupo> grupos = grupoRest.findByUsuario(usuario.getId(), username, password);
                for (Grupo grupo : grupos) {
                    List<FuncionAsignada> funcionesAsignadas = funcionAsignadaRest.findByGrupo(grupo.getId(), username, password);
                    grupo.setFuncionesAsginadas(new HashSet<>(funcionesAsignadas));
                }
                usuario.setGrupos(new HashSet<>(grupos));

            } else {
                throw new UsernameNotFoundException(username);
            }
        } catch (HttpClientErrorException ex) {
            throw new UsernameNotFoundException(username, ex);
        }
        usuario.setLocale(locale);
        return usuario;

    }


}
