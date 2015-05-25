/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.mail.HtmlEmail;
import rmischeduleserver.IPLocationFile;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.officerdailyreport.*;
import rmischeduleserver.util.MailAuthenticator;
import schedfoxlib.controller.OfficerDailyReportControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.CompanyObj;
import schedfoxlib.model.Employee;
import schedfoxlib.model.ManagementClient;
import schedfoxlib.model.OfficerDailyReport;
import schedfoxlib.model.OfficerDailyReportDocument;
import schedfoxlib.model.OfficerDailyReportText;
import schedfoxlib.model.WrapperEmployeeClientSelections;
import schedfoxlib.model.util.Record_Set;

/**
 * 
 * @author user
 */
public class OfficerDailyReportController implements OfficerDailyReportControllerInterface {

    private String companyId;

    public OfficerDailyReportController(String companyId) {
        this.companyId = companyId;
    }

    public static OfficerDailyReportController getInstance(String companyId) {
        return new OfficerDailyReportController(companyId);
    }

    public void checkIfShouldNofifyAndDoSo(OfficerDailyReport officerDailyReport) throws SaveDataException, RetrieveDataException {
        OfficerDailyReport report = null;
        try {
            report = this.getODRByShiftIdAndDate(officerDailyReport.getShiftId(), new Date());
        } catch (Exception exe) {}
            

        String email = "auto@champ.net";
        try {
            CompanyObj currComp = CompanyController.getInstance().getCompanyObjById(Integer.parseInt(companyId));
            ManagementClient managementCli = ManagementClientController.getInstance(companyId).getManagementClientById(currComp.getCompanyManagementId());
            email = managementCli.getManagement_client_email();
        } catch (Exception exe) {
        }
        boolean doesntUserShifts = false;
        try {
            doesntUserShifts = officerDailyReport.getShiftId() == 0;
        } catch (Exception e) {}
        
        if (report == null || report.getOfficerDailyReportId() == null || doesntUserShifts) {
            ClientController clientController = ClientController.getInstance(companyId);
            EmployeeController employeeController = EmployeeController.getInstance(companyId);
            Client clientToNotify = clientController.getClientById(officerDailyReport.getClientId());
            Employee employeeThatLoggedIn = employeeController.getEmployeeById(officerDailyReport.getEmployee_id());
            ArrayList<ClientContact> contacts = clientToNotify.getContacts(companyId);
            boolean notifyClient = false;

            for (int c = 0; c < contacts.size(); c++) {
                if (contacts.get(c).getClientContactEmailOnLogin()) {
                    notifyClient = true;

                    try {
                        HtmlEmail htmlEmail = new HtmlEmail();
                        htmlEmail.setFrom(email);
                        if (employeeThatLoggedIn.getFullName() == null || employeeThatLoggedIn.getFullName().length() <= 1) {
                            employeeThatLoggedIn.setEmployeeFirstName("Supervisor");
                        }
                        if (officerDailyReport.getLoggedOut() != null) {
                            htmlEmail.setSubject(employeeThatLoggedIn.getEmployeeFullName() + " has Logged Out");
                            htmlEmail.setHtmlMsg("The following employee (" + employeeThatLoggedIn.getFullName() + ") has logged out of " + clientToNotify.getClientName() + "!");
                        } else {
                            htmlEmail.setSubject(employeeThatLoggedIn.getEmployeeFullName() + " has Logged In");
                            htmlEmail.setHtmlMsg("The following employee (" + employeeThatLoggedIn.getFullName() + ") has logged into " + clientToNotify.getClientName() + "!");
                        }
                            
                        htmlEmail.addTo(contacts.get(c).getEmailAddress());
                        htmlEmail.setAuthenticator(new MailAuthenticator(IPLocationFile.getEMAIL_USER(), IPLocationFile.getEMAIL_PASSWORD()));
                        htmlEmail.setHostName(IPLocationFile.getEMAIL_HOST());
                        htmlEmail.setSmtpPort(Integer.parseInt(IPLocationFile.getEMAIL_PORT()));
                        htmlEmail.send();
                    } catch (Exception exe) {
                        exe.printStackTrace();
                    }
                }
            }
            try {
                if (notifyClient) {
                    for (int c = 0; c < contacts.size(); c++) {
                        if (contacts.get(c).getClientContactIsPrimary() == 1) {
                            HtmlEmail htmlEmail = new HtmlEmail();
                            htmlEmail.setFrom(email);
                            htmlEmail.setHtmlMsg("The following employee (" + employeeThatLoggedIn.getFullName() + ") has logged into the post!");
                            htmlEmail.setSubject("Employee Has Logged In");
                            htmlEmail.addTo(contacts.get(c).getEmailAddress());
                            htmlEmail.setAuthenticator(new MailAuthenticator(IPLocationFile.getEMAIL_USER(), IPLocationFile.getEMAIL_PASSWORD()));
                            htmlEmail.setHostName(IPLocationFile.getEMAIL_HOST());
                            htmlEmail.setSmtpPort(Integer.parseInt(IPLocationFile.getEMAIL_PORT()));
                            htmlEmail.send();
                        }
                    }
                }
            } catch (Exception exe) {
            }

        } else if (report.getLoggedOut() != null) {
            ClientController clientController = ClientController.getInstance(companyId);
            EmployeeController employeeController = EmployeeController.getInstance(companyId);
            Client clientToNotify = clientController.getClientById(officerDailyReport.getClientId());
            Employee employeeThatLoggedIn = employeeController.getEmployeeById(officerDailyReport.getEmployee_id());
            ArrayList<ClientContact> contacts = clientToNotify.getContacts(companyId);
            boolean notifyClient = false;

            for (int c = 0; c < contacts.size(); c++) {
                if (contacts.get(c).getClientContactEmailOnLogin()) {
                    notifyClient = true;

                    try {
                        HtmlEmail htmlEmail = new HtmlEmail();
                        htmlEmail.setFrom(email);
                        htmlEmail.setHtmlMsg("The following employee (" + employeeThatLoggedIn.getFullName() + ") has logged out of the post!");
                        htmlEmail.setSubject("Employee Has Logged Out");
                        htmlEmail.addTo(contacts.get(c).getEmailAddress());
                        htmlEmail.setAuthenticator(new MailAuthenticator(IPLocationFile.getEMAIL_USER(), IPLocationFile.getEMAIL_PASSWORD()));
                        htmlEmail.setHostName(IPLocationFile.getEMAIL_HOST());
                        htmlEmail.setSmtpPort(Integer.parseInt(IPLocationFile.getEMAIL_PORT()));
                        htmlEmail.send();
                    } catch (Exception exe) {
                        exe.printStackTrace();
                    }
                }
            }
            try {
                if (notifyClient) {
                    for (int c = 0; c < contacts.size(); c++) {
                        if (contacts.get(c).getClientContactIsPrimary() == 1) {
                            HtmlEmail htmlEmail = new HtmlEmail();
                            htmlEmail.setFrom(email);
                            htmlEmail.setHtmlMsg("The following employee (" + employeeThatLoggedIn.getFullName() + ") has logged out of the post!");
                            htmlEmail.setSubject("Employee Has Logged Out");
                            htmlEmail.addTo(contacts.get(c).getEmailAddress());
                            htmlEmail.setAuthenticator(new MailAuthenticator(IPLocationFile.getEMAIL_USER(), IPLocationFile.getEMAIL_PASSWORD()));
                            htmlEmail.setHostName(IPLocationFile.getEMAIL_HOST());
                            htmlEmail.setSmtpPort(Integer.parseInt(IPLocationFile.getEMAIL_PORT()));
                            htmlEmail.send();
                        }
                    }
                }
            } catch (Exception exe) {
            }
        }
    }
    
