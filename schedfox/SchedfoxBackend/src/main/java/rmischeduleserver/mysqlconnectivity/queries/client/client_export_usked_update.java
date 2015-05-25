/*
 * client_export_usked_update.java
 *
 * Created on February 14, 2005, 9:12 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class client_export_usked_update extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            "DELETE FROM " +
            "usked_client " +
            "WHERE " +
            "client_id = ");
    
    private static final String MY_QUERY2 = (
            ";INSERT INTO " +
            "usked_client " +
            "(client_branch, " +
            " usked_cli_id, " +
            " client_id) " +
            " VALUES " +
            " ("
            );

    /** Creates a new instance of employee_export_usked_update */
    public client_export_usked_update() {
        myReturnString = new String();
    }
    
    public void update(String usked_id, String cid) {
        myReturnString = "DELETE FROM usked_client WHERE client_id = " + cid + ";" +
                "INSERT INTO usked_client (client_branch, usked_cli_id, client_id) VALUES (" +
                "" + getBranch() + ",'" + usked_id + "', " + cid + ");";
    }
    
    public void update(String usked_id, String client, String myParentId) {
        //If no parentId then swap so that it treats this like its own client
        if (myParentId.length() == 0) {
            String parent = myParentId;
            myParentId = usked_id;
            usked_id = parent;
        }
        myReturnString = "DELETE FROM usked_client WHERE client_id = " + client + ";" +
                "INSERT INTO usked_client (client_branch, usked_cli_id, client_id, usked_ws_id) VALUES (" +
                "" + getBranch() + ", '" + myParentId + "', " + client + ", '" + usked_id + "');";
    }
    
    public boolean hasAccess() {
        return true;
    }   
}
