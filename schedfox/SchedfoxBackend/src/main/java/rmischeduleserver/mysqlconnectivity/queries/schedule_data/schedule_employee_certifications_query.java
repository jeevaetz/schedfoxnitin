/*
 * schedule_employee_certifications_query.java
 *
 * Created on June 13, 2005, 1:28 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class schedule_employee_certifications_query extends GeneralQueryFormat {
    
    /** Creates a new instance of schedule_employee_certifications_query */
    public schedule_employee_certifications_query() {
        myReturnString = new String();
    }
    
    public String toString() {
        return  "SELECT employee_certifications.employee_id as eid," +
                "employee_certifications.cert_id as certid," +
                "certifications.certification_name, " +
                "certifications.certification_description " +
                "FROM employee_certifications " +
                "LEFT JOIN employee ON employee.employee_id = employee_certifications.employee_id " +
                "LEFT JOIN certifications ON " +
                "certifications.certification_id = employee_certifications.cert_id " +
                "WHERE employee.branch_id = " + getBranch();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
