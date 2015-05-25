/*
 * xNewPrintData.java
 *
 * Created on December 19, 2005, 9:29 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.xprint.xdata;
import rmischeduleserver.util.StaticDateTimeFunctions;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.data_connection_types.*;
import rmischedule.data_connection.*;
import rmischedule.components.*;

import java.util.*;
import rmischedule.schedule.print.ExtendedEmployeeReport.CalendarForSingleMonth;
/**
 *
 * @author Ira Juneau
 */
public class xNewPrintData {
    
    private TreeSet<xNewPrintDay> orderByEmployeeClientAndDates;
    private TreeSet<client> listOfClients;
    private TreeSet<employee> listOfEmployees;
    private String[] startWeeks;
    private String[] endWeeks;
    private ArrayList<CalendarForSingleMonth> myCalendarObjects;
    private int currCalendar;

    private int companyId;

    /**
     * Creates a new instance of xNewPrintData 
     * You must pass in a schedule query that follows the following conventions for this to
     * work, it must contain the following information. As defined in the xNewPrintDay 
     * constructor.
     *
     * Client Name, Employee Name, Date, Start Time, End Time, ShiftId
     */
    public xNewPrintData(GeneralQueryFormat myQuery, Connection myConnection, Calendar start, Calendar end) {
        ArrayList myCompleteData = new ArrayList();
        RunQueriesEx myCompQuery = new RunQueriesEx();
        myCalendarObjects = new ArrayList();
        orderByEmployeeClientAndDates = new TreeSet(new comparatorByEmpCliDate());
        listOfEmployees = new TreeSet(new uniqueEmployees());
        listOfClients = new TreeSet(new uniqueClients());
        currCalendar = 0;
        this.companyId = Integer.parseInt(myConnection.myCompany);
        try {
            myConnection.prepQuery(myQuery);
            myCompQuery.add(myQuery);
            myCompleteData = myConnection.executeQueryEx(myCompQuery);
        } catch (Exception e) {}
        Record_Set myData = (Record_Set)myCompleteData.get(0);
        for (int i = 0; i < myData.length(); i++) {
            xNewPrintDay myNewDay = new xNewPrintDay(myData, this.companyId);
            orderByEmployeeClientAndDates.add(myNewDay);
            client myNewClient = new client(myData.getString("cname"), myData.getString("client_address"), myData.getString("client_city"), myData.getString("client_state"), myData.getString("client_zip"));
            employee myNewEmp = new employee(myData.getString("ename"));
            listOfClients.add(myNewClient);
            listOfEmployees.add(myNewEmp);
            myData.moveNext();
        }
        Calendar trueStart = StaticDateTimeFunctions.getBegOfWeek(start, Integer.parseInt(myConnection.myCompany));
        Calendar trueEnd = StaticDateTimeFunctions.getEndOfWeek(end, Integer.parseInt(myConnection.myCompany));
        int monthDiff = trueEnd.get(Calendar.MONTH) - trueStart.get(Calendar.MONTH);
        int yearDiff = trueEnd.get(Calendar.YEAR) - trueStart.get(Calendar.YEAR);
        if (monthDiff < 0) {
            monthDiff = (12 * yearDiff) + monthDiff;
        }
//        startWeeks = new String[monthDiff];
//        endWeeks = new String[monthDiff];
//        for (int i = 0; i < monthDiff; i++) {
//            startWeeks[i] = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(trueStart);
//            trueStart.add(Calendar.MONTH, 1);
//            trueStart.add(Calendar.DAY_OF_YEAR, -1);
//            endWeeks[i] = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(trueStart);
//            trueStart.add(Calendar.DAY_OF_YEAR, 1);
//            System.out.println("Week " + i + " " + startWeeks[i] + " - " + endWeeks[i]);
//        }
//        //Adjust final date correctly
//        endWeeks[endWeeks.length - 1] = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(trueEnd);
        startWeeks = new String[1];
        endWeeks = new String[1];
        startWeeks[0] = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(trueStart);
        endWeeks[0] = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(trueEnd);
        createCalendarObjectsForReports();
    }
    
