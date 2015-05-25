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
public class TerminationReason implements Serializable {
    private Integer terminationReason;
    private String reason;
    
    public TerminationReason() {
        
    }

    public TerminationReason(Record_Set rst) {
        try {
            terminationReason = rst.getInt("termination_reason_id");
        } catch (Exception exe) {}
        try {
            reason = rst.getString("reason");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the terminationReason
     */
    public Integer getTerminationReason() {
        return terminationReason;
    }

    /**
     * @param terminationReason the terminationReason to set
     */
    public void setTerminationReason(Integer terminationReason) {
        this.terminationReason = terminationReason;
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
