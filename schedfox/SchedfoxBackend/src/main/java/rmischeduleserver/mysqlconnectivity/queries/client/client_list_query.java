/*
 * client_list.java
 *
 * Created on January 21, 2005, 1:04 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class client_list_query extends GeneralQueryFormat {
    
    private String MY_QUERY = (
            " SELECT " +
            " (to_char(interval '13 mon', 'MM')::integer + (to_char(interval '13 mon', 'YYYY')::integer * 12)) as num_months, " +
            " client.* " +
            " FROM client " +
            " LEFT JOIN usked_client ON usked_client.client_id = client.client_id " +
            " WHERE "
            );
    
    private String deleted;
    private String search;
    private String worksite;
    private boolean order_by_id;
    
    /** Creates a new instance of client_list */
    public client_list_query() {
        myReturnString = new String();
    }
    
    public void update(String Deleted, String Search, String Worksite) {
        deleted = Deleted;
        search = Search;
        worksite = Worksite;
        order_by_id = false;
    }

    public void update(String Deleted, String Search, String Worksite, Boolean sbid) {
        deleted = Deleted;
        search = Search;
        worksite = Worksite;
        order_by_id = sbid;
    }
    
    public String generateSearchSQL(String search) {
        if (search.length() > 0) {
            return " And (" +
                   "  inStr(lCase(client.client_name), '" + search + "') or " +
                   "  inStr(lCase(client.client_city), '" + search + "') " +
                   ") ";
        }
        return "";
    }
    
    public String generateDeletedSQL(String deleted, boolean returnAnd) {
        if (deleted.compareTo("1") == 0) {
            if (returnAnd) {
                return " AND client.client_is_deleted >= 0";
            } else {
                return " client.client_is_deleted >= 0";
            }
        } else {
            if (returnAnd) {
                return " AND client.client_is_deleted = 0 ";
            } else {
                return " client.client_is_deleted = 0";
            }
        }
    }
    
    public String generateWorksiteSQL(String worksite) {
        if (worksite.compareTo("1") == 0) {
            return " and (client.client_worksite > 0 OR client.client_worksite = 0) ";
        } else if (worksite.equals("both")){
            return "";
        } else {
            return " and client.client_worksite = 0 ";
        }
    }
    
    public boolean hasAccess(){
        return true;
    }
    
    public String orderBy(){
        if(order_by_id){
            return " ORDER BY client.client_id ";
        }else{
            return " ORDER BY client.client_worksite, client.client_name";
        }
    }
    
    public String toString() {
        if (worksite.equals("1")) {
            return MY_QUERY + " branch_id = " + getBranch() + " AND " + generateDeletedSQL(deleted, false) + generateSearchSQL(search) +
               generateWorksiteSQL(worksite) + orderBy();
        }        
        return MY_QUERY + " branch_id = " + getBranch() + generateDeletedSQL(deleted, true) + generateSearchSQL(search) +
               generateWorksiteSQL(worksite) + orderBy();
    }
    
}
