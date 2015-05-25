/*
 * get_expired_certifications.java
 *
 * Created on March 3, 2006, 10:44 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class get_expired_certifications extends GeneralQueryFormat {
    
    private int howManyDaysToCheck;
    private int howManyDaysToWarn;
    
    /** Creates a new instance of get_expired_certifications */
    public get_expired_certifications() {
        myReturnString = new String();
    }
    
    public void update(int howManyDaysToCheck, int howManyDaysToWarn) {
        this.howManyDaysToCheck = howManyDaysToCheck;
        this.howManyDaysToWarn = howManyDaysToWarn;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {

        return"SELECT * FROM f_get_expired_certifications();";
    }
    
}
