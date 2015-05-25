/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control.model;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class TableMapClass {
    private String tableName;
    private String columnName;
    private ArrayList<String> values;

    public TableMapClass(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.values = new ArrayList<String>();
    }

    //Override to customize values.
    public Object translateValue(String value) {
        return value;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public String getValueAt(int row) {
        return values.get(row);
    }

    
    public void addValue(String value) {
        this.values.add(value);
    }

    public ArrayList<String> getValues() {
        return this.values;
    }
}
