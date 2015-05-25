/*
 * access_list_save_query.java
 *
 * Created on May 23, 2005, 10:22 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.security;

import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author jason.allen
 */
public class access_list_save_query  extends GeneralQueryFormat{
    
    private String user_id;
    private String myAccess;
    
    /** Creates a new instance of access_list_save_query */
    public access_list_save_query() {
        myReturnString = "";
        myAccess = "";
    }
    
    public void setAccessUser(String uid){
        user_id = uid;        
    }
    
    public void addAccess(int level){
        myAccess += "insert into user_access (user_id, access_id) values(" + user_id + ", " + level + ");";
    }
    
    public String toString(){
        return "Delete From user_access where user_id = " + user_id + ";" + myAccess;
    }
    
    
    public boolean hasAccess() {
        return true;
    }        
}
