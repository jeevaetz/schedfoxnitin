/*
 * get_problem_solvers_query.java
 *
 * Created on August 14, 2006, 12:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author shawn
 */
public class get_problem_solvers_query extends GeneralQueryFormat {

    private String clientId;

    /** Creates a new instance of get_problem_solvers_query */
    public get_problem_solvers_query(String clientId) {
        this.clientId = clientId;
    }

    public get_problem_solvers_query() {
        this.clientId = "";
    }

    private String getQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ps_id, ps.client_id, ps.user_id,");
        sql.append("ps_date, problem, solution, scheduler_inst,");
        sql.append("supervisor_inst, dm_inst, hr_inst, postcom_inst,");
        sql.append("officer_inst, payroll_inst, dispatch_inst,");
        sql.append("checkin_inst, (user_first_name || ' ' || user_last_name) as originator,");
        sql.append("client_name, client_phone, client_phone2 as client_cell, client_fax, ");
        sql.append("private_communication " );
        sql.append("FROM problemsolver ps ");
        sql.append("LEFT JOIN client ON client.client_id = ps.client_id ");
        sql.append("LEFT JOIN " + getManagementSchema() + ".user usr ON usr.user_id = ps.user_id ");
        sql.append("WHERE ps.client_id = ? ");
        sql.append("ORDER BY ps_date DESC;");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        return getQuery();
    }

    @Override
    public String toString() {
        String sql = getQuery();
        sql = sql.replace("?", this.clientId);
        return sql;
    }

    public boolean hasAccess() { return true; }
}
