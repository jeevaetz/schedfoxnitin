/*
 * RunQueriesEx.java
 *
 * Created on March 9, 2005, 9:46 AM
 */

package rmischeduleserver.mysqlconnectivity.queries;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.sqldriver;
/**
 *
 * @author ira
 */
public class RunQueriesEx extends GeneralQueryFormat {
    
    private static final long serialVersionUID = 1L; //Serialization Version for class should speed things up
    private StringBuilder myBuffer;
    private int updateVal;
    private boolean containsScheduleQuery;
    private boolean containsCheckInQuery;
    private boolean containsBannedQuery;
    private boolean containsAvailabilityQuery;
    private ArrayList<GeneralQueryFormat> myQueriesVector;
    
    /** Creates a new instance of RunQueriesEx */
    public RunQueriesEx() {
        myReturnString = new String();
        myBuffer = new StringBuilder(200);
        myQueriesVector = new ArrayList(10);
        updateVal = 0;
        containsScheduleQuery = false;
        containsCheckInQuery = false;
        containsBannedQuery = false;
        containsAvailabilityQuery = false;
    }
    
    public void update(GeneralQueryFormat... myQueries) {
        for (int i = 0; i < myQueries.length; i++) {
            myQueriesVector.add(myQueries[i]);
            if (myQueries[i].isScheduleQuery()) {
                containsScheduleQuery = true;
            }
            if (myQueries[i].isCheckInQuery()) {
                containsCheckInQuery = true;
            }
        }
    }

    public void setCompany(String co) {
        super.setCompany(co);
        for (int q = 0; q < this.myQueriesVector.size(); q++) {
            myQueriesVector.get(q).setCompany(co);
        }
    }

    @Override
    public void setLastUpdated(String lu) {
        super.setLastUpdated(lu);
        for (int q = 0; q < this.myQueriesVector.size(); q++) {
            myQueriesVector.get(q).setLastUpdated(lu);
        }
    }

    public void clear() {
        myBuffer = new StringBuilder(200);
        for (int i = 0; i < myQueriesVector.size(); i++) {
            myQueriesVector.get(i).clear();
        }
        //myQueriesVector.removeAllElements();
    }
    
    public void setUser(String userId) {
        super.setUser(userId);
        for (int i = 0; i < myQueriesVector.size(); i++) {
            myQueriesVector.get(i).setUser(userId);
        }
    }
    
    /**
     * Adds another query...
     */
    public void add(GeneralQueryFormat myQuery) {
        if (myQuery instanceof RunQueriesEx) {
            RunQueriesEx toAddQueries = (RunQueriesEx)myQuery;
            for (int q = 0; q < toAddQueries.getSize(); q++) {
                myQueriesVector.add(toAddQueries.getQueryAt(q));
            }
        } else {
            myQueriesVector.add(myQuery);
        }
        if (myQuery.getUpdateStatus() > 0) {
            updateVal = myQuery.getUpdateStatus();
        }
        if (myQuery.isScheduleQuery()) {
            containsScheduleQuery = true;
        }
        if (myQuery.isCheckInQuery()) {
            containsCheckInQuery = true;
        }
        if (myQuery.isBannedQuery()) {
            containsBannedQuery = true;
        }
        if (myQuery.isAvailabilityQuery()) {
            this.containsAvailabilityQuery = true;
        }
    }

    public void setDriver(sqldriver driver) {
        super.setDriver(driver);
        for (int q = 0; q < this.myQueriesVector.size(); q++) {
            myQueriesVector.get(q).setDriver(driver);
        }
    }

    /**
     * Originally Developed for checkin, allows you to have queries with multiple branches, maybe 
     * companies not sure, since they are toString already...
     */
    public void addString(GeneralQueryFormat myQuery) {
        if (myQuery.getUpdateStatus() > 0) {
            updateVal = myQuery.getUpdateStatus();
        }
        if (myQuery.isScheduleQuery()) {
            containsScheduleQuery = true;
        }
        if (myQuery.isCheckInQuery()) {
            containsCheckInQuery = true;
        }
        myBuffer.append(myQuery.toString() + ";");
    }
    
    public boolean isScheduleQuery() {
        return containsScheduleQuery;
    }
    
    public boolean isCheckInQuery() {
        return containsCheckInQuery;
    }
    
    public boolean isAvailabilityQuery() {
        return containsAvailabilityQuery;
    }
    
    public int getUpdateStatus() {
        return updateVal;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        boolean retVal = true;
        if (myQueriesVector.size() == 0) {
            retVal = false;
        }
        for (int i = 0; i < myQueriesVector.size(); i++) {
            retVal = retVal && myQueriesVector.get(i).hasPreparedStatement();
        }
        return retVal;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        for (int i = 0; i < myQueriesVector.size(); i++) {
            sql.append(myQueriesVector.get(i).getPreparedStatementString()).append(";");
        }
        return sql.toString();
    }

    @Override
    public int setPreparedStatement(PreparedStatement pstmt, int start) throws SQLException {
        int runningPos = start;
        for (int i = 0; i < myQueriesVector.size(); i++) {
            runningPos = myQueriesVector.get(i).setPreparedStatement(pstmt, runningPos);
        }
        return start;
    }

    /**
     * Used by our hearbeat...
     */
    public void updateTime(String LU) {
        for (int i = 0; i < myQueriesVector.size(); i++) {
            ((GeneralQueryFormat)myQueriesVector.get(i)).updateTime(LU);
        }
    }
    
    public void clearOnServerSide() {
        myBuffer = new StringBuilder(200);
    }
    
    public String getAt(int i) {
        return ((GeneralQueryFormat)myQueriesVector.get(i)).toString();
    }

    public GeneralQueryFormat getQueryAt(int i) {
        return myQueriesVector.get(i);
    }

    public int getSize() {
        return myQueriesVector.size();
    }
    
    public String toString() {
        String myString;
        for (int i = 0; i < myQueriesVector.size(); i++) {
            myBuffer.append(((GeneralQueryFormat)myQueriesVector.get(i)).toString() + ";");
        }
        
        myString = myBuffer.toString();
        clearOnServerSide();
        return myString;
    }
    
}
