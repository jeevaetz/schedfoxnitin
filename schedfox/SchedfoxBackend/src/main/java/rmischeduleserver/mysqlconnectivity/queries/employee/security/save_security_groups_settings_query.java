/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee.security;

import java.util.Hashtable;
import java.util.Iterator;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_security_groups_settings_query extends GeneralQueryFormat {

    private int security_group_id;
    private Hashtable<Integer, Integer> values;

    public save_security_groups_settings_query() {
        values = new Hashtable<Integer, Integer>();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(int sec_group, Hashtable<Integer, Integer> vals) {
        this.security_group_id = sec_group;
        this.values = vals;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM employee_security_group_settings;");
        Iterator<Integer> keys = values.keySet().iterator();
        while (keys.hasNext()) {
            int key = keys.next();
            sql.append("INSERT INTO employee_security_group_settings ");
            sql.append("(employee_security_group_id, security_setting) ");
            sql.append("VALUES ");
            sql.append("(" + security_group_id + ", " + (key + values.get(key)) + ");");
        }

        return sql.toString();
    }

}
