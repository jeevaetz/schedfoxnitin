/*
 * GetCompaniesQuery.java
 *
 * Created on January 19, 2005, 11:17 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.management;

import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author ira
 */
public class GetCompaniesQuery extends GeneralQueryFormat {
   
    private static final String MY_QUERY = (
            "Select * from control_db.company");
    
    private String returnString;
    
    
    /** Creates a new instance of GetCompaniesQuery */
    public GetCompaniesQuery() {
        returnString = new String();
    }

    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return MY_QUERY;
    }

    public void update(String val) {
    }
    
}
