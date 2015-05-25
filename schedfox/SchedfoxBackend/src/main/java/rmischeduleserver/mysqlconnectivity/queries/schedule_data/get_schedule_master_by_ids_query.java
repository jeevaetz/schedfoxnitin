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
public class get_schedule_master_by_ids_query extends GeneralQueryFormat {

    private Integer size;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(Integer size) {
        this.size = size;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT * FROM ");
        sql.append("schedule_master ");
        sql.append("WHERE ");
        sql.append("schedule_master_id IN ");
        sql.append("(");
        for (int s = 0; s < size; s++) {
            if (s > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");
        return sql.toString();
    }
    
}
