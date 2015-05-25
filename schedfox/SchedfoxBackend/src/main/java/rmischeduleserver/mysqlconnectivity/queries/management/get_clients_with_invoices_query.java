/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.management;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_clients_with_invoices_query extends GeneralQueryFormat {
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append(this.getManagementSchema()).append(".management_clients ");
        sql.append("WHERE ");
        sql.append("bill_start_date IS NOT NULL AND ");
        sql.append("management_is_deleted != true AND ");
        sql.append(this.getManagementSchema()).append(".get_company_emp_count(DATE(NOW()), management_clients.management_id) > 5 ");
        sql.append("ORDER BY management_client_name ASC ");
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }


}
