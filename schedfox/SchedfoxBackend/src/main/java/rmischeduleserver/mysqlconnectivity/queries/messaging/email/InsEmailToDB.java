/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.messaging.email;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class InsEmailToDB extends GeneralQueryFormat {

    public InsEmailToDB() {
    }

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
        StringBuilder queryString = new StringBuilder();
        queryString.append("INSERT INTO control_db.messaging_email_employee ");
        queryString.append("(employee_email, email_subject, email_body, sender_id) ");
        queryString.append(" VALUES (?, ?, ?, ?)");
        return queryString.toString();
    }

}
