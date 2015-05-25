/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class schedule_client_query extends GeneralQueryFormat {
    private String sdate;
    private String edate;
    private int employee_id;

    /** Creates a new instance of client_query */
    public schedule_client_query() {
        
    }

    public void update(String lastUpdated) {
        super.setLastUpdated(lastUpdated);
    }

    public void update(int employee_id, String sdate, String edate) {
        this.employee_id = employee_id;
        this.sdate = sdate;
        this.edate = edate;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT ON (client.client_id) ");
        sql.append("client.*, ");
        sql.append("(CASE WHEN client.client_worksite > 0 THEN cli.client_name || ' ' || client.client_name ELSE client.client_name END) as cname, ");
        sql.append("(CASE WHEN EXISTS (SELECT * FROM client_notes WHERE client_notes.client_id = client.client_id) THEN 1 ELSE 0 END) as hasNotes ");
        sql.append("FROM client ");
        if (employee_id != 0)  {
            sql.append("INNER JOIN (SELECT COUNT(*) as num, cid FROM assemble_schedule('" + sdate + "', '" + edate + "', " + this.getBranch() + ", " + employee_id + ") WHERE isdeleted = 0 GROUP BY cid) as sched ON sched.cid = client.client_id AND num > 0 ");
        }
        sql.append("LEFT JOIN client cli ON cli.client_id = client.client_worksite ");
        sql.append("WHERE ");
        sql.append("(client.client_is_deleted = 0 OR (client.client_is_deleted = 1 AND client.client_end_date BETWEEN '" + sdate + "' AND '" + edate + "')) ");
        sql.append("AND client.branch_id = " + getBranch() + " ");
        if (super.getLastUpdated() != null && super.getLastUpdated().length() > 0) {
            sql.append(" AND client.client_last_updated >= '" + super.getLastUpdated() + "' ");
        }
        sql.append("ORDER BY client.client_id ");
        return sql.toString();
    }

    public boolean hasAccess() {
        return true;
    }
}
