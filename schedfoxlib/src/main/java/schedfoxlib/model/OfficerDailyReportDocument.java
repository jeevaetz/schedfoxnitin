/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.math.BigDecimal;
import java.util.Date;
import schedfoxlib.controller.OfficerDailyReportControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class OfficerDailyReportDocument implements ImageInterface {

    private Integer officerDailyReportDocumentId;
    private Integer officerDailyReportTextId;
    private byte[] documentData;
    private byte[] thumbnailData;
    private String mimeType;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Date timeEntered;
    

    public OfficerDailyReportDocument() {
    }

    public OfficerDailyReportDocument(Record_Set rst) {
        try {
            officerDailyReportDocumentId = rst.getInt("officer_daily_report_document_id");
        } catch (Exception exe) {
        }
        try {
            officerDailyReportTextId = rst.getInt("officer_daily_report_text_id");
        } catch (Exception exe) {
        }
        try {
            documentData = rst.getByteArray("document_data");
        } catch (Exception exe) {
        }
        try {
            this.thumbnailData = rst.getByteArray("thumbnail_data");
        } catch (Exception exe) {}
        try {
            mimeType = rst.getString("mime_type");
        } catch (Exception exe) {
        }
        try {
            timeEntered = rst.getTimestamp("time_entered");
        } catch (Exception exe) {
        }
    }

    /**
     * @return the officerDailyReportDocumentId
     */
    public Integer getOfficerDailyReportDocumentId() {
        return officerDailyReportDocumentId;
    }

    /**
     * @param officerDailyReportDocumentId the officerDailyReportDocumentId to set
     */
    public void setOfficerDailyReportDocumentId(Integer officerDailyReportDocumentId) {
        this.officerDailyReportDocumentId = officerDailyReportDocumentId;
    }

    /**
     * @return the officerDailyReportId
     */
    public Integer getOfficerDailyReportTextId() {
        return officerDailyReportTextId;
    }

    /**
     * @param officerDailyReportId the officerDailyReportId to set
     */
    public void setOfficerDailyReportTextId(Integer officerDailyReportTextId) {
        this.officerDailyReportTextId = officerDailyReportTextId;
    }

    /**
     * @return the documentData
     */
    @Override
    public byte[] getDocumentData() {
        return documentData;
    }
    
    public byte[] getDocumentData(String companyId) {
        if (documentData == null) {
            OfficerDailyReportControllerInterface controller = ControllerRegistryAbstract.getOfficerDailyReportController(companyId);
            try {
                OfficerDailyReportDocument doc = controller.getDocumentForReport(this.officerDailyReportDocumentId, true);
                this.documentData = doc.getDocumentData();
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

    /**
     * @return the timeEntered
     */
    public Date getTimeEntered() {
        return timeEntered;
    }

    /**
     * @param timeEntered the timeEntered to set
     */
    public void setTimeEntered(Date timeEntered) {
        this.timeEntered = timeEntered;
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

    @Override
    public byte[] getThumbnailData() {
        return thumbnailData;
    }

    @Override
    public void setThumbnailData(byte[] imageData) {
        this.thumbnailData = imageData;
    }

    

}
