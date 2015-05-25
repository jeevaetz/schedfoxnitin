/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ClientContractType {
    private int clientContractTypeId;
    private String contractType;

    public ClientContractType() {
        
    }

    public ClientContractType(Record_Set rst) {
        this.clientContractTypeId = rst.getInt("client_contract_type_id");
        this.contractType = rst.getString("contract_type");
    }

    /**
     * @return the clientContractTypeId
     */
    public int getClientContractTypeId() {
        return clientContractTypeId;
    }

    /**
     * @param clientContractTypeId the clientContractTypeId to set
     */
    public void setClientContractTypeId(int clientContractTypeId) {
        this.clientContractTypeId = clientContractTypeId;
    }

    /**
     * @return the contractType
     */
    public String getContractType() {
        return contractType;
    }

    /**
     * @param contractType the contractType to set
     */
    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    @Override
    public String toString() {
        return this.contractType;
    }
}
