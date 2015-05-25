/*
 * get_user_by_password_id_query.java
 *
 * Created on February 9, 2006, 10:59 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class get_user_by_password_id_query extends GeneralQueryFormat {
    
    private String uid;
    private String pw;
    
    /** Creates a new instance of get_user_by_password_id_query */
    public get_user_by_password_id_query() {
        myReturnString = "";
    }
    
    public void update(String uid, String pw) {
        this.uid = uid;
        this.pw = pw;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return "SELECT * FROM " + getManagementSchema() + ".user WHERE user_password = '" + pw + "' AND user_id = " + uid + ";";
    }
    
}
