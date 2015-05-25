/*
 * assemble_schedule_for_unconfirmed_report.java
 *
 * Created on August 17, 2005, 3:19 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.reports;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.generic_assemble_schedule_query;
/**
 *
 * @author Ira Juneau
 */
public class assemble_schedule_for_unconfirmed_report extends generic_assemble_schedule_query{
    
    
    
    /** Creates a new instance of assemble_schedule_for_unconfirmed_report */
    public assemble_schedule_for_unconfirmed_report() {
        myReturnString = new String();
        super.setOrderByFields(super.myReportOrder);
        super.setSelectedFields(super.myReportFields);
    }
    
    protected String generateTypeSQL(String type, boolean isMaster) {
        if (isMaster) {
            return " employee.employee_id < 0 ";
        }
        return " ( schedule_type % 10 = 1 and employee.employee_id > 0) ";
    }
    
    protected String getMasterEmpString() {
        return " schedule_master.employee_id < 0 ";
    }
    
}
