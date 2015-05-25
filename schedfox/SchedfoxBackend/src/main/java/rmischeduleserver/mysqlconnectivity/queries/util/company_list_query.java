/*
 * company_list_query.java
 *
 * Created on January 24, 2005, 11:47 AM
 */
package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author ira
 */
public class company_list_query extends GeneralQueryFormat {

    private String UserId;

    /** Creates a new instance of company_list_query */
    public company_list_query() {
        myReturnString = new String();
    }

    public void update(String userId) {
        UserId = userId + " Order By company_name";
    }

    public boolean hasAccess() {
        return true;
    }

    public String toString() {
        StringBuffer sql = new StringBuffer();

        String[] tokens = UserId.split(":");

        if (tokens.length != 2) {
            sql.append("SELECT ");
            sql.append("company.company_id, ");
            sql.append("company_name, ");
            sql.append("company_db ");
            sql.append("FROM user_company ");
            sql.append("LEFT JOIN company ON company.company_id = user_company.company_id ");
            sql.append("WHERE ");
            sql.append("user_company.user_id = " + this.UserId);
        } else {
            sql.append("SELECT ");
            sql.append("company.company_id, ");
            sql.append("company_name, ");
            sql.append("company_db ");
            sql.append("FROM " + this.getManagementSchema() + ".company ");
            sql.append("WHERE ");
            sql.append("company.company_db = '" + tokens[0] + "'");
        }

        return sql.toString();
    }
}
