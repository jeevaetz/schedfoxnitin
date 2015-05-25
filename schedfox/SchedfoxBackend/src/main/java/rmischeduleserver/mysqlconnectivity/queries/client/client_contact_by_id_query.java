/*
 * client_contact_query.java
 *
 * Created on August 3, 2006, 11:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author shawn
 */
public class client_contact_by_id_query extends GeneralQueryFormat {

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT client_contact.* ");
        sql.append("FROM ");
        sql.append("client_contact ");
        sql.append("WHERE ");
        sql.append("client_contact_id =?; ");
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

}
