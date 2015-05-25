/*
 * phonelistreport.java
 *
 * Created on June 16, 2005, 10:42 AM
 */
package rmischedule.schedule.print.phonelistreports;

import java.io.InputStream;
import java.util.Hashtable;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import rmischeduleserver.mysqlconnectivity.queries.reports.phone_list_query;
import rmischedule.data_connection.Connection;
import rmischedule.ireports.viewer.IReportViewer;
import rmischedule.main.*;
import rmischedule.xprint.data.xGenericReportData;
import rmischedule.xprint.templates.genericreportcomponents.formatterClass;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class phonelistreport {

    private phone_list_query myQuery;
    private Main_Window Parent;
    private String co;
    private String br;

    /** Creates a new instance of phonelistreport */
    public phonelistreport(String branch, String company, Main_Window mw) {
        Parent = mw;
        br = branch;
        co = company;
        Connection myConn = new Connection();
        myConn.setCompany(company);
        myConn.setBranch(branch);
        Record_Set rs = new Record_Set();
        myQuery = new phone_list_query();
        myQuery.update(branch, company);
        myConn.prepQuery(myQuery);
        try {
            rs = myConn.executeQuery(myQuery);
        } catch (Exception e) {
        }
        generatePrintPreview(rs);
    }

    private void generatePrintPreview(Record_Set rs) {
        String[] columns = {"lname", "fname", "phone", "phone2", "cell", "pager"};
        String[] headers = {"Last Name", "First Name", "Phone 1", "Phone 2", "Cell", "Pager"};
        Hashtable<String, formatterClass> formatter = new Hashtable();

        try {
            InputStream reportStream =
                    getClass().getResourceAsStream("/rmischedule/ireports/GenericReport.jasper");

            Hashtable parameters = new Hashtable();
            String title = "Phone List for " + Main_Window.parentOfApplication.getCompanyNameById(co) + ", " + Main_Window.parentOfApplication.getBranchNameById(br);
            parameters.put("DateRange", "");
            xGenericReportData reportData = new xGenericReportData(rs, headers, columns, new String[]{title}, formatter);

            JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, reportData);
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
