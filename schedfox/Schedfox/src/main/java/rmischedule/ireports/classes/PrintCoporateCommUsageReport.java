/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.ireports.classes;

import java.io.InputStream;

import java.text.SimpleDateFormat;
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
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import rmischeduleserver.RMIScheduleServerImpl;

/**
 *
 * @author user
 */
public class PrintCoporateCommUsageReport extends RunReportInterface {

    protected String companyId;

    private JasperPrint report;

    public PrintCoporateCommUsageReport() {
    }

    public void runReport(String company, String branch, Map params) {
        
    }

    public void runReport(String company, Map params) {
        try {

            Calendar startWeek = this.getCalendarFromParams(params, "start_week");
            Calendar endWeek = this.getCalendarFromParams(params, "end_week");
            ArrayList<Integer> selectedDMs = (ArrayList<Integer>)params.get("selectedDM");
            if (selectedDMs == null) {
                selectedDMs = new ArrayList<Integer>();
                selectedDMs.add(-1);
            }

            String[] dates = new String[2];
            SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");

            dates[0] = myFormat.format(startWeek.getTime());
            dates[1] = myFormat.format(endWeek.getTime());
            if (dates != null) {
                Company myCompany = Main_Window.getCompanyById(company);
                InputStream reportStream =
                        getClass().getResourceAsStream("/rmischedule/ireports/corporate_communicator_sums_report.jasper");

                Connection myConn = new Connection();
                myConn.myCompany = company;


                Hashtable parameters = new Hashtable();
                parameters.put("schema", myCompany.getDB());
                parameters.put("start_date", dates[0]);
                parameters.put("end_date", dates[1]);
                parameters.put("dm_ints", selectedDMs);
                java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();

                report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JasperPrint getJasperPrint() {
        return report;
    }

    @Override
    public String getName() {
        return "cc_usage";
    }

    @Override
    public String getDisplayName() {
        return "Corporate Communicator Usage";
    }

    @Override
    public String getDescription() {
        return "This report shows what DM's added new corporate communicators for the "
                + "provided timeline.";
    }

    @Override
    public Map<String, Class> getRequiredParameters() {
        HashMap<String, Class> parameters = new HashMap<String, Class>();
        parameters.put("start_week", Date.class);
        parameters.put("end_week", Date.class);
        parameters.put("selectedDM", ArrayList.class);
        return parameters;
    }
}
