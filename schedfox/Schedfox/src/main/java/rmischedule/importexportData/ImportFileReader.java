/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.importexportData;

import rmischedule.importexportData.ImportExportEmployeeFrame;
import rmischeduleserver.control.model.MappingClass;
import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.util.Vector;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.*;
import java.util.Random;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;

/**
 *
 * @author vnguyen
 */
public class ImportFileReader {

    private String fileName;
    private Connection myConn;
    private int totalrows = 0;
    private int insertedrows = 0;
    private int updatedrows = 0;
    private ArrayList<String> columnHeaders;
    private ArrayList<ArrayList<String>> excelData;

    public ImportFileReader(String fileName, Connection myConn) throws Exception {
        this.fileName = fileName;
        this.myConn = myConn;
    }

    /**
     * Reads the file into two seperate ArrayLists, one for the column headers (row 0),
     * and the rest for the actual data.
     * @throws Exception
     */
    public void processFile() throws Exception {
        readFileIntoArrayLists();
        this.checkInputSize();
    }

    /**
     * Returns the parsed column headers, must be run after processFile.
     * @return
     */
    public ArrayList<String> getColumnHeaders() {
        return this.columnHeaders;
    }

    /**
     * Returns the parsed data, must be run after processFile.
     * @return
     */
    public ArrayList<ArrayList<String>> getFileData() {
        return this.excelData;
    }

    private void readFileIntoArrayLists() throws Exception {
        FileInputStream fs = new FileInputStream(this.fileName);
        Workbook wb = WorkbookFactory.create(fs);
        Sheet sheet = wb.getSheetAt(0);
        int rows;
        rows = sheet.getPhysicalNumberOfRows();

        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < rows; i++) {
            Row aRow = sheet.getRow(i);
            ArrayList<String> dataRow = new ArrayList<String>();
            for (int c = sheet.getRow(i).getFirstCellNum(); c < sheet.getRow(i).getLastCellNum(); c++) {
                Cell currCell = aRow.getCell(c);
                String val = "";
                if (currCell != null) {
                    int type = currCell.getCellType();
                    if (type == currCell.CELL_TYPE_FORMULA) {
                        val = currCell.getStringCellValue();
                    } else if (type == currCell.CELL_TYPE_BOOLEAN) {
                        val = Boolean.toString(currCell.getBooleanCellValue());
                    } else if (type == currCell.CELL_TYPE_NUMERIC) {
                        val = Double.toString(currCell.getNumericCellValue());
                    } else {
                        val = currCell.getStringCellValue();
                    }
                }
                dataRow.add(val);
            }
            data.add(dataRow);
        }

        this.columnHeaders = data.get(0);

        data.remove(this.columnHeaders);

        this.excelData = data;
    }

    /**
     * Gets the data for actual file, and adds it to the passed in StringBuffer
     * object.
     * @param data
     * @param output
     */
    private void printData(ArrayList<ArrayList<String>> data, StringBuffer output) {
        int i = 0;
        for (ArrayList<String> a : data) {
            output.append("Row " + i + " :  ");
            i++;

            for (String b : a) {
                output.append(b + "\t\t\r\n");
            }

        }
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        for (String s : this.columnHeaders) {
            output.append("\t" + s);
        }

        output.append("\r\n");
        this.printData(excelData, output);
        return output.toString();
    }

    private void checkInputSize() throws Exception {
        for (ArrayList<String> f : this.excelData) {
            if (this.columnHeaders.size() != f.size()) {
                //throw new Exception("Improper File formating, make sure all columns and rows line up");
            }
        }

    }
}
