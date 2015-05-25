/*
 * SocketScheduleServer.java
 *
 * Created on February 1, 2005, 1:01 PM
 */
package rmischeduleserver.SocketConnection;
import java.net.*;
import rmischeduleserver.GUI.*;
import java.io.*;
import java.util.ArrayList;
import rmischeduleserver.ErrorReporting.ErrorReporting;
import rmischeduleserver.data_connection_types.SocketCommandStructure;
import rmischeduleserver.data_connection_types.SocketClients;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.new_assemble_complete_schedule_query;
import rmischeduleserver.ActionLogging.LogWriterClass;
import java.util.concurrent.LinkedBlockingQueue;
import rmischeduleserver.data_connection_types.ClientConnection;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.sqldriver;
/**
 *
 * @author ira
 */
/**
 * COMMANDS
 * HEADERS
 * 0 - NO OP
 * 1 - MD5 TAG (Initialize Connection)
 * 2 - UPDATE TAG
 * 3 - MESSAGES TAG
 * 4 - CMDS
 * 5 - Kill Connection
 * 6 - Register Schedule Test
 * 7 - Kill Schedule Test
 */
public class SocketScheduleServer extends ConnectionBackToServer {
    
    private static int portNumber = 5000;
    private static final int maxNumberOfClients = 20000;

    final String[] enabledCipherSuites = { "SSL_RSA_WITH_RC4_128_MD5" };
    
    private ServerSocket myServer;
    private Socket mySocket;
    
    private ObjectOutputStream dos;
    private ObjectInputStream dis;
    private new_assemble_complete_schedule_query myScheduleQuery;
    
    private processCommand myCommandThread;
    public LogWriterClass myLogger;
    
    private xSocketServer socketServer;
    
    
    /** Creates a new instance of SocketScheduleServer */
    public SocketScheduleServer(ServerGUI myGui, sqldriver myD) {
        super(myGui, myD);
        myCommandThread = new processCommand();
        myCommandThread.start();
        socketServer = new xSocketServer(this);
    }
    
    
    
    /**
     * Invoked by RMIScheduleServerImpl on UpdateQuery where the query is marked as needing to tell clients
     * to update and command does not equal 0...
     */
    public void sendCommand(GeneralQueryFormat gqf, String time) {
        gqf.setRanTime(time);
        myCommandThread.addCommand(gqf);
        
    }
    
    public void addHearbeatItem(GeneralQueryFormat gqf) {
        getConnectedClients().addHeartbeatQuery(gqf);
    }
    
    
    public void sendMessage(String message, String userId) {
        for(int i = 0; i < getConnectedClients().size(); i++) {
            if(getConnectedClients().getAT(i).getUserDatabaseId().equals(userId)) {
                try { getConnectedClients().getAT(i).sendDataToThisClient(new SocketCommandStructure(message)); }
                catch(IOException ex) { ErrorReporting.sendErrorReport(ex, "Server"); }
            }
        }
    }

    @Override
    public void forceUpdate(GeneralQueryFormat query, SocketCommandStructure myCommand, ClientConnection currClient) {
        
    }

    
    private class processCommand extends Thread {
        
        private LinkedBlockingQueue<GeneralQueryFormat> commands;
        
        public processCommand() {
            commands = new LinkedBlockingQueue();
        }
        
        public void run() {
            while (true) {
                try { sleep(10); } catch (Exception e) {}
                
                if (commands.size() > 0 ) {
                    this.setPriority(Thread.MAX_PRIORITY);
                    GeneralQueryFormat gqf = commands.remove();
                    SocketCommandStructure myCommand = new SocketCommandStructure(gqf);
                    sendCommandToClients(myCommand);
                }
            }
        }
        
        public void addCommand(GeneralQueryFormat gqf) {
            commands.add(gqf);
        }
    }
    
    
    /**
     * Ugly ass method like much of this class...
     * VERY IMPORTANT ALWAYS CHECK IF MYQUERY2 IS NOT NULL BEFORE RUNNING QUERY OTHERWISE IT WILL CAUSE
     * A CONNECTION CREEP LIKE WE USE TO HAVE!
     *
     */
    public void sendCommandToClients(SocketCommandStructure myOriginalCommand) {
        ClientConnection currClient = null;
        int numOfClientsSentTo = 0;
        for (int i = getConnectedClients().size() - 1; i >= 0; i--) {
            try {
                SocketCommandStructure myCommand = new SocketCommandStructure(myOriginalCommand);
                currClient = getConnectedClients().getAT(i);
                boolean hasData = false;
                GeneralQueryFormat myQuery = null;
                if (myCommand.Command == GeneralQueryFormat.UPDATE_SCHEDULE) {
                    myQuery = currClient.getHeartbeatQuery(SocketClients.SCHEDULE_QUERY, myCommand.Branch, myCommand.Company);
                } else if (myCommand.Command == GeneralQueryFormat.UPDATE_CHECK_IN) {
                    myQuery = currClient.getHeartbeatQuery(SocketClients.CHECKIN_QUERY, myCommand.Branch, myCommand.Company);
                } else if (myCommand.Command == GeneralQueryFormat.UPDATE_AVAILABILITY) {
                    myQuery = currClient.getHeartbeatQuery(SocketClients.AVAILABILITY_QUERY, myCommand.Branch, myCommand.Company);
                } else if (myCommand.Command == GeneralQueryFormat.UPDATE_BANNED) {
                    myQuery = currClient.getHeartbeatQuery(SocketClients.BANNED_QUERY, myCommand.Branch, myCommand.Company);
                } else if (myCommand.Command == GeneralQueryFormat.UPDATE_CLIENT_CERT) {
                    myQuery = currClient.getHeartbeatQuery(SocketClients.CERTIFICATION_QUERY, myCommand.Branch, myCommand.Company);
                } else if (myCommand.Command == GeneralQueryFormat.UPDATE_EMPLOYEE_CERT) {
                    myQuery = currClient.getHeartbeatQuery(SocketClients.EMPLOYEE_CERT_QUERY, myCommand.Branch, myCommand.Company);
                } else if(myCommand.Command == GeneralQueryFormat.KILL_CLIENT) {
                    hasData = true;
                }
                
                if (myQuery != null) {
                    myQuery.updateTime(myCommand.lastUpdate);
                    if (myQuery instanceof RunQueriesEx) {
                        myCommand.setRecordSet(this.getSQLDriver().executeQueryEx((RunQueriesEx)myQuery));
                    } else {
                        ArrayList<Record_Set> arrayList = new ArrayList<Record_Set>();
                        Record_Set rst = this.getSQLDriver().executeQuery(myQuery);
                        arrayList.add(rst);
                        myCommand.setRecordSet(arrayList);
                    }
                    hasData = true;
                }
                
                if (hasData) {
                    if (!currClient.sendDataToThisClient(myCommand)) {
                        getMyGUI().printClientList(getConnectedClients());
                    } else {
                        numOfClientsSentTo++;
                    }
                }
            } catch (Exception e) {
                String userName = "";
                if(currClient != null) {
                   
                    getConnectedClients().disconnectClientByUserId(currClient.getUserDatabaseId());
                }
                ErrorReporting.sendErrorReport(e, "Server *** PROBLEM SENDING HEARTBEAT TO "  + "***");
            }
        }
    }    
    
}
