/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.ireports.classes;

import java.io.InputStream;
import java.sql.PreparedStatement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.data_connection.Connection;
import rmischedule.ireports.RunReportInterface;
import rmischedule.main.Main_Window;
import rmischedule.xprint.templates.genericreportcomponents.formatterClass;
import rmischedule.xprint.templates.genericreportwithsummarycomponents.genericReportCellTextFormatter;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.reports.over_under_report_query;
import schedfoxlib.model.Company;

/**
 * Yeah as odd as it is run this report from Monday to Monday, to get a Monday
 * to Sunday report, underlying query is a little off
 *
 * @author user
 */
public class PrintOverTime extends RunReportInterface {

    private double myTotalHours;
    private double myOverTimeHours;
    private JasperPrint myPrint;

    public PrintOverTime() {
    }

    public void runReport(String company, String branch, Map params) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        Calendar startCal = getCalendarFromParams(params, "start_week");
        Calendar endCal = getCalendarFromParams(params, "end_week");

        DateFormat databaseFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startWeek = databaseFormat.format(startCal.getTime());
        String endWeek = databaseFormat.format(endCal.getTime());

        over_under_report_query myQuery = new over_under_report_query();

        ArrayList rs = new ArrayList();
        Connection myConnection = new Connection();
        myConnection.setBranch(branch);
        RunQueriesEx myQueryEx = new RunQueriesEx();
        myQuery.update(startWeek, endWeek);
        myConnection.prepQuery(myQuery);
        myQueryEx.add(myQuery);
        myConnection.setCompany(company);

        try {
            rs = myConnection.executeQueryEx(myQueryEx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        endCal.add(Calendar.DAY_OF_YEAR, -1);


        String co = "";
        String br = "";
        try {
            co = Main_Window.parentOfApplication.getCompanyNameById(company);
        } catch (Exception e) {
        }
        try {
            br = Main_Window.parentOfApplication.getBranchNameById(branch);
        } catch (Exception e) {
        }
        Hashtable<String, formatterClass> formatter = new Hashtable();
        formatter.put("Total Hours", new printMinutesAsHours());
        formatter.put("Over Time", new printMinutesAsHours());
        try {
            InputStream reportStream =
                    getClass().getResourceAsStream("/rmischedule/ireports/OvertimeReport.jasper");

            java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
            try {
                Company myCompany = Main_Window.getCompanyById(company);
                PreparedStatement pstmt = myConnect.prepareStatement("SET search_path='" + myCompany.getDB().replaceAll("'", "''") + "'");
                pstmt.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            Hashtable parameters = new Hashtable();
            parameters.put("title", "OverTime Report for " + co + ", " + br + " " + "\r\n(" + df.format(startCal.getTime()) + " - " + df.format(endCal.getTime()) + ")");
            parameters.put("branch_id", branch);
            parameters.put("start_week", dbFormat.format(startCal.getTime()));
            parameters.put("end_week", dbFormat.format(endCal.getTime()));
            
            myPrint = JasperFillManager.fillReport(reportStream, parameters, myConnect);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runReport(String company, Map params) {
    }

    public JasperPrint getJasperPrint() {
        return myPrint;
    }

    private class myOverTimePercentage extends genericReportCellTextFormatter {

        public myOverTimePercentage(String data) {
            super(data);
        }

        public String processData(Record_Set rs) {
            return StaticDateTimeFunctions.getPercentage(myTotalHours, myOverTimeHours, 2);
        }
    }

    private class myTotalHours extends genericReportCellTextFormatter {

        private double totalHours;
        private boolean isOverT;

        public myTotalHours(String data, boolean isOverTime) {
            super(data);
            isOverT = isOverTime;
        }

        public String processData(Record_Set rs) {
            double myHours = 0;
            rs.moveToFront();
            for (int i = 0; i < rs.length(); i++) {
                myHours = 0;
                try {
                    myHours = ((Double.parseDouble(super.processData(rs)) / 60.0));
                } catch (Exception e) {
                }
                if (isOverT && myHours > 40.0) {
                    totalHours += (myHours - 40.0);
                } else if (!isOverT) {
                    totalHours += myHours;
                }
                rs.moveNext();
            }
            rs.moveToFront();
            if (isOverT) {
                myOverTimeHours = totalHours;
            } else {
                myTotalHours = totalHours;
            }
            return StaticDateTimeFunctions.trimAfterDecimalPlaces(totalHours + "", 2);
        }
    }

    private class myNewTimeFormatter extends genericReportCellTextFormatter {

        public myNewTimeFormatter(String data) {
            super(data);
        }

        public String processData(Record_Set rs) {
            try {
                return StaticDateTimeFunctions.trimAfterDecimalPlaces(((Integer.parseInt(super.processData(rs)) / 60.0) + ""), 2);
            } catch (Exception e) {
                return "0";
            }
        }
    }

    private class printMinutesAsHours extends formatterClass {

        public String formatInputString(String input) {
            String integer = "0";
            try {
                integer = StaticDateTimeFunctions.trimAfterDecimalPlaces((Integer.parseInt(input) / 60.0 + ""), 2);
            } catch (Exception e) {
            }
            return integer;
        }
    }

    private class myOverTimeCondition extends genericReportCellTextFormatter {

        String myData;

        public myOverTimeCondition(String data) {
            super(data);
            myData = data;
        }

        public boolean displayThisRow(Record_Set rs) {
            int myTime = Integer.parseInt(rs.getString("total_time"));
            if ((myTime / 60.0) > 40.0) {
                return true;
            }
            return false;
        }

        public String processData(Record_Set rs) {
            try {
                return StaticDateTimeFunctions.trimAfterDecimalPlaces(((Integer.parseInt(super.processData(rs)) / 60.0) + ""), 2);
            } catch (Exception e) {
                return "0";
            }
        }
    }

    @Override
    public String getName() {
        return "overtime";
    }

    @Override
    public String getDisplayName() {
        return "Overtime Report";
    }

    @Override
    public String getDescription() {
        return "This report shows current overtime usage.";
    }

    @Override
    public Map<String, Class> getRequiredParameters() {
        HashMap<String, Class> parameters = new HashMap<String, Class>();
        parameters.put("start_week", Date.class);
        parameters.put("end_week", Date.class);
        return parameters;
    }
}
