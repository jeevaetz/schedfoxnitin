/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.mail.HtmlEmail;
import rmischeduleserver.IPLocationFile;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.incidents.*;
import rmischeduleserver.util.MailAuthenticator;
import schedfoxlib.controller.IncidentReportControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.CompanyObj;
import schedfoxlib.model.Employee;
import schedfoxlib.model.IncidentReport;
import schedfoxlib.model.IncidentReportContact;
import schedfoxlib.model.IncidentReportDocument;
import schedfoxlib.model.IncidentReportType;
import schedfoxlib.model.ManagementClient;
import schedfoxlib.model.User;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class IncidentReportController implements IncidentReportControllerInterface {

    private String companyId;

    public IncidentReportController(String companyId) {
        this.companyId = companyId;
    }

    public static void main(String args[]) {
        try {
            IncidentReportController incidentController = new IncidentReportController("5036");
            IncidentReport incident = incidentController.getIncidentReportById(35);
            incidentController.notifyAccountsOfIncident(incident);
            //ArrayList<IncidentReport> documentsData = new IncidentReportController("2").getIncidentReportsForClient(2857);

//            File myOutputFile = new File("C:\\imagetest.png");
//            myOutputFile.createNewFile();
//            FileOutputStream oStream = new FileOutputStream(myOutputFile);
//            oStream.write(documentsData.get(0).getDocumentData());
//            oStream.flush();
//            oStream.close();
//            for (int d = 0; d < documentsData.size(); d++) {
//            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public void notifyAccountsOfIncident(IncidentReport incidentReport) throws SaveDataException, RetrieveDataException {

        ClientController clientController = ClientController.getInstance(companyId);
        EmployeeController employeeController = EmployeeController.getInstance(companyId);
        Client clientToNotify = clientController.getClientById(incidentReport.getClientId());
        User userToNotify = clientController.getDMAssignedToClient(clientToNotify.getClientId());

        HashMap<String, String> emailsToNotify = new HashMap<String, String>();

        String email = "auto@champ.net";
        try {
            CompanyObj currComp = CompanyController.getInstance().getCompanyObjById(Integer.parseInt(companyId));
            ManagementClientController manageController = ManagementClientController.getInstance(companyId);
            ManagementClient managementCli = manageController.getManagementClientById(currComp.getCompanyManagementId());
            email = managementCli.getManagement_client_email();
        } catch (Exception exe) {
        }

        if (userToNotify != null && userToNotify.getUserId() != null) {
            emailsToNotify.put(userToNotify.getUserEmail(), userToNotify.getUserEmail());
        }

        ArrayList<ClientContact> contacts = clientToNotify.getContacts(companyId);
        for (int c = 0; c < contacts.size(); c++) {
            if (contacts.get(c).getClientContactEmailOnIncident()) {
                emailsToNotify.put(contacts.get(c).getClientContactEmail(), contacts.get(c).getClientContactEmail());
            }
        }

        Iterator<String> keys = emailsToNotify.keySet().iterator();
        while (keys.hasNext()) {
            String to = keys.next();
            
            StringBuilder emailMessage = new StringBuilder();
            emailMessage.append("A new incident has occurred at the location: ").append(clientToNotify.getName()).append("<br/>");
            emailMessage.append("<a href=\"http://schedfoximage.schedfox.com/PrintIndividualIncidentReportServlet?incidentReportId=");
            emailMessage.append(incidentReport.getIncidentReportId()).append("&companyId=").append(this.companyId).append("\">Click Here to View</a>");

            try {
                HtmlEmail htmlEmail = new HtmlEmail();
                htmlEmail.setHtmlMsg(emailMessage.toString());
                htmlEmail.setFrom(email);
                htmlEmail.setSubject("Incident Report - " + clientToNotify.getName());
                htmlEmail.addTo(to);
                htmlEmail.setAuthenticator(new MailAuthenticator(IPLocationFile.getEMAIL_USER(), IPLocationFile.getEMAIL_PASSWORD()));
                htmlEmail.setHostName(IPLocationFile.getEMAIL_HOST());
                htmlEmail.setSmtpPort(Integer.parseInt(IPLocationFile.getEMAIL_PORT()));
                htmlEmail.send();
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    public ArrayList<IncidentReportType> getIncidentReportTypes() throws RetrieveDataException {
        ArrayList<IncidentReportType> retVal = new ArrayList<IncidentReportType>();
        get_incident_report_type_query getIncidentQuery = new get_incident_report_type_query();
        getIncidentQuery.setPreparedStatement(new Object[]{});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new IncidentReportType(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public IncidentReport getIncidentReportById(int incident_id) throws RetrieveDataException {
        IncidentReport retVal = new IncidentReport();
        get_incident_by_id_query getIncidentQuery = new get_incident_by_id_query();
        getIncidentQuery.setPreparedStatement(new Object[]{incident_id});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new IncidentReport(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<IncidentReportContact> getIncidentReportContactsForIncident(int incident_id) throws RetrieveDataException {
        ArrayList<IncidentReportContact> retVal = new ArrayList<IncidentReportContact>();
        get_incident_contacts_query getIncidentQuery = new get_incident_contacts_query();
        getIncidentQuery.setPreparedStatement(new Object[]{incident_id});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new IncidentReportContact(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<IncidentReportDocument> getIncidentReportDocumentsForIncident(int incident_id, boolean loadFullImageData) throws RetrieveDataException {
        ArrayList<IncidentReportDocument> retVal = new ArrayList<IncidentReportDocument>();
        get_incident_documents_query getIncidentQuery = new get_incident_documents_query(loadFullImageData);
        getIncidentQuery.setPreparedStatement(new Object[]{incident_id});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new IncidentReportDocument(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public int saveIncidentReportDocument(IncidentReportDocument document) throws SaveDataException, RetrieveDataException {
        int retVal = 0;
        boolean isInsert = false;
        if (document.getIncidentReportDocumentId() == null || document.getIncidentReportDocumentId() == 0) {
            retVal = getNextIncidentReportSequence();
            document.setIncidentReportDocumentId(retVal);
            isInsert = true;
        }
        retVal = document.getIncidentReportDocumentId();
        save_incident_document_query saveIncidentDocumentQuery = new save_incident_document_query();
        saveIncidentDocumentQuery.update(document, isInsert);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        saveIncidentDocumentQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveIncidentDocumentQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
        return retVal;
    }
    
    @Override
    public ArrayList<IncidentReport> getIncidentReportsForClient(ArrayList<Integer> clientIds, Integer numberOfDays) throws RetrieveDataException {
        ArrayList<IncidentReport> retVal = new ArrayList<IncidentReport>();
        get_incidents_by_clients_and_days_query getIncidentQuery = new get_incidents_by_clients_and_days_query();
        getIncidentQuery.update(clientIds, numberOfDays);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                IncidentReport report = new IncidentReport(rst);
                report.setOfficer(new Employee(new Date(), rst));
                report.setIncidentType(new IncidentReportType(rst));
                retVal.add(report);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<IncidentReport> getIncidentReportsForClient(int clientId, int numberOfDays) throws RetrieveDataException {
        ArrayList<IncidentReport> retVal = new ArrayList<IncidentReport>();
        get_incidents_by_client_and_days_query getIncidentQuery = new get_incidents_by_client_and_days_query();
        getIncidentQuery.setPreparedStatement(new Object[]{clientId, numberOfDays});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                IncidentReport report = new IncidentReport(rst);
                report.getOfficer(companyId);
                report.getIncidentType(companyId);
                retVal.add(report);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<IncidentReport> getIncidentReportsForClient(int clientId) throws RetrieveDataException {
        ArrayList<IncidentReport> retVal = new ArrayList<IncidentReport>();
        get_incidents_by_client_query getIncidentQuery = new get_incidents_by_client_query();
        getIncidentQuery.setPreparedStatement(new Object[]{clientId});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                IncidentReport report = new IncidentReport(rst);
                report.getOfficer(companyId);
                report.getIncidentType(companyId);
                retVal.add(report);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<IncidentReport> getIncidentReportsForEmployee(int employeeId) throws RetrieveDataException {
        ArrayList<IncidentReport> retVal = new ArrayList<IncidentReport>();
        get_incidents_by_employee_query getIncidentQuery = new get_incidents_by_employee_query();
        getIncidentQuery.setPreparedStatement(new Object[]{employeeId});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new IncidentReport(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<IncidentReport> getIncidentReportsForDate(Date date) throws RetrieveDataException {
        ArrayList<IncidentReport> retVal = new ArrayList<IncidentReport>();
        get_incidents_by_date_query getIncidentQuery = new get_incidents_by_date_query();
        getIncidentQuery.setPreparedStatement(new Object[]{date});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new IncidentReport(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<IncidentReport> getIncidentReportsForDateAndClient(Date date, int clientId) throws RetrieveDataException {
        ArrayList<IncidentReport> retVal = new ArrayList<IncidentReport>();
        get_incidents_by_date_and_client_query getIncidentQuery = new get_incidents_by_date_and_client_query();
        getIncidentQuery.setPreparedStatement(new Object[]{date, clientId});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                IncidentReport incidentReport = new IncidentReport(rst);
                incidentReport.getOfficer(companyId);
                incidentReport.getIncidentType(companyId);
                retVal.add(incidentReport);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public int saveIncidentReport(IncidentReport incidentReport) throws SaveDataException, RetrieveDataException {
        save_incident_query saveIncidentQuery = new save_incident_query();
        boolean isInsert = false;
        int retVal = 0;
        if (incidentReport.getIncidentReportId() == null || incidentReport.getIncidentReportId() == 0) {
            retVal = getNextIncidentReportSequence();
            incidentReport.setIncidentReportId(retVal);
            isInsert = true;
        }
        retVal = incidentReport.getIncidentReportId();
        saveIncidentQuery.update(incidentReport, isInsert);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        saveIncidentQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveIncidentQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
        return retVal;
    }

    public void saveIncidentReportContact(IncidentReportContact reportContact) throws SaveDataException {
        save_incident_contact_query getIncidentQuery = new save_incident_contact_query();
        boolean isInsert = false;
        if (reportContact.getIncidentReportContactId() == 0) {
            isInsert = true;
        }
        getIncidentQuery.update(reportContact, isInsert);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            conn.executeUpdate(getIncidentQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    public int getNextIncidentReportSequence() throws RetrieveDataException {
        int retVal = 0;
        get_next_incident_id_query getIdQuery = new get_next_incident_id_query();
        getIdQuery.setPreparedStatement(new Object[]{});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIdQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIdQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = rst.getInt("id");
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public int getNextIncidentDocumentReportSequence() throws RetrieveDataException {
        int retVal = 0;
        get_next_incident_document_id_query getIdQuery = new get_next_incident_document_id_query();
        getIdQuery.setPreparedStatement(new Object[]{});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIdQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIdQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = rst.getInt("id");
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public IncidentReportType getIncidentReportTypeById(int incidentTypeId) throws RetrieveDataException {
        IncidentReportType retVal = new IncidentReportType();
        get_incident_type_by_id_query getIdQuery = new get_incident_type_by_id_query();
        getIdQuery.setPreparedStatement(new Object[]{incidentTypeId});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIdQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIdQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new IncidentReportType(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public HashMap<String, Integer> getNumberOfIncidentsOverLastXDays(int clientId, int numberOfDays) throws RetrieveDataException {
        HashMap<String, Integer> retVal = new HashMap<String, Integer>();
        get_number_of_incidents_over_past_days_query xDayQuery = new get_number_of_incidents_over_past_days_query();
        xDayQuery.setPreparedStatement(new Object[]{numberOfDays, clientId});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        xDayQuery.setCompany(companyId);
        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
            Record_Set rst = conn.executeQuery(xDayQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                Integer num = 0;
                try {
                    num = rst.getInt("num");
                } catch (Exception exe) {
                }
                retVal.put(myFormat.format(rst.getDate("doy")), num);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<IncidentReport> getIncidentReportsForDatesAndClient(Date startDate, Date endDate, int clientId) throws RetrieveDataException {
        ArrayList<IncidentReport> retVal = new ArrayList<IncidentReport>();
        get_incidents_by_dates_and_client_query getIncidentQuery = new get_incidents_by_dates_and_client_query();
        getIncidentQuery.setPreparedStatement(new Object[]{startDate, endDate, clientId});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                IncidentReport incidentReport = new IncidentReport(rst);
                incidentReport.getOfficer(companyId);
                retVal.add(incidentReport);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public byte[] fetchImageDataForDocument(int incident_report_document_id) throws RetrieveDataException {
        byte[] retVal = null;
        get_incident_document_by_id_query getIncidentQuery = new get_incident_document_by_id_query();
        getIncidentQuery.setPreparedStatement(new Object[]{incident_report_document_id});
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

    public IncidentReportDocument getIncidentReportDocumentById(int incident_document_id) throws RetrieveDataException {
        IncidentReportDocument retVal = new IncidentReportDocument();
        get_incident_documents_by_id_query getIncidentQuery = new get_incident_documents_by_id_query();
        getIncidentQuery.setPreparedStatement(new Object[]{incident_document_id});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getIncidentQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getIncidentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new IncidentReportDocument(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public void saveIncidentReportType(IncidentReportType incidentType) throws SaveDataException {
        save_incident_report_type_query saveIncidentDocumentQuery = new save_incident_report_type_query();
        saveIncidentDocumentQuery.update(incidentType);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        saveIncidentDocumentQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveIncidentDocumentQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
}
