/*
 * check_for_master_shift_and_return_info_query.java
 *
 * Created on August 9, 2005, 8:28 AM
 *
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 * This Class is used to return information if a master shift with the given id is found
 * is useful for when applying a temporary shift to a master, to prompt the user that
 * they will be overwritting certain information.
 *
 */
public class check_for_master_shift_and_return_info_query extends GeneralQueryFormat {
    
    private String sid;
    private String edate;
    private String sdate;
    
    /** Creates a new instance of check_for_master_shift_and_return_info_query */
    public check_for_master_shift_and_return_info_query() {
        myReturnString = new String();
    }
    
    /**
     * myShiftId is the shift ID of the master that you want to search for, endDate and
     * begDate should be given from loaded schedules start and end date so that the 
     * sql can generate shifts that may exists then the schedule can figure out which ones actually
     * exist...
     */
    public void update(String myShiftId, String begDate, String endDate) {
        sid = myShiftId;
        edate = endDate;
        sdate = begDate;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return  "SELECT (text(schedule_master.schedule_master_id * -1) || '/' || dayofyear)    as sid, dayofyear, dow " +
                "FROM generate_date_series(date('" + sdate + "') , date('" + edate + "')) as s(dayofyear)" +
                "LEFT JOIN schedule_master ON schedule_master_id = " + sid + " AND schedule_master_day = dow AND " +
                "(schedule_master_date_started <= dayofyear AND schedule_master_date_ended >= dayofyear) WHERE " +
                "schedule_master.schedule_master_id IS NOT null";
    }
    
}
