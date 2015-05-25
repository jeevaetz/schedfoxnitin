/*
 * get_user_management_query.java
 *
 * Created on April 26, 2005, 12:02 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.login;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_user_management_query extends GeneralQueryFormat {
    
    /** Creates a new instance of get_user_management_query */
    public get_user_management_query() {

    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String uid) {
        super.setPreparedStatement(new Object[]{Integer.parseInt(uid)});
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \"user\".* ");
        sql.append("FROM " + getManagementSchema() + "." +  getDriver().getTableName("user") + " ");
        sql.append("WHERE user_id = ?");
        return sql.toString();
    }
    
}
