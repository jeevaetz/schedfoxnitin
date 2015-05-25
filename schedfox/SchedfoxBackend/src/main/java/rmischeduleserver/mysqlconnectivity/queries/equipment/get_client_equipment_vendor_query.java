/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.equipment;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_client_equipment_vendor_query extends GeneralQueryFormat  {
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("client_equipment_vendor ");
        sql.append("ORDER BY ");
        sql.append("vendor_name ");
        return sql.toString();
    }
}
