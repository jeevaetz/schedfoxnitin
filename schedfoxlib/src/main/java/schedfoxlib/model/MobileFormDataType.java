/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class MobileFormDataType {
    private Integer mobileFormDataTypeId;
    private String mobileFormDataTypeName;
    
    public MobileFormDataType() {
        
    }
    
    public MobileFormDataType(Record_Set rst) {
        try {
            mobileFormDataTypeId = rst.getInt("mobile_form_data_type_id");
        } catch (Exception exe) {}
        try {
            mobileFormDataTypeName = rst.getString("mobile_form_data_type_name");
        } catch (Exception exe) {}
    }

    /**
     * @return the mobileFormDataTypeId
     */
    public Integer getMobileFormDataTypeId() {
        return mobileFormDataTypeId;
    }

    /**
     * @param mobileFormDataTypeId the mobileFormDataTypeId to set
     */
    public void setMobileFormDataTypeId(Integer mobileFormDataTypeId) {
        this.mobileFormDataTypeId = mobileFormDataTypeId;
    }

    /**
     * @return the mobileFormDataTypeName
     */
    public String getMobileFormDataTypeName() {
        return mobileFormDataTypeName;
    }

    /**
     * @param mobileFormDataTypeName the mobileFormDataTypeName to set
     */
    public void setMobileFormDataTypeName(String mobileFormDataTypeName) {
        this.mobileFormDataTypeName = mobileFormDataTypeName;
    }
}