    /**
     * Creates our calendar objects then our report will just go through this list adding
     * whatever it can fit to each report form...much easier than other ways of doing things
     */
    private void createCalendarObjectsForReports() {
        Iterator<employee> employeeIterator = listOfEmployees.iterator();
        while(employeeIterator.hasNext()) {
            String empName = employeeIterator.next().empName;
            for (int i = 0; i < startWeeks.length; i++) {
                try {
                    TreeSet<xNewPrintData.client> myListOfClients = getListOfClients();
                    Iterator<xNewPrintData.client> myCliIterator = myListOfClients.iterator();
                    while (myCliIterator.hasNext()) {
                        CalendarForSingleMonth newCal = new CalendarForSingleMonth(this, empName, myCliIterator, startWeeks[i], endWeeks[i]);
                        if (newCal.containsData()) {
                            myCalendarObjects.add(newCal);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public ArrayList<CalendarForSingleMonth> getMyCalendars() {
        return myCalendarObjects;
    }
    
    public CalendarForSingleMonth getNextCalendar() {
        CalendarForSingleMonth myCal = myCalendarObjects.get(currCalendar);
        currCalendar++;
        return myCal;
    }
    
    public boolean hasMoreCals() {
        if (currCalendar >= myCalendarObjects.size()) {
            return false;
        }
        return true;
    }
    
    /**
     * Tests if current Calendar has the same employee as the next Calendar used for
     * our employee reports.... to see when we need to cut page
     */
    public boolean nextCalHasSameEmployee() {
        try {
            if (myCalendarObjects.get(currCalendar).getEmployee().equals(myCalendarObjects.get(currCalendar + 1).getEmployee())) {
                return true;
            }
        } catch (Exception e) {
            
        }
        return false;
    }
    
    public String getCurrentEmployeeName() {
        return myCalendarObjects.get(currCalendar).getEmployee();
    }
    
    /**
     * Gets tree set of clients...
     */
    public TreeSet<client> getListOfClients() {
        return listOfClients;
    }
    
    /**
     * Gets tree set of employees
     */ 
    public TreeSet<employee> getListOfEmployees() {
        return listOfEmployees;
    }
    
    public String[] getStartDates() {
        return startWeeks;
    }
    
    public String[] getEndDates() {
        return endWeeks;
    }
    
    /**
     * Gets a list given date range and client and employee....
     */
    public SortedSet<xNewPrintDay> getDaysByClientEmployeeAndDateRange(String cli, String emp, String sweek, String eweek) {
        return orderByEmployeeClientAndDates.subSet(new xNewPrintDay(cli, emp, "0", "2400", sweek, "-99999999", companyId), new xNewPrintDay(cli, emp, "0", "2400", eweek, "99999999999", companyId));
    }
    
    /**
     * Class used to hold information about clients really just used to relate client
     * name with address information and any other necessary information that may be 
     * required
     */
    public class client {
        public String clientName;
        public String clientAddress1;
        public String clientCity;
        public String clientState;
        public String clientZip;
        
        public client(String clientN, String clientA, String clientC, String clientS, String clientZ) {
            clientName = clientN;
            clientAddress1 = clientA;
            clientCity = clientC;
            clientState = clientS;
            clientZip = clientZ;
        }
    }
    
    /**
     * Class used to hold information about employees
     */
    public class employee{
        public String empName;
        
        public employee(String employeeN) {
            empName = employeeN;
        }
    }
    
    /**
     * Ok kinda lame but minimal work use the properties of a TreeSet to ensure that only
     * one entry per unique client is added....
     */
    private class uniqueClients implements Comparator<client> {
        public uniqueClients() {
            
        }
        
        public int compare(client cli1, client cli2) {
            return cli1.clientName.compareToIgnoreCase(cli2.clientName);
        }
    }
    
    /**
     * Ok kinda lame but minimal work use the properties of a TreeSet to ensure that only
     * one entry per unique employee is added....
     */
    private class uniqueEmployees implements Comparator<employee> {
        public uniqueEmployees() {
            
        }
        
        public int compare(employee emp1, employee emp2) {
            return emp1.empName.compareToIgnoreCase(emp2.empName);
        }
    }
    
    /**
     * Used to order by Date, ClientName, EmpName and ShiftId...
     */
    private class comparatorByEmpCliDate implements Comparator<xNewPrintDay> {
        public comparatorByEmpCliDate() {
            
        }
        
        public int compare(xNewPrintDay day1, xNewPrintDay day2) {
            int compareVal = 0;
            compareVal = day1.empName.compareTo(day2.empName);
            if (compareVal != 0) {
                return compareVal;
            }
            compareVal = day1.clientName.compareTo(day2.clientName);
            if (compareVal != 0) {
                return compareVal;
            }
            compareVal = day1.databasedate.compareTo(day2.databasedate);
            if (compareVal != 0) {
                return compareVal;
            }
            compareVal = day1.shiftId.compareTo(day2.shiftId);
            return compareVal;
        }
    }
    
}
