/*
 * master_availability_delete_query.java
 *
 * Created on January 26, 2005, 4:41 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class master_availability_delete_query extends GeneralQueryFormat {
    
    private String key;
    private String dayToEnd;
    
    /** Creates a new instance of master_availability_delete_query */
    public master_availability_delete_query() {
        myReturnString = new String();
    }
    
    public String toString() {
        return "UPDATE availability_master SET avail_m_date_ended = date('" + dayToEnd + "') - integer '1' ,  avail_m_date_started = (CASE WHEN avail_m_date_started <" +
                " date('" + dayToEnd + "') THEN avail_m_date_started ELSE date('" + dayToEnd + "') END) WHERE " +
                "avail_m_id = " + key;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    /**
     * This query updates Availability silly
     */
    public int getUpdateStatus() {
        return UPDATE_AVAILABILITY;
    }
    
    public void update(String KeyId, String dayToEndOn) {
        key = KeyId;
        try {
            if (key.charAt(0) == '-') {
                key = key.substring(1);
            }
        } catch (Exception e) {}
        dayToEnd = dayToEndOn;
    }
    
}
