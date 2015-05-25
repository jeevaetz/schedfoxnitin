/*
 * client_query.java
 *
 * Created on January 20, 2005, 4:46 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.StringTokenizer;
/**
 *
 * @author ira
 */
public class client_query extends GeneralQueryFormat{
    
    private String type;
    private String worksite;
    private String lastUpdated;
    private String sdate;
    private String edate;
    private String clientDeletedString;
    private int employee_id;
    
    /** Creates a new instance of client_query */
    public client_query() {
        myReturnString = new String();
    }
    
    public void update(String Type, String Worksite, String LastUpdated) {
        type = Type;
        worksite = Worksite;
        lastUpdated = LastUpdated; 
    }

    public void update(int employee_id, String Type, String Worksite, String sdate, String edate, String LastUpdated) {
        type = Type;
        worksite = Worksite;
        lastUpdated = LastUpdated;
        this.employee_id = employee_id;
        this.sdate = sdate;
        this.edate = edate;
    }
    
    /**
     * Used by our hearbeat...
     */
    public void setLastUpdated(String LU) {
        lastUpdated = LU;
    }
    
    /**
     * Used by XPrintData, I needed both non worksite and worksite areas and did not want to 
     * change interface so this was just added particularly for that...
     */
    public void updateWorksite(String Worksite) {
        worksite = Worksite;
    }
    
    /**
     * Works to generate last updated stuff...however...LastUpdated field in format 
     * 2005-07-26 11:45:24.531-05 chop off anything after . just in case, was not returning correct data is
     * why...
     */
    private String generateLastUpdated(String lu) {
        if (lu.length() > 0) {
            return " And client.client_last_updated >= to_timestamp('" + lu + "', 'YYYY-MM-DD HH24:MI:SS') ";
        }
        return "";
    }
    
    private String generateTypeSQL(String type) {
        if (type.length() > 0) {
            return "and client.client_type = " + type;
        }
        return "";
    }
    
    private String generateWorksiteSQL(String worksite) {
        if (worksite.length() > 0) {
            if (worksite.equals("1")) {
                return " and client.client_worksite > 0 ";
            } else if (worksite.equals("both")) {
                return "";
            } else {
                return " and client.client_worksite = 0";
            }
        }
        return "";
    }
    
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT ON (client.client_id) ");
        sql.append("   client.client_id                as    id, ");
        sql.append("   client.client_name              as    client_name, ");
        sql.append("   client.client_is_deleted        as    delclient, ");
        sql.append("   client.client_address           as    address, ");
        sql.append("   client.client_city              as    city, " );
        sql.append("   client.client_state             as    state, ");
        sql.append("   client.client_zip               as    zip, ");
        sql.append("   client.client_phone             as    phone, ");
        sql.append("   client.client_type              as    type, ");
        sql.append("   client.client_name              as    name, ");
        sql.append("   client.client_worksite          as    ws, ");
        sql.append("   client.client_training_time     as    train_time, ");
        sql.append("   usked_client.usked_cli_id       as    usked_id, ");
        sql.append("   usked_client.usked_ws_id        as    usked_ws, ");
        sql.append("   client.client_worksite_order    as    wsorder, ");
        sql.append("   client.rate_code_id             as    rate_code_id, ");
        sql.append("   client.default_non_billable, ");
        sql.append("   (CASE WHEN client.client_worksite > 0 THEN cli.client_name || ' ' || client.client_name ELSE ");
        sql.append("   client.client_name END) as cname, ");
        sql.append("   (CASE WHEN EXISTS (SELECT * FROM client_notes WHERE client_notes.client_id = client.client_id) THEN 1 ELSE 0 END) as hasNotes ");
        sql.append(" From client ");
        if (employee_id != 0)  {
            sql.append("INNER JOIN (SELECT COUNT(*) as num, cid FROM assemble_schedule('" + sdate + "', '" + edate + "', " + this.getBranch() + ", " + employee_id + ") WHERE isdeleted = 0 GROUP BY cid) as sched ON sched.cid = client.client_id AND num > 0 ");
        }
        sql.append(" LEFT JOIN usked_client on usked_client.client_id = client.client_id ");
        sql.append(" LEFT JOIN client cli ON ");
        sql.append(" cli.client_id = client.client_worksite ");
        sql.append("WHERE ");
        sql.append("(client.client_is_deleted = 0 ");
        if (sdate != null) {
            sql.append("OR (client.client_is_deleted = 1 AND client.client_end_date BETWEEN '" + sdate + "' AND '" + edate + "')) ");
        } else {
            sql.append(") ");
        }

        if (getBranch() != null && !getBranch().equals("<branch>")) {
            sql.append("AND client.branch_id = " + getBranch() + " ");
        }
        sql.append(generateWorksiteSQL(worksite));
        sql.append(generateLastUpdated(lastUpdated));
        sql.append("ORDER BY client.client_id ");

        return sql.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
