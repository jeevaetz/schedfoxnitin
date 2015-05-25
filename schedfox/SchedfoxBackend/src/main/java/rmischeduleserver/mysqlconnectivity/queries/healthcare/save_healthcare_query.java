/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.healthcare;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.HealthCareOption;

/**
 *
 * @author ira
 */
public class save_healthcare_query extends GeneralQueryFormat {

    private boolean isNew;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(HealthCareOption option) {
        if (option.getHealthcareOptionsId() == null) {
            isNew = true;
            super.setPreparedStatement(new Object[]{option.getName(), option.getActive()});
        } else {
            isNew = false;
            super.setPreparedStatement(new Object[]{option.getName(), option.getActive(), option.getHealthcareOptionsId()});
        }
        
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isNew) {
            sql.append("INSERT INTO ");
            sql.append("healthcare_options ");
            sql.append("(name, active) ");
            sql.append("VALUES ");
            sql.append("(?, ?);");
        }  else {
            sql.append("UPDATE healthcare_options ");
            sql.append("SET ");
            sql.append("name = ?, active = ? ");
            sql.append("WHERE ");
            sql.append("healthcare_options_id = ?;");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
