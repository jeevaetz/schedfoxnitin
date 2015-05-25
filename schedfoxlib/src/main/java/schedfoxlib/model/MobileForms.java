/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class MobileForms implements Serializable {

    /**
     * @return the reservedIdentifiers
     */
    public static String[] getReservedIdentifiers() {
        return reservedIdentifiers;
    }
    private Integer mobileFormsId;
    private String mobileFormName;
    private Integer clientId;
    private Integer entryType;
    private Boolean active;
    private Boolean isAutoEmail;
    private byte[] reportData;
    private Boolean displayOnDevice;
    private Boolean oncePerDay;
    private Boolean sendImmediately;

    private static String[] reservedIdentifiers = {"active_db", "checkbox_checked", "checkbox_checked_img", "checkbox_checked_URL", "checkbox_unchecked", "checkbox_unchecked_img", "checkbox_unchecked_URL", "client_id", "dateFormatter",
        "end_date", "start_date", "user_id", "incident_report_id", "client_name", "employee_name", "curr_date", "logo"};

    public MobileForms() {
        displayOnDevice = true;
        isAutoEmail = false;
        active = true;
    }

    public MobileForms(Record_Set rst) {
        try {
            mobileFormsId = rst.getInt("mobile_forms_id");
        } catch (Exception exe) {
        }
        try {
            mobileFormName = rst.getString("mobile_form_name");
        } catch (Exception exe) {
        }
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception exe) {
        }
        try {
            entryType = rst.getInt("entry_type");
        } catch (Exception exe) {
        }
        try {
            active = rst.getBoolean("active");
        } catch (Exception exe) {
        }
        try {
            isAutoEmail = rst.getBoolean("is_auto_email");
        } catch (Exception exe) {
        }
        try {
            reportData = rst.getByteArray("report_data");
        } catch (Exception exe) {
        }
        try {
            displayOnDevice = rst.getBoolean("display_on_device");
        } catch (Exception exe) {
        }
        try {
            oncePerDay = rst.getBoolean("once_per_day");
        } catch (Exception exe) {
        }
        try {
            sendImmediately = rst.getBoolean("send_immediately");
        } catch (Exception exe) {}
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
     * @return the clientId
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the entryType
     */
    public Integer getEntryType() {
        return entryType;
    }

    /**
     * @param entryType the entryType to set
     */
    public void setEntryType(Integer entryType) {
        this.entryType = entryType;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return this.mobileFormName;
    }

    /**
     * @return the isAutoEmail
     */
    public Boolean getIsAutoEmail() {
        return isAutoEmail;
    }

    /**
     * @param isAutoEmail the isAutoEmail to set
     */
    public void setIsAutoEmail(Boolean isAutoEmail) {
        this.isAutoEmail = isAutoEmail;
    }

    /**
     * @return the reportData
     */
    public byte[] getReportData() {
        return reportData;
    }

    /**
     * @param reportData the reportData to set
     */
    public void setReportData(byte[] reportData) {
        this.reportData = reportData;
    }

    /**
     * @return the displayOnDevice
     */
    public Boolean getDisplayOnDevice() {
        return displayOnDevice;
    }

    /**
     * @param displayOnDevice the displayOnDevice to set
     */
    public void setDisplayOnDevice(Boolean displayOnDevice) {
        this.displayOnDevice = displayOnDevice;
    }

    /**
     * @return the oncePerDay
     */
    public Boolean getOncePerDay() {
        return oncePerDay;
    }

    /**
     * @param oncePerDay the oncePerDay to set
     */
    public void setOncePerDay(Boolean oncePerDay) {
        this.oncePerDay = oncePerDay;
    }

    /**
     * @return the sendImmediately
     */
    public Boolean getSendImmediately() {
        return sendImmediately;
    }

    /**
     * @param sendImmediately the sendImmediately to set
     */
    public void setSendImmediately(Boolean sendImmediately) {
        this.sendImmediately = sendImmediately;
    }

}
