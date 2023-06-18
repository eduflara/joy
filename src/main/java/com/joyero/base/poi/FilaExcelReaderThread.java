package com.joyero.base.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author Eduardo
 */
public class FilaExcelReaderThread implements Runnable {

    private Row row;
    private FilaExcel filaExcel;

    public FilaExcelReaderThread(Row row, FilaExcel filaExcel) {
        this.row = row;
        this.filaExcel = filaExcel;
    }

    @Override
    public void run() {
        for (int i = 0; i < 30; i++) {
            Cell cell = null;
            cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

            if(CellType.NUMERIC.equals(cell.getCellType())) {
                double valorD = cell.getNumericCellValue();
                cell.setCellType(CellType.STRING);
                String valorString = cell.getStringCellValue();
                filaExcel.putValue(i, valorString);
            }

            if(CellType.STRING.equals(cell.getCellType())) {
                String valorS = cell.getStringCellValue();
                filaExcel.putValue(i, valorS);
            }

            if(CellType.BLANK.equals(cell.getCellType())) {
                filaExcel.putValue(i, "");
            }

        }
    }
}
