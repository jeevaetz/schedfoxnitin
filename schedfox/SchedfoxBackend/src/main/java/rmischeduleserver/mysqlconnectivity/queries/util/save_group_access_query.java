/*
 * save_group_access_query.java
 *
 * Created on April 21, 2005, 7:56 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.util.ArrayList;
/**
 *
 * @author ira
 */
public class save_group_access_query extends GeneralQueryFormat {
    
    private ArrayList Group;
    private String gid;
    private String gname;
    private String mid;
    
    /** Creates a new instance of save_group_access_query */
    public save_group_access_query() {
        myReturnString = new String();
        Group = new ArrayList();
    }
    
    /**
     * Pass in 0 for id if you want it to generate a new id for you, beautiful..
     */
    public void update(ArrayList groupArray, String id, String name, String manageId) {
        Group = groupArray;
        gid = id;
        mid = manageId;
        gname = name.replaceAll("'", "''");
    }
    
    public String toString() {
        String newId;
        if(gid.equals("0")) newId = "(SELECT max(groups_id) FROM groups)";
        else newId = gid;
        
        StringBuilder groupString = new StringBuilder();
        for (int i = 0; i < Group.size(); i++) {
            groupString.append("INSERT INTO " + getManagementSchema() + ".groups_access (groups_id, access_id) " +
                               "VALUES (" + newId + ", " + (String)Group.get(i) +");");
        }
        
        if(gid.equals("0")) {
            return  "INSERT INTO " + getManagementSchema() + ".groups (groups_id, groups_name, groups_management_id) VALUES (" +
                    "(SELECT max(groups_id) + 1 FROM groups), '" + gname + "', " + mid + ");" + groupString;
        }
        else {
            return  "UPDATE " + getManagementSchema() + ".groups SET groups_name = '" + gname + "', groups_management_id = " + mid + " WHERE groups_id = " + gid + ";" +
                    "DELETE FROM " + getManagementSchema() + ".groups_access WHERE groups_id = " + gid + ";" + groupString;
        }
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