    public ArrayList<OfficerDailyReport> getDailyReportsForEmployeeAndClientWithText(int clientId, int employeeId, Date startDate, Date endDate) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReport> retVal = new ArrayList<OfficerDailyReport>();

        EmployeeController empController = EmployeeController.getInstance(companyId);

        get_daily_reports_by_client_employee_query dailyReportQuery = new get_daily_reports_by_client_employee_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{clientId, clientId, employeeId, employeeId, 
            new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime())});

        HashMap<Integer, OfficerDailyReport> reports = new HashMap<Integer, OfficerDailyReport>();
        HashMap<Integer, Employee> users = new HashMap<Integer, Employee>();
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                OfficerDailyReport dailyReport = new OfficerDailyReport(rst);
                if (users.get(dailyReport.getEmployee_id()) == null) {
                    users.put(dailyReport.getEmployee_id(), empController.getEmployeeById(dailyReport.getEmployee_id()));
                }
                dailyReport.setOfficer(users.get(dailyReport.getEmployee_id()));
                if (reports.get(dailyReport.getOfficerDailyReportId()) == null) {
                    reports.put(dailyReport.getOfficerDailyReportId(), dailyReport);
                    dailyReport.setReportTexts(new ArrayList<OfficerDailyReportText>());
                }

                OfficerDailyReportText dailyReportText = new OfficerDailyReportText(rst);
                reports.get(dailyReport.getOfficerDailyReportId()).getReportTexts().add(dailyReportText);
                
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        
        Iterator<OfficerDailyReport> officerReports = reports.values().iterator();
        while (officerReports.hasNext()) {
            retVal.add(officerReports.next());
        }
        return retVal;
    }
    
    public ArrayList<OfficerDailyReport> getDailyReportsForEmployeeAndClientsWithText(WrapperEmployeeClientSelections employeeClientSel, Integer branchId, Date startDate, Date endDate) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReport> retVal = new ArrayList<OfficerDailyReport>();

        ArrayList<Object> val = new ArrayList<Object>();
        val.addAll(employeeClientSel.getSelectedClientIds());
        val.addAll(employeeClientSel.getSelectedEmployeeIds());
        val.add(branchId);
        val.add(branchId);
        val.add(new java.sql.Date(startDate.getTime()));
        val.add(new java.sql.Date(endDate.getTime()));
        
        get_daily_reports_by_clients_employee_query dailyReportQuery = new get_daily_reports_by_clients_employee_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.update(employeeClientSel.getSelectedClientIds().size(), employeeClientSel.getSelectedEmployeeIds().size());
        dailyReportQuery.setPreparedStatement(val.toArray());

        HashMap<Integer, OfficerDailyReport> reports = new HashMap<Integer, OfficerDailyReport>();
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                OfficerDailyReport dailyReport = new OfficerDailyReport(rst);
                dailyReport.setOfficer(new Employee(new Date(), rst));
                dailyReport.setClient(new Client(new Date(), rst));
                if (reports.get(dailyReport.getOfficerDailyReportId()) == null) {
                    reports.put(dailyReport.getOfficerDailyReportId(), dailyReport);
                    dailyReport.setReportTexts(new ArrayList<OfficerDailyReportText>());
                }

                OfficerDailyReportText dailyReportText = new OfficerDailyReportText(rst);
                reports.get(dailyReport.getOfficerDailyReportId()).getReportTexts().add(dailyReportText);
                
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        
        Iterator<OfficerDailyReport> officerReports = reports.values().iterator();
        while (officerReports.hasNext()) {
            retVal.add(officerReports.next());
        }
        return retVal;
    }

    public ArrayList<OfficerDailyReport> getDailyReportsForClientWithText(int clientId, int numberOfDays, boolean returnWithTextOnly) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReport> retVal = new ArrayList<OfficerDailyReport>();

        EmployeeController empController = EmployeeController.getInstance(companyId);

        daily_reports_by_client_id_query dailyReportQuery = new daily_reports_by_client_id_query(!returnWithTextOnly);
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{clientId});

        HashMap<Integer, Employee> users = new HashMap<Integer, Employee>();
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                OfficerDailyReport dailyReport = new OfficerDailyReport(rst);
                dailyReport.getReportTexts(companyId);
                if (users.get(dailyReport.getEmployee_id()) == null) {
                    users.put(dailyReport.getEmployee_id(), empController.getEmployeeById(dailyReport.getEmployee_id()));
                }
                dailyReport.setOfficer(users.get(dailyReport.getEmployee_id()));
                retVal.add(dailyReport);

                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<OfficerDailyReport> getDailyReportsForDates(String startDate, String endDate, Boolean showOnlyOnesWithText) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReport> retVal = new ArrayList<OfficerDailyReport>();

        get_daily_reports_by_dates_query dailyReportQuery = new get_daily_reports_by_dates_query(showOnlyOnesWithText);
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{startDate, endDate});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                OfficerDailyReport dailyReport = new OfficerDailyReport(rst);
                dailyReport.getReportTexts(companyId);
                dailyReport.getOfficer(companyId);
                retVal.add(dailyReport);

                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public ArrayList<OfficerDailyReport> getDailyReportsForClient(int clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReport> retVal = new ArrayList<OfficerDailyReport>();

        daily_reports_by_client_id_query dailyReportQuery = new daily_reports_by_client_id_query(true);
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{clientId});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                OfficerDailyReport dailyReport = new OfficerDailyReport(rst);
                dailyReport.getReportTexts(companyId);
                dailyReport.getOfficer(companyId);
                retVal.add(dailyReport);

                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public byte[] fetchImageDataForDocument(int officerReportDocumentId) throws RetrieveDataException {
        byte[] retVal = null;
        daily_report_documents_by_report_id_query getIncidentQuery = new daily_report_documents_by_report_id_query(true);
        getIncidentQuery.setPreparedStatement(new Object[]{officerReportDocumentId});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = rst.getByteArray("document_data");
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
    /**
     * Used to grab ODR's between timestamps not 
     * @param clientId
     * @param date
     * @param date2
     * @return
     * @throws RetrieveDataException 
     */
    public ArrayList<OfficerDailyReport> getDailyReportsForClientAndDates(int clientId, Date date, Date date2) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReport> retVal = new ArrayList<OfficerDailyReport>();

        daily_reports_by_client_id_and_dates_query dailyReportQuery = new daily_reports_by_client_id_and_dates_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{clientId, new java.sql.Timestamp(date.getTime()), new java.sql.Timestamp(date2.getTime())});

        HashMap<Integer, Employee> officerHash = new HashMap<Integer, Employee>();

        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                OfficerDailyReport dailyReport = new OfficerDailyReport(rst);
                if (officerHash.get(dailyReport.getEmployee_id()) == null) {
                    dailyReport.getOfficer(companyId);
                    officerHash.put(dailyReport.getEmployee_id(), dailyReport.getOfficer());
                } else {
                    dailyReport.setOfficer(officerHash.get(dailyReport.getEmployee_id()));
                }
                dailyReport.getOfficer(companyId);
                dailyReport.getReportTexts(companyId);
                retVal.add(dailyReport);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<OfficerDailyReport> getDailyReportsForClientAndDate(int clientId, Date date) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReport> retVal = new ArrayList<OfficerDailyReport>();

        daily_reports_by_client_id_and_date_query dailyReportQuery = new daily_reports_by_client_id_and_date_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{clientId, date});

        HashMap<Integer, Employee> officerHash = new HashMap<Integer, Employee>();

        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                OfficerDailyReport dailyReport = new OfficerDailyReport(rst);
                if (officerHash.get(dailyReport.getEmployee_id()) == null) {
                    dailyReport.getOfficer(companyId);
                    officerHash.put(dailyReport.getEmployee_id(), dailyReport.getOfficer());
                } else {
                    dailyReport.setOfficer(officerHash.get(dailyReport.getEmployee_id()));
                }
                dailyReport.getOfficer(companyId);
                dailyReport.getReportTexts(companyId);
                retVal.add(dailyReport);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<OfficerDailyReport> getDailyReportsForEmployee(int employeeId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReport> retVal = new ArrayList<OfficerDailyReport>();

        daily_reports_by_employee_id_query dailyReportQuery = new daily_reports_by_employee_id_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{employeeId});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new OfficerDailyReport(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    private OfficerDailyReport getLastODR(Integer employeeId, Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        OfficerDailyReport retVal = null;

        get_last_reports_by_user_id_client_id_query dailyReportQuery = new get_last_reports_by_user_id_client_id_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{clientId, employeeId});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new OfficerDailyReport(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        
        
        return retVal;
    }
    
    public ArrayList<OfficerDailyReport> getOpenDailyReportByEmployeeClientId(Integer employeeId, Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReport> retVal = new ArrayList<OfficerDailyReport>();

        daily_reports_by_user_id_client_id_query dailyReportQuery = new daily_reports_by_user_id_client_id_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{clientId, employeeId});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new OfficerDailyReport(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        try {
            if (retVal.size() == 0) {
                OfficerDailyReport report = this.getLastODR(employeeId, clientId);
                if (report != null) {
                    retVal.add(report);
                }
            }
        } catch (Exception exe) {}
        
        return retVal;
    }
    
    public OfficerDailyReport getDailyReportById(int officerDailyReportId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        OfficerDailyReport retVal = new OfficerDailyReport();

        daily_reports_by_id_query dailyReportQuery = new daily_reports_by_id_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{officerDailyReportId});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new OfficerDailyReport(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<OfficerDailyReportText> getTextForReport(int officerDailyReportId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReportText> retVal = new ArrayList<OfficerDailyReportText>();

        daily_report_texts_by_report_id_query dailyReportQuery = new daily_report_texts_by_report_id_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{officerDailyReportId});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new OfficerDailyReportText(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public OfficerDailyReportDocument getDocumentForReport(int officerDailyReportDocumentId, boolean loadFullSizedImages) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        OfficerDailyReportDocument retVal = new OfficerDailyReportDocument();

        daily_report_documents_by_doc_id_query dailyReportQuery = new daily_report_documents_by_doc_id_query(loadFullSizedImages);
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{officerDailyReportDocumentId});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new OfficerDailyReportDocument(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<OfficerDailyReportDocument> getDocumentsForReport(int officerDailyReportId, boolean loadFullSizedImages) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReportDocument> retVal = new ArrayList<OfficerDailyReportDocument>();

        daily_report_documents_by_report_id_query dailyReportQuery = new daily_report_documents_by_report_id_query(loadFullSizedImages);
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{officerDailyReportId});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new OfficerDailyReportDocument(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public OfficerDailyReportText getTextById(int officerDailyReportTextId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        OfficerDailyReportText retVal = new OfficerDailyReportText();

        daily_report_texts_by_id_query dailyReportQuery = new daily_report_texts_by_id_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{officerDailyReportTextId});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new OfficerDailyReportText(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public int saveDailyReport(OfficerDailyReport officerDailyReport) throws SaveDataException, RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        boolean isNewReport = false;
        if (officerDailyReport.getOfficerDailyReportId() == null || officerDailyReport.getOfficerDailyReportId() == 0) {
            isNewReport = true;
            get_daily_reports_next_id_query sequenceQuery = new get_daily_reports_next_id_query();
            sequenceQuery.setPreparedStatement(new Object[]{});
            sequenceQuery.setCompany(companyId);
            try {
                Record_Set rst = conn.executeQuery(sequenceQuery, "");
                officerDailyReport.setOfficerDailyReportId(rst.getInt(0));
            } catch (Exception e) {
                throw new RetrieveDataException();
            }
        }

        save_daily_report_query saveDailyReport = new save_daily_report_query();
        saveDailyReport.update(officerDailyReport, isNewReport);
        saveDailyReport.setCompany(companyId);
        try {
            conn.executeUpdate(saveDailyReport, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }

        return officerDailyReport.getOfficerDailyReportId();
    }

    public int saveDailyReportText(OfficerDailyReportText officerDailyReportText) throws SaveDataException, RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        boolean isNewReport = false;
        if (officerDailyReportText.getOfficerDailyReportTextId() == null || officerDailyReportText.getOfficerDailyReportTextId() == 0) {
            isNewReport = true;
            get_daily_report_texts_next_id_query sequenceQuery = new get_daily_report_texts_next_id_query();
            sequenceQuery.setPreparedStatement(new Object[]{});
            sequenceQuery.setCompany(companyId);
            try {
                Record_Set rst = conn.executeQuery(sequenceQuery, "");
                officerDailyReportText.setOfficerDailyReportTextId(rst.getInt(0));
            } catch (Exception e) {
                throw new RetrieveDataException();
            }
        }

        save_daily_report_text_query saveDailyReport = new save_daily_report_text_query();
        saveDailyReport.update(officerDailyReportText, isNewReport);
        saveDailyReport.setCompany(companyId);
        try {
            conn.executeUpdate(saveDailyReport, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }

        return officerDailyReportText.getOfficerDailyReportTextId();
    }

    public ArrayList<OfficerDailyReportText> getTextForClientAndNumberDates(int clientId, int numberOfDays) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReportText> retVal = new ArrayList<OfficerDailyReportText>();

        daily_report_texts_by_client_id_query dailyReportQuery = new daily_report_texts_by_client_id_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{clientId, numberOfDays});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new OfficerDailyReportText(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public int saveDailyReportDocument(OfficerDailyReportDocument document) throws SaveDataException, RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        boolean isNewReport = false;
        if (document.getOfficerDailyReportDocumentId() == null || document.getOfficerDailyReportDocumentId() == 0) {
            isNewReport = true;
            get_daily_report_documents_next_id_query sequenceQuery = new get_daily_report_documents_next_id_query();
            sequenceQuery.setPreparedStatement(new Object[]{});
            sequenceQuery.setCompany(companyId);
            try {
                Record_Set rst = conn.executeQuery(sequenceQuery, "");
                document.setOfficerDailyReportDocumentId(rst.getInt(0));
            } catch (Exception e) {
                throw new RetrieveDataException();
            }
        }

        save_daily_report_document_query saveDailyReport = new save_daily_report_document_query();
        saveDailyReport.update(document, isNewReport);
        saveDailyReport.setCompany(companyId);
        try {
            conn.executeUpdate(saveDailyReport, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }

        return document.getOfficerDailyReportDocumentId();
    }

    public OfficerDailyReport getODRByShiftIdAndDate(int shiftId, Date date) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        OfficerDailyReport retVal = new OfficerDailyReport();

        get_daily_report_by_shift_id_query dailyReportQuery = new get_daily_report_by_shift_id_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{shiftId});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new OfficerDailyReport(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public OfficerDailyReport getODRByEmployeeIdAndDate(int employeeId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        OfficerDailyReport retVal = new OfficerDailyReport();

        get_daily_report_by_employee_id_query dailyReportQuery = new get_daily_report_by_employee_id_query();
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(new Object[]{employeeId});
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new OfficerDailyReport(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public void closeOutAnyOutstandingODRs(Integer clientId, Integer userId) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        close_out_any_oustanding_odrs_query saveDailyReport = new close_out_any_oustanding_odrs_query();
        saveDailyReport.setPreparedStatement(new Object[]{userId, clientId});
        saveDailyReport.setCompany(companyId);
        try {
            conn.executeUpdate(saveDailyReport, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public ArrayList<OfficerDailyReport> getDailyReportsForClientsWithText(ArrayList<Integer> clientIds, int numberOfDays, boolean returnWithTextOnly) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<OfficerDailyReport> retVal = new ArrayList<OfficerDailyReport>();

        daily_reports_by_client_ids_query dailyReportQuery = new daily_reports_by_client_ids_query(!returnWithTextOnly, clientIds);
        dailyReportQuery.setCompany(companyId);
        dailyReportQuery.setPreparedStatement(clientIds.toArray());

        HashMap<Integer, OfficerDailyReport> odrs = new HashMap<Integer, OfficerDailyReport>();
        try {
            Record_Set rst = conn.executeQuery(dailyReportQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                if (odrs.get(rst.getInt("officer_daily_report_id")) == null) {
                    OfficerDailyReport dailyReport = new OfficerDailyReport(rst);
                    odrs.put(dailyReport.getOfficerDailyReportId(), dailyReport);
                }
                
                OfficerDailyReport odr = odrs.get(rst.getInt("officer_daily_report_id"));
                if (rst.getInt("officer_daily_report_text_id") > -1) {
                    if (odr.getReportTexts() == null) {
                        odr.setReportTexts(new ArrayList<OfficerDailyReportText>());
                    }
                    odr.getReportTexts().add(new OfficerDailyReportText(rst));
                }
                odr.setOfficer(new Employee(new Date(), rst));
                rst.moveNext();
            }
            
            Iterator<OfficerDailyReport> odrKeys = odrs.values().iterator();
            while (odrKeys.hasNext()) {
                retVal.add(odrKeys.next());
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
