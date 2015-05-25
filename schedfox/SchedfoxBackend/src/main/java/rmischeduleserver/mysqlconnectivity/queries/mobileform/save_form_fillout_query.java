/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.mobileform;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.MobileFormFillout;

/**
 *
 * @author ira
 */
public class save_form_fillout_query extends GeneralQueryFormat {

    private boolean isUpdate;

    public void update(MobileFormFillout fillout, boolean isUpdate) {
        this.isUpdate = isUpdate;
        java.sql.Timestamp notificationSent = null;
        try {
            notificationSent = new java.sql.Timestamp(fillout.getNotificationSent().getTime());
        } catch (Exception exe) {}
        super.setPreparedStatement(new Object[]{
            fillout.getEmployeeId(), fillout.getMobileFormId(), fillout.getClientId(), fillout.getActive(), notificationSent, fillout.getMobileFormFilloutId()
        });
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isUpdate == false) {
            sql.append("INSERT INTO ");
            sql.append("mobile_form_fillout ");
            sql.append("(employee_id, mobile_form_id, client_id, active, notification_sent, mobile_form_fillout_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?)");
        } else {
            sql.append("UPDATE ");
            sql.append("mobile_form_fillout ");
            sql.append("SET ");
            sql.append("employee_id = ?, mobile_form_id = ?, client_id = ?, active = ?, notification_sent = ? ");
            sql.append("WHERE ");
            sql.append("mobile_form_fillout_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
