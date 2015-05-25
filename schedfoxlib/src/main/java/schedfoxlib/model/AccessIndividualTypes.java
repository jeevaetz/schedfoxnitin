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
public class AccessIndividualTypes implements Serializable {
    private Integer accessIndividualTypeId;
    private String accessType;
    
    public AccessIndividualTypes() {
        
    }
    
    public AccessIndividualTypes(Record_Set rst) {
        this.accessIndividualTypeId = rst.getInt("access_individual_type_id");
        this.accessType = rst.getString("access_type");
    }

    /**
     * @return the accessIndividualTypeId
     */
    public Integer getAccessIndividualTypeId() {
        return accessIndividualTypeId;
    }

    /**
     * @param accessIndividualTypeId the accessIndividualTypeId to set
     */
    public void setAccessIndividualTypeId(Integer accessIndividualTypeId) {
        this.accessIndividualTypeId = accessIndividualTypeId;
    }

    /**
     * @return the accessType
     */
    public String getAccessType() {
        return accessType;
    }

    /**
     * @param accessType the accessType to set
     */
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
    
    @Override
    public String toString() {
        return this.accessType;
    }
}
