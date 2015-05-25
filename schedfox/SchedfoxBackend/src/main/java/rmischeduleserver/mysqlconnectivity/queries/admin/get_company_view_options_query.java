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
public class get_company_view_options_query extends GeneralQueryFormat {

    private int company_id;
    private int option_view_id = -1;

    public void update(int company_id) {
        this.company_id = company_id;
    }

    public void update(int company_id, int option_view_id) {
        this.company_id = company_id;
        this.option_view_id = option_view_id;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("company_view.company_view_id, company_view_option_type, company_view_key, ");
        sql.append("company_view_options.company_view_options_id, ");
        sql.append("COALESCE(option_value, company_view_default_value) as myvalue ");
        sql.append("FROM ");
        sql.append("control_db.company_view ");
        sql.append("INNER JOIN control_db.company_view_option_type ON company_view_option_type.company_view_option_type_id = company_view.company_view_option_type_id ");
        sql.append("LEFT JOIN control_db.company_view_options ON ");
        sql.append("    company_view_options.company_view_id = company_view.company_view_id AND ");
        sql.append("    company_view_options.company_id = ").append(this.company_id).append(" ");
        if (this.option_view_id >= 0) {
            sql.append("WHERE company_view.company_view_id = " + this.option_view_id + " ");
        }
        sql.append("ORDER BY company_view_option_type, company_view_key");
        return sql.toString();
    }

}
