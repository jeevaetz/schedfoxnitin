/*
 * Schedule_Heart_Beat.java
 *
 * Created on June 21, 2004, 8:44 AM
 */
package rmischedule.schedule;


import java.util.*;

import schedfoxlib.model.util.Record_Set;
import rmischedule.data_connection.Connection;
import rmischedule.schedule.components.DShift;
import rmischedule.schedule.components.SClient;
import rmischedule.schedule.components.SEmployee;
import rmischedule.schedule.components.openshifts.OpenShiftPanel;
import schedfoxlib.model.Employee;

/**
 * This is a little thread that will process heartbeat updates from the server.
 *
 * The heartbeat works in the following manner: A client sends a query to the server 
 * that overrides one of the isXXXQuery() methods from GeneralQueryFormat (for
 * example, isScheduleQuery()) to return true.  The server will store this query,
 * overwritting any query of the same type that came before it.  Then, anytime a
 * query is sent by any client that updates information of that type (getUpdateStatus()
 * returns a cooresponding value, for example GeneralQueryFormat.UPDATE_SCHEDULE), the
 * server will then run the stored query and send the results to the individual client
 * over the socket connection.  Confused yet?
 *
 * Here's a little example.  John Doe opens a schedule for Acme Co.  When he does this,
 * the server stores the query responsible for fetching ALL the shift data for that
 * company/branch because the isScheduleQuery() method returns true.  Then, Jane Smith
 * who was already viewing a schedule for Acme Co. modifies a shift.  When the server
 * recieves this request it sees that the getUpdateStatus() method for this query returns
 * GeneralQueryFormat.UPDATE_SCHEDULE.  The server then checks to see if any of its
 * connected clients are interested in viewing schedule updates for this particular
 * branch/company.  It finds schedule queries for both John Doe and Jane Smith... it then
 * executes each of these queries, and sends the results of John's stored query to John,
 * and Jane's stored query to Jane.  John and Jane's computers both recieve the update,
 * which eventually gets processed here by our Schedule_Heart_Beat thread.
 *
 * At this point you might be thinking this is pretty inefficient, and you'd be right.
 * Any modification to a shift would result in the server running a query responsible
 * for fetching ALL the data for that schedule for EACH connected client that was viewing
 * that schedule.  This is why any query that is going to be a heartbeat query should
 * consider time as an input.  Each time the server recieves a heartbeat query from a
 * cient, or sends a response to a client, it updates the lastUpdated field of the stored
 * heartbeat query so that the next time it needs to send an update to a client, it will
 * only send data that has been changed since lastUpdated.  THIS FUNCTIONALITY IS NOT
 * BUILT INTO THE HEARTBEAT LOGIC ITSELF.  Your query must use the lastUpdated field
 * to eliminate unwanted results.
 *
 * Still confused?  Take a look at some of the server code, and try to follow how it
 * keeps track of all the heartbeat junk and maybe this will make more sense.  --Shawn
 *
 * @author  jason.allen
 */
public class Schedule_Heart_Beat {

    public boolean updating;
    private boolean hold;
    private Schedule_View_Panel parent;
    private Connection myConnection;
    private boolean change;
    private boolean kill;
    private int shouldRunAgain;
    private int i, c;
    private DShift ss;
    private OpenShiftPanel myOpenShiftPanel;
    public boolean dontUpdate;
    private PriorityQueue<ArrayList> myQueueOfArrayLists;
    private String shift_id = "0";
    private String shift_master_id = "0";
    private String schedule_date = "";
    private String client_id = "0";
    private String employee_id = "0";
    private String schedule_id = "0";
    int week_no = 0;
    int start_time = 0;
    int end_time = 0;
    int day_code = 0;
    int override = 0;
    long milliseconds = 0;
    boolean isDeleted = false;
    private Calendar tmpDt = Calendar.getInstance();

    /** Creates a new instance of Schedule_Heart_Beat */
    public Schedule_Heart_Beat(Schedule_View_Panel p) {
        parent = p;
        myQueueOfArrayLists = new PriorityQueue<ArrayList>(10);
        myConnection = new Connection();
        myConnection.setBranch(parent.getBranch());
        myConnection.setCompany(parent.getCompany());
        shouldRunAgain = 0;
        dontUpdate = false;
    }

