/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.ireports.classes;

import java.io.InputStream;
import java.text.DateFormat;
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
import rmischedule.xprint.data.xGenericReportData;
import rmischedule.xprint.templates.genericreportcomponents.formatterClass;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.reports.over_under_report_query;

/**
 *
 * @author user
 */
public class PrintOverUnder extends RunReportInterface {

    private JasperPrint print;

    @Override
    public void runReport(String company, String branch, Map params) {
        Calendar startCalendar = this.getCalendarFromParams(params, "start_week");
        Calendar endCalendar = this.getCalendarFromParams(params, "end_week");
        
        String startWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(startCalendar);
        String endWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(endCalendar);
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
        }
        String[] columns = {"ename", "total_time", "days_employed", "phone"};
        String[] headers = {"Employee Name", "Total Hours", "Days Worked", "Phone Number"};
        Hashtable<String, formatterClass> formatter = new Hashtable();
        formatter.put("Total Hours", new myTimeFormatter());

        try {
            InputStream reportStream =
                    getClass().getResourceAsStream("/rmischedule/ireports/GenericReport.jasper");

            Hashtable parameters = new Hashtable();
            String co = Main_Window.parentOfApplication.getCompanyById(company).getName();
            String br = "";
            try {
                Main_Window.parentOfApplication.getBranchNameById(branch);
            } catch (Exception e) {}
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            String title = "Over/Under Report for " + co + ", " + br;
            parameters.put("DateRange", "(" + df.format(startCalendar.getTime()) + " - " + df.format(endCalendar.getTime()) + ")");
            xGenericReportData reportData = new xGenericReportData((Record_Set) rs.get(0), headers, columns, new String[]{title}, formatter);

            print = JasperFillManager.fillReport(reportStream, parameters, reportData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runReport(String company, Map params) {
    }

    @Override
    public JasperPrint getJasperPrint() {
        return print;
    }

    @Override
    public String getName() {
        return "overunder_rep";
    }

    @Override
    public String getDisplayName() {
        return "Over Under Report";
    }

    @Override
    public String getDescription() {
        return "This report displays over / under information by branch.";
    }

    @Override
    public Map<String, Class> getRequiredParameters() {
        HashMap<String, Class> parameters = new HashMap<String, Class>();
        parameters.put("start_week", Date.class);
        parameters.put("end_week", Date.class);
        return parameters;
    }
}
