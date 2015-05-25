/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.captured_email;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.CapturedEmail;

/**
 *
 * @author ira
 */
public class save_captured_email_query extends GeneralQueryFormat {

    public void update(CapturedEmail email) {
        super.setPreparedStatement(new Object[]{email.getCapturedEmail()});
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(super.getManagementSchema()).append(".");
        sql.append("captured_emails ");
        sql.append("(captured_email)");
        sql.append("VALUES ");
        sql.append("(?)");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
}
