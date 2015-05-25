/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 * Specialized class used for logins across schema so we know what company the 
 * client came from, only needed in that specific scenario.
 * @author ira
 */
public class CrossCompanyClient implements Serializable {
    private Integer companyId;
    private Client client;

    public CrossCompanyClient(Date date, Record_Set rst) {
        client = new Client(date, rst);
        try {
            this.companyId = rst.getInt("company_id");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
    
    
}
