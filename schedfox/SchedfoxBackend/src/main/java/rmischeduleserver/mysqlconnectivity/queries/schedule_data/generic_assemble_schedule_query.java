/*
 * generic_assemble_schedule_query.java
 *
 * Created on November 7, 2005, 11:12 AM
 *
 * Copyright: SchedFox 2005
 */
package rmischeduleserver.mysqlconnectivity.queries.schedule_data;

import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.*;
import java.io.*;
import rmischeduleserver.mysqlconnectivity.queries.util.GenericQuery;

/**
 *
 * @author Ira Juneau
 */
public class generic_assemble_schedule_query extends RunQueriesEx {

    public static final int SCHEDULE_ID = 1;
    public static final int CLIENT_ID = 2;
    public static final int EMPLOYEE_ID = 3;
    public static final int SCHED_DAY = 4;
    public static final int SCHED_START = 5;
    public static final int SCHED_END = 6;
    public static final int UPDATED = 7;
    public static final int DATE_START = 8;
    public static final int DATE_END = 9;
    public static final int GROUP = 10;
    public static final int PAY_OPT = 12;
    public static final int BILL_OPT = 13;
    public static final int RATE_CODE = 14;
    public static final int DELETED = 15;
    public static final int SCHED_DATE = 16;
    public static final int CLIENT_NAME = 17;
    public static final int TRAINER_ID = 19;
    public static final int TRAINER_NAME = 20;
    public static final int MASTER = 21;
    public static final int TYPE = 22;
    public static final int BRANCH = 23;
    public static final int TOTAL_HOURS = 24;
    public static final int DAYS_WORKED = 26;
    public static final int EXPORT_RATE_ID = 27;
    public static final int CLIENT_ADDRESS_1 = 28;
    public static final int CLIENT_CITY = 29;
    public static final int CLIENT_STATE = 30;
    public static final int CLIENT_ZIP = 31;
    public static final int CLIENT_PHONE = 32;
    public static final int CLIENT_PHONE_2 = 33;
    public static final int EMPLOYEE_NAME = 18;
    public static final int EMP_PHONE = 25;
    public static final int EMPLOYEE_ADDRESS = 34;
    public static final int EMPLOYEE_CITY = 35;
    public static final int EMPLOYEE_STATE = 36;
    public static final int EMPLOYEE_ZIP = 37;
    public static final int EMPLOYEE_CELL = 38;
    public transient ScheduleFields id = new ScheduleFields(SCHEDULE_ID, "schedule.schedule_id", "((schedule_master.schedule_master_id * -1) || '/' || a)", "sid"); //Pulled out (text(
    public transient ScheduleFields clientid = new ScheduleFields(CLIENT_ID, "schedule.client_id", "schedule_master.client_id", "cid");
    public transient ScheduleFields employeeid = new ScheduleFields(EMPLOYEE_ID, "schedule.employee_id", "schedule_master.employee_id", "eid");
    public transient ScheduleFields day = new ScheduleFields(SCHED_DAY, "schedule_day", "dow", "dow");
    public transient ScheduleFields start = new ScheduleFields(SCHED_START, "schedule_start", "schedule_master_start", "start_time");
    public transient ScheduleFields end = new ScheduleFields(SCHED_END, "schedule_end", "schedule_master_end", "end_time");
    public transient ScheduleFields updated = new ScheduleFields(UPDATED, "schedule_last_updated", "schedule_master_last_updated", "lu");
    public transient ScheduleFields dateS = new ScheduleFields(DATE_START, "'1'", "schedule_master_date_started", "sdate");
    public transient ScheduleFields dateE = new ScheduleFields(DATE_END, "'9'", "schedule_master_date_ended", "edate");
    public transient ScheduleFields group = new ScheduleFields(GROUP, "schedule_group", "schedule_master_group", "gp");
    public transient ScheduleFields masterId = new ScheduleFields(MASTER, "schedule.schedule_master_id", "0", "smid");
    public transient ScheduleFields type = new ScheduleFields(TYPE, "schedule.schedule_type", "schedule_master.schedule_master_type", "\"type\"");
    public transient ScheduleFields pay = new ScheduleFields(PAY_OPT, "schedule_pay_opt", "schedule_master_pay_opt", "pay_opt");
    public transient ScheduleFields bill = new ScheduleFields(BILL_OPT, "schedule_bill_opt", "schedule_master_bill_opt", "bill_opt");
    public transient ScheduleFields rate = new ScheduleFields(RATE_CODE, "schedule.rate_code_id", "schedule_master.rate_code_id", "rate_code_id");
    public transient ScheduleFields exprateId = new ScheduleFields(EXPORT_RATE_ID, "rate_code.usked_rate_code", "rate_code.usked_rate_code", "urc");
    public transient ScheduleFields deleted = new ScheduleFields(DELETED, "schedule.schedule_is_deleted", "(CASE WHEN (a < schedule_master_date_started OR a > schedule_master_date_ended) THEN 1 ELSE 0 END)", "isdeleted");
    public transient ScheduleFields date = new ScheduleFields(SCHED_DATE, "schedule_date", "a", "\"date\"");
    public transient ScheduleFields cname = new ScheduleFields(CLIENT_NAME, "(CASE WHEN client.client_name is NULL THEN cli.client_name ELSE client.client_name || ' ' || cli.client_name END)", "(CASE WHEN client.client_name is NULL THEN cli.client_name ELSE client.client_name || ' ' || cli.client_name END)", "cname");
    public transient ScheduleFields ename = new ScheduleFields(EMPLOYEE_NAME, "(employee.employee_last_name || ', ' || employee.employee_first_name)", "(employee.employee_last_name || ', ' || employee.employee_first_name)", "ename");
    public transient ScheduleFields trainId = new ScheduleFields(TRAINER_ID, "employee_trained.training_employee_id", "0", "trainerid");
    public transient ScheduleFields trainName = new ScheduleFields(TRAINER_NAME, "(SELECT (employee.employee_last_name || ', ' || employee.employee_first_name) FROM employee WHERE employee.employee_id = employee_trained.training_employee_id)", "''", "trainer");
    public transient ScheduleFields branch = new ScheduleFields(BRANCH, "cli.branch_id", "cli.branch_id", "branch_id");
    public transient ScheduleFields totalHours = new ScheduleFields(TOTAL_HOURS, "(SUM ((CASE WHEN (a < schedule_master_date_started OR a > schedule_master_date_ended) THEN schedule_master_start WHEN schedule_master_end < schedule_master_start THEN schedule_master_end + 1440 ELSE schedule_master_end END) - schedule_master_start)) ", "(SUM ((CASE WHEN schedule_is_deleted = 1 THEN schedule_start WHEN schedule_end < schedule_start THEN schedule_end + 1440 ELSE schedule_end END) - schedule_start))", "total_time");
    public transient ScheduleFields employeephone = new ScheduleFields(EMP_PHONE, "employee.employee_phone", "employee.employee_phone", "phone");
    public transient ScheduleFields daysWorked = new ScheduleFields(DAYS_WORKED, "(date_part('day', NOW() - employee.employee_hire_date))", "(date_part('day', NOW() - employee.employee_hire_date))", "days_employed");
    public transient ScheduleFields clientAddress = new ScheduleFields(CLIENT_ADDRESS_1, "cli.client_address", "cli.client_address", "client_address");
    public transient ScheduleFields clientCity = new ScheduleFields(CLIENT_CITY, "cli.client_city", "cli.client_city", "client_city");
    public transient ScheduleFields clientState = new ScheduleFields(CLIENT_STATE, "cli.client_state", "cli.client_state", "client_state");
    public transient ScheduleFields clientZip = new ScheduleFields(CLIENT_ZIP, "cli.client_zip", "cli.client_zip", "client_zip");
    public transient ScheduleFields clientPhone = new ScheduleFields(CLIENT_PHONE, "cli.client_phone", "cli.client_phone", "client_phone");
    public transient ScheduleFields clientPhone2 = new ScheduleFields(CLIENT_PHONE_2, "cli.client_phone2", "cli.client_phone2", "client_phone2");
    public transient ScheduleFields employeeaddress = new ScheduleFields(EMPLOYEE_ADDRESS, "employee.employee_address", "employee.employee_address", "employee_address");
    public transient ScheduleFields employeecity = new ScheduleFields(EMPLOYEE_CITY, "employee.employee_city", "employee.employee_city", "employee_city");
    public transient ScheduleFields employeestate = new ScheduleFields(EMPLOYEE_STATE, "employee.employee_state", "employee.employee_state", "employee_state");
    public transient ScheduleFields employeezip = new ScheduleFields(EMPLOYEE_ZIP, "employee.employee_zip", "employee.employee_zip", "employee_zip");
    public transient ScheduleFields employeecell = new ScheduleFields(EMPLOYEE_CELL, "employee.employee_cell", "employee.employee_cell", "employee_cell");
    private Hashtable<Integer, ScheduleFields> myFieldHash;
    private int[] mySelectedFields = {SCHEDULE_ID, CLIENT_ID, CLIENT_NAME, EMPLOYEE_NAME, MASTER, EMPLOYEE_ID, SCHED_DAY, SCHED_START, SCHED_END, UPDATED, DATE_START, DATE_END, GROUP, PAY_OPT, BILL_OPT, RATE_CODE, SCHED_DATE, TRAINER_ID, DELETED, TYPE};
    private int[] mySelectedHeartbeatFields = {BRANCH, SCHEDULE_ID, CLIENT_ID, MASTER, EMPLOYEE_ID, SCHED_DAY, SCHED_START, SCHED_END, UPDATED, DATE_START, DATE_END, GROUP, PAY_OPT, BILL_OPT, RATE_CODE, SCHED_DATE, TRAINER_ID, DELETED, TYPE, EMPLOYEE_NAME, CLIENT_NAME};
    private int[] orderByFields = {CLIENT_ID, EMPLOYEE_ID};
    protected int[] genericOrderFields = {CLIENT_NAME, EMPLOYEE_NAME};
    private String ClientsToInclude;
    private String EmployeesToInclude;
    public String Sdate;
    public String Edate;
    private String Type;
    private String lu;
    protected boolean ShowDeleted;
    private boolean showOpenEmps;
    private boolean hasTrainingFields;
    private boolean hasRateCode;
    protected String emps = "";
    protected String employee_id = "0";
    protected String empsM = "";
    protected transient int[] myExportFields = {EXPORT_RATE_ID, SCHEDULE_ID, CLIENT_ID, CLIENT_NAME, EMPLOYEE_NAME, MASTER, EMPLOYEE_ID, SCHED_DAY, SCHED_START, SCHED_END, UPDATED, DATE_START, DATE_END, GROUP, PAY_OPT, BILL_OPT, RATE_CODE, SCHED_DATE, TRAINER_ID, DELETED, TYPE};
    protected transient int[] myReportFields = {TRAINER_NAME, SCHEDULE_ID, CLIENT_ID, MASTER, EMPLOYEE_ID, SCHED_DAY, SCHED_START, SCHED_END, SCHED_DATE, DELETED, TYPE, CLIENT_NAME, EMPLOYEE_NAME, CLIENT_ADDRESS_1, CLIENT_CITY, CLIENT_STATE, CLIENT_ZIP};
    protected transient int[] myTrainingReportFields = {TRAINER_NAME, SCHEDULE_ID, CLIENT_ID, MASTER, EMPLOYEE_ID, SCHED_DAY, SCHED_START, SCHED_END, SCHED_DATE, DELETED, TYPE, CLIENT_NAME, EMPLOYEE_NAME};
    protected transient int[] myCheckInFields = {SCHEDULE_ID, CLIENT_ID, MASTER, EMPLOYEE_ID, SCHED_DAY, SCHED_START, SCHED_END, SCHED_DATE, DELETED, CLIENT_NAME, EMPLOYEE_NAME, BRANCH, CLIENT_ADDRESS_1, CLIENT_CITY, CLIENT_STATE, CLIENT_ZIP, CLIENT_PHONE, CLIENT_PHONE_2, EMPLOYEE_ADDRESS, EMPLOYEE_CITY, EMPLOYEE_STATE, EMPLOYEE_ZIP, EMPLOYEE_CELL, EMP_PHONE};
    protected transient int[] myOverUnderReportFields = {SCHEDULE_ID, DELETED, CLIENT_ID, MASTER, EMPLOYEE_ID, SCHED_DAY, SCHED_START, SCHED_END, SCHED_DATE, EMPLOYEE_NAME, DAYS_WORKED, EMP_PHONE};
    protected int[] myReportOrder = {CLIENT_NAME, EMPLOYEE_NAME};
    protected int[] myCheckinOrder = {SCHED_START, SCHED_END, CLIENT_NAME, EMPLOYEE_NAME};
    protected int[] myEmployeeReportOrder = {EMPLOYEE_NAME, CLIENT_NAME};
    protected int[] myClientReportOrder = {CLIENT_NAME, EMPLOYEE_NAME};

