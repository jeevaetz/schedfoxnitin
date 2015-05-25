/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import schedfoxlib.controller.IncidentReportControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class IncidentReportDocument implements ImageInterface, Serializable, GPSInterface {
    private Integer incidentReportDocumentId;
    private Integer incidentReportId;
    private byte[] documentData;
    private byte[] thumbnailData;
    private String mimeType;
    private BigDecimal latitude;
    private BigDecimal longitude;

    //Lazy loaded objects
    private transient IncidentReport incidentReport;
    
    public IncidentReportDocument() {
        
    }
    
    public IncidentReportDocument(Record_Set rst) {
        try {
            this.incidentReportDocumentId = rst.getInt("incident_report_document_id");
        } catch (Exception exe) {}
        try {
            this.incidentReportId = rst.getInt("incident_report_id");
        } catch (Exception exe) {}
        try {
            this.documentData = rst.getByteArray("document_data");
        } catch (Exception exe) {}
        try {
            this.thumbnailData = rst.getByteArray("thumbnail_data");
        } catch (Exception exe) {}
        try {
            this.mimeType = rst.getString("mime_type");
        } catch (Exception exe) {}
        try {
            this.latitude = rst.getBigDecimal("latitude");
        } catch (Exception exe) {}
        try {
            this.longitude = rst.getBigDecimal("longitude");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the incidentReportDocumentId
     */
    public Integer getIncidentReportDocumentId() {
        return incidentReportDocumentId;
    }

    /**
     * @param incidentReportDocumentId the incidentReportDocumentId to set
     */
    public void setIncidentReportDocumentId(Integer incidentReportDocumentId) {
        this.incidentReportDocumentId = incidentReportDocumentId;
    }

    /**
     * @return the incidentReportId
     */
    public Integer getIncidentReportId() {
        return incidentReportId;
    }

    /**
     * @param incidentReportId the incidentReportId to set
     */
    public void setIncidentReportId(Integer incidentReportId) {
        this.incidentReportId = incidentReportId;
    }

    public void setMapURL(String mapUrl) {
        
    }
    
    @JsonIgnore
    public String getMapURL() {
        return "http://maps.googleapis.com/maps/api/staticmap?center=" + this.getLatitude() + 
                "," + this.getLongitude() + "&zoom=17&size=400x400&sensor=false&markers=color:blue%7Clabel:S%7C" + 
                this.getLatitude() + "," + this.getLongitude();
    }
    
    /**
     * @return the documentData
     */
    @Override
    public byte[] getDocumentData() {
        return documentData;
    }

    
    @Override
    public byte[] getDocumentData(String companyId) {
        if (documentData == null) {
            IncidentReportControllerInterface controller = ControllerRegistryAbstract.getIncidentReportInterface(companyId);
            try {
                IncidentReportDocument incidentDocument = controller.getIncidentReportDocumentById(this.incidentReportDocumentId);
                this.documentData = incidentDocument.getDocumentData();
            } catch (Exception exe) {}
        }
        return documentData;
    }
    
    /**
     * @param documentData the documentData to set
     */
    @Override
    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }

    /**
     * @return the mimeType
     */
    @Override
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType the mimeType to set
     */
    @Override
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    @JsonIgnore
    public IncidentReport getIncidentReport(String companyId) {
        if (this.incidentReport == null) {
            try {
                IncidentReportControllerInterface controller = ControllerRegistryAbstract.getIncidentReportInterface(companyId);
                this.incidentReport = controller.getIncidentReportById(this.incidentReportId);
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
        return this.incidentReport;
    }

    /**
     * @return the latitude
     */
    @Override
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    @Override
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    @Override
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    @Override
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the thumbnailData
     */
    @Override
    public byte[] getThumbnailData() {
        return thumbnailData;
    }

    /**
     * @param thumbnailData the thumbnailData to set
     */
    @Override
    public void setThumbnailData(byte[] thumbnailData) {
        this.thumbnailData = thumbnailData;
    }

    @Override
    public BigDecimal getAccuracy() {
        return new BigDecimal(0);
    }

    @Override
    public Integer getEmployeeId() {
        try {
            return incidentReport.getEmployeeId();
        } catch (Exception exe) {
            return 0;
        }
    }

    @Override
    public Date getRecordedOn() {
        try {
            return incidentReport.getDateEntered();
        } catch (Exception exe) {
            return new Date();
        }
    }

    @Override
    public void setAccuracy(BigDecimal accuracy) {
        
    }

    @Override
    public void setEmployeeId(Integer employeeId) {
        
    }

    @Override
    public void setRecordedOn(Date recordedOn) {
        
    }

    @JsonIgnore
    @Override
    public Integer getIdentifier() {
        return this.getIncidentReportDocumentId();
    }
    

    @JsonIgnore
    @Override
    public boolean isWaypointScan() {
        return false;
    }

}
