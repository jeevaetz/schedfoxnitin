/*
 * RMIScheduleServerImpl.java
 *
 * Created on January 7, 2005, 12:03 PM
 */
package rmischeduleserver;

import java.rmi.*;
import java.io.*;
import java.rmi.server.*;
import javax.imageio.*;
import java.io.File;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import rmischeduleserver.ErrorReporting.ErrorReporting;
import rmischeduleserver.data_connection_types.*;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.data_connection_types.ServerSideConnection;
import rmischeduleserver.mysqlconnectivity.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
import rmischeduleserver.GUI.*;
import rmischeduleserver.security.Security_Exception;
import rmischeduleserver.data_connection_types.SocketCommandStructure;
import rmischeduleserver.SocketConnection.*;
import rmischeduleserver.data_connection_types.ClientVector;
import java.util.concurrent.ArrayBlockingQueue;

import java.util.*;

/**
 *
 * @author ira
 */
public class RMIScheduleServerImpl {

    String serverName;
    ServerSideConnection myConn;
    private static sqldriver MySqlConn;
    private static ConnectionBackToServer mySocketServer;
    public ServerGUI myGUI;
    private ArrayBlockingQueue myQueryUpdateQueue;
    private myUpdateQueryThread myUpdateThread; //Should there only be one instance of this thread or no?
    private static final String fileName = "mydump";
    private static final String activeSchema = "silverstar_db";
    private static HashMap connectionSecurityIds = new HashMap();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static RMIScheduleServerImpl scheduleServer;

    private RMIScheduleServerImpl() throws RemoteException {
        initServer();
    }

    public RMIScheduleServerImpl(boolean isBasic) {
        initBasicServer();
    }

