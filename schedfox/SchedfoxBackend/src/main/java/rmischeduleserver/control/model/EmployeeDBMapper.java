/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.control.EmployeeController;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Employee;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.employee.SaveEmployeeObjectQuery;
import rmischeduleserver.mysqlconnectivity.queries.export.get_employees_for_usked_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.GetNextEmpIDQuery;

/**
 *
 * @author vnguyen
 */
public class EmployeeDBMapper extends DBMapper {
    HashMap<String, Boolean> defaultColumn = new HashMap<String, Boolean>();
    ArrayList<DynamicFieldDef> dynamicFields = new ArrayList<DynamicFieldDef>();
    
    ArrayList<EmployeeDataObject> employees = new ArrayList<EmployeeDataObject>();

    private ArrayList<Employee> insertedEmployees;
    private ArrayList<Employee> updatedEmployees;
    private ArrayList<Employee> errorEmployees;

    public EmployeeDBMapper(RMIScheduleServerImpl myConn, String companyId, String branchId) {
        super(myConn, companyId, branchId);
    }

    public EmployeeDBMapper(ArrayList<String> columnHeaders, ArrayList<ArrayList<String>> excelData, RMIScheduleServerImpl myConn, String branchId) {
        super(columnHeaders, excelData, myConn, branchId);
    }

    @Override
    public GeneralQueryFormat generateQuery(Object[] params) {
        get_employees_for_usked_query employeeUskedQuery = new get_employees_for_usked_query();
        employeeUskedQuery.setPreparedStatement(params);
        return employeeUskedQuery;
    }
    
    @Override
    public LinkedHashMap<DataMappingClass, Integer> getMappings() {
        LinkedHashMap<DataMappingClass, Integer> retVal = new LinkedHashMap<DataMappingClass, Integer>();

        TableMapClass tFirstName = new TableMapClass("employee", "employee_first_name");
        DataMappingClass firstName = new DataMappingClass(tFirstName, "First Name", "FirstName", "fn");
        retVal.put(firstName, -1);

        TableMapClass tLastName = new TableMapClass("employee", "employee_last_name");
        DataMappingClass lastName = new DataMappingClass(tLastName, "Last Name", "LastName", "ln");
        retVal.put(lastName, -1);

        TableMapClass tMiddleName = new TableMapClass("employee", "employee_middle_initial");
        DataMappingClass middleName = new DataMappingClass(tMiddleName, "Middle Name", "Middle Initial", "MiddleInitial", "mi");
        retVal.put(middleName, -1);

        if (false) {
            TableMapClass tSSN = new TableMapClass("employee", "employee_ssn");
            DataMappingClass ssn = new DataMappingClass(tSSN, "SSN", "social", "social security number");
            retVal.put(ssn, -1);
        }
        
        TableMapClass tAddress1 = new TableMapClass("employee", "employee_address");
        DataMappingClass address1 = new DataMappingClass(tAddress1, "Address Line 1", "Address1", "Address");
        retVal.put(address1, -1);

        TableMapClass tAddress2 = new TableMapClass("employee", "employee_address2");
        DataMappingClass address2 = new DataMappingClass(tAddress2, "Address Line 2", "Address2");
        retVal.put(address2, -1);

        TableMapClass tCity = new TableMapClass("employee", "employee_city");
        DataMappingClass city = new DataMappingClass(tCity, "City");
        retVal.put(city, -1);

        TableMapClass tState = new TableMapClass("employee", "employee_state");
        DataMappingClass state = new DataMappingClass(tState, "State");
        retVal.put(state, -1);

        TableMapClass tZip = new TableMapClass("employee", "employee_zip");
        DataMappingClass zip = new DataMappingClass(tZip, "Zip");
        retVal.put(zip, -1);

        TableMapClass tPhone = new TableMapClass("employee", "employee_phone");
        DataMappingClass phone = new DataMappingClass(tPhone, "Phone", "Home Phone");
        retVal.put(phone, -1);

        TableMapClass tPhone2 = new TableMapClass("employee", "employee_phone2");
        DataMappingClass phone2 = new DataMappingClass(tPhone2, "Alt Phone", "Phone2");
        retVal.put(phone2, -1);

        TableMapClass tCell = new TableMapClass("employee", "employee_cell");
        DataMappingClass cell = new DataMappingClass(tCell, "Cell Phone", "Cell");
        retVal.put(cell, -1);

        TableMapClass tPage = new TableMapClass("employee", "employee_pager");
        DataMappingClass page = new DataMappingClass(tPage, "Page Phone", "Pager");
        retVal.put(page, -1);

        TableMapClass tEmail = new TableMapClass("employee", "employee_email");
        DataMappingClass email = new DataMappingClass(tEmail, "Email");
        retVal.put(email, -1);

        TableMapClass tAltEmail = new TableMapClass("employee", "employee_email2");
        DataMappingClass altEmail = new DataMappingClass(tAltEmail, "Alt Email", "Email2");
        retVal.put(altEmail, -1);

        TableMapClass tAllowMessaging = new TableMapClass("employee", "email_messaging");
        DataMappingClass allowMessaging = new DataMappingClass(tAllowMessaging, "Allow Messaging");
        retVal.put(allowMessaging, -1);

        TableMapClass tAllowSMS = new TableMapClass("employee", "sms_messaging");
        DataMappingClass allowSMS = new DataMappingClass(tAllowSMS, "Allow SMS");
        retVal.put(allowSMS, -1);

        TableMapClass tHireDate = new TableMapClass("employee", "employee_hire_date");
        DataMappingClass hireDate = new DataMappingClass(tHireDate, "Hire Date", "Hire");
        retVal.put(hireDate, -1);

        TableMapClass tTermDate = new TableMapClass("employee", "employee_term_date");
        DataMappingClass termDate = new DataMappingClass(tTermDate, "Term Date", "Termination");
        retVal.put(termDate, -1);

        TableMapClass tIsDeleted = new TableMapClass("employee", "employee_is_deleted");
        DataMappingClass isDeleted = new DataMappingClass(tIsDeleted, "Is Deleted", "isdeleted", "deleted");
        retVal.put(isDeleted, -1);

        TableMapClass tLastUpdated = new TableMapClass("employee", "employee_last_updated");
        DataMappingClass lastUpdated = new DataMappingClass(tLastUpdated, "Last Updated");
        retVal.put(lastUpdated, -1);

        TableMapClass tEmpType = new TableMapClass("employee", "employee_type_id");
        DataMappingClass empType = new DataMappingClass(tEmpType, "Employee Type", "Emp Type", "EmployeeType");
        retVal.put(empType, -1);

        TableMapClass tAllowLogin = new TableMapClass("employee", "is_login_available");
        DataMappingClass allowLogin = new DataMappingClass(tAllowLogin, "Allow Login");
        retVal.put(allowLogin, -1);

        TableMapClass tWebLogin = new TableMapClass("employee", "web_login");
        DataMappingClass webLogin = new DataMappingClass(tWebLogin, "UserName", "WebLogin");
        retVal.put(webLogin, -1);

        TableMapClass tWebPassword = new TableMapClass("employee", "web_pw");
        DataMappingClass webPassword = new DataMappingClass(tWebPassword, "Password", "Web Password");
        retVal.put(webPassword, -1);

        TableMapClass tBirthDate = new TableMapClass("employee", "employee_birthdate");
        DataMappingClass birthDate = new DataMappingClass(tBirthDate, "BirthDate", "BirthDay");
        retVal.put(birthDate, -1);

        TableMapClass tTypeId = new TableMapClass("employee_types", "employee_type");
        DataMappingClass typeId = new DataMappingClass(tTypeId, "Type Id");
        retVal.put(typeId, -1);

        TableMapClass tAccruedVacation = new TableMapClass("employee", "accrued_vacation");
        DataMappingClass accruedVacation = new DataMappingClass(tAccruedVacation, "Accrued Vacation", "Vacation");
        retVal.put(accruedVacation, -1);

        GeneralQueryFormat dynamicFieldDefQuery = new getDyanmicFieldDef();
        try {
            dynamicFieldDefQuery.setCompany(companyId);
            Record_Set rs = this.myConn.executeQuery(dynamicFieldDefQuery, "");
            for (int r = 0; r < rs.length(); r++) {
                String dynamic_field_def_name = rs.getString("dynamic_field_def_name");
                TableMapClass tDynamicField = new TableMapClass("dynamic_field_value", "dynamic_field_value");
                DataMappingClass dynamicField = new DataMappingClass(tDynamicField, dynamic_field_def_name);
                retVal.put(dynamicField, -1);
                rs.moveNext();
            }
        } catch (Exception ex) {
            System.out.println("An error occured in EmloyeeDBMapper " + ex.getMessage());
        }
        return retVal;
    }

    
    @Override
    public void insertValuesIntoDB(LinkedHashMap<DataMappingClass, Integer> mappedColumns) {
        this.insertValuesIntoEmployeeTable(mappedColumns);
    }

