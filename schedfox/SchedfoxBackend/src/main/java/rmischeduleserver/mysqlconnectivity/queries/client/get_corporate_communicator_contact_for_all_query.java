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
public class get_corporate_communicator_contact_for_all_query extends GeneralQueryFormat {

    private ArrayList<Integer> clientids = new ArrayList<Integer>();
    
    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(ArrayList<Integer> clientids) {
        this.clientids = clientids;
        super.setPreparedStatement(clientids.toArray());
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("client_to_user_contact ");
        sql.append("INNER JOIN ").append(super.getManagementSchema()).append(".\"user\" ON \"user\".user_id = client_to_user_contact.user_id ");
        sql.append("WHERE ");
        sql.append("client_id IN ");
        sql.append("(");
        for (int c = 0; c < clientids.size(); c++) {
            if (c > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");
        return sql.toString();
    }

}
