/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.EmailEmployeeSchedule;

import rmischeduleserver.mysqlconnectivity.queries.EmailEmployeeSchedule.getEmployeeToEmail;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.*;
import java.util.*;
import net.sf.jasperreports.engine.JRException;
import rmischeduleserver.mysqlconnectivity.queries.reports.*;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.data_connection.Connection;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
import rmischeduleserver.util.xprint.xPrintData;
import rmischedule.main.Main_Window;
import schedfoxlib.model.Company;
import rmischedule.messaging.email.SchedfoxEmail;
import rmischeduleserver.control.EmployeeController;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Employee;
import schedfoxlib.model.MessagingCommunication;

/**
 *
 * @author vnguyen
 */
public class PrintEmployees implements Runnable {

    private Connection myConn = new Connection();
    private String startWeek;
    private String endWeek;
    private Date startDate;
    private Date endDate;
    private EmailValidator emailValidator = new EmailValidator();
    private Vector<String> sent = new Vector<String>();
    private Vector<String> notSent = new Vector<String>();
    private String branchId;
    private String companyId;
    public Thread runner;

    private PrintEmployees() {
        //never called so made private for protection
    }

    public PrintEmployees(String branchId, String companyId, String startWeek, String endWeek, Calendar startDate, Calendar endDate, Connection conn) {
        this.branchId = branchId;
        this.companyId = companyId;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.startDate = startDate.getTime();
        this.endDate = endDate.getTime();
        this.myConn = conn;
        this.runner = new Thread(this);
        int result = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication.desktop,
                "Do you wish to email all employees their schedule" + this.startWeek + " to " + this.endWeek + "? \n Warning: This will run in the background and may take a few minutes");
        if (result == 0) {
            this.runner.start();
        }
    }

    public PrintEmployees(MessagingCommunication comm, Connection myConn) throws JRException, AddressException, MessagingException {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            this.startWeek = myFormat.format(comm.getSchedStart());
            this.endWeek = myFormat.format(comm.getSchedEnd());
        } catch (Exception exe) {}
        this.startDate = comm.getSchedStart();
        this.endDate = comm.getSchedEnd();
        this.myConn = myConn;
        this.companyId = myConn.myCompany;
        try {
            this.branchId = myConn.myBranch;
            if (this.branchId.trim().length() == 0) {
                this.branchId = "-1";
            }
        } catch (Exception e) {
        }

        String message = comm.getBody();
        if (message.contains("\\[shifts\\]")) {
            message = message.replaceAll("\\[shifts\\]", this.getShifts(comm.getEmployeeId()));
        } else {
            message = message.replaceAll("\\[shifts\\]", "");
        }
        String[] correctedEmails = {comm.getSentTo()};
        if (comm.getAttachPdf()) {
            byte[] dataToFile = this.printEmployeeScheduleToFile(comm.getEmployeeId().toString());
            if (dataToFile != null) {
                new SchedfoxEmail(comm.getSubject(), message, correctedEmails, dataToFile, true);
            }
        } else {
            new SchedfoxEmail(comm.getSubject(), message, correctedEmails, true);
        }
    }

    private byte[] printEmployeeScheduleToFile(String empId) throws JRException {
        JasperPrint print = this.getReport(empId);
        if (print == null) {
            return null;
        } else {
            return JasperExportManager.exportReportToPdf(print);
        }
    }

    private JasperPrint getReport(String empId) {
        client_query myClientQuery = new client_query();
        employee_query myEmployeeQuery = new employee_query();
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
            myQuery.update("", empId + "", startWeek, endWeek, "", "", false);
            xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, startWeek, endWeek, this.myConn.getServer(), this.myConn.myCompany, this.myConn.myBranch);
            tableData.setSortType(tableData.SORT_BY_EMPLOYEE);

            try {
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischeduleserver/ireports/EmployeeSchedule.jasper");

                Company companyInfo = Main_Window.parentOfApplication.getCompanyById(myQuery.getCompany());
                Branch branchInfo = Main_Window.parentOfApplication.getBranchById(myQuery.getCompany(), myQuery.getBranch());

                Hashtable parameters = new Hashtable();
                parameters.put("SUBREPORT_DIR", "rmischedule/ireports/");

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

    private void sendEmail(String[] sendTo, String name, byte[] dataForFile) {
        try {
            String begin = this.startWeek.substring(5) + "-" + this.startWeek.substring(1, 4);
            String end = this.endWeek.substring(5) + "-" + this.endWeek.substring(1, 4);
            String subject = "Schedule for: " + begin + " to " + end;
            String msg = name + " this is the " + subject + ", if it is blank then there is no schedule on file for you during this period";
            new SchedfoxEmail(subject, msg, sendTo, dataForFile, false);
            String confirmation = name + " recieved " + subject;
            this.getSent().add(confirmation);
        } catch (Exception ex) {
        }
    }

    public void run() {
        Record_Set rs = this.myConn.executeQuery(new getEmployeeToEmail(this.branchId));

        do {
            String emp_id = rs.getString("employee_id");
            String name = rs.getString("employee_first_name") + " " + rs.getString("employee_last_name");
            String emailPrimary = rs.getString("employee_email");
            String emailSecondary = rs.getString("employee_email2");

            String[] sendTo = null;
            if (this.emailValidator.validate(emailPrimary)) {
                String[] temp = {emailPrimary};
                sendTo = temp;
            } else if (this.emailValidator.validate(emailSecondary)) {
                String[] temp = {emailSecondary};
                sendTo = temp;
            } else {
                this.notSent.add(name);
            }
            if (sendTo != null) {
                try {

                    byte[] dataForFile = this.printEmployeeScheduleToFile(emp_id);
                    if (dataForFile != null) {
                        this.sendEmail(sendTo, name, dataForFile);
                    }

                } catch (Exception ex) {
                }
            }
        } while (rs.moveNext());
        StringBuilder str = new StringBuilder();
        for (String i : getSent()) {
            str.append(i + "\n");
        }
        JOptionPane.showMessageDialog(Main_Window.parentOfApplication.desktop, str.toString(), "Email Sent Confirmation", JOptionPane.OK_OPTION);
    }

    /**
     * @return the sent
     */
    public Vector<String> getSent() {
        return sent;
    }

    /**
     * @return the notSent
     */
    public Vector<String> getNotSent() {
        return notSent;
    }

    class EmailValidator {

        private Pattern pattern;
        private Matcher matcher;
        private final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public EmailValidator() {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        /**
         * Validate email with regular expression
         *
         * @param hex hex for validation
         * @return true valid hex, false invalid hex
         */
        public boolean validate(final String email) {
            matcher = pattern.matcher(email);
            Pattern spaces = Pattern.compile("\\S+");
            Matcher m = spaces.matcher(email);
            if (!m.matches()) {
                return false;
            }
            boolean temp = matcher.matches();
            System.out.println("This is a valid email: " + email);
            return true;
        }
    }

    private String getShifts(Integer empId) {
        client_query myClientQuery = new client_query();
        employee_query myEmployeeQuery = new employee_query();
        assemble_schedule_for_employee_report_query myQuery = new assemble_schedule_for_employee_report_query();

        myClientQuery.update("", "", "");
        myEmployeeQuery.update("", 0, true);

        myQuery.update("", empId + "", startWeek, endWeek, "", "", false);
        xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, startWeek, endWeek, this.myConn.getServer(), this.myConn.myCompany, this.myConn.myBranch);
        return tableData.getDataString();
    }
}
