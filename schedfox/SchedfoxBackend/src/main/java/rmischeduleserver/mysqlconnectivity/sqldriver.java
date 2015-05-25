/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.sql.DataSource;
import rmischeduleserver.IPLocationFile;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.data_connection_types.StringIntHashtable;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.management.GetBranchesQuery;
import rmischeduleserver.mysqlconnectivity.queries.management.GetCompaniesQuery;
import rmischeduleserver.security.Security_Exception;

/**
 *
 * @author user
 */
public abstract class sqldriver {

    public static final String ip = IPLocationFile.getLOCATION_OF_POSTGRES_SERVER();
    public static final String properties = "&allowMultiQueries=true&continueBatchOnError=true";
    //public static final String ip = "//localhost/";
    public static final String pw = "?user=db_user&password=sh0rtNsw33t";

    private String manageDatabase;
    private String manageSchema;
    private StringIntHashtable myCompanies;
    private StringIntHashtable myBranches;
    private String[] myCompanyDatabase;

    private PreparedStatement getTimeQuery = null;
    
    private static sqldriver instance;

    public abstract void connectToServer() throws Exception;

    public abstract Connection generateConnection() throws SQLException ;

    public abstract DataSource getConnectionPool();

    public abstract RMIScheduleServerImpl getDataConn();

    public abstract Record_Set executeQuery(GeneralQueryFormat gqf) throws SQLException;

    public abstract ArrayList<Record_Set> executeQueryEx(RunQueriesEx gqf) throws SQLException;

    public abstract void executeUpdate(GeneralQueryFormat gqf) throws SQLException;

    public abstract String generateSQLTOAddMinutes(String inputVal, String minutes);

    public static sqldriver getNewSQLDriver(RMIScheduleServerImpl myDataConnParent) throws Exception {
        if (instance == null) {
            instance = new mysqldriver(myDataConnParent);
        }
        return instance;
    }

    /**
     * Returns a date object cast for the SQL server you are connected to, input type is
     * assumed to be YYYY-MM-DD
     * @param inputDate
     * @return
     */
    public String toDate(String inputDate) {
        return "DATE('" + inputDate + "')";
    }

    public Date getTime() {
        Date retVal = new Date();
        ResultSet rs = null;
        Connection timeConnection = null;
        PreparedStatement getTimeQuery = null;
        try {
            timeConnection = getConnectionPool().getConnection();
            getTimeQuery = timeConnection.prepareStatement("SELECT NOW()",  ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = getTimeQuery.executeQuery();
            rs.next();
            retVal = rs.getDate(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                getTimeQuery.close();
            } catch (Exception e) {}
            try {
                timeConnection.close();
            } catch (Exception e) {}
        }
        return retVal;
    }

    public String saveTime(GeneralQueryFormat gqf) {
        String time = "";
        ResultSet rs = null;
        Connection timeConnection = null;
        
        try {
            timeConnection = getConnectionPool().getConnection();
            getTimeQuery = timeConnection.prepareStatement("SELECT localtimestamp",  ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rs = getTimeQuery.executeQuery();
            rs.next();
            time = rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                timeConnection.close();
            } catch (Exception e) {}
        }
        return time;
    }

    public StringIntHashtable getCompanies() {
        return this.myCompanies;
    }

    public String[] getCompanyDB() {
        return this.myCompanyDatabase;
    }

    public void getCompanyInfo() throws SQLException {
        GetCompaniesQuery myQuery = new GetCompaniesQuery();
        Connection myConn = getConnectionPool().getConnection();
        java.sql.Statement stmt = myConn.createStatement();
        ResultSet myCompany = null;
        try {
            System.out.println(myQuery.toString());
            myCompany = stmt.executeQuery(myQuery.toString());
            java.util.Vector vMyCompanyDb      = new java.util.Vector();
            java.util.Vector vCompanyConn      = new java.util.Vector();
            myCompanies              = new StringIntHashtable(10);
            int i = 0;
            while(myCompany.next()){
                myCompanies.add(myCompany.getString("company_id"), i);
                vMyCompanyDb.add(myCompany.getString("company_db"));
                i++;
            }
            if(i > 0){
                myCompanyDatabase        = new String[i];
                for(i=0;i<vMyCompanyDb.size();i++){
                    myCompanyDatabase[i]        = (String) vMyCompanyDb.get(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                myConn.close();
                myCompany.close();
            } catch (Exception e) {}
        }
    }

    public void getBranchInfo() throws SQLException {
        int branchSize = 0;

        GetBranchesQuery myQuery    = new GetBranchesQuery();
        Connection myConn = getConnectionPool().getConnection();
        java.sql.Statement stmt = myConn.createStatement();
        try {

            ResultSet myBranch          = stmt.executeQuery(myQuery.toString());

            myBranches = new StringIntHashtable(10);

            while(myBranch.next()){
                myBranches.add(
                        myBranch.getString("branch_name"),
                        myBranch.getInt("branch_id")
                        );
            }
        } catch (Exception e) {

        } finally {
            myConn.close();
        }
    }

    public void setCompanyPath(GeneralQueryFormat gqf){
        Connection myConn = null;
        java.sql.Statement sql = null;
        try {
            myConn = getConnectionPool().getConnection();
            sql = myConn.createStatement();
        } catch (Exception e) {}

        try {
            if(sql != null) {
                if(myCompanies == null){
                    sql.execute("set session search_path to " + manageSchema + ";");
                } else {
                    sql.execute(getCompanySchema(gqf));
                }
            }
        } catch (Exception e) {
            System.out.println("Problem setting company path!");
        } finally {
            try {
                myConn.close();
            } catch (Exception e) {}
        }
    }

    /**
     * Pass in Company ID as a String, get a Company Database....Fun Fun...
     */
    public abstract String getCompanySchema(GeneralQueryFormat gqf);

    public String getCompanyDB(GeneralQueryFormat gqf) {
	System.out.println(">>>>> getCompanyDB-gqf>>:"+gqf);
        int i = -1;
        try { i = myCompanies.get(gqf.getCompany()); }
        catch (Exception ex) { }

        return myCompanyDatabase[i];
    }

    public void dispose() {
        try {
            
        } catch (Exception e) {}
    }

    /**
     * Returns the SQL for getting current time from the DB
     * @return String SQL Statement
     */
    public abstract String getCurrentTimeSQL();

    /**
     * Returns the SQL for getting current date from the DB
     * @return String sql statement
     */
    public abstract String getCurrentDateSQL();

    public String getTableName(String tableName) {
        return tableName;
    }

    public boolean checkSec(GeneralQueryFormat gqf) throws Security_Exception{

        if(!RMIScheduleServerImpl.validateQuery(gqf)) {
            Security_Exception se = new Security_Exception(gqf.getMD5(), "ERROR");
            throw se;
        }

        return true;
    }

    private Record_Set executeCheck(GeneralQueryFormat gqf) throws SQLException, Security_Exception {
        Connection myConn = getConnectionPool().getConnection();
        Record_Set myRecords = new Record_Set();
        java.sql.Statement stmt = myConn.createStatement();
        try {
            myRecords = new Record_Set(stmt.executeQuery(gqf.toString()));
            myRecords.branch = gqf.getBranch();
            myRecords.company = gqf.getCompany();
        } catch (Exception e) {
            getDataConn().myGUI.printError(e.toString());
            getDataConn().myGUI.printMessage("(Execute Query) Detected SQL Error, SQL follows: ");
            getDataConn().myGUI.printMessage(gqf.toString());
        } finally {
            myConn.close();
        }
        return myRecords;
    }
}
