/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.communication;

import schedfoxlib.model.CommunicationSource;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_new_communication_source_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(CommunicationSource source) {
        super.setPreparedStatement(new Object[]{source.getCommunicationSourceId(),
            source.getCommunicationTypeId(), source.getSource()});
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append("communication_source ");
        sql.append("(communication_source_id, communication_type_id, source) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?)");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
