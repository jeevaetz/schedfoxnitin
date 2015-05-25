/*
 * new_assemble_schedule_for_checkin_query.java
 *
 * Created on August 30, 2005, 8:20 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in;
import java.util.Date;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class new_assemble_schedule_for_checkin_query extends GeneralQueryFormat {

    private boolean isPrintableQuery = false;

    private String lastUpdated;

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("assemble_schedule(?, ?, ?, ?, ?::timestamp, ?::integer[], ?) ");
        if (isPrintableQuery) {
            sql.append("ORDER BY branch_id, start_time, end_time");
        }
        return sql.toString();
    }

    public void setPrintableQuery(boolean isPrintableQuery) {
        this.isPrintableQuery = isPrintableQuery;
    }

    @Override
    public boolean isCheckInQuery() {
        return !isPrintableQuery;
    }

    @Override
    public void setLastUpdated(String time) {
        try {
            Object[] myValues = super.getPreparedStatementObjects();
            myValues[4] = time;
            super.setPreparedStatement(myValues);
            lastUpdated = time;
        } catch (Exception e) {}
    }

    @Override
    public String getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
    
}
