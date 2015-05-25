/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in;


import schedfoxlib.model.CheckIn;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_checkin_obj_query extends GeneralQueryFormat {
    
   @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(CheckIn checkInObj) {
            super.setPreparedStatement(new Object[]{
                checkInObj.getShiftId(),checkInObj.getEmployeeId()
            });
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM checkin ");
        sql.append("WHERE ");
        sql.append("shift_id = ?");
        sql.append(" AND ");
        sql.append("employee_id = ?;");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }


}
