/*
 * get_branch_by_company_query.java
 *
 * Created on June 6, 2006, 12:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author shawn
 */
public class get_branch_by_company_query extends GeneralQueryFormat {
    
    private String companyId;
    
    /** Creates a new instance of get_branch_by_company_query */
    public get_branch_by_company_query(String companyId) {
        this.companyId = companyId;
    }
    
    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT branch.branch_id, branch_name, branch_management_id, timezone, report_time ");
        sql.append("FROM ").append(getManagementSchema()).append(".branch ");
        sql.append("LEFT JOIN ").append(getManagementSchema()).append(".management_clients ON management_clients.management_id = branch.branch_management_id ");
        sql.append("WHERE ");
        sql.append("branch_management_id = ");
        sql.append("    (SELECT company_management_id FROM ");
        sql.append("     ").append(getManagementSchema()).append(".company ");
        sql.append("     ").append("WHERE ");
        sql.append("     ").append("company_id = ").append(companyId).append(" LIMIT 1) ");
        sql.append("ORDER BY branch_name; ");
        return sql.toString();
    }
    
    @Override
    public boolean hasAccess() {
        return false;
    }
    
}
