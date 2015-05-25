/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.schedule_data;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_schedule_by_id_query extends GeneralQueryFormat {

    private boolean isMaster;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(boolean isMaster) {
        this.isMaster = isMaster;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isMaster) {
            sql.append("SELECT * FROM ");
            sql.append("schedule_master ");
            sql.append("WHERE ");
            sql.append("schedule_master_id = ?; ");
        } else {
            sql.append("SELECT * FROM ");
            sql.append("schedule ");
            sql.append("WHERE ");
            sql.append("schedule_id = ?;");
        }
        return sql.toString();
    }
    
}
