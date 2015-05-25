package schedfoxlib.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import org.restlet.data.Method;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URLEncoder;
import java.util.LinkedList;
import org.restlet.ext.json.JsonRepresentation;

import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Employee;
import schedfoxlib.model.EmployeeCertification;
import schedfoxlib.model.EmployeeDeductionTypes;
import schedfoxlib.model.EmployeePaymentDeduction;
import schedfoxlib.model.EmployeePaymentHours;
import schedfoxlib.model.EmployeePaymentTaxes;
import schedfoxlib.model.EmployeePaymentWage;
import schedfoxlib.model.EmployeePayments;
import schedfoxlib.model.EmployeeTest;
import schedfoxlib.model.EmployeeTypes;
import schedfoxlib.model.EmployeeWageTypes;

public class EmployeeService implements EmployeeControllerInterface {

    private static String location = "Employees/";
    private String companyId = "2";

    public static void main(String args[]) {
        EmployeeService empService = new EmployeeService();
        try {
            Employee emp = empService.getEmployeeById(17771);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    public EmployeeService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public EmployeeService(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public String generateNewUskedId(Employee arg0)
            throws RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Employee> getAllActiveEmployees()
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "listactiveemployee");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<Employee>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<EmployeePaymentDeduction> getDeductions(int employeePaymentId)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getdeductions/" + employeePaymentId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<EmployeePaymentDeduction>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Employee getEmployeeById(int employeeId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getemployee/" + employeeId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return (Employee) gson.fromJson(cr.get().getReader(), Employee.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Vector<EmployeeCertification> getEmployeeCertificationsForEmployee(
            int arg0) throws RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EmployeeDeductionTypes getEmployeeDeductionType(int arg0)
            throws RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EmployeeTest getEmployeeTest(int arg0) throws RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<EmployeeTypes> getEmployeeTypes()
            throws RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Employee> getEmployeesTerminatedOnDate(Date arg0)
            throws RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<EmployeePaymentHours> getHours(int employeePaymentId)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "gethours/" + employeePaymentId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<EmployeePaymentHours>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<EmployeePaymentTaxes> getTaxes(int employeePaymentId)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "gettaxes/" + employeePaymentId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<EmployeePaymentTaxes>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public EmployeeWageTypes getWageType(int arg0) throws RetrieveDataException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer saveEmployee(Employee employee) throws SaveDataException,
            RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savemployee/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.post(new JsonRepresentation(gson.toJson(employee))).getReader(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Employee> getEmployeeByPhone(String phoneNumber) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Employee> getAllActiveEmployeesByBranch(int branchId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "listactiveemployeebybranch/" + branchId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<Employee>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Employee> getActiveAndRecentlyTerminatedEmployeesByBranch(int branchId) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Employee> getEmployeesRemovedFromSchedule(Date dateToCheck) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Employee getEmployeeByScanId(String scanId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getemployeebyscan/" + scanId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return (Employee) gson.fromJson(cr.get().getReader(), Employee.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Employee getEmployeeBySSN(String ssn) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getemployeebyssn/" + URLEncoder.encode(ssn, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return (Employee) gson.fromJson(cr.get().getReader(), Employee.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Employee getEmployeebyLogin(String userName, String password, String companyDb) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getemployeebylogin/" + URLEncoder.encode(userName, "UTF-8") + "/"
                    + URLEncoder.encode(password, "UTF-8") + "/" + URLEncoder.encode(companyDb, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            return (Employee) gson.fromJson(cr.get().getReader(), Employee.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<EmployeePayments> loadEmployeePayments(Integer employeeId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            Type collectionType = new TypeToken<LinkedList<EmployeePayments>>() {
            }.getType();
            
            cr = new ClientResource(Method.GET, getLocation() + "loademployeepayments/" + employeeId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            LinkedList<EmployeePayments> payments = gson.fromJson(cr.get().getReader(), collectionType);
            ArrayList<EmployeePayments> retVal = new ArrayList<EmployeePayments>();
            retVal.addAll(payments);
            return retVal;
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Employee getEmployeeBySSNLastName(String ssn, String lastName, String companyDb) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getemployeebyssn/" + URLEncoder.encode(ssn, "UTF-8") + "/"
                    + URLEncoder.encode(lastName, "UTF-8") + "/" + URLEncoder.encode(companyDb, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            return (Employee) gson.fromJson(cr.get().getReader(), Employee.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<EmployeePaymentWage> getWages(int employee_payment_id) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            Type collectionType = new TypeToken<Collection<EmployeePaymentWage>>() {
            }.getType();
            
            cr = new ClientResource(Method.GET, getLocation() + "getwages/" + employee_payment_id);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Employee> getAllEmployeesByBranch(Integer branchId, Boolean includeDeleted) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getemployeesbybranch/" + branchId + "/" + includeDeleted);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<Employee>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Employee> getActiveEmployeesBySSN(String ssn) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            Type collectionType = new TypeToken<Collection<Employee>>() {
            }.getType();
            cr = new ClientResource(Method.GET, getLocation() + "getactiveemployeesbyssn/" + URLEncoder.encode(ssn, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Boolean toggleActiveInactiveEmployee(Integer employeeId, Boolean isActive) throws SaveDataException, RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "toggleactiveinactiveemployee/" + employeeId + "/" + isActive);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Boolean.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
}
