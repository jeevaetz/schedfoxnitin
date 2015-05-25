/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.user;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.User;

/**
 *
 * @author ira
 */
public class save_user_query extends GeneralQueryFormat {

    private boolean isInsert;
    private boolean isClearPassword;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(User user, boolean isInsert, boolean isClearPassword) {
        this.isInsert = isInsert;
        this.isClearPassword = isClearPassword;
        super.setPreparedStatement(new Object[]{
                    user.getUserMd5(), user.getUserLogin(), user.getUserPassword(), user.getUserFirstName(), user.getUserLastName(),
                    user.getUserMiddleInitial(), user.getUserIsDeleted(), user.getUserManagementId(), user.getUserEmail(),
                    user.getEmailPassword(), user.getUserId()
                });
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        String password = "?";
        if (isClearPassword) {
            password = "md5(?)";
        }
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append(super.getManagementSchema()).append(".user ");
            sql.append("(user_md5, user_login, user_password, user_first_name, user_last_name, user_middle_initial, user_is_deleted, user_management_id, user_email, email_password, user_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, md5(?), ?, ?, ?, ?, ?, ?, ?, ?) ");
        } else {
            sql.append("UPDATE ");
            sql.append(super.getManagementSchema()).append(".user ");
            sql.append("SET ");
            sql.append("user_md5 = ?, user_login = ?, user_password = ").append(password).append(", user_first_name = ?, user_last_name = ?, ");
            sql.append("user_middle_initial = ?, user_is_deleted = ?, user_management_id = ?, user_email = ?, email_password = ? ");
            sql.append("WHERE ");
            sql.append("user_id = ?; ");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
