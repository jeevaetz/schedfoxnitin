/*
 * getUserIdQuery.java
 *
 * Created on January 24, 2005, 12:39 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class getUserIdQuery extends GeneralQueryFormat {
    /** Creates a new instance of getUserIdQuery */
    private String uid;
    public getUserIdQuery() {
        myReturnString = new String();
    }
    
    public void update(String id) {
        uid = id;    
    }
    
    public String toString() {
        StringBuffer sql = new StringBuffer();

        String[] tokens = uid.split(":");

        if (tokens.length != 2) {
            sql.append("SELECT ");
            sql.append("user_first_name,");
            sql.append("user_last_name, ");
            sql.append("user_id, ");
            sql.append("user_login, ");
            sql.append("user_md5, ");
            sql.append("user_email as email, ");
            sql.append("can_view_ssn ");
            sql.append("FROM control_db." + getDriver().getTableName("user") + " ");
            sql.append("WHERE ");
            sql.append("control_db." + getDriver().getTableName("user") + ".user_id = " + uid);
        } else {
            //Employee login
            sql.append("SELECT ");
            sql.append("employee_first_name as user_first_name,");
            sql.append("employee_last_name as user_last_name, ");
            sql.append("'" + tokens[0] + "' || ':' || employee_id as user_id, ");
            sql.append("employee_login as user_login, ");
            sql.append("employee_password as user_md5, ");
            sql.append("employee_email as email, ");
            sql.append("can_view_ssn ");
            sql.append("FROM " + tokens[0] + ".employee ");
            sql.append("WHERE ");
            sql.append("employee_id = " + tokens[1]);
        }
        return sql.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
