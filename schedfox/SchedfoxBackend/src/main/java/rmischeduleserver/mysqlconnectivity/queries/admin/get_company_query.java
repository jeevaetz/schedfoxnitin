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
public class get_company_query extends GeneralQueryFormat {

    private int company_id;

    public void update(int company_id) {
        this.company_id = company_id;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM " + this.getManagementSchema() + ".company ");
        sql.append("WHERE ");
        sql.append("company_id = " + this.company_id);
        return sql.toString();
    }

}
