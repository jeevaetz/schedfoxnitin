/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.event_log;

import java.text.SimpleDateFormat;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import schedfoxlib.model.EventType;
import schedfoxlib.model.ScheduleData;
import schedfoxlib.model.User;

/**
 *
 * @author ira
 */
public interface CachedEventData {
    public Employee fetchEmployeeData(Integer employeeId);
    public Client fetchClientData(Integer clientId);
    public ScheduleData fetchSchedData(String data);
    public User fetchUserData(Integer userId);
    public Branch fetchBranchData(Integer branchId);
    public EventType fetchEventTypeData(Integer eventType);
    public SimpleDateFormat getMyFormat();
}
