/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.access;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.AccessIndividuals;

/**
 *
 * @author ira
 */
public class save_access_individual_query extends GeneralQueryFormat {

    private boolean isUpdate = false;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(AccessIndividuals individuals, boolean isUpdate) {
        this.isUpdate = isUpdate;
        super.setPreparedStatement(new Object[]{
            individuals.getAccessIndividualTypeId(), individuals.getFirstName(), individuals.getLastName(), 
            individuals.getActive(), individuals.getStartDateAccess(), individuals.getEndDateAccess(),
            individuals.getTagWritten(), individuals.getClientId(),
            individuals.getAccessIndividualId()
        });
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!isUpdate) {
            sql.append("INSERT INTO ");
            sql.append("access_individuals ");
            sql.append("(access_individual_type_id, first_name, last_name, active, start_date_access, end_date_access, tag_written, client_id, access_individual_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        } else {
            sql.append("UPDATE ");
            sql.append("access_individuals ");
            sql.append("SET ");
            sql.append("access_individual_type_id = ?, first_name = ?, last_name = ?, active = ?, start_date_access = ?, end_date_access = ?, tag_written = ?, client_id = ? ");
            sql.append("WHERE ");
            sql.append("access_individual_id = ? ");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
