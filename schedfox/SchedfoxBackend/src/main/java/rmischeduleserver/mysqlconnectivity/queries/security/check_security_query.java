/*
 * check_security_query.java
 *
 * Created on February 3, 2005, 1:00 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.security;

import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author jason.allen
 */
public class check_security_query extends GeneralQueryFormat {
    
    /** Creates a new instance of check_security_query */
    public check_security_query() {
        myReturnString = "";
    }
    
    public void update(int sec, String md5){
        myReturnString = 
           " Select " +
           "   user_id, " +
           "   user_md5 " +
           " From  " +
           "   `user` " +
           " Where " +
           "   user_md5 = '" + md5 + "' And " +
           "   ((user_id in ( " +
           "       Select user_id  " +
           "       From   user_access " +
           "       Where  access_id = " + sec +
           "     )) or " +
           "     (user_id in ( " +
           "        Select user_group.user_id as user_id " +
           "        From   user_group " +
           "        Left Join group_access " +
           "          On group_access.group_id = user_group.group_id " +
           "        Where  access_id = " + sec +
           "     ) " +
           "    ) " +
           "   ) " 
        ;        
    }
    
    public boolean hasAccess() {
        return true;
    }    
}
