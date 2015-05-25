/*
 * xSocketConnection.java
 *
 * Created on December 2, 2005, 7:50 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.data_connection;
import javax.swing.*;
import java.nio.channels.*;
import java.nio.*;
import java.net.*;
import java.io.*;
import java.util.*;

import rmischeduleserver.*;
import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischedule.main.*;
import rmischeduleserver.ErrorReporting.ErrorReporting;
import rmischeduleserver.SocketConnection.*;
/**
 *
 * @author Ira Juneau
 */
public class xSocketConnection implements UpdateClientClass {
    
    private SocketChannel mySocketChannel;
    private SocketBufferObjectClass myBufferObjectClass;
    private Selector myChannelSelector;
    private boolean isUpAndRunning;
    private ByteBuffer pingBuffer;
    public static boolean doPing = false;
    private long lastPing;
    
    private int myNum;
    
    /** Creates a new instance of xSocketConnection */
    public xSocketConnection() throws IOException {
        exSocketConnection();
    }

    public void exSocketConnection() throws IOException {
        if (Main_Window.parentOfApplication.mySocketConn != null) {
            Main_Window.parentOfApplication.mySocketConn.dispose();
        }
        myNum = Main_Window.mySocketThreadNumber++;

        for (int i = 0; i < IPLocationFile.getPortSocketNumbersToTry().length; i++) {
            mySocketChannel = createSocketToServer(IPLocationFile.getPortSocketNumbersToTry()[i]);
            if (mySocketChannel != null) {
                break;
            }
        }

        myChannelSelector = Selector.open();
        mySocketChannel.register(myChannelSelector, SelectionKey.OP_READ);

        isUpAndRunning = true;
        myBufferObjectClass = new SocketBufferObjectClass();
        myBufferObjectClass.updateActiveChannel(mySocketChannel);
        sendObjectToServer(Main_Window.parentOfApplication.getMyHeartbeatInfo());
        processAllOperations myThread = new processAllOperations();
        myDetectDisc myPingThread = new myDetectDisc();
        
        this.pingBuffer = ByteBuffer.allocateDirect(4);
        this.pingBuffer.putInt(SocketBufferObjectClass.MARK_AS_A_PING_PACKET);
        this.pingBuffer.flip();
        this.lastPing = 0;
        
        myThread.start();
        myPingThread.start();
    }

    
    /**
     * Actually creates Socket conn or returns null if unsucessful...
     */ 
    public SocketChannel createSocketToServer(int portNum) {
        try {
            SocketChannel sockToServer = SocketChannel.open(new InetSocketAddress(IPLocationFile.getLOCATION_OF_RMI_SERVER(), portNum));
            sockToServer.configureBlocking(false);
            return sockToServer;
        } catch (Exception e) {
            return null;
        }
    }
    
    
    public synchronized void sendPingToServer() {
        if(doPing) {
            try {
                this.myBufferObjectClass.myChannel.write(this.pingBuffer);
                this.lastPing = System.currentTimeMillis();
                this.pingBuffer.rewind();
            } catch(IOException e) {
                if(isUpAndRunning) {
                    ErrorReporting.sendErrorReport(e, Main_Window.parentOfApplication.myUser.getLogin());
                }
                Main_Window.terminateSchedFox("The socket connection has been lost.  SchedFox is closing this session.  Please re-sign in.", "Ping Failed!");
            }
        }
    }
    
    /**
     * Thread to only process new Connections! Also sets up read and write since
     * accept will give you a socketchannel which you can process Reads and Writes From
     * ServerChannel will not allow this only allows accepts...
     */
    private class processAllOperations extends Thread {
        public void run() {
            while (true) {
                try {
                    while(myChannelSelector.select() > 0) {
                        Set selectedKeys = myChannelSelector.selectedKeys();
                        Iterator<SelectionKey> myIterator = selectedKeys.iterator();
                        
                        while (myIterator.hasNext()) {
                            SelectionKey currKey = myIterator.next();
                            int operation = currKey.readyOps();
                            if((operation & SelectionKey.OP_READ) == SelectionKey.OP_READ) processDataFromServer(currKey);                           
                            myIterator.remove();
                        }
                    }
                } catch (Exception ex) {
                    if(isUpAndRunning) {
                        ErrorReporting.sendErrorReport(ex, Main_Window.parentOfApplication.myUser.getLogin());
                    }
                    Main_Window.terminateSchedFox("The connection with the server has been lost.  SchedFox is closing this session.  Please re-sign in.", "Connection Lost!");
                }
            }
        }
    }

    
    /**
     * Small thread used to detect disconnects just writes to the server every so often
     */
    private class myDetectDisc extends Thread {
        public myDetectDisc() { }
        
