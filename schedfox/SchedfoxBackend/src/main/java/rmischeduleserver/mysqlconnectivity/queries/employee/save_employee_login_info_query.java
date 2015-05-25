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
public class save_employee_login_info_query extends GeneralQueryFormat {

    private int employeeid;
    private String originalLogin;
    private String newLogin;
    private String originalPass;
    private String newPass;
    private boolean allowLogin;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(int employeeid, String originalLogin, String newLogin, String originalPass, String newPass, boolean allowLogin) {
        this.employeeid = employeeid;
        this.originalLogin = originalLogin;
        this.newLogin = newLogin;
        this.originalPass = originalPass;
        this.newPass = newPass;
        this.allowLogin = allowLogin;
    }

    public void newEmp(String originalLogin, String newLogin, String originalPass, String newPass, boolean allowLogin) {
        this.originalLogin = originalLogin;
        this.newLogin = newLogin;
        this.originalPass = originalPass;
        this.newPass = newPass;
        this.allowLogin = allowLogin;
    }

    @Override
    public String toString() {
        /*
        StringBuffer sql = new StringBuffer();

        String allowVal = "false";
        if (allowLogin) {
            allowVal = "true";
        }

        sql.append("UPDATE employee ");
        sql.append("SET ");
        sql.append("is_login_available = " + allowVal + " ");
        if (!originalLogin.equals(newLogin)) {
            sql.append(", employee_login = '" + newLogin + "' ");
        }
        if (!originalPass.equals(newPass)) {
            sql.append(", employee_password = '" + newPass + "' ");
        }
        sql.append("WHERE ");
        if (this.employeeid == 0) {
            sql.append("employee_id = (CASE WHEN (SELECT (MAX(employee_id) + 1) From employee) IS NULL THEN 1 ELSE (SELECT (MAX(employee_id) + 1) From employee) END)");
        } else {
            sql.append("employee_id = " + employeeid);
        }
        return sql.toString();*/
        return "SELECT f_save_employee_login_info_query(" + employeeid + ",'" + originalLogin +
                    "','" + newLogin + "','" + originalPass + "','" + newPass + "'," + allowLogin + ");";
    }

}
