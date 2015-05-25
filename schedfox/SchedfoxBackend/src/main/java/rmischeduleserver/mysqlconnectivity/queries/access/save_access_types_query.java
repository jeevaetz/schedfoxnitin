/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.access;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.AccessIndividualTypes;

/**
 *
 * @author ira
 */
public class save_access_types_query extends GeneralQueryFormat {

    private boolean isUpdate = true;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(AccessIndividualTypes type) {
        if (type.getAccessIndividualTypeId() == null || type.getAccessIndividualTypeId() == 0) {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{type.getAccessType()});
        } else {
            super.setPreparedStatement(new Object[]{type.getAccessType(), type.getAccessIndividualTypeId()});
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isUpdate) {
            sql.append("UPDATE access_individual_types ");
            sql.append("SET ");
            sql.append("access_type = ? ");
            sql.append("WHERE ");
            sql.append("access_individual_type_id = ?; ");
        } else {
            sql.append("INSERT INTO access_individual_types ");
            sql.append("(access_type) ");
            sql.append("VALUES ");
            sql.append("(?); ");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