    /** Creates a new instance of generic_assemble_schedule_query */
    public generic_assemble_schedule_query() {
        myReturnString = "";
        initializeHash();
    }

    /**
     * Used to specify what fields you want returned...
     */
    public generic_assemble_schedule_query(int[] fieldsToReturn) {
        myReturnString = "";
        initializeHash();
        mySelectedFields = fieldsToReturn;

    }

    public void setSelectedFields(int[] fields) {
        mySelectedFields = fields;
    }

    /**
     * Force it to cycle through all of our queries in getAt
     * @return
     */
    @Override
    public int getSize() {
        return 1;
    }

    /**
     * Entry point of this query
     *
     */
    @Override
    public String getAt(int i) {
        return getFinalSelectStatement();
    }

    public GeneralQueryFormat getQueryAt(int i) {
        GeneralQueryFormat retVal = new GenericQuery("");
        retVal = new GenericQuery(getFinalSelectStatement());
        retVal.setBranch(this.getBranch());
        retVal.setCompany(this.getCompany());
        retVal.setDriver(this.getDriver());
        retVal.setMD5(this.getMD5());
        retVal.setLastUpdated(this.getLastUpdated());
        retVal.setManagement(this.getManagementSchema());
        retVal.setManagementId(this.getManagementId());
        retVal.setRanTime(this.getRanTime());
        retVal.setUser(this.getUser());
        return retVal;
    }

