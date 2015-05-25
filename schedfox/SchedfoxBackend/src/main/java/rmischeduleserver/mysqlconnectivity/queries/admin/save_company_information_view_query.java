/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.admin;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_company_information_view_query extends GeneralQueryFormat {

    private int company_view_options_id;
    private int company_id;
    private int company_view_id;
    private String value;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(int company_view_options_id, int company_id, int company_view_id, boolean hasAccess) {
        this.company_id = company_id;
        this.company_view_id = company_view_id;
        this.company_view_options_id = company_view_options_id;

        value = "false";
        if (hasAccess) {
            value = "true";
        }
    }

    public void update(int company_view_options_id, int company_id, int company_view_id, String value) {
        this.company_id = company_id;
        this.company_view_id = company_view_id;
        this.value = value;
        this.company_view_options_id = company_view_options_id;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM ");
        sql.append(this.getManagementSchema() + ".company_view_options ");
        sql.append("WHERE ");
        sql.append("company_id = " + this.company_id + " AND ");
        sql.append("company_view_id = " + this.company_view_id + ";");
        sql.append("INSERT INTO ");
        sql.append(this.getManagementSchema() + ".company_view_options ");
        sql.append("(company_id, company_view_id, option_value) ");
        sql.append("VALUES ");
        sql.append("(" + this.company_id + "," + this.company_view_id + ",'" + value + "')");

        return sql.toString();
    }
}
