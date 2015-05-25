/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.data_connection_types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import rmischeduleserver.SocketConnection.SocketBufferObjectClass;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;

/**
 *
 * @author user
 */
public abstract class ClientConnection {

    public String userPasswordMD5;
    public String userName;
    public String realName;
    private String userDatabaseId;
    public String rmiConnectionId;
    public String userIPConnectingFrom;
    public String managementName;

    private Hashtable<String, GeneralQueryFormat> myVectorOfHeartbeatQueries;

    public abstract void updateThisClientInfoWithChannelInformation(SocketBufferObjectClass mySocketBufferClass);

    public abstract SocketBufferObjectClass getThisClientInfoChannelInformation();

    public ClientConnection() {
        myVectorOfHeartbeatQueries = new Hashtable(50);
    }

    public void initValues(String pw, String userID, String userName, String manageName, String realName, String myIp) {
        userPasswordMD5 = pw;
        this.userName = userName;
        this.userDatabaseId = userID;
        this.userIPConnectingFrom = myIp;
        this.managementName = manageName;
        this.realName = realName;
    }

    /**
     * Used after a disconnect....
     */
    public void replaceOldHeartbeatWithNew(Hashtable<String, GeneralQueryFormat> newHash) {
        myVectorOfHeartbeatQueries = newHash;
    }
    
    /**
     * returns the hash of heartbeat queries
     * @return
     */
    public Hashtable<String, GeneralQueryFormat> getMyHashOfHeartbeatQueries() {
        return myVectorOfHeartbeatQueries;
    }

    /**
     * gets the hash table for the hearbeat
     */
    public Hashtable getHeartbeatQuery(){
        return myVectorOfHeartbeatQueries;
    }

    /**
     * gets the hash table for the hearbeat
     */
    public void setHeartbeatQuery(Hashtable h){
        myVectorOfHeartbeatQueries = h;
    }

    public String getUserDatabaseId() {
        return this.userDatabaseId;
    }
    
    /**
     * Gets a Heartbeat Query defined by given branch company, and the String defined by the
     * static in this class...
     */
    public GeneralQueryFormat getHeartbeatQuery(String type, String branch, String company) {
        String myString = type + " " + branch + ", " + company;
        return myVectorOfHeartbeatQueries.get(myString);
    }

    /**
     * Adds a heartbeat query as defined by the type as defined by the statics in this class
     * as defined...yeah...I am in a rut...
     */
    public void addHeartbeatQuery(String type, GeneralQueryFormat gqf) {
        String myString = type + " " + gqf.getBranch() + ", " + gqf.getCompany();
        GeneralQueryFormat doesExist = myVectorOfHeartbeatQueries.get(myString);
        if (doesExist == null) {
            myVectorOfHeartbeatQueries.put(myString, gqf);
        } else {
            myVectorOfHeartbeatQueries.remove(myString);
            myVectorOfHeartbeatQueries.put(myString, gqf);
        }
    }
    
    /**
     * Should be run from the socket server when info is received from the socket end of things
     * to tie in our output/input stream to the rest of our data...
     */
    public abstract void updateClientWithDataFromSocket(ObjectOutputStream os, ObjectInputStream is);

    public boolean equals(SocketClients toCompare) {
        return (userPasswordMD5.equals(toCompare.userPasswordMD5)) && (userName.equals(toCompare.userName));
    }

    /**
     * Used to send a ping to the client to detect dead connections, should only be used
     * with a byte buffer of length 4 bytes and containing an integer of type SocketBufferObjectClass.PING_PACKET
     */
    public abstract void sendPingPacketToThisClient(ByteBuffer myBuf) throws IOException;

    /**
     * Sends Data to Client making certain that our ObjectOutputStream is only being used once... Returns
     * false if this client is no longer connected
     */
    public abstract boolean sendDataToThisClient(SocketCommandStructure myCommand) throws IOException;

}
