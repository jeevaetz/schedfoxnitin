/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class GlobalMobileFormFillout implements Serializable {

    private Integer companyId;
    private String companyName;
    private String mobileFormName;
    private Date dateEntered;
    private Integer mobileFormDataFilloutId;
    private Integer mobileFormFilloutId;
    private Integer mobileFormDataId;
    private Integer mobileFormsId;
    private Boolean showInSummary;

    private ArrayList<String> filledOutDataSummaries;

    public GlobalMobileFormFillout() {
        filledOutDataSummaries = new ArrayList<String>();
    }

    public GlobalMobileFormFillout(Record_Set rst) {
        filledOutDataSummaries = new ArrayList<String>();
        try {
            this.companyId = rst.getInt("company_id");
        } catch (Exception exe) {
        }
        try {
            this.companyName = rst.getString("company_name");
        } catch (Exception exe) {
        }
        try {
            this.mobileFormName = rst.getString("mobile_form_name");
        } catch (Exception exe) {
        }
        try {
            this.dateEntered = rst.getTimestamp("date_entered");
        } catch (Exception exe) {
        }
        try {
            this.mobileFormDataFilloutId = rst.getInt("mobile_form_data_fillout_id");
        } catch (Exception exe) {
        }
        try {
            this.mobileFormFilloutId = rst.getInt("mobile_form_fillout_id");
        } catch (Exception exe) {
        }
        try {
            this.mobileFormDataId = rst.getInt("mobile_form_data_id");
        } catch (Exception exe) {
        }
        try {
            this.mobileFormsId = rst.getInt("mobile_forms_id");
        } catch (Exception exe) {
        }
        try {
            this.showInSummary = rst.getBoolean("show_in_summary");
        } catch (Exception exe) {
        }
    }

    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the mobileFormName
     */
    public String getMobileFormName() {
        return mobileFormName;
    }

    /**
     * @param mobileFormName the mobileFormName to set
     */
    public void setMobileFormName(String mobileFormName) {
        this.mobileFormName = mobileFormName;
    }

    /**
     * @return the dateEntered
     */
    public Date getDateEntered() {
        return dateEntered;
    }

    /**
     * @param dateEntered the dateEntered to set
     */
    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    /**
     * @return the mobileFormDataFilloutId
     */
    public Integer getMobileFormDataFilloutId() {
        return mobileFormDataFilloutId;
    }

    /**
     * @param mobileFormDataFilloutId the mobileFormDataFilloutId to set
     */
    public void setMobileFormDataFilloutId(Integer mobileFormDataFilloutId) {
        this.mobileFormDataFilloutId = mobileFormDataFilloutId;
    }

    /**
     * @return the mobileFormFilloutId
     */
    public Integer getMobileFormFilloutId() {
        return mobileFormFilloutId;
    }

    /**
     * @param mobileFormFilloutId the mobileFormFilloutId to set
     */
    public void setMobileFormFilloutId(Integer mobileFormFilloutId) {
        this.mobileFormFilloutId = mobileFormFilloutId;
    }

    /**
     * @return the mobileFormDataId
     */
    public Integer getMobileFormDataId() {
        return mobileFormDataId;
    }

    /**
     * @param mobileFormDataId the mobileFormDataId to set
     */
    public void setMobileFormDataId(Integer mobileFormDataId) {
        this.mobileFormDataId = mobileFormDataId;
    }

    /**
     * @return the mobileFormsId
     */
    public Integer getMobileFormsId() {
        return mobileFormsId;
    }

    /**
     * @param mobileFormsId the mobileFormsId to set
     */
    public void setMobileFormsId(Integer mobileFormsId) {
        this.mobileFormsId = mobileFormsId;
    }

    /**
     * @return the showInSummary
     */
    public Boolean getShowInSummary() {
        return showInSummary;
    }

    /**
     * @param showInSummary the showInSummary to set
     */
    public void setShowInSummary(Boolean showInSummary) {
        this.showInSummary = showInSummary;
    }

    /**
     * @return the filledOutDataSummaries
     */
    public ArrayList<String> getFilledOutDataSummaries() {
        return filledOutDataSummaries;
    }

    /**
     * @param filledOutDataSummaries the filledOutDataSummaries to set
     */
    public void setFilledOutDataSummaries(ArrayList<String> filledOutDataSummaries) {
        this.filledOutDataSummaries = filledOutDataSummaries;
    }

    @Override
    public int hashCode() {
        return 31 * this.getCompanyName().hashCode() + this.mobileFormFilloutId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GlobalMobileFormFillout other = (GlobalMobileFormFillout) obj;
        if ((this.companyName == null) ? (other.companyName != null) : !this.companyName.equals(other.companyName)) {
            return false;
        }
        try {
            if (!this.mobileFormFilloutId.equals(other.mobileFormFilloutId) && (this.mobileFormFilloutId == null || !this.mobileFormFilloutId.equals(other.mobileFormFilloutId))) {
                return false;
            }
        } catch (Exception exe) {
            return false;
        }
        return true;
    }
}
