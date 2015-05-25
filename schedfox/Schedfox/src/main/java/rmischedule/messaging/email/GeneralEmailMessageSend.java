/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.messaging.email;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import rmischedule.schedule.components.SEmployee;
import java.util.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.CompanyController;
import rmischeduleserver.control.EmailController;
import rmischeduleserver.control.MessagingController;
import rmischeduleserver.mysqlconnectivity.queries.client.client_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_query;
import rmischeduleserver.mysqlconnectivity.queries.reports.assemble_schedule_for_employee_report_query;
import rmischeduleserver.util.xprint.xPrintData;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Company;
import schedfoxlib.model.MessagingCommunication;

/**
 *
 * @author vnguyen
 */
public class GeneralEmailMessageSend {

    public GeneralEmailMessageSend(ArrayList<SEmployee> emps, Connection myConn,
            String globalMessage, boolean includeSignature, boolean attachPDF,
            boolean attachText, boolean hasLinks, Date startDate, Date endDate) throws Exception {
        Company currCompany = Main_Window.getCompanyById(myConn.myCompany);

        CompanyController companyController = new CompanyController(myConn.myCompany);
        EmailController emailController = new EmailController(currCompany, new Branch());
        HashMap<Integer, Branch> branchCache = new HashMap<Integer, Branch>();
        HashMap<String, String> emailsSent = new HashMap<String, String>();
        MessagingController messagingController = new MessagingController(myConn.myCompany);
        String userEmail = Main_Window.parentOfApplication.getUser().getEmail();
        for (int i = 0; i < emps.size(); i++) {
            try {
                MessagingCommunication comm = new MessagingCommunication();
                SEmployee tempData = emps.get(i);
                String email = tempData.getEmail().toLowerCase().trim();

                if (emailsSent.get(email) == null) {
                    emailsSent.put(email, email);

                    String message = globalMessage;
                    if (branchCache.get(tempData.getEmployee().getBranchId()) == null) {
                        branchCache.put(tempData.getEmployee().getBranchId(), companyController.getBranchById(tempData.getEmployee().getBranchId()));
                    }
                    if (startDate != null && endDate != null) {
                        message = emailController.getViewOnlyHtml(message, startDate, endDate, tempData.getEmployee(), false);
                        message = emailController.getConfirmHtml(message, startDate, endDate, tempData.getEmployee(), false);
                    }

                    String empId = Integer.toString(tempData.getId());
                    if (attachText) {
                        message = message.replaceAll("\\[shifts\\]", this.getShifts(empId, myConn, startDate, endDate));
                    } else {
                        message = message.replaceAll("\\[shifts\\]", "");
                    }
                    if (userEmail != null && userEmail.length() > 0) {
                        comm.setFromEmail(userEmail);
                    }
                    comm.setIsEmail(true);
                    comm.setIsSMS(false);
                    comm.setAttachPdf(attachPDF);
                    comm.setSentTo(email);
                    comm.setBody(message);
                    try {
                        comm.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
                    } catch (Exception exe) {}
                    comm.setSchedEnd(endDate);
                    comm.setSchedStart(startDate);
                    comm.setEmployeeId(tempData.getEmployee().getEmployeeId());
                    comm.setSubject(tempData.getSubject());
                    messagingController.saveMessagingCommunication(comm);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getShifts(String empId, Connection myConn, Date startDate, Date endDate) {
        client_query myClientQuery = new client_query();
        employee_query myEmployeeQuery = new employee_query();
        assemble_schedule_for_employee_report_query myQuery = new assemble_schedule_for_employee_report_query();

        SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        myClientQuery.update("", "", "");
        myEmployeeQuery.update("", 0, true);

        myQuery.update("", empId + "", databaseFormat.format(startDate), databaseFormat.format(endDate), "", "", false);
        xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, databaseFormat.format(startDate), databaseFormat.format(endDate), myConn.getServer(), myConn.myCompany, myConn.myBranch);
        return tableData.getDataString();
    }

    private byte[] printEmployeeScheduleToFile(String empId, Connection myConn, Date startDate, Date endDate) throws JRException {
        JasperPrint print = this.getReport(empId, myConn, myConn.myCompany, myConn.myBranch, startDate, endDate);
        if (print == null) {
            return null;
        } else {
            return JasperExportManager.exportReportToPdf(print);
        }
    }

    private JasperPrint getReport(String empId, Connection myConn, String companyId, String branchId, Date startDate, Date endDate) {
        client_query myClientQuery = new client_query();
        employee_query myEmployeeQuery = new employee_query();
        SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyy-MM-dd");
        assemble_schedule_for_employee_report_query myQuery = new assemble_schedule_for_employee_report_query();

        myClientQuery.setCompany(companyId);
        myEmployeeQuery.setCompany(companyId);
        myQuery.setCompany(companyId);

        if (!branchId.equals("-1")) {
            myClientQuery.setBranch(branchId);
            myEmployeeQuery.setBranch(branchId);
            myQuery.setBranch(branchId);
        }

        myClientQuery.update("", "", "");
        myEmployeeQuery.update("", Integer.parseInt(empId), true);

        try {
            myQuery.update("", empId + "", databaseFormat.format(startDate), databaseFormat.format(endDate), "", "", false);
            xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, databaseFormat.format(startDate), databaseFormat.format(endDate), myConn.getServer(), myConn.myCompany, myConn.myBranch);
            tableData.setSortType(tableData.SORT_BY_EMPLOYEE);

            try {
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischeduleserver/ireports/EmployeeSchedule.jasper");

                Company companyInfo = Main_Window.parentOfApplication.getCompanyById(myQuery.getCompany());
                Branch branchInfo = Main_Window.parentOfApplication.getBranchById(myQuery.getCompany(), myQuery.getBranch());

                Hashtable parameters = new Hashtable();
                parameters.put("SUBREPORT_DIR", "rmischeduleserver/ireports/");

                try {
                    parameters.put("Company_Name", companyInfo.getName());
                    parameters.put("Company_Address", branchInfo.getBranchInfo().getAddress());
                    parameters.put("Company_City", branchInfo.getBranchInfo().getCity());
                    parameters.put("Company_State", branchInfo.getBranchInfo().getState());
                    parameters.put("Company_Zip", branchInfo.getBranchInfo().getZip());
                    parameters.put("showEnvelope", new Boolean(false));
                } catch (Exception e) {
                    System.out.println("Filled in company informatin since could not load!");
                    parameters.put("Company_Name", "");
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
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }
};   //  end class block

