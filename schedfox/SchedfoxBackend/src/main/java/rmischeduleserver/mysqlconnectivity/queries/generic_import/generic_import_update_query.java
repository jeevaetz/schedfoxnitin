/*
 * generic_insert_import_query.java
 *
 * Created on February 28, 2006, 12:33 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.generic_import;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.util.*;
/**
 *
 * @author Ira Juneau
 */
public class generic_import_update_query extends GeneralQueryFormat {
    
    private ArrayList<insertIntoTableClass> myFieldInsertInformation;
    
    /** Creates a new instance of generic_insert_import_query */
    public generic_import_update_query(String... tieTogetherFields) {
        myReturnString = new String();
        myFieldInsertInformation = new ArrayList();
    }
    
    /**
     * Ahhh fun how this works is following... pass in three Strings for each column
     * 1 - What Table ie: employee
     * 2 - What field ie: employee_id
     * 3 - Value...
     * A complete call could be update("employee", "employee_id", "218", "client", "client_address", "1218 GateWay Blvd"
     * This should only be generated by our generic import crap on the client side o' things...
     */
    public ArrayList<insertIntoTableClass> update(String... fieldInformation) {
        for (int i = 0; i < fieldInformation.length; i+=3) {
            int tableEntryPosition = -1;
            for (int a = 0; a < myFieldInsertInformation.size(); a++) {
                if (myFieldInsertInformation.get(a).getTableName().equals(fieldInformation[i])) {
                    tableEntryPosition = a;
                    a = myFieldInsertInformation.size();
                }
            }
            if (tableEntryPosition == -1) {
                tableEntryPosition = myFieldInsertInformation.size();
                myFieldInsertInformation.add(new insertIntoTableClass(fieldInformation[i]));
            }
            myFieldInsertInformation.get(tableEntryPosition).addColumnData(fieldInformation[i + 1], fieldInformation[i + 2]);
        }
        return myFieldInsertInformation;
    }
    
    /**
     * Very important will not work unless this is used! Appropriate use is 
     * setWhereClause("WHERE employee.employee_id = " + myId, "employee");; 
     */
    public void setWhereClause(String whereClause, String tableName) {
        for (int i = 0; i < myFieldInsertInformation.size(); i++) {
            if (myFieldInsertInformation.get(i).getTableName().trim().equals(tableName.trim())) {
                myFieldInsertInformation.get(i).addWhereBindInformation(whereClause);
            }
        }
    }
    
    public String toString() {
        StringBuilder myBuilder = new StringBuilder();
        for (int i = 0; i < myFieldInsertInformation.size(); i++) {
            myBuilder.append(myFieldInsertInformation.get(i).toUpdateString() + ";");
        }
        return myBuilder.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public static void main(String args[]) {
        generic_import_update_query myQuery = new generic_import_update_query();
        myQuery.update("client", "client_id", "894", "client", "client_address", "1616 GateWay Blvd", "employee", "employee_first_name", "Ira", "client", "client_phone", "817-896-0931");
        myQuery.setWhereClause("WHERE employee_id = " + "516", "employee");
        myQuery.setWhereClause("WHERE client_id = " + "894", "client");
    }
    
}
