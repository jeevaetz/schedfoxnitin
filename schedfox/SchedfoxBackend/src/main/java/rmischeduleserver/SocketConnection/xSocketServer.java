/*
 * xSocketServer.java
 *
 * Created on December 2, 2005, 8:08 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.SocketConnection;
import java.nio.channels.spi.*;
import java.nio.channels.*;
import java.nio.*;
import java.net.*;
import rmischeduleserver.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import rmischeduleserver.ErrorReporting.ErrorReporting;
import rmischeduleserver.data_connection_types.*;
/**
 *
 * @author Ira Juneau
 * Credit to PKWooster...
 */
public class xSocketServer {
    
    private Selector myNewConnectionSelector;
    private ConnectionBackToServer socketServer;

    /** 
     * Creates a new instance of xSocketServer 
     * Sets up selectors and so on...
     */
    public xSocketServer(ConnectionBackToServer socketServer) {
        try {
            this.socketServer = socketServer;
            myNewConnectionSelector = Selector.open();
            for (int i = 0; i < IPLocationFile.getPortSocketNumbersToTry().length; i++) {
                createListeningSocketForConnections(IPLocationFile.getPortSocketNumbersToTry()[i]);
            }
            processAllOperations myProcessThread = new processAllOperations();
            sendPingToDetectDeadClients myDeadClientThread = new sendPingToDetectDeadClients();
            myDeadClientThread.start();
            myProcessThread.start();
        } catch (Exception e) { ErrorReporting.sendErrorReport(e, "Server"); }      
    }
    
    
    /**
     * Creates a socket for given port....
     */ 
    private void createListeningSocketForConnections(int port) {
        try {
            ServerSocketChannel mySocketServer = ServerSocketChannel.open();
            ServerSocket underLyingSocket = mySocketServer.socket();
            underLyingSocket.bind(new InetSocketAddress(IPLocationFile.getLOCATION_OF_RMI_SERVER(), port), 100);
            mySocketServer.configureBlocking(false);
            mySocketServer.register(myNewConnectionSelector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            Main.myServer.myGUI.printMessage("Could not create a socket listener for port: " + port);
        }
    }
    
    
    /**
     * Thread to only process new Connections! Also sets up read and write since
     * accept will give you a socketchannel which you can process Reads and Writes From
     * ServerChannel will not allow this only allows accepts...
     */
    private class processAllOperations extends Thread {
        
        public void run() {
            while(true) {
                try {
                    while (myNewConnectionSelector.select() > 0) {
                        Set selectedKeys = myNewConnectionSelector.selectedKeys();
                        Iterator<SelectionKey> myIterator = selectedKeys.iterator();
                        while (myIterator.hasNext()) {
                            try {
                                SelectionKey currKey = myIterator.next();
                                int operation = currKey.readyOps();
                                if((operation & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) doAccept(currKey);
                                if((operation & SelectionKey.OP_READ) == SelectionKey.OP_READ) doRead(currKey);
                            } catch (Exception ex) { ErrorReporting.sendErrorReport(ex, "Server"); }
                            myIterator.remove();
                        }
                    }
                } catch(IOException ex) { ErrorReporting.sendErrorReport(ex, "Server"); }
            }
        }
    }
    
    
    void doAccept(SelectionKey sk) {
        ServerSocketChannel sc = (ServerSocketChannel)sk.channel();
        SocketChannel clientChannel = null;
        try {
            clientChannel = sc.accept();
            clientChannel.configureBlocking(false);
            SocketBufferObjectClass mySocketBufferClass = new SocketBufferObjectClass();
            mySocketBufferClass.updateActiveChannel(clientChannel);
            clientChannel.register(myNewConnectionSelector, SelectionKey.OP_READ,  mySocketBufferClass);
            Main.myServer.myGUI.printMessage("Received A New Connection!");
        } catch(IOException ex){ ErrorReporting.sendErrorReport(ex, "Server"); }
    }
    
    
    void doRead(SelectionKey sk) {
        SocketBufferObjectClass myObjectBufferClass = (SocketBufferObjectClass)sk.attachment();
        
        if (myObjectBufferClass.myChannel.isConnected()) {
            Object myObject = null;
            
            try { myObject = myObjectBufferClass.constructObjectFromDataInChannel(); }
            catch (Exception ex) { if(!ex.getMessage().equals("An existing connection was forcibly closed by the remote host"))
                                       ErrorReporting.sendErrorReport(ex, "Server"); }
            
            if (myObject != null) {
                if(myObject instanceof rmischeduleserver.data_connection_types.SocketClients) {
                    SocketClients myNewConnectedClient = (SocketClients)myObject;
                    myNewConnectedClient.updateThisClientInfoWithChannelInformation(myObjectBufferClass);
                    myObjectBufferClass.updateWithUserInformation(myNewConnectedClient);
                    socketServer.getConnectedClients().add(myNewConnectedClient);
                }
                else if (myObject instanceof rmischeduleserver.SocketConnection.PingConnectionClass) {  }
                else { ErrorReporting.sendErrorReport(new Exception("Unhandled object type parsed from client " + myObjectBufferClass.myChannel.socket().getInetAddress() + ": " + myObject.getClass()), "Server"); }
            } else {
                try {
                    socketServer.getConnectedClients().disconnectClientByUserId(myObjectBufferClass.userId);
                    myObjectBufferClass.myChannel.close();
                    sk.cancel();
                } catch(IOException ex) { ErrorReporting.sendErrorReport(ex, "Server *** CONNECTION MIGHT NOT HAVE BEEN CLOSED PROPERLY ***"); }
            }
        }
    }
    
    
    private class sendPingToDetectDeadClients extends Thread {
        ByteBuffer myBuffer;
        
        public sendPingToDetectDeadClients() {
            myBuffer = ByteBuffer.allocateDirect(4);
            myBuffer.putInt(SocketBufferObjectClass.MARK_AS_A_PING_PACKET);
            myBuffer.flip();
        }
        
        public void run() {
            while (true) {
                try { sleep(30000); } catch (Exception e) { }
                for (int i = 0; i < socketServer.getConnectedClients().size(); i++) {
                    try { socketServer.getConnectedClients().getAT(i).sendPingPacketToThisClient(myBuffer); }
                    catch (IOException e) { socketServer.getConnectedClients().remove(i); }
                    finally { myBuffer.rewind(); }
                }
            }
        }
    }
    
}
