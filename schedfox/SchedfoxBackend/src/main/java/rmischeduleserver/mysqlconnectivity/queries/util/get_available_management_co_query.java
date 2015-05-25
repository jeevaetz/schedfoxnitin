/*
 * get_available_management_co_query.java
 *
 * Created on April 26, 2005, 2:25 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_available_management_co_query extends GeneralQueryFormat {
    
    private String manage;
    private boolean showdel;
    private boolean showSchedfoxCust=false;

    /** Creates a new instance of get_available_management_co_query */
    public get_available_management_co_query() {
        myReturnString = new String();
        manage = null;
        showdel = true;
    }
    
    public void update() {
        
    }
    
    public void update(boolean showDeleted) {
        showdel = showDeleted;
    }
    
        public void update(String manageId) {
        manage = manageId;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void showSchedfoxCust(boolean sw){
        showSchedfoxCust=sw;
    }
    public String toString() {
        String manage1 = "";
        if (manage != null) {            
            manage1 = " WHERE management_id = " + manage;
        } else {
            manage1 = "";
        }
        String deleted = "";
        if (!showdel) {
            deleted = " WHERE management_is_deleted = FALSE ";
        }
        String schedfoxCustomer="";
        if(showSchedfoxCust)
            schedfoxCustomer=" inner join control_db.schedfox_customer on customer_id=management_id ";
        return "SELECT management_id, management_client_name, management_client_address as address, " +
                "management_client_address2 as address2, management_client_city as city, management_client_state " +
                "as state, management_client_zip as zip, management_client_phone as phone, management_client_email " +
                "as email, management_is_deleted as isdeleted,management_date_started as started," +
                "amount_to_bill, bill_interval, bill_start_date, amount_per_employee,  " +
                "management_billing_email1, management_billing_email2 " +
                "FROM " + getManagementSchema() + ".management_clients " +
                "" + schedfoxCustomer + manage1 + deleted + " Order by management_client_name";
    }
    
}
