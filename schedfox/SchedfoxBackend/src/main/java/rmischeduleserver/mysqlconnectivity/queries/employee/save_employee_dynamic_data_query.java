/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_employee_dynamic_data_query extends GeneralQueryFormat {

    private ArrayList<Object[]> data;

    public save_employee_dynamic_data_query() {
        data = new ArrayList<Object[]>();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void addDynamicData(int id, String value, int def_id, int keyValue, int user_changed) {
        Object[] row = {id, value, def_id, keyValue, user_changed};
        data.add(row);
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        for (int r = 0; r < data.size(); r++) {
            Object[] row = data.get(r);
            //if (((Integer)row[0]).equals(new Integer(0))) {
                sql.append("DELETE FROM dynamic_field_value WHERE dynamic_field_def_id = " + row[2] + " AND key_for_value = " + row[3] + ";");
                sql.append("INSERT INTO ");
                sql.append("dynamic_field_value ");
                sql.append("(dynamic_field_value, dynamic_field_def_id, key_for_value, last_user_changed) ");
                sql.append("VALUES ");
                sql.append("('" + row[1] + "', " + row[2] + ", " + row[3] + "," + row[4] + ");");
//            } else {
//                sql.append("UPDATE dynamic_field_value ");
//                sql.append("SET ");
//                sql.append("dynamic_field_value = '" + row[1] + "', ");
//                sql.append("dynamic_field_def_id = " + row[2] + ", ");
//                sql.append("key_for_value = " + row[3] + ", ");
//                sql.append("last_user_changed = " + row[4] + " ");
//                sql.append("WHERE ");
//                sql.append("dynamic_field_value_id = " + row[0] + ";");
//            }
        }
        return sql.toString();
    }

}
