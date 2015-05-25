/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.util;

import schedfoxlib.model.ClientContactType;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_client_contact_type_query extends GeneralQueryFormat {

    private ClientContactType clientType;
    private boolean update;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void setObject(ClientContactType clientType, boolean update) {
        this.clientType = clientType;
        this.update = update;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!update) {
            sql.append("INSERT INTO ");
            sql.append("client_contact_type ");
            sql.append("(client_contact_type, contact_type)");
            sql.append("VALUES ");
            sql.append("(?, ?);");

            super.setPreparedStatement(new Object[]{this.clientType.getClientContactType(), this.clientType.getContactType()});
        } else {
            sql.append("UPDATE ");
            sql.append("client_contact_type ");
            sql.append("SET contact_type = ? ");
            sql.append("WHERE ");
            sql.append("client_contact_type = ? ");

            super.setPreparedStatement(new Object[]{this.clientType.getContactType(), this.clientType.getClientContactType()});
        }
        
        return sql.toString();
    }

}
