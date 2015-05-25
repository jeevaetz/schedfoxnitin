/*
 * SocketClients.java
 *
 * Created on February 14, 2005, 2:38 PM
 */

package rmischeduleserver.data_connection_types;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.SocketConnection.*;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import java.nio.*;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
/**
 *
 * @author ira
 *
 * This class is responsible for keeping the outputStream, inputStream, for the client
 * it also contains the client name and userPasswordMD5 which is used to lookup appropriate data, Also
 * it contains a Vector of all Schedule Queries which are used on schedule updates to provide
 * the client with updated data...same with the check in...
 */
public class SocketClients extends ClientConnection implements Serializable {
    //Fields to not serialize in transfer...
    public  transient ObjectOutputStream myOutput;
    public  transient ObjectInputStream myInput;
    private transient Vector<SocketCommandStructure> myQueueOfDataToSend;
    public transient SocketBufferObjectClass mySocketBufferObjectClass;
//    private transient sendDataToClient myThread;
    public  transient static final String CHECKIN_QUERY        = "Checkin";
    public  transient static final String SCHEDULE_QUERY       = "EEEESchedule";
    public  transient static final String EMPLOYEE_QUERY       = "Employee";
    public  transient static final String CLIENT_QUERY         = "Client";
    public  transient static final String BANNED_QUERY         = "Banned";
    public  transient static final String AVAILABILITY_QUERY   = "Availability";
    public  transient static final String CERTIFICATION_QUERY  = "Certification";
    public  transient static final String EMPLOYEE_CERT_QUERY  = "EmployeeCert";
    //End of fields not to serialize

    
    
    private boolean amIActive;


    /**
     * Should be accessed through the RMIClient side, to initialize this data and send it
     * to the server that will make certain that no one else is logged in under this
     * userId, unless it is a demo account or some other special thingy...
     */
    public SocketClients(String pw, String userID, String userName, String manageName, String realName, String myIp) {
        super();
        initValues(pw, userID, userName, manageName, realName, myIp);
    }

    public void updateThisClientInfoWithChannelInformation(SocketBufferObjectClass mySocketBufferClass) {
        mySocketBufferObjectClass = mySocketBufferClass;
    }

    public SocketBufferObjectClass getThisClientInfoChannelInformation(){
        return mySocketBufferObjectClass;
    }

    /**
     * Should be run from the socket server when info is received from the socket end of things
     * to tie in our output/input stream to the rest of our data...
     */
    public void updateClientWithDataFromSocket(ObjectOutputStream os, ObjectInputStream is) {
        myOutput = os;
        myInput = is;
        amIActive = true;
        myQueueOfDataToSend = new Vector();
    }

    /**
     * Used to send a ping to the client to detect dead connections, should only be used
     * with a byte buffer of length 4 bytes and containing an integer of type SocketBufferObjectClass.PING_PACKET
     */
    public void sendPingPacketToThisClient(ByteBuffer myBuf) throws IOException {
        if(mySocketBufferObjectClass.myChannel.write(myBuf) == 0)
            throw new IOException("Ping wrote 0 bytes");
    }

    /**
     * Sends Data to Client making certain that our ObjectOutputStream is only being used once... Returns
     * false if this client is no longer connected
     */
    public boolean sendDataToThisClient(SocketCommandStructure myCommand) throws IOException {
        mySocketBufferObjectClass.sendObjectThroughChannel(myCommand);
        return true;
    }

    
}