    /**
     * Set fields to order by...
     */
    public void setOrderByFields(int[] orderby) {
        orderByFields = orderby;
    }

    private void initializeHash() {
        hasTrainingFields = false;
        hasRateCode = false;
        myFieldHash = new Hashtable();
        myFieldHash.put(id.field, id);
        myFieldHash.put(clientid.field, clientid);
        myFieldHash.put(employeeid.field, employeeid);
        myFieldHash.put(day.field, day);
        myFieldHash.put(start.field, start);
        myFieldHash.put(end.field, end);
        myFieldHash.put(updated.field, updated);
        myFieldHash.put(dateS.field, dateS);
        myFieldHash.put(dateE.field, dateE);
        myFieldHash.put(group.field, group);
        myFieldHash.put(pay.field, pay);
        myFieldHash.put(bill.field, bill);
        myFieldHash.put(rate.field, rate);
        myFieldHash.put(deleted.field, deleted);
        myFieldHash.put(date.field, date);
        myFieldHash.put(cname.field, cname);
        myFieldHash.put(ename.field, ename);
        myFieldHash.put(trainId.field, trainId);
        myFieldHash.put(trainName.field, trainName);
        myFieldHash.put(masterId.field, masterId);
        myFieldHash.put(type.field, type);
        myFieldHash.put(branch.field, branch);
        myFieldHash.put(totalHours.field, totalHours);

        myFieldHash.put(daysWorked.field, daysWorked);
        myFieldHash.put(exprateId.field, exprateId);
        myFieldHash.put(clientAddress.field, clientAddress);
        myFieldHash.put(clientCity.field, clientCity);
        myFieldHash.put(clientState.field, clientState);
        myFieldHash.put(clientZip.field, clientZip);
        myFieldHash.put(clientPhone.field, clientPhone);
        myFieldHash.put(clientPhone2.field, clientPhone2);

        myFieldHash.put(employeephone.field, employeephone);
        myFieldHash.put(employeezip.field, employeezip);
        myFieldHash.put(employeecell.field, employeecell);
        myFieldHash.put(employeecity.field, employeecity);
        myFieldHash.put(employeestate.field, employeestate);
        myFieldHash.put(employeeaddress.field, employeeaddress);
    }

