/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.schedule_data;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.ScheduleViewLog;

/**
 *
 * @author ira
 */
public class save_schedule_view_query extends GeneralQueryFormat {

    private boolean isUpdate;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(ScheduleViewLog log) {
        if (log.getScheduleViewLogId() != null) {
            isUpdate = true;
            super.setPreparedStatement(new Object[]{
                log.getViewType(), log.getRemoteAddress(), log.getIsMobile(), log.getViewTime(), 
                log.getFromUrlShorteningService(), log.getEmployeeId(), log.getStartDate(), log.getEndDate(),
                log.getScheduleViewLogId()
            });
        } else {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{
                log.getViewType(), log.getRemoteAddress(), log.getIsMobile(), log.getViewTime(), 
                log.getFromUrlShorteningService(), log.getEmployeeId(), log.getStartDate(), log.getEndDate()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isUpdate) {
            sql.append("UPDATE schedule_view_log ");
            sql.append("SET ");
            sql.append("view_type = ?, remote_address = ?, is_mobile = ?, view_time = ?, ");
            sql.append("from_url_shortening_service = ?, employee_id = ?, start_date = ?, ");
            sql.append("end_date = ? ");
            sql.append("WHERE ");
            sql.append("schedule_view_log_id = ? ");
        } else {
            sql.append("INSERT INTO ");
            sql.append("schedule_view_log ");
            sql.append("(view_type, remote_address, is_mobile, view_time, from_url_shortening_service, ");
            sql.append("employee_id, start_date, end_date) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?); ");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
