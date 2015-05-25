/*
 * save_client_contact_query.java
 *
 * Created on August 4, 2006, 11:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import schedfoxlib.model.ClientContact;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author shawn
 */
public class save_client_contact_query extends GeneralQueryFormat {

    private ClientContact contact;
    private boolean update;
    
    /** Creates a new instance of save_client_contact_query */
    public save_client_contact_query(ClientContact contact, boolean update) {
        this.contact = contact;
        this.update = update;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (this.update) {
            sql.append("UPDATE client_contact ");
            sql.append("SET ");
            sql.append("client_id = ?, client_contact_type = '0', client_contact_is_primary = ?, client_contact_type_id = ?, ");
            sql.append("client_contact_title = ?, client_contact_first_name = ? , client_contact_last_name = ?, ");
            sql.append("client_contact_middle_name = ?, client_contact_address = ?, client_contact_address2 = ?, ");
            sql.append("client_contact_city = ?, client_contact_state = ?, client_contact_zip = ?, client_contact_phone = ?, ");
            sql.append("client_contact_cell = ?, client_contact_fax = ?, client_contact_email = ?, client_contact_is_deleted = ?, ");
            sql.append("client_contact_pager = ?, client_contact_email_on_login = ?, client_contact_email_on_incident = ?,  ");
            sql.append("client_contact_include_mass_email = ?, client_contact_include_daily_odr = ?, client_contact_include_daily_tracking = ? ");
            sql.append("WHERE ");
            sql.append("client_contact_id = ? ");

            super.setPreparedStatement(new Object[] {contact.getClientId(), contact.getClientContactIsPrimary(), contact.getClientContactType(),
                contact.getClientContactTitle(), contact.getClientContactFirstName(), contact.getClientContactLastName(),
                contact.getClientContactMiddleName(), contact.getClientContactAddress(), contact.getClientContactAddress2(),
                contact.getClientContactCity(), contact.getClientContactState(), contact.getClientContactZip(), contact.getClientContactPhone(),
                contact.getClientContactCell(), contact.getClientContactFax(), contact.getClientContactEmail(), contact.getClientContactIsDeleted(),
                contact.getClientContactPager(), contact.getClientContactEmailOnLogin(), contact.getClientContactEmailOnIncident(), 
                contact.getClientContactIncludeMassEmail(), contact.getClientContactIncludeDailyOdr(), contact.getClientContactIncludeDailyTracking(),
                contact.getClientContactId()});
        } else {
            sql.append("INSERT INTO client_contact ");
            sql.append("( ");
            sql.append(" client_contact_id, client_id, client_contact_type_id, client_contact_is_primary, ");
            sql.append(" client_contact_title, client_contact_first_name, client_contact_last_name, ");
            sql.append(" client_contact_middle_name, client_contact_address, client_contact_address2, ");
            sql.append(" client_contact_city, client_contact_state, client_contact_zip, client_contact_phone, ");
            sql.append(" client_contact_cell, client_contact_fax, client_contact_email, client_contact_is_deleted, ");
            sql.append(" client_contact_pager, client_contact_email_on_login, client_contact_email_on_incident, ");
            sql.append(" client_contact_include_mass_email, client_contact_include_daily_odr, client_contact_include_daily_tracking ");
            sql.append(") ");
            sql.append("VALUES ");
            sql.append("( ");
            sql.append(" ?, ?, ?, ?, ?, ");
            sql.append(" ?, ?, ?, ?, ?, ");
            sql.append(" ?, ?, ?, ?, ?, ");
            sql.append(" ?, ?, ?, ?, ?, ");
            sql.append(" ?, ?, ?, ?");
            sql.append(")");


            super.setPreparedStatement(new Object[] {contact.getClientContactId(), contact.getClientId(), contact.getClientContactType(), contact.getClientContactIsPrimary(),
                contact.getClientContactTitle(), contact.getClientContactFirstName(), contact.getClientContactLastName(),
                contact.getClientContactMiddleName(), contact.getClientContactAddress(), contact.getClientContactAddress2(),
                contact.getClientContactCity(), contact.getClientContactState(), contact.getClientContactZip(), contact.getClientContactPhone(),
                contact.getClientContactCell(), contact.getClientContactFax(), contact.getClientContactEmail(), contact.getClientContactIsDeleted(),
                contact.getClientContactPager(), contact.getClientContactEmailOnLogin(), contact.getClientContactEmailOnIncident(),
                contact.getClientContactIncludeMassEmail(), contact.getClientContactIncludeDailyOdr(), contact.getClientContactIncludeDailyTracking()
                });
        }

        return sql.toString();
    }

    public boolean hasAccess() { return true; }
    
}
