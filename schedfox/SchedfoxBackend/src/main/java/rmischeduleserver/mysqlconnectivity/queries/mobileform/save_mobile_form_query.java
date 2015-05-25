/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.mobileform;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.MobileForms;

/**
 *
 * @author ira
 */
public class save_mobile_form_query extends GeneralQueryFormat {

    private boolean isUpdate;

    public void update(MobileForms form, boolean isUpdate) {
        this.isUpdate = isUpdate;
        super.setPreparedStatement(new Object[]{
            form.getMobileFormName(), form.getClientId(), form.getEntryType(), form.getActive(), form.getIsAutoEmail(), form.getReportData(), form.getDisplayOnDevice(), form.getOncePerDay(), form.getSendImmediately(), form.getMobileFormsId()
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
            sql.append("mobile_forms ");
            sql.append("(mobile_form_name, client_id, entry_type, active, is_auto_email, report_data, display_on_device, once_per_day, send_immediately, mobile_forms_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        } else {
            sql.append("UPDATE ");
            sql.append("mobile_forms ");
            sql.append("SET ");
            sql.append("mobile_form_name = ?, client_id = ?, entry_type = ?, active = ?, is_auto_email = ?, report_data = ?, display_on_device = ?, once_per_day = ?, send_immediately = ? ");
            sql.append("WHERE ");
            sql.append("mobile_forms_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
