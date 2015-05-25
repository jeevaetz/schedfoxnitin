/*
 * ImportCCSDataEmployee.java
 *
 * Created on January 18, 2005, 1:13 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.ImportCCS;

import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.ArrayList;
/**
 *
 * @author ira
 */
public class ImportCCSDataEmployee extends GeneralQueryFormat {

    private ArrayList uskedId;
    private ArrayList Fname;
    private ArrayList Lname;
    private ArrayList Mname;
    private ArrayList Phone;
    private ArrayList Phone2;
    private ArrayList Cell;
    private ArrayList Page;
    private ArrayList Address;
    private ArrayList Address2;
    private ArrayList City;
    private ArrayList State;
    private ArrayList Zip;
    private ArrayList Ssn;
    private ArrayList Email;
    private ArrayList Hiredate;
    private ArrayList Termdate;
    private ArrayList Deleted;
    
    /** Creates a new instance of ImportCCSDataEmployee */
    public ImportCCSDataEmployee() {
        myReturnString = new String();
        clear();
    }
    
    public void update(String uskedid, String branch, String fname, String lname, String mname, String phone, String phone2,
            String cell, String page, String address, String address2, String city, String state,
            String zip,  String ssn, String email, String hiredate, String termdate, String deleted) {

        if (phone.length() > 19) {
            phone = phone.substring(0, 19);
        }
        uskedId.add(uskedid);
        Fname.add(fname);
        Lname.add(lname);
        Mname.add(mname);
        Phone.add(phone);
        Phone2.add(phone2);
        Cell.add(cell);
        Page.add(page);
        Address.add(address);
        Address2.add(address2);
        City.add(city);
        State.add(state);
        Zip.add(zip);
        Ssn.add(ssn);
        Email.add(email);
        Hiredate.add(hiredate);
        Termdate.add(termdate);
        Deleted.add(deleted);
    }
    
    public void clear() {
        uskedId = new ArrayList();
        Fname = new ArrayList();
        Lname = new ArrayList();
        Mname = new ArrayList();
        Phone = new ArrayList();
        Phone2 = new ArrayList();
        Cell = new ArrayList();
        Page = new ArrayList();
        Address = new ArrayList();
        Address2 = new ArrayList();
        City = new ArrayList();
        State = new ArrayList();
        Zip = new ArrayList();
        Ssn = new ArrayList();
        Email = new ArrayList();
        Hiredate = new ArrayList();
        Termdate = new ArrayList();
        Deleted = new ArrayList();
    }
    
    public String toString() {
        StringBuilder myBuilder = new StringBuilder();
        for (int i = 0; i < uskedId.size(); i++) {
            myBuilder.append("DELETE FROM employee WHERE employee_ssn = '" + Ssn.get(i) + "';" +
                    "INSERT INTO employee (employee_id, branch_id, employee_first_name, employee_last_name, " +
                    "employee_middle_initial, employee_phone, employee_phone2,  " +
                    "employee_cell, employee_pager, employee_ssn,  " +
                    "employee_address, employee_address2, employee_city, employee_state,  " +
                    "employee_zip,  employee_email, employee_hire_date, employee_is_deleted) VALUES (" +
                    "(CASE WHEN (SELECT (MAX(employee_id) + 1) FROM employee) IS NULL THEN 1 ELSE " +
                    "(SELECT (MAX(employee_id) + 1) FROM employee) END), " + getBranch() + ", '" + 
                    Fname.get(i) + "','" + Lname.get(i) + "','" + Mname.get(i) + "','" +
                    Phone.get(i) + "','" + Phone2.get(i) + "','" + Cell.get(i) + "','" + Page.get(i) + "','" + Ssn.get(i) + "','" +
                    Address.get(i) + "','" + Address2.get(i) + "','" + City.get(i) + "','" + State.get(i) + "','" +
                    Zip.get(i) + "','" + Email.get(i) + "','" + Hiredate.get(i) + "'," + Deleted.get(i) + ");" +
                    "DELETE FROM usked_employee WHERE emp_branch = " + getBranch() + " AND usked_emp_id = '" + uskedId.get(i) + "';" +
                    "INSERT INTO usked_employee(emp_branch, usked_emp_id, employee_id) VALUES (" +
                    "" + getBranch() + ",'" + uskedId.get(i) + "',(SELECT MAX(employee_id) FROM employee));");
        }
        return  myBuilder.toString();
    }

    public boolean hasAccess() {
        return true;
    }

}
