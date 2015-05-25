/*
 * security_level_query.java
 *
 * Created on May 12, 2005, 12:15 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.security;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author jason.allen
 */
public class security_level_query extends GeneralQueryFormat {
    
    private String uid;
   
    /** Creates a new instance of security_level_query */
    public security_level_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return "Select "
                + getManagementSchema() + ".user_access.access_id As access_id "
                + "From " + getManagementSchema() + ".user "
                + "Left Join user_access On " + getManagementSchema() + ".user.user_id = user_access.user_id "
                + "Where "
                + "" + getManagementSchema() + ".user.user_id = " + uid;
    }

    public void update(String userId) {
        uid = userId;
    }    
}
