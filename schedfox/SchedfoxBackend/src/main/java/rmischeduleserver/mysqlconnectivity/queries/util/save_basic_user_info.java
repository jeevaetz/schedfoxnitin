/*
 * save_basic_user_info.java
 *
 * Created on November 28, 2005, 12:38 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class save_basic_user_info extends GeneralQueryFormat {
    
    private String ID;
    private String Fname;
    private String Mname;
    private String Lname;
    private String Email;
    
    /** Creates a new instance of save_basic_user_info */
    public save_basic_user_info() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String id, String fname, String mname, String lname, String email) {
        ID = id;
        Fname = fname.replaceAll("'", "''");
        Mname = mname.replaceAll("'", "''");
        Lname = lname.replaceAll("'", "''");
        Email = email.replaceAll("'", "''");
    }
    
    public String toString() {
        return "UPDATE control_db.user SET user_first_name = '" + Fname + "' , user_middle_initial = '" + Mname + "' , " +
                "user_last_name = '" + Lname + "' , user_email = '" + Email + "' WHERE user_id = " + ID + ";";
    }
    
}