    public ArrayList<Employee> getInsertedEmployees() {
        return this.insertedEmployees;
    }

    public ArrayList<Employee> getUpdatedEmployees() {
        return this.updatedEmployees;
    }

    public ArrayList<Employee> getErrorEmployees() {
        return this.errorEmployees;
    }

    /**
     * Actually starts the insert / update process for employees.
     * @param mappedColumns
     */
    public void insertValuesIntoEmployeeTable(LinkedHashMap<DataMappingClass, Integer> mappedColumns) {
        ArrayList<DataMappingClass> employeesValues = super.getDataMappingByTable(mappedColumns, "employee");

        insertedEmployees = new ArrayList<Employee>();
        updatedEmployees = new ArrayList<Employee>();
        errorEmployees = new ArrayList<Employee>();

        DataMappingClass socialSecCol = super.getDataMappingClassByColumnName("employee_ssn", employeesValues);
        DataMappingClass accruedVacation = super.getDataMappingClassByColumnName("accrued_vacation", employeesValues);
        DataMappingClass emailMessaging = super.getDataMappingClassByColumnName("email_messaging", employeesValues);
        DataMappingClass address1 = super.getDataMappingClassByColumnName("employee_address", employeesValues);
        DataMappingClass address2 = super.getDataMappingClassByColumnName("employee_address2", employeesValues);
        DataMappingClass birthdate = super.getDataMappingClassByColumnName("employee_birthdate", employeesValues);
        DataMappingClass cell = super.getDataMappingClassByColumnName("employee_cell", employeesValues);
        DataMappingClass phone = super.getDataMappingClassByColumnName("employee_phone", employeesValues);
        DataMappingClass phone2 = super.getDataMappingClassByColumnName("employee_phone2", employeesValues);
        DataMappingClass pager = super.getDataMappingClassByColumnName("employee_pager", employeesValues);
        DataMappingClass city = super.getDataMappingClassByColumnName("employee_city", employeesValues);
        DataMappingClass state = super.getDataMappingClassByColumnName("employee_state", employeesValues);
        DataMappingClass zip = super.getDataMappingClassByColumnName("employee_zip", employeesValues);
        DataMappingClass email = super.getDataMappingClassByColumnName("employee_email", employeesValues);
        DataMappingClass email2 = super.getDataMappingClassByColumnName("employee_email2", employeesValues);
        DataMappingClass firstName = super.getDataMappingClassByColumnName("employee_first_name", employeesValues);
        DataMappingClass middleInit = super.getDataMappingClassByColumnName("employee_middle_initial", employeesValues);
        DataMappingClass lastName = super.getDataMappingClassByColumnName("employee_last_name", employeesValues);
        DataMappingClass hireDate = super.getDataMappingClassByColumnName("employee_hire_date", employeesValues);
        DataMappingClass termDate = super.getDataMappingClassByColumnName("employee_term_date", employeesValues);
        DataMappingClass isDeleted = super.getDataMappingClassByColumnName("employee_is_deleted", employeesValues);
        DataMappingClass lastUpdated = super.getDataMappingClassByColumnName("employee_last_updated", employeesValues);
        DataMappingClass employeeLogin = super.getDataMappingClassByColumnName("employee_login", employeesValues);
        DataMappingClass employeePassword = super.getDataMappingClassByColumnName("employee_password", employeesValues);
        DataMappingClass employeeType = super.getDataMappingClassByColumnName("employee_type", employeesValues);
        DataMappingClass employeeTypeId = super.getDataMappingClassByColumnName("employee_type_id", employeesValues);
        DataMappingClass workHrs = super.getDataMappingClassByColumnName("employee_workhrs_pweek", employeesValues);
        DataMappingClass loginAvail = super.getDataMappingClassByColumnName("is_login_available", employeesValues);
        DataMappingClass smsMessaging = super.getDataMappingClassByColumnName("sms_messaging", employeesValues);
        DataMappingClass webLogin = super.getDataMappingClassByColumnName("web_login", employeesValues);
        DataMappingClass webPassword = super.getDataMappingClassByColumnName("web_pw", employeesValues);

        ArrayList<String> socialSecurityArrayList = socialSecCol.getValues();
        for (int s = 0; s < socialSecurityArrayList.size(); s++) {
            Date currDate = new Date(myConn.getServerCurrentTimeMillis());
            Employee currEmployee = new Employee(currDate);
            boolean isNewEmployee = false;
            try {
                FindEmployeeBySocialSecurityQuery empQuery = new FindEmployeeBySocialSecurityQuery();
                try {
                    currEmployee.setEmployeeSsn(socialSecCol.getValueAt(s));
                } catch (Exception e) {}
                empQuery.setPreparedStatement(new Object[]{currEmployee.getEmployeeSsn()});
                Record_Set rs = this.myConn.executeQuery(empQuery, "");
                int employee_id = -1;
                try {
                    employee_id = rs.getInt("employee_id");
                } catch (Exception e) {}

                if (employee_id != -1) {
                    //An employee matches this lets update employee
                    currEmployee.setEmployeeId(employee_id);
                } else {
                    //No existing employee matches this, lets insert employee
                    GetNextEmpIDQuery empSeqQuery = new GetNextEmpIDQuery();
                    rs = this.myConn.executeQuery(empSeqQuery, "");
                    employee_id = rs.getInt("id");
                    currEmployee.setNewEmployeeId(employee_id);
                    isNewEmployee = true;
                }
                try {
                    currEmployee.setAccruedVacation(Integer.parseInt(accruedVacation.getValueAt(s)));
                } catch (Exception e) {}
                try {
                    currEmployee.setBranchId(Integer.parseInt(branchId));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmailMessaging(emailMessaging.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeAddress(address1.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeAddress2(address2.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeBirthdate(parseDate(birthdate.getValueAt(s)));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeCell(cell.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeCity(city.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeEmail(email.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeEmail2(email2.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeFirstName(firstName.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeLastName(lastName.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeHireDate(parseDate(hireDate.getValueAt(s)));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeIsDeleted(isDeleted.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeLastUpdated(null);
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeLogin(employeeLogin.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeMiddleInitial(middleInit.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeePager(pager.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeePassword(employeePassword.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeePhone(phone.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeePhone2(phone2.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeState(state.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeTermDate(parseDate(termDate.getValueAt(s)));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeType(Integer.parseInt(employeeType.getValueAt(s)));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeTypeId(Integer.parseInt(employeeTypeId.getValueAt(s)));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeWorkhrsPweek(Integer.parseInt(workHrs.getValueAt(s)));
                } catch (Exception e) {}
                try {
                    currEmployee.setEmployeeZip(zip.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setIsLoginAvailableShort(loginAvail.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setSmsMessaging(smsMessaging.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setWebLogin(webLogin.getValueAt(s));
                } catch (Exception e) {}
                try {
                    currEmployee.setWebPw(webPassword.getValueAt(s));
                } catch (Exception e) {}
                
                EmployeeController empController = EmployeeController.getInstance("2");
                empController.saveEmployee(currEmployee);

                if (isNewEmployee) {
                    insertedEmployees.add(currEmployee);
                } else {
                    updatedEmployees.add(currEmployee);
                }
            } catch (Exception e) {
                errorEmployees.add(currEmployee);
            }
        }
    }

    private Date parseDate(String value) throws Exception {
        Date retVal = null;
        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
            retVal = myFormat.parse(value);
        } catch (Exception e) {
            try {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                retVal = myFormat.parse(value);
            } catch (Exception exe) {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMdd");
                retVal = myFormat.parse(value);
            }
        }
        return retVal;
    }

    @Override
    public LinkedHashMap<DataMappingClass, Integer> getUskedMappings() {
        LinkedHashMap<DataMappingClass, Integer> retVal = new LinkedHashMap<DataMappingClass, Integer>();

        helperUskedMapMethod(retVal, "usked_employee", "usked_emp_id", "Employee Id");

        UskedActiveColumn tClientStatus = new UskedActiveColumn("employee", "employee_is_deleted");
        DataMappingClass clientStatus = new DataMappingClass(tClientStatus, "Is Active");
        clientStatus.setUskedId("Status");
        retVal.put(clientStatus, -1);

        TableMapClass tLastName = new TableMapClass("employee", "employee_last_name");
        DataMappingClass lastName = new DataMappingClass(tLastName, "Last Name", "LastName", "ln");
        lastName.setUskedId("Last Name");
        retVal.put(lastName, -1);

        TableMapClass tFirstName = new TableMapClass("employee", "employee_first_name");
        DataMappingClass firstName = new DataMappingClass(tFirstName, "First Name", "FirstName", "fn");
        firstName.setUskedId("First Name");
        retVal.put(firstName, -1);

        TableMapClass tMiddleName = new TableMapClass("employee", "employee_middle_initial");
        DataMappingClass middleName = new DataMappingClass(tMiddleName, "Middle Name", "MiddleName", "mi");
        middleName.setUskedId("Middle Name");
        retVal.put(middleName, -1);

        TableMapClass tAddress1 = new TableMapClass("employee", "employee_address");
        DataMappingClass address1 = new DataMappingClass(tAddress1, "Address Line 1", "Address1", "Address");
        address1.setUskedId("Address 1");
        retVal.put(address1, -1);

        TableMapClass tAddress2 = new TableMapClass("employee", "employee_address2");
        DataMappingClass address2 = new DataMappingClass(tAddress2, "Address Line 2", "Address2");
        address2.setUskedId("Address 2");
        retVal.put(address2, -1);

        TableMapClass tCity = new TableMapClass("employee", "employee_city");
        DataMappingClass city = new DataMappingClass(tCity, "City");
        city.setUskedId("City");
        retVal.put(city, -1);

        TableMapClass tState = new TableMapClass("employee", "employee_state");
        DataMappingClass state = new DataMappingClass(tState, "State");
        state.setUskedId("State");
        retVal.put(state, -1);

        TableMapClass tZip = new TableMapClass("employee", "employee_zip");
        DataMappingClass  zip = new DataMappingClass(tZip, "Zip");
        zip.setUskedId("Zip");
        retVal.put(zip, -1);

        TableMapClass tCountry = new TableMapClass("", "");
        DataMappingClass country = new DataMappingClass(tCountry, "Country");
        country.setUskedId("Country");
        retVal.put(country, -1);

        TableMapClass tSSN = new UskedSSNColumn("employee", "employee_ssn");
        DataMappingClass ssn = new DataMappingClass(tSSN, "SSN", "social", "social security number");
        ssn.setUskedId("SSN");
        retVal.put(ssn, -1);

        TableMapClass tSSNLastName = new TableMapClass("employee", "employee_last_name");
        DataMappingClass sSNLastName = new DataMappingClass(tSSNLastName, "Last Name", "LastName", "ln");
        sSNLastName.setUskedId("SS Last Name");
        retVal.put(sSNLastName, -1);

        TableMapClass tSSNFirstName = new TableMapClass("employee", "employee_first_name");
        DataMappingClass sSNFirstName = new DataMappingClass(tSSNFirstName, "First Name", "FirstName", "fn");
        sSNFirstName.setUskedId("SS First Name");
        retVal.put(sSNFirstName, -1);

        TableMapClass tSSNMiddleName = new TableMapClass("employee", "employee_middle_initial");
        DataMappingClass sSNMiddleName = new DataMappingClass(tSSNMiddleName, "Middle Name", "MiddleName", "mi");
        sSNMiddleName.setUskedId("SS Middle Name");
        retVal.put(sSNMiddleName, -1);

        TableMapClass t1099TIN = new TableMapClass("", "");
        DataMappingClass s1099TIN = new DataMappingClass(t1099TIN, "1099 TIN");
        s1099TIN.setUskedId("1099 TIN");
        retVal.put(s1099TIN, -1);

        TableMapClass t1099DBA = new TableMapClass("", "");
        DataMappingClass s1099DBA = new DataMappingClass(t1099DBA, "1099 DBA/LLC Name");
        s1099DBA.setUskedId("1099 DBA/LLC Name");
        retVal.put(s1099DBA, -1);

        TableMapClass tOfficeCode = new TableMapClass("", "");
        DataMappingClass officeCode = new DataMappingClass(tOfficeCode, "Office Code");
        officeCode.setUskedId("Office Code");
        retVal.put(officeCode, -1);

        TableMapClass tDeptCode = new TableMapClass("", "");
        DataMappingClass deptCode = new DataMappingClass(tDeptCode, "Department Code");
        deptCode.setUskedId("Department Code");
        retVal.put(deptCode, -1);

        TableMapClass tLocationCode = new TableMapClass("", "");
        DataMappingClass locationCode = new DataMappingClass(tLocationCode, "Location Code");
        locationCode.setUskedId("Location Code");
        retVal.put(locationCode, -1);

        TableMapClass tSales1Code = new TableMapClass("", "");
        DataMappingClass sales1Code = new DataMappingClass(tSales1Code, "Salesman 1 Code");
        sales1Code.setUskedId("Salesman 1 Code");
        retVal.put(sales1Code, -1);

        TableMapClass tSales2Code = new TableMapClass("", "");
        DataMappingClass sales2Code = new DataMappingClass(tSales2Code, "Salesman 2 Code");
        sales2Code.setUskedId("Salesman 2 Code");
        retVal.put(sales2Code, -1);

        TableMapClass tRankCode = new TableMapClass("", "");
        DataMappingClass rankCode = new DataMappingClass(tRankCode, "Rank Code");
        rankCode.setUskedId("Rank Code");
        retVal.put(rankCode, -1);

        TableMapClass tWorkCode = new TableMapClass("", "");
        DataMappingClass workCode = new DataMappingClass(tWorkCode, "Work Code");
        workCode.setUskedId("Work Code");
        retVal.put(workCode, -1);

        TableMapClass tTransCode = new TableMapClass("", "");
        DataMappingClass transCode = new DataMappingClass(tTransCode, "Transportation Code");
        transCode.setUskedId("Transportation Code");
        retVal.put(transCode, -1);

        TableMapClass tExternalId = new TableMapClass("employee", "employee_id");
        DataMappingClass externalId = new DataMappingClass(tExternalId, "External System Id");
        externalId.setUskedId("External System Id");
        retVal.put(externalId, -1);

        TableMapClass tMaritalStatus = new TableMapClass("", "fed_status");
        DataMappingClass maritalStatus = new DataMappingClass(tMaritalStatus, "Marital Status");
        maritalStatus.setUskedId("Marital Status");
        retVal.put(maritalStatus, -1);

        TableMapClass tRace = new TableMapClass("", "");
        DataMappingClass race = new DataMappingClass(tRace, "Race");
        race.setUskedId("Race");
        retVal.put(race, -1);

        TableMapClass tSex = new TableMapClass("", "");
        DataMappingClass sex = new DataMappingClass(tSex, "Sex");
        sex.setUskedId("Sex");
        retVal.put(sex, -1);

        TableMapClass tBirthdate = new UskedDateColumn("employee", "employee_birthdate");
        DataMappingClass birthdate = new DataMappingClass(tBirthdate, "Birthdate");
        birthdate.setUskedId("Birthdate");
        retVal.put(birthdate, -1);

        TableMapClass tHiredate = new UskedDateColumn("employee", "employee_hire_date");
        DataMappingClass hiredate = new DataMappingClass(tHiredate, "Hire Date");
        hiredate.setUskedId("Hire Date");
        retVal.put(hiredate, -1);

        TableMapClass tRehiredate = new UskedDateColumn("", "");
        DataMappingClass rehiredate = new DataMappingClass(tRehiredate, "Rehired On");
        rehiredate.setUskedId("Rehired On");
        retVal.put(rehiredate, -1);

        TableMapClass tFirstDate = new UskedDateColumn("", "");
        DataMappingClass firstDate = new DataMappingClass(tFirstDate, "First Check");
        firstDate.setUskedId("First Check");
        retVal.put(firstDate, -1);

        TableMapClass tLastDate = new UskedDateColumn("", "");
        DataMappingClass lastDate = new DataMappingClass(tLastDate, "Last Check");
        lastDate.setUskedId("Last Check");
        retVal.put(lastDate, -1);

        TableMapClass tTerminated = new UskedDateColumn("employee", "employee_term_date");
        DataMappingClass terminated = new DataMappingClass(tTerminated, "Terminated");
        terminated.setUskedId("Terminated");
        retVal.put(terminated, -1);

        TableMapClass tTermCode = new TableMapClass("", "");
        DataMappingClass termCode = new DataMappingClass(tTermCode, "Term Code");
        termCode.setUskedId("Term Code");
        retVal.put(termCode, -1);

        TableMapClass tTermType = new TableMapClass("", "");
        DataMappingClass termType = new DataMappingClass(tTermType, "Tax Type");
        termType.setUskedId("Tax Type");
        retVal.put(termType, -1);

        TableMapClass tPayFrequency = new TableMapClass("", "");
        DataMappingClass payFrequency = new DataMappingClass(tPayFrequency, "Pay Frequency");
        payFrequency.setUskedId("Pay Frequency");
        retVal.put(payFrequency, -1);
        
        TableMapClass tCheckDistribution = new TableMapClass("", "");
        DataMappingClass checkDistribution = new DataMappingClass(tCheckDistribution, "Check Distribution");
        checkDistribution.setUskedId("Check Distribution");
        retVal.put(checkDistribution, -1);

        //TODO: Calc this from rate
        TableMapClass tHolidayPay = new UskedHolidayPayColumn("", "");
        DataMappingClass holidayPay = new DataMappingClass(tHolidayPay, "Holiday Pay");
        holidayPay.setUskedId("Holiday Pay");
        retVal.put(holidayPay, -1);

        TableMapClass tOvertimeOverride = new TableMapClass("", "");
        DataMappingClass overtimeOverride = new DataMappingClass(tOvertimeOverride, "Overtime Override");
        overtimeOverride.setUskedId("Overtime Override");
        retVal.put(overtimeOverride, -1);

        TableMapClass tPayType = new TableMapClass("", "");
        DataMappingClass payType = new DataMappingClass(tPayType, "Pay Type");
        payType.setUskedId("Pay Type");
        retVal.put(payType, -1);

        TableMapClass tSalaryComp = new TableMapClass("", "");
        DataMappingClass salaryComp = new DataMappingClass(tSalaryComp, "Salary Comp Code");
        salaryComp.setUskedId("Salary Comp Code");
        retVal.put(salaryComp, -1);

        TableMapClass tSalaryPay = new TableMapClass("", "");
        DataMappingClass salaryPay = new DataMappingClass(tSalaryPay, "Salary Pay Amount");
        salaryPay.setUskedId("Salary Pay Amount");
        retVal.put(salaryPay, -1);

        TableMapClass tSalaryHours = new TableMapClass("", "");
        DataMappingClass salaryHours = new DataMappingClass(tSalaryHours, "Salary Hours");
        salaryHours.setUskedId("Salary Hours");
        retVal.put(salaryHours, -1);

        TableMapClass tSalaryPayRule = new TableMapClass("", "");
        DataMappingClass salaryPayRule = new DataMappingClass(tSalaryPayRule, "Salary Pay Rule");
        salaryPayRule.setUskedId("Salary Pay Rule");
        retVal.put(salaryPayRule, -1);

        TableMapClass tSalaryBillRule = new TableMapClass("", "");
        DataMappingClass salaryBillRule = new DataMappingClass(tSalaryBillRule, "Salary Bill Rule");
        salaryBillRule.setUskedId("Salary Bill Rule");
        retVal.put(salaryBillRule, -1);

        UskedDirectDepositAccountColumn tDDMainAcctNum = new UskedDirectDepositAccountColumn("employee_accounts", "account_number");

        TableMapClass tDDMainAcctType = new UskedDirectDepositTypeColumn("employee_accounts", "account_number", tDDMainAcctNum);
        DataMappingClass dDMainAcctType = new DataMappingClass(tDDMainAcctType, "DD Main Account Type");
        dDMainAcctType.setUskedId("DD Main Account Type");
        retVal.put(dDMainAcctType, -1);

        
        DataMappingClass dDMainAcctNum = new DataMappingClass(tDDMainAcctNum, "DD Main Account #");
        dDMainAcctNum.setUskedId("DD Main Account #");
        retVal.put(dDMainAcctNum, -1);

        TableMapClass tDDMainRoutingNum = new TableMapClass("employee_accounts", "routing_number");
        DataMappingClass dDMainRoutingNum = new DataMappingClass(tDDMainRoutingNum, "DD Main Routing #");
        dDMainRoutingNum.setUskedId("DD Main Routing #");
        retVal.put(dDMainRoutingNum, -1);

        TableMapClass tDDMainPrenoteStatus = new UskedDirectDepositPrenoteColumn("", "", tDDMainAcctNum);
        DataMappingClass dDMainPrenoteStatus = new DataMappingClass(tDDMainPrenoteStatus, "DD Main Prenote Status");
        dDMainPrenoteStatus.setUskedId("DD Main Prenote Status");
        retVal.put(dDMainPrenoteStatus, -1);

        TableMapClass tFederalStatus = new UskedFedStatusColumn("", "fed_status");
        DataMappingClass federalStatus = new DataMappingClass(tFederalStatus, "Federal Status");
        federalStatus.setUskedId("Federal Status");
        retVal.put(federalStatus, -1);

        TableMapClass tFederalExemptions = new TableMapClass("", "fed_exemption");
        DataMappingClass federalExemptions = new DataMappingClass(tFederalExemptions, "Federal Exemptions");
        federalExemptions.setUskedId("Federal Exemptions");
        retVal.put(federalExemptions, -1);

        TableMapClass tFederalExtraWith = new TableMapClass("", "fed_withholding");
        DataMappingClass federalExtraWith = new DataMappingClass(tFederalExtraWith, "Federal Extra W/H");
        federalExtraWith.setUskedId("Federal Extra W/H");
        retVal.put(federalExtraWith, -1);

        TableMapClass tEICStatus = new TableMapClass("", "");
        DataMappingClass eICStatus = new DataMappingClass(tEICStatus, "EIC Status");
        eICStatus.setUskedId("EIC Status");
        retVal.put(eICStatus, -1);

        UskedTaxCodeColumn tStateCode = new UskedTaxCodeColumn("", "state_code");
        DataMappingClass stateCode = new DataMappingClass(tStateCode, "State Tax Code");
        stateCode.setUskedId("State Tax Code");
        retVal.put(stateCode, -1);

        TableMapClass tStateStatus = new UskedTaxStatusColumn("", "state_status", tStateCode);
        DataMappingClass stateStatus = new DataMappingClass(tStateStatus, "State Status");
        stateStatus.setUskedId("State Status");
        retVal.put(stateStatus, -1);

        TableMapClass tStateExemptions = new UskedTaxExemptionsColumn("", "state_exemption", tStateCode);
        DataMappingClass stateExemptions = new DataMappingClass(tStateExemptions, "State Exemptions");
        stateExemptions.setUskedId("State Exemptions");
        retVal.put(stateExemptions, -1);

        TableMapClass tStateExtraWith = new UskedTaxWithholdingsColumn("", "state_withholding", tStateCode);
        DataMappingClass stateExtraWith = new DataMappingClass(tStateExtraWith, "State Extra W/H");
        stateExtraWith.setUskedId("State Extra W/H");
        retVal.put(stateExtraWith, -1);

        TableMapClass tStatePersonel = new TableMapClass("", "");
        DataMappingClass statePersonel = new DataMappingClass(tStatePersonel, "State Personal/Estimated");
        statePersonel.setUskedId("State Personal/Estimated");
        retVal.put(statePersonel, -1);

        UskedTaxCodeColumn tCityCode = new UskedTaxCodeColumn("", "city_code");
        DataMappingClass cityCode = new DataMappingClass(tCityCode, "City Tax Code");
        cityCode.setUskedId("City Tax Code");
        retVal.put(cityCode, -1);

        TableMapClass tCityStatus = new UskedTaxStatusColumn("", "city_status", tCityCode);
        DataMappingClass cityStatus = new DataMappingClass(tCityStatus, "City Status");
        cityStatus.setUskedId("City Status");
        retVal.put(cityStatus, -1);

        TableMapClass tCityExemptions = new UskedTaxExemptionsColumn("", "city_exemption", tCityCode);
        DataMappingClass cityExemptions = new DataMappingClass(tCityExemptions, "City Exemptions");
        cityExemptions.setUskedId("City Exemptions");
        retVal.put(cityExemptions, -1);

        TableMapClass tCityExtraWith = new UskedTaxWithholdingsColumn("", "city_withholding", tCityCode);
        DataMappingClass cityExtraWith = new DataMappingClass(tCityExtraWith, "City Extra W/H");
        cityExtraWith.setUskedId("City Extra W/H");
        retVal.put(cityExtraWith, -1);

        UskedTaxCodeColumn tSchoolCode = new UskedTaxCodeColumn("", "");
        DataMappingClass schoolCode = new DataMappingClass(tSchoolCode, "School Tax Code");
        schoolCode.setUskedId("School Tax Code");
        retVal.put(schoolCode, -1);

        TableMapClass tSchoolStatus = new UskedTaxStatusColumn("", "", tSchoolCode);
        DataMappingClass schoolStatus = new DataMappingClass(tSchoolStatus, "School Status");
        schoolStatus.setUskedId("School Status");
        retVal.put(schoolStatus, -1);

        TableMapClass tSchoolExemptions = new TableMapClass("", "");
        DataMappingClass schoolExemptions = new DataMappingClass(tSchoolExemptions, "School Exemptions");
        schoolExemptions.setUskedId("School Exemptions");
        retVal.put(schoolExemptions, -1);

        TableMapClass tSchoolExtraWith = new TableMapClass("", "");
        DataMappingClass schoolExtraWith = new DataMappingClass(tSchoolExtraWith, "School Extra W/H");
        schoolExtraWith.setUskedId("School Extra W/H");
        retVal.put(schoolExtraWith, -1);

        TableMapClass tContactOnePhone1 = new TableMapClass("employee", "employee_phone");
        DataMappingClass contactOnePhone1 = new DataMappingClass(tContactOnePhone1, "Contact 1 Phone 1");
        contactOnePhone1.setUskedId("Contact 1 Phone 1");
        retVal.put(contactOnePhone1, -1);
        
        TableMapClass tContactOnePhone2 = new TableMapClass("employee", "employee_phone2");
        DataMappingClass contactOnePhone2 = new DataMappingClass(tContactOnePhone2, "Contact 1 Phone 2");
        contactOnePhone2.setUskedId("Contact 1 Phone 2");
        retVal.put(contactOnePhone2, -1);
        
        TableMapClass tContactOnePhone3 = new TableMapClass("employee", "employee_cell");
        DataMappingClass contactOnePhone3 = new DataMappingClass(tContactOnePhone3, "Contact 1 Phone 3");
        contactOnePhone3.setUskedId("Contact 1 Phone 3");
        retVal.put(contactOnePhone3, -1);

        TableMapClass tContactOneDescription = new TableMapClass("", "");
        DataMappingClass contactOneDescription = new DataMappingClass(tContactOneDescription, "Contact 1 Description");
        contactOneDescription.setUskedId("Contact 1 Description");
        retVal.put(contactOneDescription, -1);

        TableMapClass tContactOneEmail = new TableMapClass("employee", "employee_email");
        DataMappingClass contactOneEmail = new DataMappingClass(tContactOneEmail, "Contact 1 Email");
        contactOneEmail.setUskedId("Contact 1 Email");
        retVal.put(contactOneEmail, -1);

        TableMapClass tContactTwoPhone1 = new TableMapClass("employee", "employee_pager");
        DataMappingClass contactTwoPhone1 = new DataMappingClass(tContactTwoPhone1, "Contact 2 Phone 1");
        contactTwoPhone1.setUskedId("Contact 2 Phone 1");
        retVal.put(contactTwoPhone1, -1);

        TableMapClass tContactTwoPhone2 = new TableMapClass("", "");
        DataMappingClass contactTwoPhone2 = new DataMappingClass(tContactTwoPhone2, "Contact 2 Phone 2");
        contactTwoPhone2.setUskedId("Contact 2 Phone 2");
        retVal.put(contactTwoPhone2, -1);

        TableMapClass tContactTwoPhone3 = new TableMapClass("employee", "employee_cell");
        DataMappingClass contactTwoPhone3 = new DataMappingClass(tContactTwoPhone3, "Contact 2 Phone 3");
        contactTwoPhone3.setUskedId("Contact 2 Phone 3");
        retVal.put(contactTwoPhone3, -1);

        TableMapClass tContactTwoDescription = new TableMapClass("", "");
        DataMappingClass contactTwoDescription = new DataMappingClass(tContactTwoDescription, "Contact 2 Description");
        contactTwoDescription.setUskedId("Contact 2 Description");
        retVal.put(contactTwoDescription, -1);

        TableMapClass tContactTwoEmail = new TableMapClass("employee", "employee_email2");
        DataMappingClass contactTwoEmail = new DataMappingClass(tContactTwoEmail, "Contact 2 Email");
        contactTwoEmail.setUskedId("Contact 2 Email");
        retVal.put(contactTwoEmail, -1);

        //TODO: Rate Codes
        TableMapClass tRateOneHourType = new UskedRateInfoColumn("", "first_rate_hour");
        DataMappingClass rateOneHourType = new DataMappingClass(tRateOneHourType, "Rate 1 Hour Type");
        rateOneHourType.setUskedId("Rate 1 Hour Type");
        retVal.put(rateOneHourType, -1);

        TableMapClass tRateOneRateCode = new UskedRateInfoColumn("", "first_rate_code");
        DataMappingClass rateOneRateCode = new DataMappingClass(tRateOneRateCode, "Rate 1 Rate Code");
        rateOneRateCode.setUskedId("Rate 1 Rate Code");
        retVal.put(rateOneRateCode, -1);

        TableMapClass tRateOneCompCode = new UskedRateInfoColumn("", "first_comp_code");
        DataMappingClass rateOneCompCode = new DataMappingClass(tRateOneCompCode, "Rate 1 Comp Code");
        rateOneCompCode.setUskedId("Rate 1 Comp Code");
        retVal.put(rateOneCompCode, -1);

        TableMapClass tRateOneRegPay = new UskedRateInfoColumn("", "first_pay");
        DataMappingClass rateOneRegPay = new DataMappingClass(tRateOneRegPay, "Rate 1 Regular Pay Rate");
        rateOneRegPay.setUskedId("Rate 1 Regular Pay Rate");
        retVal.put(rateOneRegPay, -1);

        TableMapClass tRateOneOvertimePay = new UskedRateInfoColumn("", "first_over_pay");
        DataMappingClass rateOneOvertimePay = new DataMappingClass(tRateOneOvertimePay, "Rate 1 Overtime Pay Rate");
        rateOneOvertimePay.setUskedId("Rate 1 Overtime Pay Rate");
        retVal.put(rateOneOvertimePay, -1);

        TableMapClass tRateOneDblTimePay = new UskedRateInfoColumn("", "first_dbl_pay");
        DataMappingClass rateOneDblTimePay = new DataMappingClass(tRateOneDblTimePay, "Rate 1 Double Time Pay Rate");
        rateOneDblTimePay.setUskedId("Rate 1 Double Time Pay Rate");
        retVal.put(rateOneDblTimePay, -1);

        TableMapClass tRateOneRegTimeBill = new UskedRateInfoColumn("", "first_bill");
        DataMappingClass rateOneRegTimeBill = new DataMappingClass(tRateOneRegTimeBill, "Rate 1 Regular Bill Rate");
        rateOneRegTimeBill.setUskedId("Rate 1 Regular Bill Rate");
        retVal.put(rateOneRegTimeBill, -1);

        TableMapClass tRateOneOvertimeTimeBill = new UskedRateInfoColumn("", "first_over_bill");
        DataMappingClass rateOneOvertimeTimeBill = new DataMappingClass(tRateOneOvertimeTimeBill, "Rate 1 Overtime Bill Rate");
        rateOneOvertimeTimeBill.setUskedId("Rate 1 Overtime Bill Rate");
        retVal.put(rateOneOvertimeTimeBill, -1);

        TableMapClass tRateOneDblTimeBill = new UskedRateInfoColumn("", "");
        DataMappingClass rateOneDblTimeBill = new DataMappingClass(tRateOneDblTimeBill, "Rate 1 Double Time Bill Rate");
        rateOneDblTimeBill.setUskedId("Rate 1 Double Time Bill Rate");
        retVal.put(rateOneDblTimeBill, -1);

        TableMapClass tRateTwoHourType = new UskedRateInfoColumn("", "second_rate_hour");
        DataMappingClass rateTwoHourType = new DataMappingClass(tRateTwoHourType, "Rate 2 Hour Type");
        rateTwoHourType.setUskedId("Rate 2 Hour Type");
        retVal.put(rateTwoHourType, -1);

        TableMapClass tRateTwoRateCode = new UskedRateInfoColumn("", "second_rate_code");
        DataMappingClass rateTwoRateCode = new DataMappingClass(tRateTwoRateCode, "Rate 2 Rate Code");
        rateTwoRateCode.setUskedId("Rate 2 Rate Code");
        retVal.put(rateTwoRateCode, -1);

        TableMapClass tRateTwoCompCode = new UskedRateInfoColumn("", "second_comp_code");
        DataMappingClass rateTwoCompCode = new DataMappingClass(tRateTwoCompCode, "Rate 2 Comp Code");
        rateTwoCompCode.setUskedId("Rate 2 Comp Code");
        retVal.put(rateTwoCompCode, -1);

        TableMapClass tRateTwoRegPay = new UskedRateInfoColumn("", "second_pay");
        DataMappingClass rateTwoRegPay = new DataMappingClass(tRateTwoRegPay, "Rate 2 Regular Pay Rate");
        rateTwoRegPay.setUskedId("Rate 2 Regular Pay Rate");
        retVal.put(rateTwoRegPay, -1);

        TableMapClass tRateTwoOvertimePay = new UskedRateInfoColumn("", "second_over_pay");
        DataMappingClass rateTwoOvertimePay = new DataMappingClass(tRateTwoOvertimePay, "Rate 2 Overtime Pay Rate");
        rateTwoOvertimePay.setUskedId("Rate 2 Overtime Pay Rate");
        retVal.put(rateTwoOvertimePay, -1);

        TableMapClass tRateTwoDblTimePay = new UskedRateInfoColumn("", "second_dbl_pay");
        DataMappingClass rateTwoDblTimePay = new DataMappingClass(tRateTwoDblTimePay, "Rate 2 Double Time Pay Rate");
        rateTwoDblTimePay.setUskedId("Rate 2 Double Time Pay Rate");
        retVal.put(rateTwoDblTimePay, -1);

        TableMapClass tRateTwoRegTimeBill = new UskedRateInfoColumn("", "second_bill");
        DataMappingClass rateTwoRegTimeBill = new DataMappingClass(tRateTwoRegTimeBill, "Rate 2 Regular Bill Rate");
        rateTwoRegTimeBill.setUskedId("Rate 2 Regular Bill Rate");
        retVal.put(rateTwoRegTimeBill, -1);

        TableMapClass tRateTwoOvertimeTimeBill = new UskedRateInfoColumn("", "second_over_bill");
        DataMappingClass rateTwoOvertimeTimeBill = new DataMappingClass(tRateTwoOvertimeTimeBill, "Rate 2 Overtime Bill Rate");
        rateTwoOvertimeTimeBill.setUskedId("Rate 2 Overtime Bill Rate");
        retVal.put(rateTwoOvertimeTimeBill, -1);

        TableMapClass tRateTwoDblTimeBill = new UskedRateInfoColumn("", "");
        DataMappingClass rateTwoDblTimeBill = new DataMappingClass(tRateTwoDblTimeBill, "Rate 2 Double Time Bill Rate");
        rateTwoDblTimeBill.setUskedId("Rate 2 Double Time Bill Rate");
        retVal.put(rateTwoDblTimeBill, -1);

        //TODO: Notes, lower priority
        TableMapClass tNoteType1 = new TableMapClass("", "");
        DataMappingClass noteType1 = new DataMappingClass(tNoteType1, "Note 1 Type");
        noteType1.setUskedId("Note 1 Type");
        retVal.put(noteType1, -1);

        TableMapClass tNotes1 = new TableMapClass("", "");
        DataMappingClass notes1 = new DataMappingClass(tNotes1, "Note 1 Notes");
        notes1.setUskedId("Note 1 Notes");
        retVal.put(notes1, -1);

        TableMapClass tNoteType2 = new TableMapClass("", "");
        DataMappingClass noteType2 = new DataMappingClass(tNoteType2, "Note 2 Type");
        noteType2.setUskedId("Note 2 Type");
        retVal.put(noteType2, -1);

        TableMapClass tNotes2 = new TableMapClass("", "");
        DataMappingClass notes2 = new DataMappingClass(tNotes2, "Note 2 Notes");
        notes2.setUskedId("Note 2 Notes");
        retVal.put(notes2, -1);

        TableMapClass tNoteType3 = new TableMapClass("", "");
        DataMappingClass noteType3 = new DataMappingClass(tNoteType3, "Note 3 Type");
        noteType3.setUskedId("Note 3 Type");
        retVal.put(noteType3, -1);

        TableMapClass tNotes3 = new TableMapClass("", "");
        DataMappingClass notes3 = new DataMappingClass(tNotes3, "Note 3 Notes");
        notes3.setUskedId("Note 3 Notes");
        retVal.put(notes3, -1);

        TableMapClass tNoteType4 = new TableMapClass("", "");
        DataMappingClass noteType4 = new DataMappingClass(tNoteType4, "Note 4 Type");
        noteType4.setUskedId("Note 1 Type");
        retVal.put(noteType4, -1);

        TableMapClass tNotes4 = new TableMapClass("", "");
        DataMappingClass notes4 = new DataMappingClass(tNotes4, "Note 4 Notes");
        notes4.setUskedId("Note 4 Notes");
        retVal.put(notes4, -1);

        TableMapClass tNoteType5 = new TableMapClass("", "");
        DataMappingClass noteType5 = new DataMappingClass(tNoteType5, "Note 5 Type");
        noteType5.setUskedId("Note 5 Type");
        retVal.put(noteType5, -1);

        TableMapClass tNotes5 = new TableMapClass("", "");
        DataMappingClass notes5 = new DataMappingClass(tNotes5, "Note 5 Notes");
        notes5.setUskedId("Note 5 Notes");
        retVal.put(notes5, -1);

        TableMapClass tBranchId = new TableMapClass("", "");
        DataMappingClass branchIdObj = new DataMappingClass(tBranchId, "Branch Id");
        branchIdObj.setUskedId("Branch Id");
        retVal.put(branchIdObj, -1);

        helperUskedMapMethod(retVal, "", "", "Tracking 1 Type");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Category");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Date1");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Date2");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Date3");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Date4");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Text1");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Text2");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Text3");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Text4");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Amount1");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Amount2");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Amount3");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Amount4");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Logical1");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Logical2");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Logical3");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Logical4");
        helperUskedMapMethod(retVal, "", "", "Tracking 1 Notes");

        helperUskedMapMethod(retVal, "", "", "Tracking 2 Type");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Category");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Date1");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Date2");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Date3");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Date4");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Text1");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Text2");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Text3");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Text4");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Amount1");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Amount2");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Amount3");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Amount4");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Logical1");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Logical2");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Logical3");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Logical4");
        helperUskedMapMethod(retVal, "", "", "Tracking 2 Notes");

        helperUskedMapMethod(retVal, "", "", "Tracking 3 Type");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Category");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Date1");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Date2");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Date3");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Date4");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Text1");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Text2");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Text3");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Text4");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Amount1");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Amount2");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Amount3");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Amount4");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Logical1");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Logical2");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Logical3");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Logical4");
        helperUskedMapMethod(retVal, "", "", "Tracking 3 Notes");

        helperUskedMapMethod(retVal, "", "", "Start/Restart DD On");
        helperUskedMapMethod(retVal, "", "", "State Resident/Non-Resident");
        helperUskedMapMethod(retVal, "", "", "City Resident/Non-Resident");

        TableMapClass tAddedOn = new UskedCurrentDateColumn("", "");
        DataMappingClass addedOn = new DataMappingClass(tAddedOn, "Added On");
        addedOn.setUskedId("Added On");
        retVal.put(addedOn, -1);

        return retVal;
    }

    private class FindEmployeeBySocialSecurityQuery extends GeneralQueryFormat {
        public FindEmployeeBySocialSecurityQuery() {

        }

        public boolean hasAccess() {
            return true;
        }

        @Override
        public boolean hasPreparedStatement() {
            return true;
        }

        @Override
        public String getPreparedStatementString() {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT employee_id FROM EMPLOYEE ");
            sb.append("WHERE employee_ssn = ?");
            return sb.toString();
        }
    }

    class EmployeeDataObject {

        ArrayList<DynamicDataPoint> dynamicData = new ArrayList<DynamicDataPoint>();
        HashMap<String, String> dataMap = new HashMap<String, String>();
        ArrayList<String> errorList = new ArrayList<String>();

        public void addDynamicData(DynamicFieldDef def, String val) {
            this.dynamicData.add(new DynamicDataPoint(def, val));
        }

        public void addError(String msg) {
            this.errorList.add(msg);
        }

        public void addData(String col, String val) {
            this.dataMap.put(col, val);
        }

        public String getValueFromCol(String col) {
            return this.dataMap.get(col);
        }
    }

    class DynamicDataPoint {

        DynamicFieldDef dynamicDef;
        String value;
        Integer emp_id;

        private DynamicDataPoint(DynamicFieldDef def, String val) {
            this.dynamicDef = def;
            this.value = val;
        }
    }

    class getDyanmicFieldDef extends GeneralQueryFormat {
        //query to return all dynamic fields associated with a schema

        @Override
        public boolean hasAccess() {
            return true;
        }

        public String toString() {
            return "SELECT * FROM dynamic_field_def ";
        }
    }
}
