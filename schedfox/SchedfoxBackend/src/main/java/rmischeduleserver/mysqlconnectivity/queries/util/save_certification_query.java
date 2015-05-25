/*
 * save_certification_query.java
 *
 * Created on May 26, 2005, 7:48 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class save_certification_query extends GeneralQueryFormat {
    
    private String Name;
    private String Desc;
    private String Renewal_Time;
    private String Id;
    
    /** Creates a new instance of save_certification_query */
    public save_certification_query() {
        myReturnString = new String();
    }
    
    public void update(String id, String name, String desc, String renewal) {
        Name = name.replaceAll("'", "''");
        Desc = desc.replaceAll("'", "''");
        Renewal_Time = renewal;
        Id = id;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        StringBuilder myBuilder = new StringBuilder();
        if (Id.length() < 20) {
            myBuilder.append("DELETE FROM certifications WHERE certification_id = " + Id + ";");
        }
        myBuilder.append("INSERT INTO certifications (certification_id, certification_name, certification_description," +
                "certification_default_renewal_time) VALUES (" + Id + ",'" + Name + "', '" + Desc + "', '" + Renewal_Time + "');");
        return myBuilder.toString();
    }
    
}
