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
public class create_user_query extends GeneralQueryFormat {
    
    private String firstName;
    private String lastName;
    private String middleInitial;
    private String loginName;
    private String clearCasePassword;
    private String email;

    private int companyId;
    
    /** Creates a new instance of create_new_user_query */
    public create_user_query() {
        myReturnString = new String();
        firstName = "";
        lastName = "";
        middleInitial = "";
        loginName = "";
        clearCasePassword = "";
        email = "";
    }
    
    
    public String toString() {
        StringBuffer sql = new StringBuffer();
        
        sql.append("Insert into control_db.user ");
        sql.append("(user_md5, user_login, ");
        sql.append("user_first_name, ");
        sql.append("user_last_name, ");
        sql.append("user_middle_initial, ");
        sql.append("user_password, user_email, ");
        sql.append("user_management_id) ");
        sql.append("Values(");
        sql.append("md5('").append(this.loginName).append("'), ");
        sql.append("'").append(this.loginName).append("',");
        sql.append("'").append(this.firstName).append("',");
        sql.append("'").append(this.lastName).append("',");
        sql.append("'").append(this.middleInitial).append("',");
        sql.append("md5('").append(this.clearCasePassword).append("'), ");
        sql.append("'").append(this.email).append("', ");
        sql.append(companyId);
        sql.append(");");
        
        sql.append("INSERT INTO control_db.user_branch_company (user_id, company_id, branch_id) VALUES ((SELECT user_id FROM control_db.user ");
	sql.append(" WHERE ");
        sql.append("user_management_id = " + companyId + " LIMIT 1), ");
        sql.append("(SELECT company_id FROM control_db.company WHERE company_management_id = ");
	sql.append(companyId + " LIMIT 1), ");
        sql.append("(SELECT branch_id FROM control_db.branch WHERE branch_management_id = ");
        sql.append(companyId + " LIMIT 1));");

        sql.append("INSERT INTO control_db.user_company (user_id, company_id) VALUES ");
        sql.append("((SELECT user_id FROM control_db.user ");
	sql.append(" WHERE user_management_id = " + companyId + " LIMIT 1), ");
        sql.append(" (SELECT company_id FROM control_db.company WHERE company_management_id = ");
	sql.append(companyId + " LIMIT 1)); ");

        sql.append("INSERT INTO control_db.user_groups (user_id, groups_id) VALUES ");
        sql.append("((SELECT user_id FROM control_db.user ");
	sql.append(" WHERE user_management_id = " + companyId + " LIMIT 1) ,3);");
        
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
            String loginName, String password, String email, int companyId) {
        this.firstName = firstName.replaceAll("'", "''").trim();
        this.lastName = lastName.replaceAll("'", "''").trim();
        this.middleInitial = middleInitial.replaceAll("'", "''").trim();
        this.clearCasePassword = password.replaceAll("'", "''").trim();
        this.loginName = loginName.replaceAll("'", "''").trim();
        this.companyId = companyId;
        this.email = email;
    }
}
