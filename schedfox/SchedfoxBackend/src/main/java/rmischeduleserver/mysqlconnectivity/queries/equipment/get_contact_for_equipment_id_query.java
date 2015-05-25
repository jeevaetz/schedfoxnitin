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
public class get_contact_for_equipment_id_query extends GeneralQueryFormat {

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
        sql.append("client_equipment_contact ");
        sql.append("WHERE ");
        sql.append("client_equipment_id = ? ");
        sql.append("AND contact_time > NOW() - interval '1 day' * ? ");
        sql.append("ORDER BY client_equipment_contact_id DESC ");
        return sql.toString();
    }
}
