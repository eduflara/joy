package com.joyero.seguridad.rest.grupo;

import com.joyero.base.jsf.Controlador;
import com.joyero.seguridad.rest.funcion.Funcion;
import com.joyero.seguridad.rest.funcion.FuncionRest;
import com.joyero.seguridad.rest.funcionAsignada.FuncionAsignada;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
@Scope("session")
public class GrupoCtl extends Controlador<Grupo, Long> {

    /* Servicios Rest inyectados */

    @Autowired
    private FuncionRest funcionRest;

    /* Atributos del controlador */

    @Getter
    private TreeNode funcionesTree = new CheckboxTreeNode();

    @Getter
    @Setter
    private List<TreeNode> nodes = new LinkedList<>();

    @Getter
    private Map<String, Funcion> funcionesMap = new HashMap<>();

    @Getter
    private Map<String, Boolean> permisosAlta = new HashMap<>();

    @Getter
    private Map<String, Boolean> permisosBaja = new HashMap<>();

    @Getter
    private Map<String, Boolean> permisosModificacion = new HashMap<>();

    @Getter
    private Map<String, Boolean> permisosConsulta = new HashMap<>();

    /* Métodos propios del controlador */

    public TreeNode[] getSelectedNodes() {
        if (nodes != null) {
            TreeNode[] temp = new TreeNode[0];
            return nodes.toArray(temp);
        }
        return null;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        if (selectedNodes == null) {
            this.nodes.clear();
        }
        if (selectedNodes != null) {
            this.nodes.clear();
            this.nodes.addAll(Arrays.asList(selectedNodes));
        }
    }

    @PostConstruct
    public void cargarFunciones() {
        funcionesTree = new CheckboxTreeNode();
        List<Funcion> funciones = funcionRest.cargarFuncionesSuperiores();
        for (Funcion funcion : funciones) {
            addTreeNode(funcion, funcionesTree);
        }
        iniciarTbl();
    }

    private void addTreeNode(Funcion funcion, TreeNode treeNode) {
        List<Funcion> funciones = funcionRest.cargarFuncionesInferiores(funcion.getId());
        funcion.setFuncionesInferiores(funciones);

        TreeNode node = new CheckboxTreeNode(funcion.getIdPagina(), treeNode);
        funcionesMap.put(funcion.getIdPagina(), funcion);
        if (funcion.getFuncionesInferiores() != null
                && !funcion.getFuncionesInferiores().isEmpty()) {
            for (Funcion funcionesInferior : funcion.getFuncionesInferiores()) {
                addTreeNode(funcionesInferior, node);
            }
        }
    }

    /**
     * Función para asignar automáticamente el permiso marcado a todas las funciones hijas
     * de la función seleccionada
     *
     * @param nombreFuncion nombre de la función
     * @param modo          uno de los posibles modos de acceso a la función A,B,M,C
     */
    public void asignarHijos(String nombreFuncion, String modo) {
        boolean opcion = false;
        switch (modo) {
            case "A":
                opcion = getPermisosAlta().get(nombreFuncion);
                break;
            case "B":
                opcion = getPermisosBaja().get(nombreFuncion);
                break;
            case "M":
                opcion = getPermisosModificacion().get(nombreFuncion);
                break;
            case "C":
                opcion = getPermisosConsulta().get(nombreFuncion);
                break;
        }
        Funcion funcion = funcionesMap.get(nombreFuncion);
        funcion.getFuncionesInferiores();
        for (Funcion funcionAux : funcion.getFuncionesInferiores()) {
            switch (modo) {
                case "A":
                    getPermisosAlta().put(funcionAux.getIdPagina(), opcion);
                    break;
                case "B":
                    getPermisosBaja().put(funcionAux.getIdPagina(), opcion);
                    break;
                case "M":
                    getPermisosModificacion().put(funcionAux.getIdPagina(), opcion);
                    break;
                case "C":
                    getPermisosConsulta().put(funcionAux.getIdPagina(), opcion);
                    break;
            }
        }
    }

