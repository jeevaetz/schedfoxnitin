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
public class security_level_edit_query extends GeneralQueryFormat {
    
    public String user_id;
    
       /** Creates a new instance of GetBranchesQuery */
    private static final String MY_QUERY = 
        ("Select access_id from user_access where user_id = ");
   
    /** Creates a new instance of security_level_query */
    public security_level_edit_query() {
        myReturnString = new String();
    }
    
    public void update(String s){
        user_id = s;        
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return MY_QUERY + user_id;        
    }
}
