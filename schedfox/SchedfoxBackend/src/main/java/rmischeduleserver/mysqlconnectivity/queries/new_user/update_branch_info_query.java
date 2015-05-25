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
public class update_branch_info_query extends GeneralQueryFormat {

    private String company_schema;

    public void update(String company_schema) {
        this.company_schema = company_schema;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer where = new StringBuffer();
        where.append("( SELECT branch_id FROM control_db.company ");
	where.append("  INNER JOIN control_db.management_clients ON management_clients.management_id = company.company_management_id ");
	where.append("  INNER JOIN control_db.branch ON branch.branch_management_id = company.company_management_id ");
        where.append("    AND company.company_db = '" + this.company_schema + "' LIMit 1) ");

        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE " + company_schema + ".client SET branch_id = " + where + ";");
        sql.append("UPDATE " + company_schema + ".employee SET branch_id = " + where + ";");
        return sql.toString();
    }

}
