/*
 * save_user_info_query.java
 *
 * Created on April 19, 2005, 9:15 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.User;
/**
 *
 * @author ira
 */
public class save_user_info_query extends GeneralQueryFormat {
    
    private Boolean isInsert;
    private Boolean isMaskedPW;
    
    public save_user_info_query() {

    }
    
    public void update(User user, boolean isMaskedPW) {
        this.isMaskedPW = isMaskedPW;
        isInsert = false;
        if (user.getUserId() == null || user.getUserId().equals(0)) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{
                user.getUserFirstName(), user.getUserMiddleInitial(), user.getUserLastName(), user.getUserEmail(),
                user.getUserMd5(), user.getUserLogin(), user.getUserPassword(), user.getUserManagementId(),
                user.getUserIsDeleted(), user.getCanViewSsn()
            });
        } else {
            super.setPreparedStatement(new Object[]{
                user.getUserFirstName(), user.getUserMiddleInitial(), user.getUserLastName(), user.getUserEmail(),
                user.getUserMd5(), user.getUserLogin(), user.getUserPassword(), user.getUserManagementId(),
                user.getUserIsDeleted(), user.getCanViewSsn(), user.getUserId()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        String md5Param = "md5(?)";
        if (this.isMaskedPW) {
            md5Param = "?";
        }
        
        if(!isInsert) {
            sql.append("UPDATE ");
            sql.append(getManagementSchema()).append(".user ");
            sql.append("SET ");
            sql.append("user_first_name = ?, user_middle_initial = ?, user_last_name = ? , user_email = ?, user_md5 = md5(?), ");
            sql.append("user_login = ?, user_password =  ").append(md5Param).append(", user_management_id = ?, user_is_deleted = ?, can_view_ssn = ? ");
            sql.append("WHERE ");
            sql.append("user_id = ?; ");
        } else {
            sql.append("INSERT INTO ");
            sql.append(getManagementSchema()).append(".user ");
            sql.append("(");
            sql.append("user_first_name, user_middle_initial, user_last_name, user_email, user_md5, ");
            sql.append("user_login, user_password, user_management_id, user_is_deleted, can_view_ssn ");
            sql.append(") ");
            sql.append("VALUES ");
            sql.append("(");
            sql.append("?, ?, ?, ?, md5(?), ?, ").append(md5Param).append(", ?, ?, ? ");
            sql.append(");");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
}
