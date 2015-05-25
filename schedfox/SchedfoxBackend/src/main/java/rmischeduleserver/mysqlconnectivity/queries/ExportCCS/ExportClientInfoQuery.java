/*
 * ExportClientInfoQuery.java
 *
 * Created on June 28, 2005, 9:18 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.ExportCCS;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class ExportClientInfoQuery extends GeneralQueryFormat {
    
    /** Creates a new instance of ExportClientInfoQuery */
    public ExportClientInfoQuery() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return  "SELECT (CASE WHEN usked_cli_id IS NOT NULL THEN " +
                "usked_cli_id ELSE text(client.client_id) END) as cid, " +
                "(CASE WHEN usked_ws_id > '' THEN usked_ws_id ELSE '' END) as worksite, " +
                "client.client_id as client_id FROM client " +
                "LEFT JOIN usked_client ON " +
                "usked_client.client_id = client.client_id " +
                "WHERE branch_id = " + getBranch();
    }
    
}
