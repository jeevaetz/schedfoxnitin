/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.IncidentReport;
import schedfoxlib.model.IncidentReportContact;
import schedfoxlib.model.IncidentReportDocument;
import schedfoxlib.model.IncidentReportType;

/**
 *
 * @author user
 */
public interface IncidentReportControllerInterface {
    public ArrayList<IncidentReport> getIncidentReportsForClient(ArrayList<Integer> clientIds, Integer numberOfDays) throws RetrieveDataException;
    public HashMap<String, Integer> getNumberOfIncidentsOverLastXDays(int clientId, int numberOfDays) throws RetrieveDataException;
    public void notifyAccountsOfIncident(IncidentReport incidentReport) throws SaveDataException, RetrieveDataException;
    public int getNextIncidentReportSequence() throws RetrieveDataException;
    public IncidentReport getIncidentReportById(int incident_id) throws RetrieveDataException;
    public IncidentReportDocument getIncidentReportDocumentById(int incident_document_id) throws RetrieveDataException;
    public ArrayList<IncidentReportDocument> getIncidentReportDocumentsForIncident(int incident_id, boolean loadFullImageData) throws RetrieveDataException;
    public byte[] fetchImageDataForDocument(int incident_report_document_id) throws RetrieveDataException;
    public int saveIncidentReportDocument(IncidentReportDocument document) throws SaveDataException, RetrieveDataException;
    public ArrayList<IncidentReport> getIncidentReportsForClient(int clientId) throws RetrieveDataException;
    public ArrayList<IncidentReport> getIncidentReportsForClient(int clientId, int numberOfDays) throws RetrieveDataException;
    public ArrayList<IncidentReport> getIncidentReportsForEmployee(int employeeId) throws RetrieveDataException;
    public ArrayList<IncidentReport> getIncidentReportsForDate(Date date) throws RetrieveDataException;
    public ArrayList<IncidentReport> getIncidentReportsForDateAndClient(Date date, int clientId) throws RetrieveDataException;
    public ArrayList<IncidentReport> getIncidentReportsForDatesAndClient(Date startDate, Date endDate, int clientId) throws RetrieveDataException;
    public int saveIncidentReport(IncidentReport incidentReport) throws SaveDataException, RetrieveDataException;
    public ArrayList<IncidentReportContact> getIncidentReportContactsForIncident(int incident_id) throws RetrieveDataException;
    public void saveIncidentReportContact(IncidentReportContact reportContact) throws SaveDataException;
    public void saveIncidentReportType(IncidentReportType incidentType) throws SaveDataException;
    public ArrayList<IncidentReportType> getIncidentReportTypes() throws RetrieveDataException;
    public IncidentReportType getIncidentReportTypeById(int incidentTypeId) throws RetrieveDataException;
}
