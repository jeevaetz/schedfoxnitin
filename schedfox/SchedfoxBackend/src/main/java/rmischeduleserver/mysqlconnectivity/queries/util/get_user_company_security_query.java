/*
 * get_user_company_security_query.java
 *
 * Created on April 19, 2005, 8:49 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_user_company_security_query extends GeneralQueryFormat {
    
    private String uid;
    private String management;
    
    /** Creates a new instance of get_user_company_security_query */
    public get_user_company_security_query() {
        myReturnString = new String();
    }
    
    public void update(String userId, String managementid) {
        uid = userId;
        management = managementid;
    }
    
    public String toString() {
        String manageSQL = new String();
        if (!management.equals("0")) {
            manageSQL = " WHERE company_management_id = "+ management;
        }
        return "SELECT company.company_id as id, company.company_id, company.company_name as name, " +
                "CASE WHEN company.company_id IN ((SELECT company_id FROM " + getManagementSchema() + ".user_company WHERE user_company.user_id = " + uid + ")) " +
                "THEN 'true' ELSE 'false' END as access FROM company LEFT JOIN " + getManagementSchema() + ".user_company " +
                "ON company.company_id = user_company.company_id AND user_company.user_id = " + uid + manageSQL + " Order by company.company_name";
    }
    
    public boolean hasAccess() {
        return true;
    }
}
