/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.ClientControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.FileLoader;

/**
 *
 * @author user
 */
public class ClientContract implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer clientContractId;
    private int clientId;
    private Date startDate;
    private Date endDate;
    private BigDecimal projectedIncrease;
    private int clientContractTypeId;
    private String renewalPeriod;
    private Date lastRenewed;
    private Boolean autoRenew;
    private Integer salesPerson;
    private Integer salesCommission;
    private Integer salesManager;
    private Boolean tempAccount;
    private Date startCommissionDate;

    //Not persisted field
    private Boolean commissionStartsHere;

    //Lazy loaded client.
    private Client client;
    private ClientContractType clientContractType;
    private File contractFile;
    private String contractFileName;
    private boolean triedFileLoad = false;

    //Contract Type Cache
    private static HashMap<Integer, ClientContractType> contractTypeCache =
            new HashMap<Integer, ClientContractType>();

    public ClientContract() {
    }

    public ClientContract(Record_Set rst) {
        try {
            this.clientContractId = rst.getInt("client_contract_id");
        } catch (Exception e) {}
        try {
            this.clientId = rst.getInt("client_id");
        } catch (Exception e) {}
        try {
            this.startDate = rst.getDate("start_date");
        } catch (Exception e) {}
        try {
            this.endDate = rst.getDate("end_date");
        } catch (Exception e) {}
        try {
            this.projectedIncrease = rst.getBigDecimal("projected_increase");
        } catch (Exception e) {}
        try {
            this.clientContractTypeId = rst.getInt("client_contract_type_id");
        } catch (Exception e) {}
        try {
            this.renewalPeriod = rst.getString("renewal_period");
        } catch (Exception e) {}
        try {
            this.lastRenewed = rst.getDate("last_renewed");
        } catch (Exception e) {}
        try {
            this.autoRenew = rst.getBoolean("auto_renew");
        } catch (Exception e) {}
        try {
            this.salesPerson = rst.getInt("salesperson");
        } catch (Exception e) {}
        try {
            this.salesManager = rst.getInt("salesmanager");
        } catch (Exception e) {}
        try {
            this.salesCommission = rst.getInt("salescommission");
        } catch (Exception e) {}
        try {
            this.tempAccount = rst.getBoolean("temp_account");
        } catch (Exception e) {}
        try {
            this.startCommissionDate = rst.getDate("start_commission_date");
        } catch (Exception e) {}
    }

    public Integer getClientContractId() {
        return clientContractId;
    }

    public void setClientContractId(Integer clientContractId) {
        this.clientContractId = clientContractId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getProjectedIncrease() {
        return projectedIncrease;
    }

    public void setProjectedIncrease(BigDecimal projectedIncrease) {
        this.projectedIncrease = projectedIncrease;
    }

    public String getFileStringForContract(String companySchema) {
        if (!triedFileLoad) {
            triedFileLoad = true;
            ArrayList<String> filesStr = FileLoader.getFileNames(companySchema,
                "con" + getClientId() + "_" + getClientContractId(), "location_additional_files");
            if (filesStr.size() > 0) {
                contractFileName = filesStr.get(0);
            }
            return null;
        }
        return contractFileName;
    }

    public File getFileForContract(String companySchema) {
        if (!triedFileLoad) {
            triedFileLoad = true;
            ArrayList<String> filesStr = FileLoader.getFileNames(companySchema,
                "con" + getClientId() + "_" + getClientContractId(), "location_additional_files");
            if (filesStr.size() > 0) {
                contractFile = FileLoader.getFileFromServer(filesStr.get(0));
            }
            return null;
        }
        return contractFile;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientContractId != null ? clientContractId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientContract)) {
            return false;
        }
        ClientContract other = (ClientContract) object;
        if ((this.clientContractId == null && other.clientContractId != null) || (this.clientContractId != null && !this.clientContractId.equals(other.clientContractId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ClientContract[clientContractId=" + clientContractId + "]";
    }

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    public Client getClient(String companyId) {
        if (client == null) {
            ClientControllerInterface clientController = ControllerRegistryAbstract.getClientController(companyId);
            try {
                client = clientController.getClientById(clientId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    public ClientContractType getClientContractType(String companyId) {
        ClientControllerInterface clientController = ControllerRegistryAbstract.getClientController(companyId);
        if (this.clientContractType == null) {
            try {
                if (contractTypeCache.get(this.clientContractTypeId) == null) {
                    contractTypeCache.put(this.clientContractTypeId, clientController.getClientContractType(this.clientContractTypeId));
                }
                this.clientContractType = contractTypeCache.get(this.clientContractTypeId);
            } catch (Exception e) {}
        }
        return this.clientContractType;
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
     * @return the renewalPeriod
     */
    public String getRenewalPeriod() {
        return renewalPeriod;
    }

    /**
     * @param renewalPeriod the renewalPeriod to set
     */
    public void setRenewalPeriod(String renewalPeriod) {
        this.renewalPeriod = renewalPeriod;
    }

    /**
     * @return the lastRenewed
     */
    public Date getLastRenewed() {
        return lastRenewed;
    }

    /**
     * @param lastRenewed the lastRenewed to set
     */
    public void setLastRenewed(Date lastRenewed) {
        this.lastRenewed = lastRenewed;
    }

    /**
     * @return the autoRenew
     */
    public Boolean getAutoRenew() {
        return autoRenew;
    }

    /**
     * @param autoRenew the autoRenew to set
     */
    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    /**
     * @return the salesPerson
     */
    public Integer getSalesPerson() {
        return salesPerson;
    }

    /**
     * @param salesPerson the salesPerson to set
     */
    public void setSalesPerson(Integer salesPerson) {
        this.salesPerson = salesPerson;
    }

    /**
     * @return the salesCommission
     */
    public Integer getSalesCommission() {
        return salesCommission;
    }

    /**
     * @param salesCommission the salesCommission to set
     */
    public void setSalesCommission(Integer salesCommission) {
        this.salesCommission = salesCommission;
    }

    /**
     * @return the salesManager
     */
    public Integer getSalesManager() {
        return salesManager;
    }

    /**
     * @param salesManager the salesManager to set
     */
    public void setSalesManager(Integer salesManager) {
        this.salesManager = salesManager;
    }

    /**
     * @return the tempAccount
     */
    public Boolean getTempAccount() {
        return tempAccount;
    }

    /**
     * @param tempAccount the tempAccount to set
     */
    public void setTempAccount(Boolean tempAccount) {
        this.tempAccount = tempAccount;
    }

    /**
     * @return the commissionStartsHere
     */
    public Boolean getCommissionStartsHere() {
        return commissionStartsHere;
    }

    /**
     * @param commissionStartsHere the commissionStartsHere to set
     */
    public void setCommissionStartsHere(Boolean commissionStartsHere) {
        this.commissionStartsHere = commissionStartsHere;
    }

    /**
     * @return the startCommissionDate
     */
    public Date getStartCommissionDate() {
        return startCommissionDate;
    }

    /**
     * @param startCommissionDate the startCommissionDate to set
     */
    public void setStartCommissionDate(Date startCommissionDate) {
        this.startCommissionDate = startCommissionDate;
    }

}
