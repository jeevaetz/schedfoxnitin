/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class SalesCall {
    private Integer salesCallId;
    private Integer equipmentId;
    private String phoneNumber;
    private Date contactDate;
    private long contactDurationMs;
    private Integer localId;
    private String phoneName;
    private String callType;
    
    public SalesCall() {
        
    }
    
    public SalesCall(Record_Set rst) {
        try {
            salesCallId = rst.getInt("sales_call_id");
        } catch (Exception exe) {}
        try {
            equipmentId = rst.getInt("equipment_id");
        } catch (Exception exe) {}
        try {
            phoneNumber = rst.getString("phone_number");
        } catch (Exception exe) {}
        try {
            contactDate = rst.getTimestamp("contact_date");
        } catch (Exception exe) {}
        try {
            contactDurationMs = rst.getInt("contact_duration_ms");
        } catch (Exception exe) {}
        try {
            localId = rst.getInt("localId");
        } catch (Exception exe) {}
        try {
            phoneName = rst.getString("phone_name");
        } catch (Exception exe) {}
        try {
            callType = rst.getString("call_type");
        } catch (Exception exe) {}
    }

    /**
     * @return the salesCallId
     */
    public Integer getSalesCallId() {
        return salesCallId;
    }

    /**
     * @param salesCallId the salesCallId to set
     */
    public void setSalesCallId(Integer salesCallId) {
        this.salesCallId = salesCallId;
    }

    /**
     * @return the equipmentId
     */
    public Integer getEquipmentId() {
        return equipmentId;
    }

    /**
     * @param equipmentId the equipmentId to set
     */
    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getFormattedPhoneNumber() {
        if (this.phoneNumber != null && this.phoneNumber.length() == 11) {
            return phoneNumber.substring(0, 1) + " (" + phoneNumber.substring(1, 4) + ") " + phoneNumber.substring(4,7) + "-" + phoneNumber.substring(7); 
        } else if (this.phoneNumber != null && this.phoneNumber.length() == 10) {
            return "(" + phoneNumber.substring(0, 3) + ") " + phoneNumber.substring(3,6) + "-" + phoneNumber.substring(6); 
        } else if (this.phoneNumber != null && this.phoneNumber.length() == 7) {
            return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3);
        } else if (this.phoneNumber != null && this.phoneNumber.equals("*86")) {
            return "*86 (Voicemail)";
        }
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the contactDate
     */
    public Date getContactDate() {
        return contactDate;
    }

    /**
     * @param contactDate the contactDate to set
     */
    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    /**
     * @return the contactDurationMs
     */
    public long getContactDurationMs() {
        return contactDurationMs;
    }

    /**
     * @param contactDurationMs the contactDurationMs to set
     */
    public void setContactDurationMs(long contactDurationMs) {
        this.contactDurationMs = contactDurationMs;
    }

    public String getContactDuration() {
        StringBuilder retVal = new StringBuilder();
        if (contactDurationMs / 60 / 60 >= 1) {
            if (contactDurationMs / 60 / 60 == 1) {
                retVal.append("1 hour ");
            } else {
                retVal.append((contactDurationMs / 60 / 60) + " hour ");
            }
        }
        if ((contactDurationMs / 60) % 60 > 0) {
            retVal.append(((contactDurationMs / 60) % 60) + " minutes ");
        }
        return retVal.toString();
    }
    
    /**
     * @return the localId
     */
    public Integer getLocalId() {
        return localId;
    }

    /**
     * @param localId the localId to set
     */
    public void setLocalId(Integer localId) {
        this.localId = localId;
    }

    /**
     * @return the phoneName
     */
    public String getPhoneName() {
        return phoneName;
    }

    /**
     * @param phoneName the phoneName to set
     */
    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    /**
     * @return the callType
     */
    public String getCallType() {
        return callType;
    }

    /**
     * @param callType the callType to set
     */
    public void setCallType(String callType) {
        this.callType = callType;
    }
}
