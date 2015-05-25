/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.importing;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class dynamicValCount extends GeneralQueryFormat {

    int id;
    String query = "select count(*) as num from dynamic_field_value where key_for_value =";

    public dynamicValCount(int id) {
        this.id = id;
    }

    public String toString() {
        StringBuilder query = new StringBuilder("select count(*) as num from dynamic_field_value where key_for_value =");
        query.append(id);
        return query.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
}
