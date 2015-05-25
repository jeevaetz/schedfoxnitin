/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import schedfoxlib.model.ProblemSolverContact;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_problem_solver_contact_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(ProblemSolverContact contact, boolean isInsert) {
        this.isInsert = isInsert;
        if (isInsert) {
            super.setPreparedStatement(new Object[]{contact.getProblemSolverContactId(), contact.getPsId()});
        } else {
            super.setPreparedStatement(new Object[]{contact.getPsId(), contact.getContactDate(), contact.getProblemSolverContactId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO problem_solver_contact ");
            sql.append("(problem_solver_contact_id, ps_id)");
            sql.append("VALUES ");
            sql.append("(?, ?)");
        } else {
            sql.append("UPDATE problem_solver_contact ");
            sql.append("SET ");
            sql.append("ps_id = ?, contact_date = ? ");
            sql.append("WHERE ");
            sql.append("problem_solver_contact_id = ?;");
        }
        return sql.toString();
    }

}
