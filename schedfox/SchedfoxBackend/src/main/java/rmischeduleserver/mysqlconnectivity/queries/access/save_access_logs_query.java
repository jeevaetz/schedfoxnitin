/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.access;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.AccessIndividualLogs;

/**
 *
 * @author ira
 */
public class save_access_logs_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(AccessIndividualLogs log) {
        super.setPreparedStatement(log.getAccessIndividualId(), log.getScannedBy(),
                log.getScannedOn(), log.getScannedType());
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO access_individual_logs ");
        sql.append("(access_individual_id, scanned_by, scanned_on, scanned_type) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?, ?); ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
