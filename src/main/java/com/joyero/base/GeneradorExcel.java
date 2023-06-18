package com.joyero.base;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.BeanUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by desarrollo on 22/03/2017.
 */
public class GeneradorExcel {

    Map<String, Integer> dato;
    private final Logger logger = Logger.getLogger("GeneradorExcel");
    private List<List<Object>> datos;
    private List<String> columnas;
    private String nombreHoja;
    private HSSFWorkbook libro;
    private XSSFWorkbook libroXlsx;
    private boolean orientacionHorizontal;

    private Map<String, BigDecimal> saboresP1;
    private Map<String, BigDecimal> saboresP2;

    public HSSFWorkbook getLibro() {
        return libro;
    }

    public void setLibro(HSSFWorkbook libro) {
        this.libro = libro;
    }

    public XSSFWorkbook getLibroXlsx() {
        return libroXlsx;
    }

    public void setLibroXlsx(XSSFWorkbook libroXlsx) {
        this.libroXlsx = libroXlsx;
    }

    public GeneradorExcel(InputStream file) {
        try {
            cargaMap();
            this.libro = new HSSFWorkbook(file);
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, ex.getMessage(), ex);
            
        }
        orientacionHorizontal = false;
    }

    public GeneradorExcel(InputStream file, String version) {
        try {
            cargaMap();
            this.libroXlsx = new XSSFWorkbook(file);

        } catch (Exception ex) {
            this.logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        orientacionHorizontal = false;
    }

    public GeneradorExcel(List<List<Object>> datos, List<String> columnas, String nombreHoja, InputStream file) {
        this.datos = datos;
        this.columnas = columnas;
        this.nombreHoja = nombreHoja;
        try {
            cargaMap();
            this.libro = new HSSFWorkbook(file);
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        orientacionHorizontal = false;
    }

    public void asignaValor(HSSFSheet hoja, int numeroFila, String numeroColumna, Object valor) {
        try {
            HSSFRow fila = hoja.getRow(numeroFila);
            HSSFCell celda = null;
            if (fila == null) {
                fila = hoja.createRow(numeroFila);
            }
            celda = fila.getCell(dato.get(numeroColumna), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (celda == null) {
                celda = fila.createCell(dato.get(numeroColumna));
            }
            if (valor instanceof String) celda.setCellValue((String) valor);
            else if (valor instanceof Date) {
                CellStyle estiloFecha = libro.createCellStyle();
                CreationHelper createHelper = libro.getCreationHelper();
                //estiloFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:MM"));
                //estiloFecha.setFillBackgroundColor(HSSFColor.RED.index);
                celda.setCellValue((Date) valor);
                //celda.setCellStyle(estiloFecha);
            } else if (valor instanceof Double) celda.setCellValue((Double) valor);
            else if (valor instanceof Integer) celda.setCellValue((Integer) valor);
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, e.getMessage(), e);
        }
        ;
    }

    public void asignaValor(XSSFSheet hoja, int numeroFila, String numeroColumna, Object valor, CellStyle style) {
        try {
            int filaReal = numeroFila - 1;
            XSSFRow fila = hoja.getRow(filaReal);
            XSSFCell celda = null;
            CellStyle stiloPropio = libroXlsx.createCellStyle();
            if (style != null)
                BeanUtils.copyProperties(style, stiloPropio);
            else
                stiloPropio = libroXlsx.createCellStyle();
            if (fila == null) {
                fila = hoja.createRow(filaReal);
            }
            celda = fila.getCell(dato.get(numeroColumna), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (celda == null) {
                celda = fila.createCell(dato.get(numeroColumna));
            }

            if (valor instanceof Date) {
                CellStyle estiloFecha = libroXlsx.createCellStyle();
                CreationHelper createHelper = libroXlsx.getCreationHelper();
                estiloFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:MM"));
                estiloFecha.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                celda.setCellValue((Date) valor);
            } else if (valor instanceof String) celda.setCellValue((String) valor);
            else if (valor instanceof Double) {
                celda.setCellValue((Double) valor);
                celda.setCellType(CellType.NUMERIC);
                if (style != null) {
                    CreationHelper createHelper = libroXlsx.getCreationHelper();
                    stiloPropio.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
                }
            } else if (valor instanceof Integer) {
                celda.setCellValue((Integer) valor);
                //celda.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                if (style != null) {
                    CreationHelper createHelper = libroXlsx.getCreationHelper();
                    stiloPropio.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));
                }
            } else if (valor instanceof BigDecimal) {
                celda.setCellValue(((BigDecimal) valor).doubleValue());
                celda.setCellType(CellType.NUMERIC);
                if (style != null) {
                    CreationHelper createHelper = libroXlsx.getCreationHelper();
                    stiloPropio.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
                }

            } else if (valor instanceof Long) celda.setCellValue(Long.toString((Long) valor));

            if (style != null)
                celda.setCellStyle(stiloPropio);
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, e.getMessage(), e);
        }
        ;
    }

    public void asignaValorHora(XSSFSheet hoja, int numeroFila, String numeroColumna, Date valor, CellStyle style) {
        try {
            int filaReal = numeroFila - 1;
            XSSFRow fila = hoja.getRow(filaReal);
            XSSFCell celda = null;
            DateFormat hourFormat = new SimpleDateFormat("HH:mm");
            if (fila == null) {
                fila = hoja.createRow(filaReal);
            }
            celda = fila.getCell(dato.get(numeroColumna), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (celda == null) {
                celda = fila.createCell(dato.get(numeroColumna));
            } else if (valor instanceof Date) {
                CellStyle estiloFecha = libroXlsx.createCellStyle();
                CreationHelper createHelper = libroXlsx.getCreationHelper();
                estiloFecha.setDataFormat(createHelper.createDataFormat().getFormat("HH:MM"));
                estiloFecha.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                celda.setCellValue(hourFormat.format(valor));
                //celda.setCellStyle(estiloFecha);
            }
            if (style != null)
                celda.setCellStyle(style);
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, e.getMessage(), e);
        }
        ;
    }

    public void actualizarFormula(XSSFSheet hoja, int numeroFila, String numeroColumna) {
        try {
            int filaReal = numeroFila - 1;
            XSSFRow fila = hoja.getRow(filaReal);
            XSSFCell celda = null;
            if (fila == null) {
                fila = hoja.createRow(filaReal);
            }
            celda = fila.getCell(dato.get(numeroColumna), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            celda.setCellFormula(celda.getCellFormula());

        } catch (Exception e) {
            this.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void asignaValor(XSSFSheet hoja, int numeroFila, int numeroColumna, Object valor, CellStyle style) {
        try {
            XSSFRow fila = hoja.getRow(numeroFila);
            XSSFCell celda = null;
            CellStyle stiloPropio = libroXlsx.createCellStyle();
            if (style != null)
                BeanUtils.copyProperties(style, stiloPropio);
            else
                stiloPropio = libroXlsx.createCellStyle();

            if (fila == null) {
                fila = hoja.createRow(numeroFila);
            }
            celda = fila.getCell(numeroColumna, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (celda == null) {
                celda = fila.createCell(numeroColumna);
            }
            if (valor instanceof String) celda.setCellValue((String) valor);
            else if (valor instanceof Date) {
                CellStyle estiloFecha = libro.createCellStyle();
                CreationHelper createHelper = libro.getCreationHelper();
                //estiloFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:MM"));
                //estiloFecha.setFillBackgroundColor(HSSFColor.RED.index);
                celda.setCellValue((Date) valor);
                //celda.setCellStyle(estiloFecha);
            } else if (valor instanceof Double) celda.setCellValue((Double) valor);
            else if (valor instanceof Integer) celda.setCellValue((Integer) valor);
            else if (valor instanceof BigDecimal) celda.setCellValue(((BigDecimal) valor).intValue());
            if (style != null)
                celda.setCellStyle(stiloPropio);
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void asignaValorRowSpan(XSSFSheet hoja, int numeroFilaIni, int numeroFilaDest, String numeroColumna, Object valor, CellStyle style) {
        try {
            int filaInicial = numeroFilaIni - 1;
            int filaFinal = numeroFilaDest - 1;


            XSSFRow fila = hoja.getRow(filaInicial);
            XSSFCell celda = null;
            if (fila == null) {
                fila = hoja.createRow(filaInicial);
            }
            celda = fila.getCell(dato.get(numeroColumna), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (celda == null) {
                celda = fila.createCell(dato.get(numeroColumna));
            }
            if (valor instanceof String) celda.setCellValue((String) valor);
            else if (valor instanceof Date) {
                CellStyle estiloFecha = libroXlsx.createCellStyle();
                CreationHelper createHelper = libroXlsx.getCreationHelper();
                estiloFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:MM"));
                estiloFecha.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
                celda.setCellValue((Date) valor);
                //celda.setCellStyle(estiloFecha);
            } else if (valor instanceof Double) celda.setCellValue((Double) valor);
            else if (valor instanceof Integer) celda.setCellValue((Integer) valor);
            else if (valor instanceof BigDecimal) celda.setCellValue(((BigDecimal) valor).doubleValue());
            else if (valor instanceof Long) celda.setCellValue(Long.toString((Long) valor));
            celda.setCellStyle(style);


            CellRangeAddress cellRangeAddress = new CellRangeAddress(filaInicial, filaFinal, dato.get(numeroColumna), dato.get(numeroColumna));
            hoja.addMergedRegion(cellRangeAddress);

            //libroXlsx.getCreationHelper().createFormulaEvaluator().notifyUpdateCell(celda);
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void asignaValorCellSpan(XSSFSheet hoja, int numeroFila, String columnaIni, String columnaFin, Object valor, CellStyle style) {
        try {
            int filaInicial = numeroFila - 1;

            CellRangeAddress cellRangeAddress = new CellRangeAddress(filaInicial, filaInicial, dato.get(columnaIni), dato.get(columnaFin));
            hoja.addMergedRegion(cellRangeAddress);

            asignaValor(hoja, numeroFila, columnaIni, valor, style);
            XSSFRow fila = hoja.getRow(filaInicial);
            for (int i = (dato.get(columnaIni) + 1); i < (dato.get(columnaFin) + 1); i++) {
                XSSFCell celda = null;
                if (fila == null) {
                    fila = hoja.createRow(filaInicial);
                }
                celda = fila.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (celda == null) {
                    celda = fila.createCell(i);
                }
                celda.setCellStyle(style);
            }

            //libroXlsx.getCreationHelper().createFormulaEvaluator().notifyUpdateCell(celda);
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public byte[] getBytes() {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            libro.write(out);
            return out.toByteArray();
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                this.logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public byte[] getBytes2007() {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            libroXlsx.write(out);
            return out.toByteArray();
        } catch (Exception ex) {
            this.logger.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                this.logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public void setOrientacionHorizontal(boolean orientacionHorizontal) {
        this.orientacionHorizontal = orientacionHorizontal;
    }

    private void cargaMap() {
        dato = new HashMap<>();
        dato.put("A", 0);
        dato.put("B", 1);
        dato.put("C", 2);
        dato.put("D", 3);
        dato.put("E", 4);
        dato.put("F", 5);
        dato.put("G", 6);
        dato.put("H", 7);
        dato.put("I", 8);
        dato.put("J", 9);
        dato.put("K", 10);
        dato.put("L", 11);
        dato.put("M", 12);
        dato.put("N", 13);
        dato.put("O", 14);
        dato.put("P", 15);
        dato.put("Q", 16);
        dato.put("R", 17);
        dato.put("S", 18);
        dato.put("T", 19);
        dato.put("V", 20);
        dato.put("W", 21);
        dato.put("X", 22);
        dato.put("Z", 23);
    }
}