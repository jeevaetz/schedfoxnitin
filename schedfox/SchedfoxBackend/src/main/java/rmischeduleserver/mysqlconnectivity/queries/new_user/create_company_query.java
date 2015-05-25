/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.new_user;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class create_company_query extends GeneralQueryFormat {
    private String companyName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String email;
    private String schemaName;

    public create_company_query() {
        companyName = "";
        addressLine1 = "";
        addressLine2 = "";
        city = "";
        state = "";
        zip = "";
        phoneNumber = "";
        email = "";
        schemaName = "";
    }

    public void update(String companyName, String addressLine1, String addressLine2,
            String city, String state, String zip, String phoneNumber, String email,
            String schemaName) {
        this.companyName = companyName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.schemaName = schemaName;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO control_db.management_clients ");
        sql.append("(management_id, management_client_name, management_client_address, ");
	sql.append("management_client_address2, management_client_city, management_client_state, ");
	sql.append("management_client_zip, management_client_phone, management_client_email) VALUES ");
        sql.append("((SELECT (MAX(management_id) + 1) as new_id FROM control_db.management_clients), ");
	sql.append("'" + this.companyName + "','" + this.addressLine1 + "','" + this.addressLine2);
        sql.append("','" + this.city + "','" + this.state + "','" + this.zip + "','" + this.phoneNumber + "',");
        sql.append("'" + this.email + "');");

        sql.append("INSERT INTO control_db.company (company_id, company_name, company_db, company_management_id) VALUES ");
	sql.append(" ((SELECT (MAX(company_id) + 1) FROM control_db.company), '" + this.companyName);
        sql.append("','" + schemaName + "', ");
        sql.append("(SELECT MAX(management_id) FROM control_db.management_clients WHERE management_client_name = '" + companyName + "'));");

        sql.append("INSERT INTO control_db.branch (branch_id, branch_name, branch_management_id) VALUES ");
	sql.append("((SELECT (MAX(branch_id) + 1) FROM control_db.branch), '" + companyName + "',");
        sql.append("(SELECT MAX(management_id) FROM control_db.management_clients WHERE management_client_name = '" + companyName + "'));");

        sql.append("SELECT MAX(management_id) FROM control_db.management_clients;");
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

}
