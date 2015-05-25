/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_company_by_database_query extends GeneralQueryFormat {

    private String companyDB;

    public void update(String companyDB) {
        this.companyDB = companyDB;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append(super.getManagementSchema() + ".company ");
        sql.append("WHERE ");
        sql.append("upper(company_url) = upper('" + companyDB + "')");
        return sql.toString();
    }


}
