/*
 * RMIScheduleServer.java
 *
 * Created on January 7, 2005, 12:03 PM
 */

package rmischeduleserver;

import schedfoxlib.model.util.Record_Set;
import java.rmi.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import javax.swing.ImageIcon;

import rmischeduleserver.SocketConnection.UpdateClientClass;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.security.Security_Exception;

import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.data_connection_types.ServerSideConnection;
import rmischeduleserver.data_connection_types.ClientVector;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
//import rmischeduleserver.util.MailMessage;
/**
 *
 * @author ira
 */
public interface RMIScheduleServer extends Remote {
    
    public void shutDownServer(String MyIp)             throws RemoteException;
    public String registerClient(ClientConnection newClient) throws RemoteException;
    public void unregisterClient(String ipOfClient)     throws RemoteException;
    public void reloadClientDatabases()                 throws RemoteException, SQLException, Security_Exception;
    
    public void closeSession(String MyIp)                                   throws RemoteException;
    public void initializeSession(GeneralQueryFormat gqf, String MyIp)      throws RemoteException;
    public void setConnectionObject(ServerSideConnection myConnection)      throws RemoteException;
    public void setInfo(String location, String data, DataInputStream dos)  throws RemoteException;  
    public void sendMessage(String message, String md5) throws RemoteException;
    public ClientVector getConnectedClients() throws RemoteException;
    public String amIConnected() throws RemoteException;
    public void reastablishEntryForClientOnServer(SocketClients clientInfo) throws RemoteException;
    public void setScheduleServer(ClientConnection classToUpdate) throws RemoteException;

    public TimeZone getServerTimeZone() throws RemoteException;
    public long getServerCurrentTimeMillis() throws RemoteException;
    
    public void createDatabase(String databaseName)                         throws RemoteException;
    
    public void validateConnection() throws SQLException, RemoteException;
    
    public ImageIcon getImage(ImageQuery gqf) throws RemoteException;
    public void saveImage(ImageQuery gqf, ImageIcon image) throws RemoteException;
    public void deleteImage(ImageQuery gqf) throws RemoteException;
    
//    public boolean sendMail(MailMessage mail) throws RemoteException;
    
    /* create new db */
    public String createNewAccount(String db) throws RemoteException;
    
    /* The SQL Execute Stuff */
    public void executeUpdate(GeneralQueryFormat gqf, String MyIp)        
         throws SQLException, RemoteException, Security_Exception;
    
    public Record_Set executeQuery(GeneralQueryFormat gqf, String MyIp)           
         throws SQLException, RemoteException, Security_Exception;
    
    public ArrayList executeQueryEx(RunQueriesEx gqf, String MyIp)
         throws SQLException, RemoteException, Security_Exception;
    
    /* Depricated */
    public Record_Set getInfo(String location, String data, String element) throws RemoteException;
    
}
