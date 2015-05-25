/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.new_user;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author Ira
 */
public class does_schema_exist_query extends GeneralQueryFormat {
    
    private String schemaName;
    
    public does_schema_exist_query() {
        myReturnString = new String();
    }
    
    public void update(String schemaName) {
        this.schemaName = schemaName.replaceAll("'", "''").replaceAll(" ", "_");
    }
    
    public String getSchemaName() {
        return schemaName;
    }
    
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) as schema_count FROM information_schema.schemata ");
        sql.append("WHERE schema_name = '" + this.schemaName + "' ");
        return sql.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
}