        public void run() {
            while (true) {
                try { sleep(5000); } catch (InterruptedException e) { }
                if(System.currentTimeMillis() - lastPing > 25000) {
                    sendPingToServer();
                }
            }
        }
    }
    
    
    /**
     * Read incoming data from the server...
     */
    public void processDataFromServer(SelectionKey sk) {
            Object myObject = null;
            
            try { myObject = myBufferObjectClass.constructObjectFromDataInChannel(); }
            catch(Exception ex) { ErrorReporting.sendErrorReport(ex, Main_Window.parentOfApplication.myUser.getLogin()); }
            
            if (myObject != null) {
                if (myObject instanceof rmischeduleserver.data_connection_types.SocketCommandStructure) {
                    processServerCommand((SocketCommandStructure)myObject);
                }
                else if (myObject instanceof rmischeduleserver.SocketConnection.PingConnectionClass) { }
                else { ErrorReporting.sendErrorReport(new Exception("Unhandled object type parsed from server: " + myObject.getClass()), Main_Window.parentOfApplication.myUser.getLogin()); }
            } else {
                if (myBufferObjectClass.isClosedConnection()) {
                    try {
                        isUpAndRunning = false;
                        sk.cancel();
                        sk.channel().close();
                    } catch(IOException ex) { ErrorReporting.sendErrorReport(ex, Main_Window.parentOfApplication.myUser.getLogin()); }
                    Main_Window.terminateSchedFox("There was a problem reading data from the server.  SchedFox is closing this session.  Please re-sign in.", "Connection Lost!");
                }
            }
    }
    
    /**
     * Private function used to handle data incoming from server, should prolly thread to allow multiples
     * at the same time...
     */
    public void processServerCommand(SocketCommandStructure myCommand) {
        switch (myCommand.Command) {
            case GeneralQueryFormat.UPDATE_SCHEDULE:
                try {
                    Main_Window.myScheduleForm.updateDataViaServer((ArrayList)myCommand.getRecordSet(0));
                    Main_Window.ciw.updateCheckInWithScheduleData((ArrayList)myCommand.getRecordSet(0));
                } catch (Exception ex)  {
                    ErrorReporting.sendErrorReport(ex, Main_Window.parentOfApplication.myUser.getLogin());
                    ex.printStackTrace();
                }
                break;
            case GeneralQueryFormat.UPDATE_CHECK_IN:
                try { Main_Window.ciw.updateCheckInWithCheckInData((ArrayList)myCommand.getRecordSet(0)); }
                catch(Exception ex) { ErrorReporting.sendErrorReport(ex, Main_Window.parentOfApplication.myUser.getLogin()); }
                break;
            case GeneralQueryFormat.UPDATE_AVAILABILITY:
                try { Main_Window.myScheduleForm.updateAvailabilityViaServer((ArrayList)myCommand.getRecordSet(0)); }
                catch(Exception ex) { ErrorReporting.sendErrorReport(ex, Main_Window.parentOfApplication.myUser.getLogin()); }
                break;
            case GeneralQueryFormat.UPDATE_BANNED:
                try { Main_Window.myScheduleForm.updateBanned((ArrayList)myCommand.getRecordSet(0)); }
                catch(Exception ex) { ErrorReporting.sendErrorReport(ex, Main_Window.parentOfApplication.myUser.getLogin()); }
                break;
            case GeneralQueryFormat.UPDATE_CLIENT_CERT:
                try { Main_Window.myScheduleForm.updateClientCerts((ArrayList)myCommand.getRecordSet(0)); }
                catch(Exception ex) { ErrorReporting.sendErrorReport(ex, Main_Window.parentOfApplication.myUser.getLogin()); }
                break;
            case GeneralQueryFormat.UPDATE_EMPLOYEE_CERT:
                try { Main_Window.myScheduleForm.updateEmpCerts((ArrayList)myCommand.getRecordSet(0)); }
                catch(Exception ex) { ErrorReporting.sendErrorReport(ex, Main_Window.parentOfApplication.myUser.getLogin()); }
                break;
            case 7:
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, myCommand.message, "Message Recieved", JOptionPane.INFORMATION_MESSAGE);
                break;            
            case GeneralQueryFormat.KILL_CLIENT:
                Main_Window.terminateSchedFox(myCommand.message, "Shutting Down");
            default:
                ErrorReporting.sendErrorReport(new Exception("Unhandled Command recieved from server: " + myCommand.Command), Main_Window.parentOfApplication.myUser.getLogin());
                break;
        }
    }
    
    public boolean isUpAndRunning() {
        return isUpAndRunning;
    }
    
    /**
     * Method to kill any reading of data....and invalidate this socket connection...
     */
    public void dispose() {
        if (myNum != Main_Window.mySocketThreadNumber) {
            try {
                isUpAndRunning = false;
                myChannelSelector.close();
                myBufferObjectClass.setIsClosedConnection();
                mySocketChannel.socket().close();
                mySocketChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Send an Object to the server by running it through a Object Output Stream then
     * a Byte Output Stream through the channel...
     */
    public void sendObjectToServer(Object myObj) {
        try {
            ClientConnection myClient = Main_Window.parentOfApplication.getMyHeartbeatInfo();
            myBufferObjectClass.sendObjectThroughChannel(myClient);
        } catch (IOException e) {
            ErrorReporting.sendErrorReport(e, Main_Window.parentOfApplication.myUser.getLogin());
            System.out.println("Problem sending data to server...");
            e.printStackTrace();
        }
    }
    
}
