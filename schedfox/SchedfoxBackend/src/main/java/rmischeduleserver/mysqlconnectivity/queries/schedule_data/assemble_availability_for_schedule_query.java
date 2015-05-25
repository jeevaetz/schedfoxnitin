/*
 * assemble_availability_for_schedule_query.java
 *
 * Created on August 24, 2005, 3:20 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.availability.*;
/**
 *
 * @author Ira Juneau
 */
public class assemble_availability_for_schedule_query extends assemble_total_avail_query {
    
    private String lastUpdated;
    
    /** Creates a new instance of assemble_availability_for_schedule_query */
    public assemble_availability_for_schedule_query() {
    }

    @Override
    public boolean isAvailabilityQuery() {
        return true;
    }

    public String getWhereClause() {
        return "INNER JOIN employee ON employee.employee_id = avail.employee_id " +
                    "WHERE employee_is_deleted != 1 ";
    }

    @Override
    public void updateTime(String LU) {
        lastUpdated = LU;
    }
    
    @Override
    public String getEmployeeString() {
        return "null";
    }

    public String getLastUpdatedString() {
        if (this.getLastUpdated() == null) {
            return "null";
        } else {
            return "to_timestamp('" + this.getLastUpdated() + "', 'YYYY-MM-DD HH24:MI:SS')";
        }
    }
    
}
