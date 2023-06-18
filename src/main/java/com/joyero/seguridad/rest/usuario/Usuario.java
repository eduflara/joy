package com.joyero.seguridad.rest.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joyero.base.rest.Entidad;
import com.joyero.seguridad.rest.funcionAsignada.FuncionAsignada;
import com.joyero.seguridad.rest.grupo.Grupo;
import lombok.Data;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Usuario implements Entidad, UserDetails {
    private Long id;

    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    private String password;

    private Date fechaAlta;

    private Date fechaBaja;

    @NotBlank
    private String nombreCompleto;

    private Boolean activo;

    private Boolean bloqueado;
    private Boolean root;

    private String idioma;

    @Email
    private String correo;

    private String telefono;

    private String dni;

    private String modoAcceso;


    private Set<Grupo> grupos;

    private Long funcionInicial;

    @JsonIgnore
    private Locale locale;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> funcionesAsignadas = new ArrayList<>();
        if(grupos!=null) {
            for (Grupo grupo : grupos) {
                funcionesAsignadas.add(new SimpleGrantedAuthority(grupo.getNombre()));
            }
            return funcionesAsignadas;
        }
        return null;
    }

    public List<FuncionAsignada> getFuncionesAsignadas() {
        List<FuncionAsignada> funcionesAsignadas = new ArrayList<>();
        if (grupos != null) {
            for (Grupo grupo : grupos) {
                funcionesAsignadas.addAll(grupo.getFuncionesAsginadas());
            }

        }
        return funcionesAsignadas;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !bloqueado;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !bloqueado;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !bloqueado;
    }

    @Override
    public boolean isEnabled() {
        return BooleanUtils.isFalse(bloqueado);
    }
}
