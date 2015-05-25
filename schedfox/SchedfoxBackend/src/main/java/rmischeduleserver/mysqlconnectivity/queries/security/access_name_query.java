/*
 * access_name_query.java
 *
 * Created on February 3, 2005, 1:43 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.security;

import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author jason.allen
 */
public class access_name_query extends GeneralQueryFormat{
    
    private static final String MY_QUERY = 
            "Select access_name From access Where access_id = "
    ;
    
    /** Creates a new instance of access_name_query */
    public access_name_query() {
        myReturnString = "";
    }
    
    public void update(String sec){
        myReturnString = MY_QUERY + sec;
    }
    
    public boolean hasAccess() {
        return true;
    }       
}
