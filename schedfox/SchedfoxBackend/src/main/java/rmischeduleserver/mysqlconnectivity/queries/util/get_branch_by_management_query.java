/*
 * get_branch_by_management_query.java
 *
 * Created on April 28, 2005, 9:47 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_branch_by_management_query extends GeneralQueryFormat {
    
    private String manageID;
    
    /** Creates a new instance of get_branch_by_management_query */
    public get_branch_by_management_query() {
        myReturnString = new String();
    }
    
    public void update(String manageId) {
        manageID = manageId;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT branch.*, branch_info.* ");
        sql.append("FROM ").append(getManagementSchema()).append(".branch ");
        sql.append("LEFT JOIN ").append(getManagementSchema()).append(".management_clients ");
        sql.append("   ON management_clients.management_id = branch.branch_management_id ");
        sql.append("LEFT JOIN ").append(getManagementSchema()).append(".branch_info ");
        sql.append("   ON branch_info.branch_id = branch.branch_id ");
        if (!manageID.equals("0")) {
            sql.append(" WHERE branch_management_id = ").append(manageID);
        }
        return sql.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
