/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_clients_by_search_params_query extends GeneralQueryFormat {
    
    private int branchSize = 0;
    
    public get_clients_by_search_params_query() {
        
    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String params, ArrayList<Integer> branches) {
        String param = "%" + params + "%";
        if (branches != null) {
            branchSize = branches.size();
        }
        
        Object[] paramsObj = new Object[5 + branches.size()];
        paramsObj[0] = param;
        paramsObj[1] = param;
        paramsObj[2] = param;
        paramsObj[3] = param;
        paramsObj[4] = param;
        for (int b = 0; b < branches.size(); b++) {
            paramsObj[b + 5] = branches.get(b);
        }
        super.setPreparedStatement(paramsObj);
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("client ");
        sql.append("WHERE ");
        sql.append("(");
        sql.append("    (client_name) ilike (?) OR ");
        sql.append("    (client_address || ', ' || client_city || ' ' || client_state) ilike (?) OR ");
        sql.append("    client_phone ilike (?) OR ");
        sql.append("    client_phone2 ilike (?) OR ");
        sql.append("    client_fax ilike (?) ");
        sql.append(") ");
        if (branchSize > 0) {
            sql.append("AND client.branch_id IN (");
            for (int b = 0; b < branchSize; b++) {
                if (b > 0) {
                    sql.append(",");
                }
                sql.append("?");
            }
            sql.append(")");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
