/*
 * save_trained_or_not_query.java
 *
 * Created on June 1, 2005, 9:36 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.training;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.util.*;
/**
 *
 * @author ira
 */
public class save_trained_or_not_query extends GeneralQueryFormat {
    
    private ArrayList cid;
    private ArrayList eid;
    private ArrayList MarkTrained;
    
    /** Creates a new instance of save_trained_or_not_query */
    public save_trained_or_not_query() {
        myReturnString = new String();
        cid = new ArrayList();
        eid = new ArrayList();
        MarkTrained = new ArrayList();
    }
 
    public void update(String Cid, String Eid, boolean markTrained) {
        cid.add(Cid);
        eid.add(Eid);
        MarkTrained.add(new Boolean(markTrained));
    }
    
    public String toString() {
        StringBuffer myBuilder = new StringBuffer();
        for (int i = 0; i < cid.size(); i++) {
            myBuilder.append("DELETE FROM employee_trained WHERE " +
                    " employee_id = " + (String)eid.get(i) +
                    " AND client_id = " + (String)cid.get(i) +
                    " AND schedule_id = 0 " +
                    " AND employee_is_done_training_this_shift = true;");
            if (((Boolean)MarkTrained.get(i)).booleanValue()) {
                myBuilder.append("INSERT INTO employee_trained (" +
                        "employee_id, client_id, schedule_id, training_employee_id, " +
                        "employee_is_done_training_this_shift) VALUES (" + (String)eid.get(i) + ", " + (String)cid.get(i) + ", " +
                        "0, 0, true);");
            }
        }
        return myBuilder.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
