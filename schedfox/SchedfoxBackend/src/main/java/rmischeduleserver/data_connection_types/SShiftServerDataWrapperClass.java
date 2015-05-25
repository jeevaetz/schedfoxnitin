/*
 * SShiftServerDataWrapperClass.java
 *
 * Created on May 25, 2005, 10:30 AM
 */

package rmischeduleserver.data_connection_types;
import java.io.Serializable;
/**
 *
 * @author ira
 */
public class SShiftServerDataWrapperClass implements Serializable {
    
    public int stime;
    public int etime;
    public String doy;
    
    /** Creates a new instance of SShiftServerDataWrapperClass */
    public SShiftServerDataWrapperClass(int Stime, int Etime, String DOY) {
        stime = Stime;
        etime = Etime;
        doy = DOY;
    }
    
}
