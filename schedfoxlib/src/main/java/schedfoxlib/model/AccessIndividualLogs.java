/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ira
 */
public class AccessIndividualLogs implements Serializable {
    private Integer accessIndividualLogId;
    private Integer accessIndividualId;
    private Integer scannedBy;
    private Date scannedOn;
    private Integer scannedType;
    
    public static int SCANNED_IN = 1;
    public static int SCANNED_OUT = 2;
    
    public AccessIndividualLogs() {
        
    }

    /**
     * @return the accessIndividualLogId
     */
    public Integer getAccessIndividualLogId() {
        return accessIndividualLogId;
    }

    /**
     * @param accessIndividualLogId the accessIndividualLogId to set
     */
    public void setAccessIndividualLogId(Integer accessIndividualLogId) {
        this.accessIndividualLogId = accessIndividualLogId;
    }

    /**
     * @return the accessIndividualId
     */
    public Integer getAccessIndividualId() {
        return accessIndividualId;
    }

    /**
     * @param accessIndividualId the accessIndividualId to set
     */
    public void setAccessIndividualId(Integer accessIndividualId) {
        this.accessIndividualId = accessIndividualId;
    }

    /**
     * @return the scannedBy
     */
    public Integer getScannedBy() {
        return scannedBy;
    }

    /**
     * @param scannedBy the scannedBy to set
     */
    public void setScannedBy(Integer scannedBy) {
        this.scannedBy = scannedBy;
    }

    /**
     * @return the scannedOn
     */
    public Date getScannedOn() {
        return scannedOn;
    }

    /**
     * @param scannedOn the scannedOn to set
     */
    public void setScannedOn(Date scannedOn) {
        this.scannedOn = scannedOn;
    }

    /**
     * @return the scannedType
     */
    public Integer getScannedType() {
        return scannedType;
    }

    /**
     * @param scannedType the scannedType to set
     */
    public void setScannedType(Integer scannedType) {
        this.scannedType = scannedType;
    }
}
