/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.*;
import java.util.ArrayList;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
// *
 * @author vnguyen
 */
public class GenericExportController {

    private boolean isUskedExport;
    private RMIScheduleServerImpl myConn = RMIScheduleServerImpl.getInstance();
    private DBMapper dbMapper;
    private String company;
    private ArrayList<DataMappingClass> selectedDisplayToColumns;
    private Object[] params;

    public GenericExportController(String co, ArrayList<DataMappingClass> selectedDisplayToColumns,
            boolean isUskedExport, DBMapper dbMapper, Object[] params) {
        this.isUskedExport = isUskedExport;
        this.dbMapper = dbMapper;
        this.params = params;
        this.company = co;
        this.selectedDisplayToColumns = selectedDisplayToColumns;
    }

    public void exportExcel97(File file) throws FileNotFoundException, IOException {
        Record_Set recordSet = this.getRecordSet();

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle rowstyle1 = wb.createCellStyle();
        HSSFFont frows = wb.createFont();

        frows.setFontHeight((short)7);
        frows.setColor(IndexedColors.DARK_GREEN.getIndex());

        rowstyle1.setFont(frows);
        rowstyle1.setWrapText(true);
        rowstyle1.setAlignment(XSSFCellStyle.ALIGN_LEFT);

        HSSFSheet st = wb.createSheet("1");

        HSSFRow firstrow = st.createRow(0);
        firstrow.setHeightInPoints(35);

        for (int c = 0; c < selectedDisplayToColumns.size(); c++) {
            if (this.isUskedExport) {
                firstrow.createCell(c).setCellValue(selectedDisplayToColumns.get(c).getUskedId());
            } else {
                firstrow.createCell(c).setCellValue(selectedDisplayToColumns.get(c).getFirstValue());
            }
        }
        HSSFRow row;
        for (int cr = 0; cr < recordSet.length(); cr++) {
            row = st.createRow(cr + 1);
            row.setHeightInPoints(20);

            for (int c = 0; c < selectedDisplayToColumns.size(); c++) {
                HSSFCell temp = row.createCell(c);
                TableMapClass currMapping = selectedDisplayToColumns.get(c).getTableMapping();
                try {
                    Object value = currMapping.translateValue(recordSet.getString(currMapping.getColumnName()));
                    if (value instanceof String) {
                        temp.setCellValue((String)value);
                    } else if (value instanceof String) {
                        temp.setCellValue((Date)value);
                    }
                } catch (NullPointerException npe) {
                    temp.setCellValue("");
                }
                row.getCell(c).setCellStyle(rowstyle1);
            }
            recordSet.moveNext();
        }

        st.createFreezePane(0, 1);
        HSSFCellStyle colnamestyle = wb.createCellStyle();
        HSSFFont fcoln = wb.createFont();
        fcoln.setFontHeight((short)10);
        fcoln.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        fcoln.setColor(IndexedColors.DARK_BLUE.getIndex());
        colnamestyle.setFont(fcoln);
        colnamestyle.setWrapText(true);
        colnamestyle.setAlignment(XSSFCellStyle.ALIGN_CENTER_SELECTION);

        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.flush();
        out.close();
    }

    public void exportExcel07(File file) throws FileNotFoundException, IOException {
        Record_Set recordSet = this.getRecordSet();

        // Set up work book object
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle rowstyle1 = wb.createCellStyle();
        XSSFFont frows = wb.createFont();

        frows.setFontHeight(7);
        frows.setColor(IndexedColors.DARK_GREEN.getIndex());

        rowstyle1.setFont(frows);
        rowstyle1.setWrapText(true);
        rowstyle1.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        XSSFSheet st = wb.createSheet("1");

        XSSFRow firstrow = st.createRow(0);
        firstrow.setHeightInPoints(35);

        for (int c = 0; c < selectedDisplayToColumns.size(); c++) {
            if (this.isUskedExport) {
                firstrow.createCell(c).setCellValue(selectedDisplayToColumns.get(c).getUskedId());
            } else {
                firstrow.createCell(c).setCellValue(selectedDisplayToColumns.get(c).getFirstValue());
            }
        }
        XSSFRow row;
        for (int cr = 0; cr < recordSet.length(); cr++) {
            row = st.createRow(cr + 1);
            row.setHeightInPoints(20);

            for (int c = 0; c < selectedDisplayToColumns.size(); c++) {
                XSSFCell temp = row.createCell(c);
                TableMapClass currMapping = selectedDisplayToColumns.get(c).getTableMapping();
                try {
                    Object value = currMapping.translateValue(recordSet.getString(currMapping.getColumnName()));
                    if (value instanceof String) {
                        temp.setCellValue((String)value);
                    } else if (value instanceof String) {
                        temp.setCellValue((Date)value);
                    }
                } catch (NullPointerException npe) {
                    temp.setCellValue("");
                }
                row.getCell(c).setCellStyle(rowstyle1);
            }
            recordSet.moveNext();
        }

        st.createFreezePane(0, 1);
        XSSFCellStyle colnamestyle = wb.createCellStyle();
        XSSFFont fcoln = wb.createFont();
        fcoln.setFontHeight(10);
        fcoln.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        fcoln.setColor(IndexedColors.DARK_BLUE.getIndex());
        colnamestyle.setFont(fcoln);
        colnamestyle.setWrapText(true);
        colnamestyle.setAlignment(XSSFCellStyle.ALIGN_CENTER_SELECTION);

        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.flush();
        out.close();
    }

    public void exportExcel(File file, boolean useLatestExcel) throws FileNotFoundException, IOException {
        if (useLatestExcel) {
            this.exportExcel07(file);
        } else {
            this.exportExcel97(file);
        }
    }

    private Record_Set getRecordSet() {
        GeneralQueryFormat query = dbMapper.generateQuery(this.params);
        query.setCompany(this.company);
        try {
            return myConn.executeQuery(query, "");
        } catch (Exception e) {
            System.out.println("There was an error running the query: " + e.getMessage());
            return null;
        }
    }
}
