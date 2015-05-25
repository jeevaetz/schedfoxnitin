/*
 * get_certifications_for_client_branch_query.java
 *
 * Created on October 13, 2005, 2:48 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class get_certifications_for_client_branch_query extends RunQueriesEx {
    
    /** Creates a new instance of get_certifications_for_client_branch_query */
    public get_certifications_for_client_branch_query() {
        myReturnString = new String();
    }
    
    public String toString() {
        return "SELECT certifications.certification_id certid,  " +
               "client.client_id cid, " +
               "certifications.certification_name cert_name, " +
               "certifications.certification_description cert_desc, " +
               //"certifications.certification_default_renewal_time renewal " +
               this.getDriver().getCurrentDateSQL() + " renewal " +
               "FROM certifications  " + 
               "LEFT JOIN client_certifications ON client_certifications.client_cert_id = certifications.certification_id " +
               "LEFT JOIN client on client.client_id  =  client_certifications.client_id " +
               "WHERE client.branch_id =   " + getBranch() + " ORDER BY client.client_id";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public boolean isCertificationQuery() {
        return true;
    }
    
}
