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
import rmischedule.data_connection.Connection;
import rmischedule.ireports.RunReportInterface;
import rmischedule.main.Main_Window;
import rmischedule.xprint.data.xGenericReportData;
import rmischedule.xprint.templates.genericreportcomponents.formatterClass;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.new_assemble_schedule_for_checkin_query;

/**
 *
 * @author user
 */
public class PrintEmployeeReport extends RunReportInterface {

    private JasperPrint report;

    @Override
    public void runReport(String company, String branch, Map params) {
        Calendar startWeek = this.getCalendarFromParams(params, "start_week");
        Calendar endWeek = this.getCalendarFromParams(params, "end_week");

        new_assemble_schedule_for_checkin_query myQuery = new new_assemble_schedule_for_checkin_query();
        myQuery.setPrintableQuery(true);
        myQuery.setPreparedStatement(new Object[]{startWeek.getTime(), endWeek.getTime(),
                    Integer.parseInt(branch), -1, null, null, 1440});
        ArrayList rs = new ArrayList();
        Connection myConnection = new Connection();
        myConnection.setBranch(branch);
        myConnection.setCompany(company);
        RunQueriesEx myQueryEx = new RunQueriesEx();
        myConnection.prepQuery(myQuery);
        myQueryEx.add(myQuery);
        try {
            rs = myConnection.executeQueryEx(myQueryEx);
        } catch (Exception e) {
        }
        String co = Main_Window.parentOfApplication.getCompanyById(company).getName();
        String br = "";
        try {
            br = Main_Window.parentOfApplication.getBranchNameById(branch);
        } catch (Exception e) {}

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

        String[] columns = {"ename", "cname", "start_time", "end_time"};
        String[] headers = {"Employee Name", "Location", "Start", "End"};
        Hashtable<String, formatterClass> formatter = new Hashtable();
        formatter.put("Start", new myTimeFormatter2());
        formatter.put("End", new myTimeFormatter2());
        try {
            InputStream reportStream =
                    getClass().getResourceAsStream("/rmischedule/ireports/GenericReport.jasper");

            Hashtable parameters = new Hashtable();
            String title = "Employee Rollcall for " + co + ", " + br;
            parameters.put("DateRange", "(" + df.format(startWeek.getTime()) + " - " + df.format(endWeek.getTime()) + ")");
            xGenericReportData reportData = new xGenericReportData((Record_Set) rs.get(0), headers, columns, new String[]{title}, formatter);

            report = JasperFillManager.fillReport(reportStream, parameters, reportData);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runReport(String company, Map params) {
    }

    @Override
    public JasperPrint getJasperPrint() {
        return report;
    }

    @Override
    public String getName() {
        return "employee_rep";
    }

    @Override
    public String getDisplayName() {
        return "Empoyee Report";
    }

    @Override
    public String getDescription() {
        return "This report provides basic information about currently active employees.";
    }

    @Override
    public Map<String, Class> getRequiredParameters() {
        HashMap<String, Class> parameters = new HashMap<String, Class>();
        parameters.put("start_week", Date.class);
        parameters.put("end_week", Date.class);
        return parameters;
    }
}
