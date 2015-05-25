/*
 * client_certification_update.java
 *
 * Created on May 26, 2005, 8:43 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.util.*;
/**
 *
 * @author ira
 */
public class client_certification_update extends GeneralQueryFormat {
    
    private String cust_id;
    private ArrayList cert_id;
    
    /** Creates a new instance of client_certification_update */
    public client_certification_update() {
        myReturnString = new String();
    }
    
    public void update(String cid, ArrayList certs) {
        cust_id = cid;
        if (cid == null) {
            cust_id = "(SELECT MAX(client_id) FROM client)";
        }
        cert_id = certs;
    }
    
    public String toString() {
        StringBuilder myStr = new StringBuilder();
        for (int i = 0; i < cert_id.size(); i++) {
            myStr.append("INSERT INTO client_certifications (client_id, client_cert_id) VALUES (" + cust_id + "," + (String)cert_id.get(i) + ");");
        }
        return "DELETE FROM client_certifications WHERE client_id = " + cust_id + ";" + myStr + ";";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_CLIENT_CERT;
    }
    
}
