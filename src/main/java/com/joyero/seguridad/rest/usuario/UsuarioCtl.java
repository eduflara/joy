package com.joyero.seguridad.rest.usuario;

import com.joyero.base.jsf.Controlador;
import com.joyero.seguridad.rest.funcionAsignada.FuncionAsignada;
import com.joyero.seguridad.rest.funcionAsignada.FuncionAsignadaRest;
import com.joyero.seguridad.rest.grupo.Grupo;
import com.joyero.seguridad.rest.grupo.GrupoRest;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Controller
@Scope(value = "session")
public class UsuarioCtl extends Controlador<Usuario, Long> {

    /* Servicios Rest inyectados */

    @Autowired
    private GrupoRest grupoRest;

    @Autowired
    private FuncionAsignadaRest funcionAsignadaRest;

    /* Atributos del controlador */

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private List<Grupo> gruposSeleccionados = new LinkedList<>();

    @Getter
    @Setter
    private List<FuncionAsignada> funcionesAsignadas;





    /* Implementar y sobrescribir métodos del controlador base  */

    @Override
    public void nuevo() {
        entidad = new Usuario();
    }

    @Override
    public void onLoad() {
        super.onLoad();

//        if (sesionControlador.getUsuario() != null) {
//            if (((Usuario) sesionControlador.getUsuario()).getIdioma() != null) {
//                sesionControlador.cambiarIdioma(((Usuario) sesionControlador.getUsuario()).getIdioma());
//            }
//        }
        if (entidad.getId() == null) {
            entidad = sesionControlador.getUsuarioActual();
        }
//        cargarDestinatariosUsuario();


    }

    @Override
    @PostConstruct
    public void iniciarTbl() {
        tablemodel = new UsuarioTbl(apiRest);
    }

    @Override
    public void postCargarSeleccion() {
        List<Grupo> grupos = grupoRest.findByUsuario(entidad.getId());
        entidad.setGrupos(new HashSet<>(grupos));

        gruposSeleccionados.clear();
        gruposSeleccionados.addAll(entidad.getGrupos());

        funcionesAsignadas = new LinkedList<>();
        for (FuncionAsignada fun : entidad.getFuncionesAsignadas()) {
            if (BooleanUtils.isTrue(fun.getFuncion().getApareceMenu()) && fun.getFuncion().getUrl() != null) {
                funcionesAsignadas.add(fun);
            }
        }
//        cargarDestinatariosUsuario();

    }

    @Override
    public void preGuardar() {
        if (entidad.getGrupos() == null) {
            entidad.setGrupos(new HashSet<>());
        }
        entidad.getGrupos().addAll(gruposSeleccionados);
        entidad.getGrupos().removeAll(getGruposNoSeleccionados());
        if (StringUtils.isNotBlank(password)) {
            MessageDigestPasswordEncoder messageDigestPasswordEncoder = new MessageDigestPasswordEncoder("sha");
            entidad.setPassword(messageDigestPasswordEncoder.encode(password));
        }
        entidad.setBloqueado(false);
    }

    @Override
    public void postGuardar() {
        //para limpiar los gruposDistribucion seleccionados una vez se guarde el registro
        gruposSeleccionados.clear();
    }


    @Override
    public String verFormulario() {
        return null;
    }

    @Override
    public String verListado() {
        return null;
    }


    /*
     * Métodos de aceso
     */

    public List<Grupo> getGrupos() {
        List<Grupo> grupos = grupoRest.getCollection();
        return grupos;
    }

    public List<Grupo> getGruposNoSeleccionados() {
        List<Grupo> gruposNoSeleccionados = new LinkedList<>(getGrupos());
        gruposNoSeleccionados.removeAll(gruposSeleccionados);
        return gruposNoSeleccionados;
    }


    public String paginaInicial() {
        if (sesionControlador.getUsuario() != null) {
            if (((Usuario) sesionControlador.getUsuario()).getFuncionInicial() != null) {

                try {
                    FuncionAsignada funcionAsignada = funcionAsignadaRest.get(((Usuario) sesionControlador.getUsuario()).getFuncionInicial());
                    barraNavegacionControlador.addElementoBarraNavegacion("inicio", "/pages/index.xhtml");
                    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                    String contexto = ec.getRequestContextPath();
                    if (contexto.isEmpty()) {
                        contexto = "/";
                    }

                    String url = funcionAsignada.getFuncion().getUrl();
//                    url = url.substring(url.indexOf('/'), url.length());
//                    redirigir(funcionAsignada.getFuncion().getIdPagina(), contexto + url);

                    barraNavegacionControlador.addElementoBarraNavegacion(funcionAsignada.getFuncion().getIdPagina(), contexto + url);
                    ec.redirect(contexto + url);
                } catch (IOException ex) {
                    System.out.println(ex);
                }


            }
        }
        return null;
    }

    public TreeNode getRoot() {
        TreeNode root;
        root = new DefaultTreeNode("Root", null);
        TreeNode node0 = new DefaultTreeNode("Documents", "11/C/13/0000118-1", root);


        TreeNode node00 = new DefaultTreeNode("document", "Petición de información", node0);


        TreeNode node001 = new DefaultTreeNode("document", "Contestación", node00);
        TreeNode node002 = new DefaultTreeNode("document", "Informe", node00);

        return root;


    }

    private TreeNode selectedNode;

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void onNodeSelect() {
        System.out.println("Meter el documento y refrescar");
    }

    public List<String> getModosAcceso(){
        return new LinkedList<>();
    }


}
