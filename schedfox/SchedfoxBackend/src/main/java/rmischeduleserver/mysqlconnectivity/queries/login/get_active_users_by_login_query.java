/*
 * login_query.java
 *
 * Created on January 20, 2005, 5:03 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.login;
import rmischeduleserver.mysqlconnectivity.queries.*;
//import com.twmacinta.util.*;
/**
 *
 * @author ira
 */
public class get_active_users_by_login_query extends GeneralQueryFormat {
    
    /** Creates a new instance of login_query */
    public get_active_users_by_login_query() {

    }

    public void update(String User) {
        super.setPreparedStatement(new Object[]{User});
    }
    
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder commonSql = new StringBuilder();
        commonSql.append("SELECT * FROM ").append(this.getManagementSchema() + "." + getDriver().getTableName("user")).append(" ");
        commonSql.append("WHERE user_login = ? AND ");
        commonSql.append("user_is_deleted = 0");
        
        return commonSql.toString();
    }
}
