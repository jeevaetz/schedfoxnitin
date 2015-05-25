/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.gps;

import java.util.Date;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_coordinates_for_client_query extends GeneralQueryFormat {

    private int numberParameters = 0;
    private Integer[] equipmentId;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Date start, Date end, Integer[] equipmentId) {
        numberParameters = equipmentId.length;
        Object[] params = new Object[equipmentId.length + 2];
        params[0] = start;
        params[1] = end;
        for (int e = 0; e < equipmentId.length; e++) {
            params[e + 2] = equipmentId[e];
        }
        super.setPreparedStatement(params);
        this.equipmentId = equipmentId;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("gps_coordinate ");
        sql.append("WHERE ");
        sql.append("recorded_on BETWEEN ? AND ? AND equipment_id IN ");
        sql.append("(");
        for (int n = 0; n < numberParameters; n++) {
            if (n != 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(") ");
        sql.append("ORDER BY ");
        sql.append("recorded_on ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
