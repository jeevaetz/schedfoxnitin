package schedfoxlib.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.imageio.ImageIO;

import schedfoxlib.controller.IncidentReportControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.*;

public class IncidentService implements IncidentReportControllerInterface {

    private static String location = "IncidentReport/";
    private String companyId;

    public IncidentService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public IncidentService(String companyId) {
        this.companyId = companyId;
    }

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
        IncidentService cliService = new IncidentService("5061");
        try {
            SchedfoxLibServiceVariables.init();

            for (int i = 1; i < 9; i++) {
                String file = "C:\\work\\secprime\\" + i + ".jpeg";

                File imageInput = new File(file);
                byte[] imageBuffer = new byte[2048];
                BufferedInputStream iReader = new BufferedInputStream(new FileInputStream(imageInput));
                ByteArrayOutputStream oStream = new ByteArrayOutputStream();
                int numRead = 0;
                while ((numRead = (iReader.read(imageBuffer))) > -1) {
                    oStream.write(imageBuffer, 0, numRead);
                }
                iReader.close();
                byte[] imageArray = oStream.toByteArray();

                ByteArrayOutputStream thumbStream = new ByteArrayOutputStream();
                BufferedImage image = ImageIO.read(imageInput);
                BufferedImage scaledImage = toBufferedImage(image.getScaledInstance(150, -1, Image.SCALE_SMOOTH));
                ImageIO.write(scaledImage, "png", thumbStream);

                IncidentReportDocument incident = new IncidentReportDocument();
                incident.setIncidentReportId(627);
                incident.setEmployeeId(15);
                incident.setDocumentData(imageArray);
                incident.setThumbnailData(thumbStream.toByteArray());
                incident.setMimeType("image/png");

                cliService.saveIncidentReportDocument(incident);
            }
            System.out.println("Here");
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    @Override
    public ArrayList<IncidentReport> getIncidentReportsForClient(int client_id)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "incidentreportsbyclient/" + client_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<IncidentReport>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<IncidentReport> getIncidentReportsForDate(Date date)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "incidentreportsbydate/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Representation rep = cr.post(new JsonRepresentation(gson.toJson(date)));
            Type collectionType = new TypeToken<Collection<IncidentReport>>() {
            }.getType();
            return gson.fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<IncidentReport> getIncidentReportsForDateAndClient(
            Date dateOfIncident, int clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "incidentreportsbydateandclient/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Representation rep = cr.post(new JsonRepresentation(gson.toJson(dateOfIncident)));
            Type collectionType = new TypeToken<Collection<IncidentReport>>() {
            }.getType();
            return gson.fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<IncidentReport> getIncidentReportsForEmployee(int employeeId)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "incidentreportsbyemployee/" + employeeId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<IncidentReport>>() {
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
    public int saveIncidentReport(IncidentReport incidentReport)
            throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveincidentreport/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            String json = cr.post(new JsonRepresentation(gson.toJson(incidentReport))).getText();
            return gson.fromJson(json, Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public IncidentReport getIncidentReportById(int incident_report_id)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "incidentreportbyid/" + incident_report_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), IncidentReport.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<IncidentReportDocument> getIncidentReportDocumentsForIncident(
            int incident_report_id, boolean loadImageData) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "incidentreportdocumentsbyid/" + incident_report_id + "/" + loadImageData);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);

        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<IncidentReportDocument>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.get().getText();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public int saveIncidentReportDocument(IncidentReportDocument document)
            throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveincidentreportdocument/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            ObjectMapper myMapper = new ObjectMapper();
            myMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

            Gson gson = SchedfoxLibServiceVariables.getGson();

            Object obj = new JsonRepresentation(myMapper.writeValueAsString(document));
            String json = cr.post(obj).getText();
            return gson.fromJson(json, Integer.class);
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
        return 0;
    }

    @Override
    public int getNextIncidentReportSequence() throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getnextid/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Integer.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<IncidentReportContact> getIncidentReportContactsForIncident(
            int incident_id) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getincidentreportcontacts/" + incident_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<IncidentReportContact>>() {
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
    public void saveIncidentReportContact(IncidentReportContact reportContact)
            throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savereportcontact/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(reportContact)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<IncidentReportType> getIncidentReportTypes() throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getincidentreporttypes/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<IncidentReportType>>() {
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
    public IncidentReportType getIncidentReportTypeById(int incidentTypeId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getincidenttypebyid/" + incidentTypeId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), IncidentReportType.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public HashMap<String, Integer> getNumberOfIncidentsOverLastXDays(int clientId, int numberOfDays) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getincidentnumbersforxdays/" + clientId + "/" + numberOfDays);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<HashMap<String, Integer>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<IncidentReport> getIncidentReportsForDatesAndClient(Date startDate, Date endDate, int clientId) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            ClientResource cr = new ClientResource(getLocation() + "incidentreportsbydatesandclient/" + clientId + "/"
                    + URLEncoder.encode(myFormat.format(startDate), "UTF-8") + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            try {
                cr.setNext(SchedfoxLibServiceVariables.getClient());
                Gson gson = SchedfoxLibServiceVariables.getGson();
                Representation rep = cr.post(new JsonRepresentation(gson.toJson(startDate)));
                Type collectionType = new TypeToken<Collection<IncidentReport>>() {
                }.getType();
                return gson.fromJson(rep.getText(), collectionType);
            } catch (Exception exe) {
                throw new RetrieveDataException();
            } finally {
                cr.getResponse().release();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public byte[] fetchImageDataForDocument(int incident_report_document_id) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "fetchimagedata/" + incident_report_document_id);
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
    public IncidentReportDocument getIncidentReportDocumentById(int incident_document_id) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getincidentreportdocbyid/" + incident_document_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), IncidentReportDocument.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<IncidentReport> getIncidentReportsForClient(int clientId, int numberOfDays) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "incidentreportsbyclientanddate/" + clientId + "/" + numberOfDays);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<IncidentReport>>() {
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
    public void notifyAccountsOfIncident(IncidentReport incidentReport) throws SaveDataException, RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveIncidentReportType(IncidentReportType incidentType) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveincidentreporttype/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(incidentType)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<IncidentReport> getIncidentReportsForClient(ArrayList<Integer> clientIds, Integer numberOfDays) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getincidentreportsformclients/" + numberOfDays);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<IncidentReport>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Reader reader = cr.post(new JsonRepresentation(gson.toJson(clientIds))).getReader();
            return gson.fromJson(reader, collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
}
