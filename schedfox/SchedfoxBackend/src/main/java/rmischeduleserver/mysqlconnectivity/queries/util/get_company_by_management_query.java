/*
 * get_company_by_management_query.java
 *
 * Created on April 28, 2005, 10:06 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_company_by_management_query extends GeneralQueryFormat {
    
    private String manageID;
    
    /** Creates a new instance of get_company_by_management_query */
    public get_company_by_management_query() {
        myReturnString = new String();
    }
    
    public void update(String manageId) {
        manageID = manageId;
    }
    
    public String toString() {
        String sql = new String();
        if (!manageID.equals("0")) {
            sql = " WHERE company_management_id = " + manageID;
        }
           
        return "SELECT company_id, company_name, company_db, company_management_id, management_client_name, " +
               "management_clients.management_date_started FROM " + getManagementSchema() +
               ".company LEFT JOIN " + getManagementSchema() + ".management_clients ON management_clients.management_id = " +
               "company.company_management_id " + sql;
               
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
