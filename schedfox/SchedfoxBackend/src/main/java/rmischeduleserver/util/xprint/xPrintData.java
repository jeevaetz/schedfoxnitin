/*
 * xPrintData.java
 *
 * Created on February 1, 2005, 11:02 AM
 */
package rmischeduleserver.util.xprint;

import java.text.SimpleDateFormat;
import java.util.*;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.client.client_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_query;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.generic_assemble_schedule_query;
import rmischeduleserver.util.StaticDateTimeFunctions;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class xPrintData implements JRDataSource {

    public static int SORT_BY_CLIENT = 1;
    public static int SORT_BY_EMPLOYEE = 2;
    private Vector<xPrintIndividualData> myArray = new Vector<xPrintIndividualData>();
    public HashMap<String, Integer> clientsHash;
    public HashMap<String, Integer> employeeHash;
    private HashMap<String, Integer> scheduleHash;
    public PClient[] clientsArray;
    public PEmployee[] employeesArray;
    private int currPos;
    public String lu;
    public int sortType;
    boolean firstRun = true;
    private boolean initialized = false;
    private boolean hasData = false;
    //Information to cache
    private generic_assemble_schedule_query assembledScheduleQ;
    private client_query clientsQ;
    private employee_query employeesQ;
    private RMIScheduleServerImpl myConn;
    private Calendar start;
    private Calendar end;
    private String companyId;
    private String branchId;

    /**
     * Creates a new instance of xPrintData
     */
    public xPrintData(generic_assemble_schedule_query assembledScheduleQ, client_query clientsQ, employee_query employeesQ, String startDate, String endDate, RMIScheduleServerImpl myConn, String companyId, String branchId) {
        start = StaticDateTimeFunctions.setCalendarToString(startDate);
        end = StaticDateTimeFunctions.setCalendarToString(endDate);
        
        currPos = 0;
        sortType = SORT_BY_CLIENT;
        this.assembledScheduleQ = assembledScheduleQ;
        this.clientsQ = clientsQ;
        this.employeesQ = employeesQ;
        this.myConn = myConn;

        this.companyId = companyId;
        this.branchId = branchId;
    }

    public boolean hasData() {
        return this.hasData;
    }

    public int getCompany() {
        return Integer.parseInt(companyId);
    }

    /**
     * Serializes the important data to a hashmap so we can transmit it to a
     * reporting servlet.
     *
     * @param hash
     */
    public void addEntriesToHashMap(HashMap hash) {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy");
        hash.put("xPrintDataStartCal", myFormat.format(start.getTime()));
        hash.put("xPrintDataEndCal", myFormat.format(end.getTime()));
        hash.put("xPrintDataSortType", sortType);
        hash.put("xPrintDataScheduleQuery", this.assembledScheduleQ);
        hash.put("xPrintDataClientQuery", this.clientsQ);
        hash.put("xPrintDataEmployeeQuery", this.employeesQ);
        hash.put("xPrintDataCompany", companyId);
        hash.put("xPrintDataCompany", branchId);
    }

    private void initialize() {
        if (!initialized) {
            initialized = true;
            assembledScheduleQ.setCompany(this.companyId);
            clientsQ.setCompany(this.companyId);
            employeesQ.setCompany(this.companyId);

            Record_Set assembledSchedule = new Record_Set();
            Record_Set clients = new Record_Set();
            Record_Set employees = new Record_Set();
            ArrayList<Record_Set> myRecords = new ArrayList(8);
            RunQueriesEx myQueryEx = new RunQueriesEx();
            myQueryEx.update(assembledScheduleQ, clientsQ, employeesQ);
            myQueryEx.setCompany(this.companyId);
            if (this.branchId != null && this.branchId.length() > 0) {
                myQueryEx.setBranch(branchId);
                assembledScheduleQ.setBranch(branchId);
                clientsQ.setBranch(branchId);
                employeesQ.setBranch(branchId);
            }
            try {
                myRecords = myConn.executeQueryEx(myQueryEx, "");
            } catch (Exception e) {
            }
            for (int i = 0; i < myRecords.size(); i++) {
                myRecords.get(i).decompressData();
            }
            assembledSchedule = myRecords.get(0);
            clients = myRecords.get(1);
            employees = myRecords.get(2);
            int size = assembledSchedule.length();
            lu = assembledSchedule.lu;
            myArray = new Vector(size);

            assembledSchedule.moveToFront();
            placeEmployeeAndClientInfo(clients, employees);
            int i = 0;
            do {
                try {
                    int hashValue = this.clientsHash.get(assembledSchedule.getString("cid"));
                    PClient client = this.clientsArray[hashValue];
                    try {
                        String empId = assembledSchedule.getString("eid");
                        PEmployee employee = this.employeesArray[this.employeeHash.get(empId)];
                        myArray.add(new xPrintIndividualData(assembledSchedule, start, end, this, client, employee));
                        hasData = true;
                    } catch (Exception exe) {
                        PEmployee employee = this.employeesArray[hashValue];
                        myArray.add(new xPrintIndividualData(assembledSchedule, start, end, this, client, employee));
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    assembledSchedule.moveNext();
                }
                i++;
            } while (!assembledSchedule.getEOF());
            Collections.sort(myArray);
        }
    }

    /**
     * If Current Position in Day Vector has multiple weeks then increment that
     * otherwise move on To new position...
     */
    public void moveNext() {
        this.initialize();
        if (currPos < myArray.size()) {
            if (!((xPrintIndividualData) myArray.get(currPos)).nextRow()) {
                currPos++;
                try {
                    //If does not have data or client is not in list movenext....
                    if (!(myArray.get(currPos)).hasData || getClientIndex() < 0) {
                        moveNext();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isEnd() {
        if (currPos >= myArray.size()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBeggining() {
        this.initialize();
        if (currPos == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setSortType(int sort) {
        sortType = sort;
        Collections.sort(myArray);
    }

    public void setToBOF() {
        currPos = 0;
        for (int i = 0; i < myArray.size(); i++) {
            (myArray.get(i)).movePrevious();
        }
    }

    public void setToEOF() {
        currPos = myArray.size();
    }

    public void movePrevious() {
        if (currPos > 0) {
            try {
                (myArray.get(currPos)).movePrevious();
            } catch (Exception e) {
            }
            currPos--;
        }
    }

    public String getEmployeeId() {
        return (myArray.get(currPos)).eid;
    }

    public String getClientId() {
        return (myArray.get(currPos)).cid;
    }

    public String getShiftId() {
        if (Integer.parseInt(myArray.get(currPos).shiftid) > 0) {
            return (myArray.get(currPos)).shiftid;
        } else {
            return (-(Integer.parseInt((myArray.get(currPos)).shiftid))) + "";
        }
    }

    public String getRealShiftId() {
        return (myArray.get(currPos)).shiftid;
    }

    public String getEmployeePhone() {
        int i = getEmployeeIndex();
        if (i >= 0) {
            return employeesArray[i].getPhone();
        } else {
            return "";
        }
    }

    public String getEmployeeCell() {
        int i = getEmployeeIndex();
        if (i >= 0) {
            return employeesArray[i].getPhone2();
        } else {
            return "";
        }
    }

    public String getEmployeeGroup() {
        int i = getEmployeeIndex();
        if (i >= 0) {
            return employeesArray[i].getDel();
        } else {
            return "";
        }
    }

    public String getEmployeeName() {
        int i = getEmployeeIndex();
        if (i >= 0) {
            return employeesArray[i].getName();
        } else {
            return "";
        }
    }

    public String getEmployeeAddress() {
        int i = getEmployeeIndex();
        if (i >= 0) {
            return employeesArray[i].getAddress();
        } else {
            return "";
        }
    }

    public String getEmployeeCity() {
        int i = getEmployeeIndex();
        if (i >= 0) {
            return employeesArray[i].getCity();
        } else {
            return "";
        }
    }

    public String getEmployeeState() {
        int i = getEmployeeIndex();
        if (i >= 0) {
            return employeesArray[i].getState();
        } else {
            return "";
        }
    }

    public String getEmployeeZip() {
        int i = getEmployeeIndex();
        if (i >= 0) {
            return employeesArray[i].getZip();
        } else {
            return "";
        }
    }

    public String getClientPhone() {
        try {
            return clientsArray[getClientIndex()].getPhone();
        } catch (Exception e) {
        }
        return "";
    }

    public String getClientAddress() {
        try {
            return clientsArray[getClientIndex()].getAddress();
        } catch (Exception e) {
        }
        return "";
    }

    public String getClientCity() {
        try {
            return clientsArray[getClientIndex()].getCity();
        } catch (Exception e) {
        }
        return "";
    }

    public String getClientState() {
        try {
            return clientsArray[getClientIndex()].getState();
        } catch (Exception e) {
        }
        return "";
    }

    public String getClientZip() {
        try {
            return clientsArray[getClientIndex()].getZip();
        } catch (Exception e) {
        }
        return "";
    }

    public String getClientName() {
        try {
            return clientsArray[getClientIndex()].getName();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public Vector<xPrintIndividualData> getData() {
        this.initialize();
        return myArray;
    }

    public int getSortType() {
        return this.sortType;
    }

    private String getComparator(int position) {
        if (this.sortType == xPrintData.SORT_BY_CLIENT) {
            return this.myArray.get(position).cid;
        } else {
            return this.myArray.get(position).eid;
        }
    }

    public xPrintSchedule getCurrentData() {
        int offset = 0;
        xPrintSchedule schedule = new xPrintSchedule(this);
        while (currPos + offset <= myArray.size() - 1
                && getComparator(currPos).equals(getComparator(currPos + offset))) {
            currPos += offset;
            schedule.addIndividualData(myArray.get(currPos));
            currPos -= offset;
            offset++;
        }
        currPos += offset - 1;
        return schedule;
    }

    /**
     * Is their more data in their particular schedule?
     *
     * @return
     */
    public Boolean hasMoreData() {
        this.initialize();
        return currPos < myArray.size() - 1 || (firstRun && myArray.size() >= 1);
    }

    public boolean next() {
        this.initialize();
        boolean retVal = false;
        if (hasMoreData()) {
            if (this.firstRun) {
                this.firstRun = false;
            } else {
                this.moveNext();
            }
            retVal = true;
        }
        return retVal;
    }

    public Object getFieldValue(JRField jrField) throws JRException {
        Object retVal = null;
        this.initialize();
        try {
            if (jrField.getName().equals("schedule")) {
                try {
                    return this.getCurrentData(); 
               } catch (Exception exe) {
                    return null;
                }
            } else if (jrField.getName().equals("employee_name")) {
                return this.getEmployeeName();
            } else if (jrField.getName().equals("client_name")) {
                return this.getClientName();
            } else if (jrField.getName().equals("employee_address")) {
                return this.getEmployeeAddress();
            } else if (jrField.getName().equals("employee_city")) {
                return this.getEmployeeCity();
            } else if (jrField.getName().equals("employee_state")) {
                return this.getEmployeeState();
            } else if (jrField.getName().equals("employee_zip")) {
                return this.getEmployeeZip();
            } else if (jrField.getName().equals("client_address")) {
                return this.getClientAddress();
            } else if (jrField.getName().equals("client_city")) {
                return this.getClientCity();
            } else if (jrField.getName().equals("client_state")) {
                return this.getClientState();
            } else if (jrField.getName().equals("client_zip")) {
                return this.getClientZip();
            } else if (jrField.getName().equals("client_phone")) {
                return this.getClientPhone();
            }
        } catch (Exception e) {
            return "";
        }
        return retVal;
    }

    public xPrintDay getDayOfWeek(int daycode) {
        return (myArray.get(currPos)).days.get(daycode);
    }

    public int getWeekLength() {
        try {
            return (myArray.get(currPos)).days.size();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Get week length in XPrintData going past array length...");
            return 0;
        }
    }

    private int getEmployeeIndex() {
        return employeeHash.get((myArray.get(currPos)).eid);
    }

    private int getClientIndex() {
        return clientsHash.get((myArray.get(currPos)).cid);
    }

    public void placeEmployeeAndClientInfo(Record_Set clients, Record_Set employees) {
        int clientlength = clients.length();
        int employeelength = employees.length();
        //Build our string arrays that will contain all client and employee data...
        clientsArray = new PClient[clientlength];
        employeesArray = new PEmployee[employeelength + 1];

        //Build our hash tables
        employeeHash = new HashMap<String, Integer>();
        clientsHash = new HashMap<String, Integer>();

        PEmployee openEmp = new PEmployee();
        openEmp.setData("0", "", "", "", "", "", "", "", "", "0");
        employeesArray[0] = openEmp;
        employeeHash.put("0", 0);
                            ;
        for (int i = 0; i < employeelength; i++) {
            employeesArray[i + 1] = new PEmployee();
            employeeHash.put(employees.getString("id"), i + 1);
            employeesArray[i + 1].setData(employees.getString("id"),
                    employees.getString("name"),
                    employees.getString("phone"),
                    employees.getString("cell"),
                    employees.getString("address"),
                    employees.getString("address2"),
                    employees.getString("city"),
                    employees.getString("state"),
                    employees.getString("zip"),
                    employees.getString("del"));
            employees.moveNext();
        }

        //Create clientHash using id value as key and binding it to the index in the array
        for (int i = 0; i < clientlength; i++) {
            clientsHash.put(clients.getString("id"), i);
            clientsArray[i] = new PClient();
            clientsArray[i].setData(clients.getString("id"),
                    clients.getString("client_name"),
                    clients.getString("address"),
                    clients.getString("city"),
                    clients.getString("state"),
                    clients.getString("zip"),
                    clients.getString("phone"));
            clients.moveNext();
        }
    }

    @Override
    public String toString() {
        StringBuilder myReturnString = new StringBuilder();
        myReturnString.append("\n");
        for (int x = 0; x < myArray.size(); x++) {
            myReturnString.append((myArray.get(x)).toString() + "\n");
        }
        return myReturnString.toString();
    }

    public String getDataString() {
        this.initialize();
        StringBuilder val = new StringBuilder();
        for (int x = 0; x < myArray.size(); x++) {
            val.append("<br/>" + this.myArray.get(x).getClient().getName() + "<br/>");
            for (int i = 0; i < myArray.get(x).days.size(); i++) {
                String day = myArray.get(x).days.get(i).toString().substring(0, 11);
                String shift = this.timeFormatter(myArray.get(x).days.get(i).toString().substring(12));
                val.append("&nbsp;&nbsp;&nbsp;&nbsp;" + day + " " + shift + "<br/>");
            }
        }

        return val.toString();
    }

    private String timeFormatter(String shift) {
        String val = new String();
        if (!shift.contains(",")) {
            String[] temp = shift.split("-");
            for (int i = 0; i < temp.length; i++) {
                temp[i] = temp[i].replaceAll("\\[", "").trim();
                temp[i] = temp[i].replaceAll("]", "").trim();
            }
            try {
                String startTime = getTime(Integer.parseInt(temp[0]));
                String endTime = getTime(Integer.parseInt(temp[1]));
                val += " " + startTime + " - " + endTime;
            } catch (Exception e) {
                return "OFF";
            }
        } else {
            String[] temp = shift.split("-");
            for (int i = 0; i < temp.length; i++) {
                temp[i] = temp[i].replaceAll("\\[", "").trim();
                temp[i] = temp[i].replaceAll("]", "").trim();
            }
            String[] startTimes = temp[0].split(",");
            String[] endTimes = temp[1].split(",");
            for (int j = 0; j < startTimes.length; j++) {
                String startTime = getTime(Integer.parseInt(startTimes[j].trim()));
                String endTime = getTime(Integer.parseInt(endTimes[j].trim()));
                if (j != 0) {
                    val += "\t\t";
                }
                val += " " + startTime + " - " + endTime;
                if (j != startTimes.length - 1) {
                    val += "\n";
                }
            }
        }
        return val;
    }

    private String getTime(int time) {
        String meridiem;
        String val = new String();
        int hrs = time / 60;
        if (hrs >= 12) {
            if (hrs != 12 && hrs != 24) {
                meridiem = " pm";
            } else {
                meridiem = " am";
            }
            hrs = hrs - 12;
        } else {
            meridiem = " am";
        }
        int mins = time % 60;
        String min;
        if (mins < 10) {
            min = mins + "0";
        } else {
            min = Integer.toString(mins);
        }
        val = hrs + ":" + min + meridiem;
        return val;
    }
}
