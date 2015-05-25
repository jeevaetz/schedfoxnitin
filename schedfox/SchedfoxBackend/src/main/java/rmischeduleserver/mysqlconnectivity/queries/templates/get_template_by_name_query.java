/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.templates;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_template_by_name_query extends GeneralQueryFormat {
    public get_template_by_name_query() {
        
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("templates ");
        sql.append("WHERE ");
        sql.append("template_name ilike ? ");
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
}

