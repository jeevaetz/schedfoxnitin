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
public class MobileFormsToClient {
    private Integer mobileFormsToClient;
    private Integer mobileFormsId;
    private Integer clientId;

    public MobileFormsToClient() {
        
    }
    
    public MobileFormsToClient(Record_Set rst) {
        try {
            mobileFormsToClient = rst.getInt("mobile_forms_to_client");
        } catch (Exception exe) {}
        try {
            mobileFormsId = rst.getInt("mobile_forms_id");
        } catch (Exception exe) {}
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the mobileFormsToClient
     */
    public Integer getMobileFormsToClient() {
        return mobileFormsToClient;
    }

    /**
     * @param mobileFormsToClient the mobileFormsToClient to set
     */
    public void setMobileFormsToClient(Integer mobileFormsToClient) {
        this.mobileFormsToClient = mobileFormsToClient;
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
    
    
}
