/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.data_connection;
   
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import rmischedule.main.Main_Window;
import rmischeduleserver.SocketConnection.SocketBufferObjectClass;
import rmischeduleserver.data_connection_types.ClientConnection;
import rmischeduleserver.data_connection_types.SocketClients;
import rmischeduleserver.data_connection_types.SocketCommandStructure;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class LocalConnection extends ClientConnection {

    public LocalConnection() {
        super();
        
    }

    public void processServerCommand(SocketCommandStructure myCommand) {
        switch (myCommand.Command) {
            case GeneralQueryFormat.UPDATE_SCHEDULE:
                try {
                    Main_Window.myScheduleForm.updateDataViaServer((ArrayList)myCommand.getRecordSet(0));
                    Main_Window.ciw.updateCheckInWithScheduleData((ArrayList)myCommand.getRecordSet(0));
                } catch (Exception ex)  {
                    ex.printStackTrace();
                }
                break;
            case GeneralQueryFormat.UPDATE_CHECK_IN:
                try {
                    Main_Window.ciw.updateCheckInWithCheckInData((ArrayList)myCommand.getRecordSet(0));
                } catch(Exception ex) { }
                break;
            case GeneralQueryFormat.UPDATE_AVAILABILITY:
                try { Main_Window.myScheduleForm.updateAvailabilityViaServer((ArrayList)myCommand.getRecordSet(0)); }
                catch(Exception ex) { }
                break;
            case GeneralQueryFormat.UPDATE_BANNED:
                try { Main_Window.myScheduleForm.updateBanned((ArrayList)myCommand.getRecordSet(0)); }
                catch(Exception ex) { }
                break;
            case GeneralQueryFormat.UPDATE_CLIENT_CERT:
                try { Main_Window.myScheduleForm.updateClientCerts((ArrayList)myCommand.getRecordSet(0)); }
                catch(Exception ex) { }
                break;
            case GeneralQueryFormat.UPDATE_EMPLOYEE_CERT:
                try { Main_Window.myScheduleForm.updateEmpCerts((ArrayList)myCommand.getRecordSet(0)); }
                catch(Exception ex) { }
                break;
            case 7:
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, myCommand.message, "Message Recieved", JOptionPane.INFORMATION_MESSAGE);
                break;
            case GeneralQueryFormat.KILL_CLIENT:
                Main_Window.terminateSchedFox(myCommand.message, "Shutting Down");
            default:
                break;
        }
    }

    @Override
    public void updateThisClientInfoWithChannelInformation(SocketBufferObjectClass mySocketBufferClass) {
       
    }

    @Override
    public SocketBufferObjectClass getThisClientInfoChannelInformation() {
        return null;
    }

    @Override
    public void updateClientWithDataFromSocket(ObjectOutputStream os, ObjectInputStream is) {
        
    }

    @Override
    public void sendPingPacketToThisClient(ByteBuffer myBuf) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean sendDataToThisClient(SocketCommandStructure myCommand) throws IOException {
        this.processServerCommand(myCommand);
        return true;
    }

}
