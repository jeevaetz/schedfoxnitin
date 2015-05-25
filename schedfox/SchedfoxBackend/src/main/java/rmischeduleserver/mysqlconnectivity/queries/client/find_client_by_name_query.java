/*
 * find_client_by_name_query.java
 *
 * Created on August 5, 2005, 12:00 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class find_client_by_name_query extends GeneralQueryFormat {
    
    private String cname;
    
    /** Creates a new instance of find_client_by_name_query */
    public find_client_by_name_query() {
        myReturnString = new String();
    }
    
    public void update(String name) {
        cname = name;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return "SELECT client_id FROM client WHERE client_name = '" + cname + "'";
    }
            
    
}
