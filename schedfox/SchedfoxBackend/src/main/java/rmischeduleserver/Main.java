 /*
 * Main.java
 *
 * Created on January 7, 2005, 12:02 PM
 */

package rmischeduleserver;

import java.rmi.*;
import java.rmi.registry.*;

import java.util.*;

import java.net.InetAddress;
import javax.swing.JOptionPane;
import rmischeduleserver.GUI.*;
import rmischeduleserver.SocketConnection.*;
import rmischeduleserver.mysqlconnectivity.*;
import rmischeduleserver.mysqlconnectivity.queries.management.GetCompaniesQuery;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.schedule_master_finalize;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.net.*;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.data_connection_types.NullSecurityManager;
//import rmischeduleserver.SocketConnection.SocketScheduleServer;
/**
 *
 * @author ira
 */
public class Main {
    private static String serverName;
    public static RMIScheduleServerImpl myServer;
    public static String myServerName;
    private static Heart_Beat hb;
    private static MemoryMonitor memMonitor;

    /** Creates a new instance of Main */
    public Main() {

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            myServerName = IPLocationFile.getLOCATION_OF_RMI_SERVER();
        } catch (Exception e) {
            System.out.println("Could not find local host address assuming server " + IPLocationFile.getLOCATION_OF_RMI_SERVER());
            myServerName = IPLocationFile.getLOCATION_OF_RMI_SERVER();
        }
        serverName = "//" + myServerName + "/scheduleServer";
        System.setSecurityManager(new NullSecurityManager());
        boolean registryCreated = false;
        try {
            myServer = RMIScheduleServerImpl.getInstance();
        } catch (Exception e) {}
        for (int i = 1099; i < 2000; i++) {
            if (!registryCreated) {
                registryCreated = tryToCreateRegistry(i);
            }
        }

        myServer.myGUI.printMessage("Up and running yay!");
        myServer.myGUI.printMessage("Starting Heart Beat");
        hb = new Heart_Beat();
        hb.start();

        memMonitor = new MemoryMonitor();
        memMonitor.start();
    }

    public static boolean tryToCreateRegistry(int port) {
//        try {
//            LocateRegistry.createRegistry(port);
//            try { Naming.bind(serverName, myServer); }
//            catch(AlreadyBoundException ex) { JOptionPane.showMessageDialog((ServerGraphicalGUI)myServer.myGUI, "RMI already bound to this name!", "Error!", JOptionPane.ERROR_MESSAGE);  System.exit(1); }
//            return true;
//        } catch (Exception e) {
//            myServer.myGUI.printError(e.toString());
//            return false;
//        }
        return true;
    }

    public static void runMaint()
    {
        hb.runUpdatesOnDatabases();
    }

/**
 *  This is our heartbeat for the finalize master
 */
    static class Heart_Beat extends Thread{

        /*  our time in seconds */
        private final static int WAIT_TIME = 3000;
        private boolean ranLastTime;

        private Calendar myCalendar;
        private schedule_master_finalize    smf;
        private GetCompaniesQuery           gcq;

        public Heart_Beat(){
            gcq = new GetCompaniesQuery();
            smf = new schedule_master_finalize();
            smf.setMD5("autoupdate" + IPLocationFile.getLOCATION_OF_RMI_SERVER() + IPLocationFile.getLOCATION_OF_POSTGRES_SERVER());
            myCalendar = Calendar.getInstance();
            ranLastTime = false;
        }

        public void runUpdatesOnDatabases()
        {
            Record_Set rs = new Record_Set();
            try{
                rs = myServer.executeQuery(gcq, "");
            }catch(Exception e)
            {
                myServer.myGUI.printMessage("HB_Dying because of Get Companies Problem");
                myServer.myGUI.printError(e.toString());
                return;
            }
            //rs.forceGenerate();
            myServer.myGUI.printMessage("Running Hearbeat Server Side For All Databases...");
            rs.decompressData();
            if(rs.length() > 0){
                for(int c = 0;c < rs.length();c++){
                    try {
                        smf.setCompany(rs.getString("company_id"));
                        try{
                            myServer.executeUpdate(smf, "");
                        }catch(Exception e){
                            myServer.myGUI.printMessage("Error running finalize");
                            myServer.myGUI.printError(e.toString());
                        }
                    } catch (Exception e) {
                        myServer.myGUI.printMessage("Problem running heartbeat for client " + rs.getString("company_name") + " database: " + rs.getString("company_db"));
                    }
                    rs.moveNext();
                }
            }
            myServer.myGUI.printMessage("Completed Hearbeat Server Side..");
        }

        public void run(){
            while(true){
                try{
                    Thread.sleep(1000 * WAIT_TIME);
                }catch(Exception e){
                    myServer.myGUI.printMessage("HB_Dying because of Sleep problem. ");
                    myServer.myGUI.printError(e.toString());
                    break;
                }
                Calendar currCal = Calendar.getInstance();
                if (currCal.get(Calendar.DAY_OF_YEAR) != myCalendar.get(Calendar.DAY_OF_YEAR)) {

                    if (ranLastTime) {
                        myCalendar = Calendar.getInstance();
                    }
                    ranLastTime = true;
                    runUpdatesOnDatabases();
                }
            }
            myServer.myGUI.printMessage("Heart Beat died!");
        }
    }

    static class MemoryMonitor extends Thread {
        Runtime runTime;

        public MemoryMonitor() {
            runTime = Runtime.getRuntime();
        }

        public void run() {
            while(true) {
                System.out.println("RMI Total Memory: " + (runTime.totalMemory() / 1048576.0));
                System.out.println("RMI Free Memory:  " + (runTime.freeMemory() / 1048576.0));
                System.out.println("RMI Max Memory:   " + (runTime.maxMemory() / 1048576.0));

                myServer.myGUI.printMessage("RMI Total Memory: " + (runTime.totalMemory() / 1048576.0));
                myServer.myGUI.printMessage("RMI Free Memory:  " + (runTime.freeMemory() / 1048576.0));
                myServer.myGUI.printMessage("RMI Max Memory:   " + (runTime.maxMemory() / 1048576.0));

                try { sleep(300000); } catch(Exception ex) {System.out.println("Error in Main.java:"+ex); }
            }

            
        }
    }
}