    public String generateOrderBy() {
        StringBuilder myBuilder = new StringBuilder();
        if (orderByFields.length > 0) {
            myBuilder.append("ORDER BY ");
        }
        for (int i = 0; i < orderByFields.length; i++) {
            myBuilder.append(myFieldHash.get(orderByFields[i]).asValue + ",");
        }
        try {
            myBuilder.deleteCharAt(myBuilder.length() - 1);
        } catch (Exception e) {
        }
        return myBuilder.toString();
    }

    public boolean hasAccess() {
        return true;
    }

    protected String generateCompleteWhereClause() {
        return "";
    }

    protected String getEmployeesToIncludeString() {
        String retVal = "null::integer[]";
        if (this.EmployeesToInclude.trim().length() > 0) {
            retVal = "Array[" + this.EmployeesToInclude.trim().replace('(', ' ').replace(')', ' ') + "]";
        }
        return retVal;
    }
    
    protected String getClientsToIncludeString() {
        String retVal = "null::integer[]";
        if (this.ClientsToInclude.trim().length() > 0) {
            retVal = "Array[" + this.ClientsToInclude.trim().replace('(', ' ').replace(')', ' ') + "]";
        }
        return retVal;
    }

    /**
     * Overload to do funky things like a join on this or so on...
     */
    protected String getFinalSelectStatement() {
        String branchId = this.getBranch();
        if (branchId.equals("<branch>")) {
            branchId = "-1";
        }
        if (this.getLastUpdated() == null) {
            return "SELECT " + getBasicSelect() + additionalFields() + " FROM assemble_schedule(DATE('" + Sdate + "'), DATE('" + Edate + "'), " + branchId + ", " + this.getClientsToIncludeString() + ", " + this.getEmployeesToIncludeString() + ") " + generateCompleteWhereClause() + generateOrderBy();
        } else {
            return "SELECT " + getBasicSelect() + additionalFields() + " FROM assemble_schedule(DATE('" + Sdate + "'), DATE('" + Edate + "'), " + branchId + ", -1, (SELECT TIMESTAMP WITH TIME ZONE '" + this.getLastUpdated() + "'), " + this.getClientsToIncludeString() + ") " + generateCompleteWhereClause() + generateOrderBy();
        }
    }

    
    public String getBasicSelect() {
        return "assemble_schedule.* ";
    }

