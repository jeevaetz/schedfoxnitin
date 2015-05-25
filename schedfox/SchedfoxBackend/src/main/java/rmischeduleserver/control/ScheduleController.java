package rmischeduleserver.control;

import schedfoxlib.controller.ScheduleControllerInterface;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.controller.exceptions.NoSuchEmployeeException;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.AssembleCheckinScheduleType;
import schedfoxlib.model.Caller;
import schedfoxlib.model.CheckIn;
import schedfoxlib.model.Employee;
import schedfoxlib.model.ScheduleData;
import schedfoxlib.model.UserInterface;
import rmischeduleserver.log.MyLogger;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.employee.update_employee_phn_by_type_query;
import rmischeduleserver.mysqlconnectivity.queries.ivr.get_calls_by_employeeID;
import rmischeduleserver.mysqlconnectivity.queries.ivr.log_caller_query;
import rmischeduleserver.mysqlconnectivity.queries.login.get_employee_by_employeeid;
import rmischeduleserver.mysqlconnectivity.queries.login.get_employees_by_cid;
import rmischeduleserver.mysqlconnectivity.queries.login.get_employees_by_cid_from_inbound_caller;
import rmischeduleserver.mysqlconnectivity.queries.login.get_employees_by_last_4_social_query;
import rmischeduleserver.mysqlconnectivity.queries.login.get_employee_by_social_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.get_changed_shifts_notified_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.get_checkin_obj_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.get_checkin_obj_with_no_check_out_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.get_missed_checkins_for_client_contacts_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.get_missed_checkins_for_dm_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.get_missed_checkins_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.get_notified_checkins_that_have_not_resolved_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.new_assemble_schedule_for_checkin_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.save_check_in_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.save_check_out_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.save_checkin_notification_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.save_checkin_obj_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.create_temporary_shift_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.edit_temporary_shift_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.generic_assemble_schedule_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.get_people_taken_off_shifts_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.get_schedule_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.get_schedule_by_ids_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.get_schedule_logs_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.get_schedule_master_by_ids_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.override_schedule_master_for_day_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.save_schedule_view_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.update_master_shift_time;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.update_temp_shift_time;
import schedfoxlib.model.CheckinNotifications;
import schedfoxlib.model.Client;
import schedfoxlib.model.ScheduleViewLog;
import schedfoxlib.model.ShiftOptionsClass;
import schedfoxlib.model.ShiftTypeClass;

/**
 * Singleton class that communicates to the SchedFox database and returns object
 * for use in outside API's
 *
 * @author dalbers
 */
public class ScheduleController implements ScheduleControllerInterface {

    //private static ScheduleController myInstance;
    private String companyId;
    public static int HOME = 1;
    public static int CELL = 2;
    public static int ALT = 3;

    public ScheduleController(String companyId) {
        this.companyId = companyId;
    }

    public ScheduleController(String companyId, MyLogger log) {
        this.companyId = companyId;
    }

    @Override
    public boolean employeeExists(String ssn) {
        boolean success = false;
        return success;
    }
    
