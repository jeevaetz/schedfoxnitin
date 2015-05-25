/*
 * columnData.java
 *
 * Created on February 28, 2006, 2:03 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.generic_import;
import java.io.*;
/**
 *
 * @author Ira Juneau
 */
public class columnData implements Serializable {
    
    private String colName;
    private String colData;
    
    public columnData(String colName, String colData) {
        this.colName = colName;
        this.colData = colData;
    }
    
    public String getColumnName() {
        return this.colName;
    }
    
    public String getColumnData() {
        return this.colData;
    }
    
    public String toOutputString() {
        return " Column Name " + colName + " Data " + colData;
    }
    
    public String toString() {
        return " " + colName + ", '" + colData + "' ";
    }
}
