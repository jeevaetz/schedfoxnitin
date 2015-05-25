/*
 * xNewPrintDay.java
 *
 * Created on December 19, 2005, 9:31 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.xprint.xdata;
import rmischeduleserver.util.StaticDateTimeFunctions;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.data_connection_types.*;
import rmischedule.components.*;
import rmischedule.main.Main_Window;
/**
 *
 * @author Ira Juneau
 */
public class xNewPrintDay {
    
    public String clientName;
    public String empName;
    public String stime;
    public String etime;
    public String dispdate;
    public String databasedate;
    public String shiftId;
    public String diffTime;

    private int companyId;

    /**
     * Creates a new instance of xNewPrintDay 
     */
    public xNewPrintDay(Record_Set rs, int companyId) {
        this.companyId = companyId;
        setFields(rs.getString("cname"), rs.getString("ename"), rs.getString("start_time"), rs.getString("end_time"),
                rs.getString("date"), rs.getString("sid"));
    }
    
    public xNewPrintDay(String cname, String ename, String s, String e, String date, String id, int companyId) {
        this.companyId = companyId;
        setFields(cname, ename, s, e, date, id);
    }
    
    private void setFields(String cname, String ename, String s, String e, String date, String id) {
        clientName = cname;
        empName = ename;
        stime = StaticDateTimeFunctions.stringToDefinedFormattedTime(s, Main_Window.parentOfApplication.is12HourFormat());
        etime = StaticDateTimeFunctions.stringToDefinedFormattedTime(e, Main_Window.parentOfApplication.is12HourFormat());
        try {
            diffTime = new String(StaticDateTimeFunctions.getDifferenceAndRoundByNumberOfMinutes(Double.parseDouble(s), Double.parseDouble(e), companyId) + "");
        } catch (Exception exe) {
            diffTime = "0";
        }
        databasedate = date;
        shiftId = id;
    }
    
}
