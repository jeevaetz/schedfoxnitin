/*
 * branch_list_query.java
 *
 * Created on January 24, 2005, 12:08 PM
 */
package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author ira
 */
public class branch_list_query extends GeneralQueryFormat {

    private String UserId;

    /** Creates a new instance of branch_list_query */
    public branch_list_query() {
        myReturnString = new String();
    }

    public void update(String userId) {
        UserId = userId;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("branch.* ");
        sql.append("FROM control_db.user_branch ");
        sql.append("LEFT JOIN control_db.branch ON branch.branch_id = user_branch.branch_id ");
        sql.append("WHERE user_branch.user_id = " + UserId + " ");
        sql.append("ORDER BY branch_name");
        return sql.toString();
    }

    public boolean hasAccess() {
        return true;
    }
}
