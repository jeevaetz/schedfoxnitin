/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import schedfoxlib.controller.exceptions.NoSuchEmployeeException;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.*;

/**
 *
 * @author user
 */
public interface ScheduleControllerInterface {

    /**
     * Check in process to add a row into the check in table when a guard calls the IVR system.
     *
     * @param schedule Schedule object containing all the schedule information for this check in action
     * @param user Employee object is used for this userInterface
     * @param emp Employee that is checking in
     * @throws SaveDataException
     */
    public void checkInEmployee(AssembleCheckinScheduleType schedule, UserInterface user, Employee emp) throws SaveDataException;

    public HashMap<Employee, Client> getEmployeesAndClientsNoLongerWorking(int branchId) throws RetrieveDataException;
            
    public boolean checkOut(AssembleCheckinScheduleType schedule, UserInterface user, Employee emp) throws SaveDataException;

    public String checkIn(Integer empId) throws SaveDataException;
    
    public String checkOut(Integer empId) throws SaveDataException;
    
    public boolean checkOutCron(CheckIn checkInObj) throws SaveDataException;

    public ScheduleData getScheduleByIdentifier(Integer scheduleId) throws RetrieveDataException;
    
    /**
     * Return an instance of this class to be used to retrieve data from SchedFox database to be used in web service model
     *
     * @param companyId Database schema id for the company
     * @return myInstance ScheduleController instance
     *
     * public static ScheduleController getInstance(String companyId) {
     * if (myInstance == null) {
     * myInstance = new ScheduleController(companyId);
     * RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
     * try {
     * conn.setConnectionObject(new ServerSideConnection());
     * } catch (Exception e) {
     * System.out.println("Could not set up server!");
     * }
     * }
     *
     * return myInstance;
     * }
     */
    boolean employeeExists(String ssn);

    public ArrayList<ScheduleData> getPatrolProCurrentScheduleDataForEmployee(Integer employee, Integer bufferInMinutes) throws RetrieveDataException;
            
    ArrayList<Caller> getAllCallsByEmployeeID(int employeeID);

    /**
     * Pass the unique schedule id and the employee id to get a CheckIn Object, the object will return an empty object
     * with the isCheckedIn() method set to false if there isn't a check in available
     *
     * @param scheduleID Unique schedule identifier
     * @param employeeID Unique employee identifier
     * @param companyID Schema company ID
     * @return checkInEmployee CheckIn Object created from the result set results of the query
     * @throws RetrieveDataException
     */
    CheckIn getCheckInObj(String scheduleID, Integer employeeID, String companyID) throws RetrieveDataException;

    ArrayList<CheckIn> getCheckInObjWithNoCheckOut(int timeStamp, int person, int timeBuffer, String startDate, String endDate) throws RetrieveDataException;
    
    /**
     * Look up and employee using the Caller ID (cid), this method checks all the phone numbers in the employee record for a match
     *
     * @param currentDate Today's date
     * @param cid Caller ID number to look up
     * @return employee Employee object found for the caller ID
     * @throws NoSuchEmployeeException
     * @throws RetrieveDataException
     */
    ArrayList<Employee> getEmployeeByCID(Date currentDate, String cid) throws NoSuchEmployeeException, RetrieveDataException;

    ArrayList<Employee> getEmployeeByCIDInInboundCallsTable(Date currentDate, String cid) throws NoSuchEmployeeException, RetrieveDataException;

    Employee getEmployeeByEmployeeID(Date currentDate, int employeeID) throws RetrieveDataException;

    ArrayList<Employee> getEmployeeByLast4SSN(Date currentDate, String ssn) throws RetrieveDataException;

    /**
     * Grabs the current schedules at a location that are happening now +- buffer
     * @param clientId
     * @param bufferInMinutes
     * @return
     * @throws RetrieveDataException 
     */
    public ArrayList<ScheduleData> getCurrentSchedulesAtLocationWithBuffer(int clientId, int bufferInMinutes) throws RetrieveDataException;
    
    /**
     * Search for an employee using the current date and the employee's social security number
     *
     * @param currentDate Today's date
     * @param ssn Employee's social security number
     * @return employee Employee object found for the SSN
     * @throws NoSuchEmployeeException
     * @throws RetrieveDataException
     */
    Employee getEmployeeBySSN(Date currentDate, String ssn) throws RetrieveDataException;

    public ArrayList<ScheduleViewLog> getViewsForSchedule(Integer employeeId, Date startDate, Date endDate) throws RetrieveDataException;
    
    public void saveViewForSchedule(ScheduleViewLog log) throws SaveDataException;
    
    /**
     * Retrieve an Array list containing all the schedule objects for the employee object passed in based on a start
     * and finish date.
     * @param startDate Date to start the look up
     * @param endDate Date to end the look up
     * @param emp Employee object to use for the schedule search
     * @return retVal List containing all ScheduleData objects
     * @throws RetrieveDataException
     */
    ArrayList<ScheduleData> getSchedule(Date startDate, Date endDate, Employee emp) throws RetrieveDataException;

    /**
     * Look up the schedule object that is closest to the current time, list should contain only one object
     *
     * @param startDate Today's date
     * @param endDate Today's date
     * @param bufferInMinutes Number of minutes to search for the schedule from the current time.
     * @param emp Employee that the schedule belongs to
     * @return retVal A list with a single Schedule object
     * @throws RetrieveDataException
     */
    ArrayList<AssembleCheckinScheduleType> getScheduleForCheckin(Date startDate, Date endDate, int bufferInMinutes, Employee emp) throws RetrieveDataException;

    public ArrayList<ScheduleData> getScheduleForDateRange(Date startDate, Date endDate, int clientId) throws RetrieveDataException;
    
    /**
     * Call handling
     * Moving this to new controller
     */
    void logInboundCall(Caller caller);

    void saveEmployeePhoneNumber(int employeeId, String phoneNumber, int phnType) throws SaveDataException;
    
}
