/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.SocketConnection;

import rmischeduleserver.data_connection_types.SocketCommandStructure;

/**
 *
 * @author user
 */
public interface UpdateClientClass {
    public void processServerCommand(SocketCommandStructure myCommand);
}
