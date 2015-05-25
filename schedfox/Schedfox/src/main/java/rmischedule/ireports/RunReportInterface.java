/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.ireports;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.main.Main_Window;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.xprint.templates.genericreportcomponents.formatterClass;

/**
 *
 * @author user
 */
public abstract class RunReportInterface {

    public abstract void runReport(String company, String branch, Map params);

    public abstract void runReport(String company, Map params);

    public abstract JasperPrint getJasperPrint();

    public abstract String getName();

    public abstract String getDisplayName();

    public abstract String getDescription();

    public abstract Map<String, Class> getRequiredParameters();

    /**
     * Breaks down a value in either a Calendar or a String in either (MM/dd/yyyy or yyyy/dd/MM)
     * to a Calendar, useful for webapp vs swing.
     * @param map
     * @param param
     * @return
     */
    public Calendar getCalendarFromParams(Map map, String param) {
        Calendar cal = Calendar.getInstance();
        if (map.get(param) instanceof String[]) {
            try {
                String sWeek = ((String[]) map.get(param))[0];
                cal.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(sWeek));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (map.get(param) instanceof String) {
            try {
                cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse((String) map.get(param)));
            } catch (Exception e) {
            }
        } else if (map.get(param) instanceof Calendar) {
            cal.setTime(((Calendar) map.get(param)).getTime());
        }
        return cal;
    }
    
    public class mySocialFormatter extends formatterClass {

        public String formatInputString(String input) {
            try {
                if (Main_Window.parentOfApplication.getUser().getCanViewSsn()) {
                    return input.substring(0, 3) + "-" + input.substring(3, 5) + "-" + input.substring(5, 9);
                } else {
                    return "XXX-XX-" + input.substring(input.length() - 4);
                }
            } catch (Exception e) {
                return input;
            }
        }
    }

    public class myAgeFormatter extends formatterClass {
        public String formatInputString(String input) {
            try {
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar currCalendar = Calendar.getInstance();
                Calendar compCalendar = Calendar.getInstance();
                compCalendar.setTime(myFormat.parse(input));
                int yearDiff = currCalendar.get(Calendar.YEAR) - compCalendar.get(Calendar.YEAR);
                if (currCalendar.get(Calendar.MONTH) < compCalendar.get(Calendar.MONTH)) {
                    yearDiff--;
                }
                return yearDiff + "";
            } catch (Exception e) {
                return input;
            }
        }
    }
    
    public class GenderFormatter extends formatterClass {
        public String formatInputString(String input) {
            if (input.equals("1")) {
                return "Male";
            } else if (input.equals("2")) {
                return "Female";
            } else {
                return "Not Set";
            }
        }
    }
    
    public class myEndDateFormatter extends formatterClass {

        public String formatInputString(String input) {
            if (input.equals("1000-10-10") || input.compareTo("2100-00-00") > 0) {
                return "";
            }
            return input;
        }
    }
    
    public class myTimeFormatter2 extends formatterClass {

        public String formatInputString(String input) {
            return StaticDateTimeFunctions.stringToFormattedTime(input, Main_Window.parentOfApplication.is12HourFormat());
        }
    }

    public class myTimeFormatter extends formatterClass {

        public String formatInputString(String input) {
            return (Integer.parseInt(input) / 60) + " hrs";
        }
    }
}
