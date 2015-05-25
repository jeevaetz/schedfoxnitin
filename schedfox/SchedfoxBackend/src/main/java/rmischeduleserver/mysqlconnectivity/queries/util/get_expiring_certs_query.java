/*
 * get_expiring_certs_query.java
 *
 * Created on October 12, 2006, 10:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.util;

import java.util.*;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 * A query to get a list of the employee certifications that are going to expire during the
 * given time period.
 *
 * @author shawn
 */
public class get_expiring_certs_query extends GeneralQueryFormat {
    
    /** Creates a new instance of get_expiring_certs_query */
    public get_expiring_certs_query(String branchId, String start, String end) {

        this.myReturnString =   " SELECT (employee_last_name || ', ' || employee_first_name) as ename," +
                                        " certification_name as certname," +
                                        " cert_expires_on as expiration" +
                                " FROM employee_certifications empcerts, employee emp, certifications certs" +
                                " WHERE (DATE '" + start + "', DATE '" + end + "') OVERLAPS (cert_expires_on, cert_expires_on)" +
//                                " WHERE cert_expires_on < (DATE '" + end + "') " +
                                        " AND emp.employee_id = empcerts.employee_id" +
                                        " AND emp.branch_id = " + branchId +
                                        " AND certification_id = empcerts.cert_id" +
                                        " AND emp.employee_is_deleted = 0" +
                                " ORDER BY expiration ASC;";
    }

    public boolean hasAccess() { return true; }
    
}
