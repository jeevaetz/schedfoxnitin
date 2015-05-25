/*
 * master_availabilitiy_save_query.java
 *
 * Created on January 26, 2005, 3:31 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import schedfoxlib.model.AvailabilityMaster;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class master_availability_save_query extends GeneralQueryFormat {

    private boolean isInsert;

    /** Creates a new instance of master_availabilitiy_save_query */
    public master_availability_save_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }

    public void update(AvailabilityMaster availMaster) {
        if (availMaster.getAvailMId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{
                availMaster.getEmployeeId(), availMaster.getAvailMDateStarted(),
                availMaster.getAvailMDateEnded(), availMaster.getAvailMTimeStarted(),
                availMaster.getAvailMTimeEnded(), availMaster.getAvailMDayOfWeek(),
                availMaster.getAvailMRow()
            });
        } else {
            isInsert = false;
            super.setPreparedStatement(new Object[]{
                availMaster.getAvailMId(),
                availMaster.getEmployeeId(), availMaster.getAvailMDateStarted(),
                availMaster.getAvailMDateEnded(), availMaster.getAvailMTimeStarted(),
                availMaster.getAvailMTimeEnded(), availMaster.getAvailMDayOfWeek(),
                availMaster.getAvailMRow(), (availMaster.getAvailMId() * -1)
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
        if (isInsert) {
            sql.append("SELECT nextval('availability_master_seq');");
            sql.append("INSERT INTO ");
            sql.append("availability_master ");
            sql.append("(");
            sql.append("    avail_m_id, employee_id, avail_m_date_started, avail_m_date_ended, ");
            sql.append("    avail_m_time_started, avail_m_time_ended, avail_m_day_of_week, ");
            sql.append("    avail_m_row");
            sql.append(") ");
            sql.append("VALUES ");
            sql.append("(");
            sql.append("    currval('availability_master_seq'), ");
            sql.append("    ?, ?, ?, ");
            sql.append("    ?, ?, ?, ");
            sql.append("    ?");
            sql.append(")");
        } else {
            sql.append("SELECT ?;");
            sql.append("UPDATE availability_master SET ");
            sql.append("    employee_id = ?, avail_m_date_started = ?, avail_m_date_ended = ?, ");
            sql.append("    avail_m_time_started = ?, avail_m_time_ended = ?, avail_m_day_of_week = ?, ");
            sql.append("    avail_m_row = ? ");
            sql.append("WHERE ");
            sql.append("avail_m_id = ?");
        }
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
