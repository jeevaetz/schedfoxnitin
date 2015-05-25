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
public class client_contact_query extends GeneralQueryFormat {
    
    /** Creates a new instance of client_contact_query */
    public client_contact_query(String clientID) {
        this.myReturnString =
               "SELECT client_contact_id          as id," +
                     " client_id, " +
                     " client_contact_type, " +
                     " client_contact_phone       as phone," +
                     " client_contact_cell        as cell," +
                     " client_contact_email       as email," +
                     " client_contact_fax         as fax," +
                     " client_contact_is_primary  as isprimary," +
                     " client_contact_pager, " +
                     " client_contact_first_name  as fname," +
                     " client_contact_last_name   as lname," +
                     " client_contact_middle_name as mname," +
                     " client_contact_type_id     as type," +
                     " client_contact_title       as title," +
                     " client_contact_address     as address1," +
                     " client_contact_address2    as address2," +
                     " client_contact_city        as city," +
                     " client_contact_state       as state," +
                     " client_contact_zip         as zip," +
                     " client_contact_email_on_login, " +
                     " client_contact_email_on_incident, " +
                     " client_contact_include_mass_email, " +
                     " client_contact_include_daily_odr, " +
                     " client_contact_include_daily_tracking " +
              " FROM client_contact" +
              " WHERE client_id = " + clientID + " " +
                "ORDER BY client_contact_first_name, client_contact_last_name";
    }
    
    public boolean hasAccess() { return true; }
    
}
