/*
 * access_list_query.java
 *
 * Created on May 23, 2005, 8:41 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.security;

import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author jason.allen
 */
public class access_list_query  extends GeneralQueryFormat{
    
    String myStr = "";
    
    /** Creates a new instance of access_list_query */
    public access_list_query() {
        myReturnString = "";
        myStr = "";
    }
    
    public void update(int BegLevel, int EndLevel){
        myStr = "Select " 
              + "  access_id, access_name ,access_desc "
              + " From access "
              + " Where "
              + "  access_id > " + BegLevel + " And " 
              + "  access_id < " + EndLevel;
    }
    
    public String toString(){
        return myStr;
    }
    
    public boolean hasAccess(){
        return true;
    }
}
