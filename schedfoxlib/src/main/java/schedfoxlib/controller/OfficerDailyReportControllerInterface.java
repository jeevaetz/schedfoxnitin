/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.OfficerDailyReport;
import schedfoxlib.model.OfficerDailyReportDocument;
import schedfoxlib.model.OfficerDailyReportText;
import schedfoxlib.model.WrapperEmployeeClientSelections;

/**
 *
 * @author user
 */
public interface OfficerDailyReportControllerInterface {
    
    public ArrayList<OfficerDailyReport> getDailyReportsForEmployeeAndClientsWithText(WrapperEmployeeClientSelections employeeClientSel, Integer branchId, Date startDate, Date endDate) throws RetrieveDataException;
    public void closeOutAnyOutstandingODRs(Integer clientId, Integer userId) throws SaveDataException;
    public ArrayList<OfficerDailyReport> getDailyReportsForClient(int clientId) throws RetrieveDataException;
    public ArrayList<OfficerDailyReport> getDailyReportsForClientsWithText(ArrayList<Integer> clientIds, int numberOfDays, boolean returnWithTextOnly) throws RetrieveDataException;
    public ArrayList<OfficerDailyReport> getDailyReportsForEmployeeAndClientWithText(int clientId, int employeeId, Date startDate, Date endDate) throws RetrieveDataException;
    public ArrayList<OfficerDailyReport> getDailyReportsForDates(String startDate, String endDate, Boolean showOnlyOnesWithText) throws RetrieveDataException;
    public ArrayList<OfficerDailyReport> getDailyReportsForClientAndDate(int clientId, Date date) throws RetrieveDataException;
    public ArrayList<OfficerDailyReport> getDailyReportsForEmployee(int employeeId) throws RetrieveDataException;
    public OfficerDailyReport getODRByShiftIdAndDate(int shiftId, Date date) throws RetrieveDataException;
    public OfficerDailyReport getODRByEmployeeIdAndDate(int employeeId) throws RetrieveDataException;
    public OfficerDailyReport getDailyReportById(int officerDailyReportId) throws RetrieveDataException;
    public OfficerDailyReportDocument getDocumentForReport(int officerDailyReportDocumentId, boolean loadFullSizedImages) throws RetrieveDataException;
    public ArrayList<OfficerDailyReportText> getTextForReport(int officerDailyReportId) throws RetrieveDataException;
    public ArrayList<OfficerDailyReport> getDailyReportsForClientWithText(int clientId, int numberOfDays, boolean returnWithTextOnly) throws RetrieveDataException;
    public ArrayList<OfficerDailyReportDocument> getDocumentsForReport(int officerDailyReportId, boolean loadFullSizedImages) throws RetrieveDataException;
    public ArrayList<OfficerDailyReportText> getTextForClientAndNumberDates(int clientId, int numberOfDays) throws RetrieveDataException;
    public OfficerDailyReportText getTextById(int officerDailyReportTextId) throws RetrieveDataException;
    public byte[] fetchImageDataForDocument(int officer_report_document_id) throws RetrieveDataException;
    public void checkIfShouldNofifyAndDoSo(OfficerDailyReport officerDailyReport) throws SaveDataException, RetrieveDataException;
    public ArrayList<OfficerDailyReport> getOpenDailyReportByEmployeeClientId(Integer employeeId, Integer clientId) throws RetrieveDataException;
    
    public int saveDailyReport(OfficerDailyReport officerDailyReport) throws SaveDataException, RetrieveDataException;
    public int saveDailyReportDocument(OfficerDailyReportDocument document) throws SaveDataException, RetrieveDataException;
    public int saveDailyReportText(OfficerDailyReportText officerDailyReportText) throws SaveDataException, RetrieveDataException;
}
