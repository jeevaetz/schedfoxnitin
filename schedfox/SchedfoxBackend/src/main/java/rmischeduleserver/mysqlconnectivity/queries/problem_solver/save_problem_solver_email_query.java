/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.problem_solver;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.ProblemsolverEmail;

/**
 *
 * @author ira
 */
public class save_problem_solver_email_query extends GeneralQueryFormat {

    private ProblemsolverEmail email;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    
    public void update(ProblemsolverEmail email) {
        this.email = email;
        if (this.email.getProblemsolverEmailId() == null || this.email.getProblemsolverEmailId() == 0) {
            super.setPreparedStatement(new Object[] {
                email.getPsId(), email.getUserId(), email.getSentTo(), email.getCcd(), 
                email.getEmailSubject(), email.getEmailBody(), email.getAttachPdf()
            });
        } else {
            super.setPreparedStatement(new Object[] {
                email.getPsId(), email.getUserId(), new java.sql.Timestamp(email.getDatetimerequested().getTime()),
                new java.sql.Timestamp(email.getDatetimesent().getTime()), email.getSentTo(), email.getCcd(),
                email.getEmailSubject(), email.getEmailBody(), email.getAttachPdf(),
                email.getProblemsolverEmailId()
            });
        }
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (this.email.getProblemsolverEmailId() == null || email.getProblemsolverEmailId() == 0) {
            sql.append("INSERT INTO problemsolver_email ");
            sql.append("(ps_id, user_id, sent_to, ccd, email_subject, email_body, attach_pdf) ");
            sql.append(" VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?) ");
        } else {
            sql.append("UPDATE problemsolver_email SET ");
            sql.append("ps_id = ?, user_id = ?, datetimerequested = ?, datetimesent = ?, ");
            sql.append("sent_to = ?, ccd = ?, email_subject = ?, email_body = ?, ");
            sql.append("attach_pdf = ? ");
            sql.append("WHERE ");
            sql.append("problemsolver_email_id = ?;");
        }
        return sql.toString();
    }
}
