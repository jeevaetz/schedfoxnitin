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
public class get_dynamic_field_values_for_key_query extends GeneralQueryFormat {

    private int key;
    private int location;
    private boolean getForClientLogin;
    private boolean getForEmployeeLogin;

    public get_dynamic_field_values_for_key_query() {
        
    }

    public void update(int key, int location, boolean getForClientLogin, boolean getForEmployeeLogin) {
        this.key = key;
        this.location = location;
        this.getForClientLogin = getForClientLogin;
        this.getForEmployeeLogin = getForEmployeeLogin;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT ON (dynamic_field_def_name, key_for_value) ");
        sql.append("dynamic_field_def.dynamic_field_def_id, dynamic_field_def_name, dynamic_field_type_id, ");
        sql.append("dynamic_field_value_id, dynamic_field_value, last_user_changed, last_updated, ");
        sql.append("key_for_value, dynamic_field_def_name, dynamic_field_type_id, is_required, dynamic_field_def_default ");
        sql.append("FROM dynamic_field_def ");
        sql.append("LEFT JOIN dynamic_field_value ON ");
        sql.append("    dynamic_field_value.dynamic_field_def_id = dynamic_field_def.dynamic_field_def_id ");
        if (key != 0) {
            sql.append("    AND dynamic_field_value.key_for_value = " + key + " ");
        }
        sql.append("WHERE ");
        sql.append("dynamic_field_def.is_active = true ");
        if (this.getForClientLogin) {
            sql.append("AND dynamic_field_def.display_in_client = true ");
        } else if (this.getForEmployeeLogin) {
            sql.append("AND dynamic_field_def.display_in_employee = true ");
        }
        if (location != -1) {
            sql.append("AND dynamic_field_location_id = " + location + " ");
        }
        sql.append("ORDER BY dynamic_field_def_name ");
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
}
