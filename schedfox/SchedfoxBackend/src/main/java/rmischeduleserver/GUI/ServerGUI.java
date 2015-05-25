/*
 * ServerGUI.java
 *
 * Created on January 21, 2005, 2:17 PM
 */

package rmischeduleserver.GUI;
import java.util.*;
import rmischeduleserver.data_connection_types.ClientVector;
/**
 *
 * @author ira
 */
public interface ServerGUI {
    
    public void printMessage(String message);
    
    public void printError(String error);
    
    public void printSocketMessage(String message);
    
    public void printClientList(ClientVector list);
    
    public void setVisible(boolean val) ;
    
    public void printQueryStatus(String query);
    
    public void setQueryStartTime();
    
    public void sendMaintToClient();
    
}
