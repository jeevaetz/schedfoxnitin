/*
 * mysqldriver.java
 *
 * Created on January 18, 2005, 8:59 AM
 */
package rmischeduleserver.mysqlconnectivity;

import schedfoxlib.model.util.Record_Set;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import rmischeduleserver.*;
import rmischeduleserver.data_connection_types.*;

import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author ira
 */
public class mysqldriver extends sqldriver {

    public static final String ip = IPLocationFile.getLOCATION_OF_POSTGRES_SERVER();
    public static final String properties = "&allowMultiQueries=true&continueBatchOnError=true&autoReconnect=true"; //Add &ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory
    public static final String pw = "?user=schedfox_user&password=sch3df0x1ng";
    public static final String connectString = "jdbc:postgresql:" + mysqldriver.ip +
            IPLocationFile.getDATABASE_NAME() + mysqldriver.pw + mysqldriver.properties;
    private RMIScheduleServerImpl myDataConnection;
    private BasicDataSource source;

    public mysqldriver(RMIScheduleServerImpl myDataConnParent) throws Exception {
        myDataConnection = myDataConnParent;

        connectToServer();

        getCompanyInfo();
        getBranchInfo();
    }

    public mysqldriver() {
    }

    @Override
    public String getCurrentTimeSQL() {
        return "(NOW())";
    }

    @Override
    public String getCurrentDateSQL() {
        return "(DATE(NOW()))";
    }

    @Override
    public RMIScheduleServerImpl getDataConn() {
        return myDataConnection;
    }

