/*
 * schedule_move_query.java
 *
 * Created on November 11, 2005, 12:41 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class schedule_move_query extends GeneralQueryFormat {
    
    private String sid;
    private String neid;
    private String ncid;
    private String ngroup;
    private String ntype;
    
    /** Creates a new instance of schedule_move_query */
    public schedule_move_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String shiftId, String newEmp, String newCli, String newGroup, String newType) {
        sid = shiftId;
        neid = newEmp;
        ncid = newCli;
        ngroup = newGroup;
        ntype = newType;
    }
    
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT move_temp_schedule(" + this.sid + ", ");
        sql.append(this.ncid +"," + this.neid + ", " + this.ngroup + "," + this.ntype + ", " + this.getUser() + ")");
        return sql.toString();
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }
}
