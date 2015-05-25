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
public class get_existing_schemas_for_co extends GeneralQueryFormat {
    private String companyName;

    public void update(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT nspname ");
        sql.append("FROM pg_namespace ");
        sql.append("WHERE ");
        sql.append("nspname LIKE ('" + companyName + "%_db')");
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
}
