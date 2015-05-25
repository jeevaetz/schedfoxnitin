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
public class save_dynamic_field_query extends GeneralQueryFormat {

    private int id;
    private int location_id;
    private int type_id;
    private String name;
    private String defaultValue;
    private boolean isRequired;
    private boolean isActive;
    private boolean showInClientLogin;
    private boolean showInEmployeeLogin;
    
    public void update(int id, int location_id, int type_id, String name, String defaultValue, 
            boolean isRequired, boolean isActive, boolean showInClientLogin, boolean showInEmpLogin) {
        this.id = id;
        this.location_id = location_id;
        this.type_id = type_id;
        this.name = name.replaceAll("'", "");
        this.defaultValue = defaultValue.replaceAll("'", "");;
        this.isRequired = isRequired;
        this.isActive = isActive;
        this.showInClientLogin = showInClientLogin;
        this.showInEmployeeLogin = showInEmpLogin;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        String isRequiredVal = "true";
        if (!isRequired) {
            isRequiredVal = "false";
        }
        String isActiveVal = "true";
        if (!isActive) {
            isActiveVal = "false";
        }
        String showInClientLoginStr = "true";
        String showInEmployeeLoginStr = "true";
        if (!showInClientLogin) {
            showInClientLoginStr = "false";
        }
        if (!showInEmployeeLogin) {
            showInEmployeeLoginStr = "false";
        }
        if (id == 0) {
            sql.append("INSERT INTO ");
            sql.append("dynamic_field_def ");
            sql.append("(dynamic_field_def_name, dynamic_field_type_id, dynamic_field_location_id, ");
            sql.append(" dynamic_field_def_default, is_required, is_active, display_in_client, display_in_employee) ");
            sql.append(" VALUES ");
            sql.append("('" + name + "'," + type_id + "," + location_id + ",'" + defaultValue + "', " + isRequiredVal + ", ");
            sql.append(isActiveVal + "," + showInClientLoginStr + "," + showInEmployeeLoginStr + ");");
        } else {
            sql.append("UPDATE ");
            sql.append("dynamic_field_def ");
            sql.append("SET ");
            sql.append("dynamic_field_def_name = '" + name + "', ");
            sql.append("dynamic_field_type_id = " + type_id + ", ");
            sql.append("dynamic_field_location_id = " + location_id + ", ");
            sql.append("dynamic_field_def_default = '" + defaultValue + "', ");
            sql.append("is_required = " + isRequiredVal + ", ");
            sql.append("is_active = " + isActiveVal + ", ");
            sql.append("display_in_client = " + showInClientLoginStr + ", ");
            sql.append("display_in_employee = " + showInEmployeeLoginStr + " ");
            sql.append("WHERE ");
            sql.append("dynamic_field_def_id = " + id);
        }
        return sql.toString();
    }
}
