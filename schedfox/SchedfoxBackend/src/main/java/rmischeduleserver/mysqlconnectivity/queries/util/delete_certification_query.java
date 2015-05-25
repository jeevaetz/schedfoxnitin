/*
 * delete_certification_query.java
 *
 * Created on August 18, 2006, 11:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author shawn
 */
public class delete_certification_query extends GeneralQueryFormat {
    
    /** Creates a new instance of delete_certification_query */
    public delete_certification_query(String id) {
        this.myReturnString = "DELETE FROM certifications WHERE certification_id = " + id + ";" +
                              "DELETE FROM client_certifications WHERE client_cert_id = " + id + ";" +
                              "DELETE FROM employee_certifications WHERE cert_id = " + id + ";";
    }
    
    public boolean hasAccess() { return true; }
}
