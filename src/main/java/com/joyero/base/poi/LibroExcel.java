package com.joyero.base.poi;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Eduardo
 */
public class LibroExcel {

    private int lineaEmpieza;
    private InputStream inputStream;
    private List<FilaExcel> filas;
    private String error;

    public int getLineaEmpieza() {
        return lineaEmpieza;
    }

    public void setLineaEmpieza(int lineaEmpieza) {
        this.lineaEmpieza = lineaEmpieza;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

/*    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }*/

    public List<FilaExcel> getFilas() {
        return filas;
    }

    public void cargaHoja() {

        Workbook libro = null;
        boolean hayError = false;
        int linea = lineaEmpieza;
        filas = new ArrayList<>();
        try {
            libro = WorkbookFactory.create(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Sheet hoja = libro.getSheetAt(0);

        List<Thread> threads = new LinkedList<>();

        while (linea <= hoja.getLastRowNum() && !hayError) {
            try {
                /*System.out.println("linea"+linea);*/
                Row row = hoja.getRow(linea);
                FilaExcel lecturaExcelFila = new FilaExcel(linea);

                filas.add(lecturaExcelFila);

                FilaExcelReaderThread filaExcelReaderThread = new FilaExcelReaderThread(row, lecturaExcelFila);
                Thread thread = new Thread(filaExcelReaderThread, "[FilaExcelReader-" + linea + "]");
                thread.start();
                threads.add(thread);


/*                if (error == null) {
                    filas.add(lecturaExcelFila);
                } else {
                    hayError = true;
                }*/
                linea++;
            } catch (Exception e) {
                error = e.getMessage();
                hayError = true;
            }

        }

        for (Thread thread : threads) {
            try {
                thread.join();
                    /*System.out.println("-JOIN-" + thread.getName());*/
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public String getError() {
        return error;
    }


}
