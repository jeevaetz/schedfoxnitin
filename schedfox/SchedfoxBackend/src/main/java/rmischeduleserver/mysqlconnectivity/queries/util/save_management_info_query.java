/*
 * save_management_info_query.java
 *
 * Created on April 28, 2005, 7:50 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import schedfoxlib.model.ManagementClient;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class save_management_info_query extends GeneralQueryFormat {
    
    private boolean isInsert;
    
    /** Creates a new instance of save_management_info_query */
    public save_management_info_query() {
        myReturnString = new String();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(ManagementClient client) {
        if (client.getManagement_id() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{
                client.getManagement_client_name(), client.getManagement_client_address(), client.getManagement_client_address2(),
                client.getManagement_client_city(), client.getManagement_client_state(), client.getManagement_client_zip(),
                client.getManagement_client_phone(), client.getManagement_client_email(), client.isManagement_is_deleted(),
                client.getAmount_to_bill(), client.getBill_interval(), client.getBill_start_date(), client.getAmount_per_employee(),
                client.getManagement_billing_email1(), client.getManagement_billing_email2()
            });
        } else {
            isInsert = false;
            super.setPreparedStatement(new Object[]{
                client.getManagement_client_name(), client.getManagement_client_address(), client.getManagement_client_address2(),
                client.getManagement_client_city(), client.getManagement_client_state(), client.getManagement_client_zip(),
                client.getManagement_client_phone(), client.getManagement_client_email(), client.isManagement_is_deleted(),
                client.getAmount_to_bill(), client.getBill_interval(), client.getBill_start_date(), client.getAmount_per_employee(), 
                client.getManagement_billing_email1(), client.getManagement_billing_email2(),
                client.getManagement_id()
            });
        }
    }
    
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO ").append(getManagementSchema()).append(".management_clients ");
            sql.append("(");
            sql.append(" management_client_name, management_client_address, management_client_address2, ");
            sql.append(" management_client_city, management_client_state, management_client_zip, ");
            sql.append(" management_client_phone, management_client_email, management_is_deleted, ");
            sql.append(" amount_to_bill, bill_interval, bill_start_date, amount_per_employee, ");
            sql.append(" management_billing_email1, management_billing_email2 ");
            sql.append(") VALUES (");
            sql.append("    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?::interval, ?, ?, ?, ?");
            sql.append(");");
        } else {
            sql.append("UPDATE ").append(getManagementSchema()).append(".management_clients ");
            sql.append("SET ");
            sql.append(" management_client_name = ?, management_client_address = ?, management_client_address2 = ?, ");
            sql.append(" management_client_city = ?, management_client_state = ?, management_client_zip = ?, ");
            sql.append(" management_client_phone = ?, management_client_email = ?, management_is_deleted = ?, ");
            sql.append(" amount_to_bill = ?, bill_interval = ?::interval, bill_start_date = ?, amount_per_employee = ?, ");
            sql.append(" management_billing_email1 = ?, management_billing_email2 = ? ");
            sql.append("WHERE ");
            sql.append("management_id = ?;");
        }
        return sql.toString();
    }
    
}
