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
public class update_employee_phn_by_type_query extends GeneralQueryFormat {

    /*
    public enum PhoneType {
        HOME(), CELL(), ALT;

        PhoneType() {

        }

    }
     *
     */

    private static int HOME = 1;
    private static int CELL = 2;
    private static int ALT  = 3;


    private int phoneType;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(String number, int empId, int phoneType) {
        this.phoneType = phoneType;
        super.setPreparedStatement(new Object[]{number, empId});
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE employee ");
        sql.append("SET ");
        if (this.phoneType == HOME) {
            sql.append("employee_phone = ? ");
        } else if (phoneType == CELL) {
            sql.append("employee_cell = ? ");
        } else if (phoneType == ALT) {
            sql.append("employee_phone2 = ? ");
        }
        sql.append("WHERE ");
        sql.append("employee_id = ?;");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
