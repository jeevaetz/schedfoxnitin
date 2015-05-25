/*
 * SourceClass.java
 *
 * Created on February 21, 2006, 1:36 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.data_import.dataImportClasses;

/**
 *
 * @author Ira Juneau
 */
public class SourceClass {
    
    private TableClass myTable;
    private String myColumn;
    private boolean hasData;
    
    /** Creates a new instance of SourceClass */
    public SourceClass(TableClass tableName, String columnName) {
        myTable = tableName;
        tableName.addFieldToTableSelect(this);
        myColumn = columnName;
        hasData = true;
    }
    
    public SourceClass() {
        hasData = false;
        myTable = null;
        myColumn = "";
    }
    
    /**
     * Has this data been defined as having a source for this particular export type..
     */
    public boolean hasData() {
        return hasData;
    }
    
    /**
     * Method is called in the TableClass after it has retrieved the info, be default
     * it does nothing but if processing of the data is needed overload this method.
     */
    public String processData(String data) {
        return data;
    }
    
    /**
     * Get a table 
     */
    public String getTableName() {
        return myTable.getTable();
    }
    
    public String getTablePath() {
        return myTable.getPath();
    }
    
    public String myColumn() {
        return myColumn;
    }
}
