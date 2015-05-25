/*
 * schedule_master_move_query.java
 *
 * Created on July 21, 2005, 8:50 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.util.StringTokenizer;
/**
 *
 * @author ira
 */
public class schedule_master_move_query extends GeneralQueryFormat {
    
    private String shiftId;
    private String dateToMoveShiftOn;
    private String newClientId;
    private String newEmployeeId;
    private String newGroupId;
    
    /** Creates a new instance of schedule_master_move_query */
    public schedule_master_move_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    /**
     * shiftId needs to be in the format -shiftid/date for this to work properly
     */
    public void update(String myShiftId, String oldEmployeeId, String newEmployee, String oldClient, String newClient, String oldGroupId, String newGroup) {
        StringTokenizer myToken = new StringTokenizer(myShiftId, "/");
        shiftId = (Integer.parseInt(myToken.nextToken()) * -1) + "";
        dateToMoveShiftOn = myToken.nextToken();
        newClientId = newClient;
        newEmployeeId = newEmployee;
        newGroupId = newGroup;
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT move_permanent_schedule(" + this.shiftId + ", '" + this.dateToMoveShiftOn + "',");
        sql.append(this.newClientId +"," + this.newEmployeeId + ", " + this.newGroupId + "," + this.getUser() + ")");
        return sql.toString();
    }
    
}
