/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.ArrayList;
import java.util.Date;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.notification.get_notification_sent_by_id_and_type_query;
import rmischeduleserver.mysqlconnectivity.queries.notification.get_removed_emps_from_sched_query;
import rmischeduleserver.mysqlconnectivity.queries.notification.save_notification_query;
import schedfoxlib.model.Employee;
import schedfoxlib.model.NotificationSent;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class NotificationController {

    private String companyId;

    private NotificationController(String companyId) {
        this.companyId = companyId;
    }

    public static NotificationController getInstance(String companyId) {
        return new NotificationController(companyId);
    }

    public ArrayList<Employee> getEmployeesTakenOffSchedule() {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Employee> retVal = new ArrayList<Employee>();
        try {
            get_removed_emps_from_sched_query query = new get_removed_emps_from_sched_query();
            query.setCompany(companyId);
            query.setPreparedStatement(new Object[]{});
            EmployeeController empController = EmployeeController.getInstance(companyId);
            
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(empController.getEmployeeById(rst.getInt("eid")));
                rst.moveNext();
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return retVal;
    }
    
    public NotificationSent getNotificationByEmployeeIdAndType(int employeeId, int notificationType) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        NotificationSent retVal = new NotificationSent();
        try {
            get_notification_sent_by_id_and_type_query query = new get_notification_sent_by_id_and_type_query();
            query.setCompany(companyId);
            query.setPreparedStatement(new Object[]{employeeId, notificationType});
            Record_Set rst = conn.executeQuery(query, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal = new NotificationSent(rst);
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
    }

    public void saveNotificationSent(NotificationSent notification) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            save_notification_query query = new save_notification_query();
            query.setCompany(companyId);
            query.update(notification);
            conn.executeUpdate(query, "");
        } catch (Exception e) {
        }
    }
}
