/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.new_user;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author Ira
 */
public class check_if_user_exists_query extends GeneralQueryFormat {

    private String userName;
    
    public check_if_user_exists_query() {
        myReturnString = new String();
        userName = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String userName) {
        this.userName = userName.replaceAll("'", "''").trim();
    }
    
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(*) as number_of_users FROM control_db.user ");
        sql.append("WHERE upper(user_login) = upper('" + this.userName + "') ");
        sql.append("AND user_is_deleted = 0;");
        return sql.toString();
    }
}
