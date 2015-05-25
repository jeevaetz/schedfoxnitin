/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import schedfoxlib.model.ClientRating;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_client_rating_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(ClientRating rating) {
        super.setPreparedStatement(new Object[]{rating.getClientId(),
            rating.getUserId(), rating.getCustomer_rating()});
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append("client_ratings ");
        sql.append("(client_id, user_id, customer_rating) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?)");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
