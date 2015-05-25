/*
 * get_certifications_for_employee_branch_query.java
 *
 * Created on October 13, 2005, 1:36 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
/**
 *
 * @author Ira Juneau
 */
public class get_certifications_for_employee_branch_query extends RunQueriesEx {
    
    /**
     * Creates a new instance of get_certifications_for_employee_branch_query 
     */
    public get_certifications_for_employee_branch_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public boolean isEmployeeCertQuery() {
        return true;
    }
    
    public String toString() {
        return "SELECT certifications.certification_id certid,  " +
               "employee.employee_id eid, " +
               "certifications.certification_name cert_name, " +
               "certifications.certification_description cert_desc, " +
               //"certifications.certification_default_renewal_time AS renewal, " +
               this.getDriver().getCurrentDateSQL() + " renewal, " +
               "COALESCE(employee_certifications.cert_aquired_on, date '1000-10-10') acquired, " +
               "COALESCE(employee_certifications.cert_expires_on, date '1000-10-10') expired,  " +
               "(CASE WHEN EXISTS (SELECT 1 FROM employee_certifications WHERE  " +
               "certifications.certification_id = employee_certifications.cert_id " +
               ") THEN 'true' ELSE 'false' END) as isCert " +
               "FROM certifications  " + 
               "LEFT JOIN employee_certifications ON employee_certifications.cert_id = certifications.certification_id " +
               "LEFT JOIN employee on employee.employee_id  =  employee_certifications.employee_id " +
               "WHERE employee.branch_id =   " + getBranch() + " ORDER BY employee.employee_id";
    }
    
}
