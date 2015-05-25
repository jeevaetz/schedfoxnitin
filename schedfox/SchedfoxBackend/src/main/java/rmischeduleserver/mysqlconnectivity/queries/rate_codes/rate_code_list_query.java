/*
 * client_list.java
 *
 * Created on January 21, 2005, 1:04 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.rate_codes;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author jason.allen
 */
public class rate_code_list_query extends GeneralQueryFormat {
    
    private String MY_QUERY = (
            " SELECT " + 
            " rate_code_id     as id, "       +
            " rate_code_name   as name, "     +
            " usked_rate_code  as usked_id " +
            " FROM rate_code " +
            " ORDER by rate_code_name "
    );
    
    /** Creates a new instance of client_list */
    public rate_code_list_query() {
        myReturnString = new String();
    }
    
    public void update() {
        
    }
    
    public boolean hasAccess(){
        return true;
    }
    
    public String toString() {
        return MY_QUERY;
    }
    
}
