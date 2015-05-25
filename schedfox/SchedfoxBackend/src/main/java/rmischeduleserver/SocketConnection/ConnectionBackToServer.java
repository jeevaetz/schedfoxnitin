/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.SocketConnection;

import rmischeduleserver.GUI.ServerGUI;
import rmischeduleserver.data_connection_types.ClientConnection;
import rmischeduleserver.data_connection_types.ClientVector;
import rmischeduleserver.data_connection_types.SocketCommandStructure;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.sqldriver;

/**
 *
 * @author user
 */
public abstract class ConnectionBackToServer {

    private static ClientVector connectedClients;
    private static ServerGUI myGUI;
    private sqldriver myDriver;
    
    public ConnectionBackToServer(ServerGUI myGui, sqldriver myD) {
        connectedClients = new ClientVector();
        myDriver = myD;
        myGUI = myGui;
        myGUI.printMessage("Initializing Socket Server");
    }

    public abstract void sendCommand(GeneralQueryFormat gqf, String time);
    public abstract void addHearbeatItem(GeneralQueryFormat gqf);
    public abstract void sendMessage(String message, String userId);
    public abstract void sendCommandToClients(SocketCommandStructure myOriginalCommand);
    public abstract void forceUpdate(GeneralQueryFormat query, SocketCommandStructure myCommand, ClientConnection currClient);

    /**
     * Retrieves the SQL Driver
     * @return
     */
    public sqldriver getSQLDriver() {
        return myDriver;
    }

    /**
     * @return the myGUI
     */
    public ServerGUI getMyGUI() {
        return ConnectionBackToServer.myGUI;
    }

    /**
     * @param aMyGUI the myGUI to set
     */
    public void setMyGUI(ServerGUI aMyGUI) {
        ConnectionBackToServer.myGUI = aMyGUI;
    }

    /**
     * @return the ConnectedClients
     */
    public ClientVector getConnectedClients() {
        return connectedClients;
    }

    /**
     * @param aConnectedClients the ConnectedClients to set
     */
    public void setConnectedClients(ClientVector aConnectedClients) {
        ConnectionBackToServer.connectedClients = aConnectedClients;
    }

}
