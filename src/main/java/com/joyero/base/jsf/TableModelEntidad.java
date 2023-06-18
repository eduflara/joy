package com.joyero.base.jsf;

import com.joyero.base.rest.ApiRest;
import com.joyero.base.rest.Entidad;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @param <E>
 * @author Alejandro
 */
public abstract class TableModelEntidad<E extends Entidad, ID extends Serializable> extends LazyDataModel<E> {

    private ApiRest<E, ID> apiRest;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private E seleccion;

    @Getter
    @Setter
    private List<E> seleccionMultiple;

    @Setter
    @Getter
    private List<E> datasource;

    @Getter
    private final Map<String, Object> filtros = new HashMap<>();

    @Getter
    private final MultiValueMap<String, Object> filtrosMultiValue = new LinkedMultiValueMap<>();

    @Getter
    @Setter
    private boolean limpiarFiltros = false;

    public TableModelEntidad() {
    }

    public TableModelEntidad(ApiRest<E, ID> apiRest) {
        this.apiRest = apiRest;
    }

    public TableModelEntidad(ApiRest<E, ID> apiRest, String url) {
        this(apiRest);
        this.url = url;
    }

    @Override
    public E getRowData(String rowKey) {
        E row = null;
        for (E obj : datasource) {
            if ((obj.getId().toString()).equals(rowKey)) {
                row = obj;
            }
        }
        return row;
    }

    @Override
    public Object getRowKey(E obj) {
        if (obj != null && obj.getId() != null) {
            return obj.getId().toString();
        }
        return null;
    }

    public List<E> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, FilterMeta> filterBy) {

        filterBy.entrySet().removeIf(
                filter -> filter.getValue().getFilterValue() == null
        );

        Map<String, Object> filters = filterBy.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey(),
                entry -> entry.getValue().getFilterValue()
        ));

        asignarFiltros(filters);
        Long total = null;
        if (url != null) {
            total = apiRest.getCount(filters, url);
        } else {
            total = apiRest.getCount(filters);
        }

        setRowCount(total.intValue());
        List<E> resultado = null;
        if (url != null) {
            resultado = apiRest.getCollection(first, pageSize, sortField, sortOrder, filters, url);
        } else {
            resultado = apiRest.getCollection(first, pageSize, sortField, sortOrder, filters);
        }
        datasource = resultado;
        getFiltros().putAll(filters);

        return getDatasource();
    }


    //TODO
    @Override
    public List<E> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        return null;
    }

    public void limpiarSeleccion() {
        seleccion = null;
        seleccionMultiple = null;
    }

    public void limpiarFiltros() {
        limpiarFiltros = true;
    }

    public void limpiarFiltrosTableModel() {
        this.filtros.clear();
    }

    public void asignarFiltros(Map<String, Object> filtros) {
        if (limpiarFiltros) {
            filtros.clear();
            this.filtros.clear();
            limpiarFiltros = false;
        } else {

            for (Map.Entry entry : this.filtros.entrySet()) {
                filtros.putIfAbsent(entry.getKey().toString(), entry.getValue());
            }

            filtros.entrySet().removeIf(filtro -> filtro.getValue() == null || filtro.getValue().equals(""));

        }
    }

    public void processorXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
        }

        for (int colNum = 0; colNum < header.getLastCellNum(); colNum++) {
            wb.getSheetAt(0).autoSizeColumn(colNum);
        }

    }

    public void processorPDF(Object document) {

    }

}
