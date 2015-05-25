/*
 * assemble_schedule_for_employee_report_query.java
 *
 * Created on July 7, 2005, 9:49 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.reports;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.*;
/**
 *
 * @author ira
 */
public class assemble_schedule_for_employee_report_query extends generic_assemble_schedule_query {
    
    private String emps;
    
    /** Creates a new instance of assemble_schedule_for_employee_report_query */
    public assemble_schedule_for_employee_report_query() {
        super();
        super.setSelectedFields(myReportFields);
        super.setOrderByFields(myEmployeeReportOrder);
    }

    @Override
    public void update(String clientsToInclude, String employeesToInclude, String sdate, String edate, String type, String lastupdated, boolean showDeleted) {
        super.update("","",sdate,edate,type,lastupdated,showDeleted);
        emps = employeesToInclude;
    }

    @Override
    public String generateCompleteWhereClause() {
        if (emps.trim().length() > 0) {
            return " WHERE eid = " + emps + " ";
        } else {
            return " WHERE isdeleted = 0 AND eid != 0 ";
        }
    }
    
}
