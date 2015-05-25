/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.OfficerDailyReportControllerInterface;
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
public class OfficerDailyReportService implements OfficerDailyReportControllerInterface {

    private static String location = "OfficerDailyReport/";
    private String companyId;

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

    public static void main(String args[]) {
        OfficerDailyReportService officerDailyReport = new OfficerDailyReportService("2");

        try {
            OfficerDailyReport report = new OfficerDailyReport();
            report.setClientId(6450);
            report.setEmployee_id(16731);
            //report.setShiftId(1791941);
            report.setClientEquipmentId(267);
            report.setLoggedIn(new Date());
            officerDailyReport.saveDailyReport(report);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public OfficerDailyReportService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public OfficerDailyReportService(String companyId) {
        this.companyId = companyId;
    }

    public String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    @Override
    public ArrayList<OfficerDailyReport> getDailyReportsForClient(int clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "incidentreportsbyclient/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReport>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<OfficerDailyReport> getDailyReportsForClientAndDate(int clientId, Date date) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "incidentreportsbyclientanddate/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReport>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Representation rep = cr.post(new JsonRepresentation(gson.toJson(date)));
            return gson.fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<OfficerDailyReport> getDailyReportsForEmployee(int employeeId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "incidentreportsbyemloyee/" + employeeId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReport>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public OfficerDailyReport getDailyReportById(int officerDailyReportId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "incidentreportsbyid/" + officerDailyReportId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), OfficerDailyReport.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<OfficerDailyReportText> getTextForReport(int officerDailyReportId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "gettextforreport/" + officerDailyReportId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReportText>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public OfficerDailyReportText getTextById(int officerDailyReportTextId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "gettextbyid/" + officerDailyReportTextId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), OfficerDailyReportText.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public int saveDailyReport(OfficerDailyReport officerDailyReport) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "savedailyreport");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            JsonRepresentation json = new JsonRepresentation(gson.toJson(officerDailyReport));
            Representation rep = cr.post(json);
            return gson.fromJson(rep.getText(), Integer.class);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public int saveDailyReportText(OfficerDailyReportText officerDailyReportText) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savedailyreporttext/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            officerDailyReportText.setDocuments(null);
            JsonRepresentation json = new JsonRepresentation(gson.toJson(officerDailyReportText));
            Representation rep = cr.post(json);
            return gson.fromJson(rep.getText(), Integer.class);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<OfficerDailyReportText> getTextForClientAndNumberDates(int clientId, int numberOfDays) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "gettextforclientanddates/" + clientId + "/" + numberOfDays);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReportText>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<OfficerDailyReportDocument> getDocumentsForReport(int officerDailyReportId, boolean loadImages) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getdocumentsforreportid/" + officerDailyReportId + "/" + loadImages);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReportDocument>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public int saveDailyReportDocument(OfficerDailyReportDocument document) throws SaveDataException, RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savedailyreportdocument/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            ObjectMapper myMapper = new ObjectMapper();
            myMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

            Gson gson = SchedfoxLibServiceVariables.getGson();

            Object obj = new JsonRepresentation(myMapper.writeValueAsString(document));
            String json = cr.post(obj).getText();

            return gson.fromJson(json, Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public byte[] fetchImageDataForDocument(int officer_report_document_id) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getdocumentsforreportid/" + officer_report_document_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), byte[].class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public OfficerDailyReportDocument getDocumentForReport(int officerDailyReportDocumentId, boolean loadFullSizedImages) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getdocumentforreportid/" + officerDailyReportDocumentId + "/" + loadFullSizedImages);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), OfficerDailyReportDocument.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<OfficerDailyReport> getDailyReportsForClientWithText(int clientId, int numberOfDays, boolean onlyWithText) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getdailytextforclientwithtext/" + clientId + "/" + numberOfDays + "/" + onlyWithText);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReport>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public OfficerDailyReport getODRByShiftIdAndDate(int shiftId, Date date) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            ClientResource cr = new ClientResource(getLocation() + "getodrbyshiftidanddate/" + shiftId + "/" + URLEncoder.encode(myFormat.format(date), "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            try {
                cr.setNext(SchedfoxLibServiceVariables.getClient());
                Gson gson = SchedfoxLibServiceVariables.getGson();
                return gson.fromJson(cr.get().getReader(), OfficerDailyReport.class);
            } catch (Exception exe) {
                throw new RetrieveDataException();
            } finally {
                cr.getResponse().release();
            }
        } catch (UnsupportedEncodingException ue) {
            ue.printStackTrace();
            throw new RetrieveDataException();
        }
    }