    public static RMIScheduleServerImpl getInstance() {
        if (scheduleServer == null) {
            try {
                scheduleServer = new RMIScheduleServerImpl();
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
        return scheduleServer;
    }

    private void initBasicServer() {
        if (myGUI == null) {
            try { //Try to get graphical gui
                myGUI = new ServerCommandLineGUI();
                //myGUI.setVisible(true);
            } catch (Exception e) {
                myGUI = new ServerCommandLineGUI();
            }
        }
        serverName = "";


        try {
            if (MySqlConn == null) {
                MySqlConn = sqldriver.getNewSQLDriver(this);
                this.myGUI.printMessage("Connection to POSTGres successful");
            }
        } catch (Exception ex) {
            this.myGUI.printError(ex.toString());
            ex.printStackTrace();
        }
    }

    private void initServer() {
        myUpdateThread = new myUpdateQueryThread();
        myQueryUpdateQueue = new ArrayBlockingQueue(40);
        myUpdateThread.start();
        this.initBasicServer();
    }

    public static sqldriver getConnection() {
        if (MySqlConn == null) {
            RMIScheduleServerImpl server = RMIScheduleServerImpl.getInstance();
            server.initBasicServer();
        }
        return MySqlConn;
    }

    public void setScheduleServer(ClientConnection classToUpdate) throws RemoteException {
        mySocketServer = new LocalServer(myGUI, getSQLConnection());
        mySocketServer.getConnectedClients().add(classToUpdate);
    }

    public sqldriver getSQLConnection() {
        return MySqlConn;
    }

    public ConnectionBackToServer getSocketConnection() {
        return mySocketServer;
    }

    /**
     * Registers A Client As Used... If an error(s) are encountered they will be passed
     * back in the String [], otherwise method returns null to indicate no problems...
     */
    public String registerClient(ClientConnection newClient) throws RemoteException {
        myGUI.printMessage(newClient.userName + " Connected From " + newClient.userIPConnectingFrom);
        CleanHashMap();
        if (!newClient.userName.equals("demo") && (newClient instanceof SocketClients)) {
            for (int i = 0; i < mySocketServer.getConnectedClients().size(); i++) {
                if (mySocketServer.getConnectedClients().get(i).getUserDatabaseId().equals(newClient.getUserDatabaseId())) {
                    try {
                        SocketCommandStructure myCommand = new SocketCommandStructure(GeneralQueryFormat.KILL_CLIENT);
                        myCommand.message = "Somebody else has tried to log into this account.  SchedFox is closing your connection.";
                        mySocketServer.getConnectedClients().getAT(i).sendDataToThisClient(myCommand);
                    } catch (Exception e) {
                        ErrorReporting.sendErrorReport(e, "Server");
                        mySocketServer.getConnectedClients().remove(i);
                    }

                    return "This account is already logged in.  Having more than one user per account is a violation of the user agreement.  SchedFox is closing your connection.";
                }
            }
        }

        java.rmi.server.UID userId = new java.rmi.server.UID();
        RMIScheduleServerImpl.connectionSecurityIds.put("id:" + userId.toString(), newClient.getUserDatabaseId());
        return "id:" + userId.toString();
    }

    public static boolean validateQuery(GeneralQueryFormat gqf) {
        return true;
    }

    public static void CleanHashMap() {
        Iterator it = connectionSecurityIds.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String userId = (String) pairs.getValue();
            boolean good = false;
            try {
                for (int i = 0; i < mySocketServer.getConnectedClients().size(); i++) {
                    if (mySocketServer.getConnectedClients().getAT(i).getUserDatabaseId().equals(userId)) {
                        good = true;
                    }
                }
            } catch (Exception e) {
                //something broke for this guy.
                //potential memory leak.
                good = true;
            }
            if (!good) {
                connectionSecurityIds.remove(pairs.getKey());
            }
        }
    }

    /**
     * Unregisters A Client... (now disconnects them)
     */
    public void unregisterClient(String userId) throws RemoteException {
        SocketCommandStructure command = new SocketCommandStructure(GeneralQueryFormat.KILL_CLIENT);
        command.message = "You are being disconnected at an admin's request.";
        for (int i = 0; i < mySocketServer.getConnectedClients().size(); i++) {
            if (mySocketServer.getConnectedClients().getAT(i).getUserDatabaseId().equals(userId)) {
                try {
                    mySocketServer.getConnectedClients().getAT(i).sendDataToThisClient(command);
                } catch (Exception ex) {
                    ErrorReporting.sendErrorReport(ex, "RMIServer");
                }
            }
        }
    }

    public void dispose(String MyIp) {
        myGUI.printMessage("Disposing, cleaning up Ports and so on...");
        MySqlConn.dispose();
    }

    public ServerSideConnection getConnectionObject() throws RemoteException {
        return myConn;
    }

    public void setConnectionObject(ServerSideConnection myConnection) throws RemoteException {
        myConn = myConnection;
    }

    public Record_Set getInfo(String location, String data, String element) throws RemoteException {
        return myConn.getInfo(location, data, element);
    }

    public void setInfo(String location, String data, DataInputStream dos) throws RemoteException {
        myConn.setInfo(location, data, dos);
    }

    public ArrayList executeQueryEx(RunQueriesEx gqf, String MyIp) throws SQLException {
        if (!validateQuery(gqf)) {
            return null;
        }

        String time = MySqlConn.saveTime(gqf);
        ArrayList myReturn = MySqlConn.executeQueryEx(gqf);
        if (mySocketServer != null) {
            mySocketServer.addHearbeatItem(gqf);
            if (gqf.getUpdateStatus() != 0) {
                mySocketServer.sendCommand(gqf, time);
            }
        }
        return myReturn;
    }

    public String createNewAccount(String db) throws RemoteException {
        StringBuffer sb = new StringBuffer();
        try {
            java.net.URL url = new java.net.URL("http://www.schedfox.com/check_db1.php?db=" + db);
            java.net.URLConnection connection = url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            int b = 0;
            while ((b = bis.read()) != -1) {
                sb.append((char) b);
            }
            if (bis != null) {
                bis.close();
            }

            Main.myServer.reloadClientDatabases();
        } catch (SQLException ex) {
            return "error";
        } catch (Security_Exception ex) {
            return "error";
        } catch (IOException ex) {
            return "error";
        }

        return sb.toString();
    }

    public Record_Set executeQuery(GeneralQueryFormat gqf, String MyIp) throws SQLException {
        if (!validateQuery(gqf)) {
            return null;
        }

        String time = MySqlConn.saveTime(gqf);
        Record_Set rs = MySqlConn.executeQuery(gqf);
        if (mySocketServer != null && gqf.getUpdateStatus() != 0) {
            mySocketServer.sendCommand(gqf, time);
        }
        if (mySocketServer != null) {
            mySocketServer.addHearbeatItem(gqf);
        }
        return rs;
    }

    public void executeUpdate(GeneralQueryFormat gqf, String MyIp) throws SQLException {
        if (!validateQuery(gqf)) {
            return;
        }

        String time = MySqlConn.saveTime(gqf);
        MySqlConn.executeUpdate(gqf);
        if (mySocketServer != null && gqf.getUpdateStatus() != 0) {
            mySocketServer.sendCommand(gqf, time);
        }
    }

    public void initializeSession(GeneralQueryFormat gqf, String MyIp) throws RemoteException {
    }

    public void closeSession(String MyIp) throws RemoteException {
    }

    public void shutDownServer(String MyIp) throws RemoteException {
        try {
            Naming.unbind(serverName);
        } catch (Exception e) {
            ErrorReporting.sendErrorReport(e, "RMIServer");
            myGUI.printError("In Shut Down Server" + e.toString());
        }
        this.dispose(MyIp);
    }

    public ClientVector getConnectedClients() throws RemoteException {
        return mySocketServer.getConnectedClients();
    }

    public void sendMessage(String message, String userId) throws RemoteException {
        mySocketServer.sendMessage(message, userId);
    }

    /**
     * Dumps database strucute from our dbase, then create new database, then deletes 
     * flat file sql...
     *
     * m4 -Dsilverstar_db=new_db mydump > mynewdump
     * 
     * depricated
     */
    public void createDatabase(String databaseName) throws RemoteException {
        Runtime myRunTime = Runtime.getRuntime();
        StringBuilder myBuilder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("newschemadump")));
            String currString = null;
            do {
                currString = br.readLine();
                if (currString != null) {
                    myBuilder.append(currString);
                }
            } while (currString != null);
            br.close();

