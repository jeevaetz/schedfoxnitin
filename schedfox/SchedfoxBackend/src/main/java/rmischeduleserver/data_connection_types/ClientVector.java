/*
 * SocketClientsVector.java
 *
 * Created on February 15, 2005, 7:27 AM
 */

package rmischeduleserver.data_connection_types;
import java.util.*;
import java.io.*;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
/**
 *
 * @author ira
 */
public class ClientVector extends Vector<ClientConnection> implements Serializable {
    public ClientVector() {
        super();
    }
    
    /**
     * Smart little method, checks if Vector has an entry with this userid, if so it will
     * update the heartbeat information for that client. Otherwise it will just add the
     * whole client info to the Vector...   
     */
    public boolean add(SocketClients newClient) {
        if (!contains(newClient)) {
            super.add(newClient);
        } else {
            SocketClients tempCli;
            for (int i = 0; i < this.size(); i++) {
                tempCli = (SocketClients)super.get(i);
                if (tempCli.getUserDatabaseId().equals(newClient.getUserDatabaseId())) {
                    tempCli.updateThisClientInfoWithChannelInformation(newClient.getThisClientInfoChannelInformation());
                    return true;
                }
            }
        }
        return true;
    }
    
    /**
     * Used to replace the sockets for any clients that match the md5 given, this is used to
     * reconnect the client so that heartbeat will still be recieved for client.
     */
    public void resetConnection(SocketClients newClient) {
        for (int i = 0; i < size(); i++) {
            if (((SocketClients)(get(i))).userPasswordMD5.equals(newClient.userPasswordMD5)) {
                ((SocketClients)(get(i))).myInput = newClient.myInput;
                ((SocketClients)(get(i))).myOutput = newClient.myOutput;
            }
        }
    }
    
    /**
     * Should be used whenever a client has been deemed to be permanently disconnected from
     * the server, via either the unregister client method or any time out proceedure on the
     * socket end...
     */
    public void disconnectClientByUserId(String userId) {
        
        for (int i = 0; i < this.size(); i++) {
            if (this.getAT(i).getUserDatabaseId().equals(userId)) {
                try { 
                    ((SocketClients)this.getAT(i)).mySocketBufferObjectClass.myChannel.close();
                } catch(Exception ex) { }
                this.remove(i);
                return;
            }
        }
    }
    
    
    public synchronized ClientConnection remove(int i) {
        
        //SocketScheduleServer.getMyGUI().printMessage(this.getAT(i).userName + " is being disconnected.");
        try { 
            ((SocketClients)this.getAT(i)).mySocketBufferObjectClass.myChannel.close();
        } catch (Exception ex) { }
        ClientConnection returnVal = super.remove(i);
        //SocketScheduleServer.getMyGUI().printClientList(this);
        return returnVal;
    }
    
    
    public boolean contains(SocketClients cliToTest) {
        SocketClients tempCli;
        for (int i = 0; i < this.size(); i++) {
            tempCli = (SocketClients)super.get(i);
            if (tempCli.getUserDatabaseId().equals(cliToTest.getUserDatabaseId())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean contains(String name){
        SocketClients tempCli;
        for (int i = 0; i < this.size(); i++) {
            tempCli = (SocketClients)super.get(i);
            if (tempCli.userName.equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public void removeByMD5AndBranch(String md5, String branch, String company) {
        SocketClients tempCli;
        for (int i = 0; i < this.size(); i++) {
            tempCli = (SocketClients)super.get(i);
        }
    }
    
    /**
     * Adds a heartbeat query....
     */
    public void addHeartbeatQuery(GeneralQueryFormat myHeartbeatQuery) {
        ClientConnection tempCli;
        String myQueryType = "";
        if (myHeartbeatQuery.isScheduleQuery()) {
            myQueryType = SocketClients.SCHEDULE_QUERY;
        } else if (myHeartbeatQuery.isCheckInQuery()) {
            myQueryType = SocketClients.CHECKIN_QUERY;
        } else if (myHeartbeatQuery.isAvailabilityQuery()) {
            myQueryType = SocketClients.AVAILABILITY_QUERY;
        } else if (myHeartbeatQuery.isBannedQuery()) {
            myQueryType = SocketClients.BANNED_QUERY;
        } else if (myHeartbeatQuery.isCertificationQuery()) {
            myQueryType = SocketClients.CERTIFICATION_QUERY;
        } else if (myHeartbeatQuery.isEmployeeCertQuery()) {
            myQueryType = SocketClients.EMPLOYEE_CERT_QUERY;
        }
        if (myQueryType.length() > 0) {
            for (int i = 0; i < this.size(); i++) {
                tempCli = (ClientConnection)super.get(i);
                if (myHeartbeatQuery.getUser().equals(tempCli.getUserDatabaseId())) {
                    try {
                        tempCli.addHeartbeatQuery(myQueryType, (RunQueriesEx)myHeartbeatQuery);
                    } catch (java.lang.ClassCastException cce) {
                        //Throw away just a bad class that shouldn't be used
                    }
                }
            }
        }
    }
    
    public void remove(ObjectOutputStream inputS) {
        SocketClients tempCli;
        for (int i = 0; i < this.size(); i++) {
            tempCli = (SocketClients)super.get(i);
            if (tempCli.myOutput == (inputS)) {
                super.remove(i);
                return;
            }
        }
    }
    
    public void remove(String Name) {
        SocketClients tempCli;
        for (int i = 0; i < this.size(); i++) {
            tempCli = (SocketClients)super.get(i);
            if (tempCli.userName.equals(Name)) {
                super.remove(i);
            }
        }
    }
    
    public ClientConnection getAT(int i) {
        return (ClientConnection)super.get(i);
    }
    
    public SocketClients get(SocketCommandStructure command) {
        SocketClients tempCli;
        for (int i = 0; i < this.size(); i++) {
            tempCli = (SocketClients)super.get(i);
            if (tempCli.getUserDatabaseId().equals(command.userId)) {
                return (SocketClients)super.get(i);
            }
        }
        return null;
    }

}
