/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.user;

import schedfoxlib.model.UserCommissionCaps;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_user_commission_cap_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append(super.getManagementSchema()).append(".user_commission_caps ");
            sql.append("(user_id, yearly_cap, year_number) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append(super.getManagementSchema()).append(".user_commission_caps ");
            sql.append("SET ");
            sql.append("user_id = ?, yearly_cap = ?, year_number = ? ");
            sql.append("WHERE ");
            sql.append("user_commission_cap_id = ?; ");
        }
        return sql.toString();
    }

    public void upate(UserCommissionCaps caps) {
        if (caps.getUserCommissionCapId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[] {
                caps.getUserId(), caps.getYearlyCaps(), caps.getYearNumber()
            });
        } else {
            isInsert = false;
            super.setPreparedStatement(new Object[] {
                caps.getUserId(), caps.getYearlyCaps(), caps.getYearNumber(),
                caps.getUserCommissionCapId()
            });
        }
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
