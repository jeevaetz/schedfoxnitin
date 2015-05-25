/*
 * schedule_client_certifications_query.java
 *
 * Created on June 13, 2005, 11:44 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class schedule_client_certifications_query extends GeneralQueryFormat {
    
    /** Creates a new instance of schedule_client_certifications_query */
    public schedule_client_certifications_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return  "SELECT client_certifications.client_id as cid," +
                "client_certifications.client_cert_id as certid," +
                "certifications.certification_name, " +
                "certifications.certification_description " +
                "FROM client_certifications " +
                "LEFT JOIN client ON client.client_id = client_certifications.client_id " +
                "LEFT JOIN certifications ON " +
                "certifications.certification_id = client_certifications.client_cert_id " +
                "WHERE client.branch_id = " + getBranch();
    }
    
}
