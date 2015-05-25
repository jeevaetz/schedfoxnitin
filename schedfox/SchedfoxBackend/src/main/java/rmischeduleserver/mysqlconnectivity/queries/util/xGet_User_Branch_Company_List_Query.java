/*
 * xGet_User_Branch_Company_List_Query.java
 *
 * Created on November 22, 2005, 9:27 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class xGet_User_Branch_Company_List_Query extends GeneralQueryFormat {
    
    private String uid;
    private String mid;
    
    /** Creates a new instance of xGet_User_Branch_Company_List_Query */
    public xGet_User_Branch_Company_List_Query() {
        myReturnString = new String();
    }
    
    public void update(String userId, String managementId) {
        if (managementId.equals("0")) {
            mid = "";
        } else {
            mid = " WHERE management_id = " + managementId;
        }
        uid = userId;
    }
    
    public String toString() {
        StringBuffer sql = new StringBuffer();

        String[] tokens = uid.split(":");

        if (tokens.length != 2) {
            sql.append("SELECT branch_id as bid, branch_name as bname, company_id as cid, company_name as cname, ");
            sql.append("(CASE WHEN EXISTS (SELECT * FROM user_branch_company WHERE user_id = " + uid + " AND ");
            sql.append("user_branch_company.branch_id = branch.branch_id AND user_branch_company.company_id = company.company_id) THEN ");
            sql.append("'true' ELSE 'false' END) as hasaccess ");
            sql.append("FROM ");
            sql.append("management_clients ");
            sql.append("LEFT JOIN branch ON branch_management_id = management_id ");
            sql.append("LEFT JOIN company ON company_management_id = management_id " + mid + " ");
            sql.append("ORDER BY company_name, cid, branch_name");
        } else {
            sql.append("SELECT branch_id as bid, branch_name as bname, company_id as cid, company_name as cname, ");
            sql.append("true as hasaccess ");
            sql.append("FROM ");
            sql.append(this.getManagementSchema() + ".company ");
            sql.append("INNER JOIN " + tokens[0] + ".employee_to_client_access ON employee_to_client_access.employee_id = " + tokens[1] + " ");
            sql.append("INNER JOIN " + tokens[0] + ".client ON client.client_id = employee_to_client_access.client_id ");
            sql.append("INNER JOIN " + this.getManagementSchema() + ".branch ON branch.branch_id = client.branch_id ");
            sql.append("WHERE ");
            sql.append("company.company_db = '" + tokens[0] + "'");
        }
        return sql.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