            this.executeUpdate(new GenericQuery(myBuilder.toString().replaceAll("newschemadb", databaseName)), "");
        } catch (Exception e) {
            ErrorReporting.sendErrorReport(e, "RMIServer");
            e.printStackTrace();
            this.myGUI.printError(e.toString());
        }
    }

    /**
     * Function to get an image from the server.  This is being used for employee pictures... the GQF passed in
     * should have a string that only contains the employee ID that we want the picture for.  Returns an ImageIcon
     * because they're serializable.
     */
    public ImageIcon getImage(ImageQuery gqf) throws RemoteException {
        String path = gqf.baseDirectory + File.separator + MySqlConn.getCompanyDB(gqf) + File.separator + gqf.fileName;
        if (!path.endsWith(".jpg")) {
            return null;
        }

        File imageFile = new File(path);
        java.awt.Image img;
        try {
            img = ImageIO.read(imageFile);
        } catch (Exception ex) {
            return null;
        }
        return new ImageIcon(img, imageFile.getName());
    }

    /**
     * Function to save an image to the server.  Again, for employee pictures... and again, the GQF should have
     * only a string containing the employee ID.
     */
    public void saveImage(ImageQuery gqf, ImageIcon image) throws RemoteException {
        String path = gqf.baseDirectory + File.separator + MySqlConn.getCompanyDB(gqf) + File.separator + gqf.fileName;
        if (!path.endsWith(".jpg")) {
            return;
        }

        File imageFile = new File(path);
        imageFile.mkdirs();
        System.out.println(path);
        java.awt.Image img = image.getImage();
        BufferedImage outImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        outImage.createGraphics().drawImage(img, 0, 0, null);
        try {
            ImageIO.write(outImage, "jpeg", imageFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteImage(ImageQuery gqf) throws RemoteException {
        String path = gqf.baseDirectory + File.separator + MySqlConn.getCompanyDB(gqf) + File.separator + gqf.fileName;
        if (!path.endsWith(".jpg")) {
            return;
        }

        File imageFile = new File(path);
        System.out.println(path);
        imageFile.delete();
    }

    /**
     * Empty Method used to see if clients connection to server is active or not
     * used when client says they wish to reconnect to server...
     */
    public void validateConnection() throws SQLException, RemoteException {
        //Blank body just used to see if exception thrown on client side indicating error
    }

    /**
     * Stub to allow the client to tell the server to reload it's list of client databases
     * used when a new client has been added in.
     */
    public void reloadClientDatabases() throws RemoteException, SQLException, Security_Exception {
        myGUI.printMessage("Reloading databases");
        MySqlConn.getCompanyInfo();
        MySqlConn.getBranchInfo();
    }

    public String amIConnected() throws RemoteException {
        return "HELL YEAH CONNECTED TO RMI!";
    }

    /**
     * Used by the client after both an RMI connection have been reestablished. 
     * Used to see if this user is currently marked as being on. If not add information to
     * Vector..., otherwise do nothing...
     */
    public void reastablishEntryForClientOnServer(SocketClients clientInfo) throws RemoteException {
//        boolean isFound = false;
//        for (int i = 0; i < SocketScheduleServer.ConnectedClients.size(); i++) {
//            if (SocketScheduleServer.ConnectedClients.get(i).userDatabaseId.equals(clientInfo.userDatabaseId)) {
//                isFound = true;
//            }
//        }
//        if (!isFound) {
//            SocketScheduleServer.ConnectedClients.add(clientInfo);
//        }
    }

    /**
     * Rather obvious to be used in conjunction with our getServerTimeZone and the client
     * Time Zone to get an accurate time for our clients...
     */
    public long getServerCurrentTimeMillis() {
        //TODO: This should go off of DB, used to be fine when we were running on db server one RMI Server, no mas...
        return System.currentTimeMillis();
    }

    /**
     * Used in conjunction with getServerCurrentTimeMillis and the client Time Zone
     * to get an accurate time for each client...
     */
    public TimeZone getServerTimeZone() throws RemoteException {
        return TimeZone.getDefault();
    }

    private class myUpdateQueryThread extends Thread {

        public myUpdateQueryThread() {
            this.setPriority(Thread.MAX_PRIORITY);
        }

        public void run() {
            while (true) {
                try {
                    GeneralQueryFormat gqf = ((GeneralQueryFormat) myQueryUpdateQueue.take());
                    String time = MySqlConn.saveTime(gqf);
                    MySqlConn.executeUpdate(gqf);
                    if (gqf.getUpdateStatus() != 0) {
                        mySocketServer.sendCommand(gqf, time);
                    }
                } catch (Exception e) {
                    ErrorReporting.sendErrorReport(e, "RMIServer");
                    myGUI.printError("In Execute Update: " + e.toString());
                    try {
                        sleep(100);
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }
}
