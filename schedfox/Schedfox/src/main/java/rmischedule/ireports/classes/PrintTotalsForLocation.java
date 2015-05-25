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
import rmischedule.xprint.templates.genericreportcomponents.formatterClass;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.reports.totals_by_location_query;

/**
 *
 * @author user
 */
public class PrintTotalsForLocation extends RunReportInterface {

    private JasperPrint report;

    @Override
    public void runReport(String company, String branch, Map params) {
        Calendar st = this.getCalendarFromParams(params, "start_week");
        Calendar ed = this.getCalendarFromParams(params, "end_week");
        String startWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(st);
        String endWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(ed);
        totals_by_location_query myQuery = new totals_by_location_query();
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
        String[] myHeaders = new String[7];
        for (int i = 0; i < myHeaders.length; i++) {
            myHeaders[i] = StaticDateTimeFunctions.convertCalendarToReadableFormat(st);
            st.add(Calendar.DAY_OF_MONTH, 1);
        }
        String[] headers = {myHeaders[0], myHeaders[1], myHeaders[2], myHeaders[3], myHeaders[4], myHeaders[5], myHeaders[6], "Total"};
        String co = Main_Window.parentOfApplication.getCompanyById(company).getName();
        String br = "";
        try {
            br = Main_Window.parentOfApplication.getBranchNameById(branch);
        } catch (Exception e) {}

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        formatterClass timeFormatter = new myTimeFormatter();

        Hashtable<String, formatterClass> formatter = new Hashtable();
        for (int i = 0; i < myHeaders.length; i++) {
            formatter.put(myHeaders[i], timeFormatter);
        }
        try {
            InputStream reportStream =
                    getClass().getResourceAsStream("/rmischedule/ireports/UskedExportReport.jasper");

            Hashtable parameters = new Hashtable();
            String titles = "Exported Data For " + co + ", " + br + " " + "\r\n(" + df.format(ed.getTime()) + " - " + df.format(st.getTime()) + ")";
            rmischedule.data_export.ultra32.ExportReportData reportData = new rmischedule.data_export.ultra32.ExportReportData(titles, headers, (Record_Set) rs.get(0));

            report = JasperFillManager.fillReport(reportStream, parameters, reportData);
        } catch (Exception exe) {
            exe.printStackTrace();
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
        return "totals_rep";
    }

    @Override
    public String getDisplayName() {
        return "Location Totals Report";
    }

    @Override
    public String getDescription() {
        return "This report prints totals by location.";
    }

    @Override
    public Map<String, Class> getRequiredParameters() {
        HashMap<String, Class> parameters = new HashMap<String, Class>();
        parameters.put("start_week", Date.class);
        parameters.put("end_week", Date.class);
        return parameters;
    }
}
