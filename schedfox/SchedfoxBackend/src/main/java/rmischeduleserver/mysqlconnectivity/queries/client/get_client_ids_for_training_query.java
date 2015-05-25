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
public class get_client_ids_for_training_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM usked_client ");
        sql.append("INNER JOIN ");
        sql.append("( ");
        sql.append("SELECT DISTINCT cid FROM assemble_schedule(DATE(NOW() - interval '2 year'), DATE(NOW() + interval '1 week'), -1, ?) ");
        sql.append(")) as client_join ON client_join.cid = usked_client.client_id ");
        return sql.toString();
    }

}
