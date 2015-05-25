/*
 * client_info_query.java
 *
 * Created on January 25, 2005, 2:43 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.*;
/**
 *
 * @author ira
 */
public class client_info_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            " Select " +
            " client.client_id          as id, " +
            " client.client_name        as name, " +
            " client.client_phone       as phone, " +
            " client.client_phone2      as phone2, " +
            " client.client_fax         as fax, " +
            " client.client_address     as address, " +
            " client.client_address2    as address2, " +
            " client.client_city        as city, " +
            " client.client_state       as state, " +
            " client.client_zip         as zip, " +
            " client.client_worksite    as ws, " +
            " (CASE WHEN client.client_is_deleted=1 THEN 'true' ELSE 'false' END)  as deleted, " +
            " client.management_id      as management_id, " +
            " client.client_start_date  as client_start_date, " +
            " client.client_end_date    as client_end_date, " +
            " client.client_training_time     as ctrainingtime, " +
            " client.client_bill_for_training as cbilltrain, " +
            " client.client_worksite_order    as wsorder, " +
            " client.rate_code_id        as rate_code_id, " +
            " client.default_non_billable, " +
            " client.store_volume_id, " +
            " client.check_out_option_id, " +
            " client.checkin_from_post_phone, " +
            " client.display_client_in_call_queue, " +
            " (to_char(default_contract_renewal, 'MM')::integer + (to_char(default_contract_renewal, 'YYYY')::integer * 12)) as num_months, " +
            " client.store_remote_market_id "
    );
    
    private String CompanyId;
    
    /** Creates a new instance of client_info_query */
    public client_info_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String companyId) {
        CompanyId = companyId;
    }
    
    public String toString() {
        return  MY_QUERY + 
                "From client " +
                "LEFT JOIN client_contact ON client.client_id = client_contact.client_id " +
                "Where " +
                " client.branch_id = " + getBranch() + " AND " +
                " client.client_id = " + CompanyId + " " +
                "Order By client.client_name ";
    }
    
}
