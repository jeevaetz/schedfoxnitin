/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.ireports.classes;

import java.io.InputStream;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.data_connection.Connection;
import rmischedule.ireports.RunReportInterface;
import rmischedule.main.Main_Window;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.model.Company;

/**
 *
 * @author user
 */
public class PrintClientBreakdown extends RunReportInterface {

    private JasperPrint print;

    @Override
    public void runReport(String company, String branch, Map params) {
        InputStream reportStream =
                getClass().getResourceAsStream("/rmischedule/ireports/client_location_breakdown.jasper");

        Connection myConn = new Connection();
        myConn.myCompany = company;
        Company myCompany = Main_Window.getCompanyById(company);

        Hashtable parameters = new Hashtable();
        parameters.put("schema", myCompany.getDB());
        parameters.put("branchId", branch);
        try {
            java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();

            print = JasperFillManager.fillReport(reportStream, parameters, myConnect);
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
        return "client_breakdown_report";
    }

    @Override
    public String getDisplayName() {
        return "Client Breakdown Report";
    }

    @Override
    public String getDescription() {
        return "Displays the breakdown of armed ot unarmed clients as well as industry type.";
    }

    @Override
    public Map<String, Class> getRequiredParameters() {
        return new HashMap<String, Class>();
    }
}
