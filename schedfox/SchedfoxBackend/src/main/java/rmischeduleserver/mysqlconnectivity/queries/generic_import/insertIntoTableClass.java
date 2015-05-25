/*
 * insertIntoTableClass.java
 *
 * Created on February 28, 2006, 2:02 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.generic_import;
import java.util.*;
import java.io.*;
/**
 *
 * @author Ira Juneau
 */
public class insertIntoTableClass implements Serializable {
    
    private ArrayList<columnData> myColumnsOfData;
    private String myTableName;
    private String completeWhereClause;
    
    public insertIntoTableClass(String tableName) {
        myTableName = tableName.trim();
        myColumnsOfData = new ArrayList();
        completeWhereClause = new String();
    }
    
    public void addWhereBindInformation(String whereClause) {
        completeWhereClause = whereClause;
    }
    
    public String getTableName() {
        return myTableName;
    }
    
    public void addColumnData(String colName, String colData) {
        myColumnsOfData.add(new columnData(colName, colData));
    }
    
    public String toOutputString() {
        StringBuilder myBuilder = new StringBuilder();
        myBuilder.append("Table Name - " + myTableName + "\n");
        for (int i = 0; i < myColumnsOfData.size(); i++) {
            myBuilder.append(myColumnsOfData.get(i).toOutputString() + "\n");
        }
        return myBuilder.toString();
    }
    
    /**
     * Gets that VALUES (...) string for out SQL statement
     */
    public String getValuesString() {
        StringBuilder myBuilder = new StringBuilder();
        myBuilder.append("(");
        for (int i = 0; i < myColumnsOfData.size(); i++) {
            myBuilder.append("'" + myColumnsOfData.get(i).getColumnData() + "'");
            if (i < myColumnsOfData.size() - 1) {
                myBuilder.append(", ");
            }
        }
        if (completeWhereClause.trim().length() == 0) {
            myBuilder.append(");");
        } else {
            
        }
        return myBuilder.toString();
    }
    
    /**
     * Gets that (...) column definition list for out SQL statement
     */
    public String getColumnsString() {
        StringBuilder myBuilder = new StringBuilder();
        myBuilder.append(" (");
        for (int i = 0; i < myColumnsOfData.size(); i++) {
            myBuilder.append(myColumnsOfData.get(i).getColumnName());
            if (i < myColumnsOfData.size() - 1) {
                myBuilder.append(", ");
            }
        }
        myBuilder.append(") VALUES ");
        return myBuilder.toString();
    }
    
    public String toUpdateString() {
        if (completeWhereClause.trim().length() == 0) {
            return "No Where Clause Specified...Will not work until then";
        }
        StringBuilder myBuilder = new StringBuilder();
        myBuilder.append(" UPDATE " + myTableName + " SET ");
        for (int i = 0; i < myColumnsOfData.size(); i++) {
            myBuilder.append(myColumnsOfData.get(i).getColumnName() + " = '" + myColumnsOfData.get(i).getColumnData() + "'");
            if (i < myColumnsOfData.size() - 1) {
                myBuilder.append(",");
            }
        }
        myBuilder.append(" " + completeWhereClause);
        return myBuilder.toString();
    }
    
    
    public String toString() {
        return " INSERT INTO " + myTableName + getColumnsString() + getValuesString();
    }
}
