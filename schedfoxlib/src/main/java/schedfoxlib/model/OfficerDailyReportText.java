/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.OfficerDailyReportControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class OfficerDailyReportText extends OfficerCommunication implements Serializable {

    private Integer officerDailyReportTextId;
    private Integer officerDailyReportId;
    private String text;
    private Integer enteredBy;
    private Date enteredOn;
    private Integer imageCount;
    private ArrayList<OfficerDailyReportDocument> documents;

    private Integer docCount;

    public OfficerDailyReportText() {
    }

    public OfficerDailyReportText(Record_Set rst) {
        try {
            officerDailyReportTextId = rst.getInt("officer_daily_report_text_id");
        } catch (Exception exe) {
        }
        try {
            officerDailyReportId = rst.getInt("officer_daily_report_id");
        } catch (Exception exe) {
        }
        try {
            text = rst.getString("text");
        } catch (Exception exe) {
        }
        try {
            enteredBy = rst.getInt("entered_by");
        } catch (Exception exe) {
        }
        try {
            enteredOn = rst.getTimestamp("entered_on");
        } catch (Exception exe) {
        }
        try {
            imageCount = rst.getInt("piccount");
        } catch (Exception exe) {
        }
        try {
            docCount = rst.getInt("doc_count");
        } catch (Exception exe) {
        }
    }

    /**
     * @return the officerDailyReportTextId
     */
    public Integer getOfficerDailyReportTextId() {
        return officerDailyReportTextId;
    }

    /**
     * @param officerDailyReportTextId the officerDailyReportTextId to set
     */
    public void setOfficerDailyReportTextId(Integer officerDailyReportTextId) {
        this.officerDailyReportTextId = officerDailyReportTextId;
    }

    /**
     * @return the officerDailyReportId
     */
    public Integer getOfficerDailyReportId() {
        return officerDailyReportId;
    }

    /**
     * @param officerDailyReportId the officerDailyReportId to set
     */
    public void setOfficerDailyReportId(Integer officerDailyReportId) {
        this.officerDailyReportId = officerDailyReportId;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the enteredBy
     */
    public Integer getEnteredBy() {
        return enteredBy;
    }

    /**
     * @param enteredBy the enteredBy to set
     */
    public void setEnteredBy(Integer enteredBy) {
        this.enteredBy = enteredBy;
    }

    /**
     * @return the enteredOn
     */
    public Date getEnteredOn() {
        return enteredOn;
    }

    /**
     * @param enteredOn the enteredOn to set
     */
    public void setEnteredOn(Date enteredOn) {
        this.enteredOn = enteredOn;
    }

    @Override
    public Date getDateEntered() {
        return this.enteredOn;
    }

    @Override
    public String getType() {
        return "Daily Report";
    }

    @Override
    public Integer getId() {
        return this.officerDailyReportTextId;
    }

    @Override
    public Integer getImageCount() {
        return this.imageCount;
    }

    /**
     * @param imageCount the imageCount to set
     */
    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }

    public void setDocuments(ArrayList<OfficerDailyReportDocument> docs) {
        this.documents = docs;
    }

    public ArrayList<OfficerDailyReportDocument> getReportImages(String companyId) {
        if (this.documents == null) {
            try {
                OfficerDailyReportControllerInterface officerController = ControllerRegistryAbstract.getOfficerDailyReportController(companyId);
                this.documents = officerController.getDocumentsForReport(this.officerDailyReportTextId, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.documents;
    }

    @Override
    public ArrayList<ImageInterface> getImages(String companyId) {
        ArrayList<ImageInterface> retVal = new ArrayList<ImageInterface>();
        retVal.addAll(this.getReportImages(companyId));
        return retVal;
    }

    /**
     * @return the docCount
     */
    public Integer getDocCount() {
        return docCount;
    }

    /**
     * @param docCount the docCount to set
     */
    public void setDocCount(Integer docCount) {
        this.docCount = docCount;
    }
}
