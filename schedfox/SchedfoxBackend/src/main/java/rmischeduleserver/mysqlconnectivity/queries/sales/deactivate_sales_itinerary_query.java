/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.sales;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.User;

/**
 *
 * @author ira
 */
public class deactivate_sales_itinerary_query extends GeneralQueryFormat {

    private ArrayList<String> guids;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(ArrayList<String> guids, User user) {
        this.guids = guids;
        ArrayList<Object> psts = new ArrayList<Object>();
        psts.add(user.getUserId());
        psts.addAll(guids);
        super.setPreparedStatement(psts.toArray());
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append("sales_itinerary ");
        sql.append("SET active = false ");
        sql.append("WHERE ");
        sql.append("user_id = ? AND ");
        sql.append("external_gid NOT IN (");
        for (int g = 0; g < guids.size(); g++) {
            if (g > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
