/*
 * employee_return_by_ssn_query.java
 *
 * Created on January 26, 2005, 1:04 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class find_employees_by_characteristics_query extends GeneralQueryFormat {
    
    private String Social;
    private String fname;
    private String lname;
    private String hdate;
    private String tdate;
    private String city;
    private String zip;
    private String email;
    private String phone;
    
    /** Creates a new instance of employee_return_by_ssn_query */
    public find_employees_by_characteristics_query() {
        myReturnString = new String();
        Social = "";
        fname = "";
        lname = "";
        hdate = "";
        tdate = "";
        city = "";
    }
    
    public void update(String Social, String fname, String lname, String hdate, String tdate, String city,
            String zip, String email, String phone) {

         this.Social = Social.trim();
         this.fname = fname.trim();
         this.lname = lname.trim();
         this.hdate = hdate.trim();
         this.tdate = tdate.trim();
         this.city = city.trim();
         this.zip = zip.trim();
         this.email = email.trim();
         this.phone = phone.trim();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return "SELECT * FROM f_find_employees_by_characteristics_query('" + Social + "','" +
                fname + "','" + lname + "','" + hdate + "','" + tdate + "','" + city + "','" +
                zip + "','" + email + "','" + phone + "');";
    }
    
    private String addAndsAsNeeded(String... myFields) {
        String myRetString = "WHERE ";
        if (myFields.length == 0) {
            return "";
        }
        int firstNonBlank = 0;
        for (int i = 0; i < myFields.length; i++) {
            if (myFields[i].length() > 0) {
                if (firstNonBlank == 0) {
                    myRetString += myFields[i];
                    firstNonBlank++;
                } else {
                    myRetString += " AND " + myFields[i];
                }
            }
        }
        return myRetString;
    }
    
}