    private void compararNodos(TreeNode node, Funcion funcion) {
        if (node.getChildren() != null
                && node.getChildCount() > 0) {
            for (TreeNode nodeAux : node.getChildren()) {
                compararNodos(nodeAux, funcion);
            }
        }

        if (node.getData() == null || funcion.getIdPagina() == null) {
            //
        } else if (node.getData().equals(funcion.getIdPagina())) {
            nodes.add(node);
            ((CheckboxTreeNode) node).setSelected(true);
        }

    }

    /*
     * Implementar y sobrescribir métodos del controlador base
     */

    @Override
    public void nuevo() {
        entidad = new Grupo();
        postCargarSeleccion();
    }

    @Override
    public void onLoad() {
        super.onLoad();


    }

    @Override
//    @PostConstruct
    public void iniciarTbl() {
        tablemodel = new GrupoTbl(apiRest);
    }

    @Override
    public void postCargarSeleccion() {
        if (permisosAlta != null && permisosBaja != null && permisosModificacion != null && permisosConsulta != null) {
            permisosAlta.clear();
            permisosBaja.clear();
            permisosModificacion.clear();
            permisosConsulta.clear();

            cargarFunciones();

            if (entidad.getFuncionesAsginadas() != null) {
                for (FuncionAsignada funcionAsignada : entidad.getFuncionesAsginadas()) {
                    permisosAlta.put(funcionAsignada.getFuncion().getIdPagina(), funcionAsignada.getAlta());
                    permisosBaja.put(funcionAsignada.getFuncion().getIdPagina(), funcionAsignada.getBaja());
                    permisosModificacion.put(funcionAsignada.getFuncion().getIdPagina(), funcionAsignada.getModificar());
                    permisosConsulta.put(funcionAsignada.getFuncion().getIdPagina(), funcionAsignada.getConsultar());
                    compararNodos(funcionesTree, funcionAsignada.getFuncion());
                }
            }
        }
    }

    @Override
    public String verFormulario() {
        return null;
    }

    @Override
    public String verListado() {
        return null;
    }


    @Override
    public void preGuardar() {
        if (nodes != null && !nodes.isEmpty()) {
            List<String> funcionesSeleccionadas = new LinkedList<>();
            List<FuncionAsignada> funcionesEliminar = new LinkedList<>();
            for (TreeNode selectedNode : nodes) {
                String nombreFuncion = (String) selectedNode.getData();
                Funcion funcion = funcionesMap.get(nombreFuncion);

                for (String s : funcionesMap.keySet()) {
                    System.out.println("..>" + s);
                }

                funcionesSeleccionadas.add(nombreFuncion);

                FuncionAsignada funcionAsignada = new FuncionAsignada();
                funcionAsignada.setIdGrupo(entidad.getId());

                funcionAsignada.setFuncion(funcion);
                funcionAsignada.setIdFuncion(funcion.getId());

                funcionAsignada.setAlta(permisosAlta.get(nombreFuncion));
                funcionAsignada.setBaja(permisosBaja.get(nombreFuncion));
                funcionAsignada.setModificar(permisosModificacion.get(nombreFuncion));
                funcionAsignada.setConsultar(permisosConsulta.get(nombreFuncion));

                if (!entidad.getFuncionesAsginadas().contains(funcionAsignada)) {
                    entidad.getFuncionesAsginadas().add(funcionAsignada);
//                    funcionAsignada.setGrupo(entidad);
                } else {
                    for (FuncionAsignada funcionAux : entidad.getFuncionesAsginadas()) {
                        if (funcionAux.equals(funcionAsignada)) {
                            funcionAux.setAlta(permisosAlta.get(nombreFuncion));
                            funcionAux.setBaja(permisosBaja.get(nombreFuncion));
                            funcionAux.setModificar(permisosModificacion.get(nombreFuncion));
                            funcionAux.setConsultar(permisosConsulta.get(nombreFuncion));
                        }
                    }
                }
            }

            for (FuncionAsignada funcionAsignada : entidad.getFuncionesAsginadas()) {
                if (!funcionesSeleccionadas.contains(funcionAsignada.getFuncion().getIdPagina())) {
                    funcionesEliminar.add(funcionAsignada);
                }
            }
            entidad.getFuncionesAsginadas().removeAll(funcionesEliminar);
            //servicioSeguridad.eliminarFuncionesAsignadas(funcionesEliminar);
        }


    }
}
