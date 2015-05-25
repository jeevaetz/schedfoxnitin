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
public class security_level_for_user_and_groups_query extends GeneralQueryFormat {
    
    private String uid;
   
    /** Creates a new instance of security_level_query */
    public security_level_for_user_and_groups_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        StringBuffer sql = new StringBuffer();
        String[] tokens = uid.split(":");

        if (tokens.length == 2) {
            uid = "-"  + tokens[1];
        }


        return "(Select user_access.access_id as access_id FROM control_db.user_access  " +
                "WHERE user_access.user_id = " + uid + ") UNION " +
                "(SELECT groups_access.access_id FROM control_db.user_groups LEFT JOIN control_db.groups_access " +
                "ON groups_access.groups_id = user_groups.groups_id WHERE user_id = " + uid + ")";
    }

    public void update(String userId) {
        uid = userId;
    }    
}
