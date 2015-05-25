/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.ireports.classes;


import rmischedule.data_connection.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.ireports.RunReportInterface;
import rmischedule.main.Main_Window;
import rmischedule.xprint.xdata.xNewPrintData;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischeduleserver.mysqlconnectivity.queries.reports.assemble_schedule_for_employee_report_query;

/**
 *
 * @author user
 */
public class PrintExtendedEmployeeReport extends RunReportInterface {

    private JasperPrint myPrint;

    public void runReport(String company, String branch, Map params) {
        Connection myConnection = new Connection();
        myConnection.setBranch(branch);
        myConnection.setCompany(company);

        Calendar startCalendar = getCalendarFromParams(params, "start_week");
        Calendar endCalendar = getCalendarFromParams(params, "end_week");

        String startWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(startCalendar);
        String endWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(endCalendar);

        assemble_schedule_for_employee_report_query myQuery = new assemble_schedule_for_employee_report_query();
        myQuery.update("", "", startWeek, endWeek, "", "", false);
        xNewPrintData myData = new xNewPrintData(myQuery, myConnection, startCalendar, endCalendar);

        sjq.print.PrintDocument doc = new sjq.print.PrintDocument("Extended Employee Schedules");
        while (myData.hasMoreCals()) {
            doc.addComponent(myData.getNextCalendar());
            if (!myData.nextCalHasSameEmployee()) {
                doc.addPageBreak();
            }
        }

        sjq.print.PrintPreviewInternalFrame ppf = new sjq.print.PrintPreviewInternalFrame(doc);
        Main_Window.parentOfApplication.desktop.add(ppf);
        ppf.displayPrintPreview();
    }

    public void runReport(String company, Map params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public JasperPrint getJasperPrint() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        return "employee_ext";
    }

    @Override
    public String getDisplayName() {
        return "Empoyee Extended Report";
    }

    @Override
    public String getDescription() {
        return "This report provides extended information about currently active employees.";
    }

    @Override
    public Map<String, Class> getRequiredParameters() {
        HashMap<String, Class> parameters = new HashMap<String, Class>();
        parameters.put("start_week", Date.class);
        parameters.put("end_week", Date.class);
        return parameters;
    }
}
