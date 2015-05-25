/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.user;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class save_and_clear_user_branch_company_query extends GeneralQueryFormat {
    
    private int arraySize;
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    public void update(Integer userId, Integer companyId, ArrayList<Integer> branches) {
        arraySize = branches.size();
        
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(userId);
        params.add(companyId);
        for (int b = 0; b < branches.size(); b++) {
            params.add(userId);
            params.add(branches.get(b));
            params.add(companyId);
        }
        super.setPreparedStatement(params.toArray());
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append("control_db.user_branch_company ");
        sql.append("WHERE ");
        sql.append("user_id = ? AND company_id = ?;");
        for (int b = 0; b < arraySize; b++) {
            sql.append("INSERT INTO ");
            sql.append("control_db.user_branch_company ");
            sql.append("(user_id, branch_id, company_id)");
            sql.append("VALUES ");
            sql.append("(?, ?, ?); ");
        }
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
}
