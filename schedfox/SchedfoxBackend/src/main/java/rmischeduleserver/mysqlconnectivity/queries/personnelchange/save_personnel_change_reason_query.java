/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.personnelchange;

import java.sql.Timestamp;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.PersonnelChange;

/**
 *
 * @author ira
 */
public class save_personnel_change_reason_query extends GeneralQueryFormat {

    private boolean isInsert;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(PersonnelChange change) {
        Timestamp sentDate = null;
        if (change.getDateSent() != null) {
            sentDate = new Timestamp(change.getDateSent().getTime());
        }
        if (change.getPersonnelChangeId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{change.getDateOfChange(), change.getEmployeeId(), 
                change.getClientId(), change.getReasonId(), change.getReasonText(),
                change.getUserId(), sentDate, change.getTemplateText()});
        } else {
            isInsert = false;
            super.setPreparedStatement(new Object[]{change.getEmployeeId(), 
                change.getClientId(), change.getReasonId(), change.getReasonText(),
                change.getUserId(), sentDate, change.getTemplateText(), 
                change.getPersonnelChangeId()});
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append("personnel_changes ");
            sql.append("(date_of_change, employee_id, client_id, reason_id, reason_text, user_id, date_sent, template_text) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?)");
        } else {
            sql.append("UPDATE personnel_changes ");
            sql.append("SET ");
            sql.append("employee_id = ?, client_id = ?, reason_id = ?, reason_text = ?, user_id = ?, date_sent = ?, template_text = ? ");
            sql.append("WHERE ");
            sql.append("personnel_change_id = ? ");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
