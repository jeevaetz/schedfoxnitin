/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.xprint.data;

/**
 *
 * @author Ira
 */
public class CrossTabRow {

    private Integer rowHeader;
    private String columnHeader;
    private String rowData;

    public CrossTabRow(int rowH, String columnH, String rowD) {
        this.rowHeader = new Integer(rowH);
        this.columnHeader = columnH;
        this.rowData = rowD;
    }

    public Integer getRowHeader() {
        return this.rowHeader;
    }

    public String getColumnHeader() {
        return this.columnHeader;
    }

    public String getRowData() {
        return this.rowData;
    }
    }
