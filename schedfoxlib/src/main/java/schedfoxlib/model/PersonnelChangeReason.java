/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class PersonnelChangeReason {
    private Integer reason_id;
    private String reason;
    
    public PersonnelChangeReason() {
        
    }

    public PersonnelChangeReason(Record_Set rst) {
        try {
            reason_id = rst.getInt("reason_id");
        } catch (Exception exe) {}
        try {
            reason = rst.getString("reason");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the reason_id
     */
    public Integer getReason_id() {
        return reason_id;
    }

    /**
     * @param reason_id the reason_id to set
     */
    public void setReason_id(Integer reason_id) {
        this.reason_id = reason_id;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    @Override
    public String toString() {
        return this.reason;
    }
}
