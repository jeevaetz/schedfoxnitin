/*
 * ServerCommandLineGUI.java
 *
 * Created on January 21, 2005, 9:09 AM
 */

package rmischeduleserver.GUI;
import java.util.*;
import rmischeduleserver.data_connection_types.ClientVector;
import rmischeduleserver.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.data_connection_types.*;
/**
 *
 * @author ira
 */
public class ServerCommandLineGUI implements ServerGUI {
    
    /** Creates a new instance of ServerCommandLineGUI */
    public ServerCommandLineGUI() {
    }
    
    public String getTime() {
        Calendar time = Calendar.getInstance();
        return "[ " + time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND) + " ]";
    }
    
    public void printMessage(String message) {
        System.out.println("Message " + getTime() + message);
    }
    
    public void printError(String error) {
        System.out.println("Error " + getTime() + error);
    }
    
    public void setVisible(boolean val) {
        
    }

    public void printSocketMessage(String message) {
        System.out.println("Message " + getTime() + message);
    }

    public void printClientList(rmischeduleserver.data_connection_types.ClientVector list) {
    }

    public void printQueryStatus(String query) {
    }

    public void setQueryStartTime() {
    }
   
    public void sendMaintToClient(){
        SocketCommandStructure command = new SocketCommandStructure(GeneralQueryFormat.KILL_CLIENT);
        command.message = "The SchedFox Server has been shut down for maintenence.  Please wait a few minutes before trying to log back in.";
        Main.myServer.getSocketConnection().sendCommandToClients(command);
    }    
    
}
