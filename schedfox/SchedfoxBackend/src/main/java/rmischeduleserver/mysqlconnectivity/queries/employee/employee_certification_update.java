/*
 * employee_certification_update.java
 *
 * Created on May 26, 2005, 12:34 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class employee_certification_update extends GeneralQueryFormat {
    
    private String cert_id;
    private String emp_id;
    private boolean addingNew;
    private String start;
    private String end;
    
    /** Creates a new instance of employee_certification_update */
    public employee_certification_update() {
        myReturnString = new String();
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_EMPLOYEE_CERT;
    }
    
    public void update(String eid, String certid, boolean adding, String sdate, String edate) {
        cert_id = certid;
        emp_id = eid;
        addingNew = adding;
        start = sdate;
        end = edate;
    }
    
    public String toString() {
        return "SELECT f_employee_certification_update(" + emp_id + "," + cert_id + "," +
                addingNew + ",'" +  start + "'::date,'" + end + "'::date);";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
