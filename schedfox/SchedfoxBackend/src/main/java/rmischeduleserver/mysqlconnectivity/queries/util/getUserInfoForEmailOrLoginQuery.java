/*
 * getUserInfoForEmailOrLoginQuery.java
 *
 * Created on November 25, 2005, 9:24 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class getUserInfoForEmailOrLoginQuery extends GeneralQueryFormat {
    
    private String myInfo;
    
    /** Creates a new instance of getUserInfoForEmailOrLoginQuery */
    public getUserInfoForEmailOrLoginQuery() {
        myReturnString = new String();
    }
    
    public void update(String info) {
        myInfo = info;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return "SELECT user_email as email, user_password as md5, user_id as uid FROM " + getManagementSchema() + ".user WHERE user_email = '" + myInfo + "' OR user_login = '" + myInfo + "';";
    }
    
}