    @Override
    public void checkIfShouldNofifyAndDoSo(OfficerDailyReport officerDailyReport) throws SaveDataException, RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "checkifshouldnotifyanddoso/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Representation rep = cr.post(new JsonRepresentation(gson.toJson(officerDailyReport)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public OfficerDailyReport getODRByEmployeeIdAndDate(int employeeId) throws RetrieveDataException {
        try {
            ClientResource cr = new ClientResource(getLocation() + "getodrbyemployeeidanddate/" + employeeId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            try {
                cr.setNext(SchedfoxLibServiceVariables.getClient());
                Gson gson = SchedfoxLibServiceVariables.getGson();
                return gson.fromJson(cr.get().getReader(), OfficerDailyReport.class);
            } catch (Exception exe) {
                throw new RetrieveDataException();
            } finally {
                cr.getResponse().release();
            }
        } catch (Exception ue) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public ArrayList<OfficerDailyReport> getDailyReportsForDates(String startDate, String endDate, Boolean showOnlyOnesWithText) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(getLocation() + "getdailyreportsfordates/"
                    + startDate + "/" + endDate + "/" + showOnlyOnesWithText);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReport>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {
            }
        }
    }

    @Override
    public void closeOutAnyOutstandingODRs(Integer clientId, Integer userId) throws SaveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(getLocation() + "closeoutanyoustandingodrs/"
                    + clientId + "/" + userId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            cr.get();
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {
            }
        }
    }

    @Override
    public ArrayList<OfficerDailyReport> getOpenDailyReportByEmployeeClientId(Integer employeeId, Integer clientId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(getLocation() + "getopendailyreportsbyemployeeclientid/"
                    + employeeId + "/" + clientId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReport>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {
            }
        }
    }

    @Override
    public ArrayList<OfficerDailyReport> getDailyReportsForEmployeeAndClientWithText(int clientId, int employeeId, Date startDate, Date endDate) throws RetrieveDataException {
        ClientResource cr = null;
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            cr = new ClientResource(getLocation() + "getdailyreportsforemployeeandclientwithtext/"
                    + clientId + "/" + employeeId + "/" + URLEncoder.encode(myFormat.format(startDate), "UTF-8")
                    + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReport>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {
            }
        }
    }

    @Override
    public ArrayList<OfficerDailyReport> getDailyReportsForClientsWithText(ArrayList<Integer> clientIds, int numberOfDays, boolean returnWithTextOnly) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(getLocation() + "getdailyreportsforclientswithtext/"
                    + numberOfDays + "/" + returnWithTextOnly);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReport>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Representation rep = cr.post(new JsonRepresentation(gson.toJson(clientIds)));
            return gson.fromJson(rep.getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {
            }
        }
    }

    @Override
    public ArrayList<OfficerDailyReport> getDailyReportsForEmployeeAndClientsWithText(WrapperEmployeeClientSelections employeeClientSel, Integer branchId, Date startDate, Date endDate) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = null;
        try {
            cr = new ClientResource(getLocation() + "getdailyreportsforemployeeandclientswithtext/" + branchId + "/" +
                    URLEncoder.encode(myFormat.format(startDate), "UTF-8") + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<OfficerDailyReport>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Representation rep = cr.post(new JsonRepresentation(gson.toJson(employeeClientSel)));
            return gson.fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
}
