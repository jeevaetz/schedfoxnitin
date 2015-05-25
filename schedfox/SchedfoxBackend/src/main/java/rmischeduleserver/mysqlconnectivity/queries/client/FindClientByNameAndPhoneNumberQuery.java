/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class FindClientByNameAndPhoneNumberQuery extends GeneralQueryFormat {

    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT client_id FROM client ");
        sb.append("WHERE ");
        sb.append("regexp_replace(client_phone, '[^0-9]*' ,'', 'g') = regexp_replace(?, '[^0-9]*' ,'', 'g') AND ");
        sb.append("UPPER(client_name) = UPPER(?);");
        return sb.toString();
    }
}
