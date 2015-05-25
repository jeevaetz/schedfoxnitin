/*
 * DataSynchronizerThread.java
 *
 * Created on June 23, 2005, 7:29 AM
 */

package rmischedule.schedule.checkincheckout;

import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Company;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.*;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.data_connection_types.*;
import java.util.Calendar;
import rmischedule.data_connection.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import rmischedule.main.*;
import schedfoxlib.model.AssembleCheckinScheduleType;
import schedfoxlib.model.Branch;
/**
 *
 * @author ira
 *
 * This is an important class for the Check In, it is responsible for updating the
 * data of the check in every 5 to 10 minutes to knock off old shifts and load new ones
 * as time progresses...
 */
public class DataSynchronizerThread extends Thread {
    private static final int numMinutesToReloadData = 5;
    private static final int numMinutesInFutureToLoadData = 180;
    private CheckInCheckOutDataObject myDataObjectParent;
    
    
    /** Creates a new instance of DataSynchronizerThread */
    public DataSynchronizerThread(CheckInCheckOutDataObject myObject) {
        myDataObjectParent = myObject;
    }
    
    /**
     * THis is not really a happy method, I am dissatisfied with it since it creates multiple
     * connections to each Branch/Company instead of hitting just once, need to find a way to optimize
     * this shit...but is' damn hard w/ current query strucuture of the completeCheckInQuery...
     */
    public static Date getArrayListForAllCompanies(GeneralQueryFormat myQuery, ArrayList<AssembleCheckinScheduleType> arrayOfValues) {
        Vector<Company> companies = Main_Window.parentOfApplication.getListOfCompanies();
        Connection myConn = new Connection();
        Date lastUpdated = null;
        for (int i = 0; i < companies.size(); i++) {
            Company currCompany = companies.get(i);
            try {
                //We dont' care what branch set it to -1 so that the schedule query will pull for all branches
                myConn.setBranch("-1");
                myConn.setCompany(currCompany.getId());
                myConn.prepQuery(myQuery);
                Record_Set rst = myConn.executeQuery(myQuery);
                for (int r = 0; r < rst.length(); r++) {
                    AssembleCheckinScheduleType checkinType = new AssembleCheckinScheduleType(rst);
                    checkinType.setCompanyId(currCompany.getId());
                    arrayOfValues.add(checkinType);
                    rst.moveNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (arrayOfValues.size() > 0) {
            lastUpdated = arrayOfValues.get(0).getLu();
        }
        return lastUpdated;
    }

    
    /**
     * Stupid method only exists to seperate by branch and company...
     */
    public static ArrayList<Record_Set> getPrintableArrayListForAllCompanies(GeneralQueryFormat myQuery,
            Calendar myCalendar) {
        Vector<Company> myCompanies = Main_Window.parentOfApplication.getListOfCompanies();
        Connection myConn = new Connection();
        ArrayList<Record_Set> retVal = new ArrayList<Record_Set>();
        for (int i = 0; i < myCompanies.size(); i++) {
            Vector<Branch> myBranches = myCompanies.get(i).getBranches();
            for (int x = 0; x < myBranches.size(); x++) {
                myConn.setCompany(myCompanies.get(i).getId());
                myConn.setBranch(myBranches.get(x).getBranchId() + "");
                myQuery.setPreparedStatement(new Object[]{myCalendar.getTime(), myCalendar.getTime(),
                    myBranches.get(x).getBranchId(), -1, null, null, 1440});
                try {
                    myConn.prepQuery(myQuery);
                    retVal.add(myConn.executeQuery(myQuery));
                } catch (Exception e) {}
            }
        }
        return retVal;
    }

    @Override
    public void run() {
        while (true) {
            Calendar startCal = Calendar.getInstance();
            startCal.add(Calendar.DAY_OF_YEAR, -1);
            Calendar endCal = Calendar.getInstance();
            endCal.add(Calendar.DAY_OF_YEAR, 1);
            new_assemble_schedule_for_checkin_query myQuery = new new_assemble_schedule_for_checkin_query();
            myQuery.setPreparedStatement(new Object[]{startCal.getTime(), endCal.getTime(), 
                -1, -1, null, null, Main_Window.AMOUNT_TO_CHECKIN_BUFFER});

            ArrayList<AssembleCheckinScheduleType> scheduleData = new ArrayList<AssembleCheckinScheduleType>();
            getArrayListForAllCompanies(myQuery, scheduleData);
            myDataObjectParent.updateViaTimer(scheduleData);
            
            try { sleep(60000 * numMinutesToReloadData); } catch (Exception ex) { }
        }
    }
    
}
