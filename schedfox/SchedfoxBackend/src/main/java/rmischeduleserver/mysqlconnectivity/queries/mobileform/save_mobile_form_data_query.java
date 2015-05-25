/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.mobileform;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.MobileFormData;

/**
 *
 * @author ira
 */
public class save_mobile_form_data_query extends GeneralQueryFormat {

    private boolean isUpdate;

    public void update(MobileFormData data, boolean isUpdate) {
        this.isUpdate = isUpdate;
        super.setPreparedStatement(new Object[]{
            data.getMobileFormsId(), data.getDataLabel(), data.getDateType(), data.getActive(), data.getOrdering(), data.getDefaultValue(), data.getShowInSummary(), data.getMobileFormDataId()
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
            sql.append("mobile_form_data ");
            sql.append("(mobile_forms_id, data_label, date_type, active, ordering, default_value, show_in_summary, mobile_form_data_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?)");
        } else {
            sql.append("UPDATE ");
            sql.append("mobile_form_data ");
            sql.append("SET ");
            sql.append("mobile_forms_id = ?, data_label = ?, date_type = ?, active = ?, ordering = ?, default_value = ?, show_in_summary = ? ");
            sql.append("WHERE ");
            sql.append("mobile_form_data_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
