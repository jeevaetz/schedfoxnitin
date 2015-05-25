/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.timeoff;

import schedfoxlib.model.TimeOffAccrual;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_timeoff_accrual_query extends GeneralQueryFormat {

    private boolean isInsert = false;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(TimeOffAccrual timeOffAccrual) {
        if (timeOffAccrual.getTimeOffAccrualId() == null || timeOffAccrual.getTimeOffAccrualId() == 0) {
            isInsert = true;
            super.setPreparedStatement(timeOffAccrual.getTimeOffInterval(), timeOffAccrual.getDays(),
                    timeOffAccrual.getActive());
        } else {
            super.setPreparedStatement(timeOffAccrual.getTimeOffInterval(), timeOffAccrual.getDays(),
                    timeOffAccrual.getActive(), timeOffAccrual.getTimeOffAccrualId());
        }
    }

    public String getPreparedStatement() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO time_off_accrual ");
        sql.append("time_off_accrual_id ");

        return sql.toString();
    }

    public boolean hasPreparedStatement() {
        return true;
    }

}
