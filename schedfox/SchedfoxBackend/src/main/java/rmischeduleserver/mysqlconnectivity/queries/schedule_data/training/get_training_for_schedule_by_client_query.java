/*
 * get_training_for_schedule_by_client_query.java
 *
 * Created on October 10, 2005, 10:36 AM
 *
 * Copyright: SchedFox 2005
 */
package rmischeduleserver.mysqlconnectivity.queries.schedule_data.training;

import rmischeduleserver.mysqlconnectivity.queries.schedule_data.*;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author Ira Juneau
 */
public class get_training_for_schedule_by_client_query extends GeneralQueryFormat {

    private String scheduleStart;
    private String daysBackToCheck;

    /** Creates a new instance of get_training_for_schedule_by_client_query */
    public get_training_for_schedule_by_client_query() {
    }

    public void update(String scheduleStart, String daysBackToCheck) {
        this.scheduleStart = scheduleStart;
        this.daysBackToCheck = daysBackToCheck;
    }

    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT (SUM((CASE WHEN sch.start_time >= sch.end_time ");
        sql.append("THEN sch.end_time + 1440 ");
        sql.append("ELSE sch.end_time END) -  sch.start_time) / 60) as time, sch.eid, sch.cid, ");
        sql.append("COALESCE((SELECT true FROM employee_trained et WHERE et.employee_id = sch.eid AND et.client_id = sch.cid LIMIT 1), false) is_trained ");
        sql.append("FROM client ");
        sql.append("INNER JOIN (SELECT * FROM assemble_schedule (" + this.getDriver().toDate(daysBackToCheck) + ", " + this.getDriver().toDate(scheduleStart) + ", " + this.getBranch() + ")) sch ON sch.cid = client.client_id ");
        sql.append("WHERE ");
        sql.append("client.client_training_time > 0 AND sch.isdeleted = 0 AND ");
        sql.append("client.branch_id = " + this.getBranch() + " AND client.client_is_deleted != 1 ");
        sql.append("GROUP BY sch.cid, sch.eid ");
        sql.append("ORDER BY sch.cid");
        return sql.toString();
    }
}
