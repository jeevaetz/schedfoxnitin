/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.new_user;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 * Danger, we really need to defend against SQL injection in a more robust manner
 * than just getting rid of all single quotes...For now however I have no 
 * mechanism for creating a PreparedStatement this should be done asap.
 * @author Ira
 */
public class create_new_user_query extends GeneralQueryFormat {
    
    private String firstName;
    private String lastName;
    private String middleInitial;
    private String loginName;
    private String clearCasePassword;
    private String companyName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String schemaName;
    
    /** Creates a new instance of create_new_user_query */
    public create_new_user_query() {
        myReturnString = new String();
        firstName = "";
        lastName = "";
        middleInitial = "";
        loginName = "";
        clearCasePassword = "";
        companyName = "";
        addressLine1 = "";
        addressLine2 = "";
        city = "";
        state = "";
        zip = "";
        phoneNumber = "";
        schemaName = "";
    }
    
    
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO control_db.management_clients ");
        sql.append("(management_id, management_client_name, management_client_address, ");
	sql.append("management_client_address2, management_client_city, management_client_state, ");
	sql.append("management_client_zip, management_client_phone, management_client_email) VALUES ");
        sql.append("((SELECT (max(management_id) + 1) as new_id FROM control_db.management_clients), ");
	sql.append("'" + this.companyName + "','" + this.addressLine1 + "','" + this.addressLine2);
        sql.append("','" + this.city + "','" + this.state + "','" + this.zip + "','" + this.phoneNumber + "',");
        sql.append("'" + this.loginName + "');");
        
        sql.append("INSERT INTO control_db.company (company_id, company_name, company_db, company_management_id) VALUES ");
	sql.append(" ((SELECT (MAX(company_id) + 1) FROM control_db.company), '" + this.companyName);
        sql.append("','" + schemaName + "', ");
        sql.append("(SELECT MAX(management_id) FROM control_db.management_clients WHERE management_client_name = '" + companyName + "'));");
        
        sql.append("INSERT INTO control_db.branch (branch_id, branch_name, branch_management_id) VALUES ");
	sql.append("((SELECT (MAX(branch_id) + 1) FROM control_db.branch), '" + companyName + "',");
        sql.append("(SELECT MAX(management_id) FROM control_db.management_clients WHERE management_client_name = '" + companyName + "'));");
        
        sql.append("Insert into control_db.user ");
        sql.append("(user_id, user_md5, user_login, ");
        sql.append("user_first_name, ");
        sql.append("user_last_name, ");
        sql.append("user_middle_initial, ");
        sql.append("user_password, ");
        sql.append("user_management_id) ");
        sql.append("Values(");
        sql.append("(SELECT (MAX(user_id) + 1) FROM control_db.user),");
        sql.append("md5('").append(this.loginName).append("'), ");
        sql.append("'").append(this.loginName).append("',");
        sql.append("'").append(this.firstName).append("',");
        sql.append("'").append(this.lastName).append("',");
        sql.append("'").append(this.middleInitial).append("',");
        sql.append("md5('").append(this.clearCasePassword).append("'), ");
        sql.append("(SELECT MAX(management_id) FROM control_db.management_clients WHERE management_client_name = '" + companyName + "') ");
        sql.append(");");
        
        sql.append("INSERT INTO control_db.user_branch_company (user_id, company_id, branch_id) VALUES ((SELECT user_id FROM control_db.user ");
	sql.append(" WHERE ");
        sql.append("user_management_id = (SELECT MAX(management_id) FROM control_db.management_clients WHERE management_client_name = '" + companyName + "') LIMIT 1), ");
        sql.append("(SELECT company_id FROM control_db.company WHERE company_management_id = ");
	sql.append("(SELECT MAX(management_id) FROM control_db.management_clients WHERE management_client_name = '" + companyName + "') LIMIT 1), ");
        sql.append("(SELECT branch_id FROM control_db.branch WHERE branch_management_id = ");
        sql.append("(SELECT MAX(management_id) FROM control_db.management_clients WHERE management_client_name = '" + companyName + "') LIMIT 1));");
        
        sql.append("INSERT INTO control_db.user_groups (user_id, groups_id) VALUES ");
        sql.append("((SELECT user_id FROM control_db.user ");
	sql.append(" WHERE user_management_id = (SELECT MAX(management_id) FROM control_db.management_clients WHERE management_client_name = '" + companyName + "') LIMIT 1) ,3);");
        
        return sql.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    /**
     * Takes in information, prevents simple SQL injection attacks...
     * @param firstName
     * @param lastName
     * @param middleInitial
     * @param loginName
     * @param password
     */
    public void update(String firstName, String lastName, String middleInitial, 
            String loginName, String password, String companyName, String address1,
            String address2, String city, String state, String zip, String phoneNumber,
            String schemaName) {
        this.firstName = firstName.replaceAll("'", "''").trim();
        this.lastName = lastName.replaceAll("'", "''").trim();
        this.middleInitial = middleInitial.replaceAll("'", "''").trim();
        this.clearCasePassword = password.replaceAll("'", "''").trim();
        this.loginName = loginName.replaceAll("'", "''").trim();
        this.companyName = companyName.replaceAll("'", "''").trim();
        this.addressLine1 = address1.replaceAll("'", "''").trim();
        this.addressLine2 = address2.replaceAll("'", "''").trim();
        this.city = city.replaceAll("'", "''").trim();
        this.state = state.replaceAll("'", "''").trim();
        this.zip = zip.replaceAll("'", "''").trim();
        this.phoneNumber = phoneNumber.replaceAll("'", "''").trim();
        this.schemaName = schemaName.toLowerCase();
        
    }
}