    @Override
    public Record_Set executeQuery(GeneralQueryFormat gqf) throws SQLException{
        Connection myConn = getConnectionPool().getConnection();
        java.sql.Statement stmt = null;
        PostgresRecord_Set myRecords = new PostgresRecord_Set();
        gqf.setDriver(this);
        String time = saveTime(gqf);
        try {
            if (gqf.hasPreparedStatement()) {
                String statement = getCompanySchema(gqf) + gqf.getPreparedStatementString();
                stmt = myConn.prepareStatement(statement, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                stmt.setFetchSize(10000);
                gqf.setPreparedStatement(((PreparedStatement) stmt), 1);
                ((PreparedStatement) stmt).execute();
            } else {
                stmt = myConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                stmt.setFetchSize(10000);
                stmt.execute(getCompanySchema(gqf) + gqf.toString());

            }
            if (stmt.getMoreResults()) {
                myRecords = new PostgresRecord_Set(stmt.getResultSet());
                myRecords.branch = gqf.getBranch();
                myRecords.company = gqf.getCompany();
            } else if (stmt.getMoreResults()) {
                myRecords = new PostgresRecord_Set(stmt.getResultSet());
                myRecords.branch = gqf.getBranch();
                myRecords.company = gqf.getCompany();
            }
            myRecords.generateAndCompress();
            myRecords.lu = time;
        } catch (Exception e) {
            e.printStackTrace();
            getDataConn().myGUI.printError(e.toString());
            getDataConn().myGUI.printMessage("(Execute Query) Detected SQL Error, SQL follows: ");
            getDataConn().myGUI.printMessage(gqf.toString());
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {}
            myConn.close();
        }
        return myRecords;
    }

    public ArrayList<Record_Set> executeQueryEx(RunQueriesEx gqf) throws SQLException {
        ArrayList<Record_Set> myReturnRecords = new ArrayList(8);
        Connection myConn = getConnectionPool().getConnection();
        java.sql.Statement stmt = null;
        Record_Set myRecords = new Record_Set();
        gqf.setDriver(this);
        String time = saveTime(gqf);
        String query = gqf.toString();
        try {
            if (gqf.hasPreparedStatement()) {
                String statement = getCompanySchema(gqf) + gqf.getPreparedStatementString();
                stmt = myConn.prepareStatement(statement, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                stmt.setFetchSize(10000);
                gqf.setPreparedStatement(((PreparedStatement)stmt), 1);
                ((PreparedStatement)stmt).execute();
            } else {
                stmt = myConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                stmt.setFetchSize(10000);
                stmt.execute(getCompanySchema(gqf) + query);
            }
            ResultSet currResultSet = null;
            while (stmt.getMoreResults(java.sql.Statement.CLOSE_CURRENT_RESULT) || stmt.getUpdateCount() > -1) {
                if (stmt.getUpdateCount() == -1) {
                    currResultSet = stmt.getResultSet();
                    if (currResultSet != null) {
                        try {
                            currResultSet.findColumn("f_set_path");
                        } catch (Exception exe) {
                            myRecords = new Record_Set(currResultSet); 
                            myRecords.branch = gqf.getBranch();
                            myRecords.company = gqf.getCompany();
                            myRecords.generateAndCompress();
                            myRecords.lu = time;
                            myReturnRecords.add(myRecords);
                            try {
                                currResultSet.close();
                            } catch (Exception throwAway) {
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            getDataConn().myGUI.printError(e.toString());
            getDataConn().myGUI.printMessage("(Execute QueryEX) Detected SQL Error, SQL follows: ");
            getDataConn().myGUI.printMessage(query);
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {}
            myConn.close();
        }
        return myReturnRecords;
    }

    public void executeUpdate(GeneralQueryFormat gqf) throws SQLException {
        Connection myConn = getConnectionPool().getConnection();
        java.sql.Statement stmt = myConn.createStatement();
        PreparedStatement pstmt = null;
        try {
            if (gqf.isTableCreationQuery()) {
                stmt.executeUpdate(gqf.toString());
                this.getCompanyInfo();
                this.getBranchInfo();
            } else {
                if (gqf.hasPreparedStatement()) {
                    String statement = getCompanySchema(gqf) + gqf.getPreparedStatementString();
                    pstmt = myConn.prepareStatement(statement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    gqf.setPreparedStatement(pstmt, 1);
                    pstmt.execute();
                } else {
                    stmt = myConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    stmt.execute(getCompanySchema(gqf) + gqf.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (pstmt != null) {
                System.out.println(pstmt.toString());
            }
            getDataConn().myGUI.printError(e.toString());
            getDataConn().myGUI.printMessage("(Update Query) Detected SQL Error, SQL follows: ");
            getDataConn().myGUI.printMessage(gqf.toString());
        } finally {
            myConn.close();
            try {
                stmt.close();
            } catch (Exception e) {}
        }
    }

    public void connectToServer() throws Exception {
        try {
            source = new BasicDataSource();
            source.setDriverClassName("org.postgresql.Driver");
            source.setUrl(connectString);
            source.setUsername("db_user");
            source.setPassword("sh0rtNsw33t");
            source.setMaxOpenPreparedStatements(-1);
//            source.setMaxActive(5);
//            source.setMaxIdle(2);
//            source.setInitialSize(2);
            source.setMaxActive(-1);
            source.setTestOnBorrow(true);
            source.setValidationQuery("SELECT 1;");
        } catch (Exception e) {
            throw e;
        }
        //source.setInitialConnections(4);
        //source.setMaxConnections(10);
    }

    public String getCompanySchema(GeneralQueryFormat gqf) {
        int i = -1;
        try {
            i = getCompanies().get(gqf.getCompany());
        } catch (Exception e) {
        }
        if (i > -1) {
            return "set local search_path='" + getCompanyDB()[i] + "';";
        } else {
            return "select control_db.f_set_path('a');";
        }
    }

    @Override
    public Connection generateConnection() throws SQLException {
        return getConnectionPool().getConnection();
    }

    @Override
    public DataSource getConnectionPool() {
        return this.source;
    }

    @Override
    public String generateSQLTOAddMinutes(String inputVal, String minutes) {
        return "(" + inputVal + " + interval '1 min' * " + minutes + "::integer)";
    }
}

