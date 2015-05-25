/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.mobileform;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.MobileFormData;
import schedfoxlib.model.MobileFormDataFillout;

/**
 *
 * @author ira
 */
public class save_mobile_form_data_fillout_query extends GeneralQueryFormat {

    private int size;
    
    public void update(ArrayList<MobileFormDataFillout> filloutData) {
        Object[] data = new Object[filloutData.size() * 6];
        for (int i = 0; i < filloutData.size() * 6; i+=6) {
            data[i] = filloutData.get(i / 6).getMobileFormFilloutId();
            data[i + 1] = filloutData.get(i / 6).getMobileFormDataId();
            data[i + 2] = filloutData.get(i / 6).getMobileFormFilloutId();
            data[i + 3] = filloutData.get(i / 6).getMobileFormDataId();
            data[i + 4] = filloutData.get(i / 6).getMobileData();
            data[i + 5] = filloutData.get(i / 6).getMobileDataBytes();
        }
        super.setPreparedStatement(data);
        size = filloutData.size();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        for (int s = 0; s < size; s++) {
            sql.append("DELETE FROM ");
            sql.append("mobile_form_data_fillout ");
            sql.append("WHERE ");
            sql.append("mobile_form_fillout_id = ? AND mobile_form_data_id = ?; ");
            sql.append("INSERT INTO ");
            sql.append("mobile_form_data_fillout ");
            sql.append("(mobile_form_fillout_id, mobile_form_data_id, mobile_data, mobile_data_bytes) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?);");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
