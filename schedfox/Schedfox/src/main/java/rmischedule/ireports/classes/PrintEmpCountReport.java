/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.ireports.classes;

import java.io.InputStream;
import java.sql.PreparedStatement;
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
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import rmischeduleserver.RMIScheduleServerImpl;

/**
 *
 * @author user
 */
public class PrintEmpCountReport extends RunReportInterface {

    private JasperPrint report;

    @Override
    public void runReport(String company, String branch, Map params) {
    }

    @Override
    public void runReport(String company, Map params) {
        try {
            Calendar startWeek = this.getCalendarFromParams(params, "start_week");
            Calendar endWeek = this.getCalendarFromParams(params, "end_week");
            Company myCompany = Main_Window.parentOfApplication.getCompanyById(company);
            InputStream reportStream =
                    getClass().getResourceAsStream("/rmischedule/ireports/employees_by_office.jasper");

            Connection myConn = new Connection();
            myConn.myCompany = company;


            Hashtable parameters = new Hashtable();
            parameters.put("schema", myCompany.getDB());
            parameters.put("start_date", StaticDateTimeFunctions.convertCalendarToDatabaseFormat(startWeek));
            parameters.put("end_date", StaticDateTimeFunctions.convertCalendarToDatabaseFormat(endWeek));
            java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
            try {
                PreparedStatement pstmt = myConnect.prepareStatement("SET search_path='" + myCompany.getDB().replaceAll("'", "''") + "'");
                pstmt.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public JasperPrint getJasperPrint() {
        return report;
    }

    @Override
    public String getName() {
        return "emp_count";
    }

    @Override
    public String getDisplayName() {
        return "Current Employee Count";
    }

    @Override
    public String getDescription() {
        return "This report shows the current active employee counts across all "
                + "branches.";
    }

    @Override
    public Map<String, Class> getRequiredParameters() {
        HashMap<String, Class> parameters = new HashMap<String, Class>();
        parameters.put("start_week", Date.class);
        parameters.put("end_week", Date.class);
        return parameters;
    }
}
