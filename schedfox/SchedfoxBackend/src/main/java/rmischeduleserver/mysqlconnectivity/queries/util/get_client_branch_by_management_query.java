/*
 * get_client_branch_by_management_query.java
 *
 * Created on September 20, 2005, 9:18 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class get_client_branch_by_management_query extends GeneralQueryFormat {
    
    private String uid;
    private String mycompany;
    private String loginType;
    
    /** Creates a new instance of get_client_branch_by_management_query */
    public get_client_branch_by_management_query() {
        myReturnString = new String();
    }
    
    public void update(String user_id, String company, String loginType) {
        this.uid = user_id;
        this.mycompany = company;
        this.loginType = loginType;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        StringBuffer sql = new StringBuffer();

        if (this.loginType.equalsIgnoreCase("user")) {
            sql.append("SELECT DISTINCT company.*, branch.*, ");
            sql.append("branch_info.address, branch_info.address2, branch_info.city, branch_info.state, branch_info.zip, branch_info.phone ");
            sql.append("FROM ");
            sql.append("user_branch_company ");
            sql.append("LEFT JOIN branch ON branch.branch_id = user_branch_company.branch_id ");
            sql.append("LEFT JOIN branch_info ON user_branch_company.branch_id = branch_info.branch_id ");
            sql.append("LEFT JOIN company ON company.company_id = user_branch_company.company_id ");
            sql.append("WHERE user_branch_company.user_id = "+ uid + " AND company.company_id IS NOT null ");
            sql.append("AND branch.branch_id IS NOT null ORDER BY company_name, branch_name");
        } else {
            sql.append("SELECT DISTINCT company.*, branch.*, ");
            sql.append("branch_info.address, branch_info.address2, branch_info.city, branch_info.state, branch_info.zip, branch_info.phone ");
            sql.append("FROM ");
            sql.append(this.getManagementSchema() + ".company ");
            if (this.loginType.equalsIgnoreCase("Employee")) {
                sql.append("INNER JOIN " + mycompany + ".employee ON employee.employee_id = " + uid + " ");
                sql.append("INNER JOIN " + this.getManagementSchema() + ".branch ON branch.branch_id = employee.branch_id ");
                sql.append("LEFT JOIN " + this.getManagementSchema() + ".branch_info ON branch.branch_id = branch_info.branch_id ");
            } else if (this.loginType.equalsIgnoreCase("Client")) {
                sql.append("INNER JOIN " + mycompany + ".client ON client.client_id = " + uid + " ");
                sql.append("INNER JOIN " + this.getManagementSchema() + ".branch ON branch.branch_id = client.branch_id ");
                sql.append("LEFT JOIN " + this.getManagementSchema() + ".branch_info ON branch.branch_id = branch_info.branch_id ");
            }
            sql.append("WHERE ");
            sql.append("company.company_db = '" + mycompany + "'");
        }

        return sql.toString();
    }
    
}