    public void saveCheckinNotifications(CheckinNotifications checkinNotification) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_checkin_notification_query saveNotificationQuery = new save_checkin_notification_query();
        saveNotificationQuery.setCompany(companyId);
        saveNotificationQuery.update(checkinNotification);
        try {
            conn.executeQuery(saveNotificationQuery, "");
        } catch (Exception exe) {}
    }

    @Override
    //@TODO: Need to revisit, causes a lot of load on the database
    public HashMap<Employee, Client> getEmployeesAndClientsNoLongerWorking(int branchId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        HashMap<Employee, Client> retVal = new HashMap<Employee, Client>();
        get_people_taken_off_shifts_query shiftsQuery = new get_people_taken_off_shifts_query();
        shiftsQuery.setPreparedStatement(new Object[]{branchId, branchId, branchId});
        
        EmployeeController employeeController = EmployeeController.getInstance(this.companyId);
        ClientController clientController = ClientController.getInstance(this.companyId);
        
        try {
            shiftsQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(shiftsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                int clientId = rst.getInt("client_id");
                int employeeId = rst.getInt("employee_id");
                
                Client client = clientController.getClientById(clientId);
                Employee employee = employeeController.getEmployeeById(employeeId);
                retVal.put(employee, client);
                rst.moveNext();
            }
        } catch (SQLException exe) {
            conn = null;
            throw new RetrieveDataException();
        }
        
        return retVal;
    }
    
    @Override
    public void saveEmployeePhoneNumber(int employeeId, String phoneNumber, int phnType) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        update_employee_phn_by_type_query phnQuery = new update_employee_phn_by_type_query();
        phnQuery.update(phoneNumber, employeeId, phnType);
        phnQuery.setCompany(companyId);
        try {
            conn.executeUpdate(phnQuery, "");
        } catch (SQLException e) {
            conn = null;
            throw new SaveDataException();
        }
        conn = null;
    }

    /**
     * ** Employee Look up objects ***********
     */
    /**
     * Search for an employee using the current date and the employee's social
     * security number
     *
     * @param currentDate Today's date
     * @param ssn Employee's social security number
     * @return employee Employee object found for the SSN
     * @throws NoSuchEmployeeException
     * @throws RetrieveDataException
     */
    @Override
    public Employee getEmployeeBySSN(Date currentDate, String ssn) throws RetrieveDataException {//NoSuchEmployeeException,
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_employee_by_social_query ssnQuery = new get_employee_by_social_query();

        ssnQuery.setPreparedStatement(new Object[]{ssn});
        ssnQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(ssnQuery, "");
            if (rst.length() == 0) {
                conn = null;
                return null;
            }
            //throw new NoSuchEmployeeException();

            Employee emp = new Employee(currentDate, rst);

            conn = null;
            return emp;
        } catch (SQLException e) {
            conn = null;
            throw new RetrieveDataException();
        }
    }

    @Override
    public Employee getEmployeeByEmployeeID(Date currentDate, int employeeID) throws RetrieveDataException {//NoSuchEmployeeException,
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_employee_by_employeeid empIDquery = new get_employee_by_employeeid();
        empIDquery.setPreparedStatement(new Object[]{employeeID});
        empIDquery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(empIDquery, "");
            if (rst.length() == 0) {
                conn = null;
                return null;
            }
            //throw new NoSuchEmployeeException();

            Employee emp = new Employee(currentDate, rst);
            conn = null;
            return emp;
        } catch (SQLException e) {
            conn = null;

            throw new RetrieveDataException();
        }
    }

    @Override
    public ArrayList<Employee> getEmployeeByLast4SSN(Date currentDate, String ssn) throws RetrieveDataException {//NoSuchEmployeeException,
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Employee> emps = new ArrayList<Employee>();
        get_employees_by_last_4_social_query ssnQuery = new get_employees_by_last_4_social_query();
        String ssnStr = "%" + ssn;

        ssnQuery.setPreparedStatement(new Object[]{ssnStr});
        ssnQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(ssnQuery, "");
            if (rst.length() == 0) {
                conn = null;
                return null;
            } else {
                for (int r = 0; r < rst.length(); r++) {
                    emps.add(new Employee(currentDate, rst));
                    rst.moveNext();
                }
            }



        } catch (SQLException e) {
            conn = null;
            throw new RetrieveDataException();
        }
        conn = null;
        return emps;
    }

    /**
     * Look up and employee using the Caller ID (cid), this method checks all
     * the phone numbers in the employee record for a match
     *
     * @param currentDate Today's date
     * @param cid Caller ID number to look up
     * @return employee Employee object found for the caller ID
     * @throws NoSuchEmployeeException
     * @throws RetrieveDataException
     */
    @Override
    public ArrayList<Employee> getEmployeeByCID(Date currentDate, String cid) throws NoSuchEmployeeException, RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_employees_by_cid cidQuery = new get_employees_by_cid();
        cidQuery.setPreparedStatement(new Object[]{cid, cid, cid});
        cidQuery.setCompany(companyId);
        ArrayList<Employee> emps = new ArrayList<Employee>();
        try {
            Record_Set rst = conn.executeQuery(cidQuery, "");
            if (rst.length() == 0) {
                //return null;
                conn = null;
                //throw new NoSuchEmployeeException();
            } else {
                for (int r = 0; r < rst.length(); r++) {
                    emps.add(new Employee(currentDate, rst));
                    rst.moveNext();
                }
            }

        } catch (SQLException e) {
            conn = null;
            throw new RetrieveDataException();
        }

        ArrayList<Employee> secondEmp = getEmployeeByCIDInInboundCallsTable(currentDate, cid);
        if (secondEmp != null) {

            for (int i = 0; i < secondEmp.size(); i++) {
                Employee tmpEmp = secondEmp.get(i);
                boolean match = false;
                for (int j = 0; j < emps.size(); j++) {

                    if (emps.get(j).getEmployeeSsn().equals(tmpEmp.getEmployeeSsn())) {
                        match = true;
                    }

                }
                if (!match) {
                    emps.add(tmpEmp);
                }

            }

        } else {
            conn = null;
            return null;
        }
        conn = null;
        return emps;
    }

    @Override
    public ArrayList<Employee> getEmployeeByCIDInInboundCallsTable(Date currentDate, String cid) throws NoSuchEmployeeException, RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_employees_by_cid_from_inbound_caller cidQuery = new get_employees_by_cid_from_inbound_caller();

        cidQuery.setCompany(companyId);

        cidQuery.setPreparedStatement(new Object[]{cid});

        System.out.println("CID QUERY = " + cidQuery.getPreparedStatementString());




        ArrayList<Employee> emps = new ArrayList<Employee>();
        try {
            Record_Set rst = conn.executeQuery(cidQuery, "");
            if (rst.length() == 0) {
                conn = null;
                return null;
                //throw new NoSuchEmployeeException();
            } else {
                for (int r = 0; r < rst.length(); r++) {
                    emps.add(new Employee(currentDate, rst));
                    rst.moveNext();
                }

            }

        } catch (SQLException e) {
            conn = null;
            throw new RetrieveDataException();
        }
        conn = null;
        return emps;
    }
    
    public ArrayList<CheckinNotifications> checkChangedNonResolvedNotifications() throws RetrieveDataException {
        ArrayList<CheckinNotifications> retVal = new ArrayList<CheckinNotifications>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_changed_shifts_notified_query myQuery = new get_changed_shifts_notified_query();
        myQuery.setPreparedStatement(new Object[]{});
        myQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new CheckinNotifications(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
        }
        return retVal;
    }
    
    public ArrayList<CheckinNotifications> checkNonResolvedNotifications() throws RetrieveDataException {
        ArrayList<CheckinNotifications> retVal = new ArrayList<CheckinNotifications>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_notified_checkins_that_have_not_resolved_query myQuery = new get_notified_checkins_that_have_not_resolved_query();
        myQuery.setPreparedStatement(new Object[]{});
        myQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new CheckinNotifications(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
        }
        return retVal;
    }
    
    public ArrayList<ScheduleData> getNonCheckedInSchedulesForClientContact(Integer maxBuffer) throws RetrieveDataException {
        ArrayList<ScheduleData> retVal = new ArrayList<ScheduleData>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_missed_checkins_for_client_contacts_query missedCheckinQuery = new get_missed_checkins_for_client_contacts_query();
        missedCheckinQuery.setPreparedStatement(new Object[]{maxBuffer});
        missedCheckinQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(missedCheckinQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ScheduleData(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
        }
        return retVal;
    }

    public ArrayList<ScheduleData> getNonCheckedInSchedulesForDMs(Integer maxBuffer) throws RetrieveDataException {
        ArrayList<ScheduleData> retVal = new ArrayList<ScheduleData>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_missed_checkins_for_dm_query missedCheckinQuery = new get_missed_checkins_for_dm_query();
        missedCheckinQuery.setPreparedStatement(new Object[]{maxBuffer});
        missedCheckinQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(missedCheckinQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ScheduleData(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
        }
        return retVal;
    }
    
    public ArrayList<ScheduleData> getNonCheckedInSchedules(Integer minBuffer, Integer maxBuffer) throws RetrieveDataException {
        ArrayList<ScheduleData> retVal = new ArrayList<ScheduleData>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_missed_checkins_query missedCheckinQuery = new get_missed_checkins_query();
        missedCheckinQuery.setPreparedStatement(new Object[]{minBuffer, maxBuffer});
        missedCheckinQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(missedCheckinQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ScheduleData(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
        }
        return retVal;
    }
    
    /**
     * *** Schedule Objects *****************
     */
    /**
     * Retrieve an Array list containing all the schedule objects for the
     * employee object passed in based on a start and finish date.
     *
     * @param startDate Date to start the look up
     * @param endDate Date to end the look up
     * @param emp Employee object to use for the schedule search
     * @return retVal List containing all ScheduleData objects
     * @throws RetrieveDataException
     */
    @Override
    public ArrayList<ScheduleData> getSchedule(Date startDate, Date endDate, Employee emp) throws RetrieveDataException {
        ArrayList<ScheduleData> retVal = new ArrayList<ScheduleData>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        generic_assemble_schedule_query scheduleQuery = new generic_assemble_schedule_query();
        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        scheduleQuery.update("", "(" + emp.getEmployeeId() + ")",
                myDateFormat.format(startDate),
                myDateFormat.format(endDate), "", null, false);
        scheduleQuery.setCompany(companyId);
        scheduleQuery.setBranch("-1");
        try {
            Record_Set rst = conn.executeQuery(scheduleQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ScheduleData(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
            throw new RetrieveDataException();
        }
        conn = null;
        return retVal;
    }
    
    public ArrayList<ScheduleData> getSchedule(Date startDate, Date endDate, Employee emp, Client cli) throws RetrieveDataException {
        ArrayList<ScheduleData> retVal = new ArrayList<ScheduleData>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        generic_assemble_schedule_query scheduleQuery = new generic_assemble_schedule_query();
        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String clientStr = "";
        if (cli != null) {
            clientStr = cli.getClientId() + "";
        }
        String employeeStr = "";
        if (emp != null) {
            employeeStr = emp.getEmployeeId() + "";
        }
        scheduleQuery.update(clientStr, employeeStr,
                myDateFormat.format(startDate),
                myDateFormat.format(endDate), "", null, false);
        scheduleQuery.setCompany(companyId);
        scheduleQuery.setBranch("-1");
        try {
            Record_Set rst = conn.executeQuery(scheduleQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ScheduleData(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
            throw new RetrieveDataException();
        }
        conn = null;
        return retVal;
    }

    public ArrayList<ScheduleData> getPatrolProCurrentScheduleDataForEmployee(Integer employee, Integer bufferInMinutes) throws RetrieveDataException {
        ArrayList<ScheduleData> retVal = new ArrayList<ScheduleData>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        new_assemble_schedule_for_checkin_query scheduleQuery = new new_assemble_schedule_for_checkin_query();

        GenericController genericFactory = GenericController.getInstance("2");
        Date date = new Date(genericFactory.getCurrentTimeMillis());
        
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        
        startCal.setTime(date);
        endCal.setTime(date);
        
        startCal.add(Calendar.DAY_OF_MONTH, -1);
        endCal.add(Calendar.DAY_OF_MONTH, 1);
        
        Employee emp = EmployeeController.getInstance(companyId).getEmployeeById(employee);
        if (emp.getEmployeeIsDeleted() == 1) {
            return new ArrayList<ScheduleData>();
        }
        
        scheduleQuery.setPreparedStatement(new Object[]{startCal.getTime(), endCal.getTime(),
                    -1, employee, null, null, bufferInMinutes});
        scheduleQuery.setCompany(companyId);
        scheduleQuery.setBranch("-1");
        try {
            Record_Set rst = conn.executeQuery(scheduleQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                ScheduleData sched = new ScheduleData(rst);
                if (sched.getIsDeleted() != 1) {
                    retVal.add(sched);
                }
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
            throw new RetrieveDataException();
        }
        
        ClientController clientController = ClientController.getInstance(this.companyId);
        
        //This is a bit hacky specifically for Patrol Pro so that worksites allow us to login to Patrol Pro.
        try {
            for (int r = 0; r < retVal.size(); r++) {
                ScheduleData sched = retVal.get(r);
                Client cli = clientController.getClientById(sched.getClientId());
                if (cli.getClientWorksite() != 0) {
                    retVal.get(r).setClientId(cli.getClientWorksite());
                }
            }
        } catch (Exception exe) {}
        
        return retVal;
    }
    
    public ArrayList<Integer> getClientsWorkedAtLastXWeeks(Integer employeeId, int weekToStartAt, int numberOfWeeksToCheckBack) {
        ArrayList<Integer> clients = null;

        try {
            GenericController genericController = GenericController.getInstance(companyId);

            Date currentDate = new Date(genericController.getCurrentTimeMillis());
            Calendar currentCalendar = Calendar.getInstance();
            Calendar startCalendar = Calendar.getInstance();
            currentCalendar.setTime(currentDate);
            startCalendar.setTime(currentDate);
            startCalendar.add(Calendar.WEEK_OF_YEAR, -numberOfWeeksToCheckBack);
            currentCalendar.add(Calendar.WEEK_OF_YEAR, -weekToStartAt);

            Employee employee = new Employee();
            employee.setEmployeeId(employeeId);
            
            ArrayList<ScheduleData> schedules = this.getSchedule(startCalendar.getTime(), currentCalendar.getTime(), employee);
            HashMap<Integer, ArrayList<ScheduleData>> schedulesByWeek = new HashMap<Integer, ArrayList<ScheduleData>>();

            //Loop through everything and catagorize by weeks
            for (int s = 0; s < schedules.size(); s++) {
                ScheduleData currSchedule = schedules.get(s);

                Calendar currCalendar = Calendar.getInstance();
                currCalendar.setTime(currSchedule.getDate());

                if (schedulesByWeek.get(currCalendar.get(Calendar.WEEK_OF_YEAR)) == null) {
                    schedulesByWeek.put(currCalendar.get(Calendar.WEEK_OF_YEAR), new ArrayList<ScheduleData>());
                }
                schedulesByWeek.get(currCalendar.get(Calendar.WEEK_OF_YEAR)).add(currSchedule);
            }

            Iterator<Integer> keyIterator = schedulesByWeek.keySet().iterator();
            while (keyIterator.hasNext()) {
                ArrayList<ScheduleData> currSchedules = schedulesByWeek.get(keyIterator.next());
                //First run put all client ids in collection
                if (clients == null) {
                    clients = new ArrayList();
                    HashMap<Integer, Integer> clientIds = new HashMap<Integer, Integer>();
                    for (int s = 0; s < currSchedules.size(); s++) {
                        clientIds.put(currSchedules.get(s).getClientId(), schedules.get(s).getClientId());
                    }
                    clients.addAll(clientIds.keySet());
                } else {
                    //Now remove any that don't exist
                    for (int c = clients.size() - 1; c >= 0; c--) {
                        boolean exists = false;
                        for (int s = 0; s < currSchedules.size(); s++) {
                            if (currSchedules.get(s).getClientId().equals(clients.get(c))) {
                                exists = true;
                            }
                        }
                        if (!exists) {
                            clients.remove(c);
                        }
                    }
                }
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return clients;
    }
    
    /**
     * Look up the schedule object that is closest to the current time, list
     * should contain only one object
     *
     * @param startDate Today's date
     * @param endDate Today's date
     * @param bufferInMinutes Number of minutes to search for the schedule from
     * the current time.
     * @param emp Employee that the schedule belongs to
     * @return retVal A list with a single Schedule object
     * @throws RetrieveDataException
     */
    @Override
    public ArrayList<AssembleCheckinScheduleType> getScheduleForCheckin(Date startDate, Date endDate, int bufferInMinutes, Employee emp) throws RetrieveDataException {
        ArrayList<AssembleCheckinScheduleType> retVal = new ArrayList<AssembleCheckinScheduleType>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        new_assemble_schedule_for_checkin_query scheduleQuery = new new_assemble_schedule_for_checkin_query();

        scheduleQuery.setPreparedStatement(new Object[]{startDate, endDate,
                    -1, emp.getEmployeeId(), null, null, bufferInMinutes});
        scheduleQuery.setCompany(companyId);
        scheduleQuery.setBranch("-1");
        try {
            Record_Set rst = conn.executeQuery(scheduleQuery, "");
            for (int r = 0; r < rst.length(); r++) {

                retVal.add(new AssembleCheckinScheduleType(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
            throw new RetrieveDataException();
        }
        conn = null;

        return retVal;
    }

    /**
     * ********* Check In and Out Methods *************************************************************
     */
    /**
     * Pass the unique schedule id and the employee id to get a CheckIn Object,
     * the object will return an empty object with the isCheckedIn() method set
     * to false if there isn't a check in available
     *
     * @param scheduleID Unique schedule identifier
     * @param employeeID Unique employee identifier
     * @param companyID Schema company ID
     * @return checkInEmployee CheckIn Object created from the result set
     * results of the query
     * @throws RetrieveDataException
     */
    @Override
    public CheckIn getCheckInObj(String scheduleID, Integer employeeID, String companyID) throws RetrieveDataException {
        CheckIn checkIn = new CheckIn();
        checkIn.setShiftId(scheduleID);
        checkIn.setEmployeeId(employeeID);

        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_checkin_obj_query checkInQuery = new get_checkin_obj_query();
        //checkInQuery.setPreparedStatement(new Object[]{scheduleID,employeeID});
        checkInQuery.update(checkIn);
        checkInQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(checkInQuery, "");
            if (rst.length() == 0) {
                checkIn = new CheckIn();
                checkIn.setCheckedIn(false);

                //throw new NoSuchEmployeeException();
            } else {
                checkIn = new CheckIn(rst);
                if (checkIn.getPersonCheckedIn() != 0) {
                    checkIn.setCheckedIn(true);
                } else {
                    checkIn.setCheckedIn(false);
                }
                if (checkIn.getPersonCheckedOut() != 0) {
                    checkIn.setCheckedOut(true);
                } else {
                    checkIn.setCheckedOut(false);
                }

            }

        } catch (SQLException e) {
            conn = null;
            throw new RetrieveDataException();
        }
        conn = null;
        return checkIn;
    }

    @Override
    public ArrayList<CheckIn> getCheckInObjWithNoCheckOut(int timeStamp, int person, int timeBuffer, String startDate, String endDate) throws RetrieveDataException {
        ArrayList<CheckIn> checkIns = new ArrayList<CheckIn>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_checkin_obj_with_no_check_out_query checkInQuery = new get_checkin_obj_with_no_check_out_query();

        checkInQuery.setCompany(companyId);
        checkInQuery.update(0, 0, timeBuffer, startDate, endDate);
        System.out.println("This is the query: " + checkInQuery.getPreparedStatementString());

        try {
            Record_Set rst = conn.executeQuery(checkInQuery, "");
            if (rst.length() == 0) {
                return null;
                //throw new NoSuchEmployeeException();
            } else {
                for (int r = 0; r < rst.length(); r++) {

                    checkIns.add(new CheckIn(rst));
                    rst.moveNext();
                }
            }

        } catch (SQLException e) {
            conn = null;
            throw new RetrieveDataException();
        }
        conn = null;
        return checkIns;
    }

    /**
     * Check in process to add a row into the check in table when a guard calls
     * the IVR system.
     *
     * @param schedule Schedule object containing all the schedule information
     * for this check in action
     * @param user Employee object is used for this userInterface
     * @param emp Employee that is checking in
     * @throws SaveDataException
     */
    @Override
    public void checkInEmployee(AssembleCheckinScheduleType schedule, UserInterface user, Employee emp) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_check_in_query saveQuery = new save_check_in_query();

        saveQuery.setCompany(companyId);

        try {
            long currentTime = conn.getServerCurrentTimeMillis();
            //SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String _now = format.format(new Date());

            CheckIn checkinObj = new CheckIn();
            checkinObj.setCheckInDate(schedule.getDate());
            checkinObj.setShiftId(schedule.getSid());
            checkinObj.setEmployeeId(emp.getEmployeeId());
            checkinObj.setEndTime(schedule.getEnd_time());
            checkinObj.setStartTime(schedule.getStart_time());
            if (user instanceof Employee) {
                checkinObj.setPersonCheckedIn(-1 * user.getId());
            } else {
                checkinObj.setPersonCheckedIn(user.getId());
            }
            checkinObj.setTimeStamp(currentTime);

            System.out.println("Check data:" + checkinObj.getPersonCheckedIn() + ", " + checkinObj.getTimeStamp() + ", "
                    + checkinObj.getPersonCheckedOut() + ", " + checkinObj.getTimeStampOut() + ", "
                    + checkinObj.getCheckInDate() + ", " + checkinObj.getEmployeeId() + ", " + checkinObj.getShiftId() + ", "
                    + checkinObj.getCheckInLastUpdated() + ", " + checkinObj.getStartTime() + ", " + checkinObj.getEndTime());

            saveQuery.update(_now, String.valueOf(schedule.getSid()), String.valueOf(schedule.getEnd_time()), String.valueOf(schedule.getStart_time()),
                    String.valueOf(checkinObj.getEmployeeId()), String.valueOf(checkinObj.getPersonCheckedIn()), String.valueOf(checkinObj.getTimeStamp()));
            conn.executeUpdate(saveQuery, "");

        } catch (Exception e) {
            conn = null;
            throw new SaveDataException();
        }
        conn = null;
    }

    @Override
    public boolean checkOut(AssembleCheckinScheduleType schedule, UserInterface user, Employee emp) throws SaveDataException {

        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_check_out_query saveQuery = new save_check_out_query();
        long currentTime = conn.getServerCurrentTimeMillis();
        saveQuery.setCompany(companyId);
        CheckIn checkinObj = new CheckIn();
        checkinObj.setCheckInDate(schedule.getDate());
        checkinObj.setShiftId(schedule.getSid());
        checkinObj.setEmployeeId(emp.getEmployeeId());
        checkinObj.setEndTime(schedule.getEnd_time());
        checkinObj.setStartTime(schedule.getStart_time());
        checkinObj.setPersonCheckedOut(-1 * user.getId());
        checkinObj.setTimeStampOut(currentTime);

        try {


            saveQuery.update(String.valueOf(checkinObj.getShiftId()), String.valueOf(checkinObj.getTimeStampOut()));
            conn.executeUpdate(saveQuery, "");

        } catch (Exception e) {
            conn = null;
            throw new SaveDataException();

        }
        conn = null;
        return false;
    }

    @Override
    public boolean checkOutCron(CheckIn checkInObj) throws SaveDataException {

        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_checkin_obj_query saveQuery = new save_checkin_obj_query();

        saveQuery.setCompany(companyId);

        try {

            long currentTime = conn.getServerCurrentTimeMillis();
            checkInObj.setTimeStampOut(currentTime);


            saveQuery.update(checkInObj);
            conn.executeUpdate(saveQuery, "");

        } catch (Exception e) {
            conn = null;
            throw new SaveDataException();

        }
        conn = null;
        return false;
    }

    /**
     * ** Call handling Moving this to new controller
     **
     */
    @Override
    public void logInboundCall(Caller caller) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        log_caller_query callerQuery = new log_caller_query();

        callerQuery.setCompany(companyId);
        callerQuery.setPreparedStatement(new Object[]{caller});
        try {
            conn.executeUpdate(callerQuery, "");
        } catch (Exception e) {
            conn = null;
        }
        conn = null;
    }

    @Override
    public ArrayList<Caller> getAllCallsByEmployeeID(int employeeID) {
        ArrayList<Caller> calls = new ArrayList<Caller>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_calls_by_employeeID callerQuery = new get_calls_by_employeeID();

        callerQuery.setCompany(companyId);
        callerQuery.setPreparedStatement(new Object[]{employeeID});
        try {
            Record_Set rs = conn.executeQuery(callerQuery, "");
            if (rs.length() == 0) {
                return null;
            } else {
                for (int r = 0; r < rs.length(); r++) {
                    calls.add(new Caller(rs));
                    rs.moveNext();
                }
            }
        } catch (Exception e) {
            conn = null;
        }
        conn = null;
        return calls;

    }

    public ArrayList<ScheduleData> getCurrentSchedulesAtLocationWithBuffer(int clientId, int bufferInMinutes) throws RetrieveDataException {
        ArrayList<ScheduleData> retVal = new ArrayList<ScheduleData>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        new_assemble_schedule_for_checkin_query scheduleQuery = new new_assemble_schedule_for_checkin_query();

        EmployeeController employeeController = EmployeeController.getInstance(companyId);

        GenericController genFactory = GenericController.getInstance(companyId);
        long currentTimeFromServer = genFactory.getCurrentTimeMillis();
        Date startDate = new Date(currentTimeFromServer);
        Date endDate = new Date(currentTimeFromServer);

        scheduleQuery.setPreparedStatement(new Object[]{startDate, endDate,
                    -1, null, null, "{" + clientId + "}", bufferInMinutes});
        scheduleQuery.setCompany(companyId);
        scheduleQuery.setBranch("-1");
        try {
            Record_Set rst = conn.executeQuery(scheduleQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                ScheduleData schedData = new ScheduleData(rst);

                Employee schedEmp = employeeController.getEmployeeById(schedData.getEmployeeId());
                schedData.setEmployee(schedEmp);

                retVal.add(schedData);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public ArrayList<ScheduleData> getScheduleForDateRange(Date startDate, Date endDate, int clientId) throws RetrieveDataException {
        return getScheduleForDateRange(startDate, endDate, clientId + "");
    }

    public ArrayList<ScheduleData> getScheduleForDateRange(Date startDate, Date endDate, String clientId) throws RetrieveDataException {
        ArrayList<ScheduleData> retVal = new ArrayList<ScheduleData>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        generic_assemble_schedule_query scheduleQuery = new generic_assemble_schedule_query();
        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        scheduleQuery.update(clientId + "", "",
                myDateFormat.format(startDate),
                myDateFormat.format(endDate), "", "", false);
        scheduleQuery.setCompany(companyId);
        scheduleQuery.setBranch("-1");
        try {
            Record_Set rst = conn.executeQuery(scheduleQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ScheduleData(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
            throw new RetrieveDataException();
        }
        conn = null;
        return retVal;
    }
    
    public ArrayList<ScheduleData> getSchedulesByIdentifiers(ArrayList<Integer> scheduleId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        
        ArrayList<Integer> tempSchedule = new ArrayList<Integer>();
        ArrayList<Integer> masterSchedule = new ArrayList<Integer>();
        for (int s = 0; s < scheduleId.size(); s++) {
            if (scheduleId.get(s) > 0) {
                tempSchedule.add(scheduleId.get(s));
            } else {
                masterSchedule.add(scheduleId.get(s) * -1);
            }
        }
        
        get_schedule_by_ids_query scheduleByIdQuery = new get_schedule_by_ids_query();
        scheduleByIdQuery.update(tempSchedule.size());
        scheduleByIdQuery.setPreparedStatement(tempSchedule.toArray());
        scheduleByIdQuery.setCompany(companyId);
        scheduleByIdQuery.setBranch("-1");

        ArrayList<ScheduleData> retVal = new ArrayList<ScheduleData>();
        try {
            Record_Set rst = conn.executeQuery(scheduleByIdQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ScheduleData(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
            throw new RetrieveDataException();
        }

        get_schedule_master_by_ids_query scheduleMasterByIdQuery = new get_schedule_master_by_ids_query();
        scheduleMasterByIdQuery.update(masterSchedule.size());
        scheduleMasterByIdQuery.setPreparedStatement(masterSchedule.toArray());
        scheduleMasterByIdQuery.setCompany(companyId);
        scheduleMasterByIdQuery.setBranch("-1");

        try {
            Record_Set rst = conn.executeQuery(scheduleMasterByIdQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                ScheduleData masterData = new ScheduleData(rst);
                masterData.setScheduleId((Integer.parseInt(masterData.getScheduleId())) + "");
                retVal.add(masterData);
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
            throw new RetrieveDataException();
        }
        
        return retVal;
    }

    public ScheduleData getScheduleByIdentifier(Integer scheduleId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_schedule_by_id_query scheduleByIdQuery = new get_schedule_by_id_query();
        scheduleByIdQuery.update(scheduleId < 0);
        scheduleByIdQuery.setPreparedStatement(new Object[]{Math.abs(scheduleId)});

        scheduleByIdQuery.setCompany(companyId);
        scheduleByIdQuery.setBranch("-1");

        ScheduleData retVal = null;
        try {
            Record_Set rst = conn.executeQuery(scheduleByIdQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ScheduleData(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            conn = null;
            throw new RetrieveDataException();
        }

        return retVal;
    }
    
        
    public void createTempShift(String employeeId, String clientId, String userId, String dayOfYear, String startT, String endT, Integer dayOfWeek,
            ShiftTypeClass myType, String scheduleId, String masterShiftId, ShiftOptionsClass myPayOptions, ShiftOptionsClass myBillOptions,
            Integer rate_code) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        GeneralQueryFormat query = this.getTempShiftQuery(employeeId, clientId, userId, dayOfYear, startT, endT, dayOfWeek, myType, scheduleId, masterShiftId, myPayOptions, myBillOptions, rate_code);
        query.setCompany(companyId);
        try {
            conn.executeUpdate(query, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }
    
    public void deletePermShiftForOneWeek(String shiftId, String employeeId, String clientId, String userId) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        GeneralQueryFormat query = this.getDeleteMasterShiftForOneWeek(shiftId, employeeId, clientId, userId);
        query.setCompany(companyId);
        try {
            conn.executeUpdate(query, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }
    
    public void editTempShift(String employeeId, String clientId, String override, String dayOfYear, String startT, String endT,
        String dayOfWeek, ShiftTypeClass myType, String scheduleId, String masterShiftId, String deleted, String shiftId,
        ShiftOptionsClass myPayOptions, ShiftOptionsClass myBillOptions, int rate_code, String userId) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        GeneralQueryFormat query = this.getEditTempShiftQuery(employeeId, clientId, override, dayOfYear, startT, endT, dayOfWeek, myType, scheduleId, masterShiftId, deleted, shiftId, myPayOptions, myBillOptions, rate_code);
        query.setUser(userId);
        query.setCompany(companyId);
        try {
            conn.executeUpdate(query, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    /**
     * Method used to update the last updated time for a temporary shift only,
     */
    public GeneralQueryFormat getTempShiftTimeUpdateQuery(String shiftId) {
        update_temp_shift_time myUpdateTimeQuery = new update_temp_shift_time();
        myUpdateTimeQuery.update(shiftId);
        return myUpdateTimeQuery;
    }
    
    /**
     * Method used to updated the last updated time for a master shift only, very basic
     */
    public GeneralQueryFormat updateMasterShiftTime(String masterShiftId) {
        update_master_shift_time myUpdateTimeQuery = new update_master_shift_time();
        myUpdateTimeQuery.update(masterShiftId);
        return myUpdateTimeQuery;
    }
    
    /**
     * Method used to delete this Master Shift for only one week. Just overwrites 
     * it with a blank shift...
     */
    public GeneralQueryFormat getDeleteMasterShiftForOneWeek(String shiftId, String employeeId, String clientId, String userId) {
        override_schedule_master_for_day_query myMasterOverwriteQuery = new override_schedule_master_for_day_query();
        myMasterOverwriteQuery.update(shiftId, employeeId, clientId);
        myMasterOverwriteQuery.setUser(userId);
        return myMasterOverwriteQuery;
    }
    
    /**
     * Create a temp shift from the info given...
     */
    public GeneralQueryFormat getTempShiftQuery(String employeeId, String clientId, String userId, String dayOfYear, String startT, String endT, Integer dayOfWeek,
            ShiftTypeClass myType, String scheduleId, String masterShiftId, ShiftOptionsClass myPayOptions, ShiftOptionsClass myBillOptions,
            Integer rate_code) {
        create_temporary_shift_query myUpdateQuery = new create_temporary_shift_query();
        myUpdateQuery.update(employeeId, clientId, "0", dayOfYear, startT, endT, dayOfWeek + "", myType.toString(), 
                scheduleId, masterShiftId, "0", "0", myPayOptions.toString(), myBillOptions.toString(), rate_code);
        myUpdateQuery.setUser(userId);
        return myUpdateQuery;
    }
    
    /**
     * Edits an existing temp shift....
     */
    public GeneralQueryFormat getEditTempShiftQuery(String employeeId, String clientId, String override, String dayOfYear, String startT, String endT,
        String dayOfWeek, ShiftTypeClass myType, String scheduleId, String masterShiftId, String deleted, String shiftId,
        ShiftOptionsClass myPayOptions, ShiftOptionsClass myBillOptions, int rate_code) {
        edit_temporary_shift_query myTempEditQuery =  new edit_temporary_shift_query();
        myTempEditQuery.update(employeeId, clientId, override, dayOfYear, startT, endT ,dayOfWeek + "", myType.toString(), 
                scheduleId, masterShiftId, deleted, shiftId, myPayOptions.toString(), myBillOptions.toString(), rate_code);
        return myTempEditQuery;

    }

    public ArrayList<ScheduleViewLog> getViewsForSchedule(Integer employeeId, Date startDate, Date endDate) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ScheduleViewLog> retVal = new ArrayList<ScheduleViewLog>();
        get_schedule_logs_query logsQuery = new get_schedule_logs_query();
        logsQuery.setPreparedStatement(new Object[]{employeeId, startDate, endDate});
        logsQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(logsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ScheduleViewLog(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public void saveViewForSchedule(ScheduleViewLog log) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_schedule_view_query saveQuery = new save_schedule_view_query();
        saveQuery.update(log);
        saveQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    @Override
    public String checkIn(Integer empId) throws SaveDataException {
        String error = "Error: ";
        
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -1);
        
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 1);
        
        Employee emp = new Employee(new Date());
        emp.setEmployeeId(empId);
        
        try {
            ArrayList<AssembleCheckinScheduleType> schedules = getScheduleForCheckin(startDate.getTime(), endDate.getTime(), 60, emp);
            if (schedules.isEmpty()) {
                return error + "No schedules";
            } else if (schedules.size() == 1) {
                AssembleCheckinScheduleType schedule = schedules.get(0);
                if (schedule.isCheckedIn()) {
                    return error + "Already checked in";
                }
                try {
                    this.checkInEmployee(schedule, emp, emp);
                    return "Success";
                } catch (Exception exe) {
                    return error + "Couldn't check in";
                }
            } else {
                return error + "Multiple schedules";
            }
        } catch (Exception exe) {
            return error + "Unknown Error"; 
        }
        
    }

    @Override
    public String checkOut(Integer empId) throws SaveDataException {
        String error = "Error: ";
        
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -1);
        
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 1);
        
        Employee emp = new Employee(new Date());
        emp.setEmployeeId(empId);
        
        try {
            ArrayList<AssembleCheckinScheduleType> schedules = getScheduleForCheckin(startDate.getTime(), endDate.getTime(), 60, emp);
            if (schedules.isEmpty()) {
                return error + "No schedules";
            } else if (schedules.size() == 1) {
                AssembleCheckinScheduleType schedule = schedules.get(0);
                if (schedule.isCheckedOut()) {
                    return error + "Already checked out";
                }
                try {
                    this.checkOut(schedule, emp, emp);
                    return "Success";
                } catch (Exception exe) {
                    return error + "Couldn't check in";
                }
            } else {
                return error + "Multiple schedules";
            }
        } catch (Exception exe) {
            return error + "Unknown Error"; 
        }
    }
}