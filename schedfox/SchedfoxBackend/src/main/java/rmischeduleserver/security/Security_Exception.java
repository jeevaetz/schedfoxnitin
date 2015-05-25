/*
 * Security_Exception.java
 *
 * Created on February 3, 2005, 10:20 AM
 */

package rmischeduleserver.security;

/**
 *
 * @author jason.allen
 */
public class Security_Exception extends Exception implements java.io.Serializable{
    
    /** Creates a new instance of Security_Exception */
    public Security_Exception(String md5, String Access_Violation){
        super("Trying to access a Restricted Function " + Access_Violation);
    }    
}
