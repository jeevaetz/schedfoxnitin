/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import schedfoxlib.model.ProblemSolverContacts;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_problem_solver_contacts_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(ProblemSolverContacts problemContacts) {
        if (problemContacts.getProblemSolverContactsId() == null) {
            this.isInsert = true;
            super.setPreparedStatement(new Object[]{
                problemContacts.getProblemSolverContactId(), problemContacts.getContactId(), 
                problemContacts.getContactType(), problemContacts.getMessageDeliveredDate()
            });
        } else {
            super.setPreparedStatement(new Object[]{
                problemContacts.getProblemSolverContactId(), problemContacts.getContactId(), problemContacts.getContactType(),
                problemContacts.getMessageDeliveredDate(), problemContacts.getProblemSolverContactsId()
            });
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO problem_solver_contacts ");
            sql.append("(problem_solver_contact_id, contact_id, contact_type, message_delivered_date)");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?)");
        } else {
            sql.append("UPDATE problem_solver_contacts ");
            sql.append("SET ");
            sql.append("problem_solver_contact_id = ?, contact_id = ?, contact_type = ?, message_delivered_date = ? ");
            sql.append("WHERE ");
            sql.append("problem_solver_contacts_id = ?;");
        }
        return sql.toString();
    }

}
