/*
 * client_list.java
 *
 * Created on January 21, 2005, 1:04 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.rate_codes;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class rate_code_update extends GeneralQueryFormat {

    private String id, name, usked;
    
    /** Creates a new instance of client_list */
    public rate_code_update() {
        myReturnString = new String();
    }
    
    /**
     *  Update the information so that we can add or update our rate codes
     */    
    public void update(String rate_id, String rate_name, String rate_usked) {
          id = (rate_id == null)?"":rate_id;
        name = rate_name;
       usked = rate_usked;
    }
    
    /**
     * Security check.
     */    
    public boolean hasAccess(){
        return true;
    }
    
    public String toString() {
        if(id.length() > 0){
            return 
                "UPDATE rate_code " +
                "SET "   +
                " rate_code_name  = '" +  name.replaceAll("'","''") + "', " +
                " usked_rate_code = '" + usked.replaceAll("'","''") + "' " +
                "WHERE " +
                " rate_code_id = " + id;                    
        }else{
            return 
                "INSERT INTO rate_code " +
                "(rate_code_name, usked_rate_code) " +
                "VALUES" +
                "('" + name.replaceAll("'","''") + "','" + 
                      usked.replaceAll("'","''") + "')";                    
        }
    }
    
}
