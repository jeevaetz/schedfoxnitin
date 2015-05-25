/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ira
 */
public class MobileFormDataSearch implements Serializable {
    
    private String dataToSearch;
    private Integer mobileFormDataId;
    private Integer mobileFormDataTypeId;
    
    private MobileFormData mobileFormData;
    
    private ArrayList<MobileFormDataFillout> fillouts;
    
    public MobileFormDataSearch() {
        
    }

    /**
     * @return the dataToSearch
     */
    public String getDataToSearch() {
        return dataToSearch;
    }

    /**
     * @param dataToSearch the dataToSearch to set
     */
    public void setDataToSearch(String dataToSearch) {
        this.dataToSearch = dataToSearch;
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
     * @return the mobileFormData
     */
    public MobileFormData getMobileFormData() {
        return mobileFormData;
    }

    /**
     * @param mobileFormData the mobileFormData to set
     */
    public void setMobileFormData(MobileFormData mobileFormData) {
        this.mobileFormData = mobileFormData;
    }

    /**
     * @return the fillouts
     */
    public ArrayList<MobileFormDataFillout> getFillouts() {
        return fillouts;
    }

    /**
     * @param fillouts the fillouts to set
     */
    public void setFillouts(ArrayList<MobileFormDataFillout> fillouts) {
        this.fillouts = fillouts;
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
}
