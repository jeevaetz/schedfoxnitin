/*
 * ImportCCSDataSchedule.java
 *
 * Created on January 18, 2005, 11:41 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.ImportCCS;

import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.ArrayList;
/**
 *
 * @author ira
 */
public class ImportCCSDataSchedule extends GeneralQueryFormat {
    
    private StringBuilder myQueryBuffer;
    private boolean clearBuffer;
    
    private ArrayList Eid;
    private ArrayList Cid;
    private ArrayList Worksite;
    private ArrayList Start;
    private ArrayList End;
    private ArrayList Dates;
    
    public ImportCCSDataSchedule() {
         myReturnString = new String(); 
         clearBuffer = false;
         myQueryBuffer = new StringBuilder(10000);
         Eid = new ArrayList();
         Cid = new ArrayList();
         Worksite = new ArrayList();
         Start = new ArrayList();
         End = new ArrayList();
         Dates = new ArrayList();
    }
     
    public void update(
        String EmpId, String CustId, String WorkSiteId, 
        String Starts, String Ends, String day
    ){
        Eid.add(EmpId);
        Cid.add(CustId);
        Worksite.add(WorkSiteId);
        Start.add(Starts);
        End.add(Ends);
        Dates.add(day);
    }
    
    public void clear() {
        myQueryBuffer = new StringBuilder(10000);
        Eid = new ArrayList();
        Cid = new ArrayList();
        Worksite = new ArrayList();
        Start = new ArrayList();
        End = new ArrayList();
        Dates = new ArrayList();
    }
    
    public String toString() {
        for (int i = 0; i < Eid.size(); i++) {
            myQueryBuffer.append("INSERT INTO schedule(employee_id, " +
                "client_id, " +
                "schedule_override," +
                "schedule_date, " +
                "schedule_start," +
                "schedule_end,   " +
                "schedule_type,  " +
                "schedule_group,  " +
                "schedule_day, " +
                "schedule_master_id, " +
                "schedule_is_deleted) " +
                "VALUES((CASE WHEN (SELECT usked_employee.employee_id FROM usked_employee WHERE usked_emp_id = '" + Eid.get(i) + "' AND " +
                "usked_employee.emp_branch = " + getBranch() + " LIMIT 1) IS NULL THEN 0 ELSE (SELECT usked_employee.employee_id FROM usked_employee WHERE usked_emp_id = '" + Eid.get(i) + "' AND " +
                "usked_employee.emp_branch = " + getBranch() + " LIMIT 1) END), (CASE WHEN (SELECT usked_client.client_id FROM usked_client WHERE " +
                "usked_client.usked_cli_id = '" + Cid.get(i) + "' AND usked_client.client_branch = " + getBranch() + " AND usked_client.usked_ws_id = " +
                "'" + Worksite.get(i) + "' LIMIT 1) IS NULL THEN 0 ELSE (SELECT usked_client.client_id FROM usked_client WHERE " +
                "usked_client.usked_cli_id = '" + Cid.get(i) + "' AND usked_client.client_branch = " + getBranch() + " AND usked_client.usked_ws_id = " +
                "'" + Worksite.get(i) + "' LIMIT 1) END), 2, Date('" + Dates.get(i) + "'), " + Start.get(i) + ", " + End.get(i) + ", 0, 0, (SELECT extract (DOW FROM Date('" + Dates.get(i) + "'))), 0, 0);"); 
             

        }
        return myQueryBuffer.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
