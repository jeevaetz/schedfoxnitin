/*
 * assemble_schedule_for_training_query.java
 *
 * Created on August 9, 2005, 9:03 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.reports;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.generic_assemble_schedule_query;
/**
 *
 * @author Ira Juneau
 */
public class assemble_schedule_for_training_query extends generic_assemble_schedule_query {
    
    /** Creates a new instance of assemble_schedule_for_training_query */
    public assemble_schedule_for_training_query() {
        myReturnString = new String();
        super.setOrderByFields(super.myReportOrder);
        super.setSelectedFields(super.myTrainingReportFields);
    }
    
    protected String generateCompleteWhereClause() {
        return " WHERE ( (\"type\" / 100) % 10 = 3 OR (\"type\" / 100) % 10 = 6)";
    }
    
    /**
     * Clear out all master schedules since only temporaries can have training
     */
    public String getMasterEmpString() {
        return " AND schedule_master.employee_id < 0 ";
    }
    
}
