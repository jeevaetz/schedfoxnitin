/*
 * client_certifications_list_query.java
 *
 * Created on May 26, 2005, 8:18 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class client_certifications_list_query extends GeneralQueryFormat {
    
    private String cust_id;
    private boolean showAllEmps;
    
    /** Createsa new instance of client_employees_banned_list_query */
    public client_certifications_list_query() {
        myReturnString = new String();
    }
    
    public void update(String cid, boolean showAll) {
        cust_id = cid;
        showAllEmps = showAll;
    }
    
    public String toString() {
        StringBuilder myString = new StringBuilder();
        myString.append("Select certifications.certification_id as cid, " +
                "certifications.certification_name as cert_name," +
                "certifications.certification_description as cert_desc, " +
                //"certifications.certification_default_renewal_time as renewal, " +
                "(CASE WHEN EXISTS (SELECT 1 FROM client_certifications WHERE " +
                "client_certifications.client_id = " + cust_id + " AND " +
                "certifications.certification_id = client_certifications.client_cert_id	) THEN 'true' ELSE 'false' END) as isCert " +
                "FROM certifications LEFT JOIN client on client.client_id  = " + cust_id +
                " WHERE client.branch_id = " + getBranch());
        if (!showAllEmps) {
            myString.append(" AND (EXISTS (SELECT 1 FROM client_certifications WHERE " +
                "client_certifications.client_id = " + cust_id + " AND " +
                "certifications.certification_id = client_certifications.client_cert_id))");
        }
        return myString.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
}
