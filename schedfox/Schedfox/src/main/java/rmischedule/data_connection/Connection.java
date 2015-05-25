/*
 * RMIScheduleClientImpl.java
 *
 * Created on January 7, 2005, 1:57 PM
 */
package rmischedule.data_connection;

import schedfoxlib.model.util.Record_Set;
import java.net.*;
import java.rmi.*;
import java.util.*;

import rmischedule.main.*;
import rmischeduleserver.*;

import rmischedule.components.Branch_LB;
import rmischedule.components.Company_LB;

import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.data_connection_types.*;

/**
 *
 * @author ira
 */
public class Connection {

    rmischeduleserver.data_connection_types.ServerSideConnection myConn;
    Main_Window parent;
    Company_LB myCompanyLB;
    Branch_LB myBranchLB;
    public String myCompany;
    public String myBranch;
    private RMIScheduleServerImpl myServer;
    private String myIp;
    private String userId;
    private String managementId;
    String myManagementDatabase;
    private SocketClients myReconnectQueriesAndInfo;

    /** Creates a new instance of RMIScheduleClientImpl */
    public Connection() {
        myConn = new rmischeduleserver.data_connection_types.ServerSideConnection();
        myCompany = new String();
        myBranch = new String();
        managementId = new String();
        parent = Main_Window.parentOfApplication;
        myServer = parent.getServer();
        if (parent == null) {
            myManagementDatabase = "SchedData";
        } else {
            myManagementDatabase = parent.myManagementDatabase;
        }

        try {
            myIp = InetAddress.getLocalHost().getHostAddress();
            myServer.setConnectionObject(myConn);
            managementId = parent.getManagementId();
            userId = parent.getUser().getUserId();
        } catch (Exception e) {
        }
    }

    public RMIScheduleServerImpl getServer() {
        return this.myServer;
    }

    public void shutDownServer() {
        try {
            myServer.shutDownServer(myIp);
            System.out.println("Shut Down Server Successfully");
        } catch (Exception e) {
            System.out.println("Could not shut down server...");
        }
    }

    public String getManagementId() {
        return managementId;
    }

    public void setBranchLB(Branch_LB lbBranch) {
        myBranchLB = lbBranch;
    }

    public void setCompanyLB(Company_LB lbCompany) {
        myCompanyLB = lbCompany;
    }

    public void setCompany(String company) {
        myCompany = company;
    }

    public String getUser() {
        return userId;
    }

