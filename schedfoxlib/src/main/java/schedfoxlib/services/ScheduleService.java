package schedfoxlib.services;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;

import schedfoxlib.controller.ScheduleControllerInterface;
import schedfoxlib.controller.exceptions.NoSuchEmployeeException;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.AssembleCheckinScheduleType;
import schedfoxlib.model.Caller;
import schedfoxlib.model.CheckIn;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import schedfoxlib.model.ScheduleData;
import schedfoxlib.model.ScheduleViewLog;
import schedfoxlib.model.UserInterface;

public class ScheduleService implements ScheduleControllerInterface {

    private static String location = "Schedule/";
    private String companyId;

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    public ScheduleService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public ScheduleService(String companyId) {
        this.companyId = companyId;
    }

    public static void main(String args[]) {
        ScheduleService scheduleServer = new ScheduleService();
        EmployeeService employeeServer = new EmployeeService();

        try {
            ScheduleViewLog log = new ScheduleViewLog();
            log.setEmployeeId(123);
            log.setEndDate(new Date("01/08/2013"));
            log.setStartDate(new Date("01/01/2013"));
            log.setFromUrlShorteningService(true);
            log.setIsMobile(true);
            log.setRemoteAddress("remotehost");
            log.setViewTime(new Date());
            log.setViewType(2);
            scheduleServer.saveViewForSchedule(log);
            
            ArrayList<ScheduleViewLog> logs = scheduleServer.getViewsForSchedule(123, new Date("01/01/2013"), new Date("01/08/2013"));
            
            Employee emp = employeeServer.getEmployeeById(13457);
            ArrayList<ScheduleData> schedule = scheduleServer.getSchedule(new Date("02/22/2012"), new Date("03/01/2012"), emp);
            ArrayList<AssembleCheckinScheduleType> schedules = scheduleServer.getScheduleForCheckin(new Date(), new Date(), 5000, emp);
            System.out.println("Here");
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    @Override
    public void checkInEmployee(AssembleCheckinScheduleType scheduleType,
            UserInterface userInterface, Employee emp) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "checkinemployee/" + emp.getEmployeeId());
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(scheduleType)));
            SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), Boolean.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public boolean checkOut(AssembleCheckinScheduleType scheduleType,
            UserInterface userInterface, Employee emp) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "checkoutemployee/" + emp.getEmployeeId());
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(scheduleType)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), Boolean.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public boolean checkOutCron(CheckIn checkInObj) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "checkoutcron/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(checkInObj)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), Boolean.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public boolean employeeExists(String ssn) {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "employeeexists/" + ssn);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Boolean.class);
        } catch (Exception exe) {
            return false;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Caller> getAllCallsByEmployeeID(int employeeID) {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "callsbyemployeeid/" + employeeID);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Caller>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public CheckIn getCheckInObj(String scheduleID, Integer employeeID, String companyID)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getcheckinobj/" + scheduleID + "/"
                + employeeID + "/" + companyID);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), CheckIn.class);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<CheckIn> getCheckInObjWithNoCheckOut(int arg0, int arg1,
            int arg2, String arg3, String arg4) throws RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Employee> getEmployeeByCID(Date arg0, String arg1)
            throws NoSuchEmployeeException, RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Employee> getEmployeeByCIDInInboundCallsTable(Date arg0,
            String arg1) throws NoSuchEmployeeException, RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Employee getEmployeeByEmployeeID(Date arg0, int arg1)
            throws RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Employee> getEmployeeByLast4SSN(Date arg0, String arg1)
            throws RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Employee getEmployeeBySSN(Date arg0, String arg1)
            throws RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<ScheduleData> getSchedule(Date startDate, Date endDate,
            Employee employee) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getschedule/"
                + URLEncoder.encode(myFormat.format(startDate)) + "/" + URLEncoder.encode(myFormat.format(endDate)));
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(employee)));
            Type collectionType = new TypeToken<Collection<ScheduleData>>() {
            }.getType();
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<AssembleCheckinScheduleType> getScheduleForCheckin(
            Date startDate, Date endDate, int buffer, Employee employee)
            throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        String location = getLocation() + "getscheduleforcheckin/" + buffer + "/"
                + URLEncoder.encode(myFormat.format(startDate)) + "/" + URLEncoder.encode(myFormat.format(endDate));
        ClientResource cr = new ClientResource(Method.POST, location);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(employee)));
            Type collectionType = new TypeToken<Collection<AssembleCheckinScheduleType>>() {
            }.getType();
            String repTxt = rep.getText();
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void logInboundCall(Caller arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void saveEmployeePhoneNumber(int arg0, String arg1, int arg2)
            throws SaveDataException {
        // TODO Auto-generated method stub
    }

    @Override
    public ArrayList<ScheduleData> getCurrentSchedulesAtLocationWithBuffer(int clientId, int bufferInMinutes) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getschedulesatlocationwithbuffer/"
                + clientId + "/" + bufferInMinutes);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Representation rep = cr.get();
            Type collectionType = new TypeToken<Collection<ScheduleData>>() {
            }.getType();
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ScheduleData> getScheduleForDateRange(Date startDate, Date endDate, int clientId) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        String mylocation = getLocation() + "getschedulefordaterange/" + clientId + "/"
                + URLEncoder.encode(myFormat.format(startDate)) + "/" + URLEncoder.encode(myFormat.format(endDate));
        ClientResource cr = new ClientResource(Method.POST, mylocation);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Representation rep = cr.get();
            Type collectionType = new TypeToken<Collection<ScheduleData>>() {
            }.getType();
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ScheduleData getScheduleByIdentifier(Integer scheduleId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getschedulebyidentifier/"
                + scheduleId + "/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Representation rep = cr.get();
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), ScheduleData.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ScheduleData> getPatrolProCurrentScheduleDataForEmployee(Integer employee, Integer bufferInMinutes) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getschedulebyidentifier/"
                + employee + "/" + bufferInMinutes);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Representation rep = cr.get();
            Type collectionType = new TypeToken<Collection<ScheduleData>>() {
            }.getType();
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public HashMap<Employee, Client> getEmployeesAndClientsNoLongerWorking(int branchId) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<ScheduleViewLog> getViewsForSchedule(Integer employeeId, Date startDate, Date endDate) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.POST, getLocation() + "getviewsforschedule/"
                + employeeId + "/" + URLEncoder.encode(myFormat.format(startDate), "UTF-8") + "/"
                + URLEncoder.encode(myFormat.format(endDate), "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Representation rep = cr.get();
            Type collectionType = new TypeToken<Collection<ScheduleData>>() {
            }.getType();
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveViewForSchedule(ScheduleViewLog log) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "saveviewforschedule/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(log)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public String checkIn(Integer empId) throws SaveDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String checkOut(Integer empId) throws SaveDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
