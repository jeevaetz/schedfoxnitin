/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_clients_by_branches_query extends GeneralQueryFormat {

    private int sizeArray;
    
    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(ArrayList<Integer> branches) {
        sizeArray = branches.size();
        Object[] obj = new Object[branches.size()];
        for (int b = 0; b < branches.size(); b++) {
            obj[b] = branches.get(b);
        }
        super.setPreparedStatement(obj);
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT client.*, latitude, longitude FROM client ");
        sql.append("LEFT JOIN address_geocode ON upper(trim(address_geocode.address1)) = upper(trim(client.client_address)) AND ");
        sql.append("    upper(trim(address_geocode.city)) = upper(trim(client.client_city)) AND upper(trim(address_geocode.state)) = upper(trim(client.client_state)) AND ");
        sql.append("    upper(trim(address_geocode.zip)) = upper(trim(client.client_zip)) AND upper(trim(address_geocode.address2)) = upper(trim(client.client_address2)) ");
        sql.append("WHERE ");
        sql.append("client_is_deleted != 1 AND branch_id IN (");
        for (int b = 0; b < sizeArray; b++) {
            if (b > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(") ");
        sql.append("ORDER BY client_name ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
