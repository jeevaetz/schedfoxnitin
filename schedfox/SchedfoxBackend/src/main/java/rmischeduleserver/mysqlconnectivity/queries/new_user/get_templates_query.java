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
public class get_templates_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append(this.getManagementSchema() + ".template_database ");
        sql.append("ORDER BY template_database_id ASC; ");
        return sql.toString();
    }

}
