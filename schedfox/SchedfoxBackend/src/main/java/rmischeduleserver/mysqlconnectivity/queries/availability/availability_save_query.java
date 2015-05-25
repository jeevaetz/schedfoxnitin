/*
 * availability_save_query.java
 *
 * Created on January 26, 2005, 4:49 PM
 */
package rmischeduleserver.mysqlconnectivity.queries.availability;

import schedfoxlib.model.Availability;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author ira
 */
public class availability_save_query extends GeneralQueryFormat {

    private boolean isInsert;

    /**
     * Creates a new instance of availability_save_query
     */
    public availability_save_query() {
        myReturnString = new String();
    }

    public boolean hasAccess() {
        return true;
    }

    public void update(Availability avail) {
        super.setPreparedStatement(new Object[]{
                    avail.getEmployeeId(), avail.getAvailDayOfYear(),
                    avail.getEmployeeId(), avail.getAvailDayOfYear(),
                    avail.getStartTime(), avail.getEndTime(), avail.getAvailType(),
                    avail.getCreatedBy(), avail.getAvailMasterRow(),
                    avail.getApprovedOn(), avail.getApprovedBy(),
                    avail.getHoursCompensated()
                });
        isInsert = true;

    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
//        if (isInsert) {
        sql.append("DELETE FROM availability WHERE employee_id = ? AND avail_day_of_year = ?;");
        sql.append("SELECT nextval('availability_seq');");
        sql.append("INSERT INTO ");
        sql.append("availability ");
        sql.append("(");
        sql.append(" avail_id, employee_id, avail_day_of_year, avail_start_time, ");
        sql.append(" avail_end_time, avail_type, createdon, createdby, avail_master_row, ");
        sql.append(" approvedon, approvedby, avail_last_updated, hours_compensated");
        sql.append(") ");
        sql.append("VALUES ");
        sql.append("(currval('availability_seq'), ?, ?, ?, ?, ?, NOW(), ?, ?, ?, ?, NOW(), ?);");
//        } else {
//            sql.append("UPDATE ");
//            sql.append("availability ");
//            sql.append("SET ");
//            sql.append("employee_id = ?, avail_day_of_year = ?, avail_start_time = ?, ");
//            sql.append("avail_end_time = ?, avail_type = ?, createdon = ?, ");
//            sql.append("createdby = ?, avail_master_row = ?, approvedon = ?, approvedby = ?, ");
//            sql.append("avail_last_updated = NOW(), hours_compensated = ? ");
//            sql.append("WHERE ");
//            sql.append("avail_id = ?;");
//        }
        return sql.toString();
    }

    /**
     * This query updates Availability silly
     */
    @Override
    public int getUpdateStatus() {
        return UPDATE_AVAILABILITY;
    }
}
