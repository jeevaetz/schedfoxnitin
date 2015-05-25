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
public class save_company_url_information_query extends GeneralQueryFormat {

    private int company_id;
    private String company_url;

    public void update(int company_id, String company_url) {
        this.company_id = company_id;
        this.company_url = company_url;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        sql.append(this.getManagementSchema() + ".company ");
        sql.append("SET ");
        sql.append("company_url = '" + this.company_url + "'");
        sql.append("WHERE ");
        sql.append("company_id = " + this.company_id);
        return sql.toString();
    }

}
