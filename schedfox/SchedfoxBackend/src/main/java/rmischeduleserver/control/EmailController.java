/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.poi.util.IOUtils;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.control.model.GoogleURLModel;
import rmischeduleserver.mysqlconnectivity.queries.client.client_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_query;
import rmischeduleserver.mysqlconnectivity.queries.reports.assemble_schedule_for_employee_report_query;
import rmischeduleserver.util.xprint.xPrintData;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Company;
import schedfoxlib.model.Employee;

/**
 *
 * @author ira
 */
public class EmailController {

    private Company company;
    private Branch branch;
    public static final String VERIFY_URL = "\\[verify_url\\]";
    public static final String VIEW_URL = "\\[view_url\\]";

    public EmailController(Company company, Branch branch) {
        this.company = company;
        this.branch = branch;
    }

    public boolean containsVerifyOrView(String text) {
        Pattern verifyPattern = Pattern.compile(VERIFY_URL);
        Matcher verifyMatcher = verifyPattern.matcher(text);

        Pattern viewPattern = Pattern.compile(VIEW_URL);
        Matcher viewMatcher = viewPattern.matcher(text);

        return verifyMatcher.find() || viewMatcher.find();
    }

    public String getConfirmHtml(String emailText, Date startDate, Date endDate, Employee employee, boolean isSMS) {
        try {
            Date currDate = new Date();
            SimpleDateFormat myFormat = new SimpleDateFormat("MM.dd.yyyy.HH.mm.ss");
            String url = "http://schedfoximage.schedfox.com:8081/SchedfoxEndPoint/view_schedule.xhtml?employeeId="
                    + employee.getEmployeeId() + "&startDate=" + URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd").format(startDate))
                    + "&endDate=" + URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd").format(endDate), "UTF-8") + "&companyId=" + company.getCompId() 
                    + "&currDate=" + URLEncoder.encode(myFormat.format(currDate), "UTF-8");
            if (isSMS) {
                return emailText.replaceAll(VERIFY_URL, EmailController.getShortenedURL(url));
            } else {
                return emailText.replaceAll(VERIFY_URL, "<a href=\"" + url + "\">here</a>").replaceAll("\n", "<br/>");
            }
        } catch (Exception exe) {
            return "";
        }
    }

    public static String getShortenedURL(String longURL) {
        String retVal = "";

        InputStream iStream = null;
        ByteArrayOutputStream oStream = null;
        
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = null;
            HttpEntity entity = null;
            HttpPost httpost = new HttpPost("http://s.champ.net/index.php?s=" + URLEncoder.encode(longURL, "UTF-8"));
            httpost.setHeader("Content-Type", "application/json");

            StringEntity s = new StringEntity("{\"longUrl\": \"" + longURL + "\"}");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            entity = s;
            httpost.setEntity(entity);

            response = httpclient.execute(httpost);
            entity = response.getEntity();

            iStream = entity.getContent();
            oStream = new ByteArrayOutputStream();

            IOUtils.copy(iStream, oStream);
            
            return oStream.toString();
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            try {
                iStream.close();
            } catch (Exception exe) {}
            try {
                oStream.close();
            } catch (Exception exe) {}
        }

        return retVal;
    }

    public static void main(String args[]) {
        EmailController.getShortenedURL("www.google.com");
    }

    public String getViewOnlyHtml(String emailText, Date startDate, Date endDate, Employee employee, boolean isSMS) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String url = "http://schedfoximage.schedfox.com:8081/SchedfoxEndPoint/view_current_schedule.xhtml?employeeId=" + employee.getEmployeeId()
                    + "&companyId=" + company.getCompId() + "&type=pdf&startDate=" + URLEncoder.encode(dateFormat.format(startDate.getTime()), "UTF-8") 
                    + "&isSMS=" + isSMS;
            if (isSMS) {
                return emailText.replaceAll(VIEW_URL, EmailController.getShortenedURL(url));
            } else {
                return emailText.replaceAll(VIEW_URL, "<a href=\"" + url + "\">here</a>").replaceAll("\n", "<br/>");
            }
        } catch (Exception exe) {
            return "";
        }
    }

    public JasperPrint getReport(String empId, String startWeek, String endWeek) {
        client_query myClientQuery = new client_query();
        employee_query myEmployeeQuery = new employee_query();
        assemble_schedule_for_employee_report_query myQuery = new assemble_schedule_for_employee_report_query();

        myClientQuery.setCompany(company.getCompId());
        myEmployeeQuery.setCompany(company.getCompId());
        myQuery.setCompany(company.getCompId());

        myClientQuery.setBranch(branch.getBranchId().toString());
        myEmployeeQuery.setBranch(branch.getBranchId().toString());
        myQuery.setBranch(branch.getBranchId().toString());

        myClientQuery.update("", "", "");
        myEmployeeQuery.update("", Integer.parseInt(empId), true);


        try {
            myQuery.update("", empId + "", startWeek, endWeek, "", "", false);

            RMIScheduleServerImpl myConn = RMIScheduleServerImpl.getInstance();
            xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, startWeek, endWeek, myConn, company.getCompId().toString(), branch.getBranchId().toString());
            tableData.setSortType(tableData.SORT_BY_EMPLOYEE);


            try {
                InputStream reportStream = null;
                try {
                    reportStream =
                            getClass().getResourceAsStream("/rmischeduleserver/ireports/EmployeeSchedule.jasper");
                } catch (Exception exe) {
                }
                if (reportStream == null) {
                    System.out.println("No resource stream!");
                    reportStream = this.getClass().getClassLoader().getResourceAsStream("/rmischeduleserver/ireports/EmployeeSchedule.jasper");
                }

                Hashtable parameters = new Hashtable();
                parameters.put("SUBREPORT_DIR", "rmischeduleserver/ireports/");

                try {
                    parameters.put("Company_Name", company.getName());
                    parameters.put("Company_Address", branch.getBranchInfo().getAddress());
                    parameters.put("Company_City", branch.getBranchInfo().getCity());
                    parameters.put("Company_State", branch.getBranchInfo().getState());
                    parameters.put("Company_Zip", branch.getBranchInfo().getZip());
                    parameters.put("showEnvelope", new Boolean(false));
                } catch (Exception e) {
                    System.out.println("Filled in company informatin since could not load!");
                    parameters.put("Company_Name", "Champion National Security");
                    parameters.put("Company_Address", "");
                    parameters.put("Company_City", "");
                    parameters.put("Company_State", "");
                    parameters.put("Company_Zip", "");
                    parameters.put("showEnvelope", new Boolean(false));
                }
                JasperPrint retVal = JasperFillManager.fillReport(reportStream, parameters, tableData);
                if (tableData.hasData()) {
                    return retVal;
                } else {
                    System.out.println("No data!");
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
