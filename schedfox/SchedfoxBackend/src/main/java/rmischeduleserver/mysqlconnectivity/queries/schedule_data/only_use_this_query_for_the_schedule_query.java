/*
 * only_use_this_query_for_the_schedule_query.java
 *
 * Created on August 26, 2005, 10:54 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;

/**
 *
 * @author Ira Juneau
 * Hmmm ok, why do I have this class, this is why, on the server side of things we keep track of only one schedulequery
 * that is determined by the method isScheduleQuery(), for each client/branch...ok well anyway why this? Ok well before we 
 * were running new_assemble_complete_schedule_query for a multitude of things reports etc...and it was marked as the schedule
 * query. So it would overwrite everything, hmmm this would mean that I could break everything by loading a schedule, then 
 * printing a report for that branch, client because the ranges would be different...the heartbeat would only work for one week from
 * then on out...
 */
public class only_use_this_query_for_the_schedule_query extends generic_assemble_schedule_query {
    
    /** Creates a new instance of only_use_this_query_for_the_schedule_query */
    public only_use_this_query_for_the_schedule_query() {
        //this.setOrderByFields(genericOrderFields);
    }
    
    public String generateOrderBy() {
        return " ORDER BY cname, ename, start_time ";
    }
    
    public String additionalFields() {
        return ", (CASE WHEN EXISTS (SELECT * FROM schedule_notes WHERE schedule_notes.shift_id = assemble_schedule.sid LIMIT 1) THEN true ELSE false END) as hasnote ";
    }
    
    /**
     * This is the scheduleQuery
     */
    public boolean isScheduleQuery() {
        return true;
    }
    
}
