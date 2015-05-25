/*
 * save_branch_information_query.java
 *
 * Created on April 28, 2005, 11:21 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.BranchInfo;
/**
 *
 * @author ira
 */
public class save_branch_information_query extends GeneralQueryFormat {
    
    /** Creates a new instance of save_branch_information_query */
    public save_branch_information_query() {
        
    }
    
    public void update(BranchInfo branchInfo) {
        super.setPreparedStatement(new Object[]{
            branchInfo.getBranchId(), branchInfo.getBranchId(), branchInfo.getAddress(),
            branchInfo.getAddress2(), branchInfo.getCity(), branchInfo.getState(), branchInfo.getZip(),
            branchInfo.getPhone(), branchInfo.getContactName(), branchInfo.getContactPhone(), 
            branchInfo.getContactEmail()
        });
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(getManagementSchema()).append(".branch_info ");
        sql.append("WHERE ");
        sql.append("branch_id = ?;");
        sql.append("INSERT INTO ").append(getManagementSchema()).append(".branch_info ");
        sql.append("(branch_id, address, address2, city, state, zip, phone, contact_name, contact_phone, contact_email) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    
}
