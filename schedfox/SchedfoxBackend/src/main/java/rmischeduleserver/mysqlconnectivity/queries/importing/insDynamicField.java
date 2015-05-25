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
public class insDynamicField extends GeneralQueryFormat {

    /*
     *   dynamic_field_value_id integer NOT NULL DEFAULT nextval('champion_db.dynamic_field_value_seq'::regclass),
    dynamic_field_value text NOT NULL,
    dynamic_field_def_id integer NOT NULL,
    last_user_changed integer NOT NULL,
    last_updated date NOT NULL DEFAULT now(),
    key_for_value integer,
     */
    String dynamic_field_value;
    int dynamic_field_def_id;
    int last_user_changed;
    int key_for_value;//employeeId

    public insDynamicField(String val, int dynamic_field_def_id, int last_user_changed, int id) {
        this.dynamic_field_value = val;
        this.dynamic_field_def_id = dynamic_field_def_id;
        this.last_user_changed = last_user_changed;
        this.key_for_value = id;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO dynamic_field_value ");
        sb.append("(dynamic_field_value, dynamic_field_def_id, last_user_changed, key_for_value) values" );
        sb.append("('" + this.dynamic_field_value + "','" + this.dynamic_field_def_id + "','"
                + this.last_user_changed + "','" + this.key_for_value + "')");
        System.out.println(sb.toString());
        return sb.toString();
    }
}