    public void kill() {
        kill = true;
    }

    public void setDontUpdate(boolean val) {
        dontUpdate = val;
    }

    public void setOpenShiftPanel(OpenShiftPanel openPanel) {
        myOpenShiftPanel = openPanel;
    }

    public void queueUpdate(ArrayList Records) {
        myQueueOfArrayLists.add(Records);
    }

    public void beginUpdate(ArrayList myList) {
        if (this.parent.isDoneLoadingCompletely) {
            try {
                updateClients((Record_Set) myList.get(1));
                updateEmployees((Record_Set) myList.get(2));
            } catch (Exception e) {
            }
            updateShifts((Record_Set) myList.get(0));
        }
    }

    /* client update */
    private void updateClients(Record_Set rs) {
        if (shouldRunAgain > 0 || dontUpdate) {
            return;
        }

        if (rs.length() > 0) {
            int i, c;
            int size = rs.length();

            for (i = 0; i < size; i++) {
                if (parent.updateClient(rs)) {
                    change = true;
                }
                rs.moveNext();
            }
        }
    }

    /* employee update */
    private void updateEmployees(Record_Set rs) {
        if (rs.length() > 0) {
            int i, c;
            int size = rs.length();
            do {
                Employee myEmployee = new Employee(new Date(), rs);
                SEmployee aEmployee = new SEmployee(
                        myEmployee,
                        null,
                        parent,
                        rs.getString("hasnotes"),
                        rs.getString("employee_types"),
                        null);
                try {
                    aEmployee.setAllow_sms_messaging(rs.getBoolean("sms_messaging"));
                } catch (Exception e) {
                    aEmployee.setAllow_sms_messaging(false);
                }
                if (parent.updateEmployee(aEmployee)) {
                    change = true;
                }
            } while (rs.moveNext());
        }
    }

    /* 
     *  Update normal shifts
     *  Note:  Please bare in mind that this is very pulemenary.  This just
     *         Does basic updating on the shifts nothing as advanced as 
     *         updating for deletions and ending dates of clients.
     */
    private void updateShifts(Record_Set rs) {
        if (shouldRunAgain > 0 || dontUpdate) {
            return;
        }
        parent.lastUpdated = rs.lu;
        for (int i = 0; i < rs.length(); i++) {
            parseSchedule(rs);
            rs.moveNext();
        }

        parent.orderClients(null);
    }

    private boolean parseSchedule(Record_Set rs) {
        shift_id = rs.getString("sid");
        shift_master_id = rs.getString("smid");
        client_id = rs.getString("cid");
        employee_id = rs.getString("eid");
        schedule_date = rs.getString("date");
        schedule_id = rs.getString("gp");
        week_no = rs.getInt("wk");
        start_time = rs.getInt("start_time");
        end_time = rs.getInt("end_time");
        day_code = rs.getInt("dow");
        override = rs.getInt("ov");
        isDeleted = rs.getBoolean("isdeleted");
        String trainer = rs.getString("trainerid");
        try {
        } catch (Exception e) {
            System.out.println("No weekly rotation specified");
        }

        SEmployee tempEmp = parent.hash_employees.get(Integer.parseInt(employee_id));
        SClient tempCli = parent.hash_clients.get(Integer.parseInt(client_id));

        ss = new DShift(
                shift_id,
                shift_master_id,
                tempCli,
                tempEmp,
                start_time,
                end_time,
                day_code,
                parent,
                schedule_id,
                isDeleted,
                rs.getString("type"),
                schedule_date,
                rs.getString("edate"),
                rs.getString("sdate"),
                rs.getString("pay_opt"),
                rs.getString("bill_opt"),
                rs.getInt("rate_code_id"),
                trainer,
                rs.getString("lu"),
                rs.getString("hasnote"),
                1);

        parent.updateShift(ss);
        parent.CheckScheduleForConflicts(tempEmp.getId() + "");
        return true;
    }

    public void killMe() {
        kill = true;
    }
}
