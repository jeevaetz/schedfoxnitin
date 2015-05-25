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
public class get_client_contacts_query extends GeneralQueryFormat {
    
    public get_client_contacts_query() {
        
    }
    
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
        sql.append("SELECT ");
        sql.append("client_contact_id as id, client_id, client_contact_type, client_contact_phone as phone, ");
        sql.append("client_contact_cell as cell, client_contact_email as email, client_contact_fax as fax, ");
        sql.append("client_contact_is_primary as isprimary, client_contact_pager, client_contact_first_name  as fname, ");
        sql.append("client_contact_last_name as lname, client_contact_middle_name as mname, client_contact_type_id as type, ");
        sql.append("client_contact_title as title, client_contact_address as address1, client_contact_address2 as address2, ");
        sql.append("client_contact_city as city, client_contact_state as state, client_contact_zip as zip, ");
        sql.append("client_contact_email_on_login, client_contact_email_on_incident, client_contact_include_mass_email, ");
        sql.append("client_contact_include_daily_odr, client_contact_include_daily_tracking ");
        sql.append("FROM client_contact ");
        sql.append("WHERE ");
        sql.append("client_id = ? AND (? = true OR (? = false AND client_contact_is_deleted = 0)) ");
        sql.append("ORDER BY client_contact_first_name, client_contact_last_name ");
        return sql.toString();
    }
    
}
