/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.ireports.classes;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.ireports.RunReportInterface;
import rmischedule.main.Main_Window;
import rmischedule.xprint.templates.genericreportcomponents.formatterClass;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.control.CompanyController;
import schedfoxlib.model.Branch;

/**
 *
 * @author user
 */
public class PrintDemographics extends RunReportInterface {

    private JasperPrint myPrint;

    public void runReport(String company, String branch, Map params) {
        Hashtable<String, formatterClass> formatter = new Hashtable();

        try {
            String schema = "champion_db";
            try {
                schema = Main_Window.parentOfApplication.getCompanyById(company).getDB();
            } catch (Exception e) {}

            InputStream reportStream =
                    getClass().getResourceAsStream("/rmischedule/ireports/eeco_report.jasper");

            String branchName = "All Branches";
            try {
                CompanyController compController = new CompanyController();
                Branch myBranch = compController.getBranchById(Integer.parseInt(branch));
                branchName = myBranch.getBranchName();
            } catch (Exception exe) {}
            
            Hashtable parameters = new Hashtable();
            parameters.put("schema", schema);
            parameters.put("branchId", branch);
            parameters.put("branch_name", branchName);

            java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();

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

    @Override
    public String getName() {
        return "demo";
    }

    @Override
    public String getDisplayName() {
        return "Demographics Report";
    }

    @Override
    public String getDescription() {
        return "This report provides a breakdown of employees by gender and race, "
                + "for government statistic reporting.";
    }

    @Override
    public Map<String, Class> getRequiredParameters() {
        HashMap<String, Class> parameters = new HashMap<String, Class>();
        parameters.put("start_week", Date.class);
        parameters.put("end_week", Date.class);
        return parameters;
    }
}
