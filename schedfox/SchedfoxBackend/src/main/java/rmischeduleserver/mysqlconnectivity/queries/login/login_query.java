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
public class login_query extends GeneralQueryFormat {
    
    /** Creates a new instance of login_query */
    public login_query() {

    }

    public void update(String User, String Pw) {
        super.setPreparedStatement(new Object[]{User, Pw});
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
        StringBuffer commonSql = new StringBuffer();
        commonSql.append("SELECT * FROM " + this.getManagementSchema() + "." + getDriver().getTableName("user"));
        commonSql.append(" WHERE user_login = ? AND ");
        commonSql.append("user_password = md5(?) AND user_is_deleted = 0");
        commonSql.append(" LIMIT 1");
        
        return commonSql.toString();
    }
}
