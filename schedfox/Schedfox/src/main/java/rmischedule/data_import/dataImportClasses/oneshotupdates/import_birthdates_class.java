/*
 * import_birthdates_class.java
 *
 * Created on March 2, 2006, 10:26 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.data_import.dataImportClasses.oneshotupdates;
import rmischedule.data_import.dataImportClasses.usked.*;
import rmischedule.data_import.dataImportClasses.*;
import java.util.*;

import rmischeduleserver.mysqlconnectivity.queries.generic_import.*;
/**
 *
 * @author Ira Juneau
 */
public class import_birthdates_class {
    
    /** Creates a new instance of import_birthdates_class */
    public import_birthdates_class() {
    }
    
    public void updateBirthdatesForCompany(String companyId, String branchId, String uskedPath) {
        long startTime = System.currentTimeMillis();
        uskedDataSource mySource;
        Vector<SourceToDestinationClass> myVectOfScheduleFields = new Vector();
        mySource = new uskedDataSource(uskedPath);
        ArrayList<SourceToDestinationClass> myEmployeeColumns = new ArrayList();
        myEmployeeColumns.add(GetDataFromSource.employeeBirthdate);
        myEmployeeColumns.add(GetDataFromSource.employeeId);
        rmischedule.data_connection.Connection myConn = new rmischedule.data_connection.Connection();
        do {
            ArrayList<String> getEmployeeInfo = mySource.getEmployeeTable().getDataFromRow(myEmployeeColumns);
            int pos = 0;
            if (!getEmployeeInfo.get(0).trim().equals("1000-10-10 00:00:00")) {
                generic_import_update_query myImportQuery = new generic_import_update_query();
                String bdate = getEmployeeInfo.get(0).split(" ")[0];
                myImportQuery.update(myEmployeeColumns.get(1).getDestinationTable(), myEmployeeColumns.get(pos).getDestinationName(), bdate);
                myImportQuery.setWhereClause("WHERE employee_id IN (SELECT employee_id FROM usked_employee WHERE usked_emp_id = '" + getEmployeeInfo.get(++pos) + "') AND branch_id = " + branchId + " ", "employee");
                myConn.setCompany(companyId);
                try {
                    myConn.executeQuery(myImportQuery);
                } catch (Exception e) {}
            }
        } while (mySource.getEmployeeTable().moveNext());
        long endTime = System.currentTimeMillis();
        System.out.println("Done! Time in Milliseconds to parse: " + (endTime - startTime) + "ms");
    }
    
}
