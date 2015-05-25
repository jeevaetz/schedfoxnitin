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
public class MobileFormsType implements Serializable {
    private Integer mobileFormsTypeId;
    private String mobileFormTypeName;
    
    public MobileFormsType() {
        
    }
    
    public MobileFormsType(Record_Set rst) {
        try {
            this.mobileFormTypeName = rst.getString("mobile_form_type_name");
        } catch (Exception exe) {}
        try {
            this.mobileFormsTypeId = rst.getInt("mobile_forms_type_id");
        } catch (Exception exe) {}
    }

    /**
     * @return the mobileFormsTypeId
     */
    public Integer getMobileFormsTypeId() {
        return mobileFormsTypeId;
    }

    /**
     * @param mobileFormsTypeId the mobileFormsTypeId to set
     */
    public void setMobileFormsTypeId(Integer mobileFormsTypeId) {
        this.mobileFormsTypeId = mobileFormsTypeId;
    }

    /**
     * @return the mobileFormTypeName
     */
    public String getMobileFormTypeName() {
        return mobileFormTypeName;
    }

    /**
     * @param mobileFormTypeName the mobileFormTypeName to set
     */
    public void setMobileFormTypeName(String mobileFormTypeName) {
        this.mobileFormTypeName = mobileFormTypeName;
    }
}
