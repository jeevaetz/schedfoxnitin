/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.mobileform;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class save_mobile_form_to_clients_query extends GeneralQueryFormat {

    private int clientIdSize;

    public void update(Integer mobileFormId, ArrayList<Integer> clientIds) {
        Object[] data = new Object[clientIds.size() * 2 + 1];
        data[0] = mobileFormId;
        for (int c = 0; c < clientIds.size(); c++) {
            data[c * 2 + 1] = mobileFormId;
            data[(c * 2) + 2] = clientIds.get(c);
        }
        clientIdSize = clientIds.size();
        super.setPreparedStatement(data);
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM mobile_forms_to_client WHERE mobile_forms_id = ?;");
        for (int c = 0; c < clientIdSize; c++) {
            sql.append("INSERT INTO ");
            sql.append("mobile_forms_to_client ");
            sql.append("(mobile_forms_id, client_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?); ");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