    @Override
    public String toString() {
        return getFinalSelectStatement();
    }

    
    protected String additionalFields() {
        return "";
    }

    /**
     * Used by our hearbeat...also rearranges by updated field so that all clients will get updates exactly how they
     * should!
     */
    @Override
    public void updateTime(String LU) {
        lu = LU;
        orderByFields = new int[1];
        orderByFields[0] = UPDATED;
        mySelectedFields = mySelectedHeartbeatFields;
    }

    /**
     * Overload with return "> 0"; if you want to return info for all branches like in the checkin
     */
    protected String generateBranch() {
        return "cli.branch_id = " + getBranch() + " ";
    }

    /**
     * Generates Type SQL
     */
    protected String generateTypeSQL(String mytype, boolean isMaster) {
        if (mytype.length() > 0) {
            if (isMaster) {
                return "(" + type.masterField + " = " + mytype + ")";
            } else {
                return "(" + type.scheduleField + " = " + mytype + ")";
            }
        }
        return " ";
    }

    /**
     * Generates Last Updated SQL, is protected so child classes can use to modify this class behaviour
     */
    protected String generateLastUpdateSQL(String dt, String fieldToCheck) {
        if (dt.length() > 0) {
            return "( " + fieldToCheck + " >= '" + dt + "') ";
        }
        return "";
    }

    /**
     * Takes comma delimited List and generates SQL for it....fun fun
     */
    protected String generateToIncludeSQL(String listToInclude, String fieldToCheck) {
        String returnString = "";
        if (listToInclude.length() > 0) {
            returnString = "" + fieldToCheck + " IN (" + listToInclude + ") ";
        }
        return returnString;
    }

    protected String generateDeletedSQL(boolean showDeleted) {
        //return " AND (employee_is_deleted = 0 OR employee_is_deleted = 1 OR employee_is_deleted IS NULL) ";
        return "";
    }

    protected String generateDeletedClientSQL(boolean showDeleted) {
        return " cli.client_is_deleted = 0 ";
    }

    protected class ScheduleFields implements Serializable {

        public String scheduleField;
        public String masterField;
        public int field;
        public String asValue;

        public ScheduleFields(int fieldNumber, String schedVal, String masterVal, String asVal) {
            field = fieldNumber;
            asValue = asVal;
            scheduleField = schedVal;
            masterField = masterVal;
        }
    }

    /**
     * Our update method
     */
    public void update(String clientsToInclude, String employeesToInclude, String employee_id, String sdate, String edate, String type, String lastupdated, boolean showDeleted) {
        ClientsToInclude = clientsToInclude;
        EmployeesToInclude = employeesToInclude;
        Sdate = sdate;
        Edate = edate;
        Type = type;
        lu = lastupdated;
        ShowDeleted = showDeleted;
        this.employee_id = employee_id;
    }


    /**
     * Our update method
     */
    public void update(String clientsToInclude, String employeesToInclude, String sdate, String edate, String type, String lastupdated, boolean showDeleted) {
        ClientsToInclude = clientsToInclude;
        EmployeesToInclude = employeesToInclude;
        Sdate = sdate;
        Edate = edate;
        Type = type;
        lu = lastupdated;
        ShowDeleted = showDeleted;
    }
}
