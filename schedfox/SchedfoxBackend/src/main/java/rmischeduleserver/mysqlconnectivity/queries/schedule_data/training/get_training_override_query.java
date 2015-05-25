/*
 * get_training_override_query.java
 *
 * Created on October 23, 2006, 7:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.training;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author shawn
 */
public class get_training_override_query extends GeneralQueryFormat {

    /** Creates a new instance of get_training_override_query */
    public get_training_override_query() {

    }
    
    public boolean hasAccess() { return true; }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT true is_trained, employee_id, client.client_id ");
        sql.append("FROM client ");
        sql.append("INNER JOIN employee_trained ON employee_trained.client_id = client.client_id ");
        sql.append("WHERE client.branch_id = " + this.getBranch() + " AND employee_id != 0 AND client.client_is_deleted != 1 ");
        sql.append("GROUP BY employee_id, client.client_id ");
        sql.append("ORDER BY client_id ");
        return sql.toString();
    }
    
}
