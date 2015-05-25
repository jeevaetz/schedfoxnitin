/*
 * change_password_for_user_query.java
 *
 * Created on November 28, 2005, 10:45 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class change_password_for_user_query extends GeneralQueryFormat {
    
    private boolean hasMD5;
    private String md5OrPw;
    private String newPW;
    private String userId;
    
    /** Creates a new instance of change_password_for_user_query */
    public change_password_for_user_query() {
        myReturnString = "";
        hasMD5 = false;
    }
    
    public void updateForMD5(String oldMD5PW, String NewPassword, String uid) {
        hasMD5 = true;
        md5OrPw = oldMD5PW;
        newPW = NewPassword;
        userId = uid;
    }
    
    public void updateForPw(String oldPW, String newPassword, String uid) {
        md5OrPw = oldPW;
        newPW = newPassword;
        userId = uid;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        if (hasMD5) {
            return "UPDATE control_db.user SET user_password = md5('" + newPW + "') WHERE user_id = " + userId + " AND " +
                    "user_password = '" + md5OrPw + "';";
        } else {
            return "UPDATE control_db.user SET user_password = md5('" + newPW + "') WHERE user_id = " + userId + " AND " +
                    "user_password = md5('" + md5OrPw + "');";
        }
    }
    
}
