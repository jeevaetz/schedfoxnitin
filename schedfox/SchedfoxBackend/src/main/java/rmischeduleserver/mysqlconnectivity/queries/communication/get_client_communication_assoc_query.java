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
public class get_client_communication_assoc_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(CommunicationSource source) {
        super.setPreparedStatement(new Object[]{source.getCommunicationSourceId()});
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT client.* ");
        sql.append("FROM ");
        sql.append("client ");
        sql.append("INNER JOIN communication_link_to_client ON communication_link_to_client.client_id = client.client_id ");
        sql.append("WHERE ");
        sql.append("communication_source_id = ?;");
        return sql.toString();
    }

}
