/*
 * CheckEmployeeExportForDuplicateUskedId.java
 *
 * Created on November 23, 2005, 9:34 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class CheckEmployeeExportForDuplicateUskedId extends GeneralQueryFormat {
    
    private String employeeId;
    private String uskedID;
    
    /** Creates a new instance of CheckEmployeeExportForDuplicateUskedId */
    public CheckEmployeeExportForDuplicateUskedId() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String eid, String uskedId) {
        employeeId = eid;
        uskedID = uskedId;
    }
    
    public String toString() {
    return "SELECT * FROM f_CheckEmployeeExportForDuplicateUskedId(" + employeeId + ",'"
                 + uskedID + "'," + getBranch()  +");"; 
        }
    
}