    /**
     * Gets the current server time in Milliseconds or returns -1 to indicate an
     * error was found while trying to get the server time...
     */
    public long getServerTimeMillis() {
        try {
            return myServer.getServerCurrentTimeMillis();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Gets the Time Zone from our RMI server...or returns client time zone if 
     * an RMI error happens...
     */
    public TimeZone getServerTimeZone() {
        try {
            return myServer.getServerTimeZone();
        } catch (Exception e) {
            return TimeZone.getDefault();
        }
    }

    public void setBranch(String branch) {
        myBranch = branch;
    }

    public void createDatabase(String databaseName) throws RemoteException {
        try {
            myServer.createDatabase(databaseName);
        } catch (Exception e) {
        }
    }

    public void prepQuery(GeneralQueryFormat gqf) {
        gqf.setMD5(Main_Window.gqfId);
        gqf.setBranch(getBranch());
        gqf.setCompany(getCompany());
        gqf.setManagement(getManagementDatabase());
        gqf.setManagementId(getManagementId());
        try {
            gqf.setUser(Main_Window.parentOfApplication.getUser().getUserId());
        } catch (Exception e) {
            gqf.setUser("-1");
        }
    }

    public void setManagementDatabase(String ManagementDatabase) {
        myManagementDatabase = ManagementDatabase;
    }

    public String getManagementDatabase() {
        return myManagementDatabase;
    }

    /**
     * Used to execute a single query that returns one record set...
     */
    public Record_Set executeQuery(GeneralQueryFormat gqf) {
        if (Main_Window.parentOfApplication.mySocketConn != null) {
            Main_Window.parentOfApplication.mySocketConn.sendPingToServer();
        }

        gqf.setMD5(Main_Window.gqfId);

        Record_Set myRecordSet = null;
        prepQuery(gqf);
        try {
            if (Main_Window.parentOfApplication != null) {
                Main_Window.parentOfApplication.addNewHeartbeatQueryToMainCopy(gqf);
            }
            myRecordSet = myServer.executeQuery(gqf, myIp);
            myRecordSet.decompressData();
        } catch (Exception e) {
            e.printStackTrace();
            Main_Window.terminateSchedFox("SchedFox has encountered an unexpected problem.  SchedFox is closing this session.  Please re-sign in.", "Unhandled Error!");
        } finally {
            gqf.clear();
        }

        return myRecordSet;
    }

    /**
     * Used to execute a query that does not return any values. IE: an update
     * query
     */
    public void executeUpdate(GeneralQueryFormat gqf) {
        if (Main_Window.parentOfApplication.mySocketConn != null) {
            Main_Window.parentOfApplication.mySocketConn.sendPingToServer();
        }

        gqf.setMD5(Main_Window.gqfId);

        prepQuery(gqf);
        try {
            if (Main_Window.parentOfApplication != null) {
                Main_Window.parentOfApplication.addNewHeartbeatQueryToMainCopy(gqf);
            }
            myServer.executeUpdate(gqf, myIp);
        } catch (Exception e) {
            e.printStackTrace();
            Main_Window.terminateSchedFox("SchedFox has encountered an unexpected problem.  SchedFox is closing this session.  Please re-sign in.", "Unhandled Error!");
        } finally {
            gqf.clear();
        }
    }

    /**
     * Used to execute a RunQueriesEx object which is really a query consisting
     * of multiple queries, returns an arraylist of Record_Sets...
     */
    public ArrayList<Record_Set> executeQueryEx(RunQueriesEx gqf) {
        return executeQueryEx(gqf, true);
    }

    public ArrayList<Record_Set> executeQueryEx(RunQueriesEx gqf, boolean useDialog) {
        if (Main_Window.parentOfApplication.mySocketConn != null) {
            Main_Window.parentOfApplication.mySocketConn.sendPingToServer();
        }

        gqf.setMD5(Main_Window.gqfId);
        ArrayList<Record_Set> myReturnArray = null;
        prepQuery(gqf);
        try {
            if (Main_Window.parentOfApplication != null) {
                Main_Window.parentOfApplication.addNewHeartbeatQueryToMainCopy(gqf);
            }
            myReturnArray = myServer.executeQueryEx(gqf, myIp);
            for (int i = 0; i < myReturnArray.size(); i++) {
                try {
                    ((Record_Set) myReturnArray.get(i)).decompressData();
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Main_Window.terminateSchedFox("SchedFox has encountered an unexpected problem.  SchedFox is closing this session.  Please re-sign in.", "Unhandled Error!");
        } finally {
            gqf.clear();
        }

        return myReturnArray;
    }

    /**
     * Used to tell if RMIConnection is established, will return false if not, return
     * true if connection established...
     */
    public boolean validateConnection() {
        boolean returnVal = false;
        try {
            myServer.validateConnection();
            returnVal = true;
        } catch (Exception e) {
        }
        return returnVal;
    }

    public void initializeSession(GeneralQueryFormat gqf) {
        prepQuery(gqf);
        try {
            myServer.initializeSession(gqf, myIp);
        } catch (Exception e) {
        }
    }

    /**
     * Tells the server to grab all client database information from server
     * and build appropriate data structures to allow access to new databases.
     */
    public void relaodClientInfoOnServer() {
        try {
            myServer.reloadClientDatabases();
        } catch (Exception e) {
            System.out.println("Exception on trying to tell server to reload database info in reloadClientInfoOnServer()");
        }
    }

    public void closeSession() {
        try {
            myServer.closeSession(myIp);
            //parent.mySocketConn.closeConnection();
        } catch (Exception e) {

        }
    }

    private String getBranch() {
        String branch = myBranch;
        //try {
        if (myBranchLB != null) {
            branch = myBranchLB.getSelectedBranchId();
        }
        //} catch (Exception e) {}
        return branch;
    }

    private String getCompany() {
        String company = myCompany;
        try {
            if (myCompanyLB != null) {
                company = myCompanyLB.getSelectedCompanyId();
            }
        } catch (Exception e) {

        }
        return company;
    }

    public String toString() {
        return "Company Id = " + myCompany + " Branch Id = " + myBranch;
    }

    public ClientVector getConnectedClients() {
        try {
            return myServer.getConnectedClients();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendMessage(String message, String md5) {
        try {
            myServer.sendMessage(message, md5);
        } catch (Exception ex) {

        }
    }

    public String createNewDb(String Db) throws RemoteException {
        return myServer.createNewAccount(Db);
    }

}
