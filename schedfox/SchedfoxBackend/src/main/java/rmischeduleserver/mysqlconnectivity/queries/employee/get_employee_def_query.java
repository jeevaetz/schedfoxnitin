/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_employee_def_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("dynamic_field_value_id, dynamic_field_value, dynamic_field_def_name  ");
        sql.append("FROM ");
        sql.append("dynamic_field_def ");
        sql.append("LEFT JOIN dynamic_field_value ON dynamic_field_value.dynamic_field_def_id = dynamic_field_def.dynamic_field_def_id ");
        return sql.toString();
    }

}
