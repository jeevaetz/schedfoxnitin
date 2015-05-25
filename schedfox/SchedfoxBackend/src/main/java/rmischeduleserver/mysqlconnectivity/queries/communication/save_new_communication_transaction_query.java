/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.communication;

import schedfoxlib.model.CommunicationTransaction;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_new_communication_transaction_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(CommunicationTransaction source) {
        super.setPreparedStatement(new Object[]{source.getCommunicationSourceId(),
            source.getData(), source.getAssociatedUserId(), source.getAssociatedUserType()});
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append("communication_transaction ");
        sql.append("(communication_source_id, data, associated_user_id, associated_user_type) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ? , ?)");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}

