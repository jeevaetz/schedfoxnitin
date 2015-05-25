/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.BillingControllerInterface;
import schedfoxlib.controller.ClientContactControllerInterface;
import schedfoxlib.controller.ClientControllerInterface;
import schedfoxlib.controller.EquipmentControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class Client implements Serializable, AddressInterface, Entity, Comparable {

    private static final long serialVersionUID = 1L;
    private Date currDate;
    private Integer newClientId;
    private Integer clientId;
    private int branchId;
    private String clientName;
    private String clientPhone;
    private String clientPhone2;
    private String clientFax;
    private Address clientAddressObj;
    private int managementId;
    private Date clientStartDate;
    private Date clientEndDate;
    private short clientIsDeleted;
    private int clientType;
    private Date clientLastUpdated;
    private int clientWorksite;
    private Double clientTrainingTime;
    private Boolean clientBillForTraining;
    private Integer clientWorksiteOrder;
    private Integer rateCodeId;
    private String clientBreak;
    private Integer storeVolumeId;
    private Boolean storeRemoteMarketId;
    private String url;
    private String cUserName;
    private String cPassword;
    private Boolean defaultNonBillable;
    private Integer checkOutOptionId;
    private Integer numMonths;
    private Integer notifyDmLateCheckoutMinutes;
    private Boolean logIntoRoute;
    private Boolean displayClientInCallQueue;
    private String reportTime;

    //Set sometimes....
    private Boolean hasNotes;
    private Boolean checkinFromPostPhone;
    private String uskedIdentifier;
    private Integer lastRating;
    private String clientIndustry;
    private ArrayList<Integer> assignedClientEquipmentIds;
    
    //Lazy loaded objects
    private transient ArrayList<ClientContact> contacts;
    private transient ArrayList<ClientRateCode> rateCodes;
    private transient ArrayList<ClientEquipment> clientEquipment;
    private transient ClientExport clientExport;
    private transient Client parentSite;
    private transient User dmAssociatedWithAccount;

    private ArrayList<ClientContact> nonCachedContacts;
    
    public Client() {
        this.assignedClientEquipmentIds = new ArrayList<Integer>();
    }

    public Client(Date currDate) {
        this.currDate = currDate;
        this.clientId = new Integer(0);
        this.assignedClientEquipmentIds = new ArrayList<Integer>();
    }

    public Client(Date currDate, Integer clientId) {
        this.clientId = clientId;
        this.currDate = currDate;
        this.assignedClientEquipmentIds = new ArrayList<Integer>();
    }

    public Client(Date currDate, Record_Set rst) {
        this.currDate = currDate;
        try {
            this.clientId = rst.getInt("client_id");
        } catch (Exception e) {
        }
        try {
            this.clientName = rst.getString("client_name");
        } catch (Exception e) {
        }
        try {
            this.branchId = rst.getInt("branch_id");
        } catch (Exception e) {
        }
        clientAddressObj = new Address();
        try {
            this.clientAddressObj.setLatitude(rst.getFloat("latitude"));
        } catch (Exception e) {
        }
        try {
            this.clientAddressObj.setLongitude(rst.getFloat("longitude"));
        } catch (Exception e) {
        }
        try {
            this.clientAddressObj.setAddress1(rst.getString("client_address"));
        } catch (Exception e) {
        }
        try {
            this.clientAddressObj.setAddress2(rst.getString("client_address2"));
        } catch (Exception e) {
        }
        try {
            this.clientAddressObj.setCity(rst.getString("client_city"));
        } catch (Exception e) {
        }
        try {
            this.clientAddressObj.setState(rst.getString("client_state"));
        } catch (Exception e) {
        }
        try {
            this.clientAddressObj.setZip(rst.getString("client_zip"));
        } catch (Exception e) {
        }
        try {
            this.clientPhone = rst.getString("client_phone");
        } catch (Exception e) {
        }
        try {
            this.clientPhone2 = rst.getString("client_phone2");
        } catch (Exception e) {
        }
        try {
            this.clientFax = rst.getString("client_fax");
        } catch (Exception e) {
        }
        try {
            this.rateCodeId = rst.getInt("rate_code_id");
        } catch (Exception e) {
        }
        try {
            this.clientIsDeleted = 0;
            if (rst.getBoolean("client_is_deleted")) {
                this.clientIsDeleted = 1;
            }
        } catch (Exception e) {
        }

        try {
            this.clientStartDate = rst.getDate("client_start_date");
        } catch (Exception e) {
        }
        try {
            this.clientEndDate = rst.getDate("client_end_date");
        } catch (Exception e) {
        }
        try {
            this.clientTrainingTime = new Double(rst.getInt("client_training_time"));
        } catch (Exception e) {
        }
        try {
            this.clientWorksite = rst.getInt("client_worksite");
        } catch (Exception e) {
        }
        try {
            this.url = rst.getString("url");
        } catch (Exception e) {
        }
        try {
            this.cUserName = rst.getString("cusername");
        } catch (Exception e) {
        }
        try {
            this.cPassword = rst.getString("cpassword");
        } catch (Exception e) {
        }
        try {
            this.defaultNonBillable = rst.getBoolean("default_non_billable");
        } catch (Exception e) {
        }
        try {
            this.setClientBillForTrainingBoolean(rst.getBoolean("client_bill_for_training"));
        } catch (Exception e) {
        }
        try {
            if (rst.getBoolean("hasnotes")) {
                hasNotes = rst.getBoolean("hasnotes");
            }
        } catch (Exception e) {
        }
        try {
            this.checkOutOptionId = rst.getInt("check_out_option_id");
        } catch (Exception e) {
        }
        try {
            this.numMonths = rst.getInt("num_months");
        } catch (Exception e) {
        }
        try {
            this.checkinFromPostPhone = rst.getBoolean("checkin_from_post_phone");
        } catch (Exception e) {
        }
        try {
            this.notifyDmLateCheckoutMinutes = rst.getInt("notify_dm_late_checkout_minutes");
        } catch (Exception e) {}
        try {
            this.logIntoRoute = rst.getBoolean("log_into_route");
        } catch (Exception e) {}
        try {
            this.uskedIdentifier = rst.getString("usked_identifier");
        } catch (Exception e) {}
        try {
            this.lastRating = rst.getInt("last_rating");
        } catch (Exception e) {}
        try {
            this.clientIndustry = rst.getString("client_industry");
        } catch (Exception e) {}
        try {
            this.displayClientInCallQueue = rst.getBoolean("display_client_in_call_queue");
        } catch (Exception e) {}
        try {
            this.reportTime = rst.getString("report_time");
        } catch (Exception e) {}
        this.assignedClientEquipmentIds = new ArrayList<Integer>();
    }

    public Client(Date currDate, Integer clientId, int branchId, String clientName, String clientPhone, String clientPhone2, String clientFax, String clientAddress, String clientAddress2, String clientCity, String clientState, String clientZip, int managementId, Date clientStartDate, Date clientEndDate, short clientIsDeleted, int clientType, Date clientLastUpdated, int clientWorksite) {
        this.clientId = clientId;
        this.currDate = currDate;

        this.branchId = branchId;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.clientPhone2 = clientPhone2;
        this.clientFax = clientFax;
        this.clientAddressObj.setAddress1(clientAddress);
        this.clientAddressObj.setAddress2(clientAddress2);
        this.clientAddressObj.setCity(clientCity);
        this.clientAddressObj.setState(clientState);
        this.clientAddressObj.setZip(clientZip);
        this.managementId = managementId;
        this.clientStartDate = clientStartDate;
        this.clientEndDate = clientEndDate;
        this.clientIsDeleted = clientIsDeleted;
        this.clientType = clientType;
        this.clientLastUpdated = clientLastUpdated;
        this.clientWorksite = clientWorksite;
        this.assignedClientEquipmentIds = new ArrayList<Integer>();
    }

    @Override
    public int compareTo(Object obj) {
        if (obj instanceof Entity) {
            return this.getName().compareTo(((Entity) obj).getName());
        }
        return 1;
    }

    public boolean contains(String searchString) {
        boolean retVal = false;
        try {
            if (clientName.toUpperCase().contains(searchString.toUpperCase())) {
                retVal = true;
            } else if (clientPhone.replaceAll("[^\\d.]", "").toUpperCase().contains(searchString.toUpperCase())) {
                retVal = true;
            } else if (clientPhone2.replaceAll("[^\\d.]", "").toUpperCase().contains(searchString.toUpperCase())) {
                retVal = true;
            } else if (clientFax.replaceAll("[^\\d.]", "").toUpperCase().contains(searchString.toUpperCase())) {
                retVal = true;
            } else if (clientAddressObj.getAddress1().toUpperCase().contains(searchString.toUpperCase())) {
                retVal = true;
            } else if (clientAddressObj.getAddress2().toUpperCase().contains(searchString.toUpperCase())) {
                retVal = true;
            } else if (clientAddressObj.getCity().toUpperCase().contains(searchString.toUpperCase())) {
                retVal = true;
            } else if (clientAddressObj.getState().toUpperCase().contains(searchString.toUpperCase())) {
                retVal = true;
            } else if (clientAddressObj.getZip().toUpperCase().contains(searchString.toUpperCase())) {
                retVal = true;
            }
            for (int c = 0; c < this.contacts.size(); c++) {
                ClientContact contact = this.contacts.get(c);
                if ((contact.getClientContactFirstName() + " " + contact.getClientContactLastName()).toUpperCase().contains(searchString.toUpperCase())) {
                    retVal = true;
                } else if (contact.getClientContactCell().replaceAll("[^\\d.]", "").toUpperCase().contains(searchString.toUpperCase())) {
                    retVal = true;
                } else if (contact.getClientContactFax().replaceAll("[^\\d.]", "").toUpperCase().contains(searchString.toUpperCase())) {
                    retVal = true;
                } else if (contact.getClientContactEmail().toUpperCase().contains(searchString.toUpperCase())) {
                    retVal = true;
                } else if (contact.getClientContactPhone().replaceAll("[^\\d.]", "").toUpperCase().contains(searchString.toUpperCase())) {
                    retVal = true;
                } else if (contact.getClientContactAddress().toUpperCase().contains(searchString.toUpperCase())) {
                    retVal = true;
                } else if (contact.getClientContactAddress2().toUpperCase().contains(searchString.toUpperCase())) {
                    retVal = true;
                } else if (contact.getClientContactCity().toUpperCase().contains(searchString.toUpperCase())) {
                    retVal = true;
                } else if (contact.getClientContactState().toUpperCase().contains(searchString.toUpperCase())) {
                    retVal = true;
                } else if (contact.getClientContactZip().toUpperCase().contains(searchString.toUpperCase())) {
                    retVal = true;
                }
            }
        } catch (Exception exe) {
            //exe.printStackTrace();
        }
        return retVal;
    }

    public Object getValueFromDbName(String columnName) {
        if (columnName != null) {
            if (columnName.equalsIgnoreCase("client_id")) {
                return this.clientId;
            } else if (columnName.equalsIgnoreCase("branch_id")) {
                return this.branchId;
            } else if (columnName.equalsIgnoreCase("client_address")) {
                return this.getClientAddressObj().getAddress1();
            } else if (columnName.equalsIgnoreCase("client_address2")) {
                return this.getClientAddressObj().getAddress2();
            } else if (columnName.equalsIgnoreCase("client_city")) {
                return this.getClientAddressObj().getCity();
            } else if (columnName.equalsIgnoreCase("client_state")) {
                return this.getClientAddressObj().getState();
            } else if (columnName.equalsIgnoreCase("client_zip")) {
                return this.getClientAddressObj().getZip();
            } else if (columnName.equalsIgnoreCase("client_phone")) {
                return this.clientPhone;
            } else if (columnName.equalsIgnoreCase("client_phone2")) {
                return this.clientPhone2;
            } else if (columnName.equalsIgnoreCase("client_fax")) {
                return this.clientFax;
            } else if (columnName.equalsIgnoreCase("client_is_deleted")) {
                return this.clientIsDeleted;
            } else if (columnName.equalsIgnoreCase("client_name")) {
                return this.clientName;
            } else if (columnName.equalsIgnoreCase("client_id")) {
                return this.clientId;
            } else if (columnName.equalsIgnoreCase("client_id")) {
                return this.clientId;
            } else if (columnName.equalsIgnoreCase("client_id")) {
                return this.clientId;
            } else if (columnName.equalsIgnoreCase("client_start_date")) {
                return this.clientStartDate;
            } else if (columnName.equalsIgnoreCase("client_end_date")) {
                return this.clientEndDate;
            } else if (columnName.equalsIgnoreCase("client_training_time")) {
                return this.clientTrainingTime;
            } else if (columnName.equalsIgnoreCase("url")) {
                return this.url;
            }
        }
        return "";
    }

    public Integer getNewClientId() {
        return newClientId;
    }

    public void setNewClientId(Integer newClientId) {
        this.newClientId = newClientId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getClientName() {
        if (clientName == null) {
            return "";
        }
        return clientName;
    }

    public void setClientName(String clientName) {
        if (clientName != null && clientName.length() > 100) {
            clientName = clientName.substring(0, 99);
        }
        this.clientName = clientName;
    }

    public String getClientPhone() {
        if (clientPhone == null) {
            return "";
        }
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        if (clientPhone != null && clientPhone.length() > 20) {
            clientPhone = clientPhone.substring(0, 19);
        }
        if (clientPhone != null) {
            clientPhone = clientPhone.replaceAll("\\D", "");
            if (clientPhone.length() >= 10) {
                clientPhone = clientPhone.substring(0, 3) + "-" + clientPhone.substring(3, 6) + "-" + clientPhone.substring(6, 10);
            }
        }
        this.clientPhone = clientPhone;
    }

    public String getClientPhone2() {
        if (clientPhone2 == null) {
            return "";
        }
        return clientPhone2;
    }

    public void setClientPhone2(String clientPhone2) {
        if (clientPhone2 != null && clientPhone2.length() > 20) {
            clientPhone2 = clientPhone2.substring(0, 19);
        }
        if (clientPhone2 != null) {
            clientPhone2 = clientPhone2.replaceAll("\\D", "");
            if (clientPhone2.length() >= 10) {
                clientPhone2 = clientPhone2.substring(0, 3) + "-" + clientPhone2.substring(3, 6) + "-" + clientPhone2.substring(6, 10);
            }
        }
        this.clientPhone2 = clientPhone2;
    }

    public String getClientFax() {
        if (clientFax == null) {
            return "";
        }
        return clientFax;
    }

    public void setClientFax(String clientFax) {
        if (clientFax != null && clientFax.length() > 20) {
            clientFax = clientFax.substring(0, 19);
        }
        if (clientFax != null) {
            clientFax = clientFax.replaceAll("\\D", "");
            if (clientFax.length() >= 10) {
                clientFax = clientFax.substring(0, 3) + "-" + clientFax.substring(3, 6) + "-" + clientFax.substring(6, 10);
            }
        }
        this.clientFax = clientFax;
    }

    public String getClientAddress() {
        try {
            if (getClientAddressObj() == null) {
                return "";
            }
            return getClientAddressObj().getAddress1();
        } catch (Exception e) {
            return "";
        }
    }

    public void setClientAddress(String clientAddress) {
        try {
            if (clientAddress != null && clientAddress.length() > 50) {
                clientAddress = clientAddress.substring(0, 49);
            }
            if (this.getClientAddressObj() == null) {
                this.setClientAddressObj(new Address());
            }
            this.getClientAddressObj().setAddress1(clientAddress);
        } catch (Exception e) {
        }
    }

    public String getClientAddress2() {
        try {
            if (getClientAddressObj() == null) {
                return "";
            }
            return getClientAddressObj().getAddress2();
        } catch (Exception e) {
            return "";
        }
    }

    public void setClientAddress2(String clientAddress2) {
        try {
            if (clientAddress2 != null && clientAddress2.length() > 50) {
                clientAddress2 = clientAddress2.substring(0, 49);
            }
            if (this.getClientAddressObj() == null) {
                this.setClientAddressObj(new Address());
            }
            this.getClientAddressObj().setAddress2(clientAddress2);
        } catch (Exception e) {
        }
    }

    public String getClientCity() {
        try {
            if (getClientAddressObj() == null) {
                return "";
            }
            return getClientAddressObj().getCity();
        } catch (Exception e) {
            return "";
        }
    }

    public void setClientCity(String clientCity) {
        try {
            if (clientCity != null && clientCity.length() > 50) {
                clientCity = clientCity.substring(0, 49);
            }
            this.getClientAddressObj().setCity(clientCity);
        } catch (Exception e) {
        }
    }

    public String getClientState() {
        try {
            if (getClientAddressObj() == null) {
                return "";
            }
            return getClientAddressObj().getState();
        } catch (Exception e) {
            return "";
        }
    }

    public void setClientState(String clientState) {
        try {
            if (clientState != null && clientState.length() > 2) {
                clientState = clientState.substring(0, 2);
            }
            if (clientState != null) {
                clientState = clientState.toUpperCase();
            }
            this.getClientAddressObj().setState(clientState);
        } catch (Exception e) {
        }
    }

    public String getClientZip() {
        try {
            if (getClientAddressObj() == null) {
                return "";
            }
            return getClientAddressObj().getZip();
        } catch (Exception e) {
            return "";
        }
    }

    public void setClientZip(String clientZip) {
        try {
            if (clientZip != null) {
                //clientZip = clientZip.replaceAll("\\D", "");
            }
            if (clientZip != null && clientZip.length() > 20) {
                clientZip = clientZip.substring(0, 19);
            }
            this.getClientAddressObj().setZip(clientZip);
        } catch (Exception e) {
        }
    }

    public int getManagementId() {
        return managementId;
    }

    public void setManagementId(int managementId) {
        this.managementId = managementId;
    }

    public Date getClientStartDate() {
        return clientStartDate;
    }

    public void setClientStartDate(Date clientStartDate) {
        if (clientStartDate == null) {
            SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
            try {
                clientStartDate = myFormat.parse("12/31/2100");
            } catch (Exception e) {
                clientStartDate = new Date();
            }
        }
        this.clientStartDate = clientStartDate;
    }

    public Date getClientEndDate() {
        return clientEndDate;
    }

    public void setClientEndDate(Date clientEndDate) {
        if (clientEndDate == null) {
            SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
            try {
                clientEndDate = myFormat.parse("12/31/2100");
            } catch (Exception e) {
                clientEndDate = new Date();
            }
        }
        this.clientEndDate = clientEndDate;
    }

    public boolean getDeleted() {
        boolean deleted = false;
        try {
            deleted = this.clientIsDeleted == 1;
        } catch (Exception exe) {
            deleted = false;
        }
        return deleted;
    }
    
    public void setDeleted(boolean del) {
        if (del) {
            this.clientIsDeleted = 1;
        } else {
            this.clientIsDeleted = 0;
        }
    }
    
    public short getClientIsDeleted() {
        return clientIsDeleted;
    }

    public void setClientIsDeleted(String clientIsDeleted) {
        short clientDel = 0;
        if (clientIsDeleted.equalsIgnoreCase("true")
                || clientIsDeleted.equalsIgnoreCase("yes")
                || clientIsDeleted.equalsIgnoreCase("1")) {
            clientDel = 1;
        }
        this.clientIsDeleted = clientDel;
    }

    public void setClientIsDeletedShort(short clientIsDeleted) {
        this.clientIsDeleted = clientIsDeleted;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int clientType) {
        this.clientType = clientType;
    }

    public Date getClientLastUpdated() {
        if (clientLastUpdated == null) {
            SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
            try {
                clientLastUpdated = myFormat.parse("12/31/2100");
            } catch (Exception e) {
                clientLastUpdated = new Date();
            }
        }
        return clientLastUpdated;
    }

    public void setClientLastUpdated(Date clientLastUpdated) {
        this.clientLastUpdated = clientLastUpdated;
    }

    public int getClientWorksite() {
        return clientWorksite;
    }

    public void setClientWorksite(int clientWorksite) {
        this.clientWorksite = clientWorksite;
    }

    public Double getClientTrainingTime() {
        return clientTrainingTime;
    }

    public void setClientTrainingTime(Double clientTrainingTime) {
        this.clientTrainingTime = clientTrainingTime;
    }

    public Boolean getClientBillForTraining() {
        return clientBillForTraining;
    }

    public void setClientBillForTraining(String clientBillForTraining) {
        boolean clientTraining = false;
        if (clientBillForTraining.equalsIgnoreCase("true")
                || clientBillForTraining.equalsIgnoreCase("yes")
                || clientBillForTraining.equalsIgnoreCase("1")) {
            clientTraining = true;
        }
        this.clientBillForTraining = clientTraining;
    }

    public void setClientBillForTrainingBoolean(Boolean clientBillForTraining) {
        this.clientBillForTraining = clientBillForTraining;
    }

    public Integer getClientWorksiteOrder() {
        return clientWorksiteOrder;
    }

    public void setClientWorksiteOrder(Integer clientWorksiteOrder) {
        this.clientWorksiteOrder = clientWorksiteOrder;
    }

    public Integer getRateCodeId() {
        return rateCodeId;
    }

    public void setRateCodeId(Integer rateCodeId) {
        this.rateCodeId = rateCodeId;
    }

    public String getClientBreak() {
        return clientBreak;
    }

    public void setClientBreak(String clientBreak) {
        this.clientBreak = clientBreak;
    }

    public Integer getStoreVolumeId() {
        return storeVolumeId;
    }

    public void setStoreVolumeId(Integer storeVolumeId) {
        this.storeVolumeId = storeVolumeId;
    }

    public Boolean getStoreRemoteMarketId() {
        return storeRemoteMarketId;
    }

    public void setStoreRemoteMarketId(Boolean storeRemoteMarketId) {
        this.storeRemoteMarketId = storeRemoteMarketId;
    }

    public void setContacts(ArrayList<ClientContact> contacts) {
        this.contacts = contacts;
    }

    public ArrayList<ClientContact> getContactsWOFetch() {
        if (this.contacts == null) {
            this.contacts = new ArrayList<ClientContact>();
        }
        return this.contacts;
    }

    public ArrayList<ClientContact> getContacts(String companyId) {
        if (this.contacts == null) {
            try {
                ClientContactControllerInterface clientContractController = ControllerRegistryAbstract.getClientContractController(companyId);
                this.contacts = clientContractController.getClientContracts(clientId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.contacts;
    }

    public ArrayList<ClientEquipment> getClientEquipment(String companyId) {
        if (this.clientEquipment == null) {
            try {
                EquipmentControllerInterface equipmentInterface = ControllerRegistryAbstract.getEquipmentController(companyId);
                this.clientEquipment = equipmentInterface.getClientEquipmentGroupedByMdn(-1, this.clientId, true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return this.clientEquipment;
    }
    
    public ArrayList<ClientRateCode> getClientRateCodes(String companyId) {
        if (this.contacts == null) {
            try {
                BillingControllerInterface billingController = ControllerRegistryAbstract.getBillingController(companyId);
                this.rateCodes = billingController.getClientRateCodes(this.clientId);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return this.rateCodes;
    }

    /**
     * Returns the parent worksite, returns null if this client does not have a
     * parent.
     *
     * @param companyId
     * @return Client or null
     */
    public Client getParentWorksite(String companyId) {
        if (this.parentSite == null && this.clientWorksite > 0) {
            try {
                ClientControllerInterface clientController = ControllerRegistryAbstract.getClientController(companyId);
                this.parentSite = clientController.getClientById(this.clientWorksite);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.parentSite;
    }

    public ClientExport getClientExport(String companyId) {
        if (this.clientExport == null) {
            try {
                ClientControllerInterface clientController = ControllerRegistryAbstract.getClientController(companyId);
                this.clientExport = clientController.getClientExport(this.clientId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.clientExport;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientId != null ? clientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Client)) {
            if (object instanceof String && this.getClientId().toString().equals(object)) {
                return true;
            }
            return false;
        }
        Client other = (Client) object;
        if ((this.clientId == null && other.clientId != null) || (this.clientId != null && !this.clientId.equals(other.clientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClientName();
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress1() {
        try {
            return this.getClientAddressObj().getAddress1();
        } catch (Exception e) {
            return "";
        }
    }

    public String getAddress2() {
        try {
            return this.getClientAddressObj().getAddress2();
        } catch (Exception e) {
            return "";
        }
    }

    public String getCity() {
        try {
            return this.getClientAddressObj().getCity();
        } catch (Exception e) {
            return "";
        }
    }

    public String getState() {
        try {
            return this.getClientAddressObj().getState();
        } catch (Exception e) {
            return "";
        }
    }

    public String getZip() {
        try {
            return this.getClientAddressObj().getZip();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isValid() {
        boolean retVal = true;
        if (this.getZip() == null) {
            retVal = false;
        } else {
            String testString = this.getZip().replaceAll("\\D+", "");
            if (testString.trim().length() == 0) {
                retVal = false;
            }
        }
        return retVal;
    }

    /**
     * @return the hasNotes
     */
    public Boolean getHasNotes() {
        if (hasNotes == null) {
            return false;
        }
        return hasNotes;
    }

    /**
     * @param hasNotes the hasNotes to set
     */
    public void setHasNotes(Boolean hasNotes) {
        this.hasNotes = hasNotes;
    }

    /**
     * @return the defaultNonBillable
     */
    public Boolean getDefaultNonBillable() {
        return defaultNonBillable;
    }

    /**
     * @param defaultNonBillable the defaultNonBillable to set
     */
    public void setDefaultNonBillable(Boolean defaultNonBillable) {
        this.defaultNonBillable = defaultNonBillable;
    }

    /**
     * @return the checkOutOptionId
     */
    public Integer getCheckOutOptionId() {
        return checkOutOptionId;
    }

    /**
     * @param checkOutOptionId the checkOutOptionId to set
     */
    public void setCheckOutOptionId(Integer checkOutOptionId) {
        this.checkOutOptionId = checkOutOptionId;
    }

    /**
     * @return the numMonths
     */
    public Integer getNumMonths() {
        return numMonths;
    }

    /**
     * @param numMonths the numMonths to set
     */
    public void setNumMonths(Integer numMonths) {
        this.numMonths = numMonths;
    }

    public Address getAddressObj() {
        return this.getClientAddressObj();
    }

    /**
     * @return the checkinFromPostPhone
     */
    public Boolean getCheckinFromPostPhone() {
        return checkinFromPostPhone;
    }

    /**
     * @param checkinFromPostPhone the checkinFromPostPhone to set
     */
    public void setCheckinFromPostPhone(Boolean checkinFromPostPhone) {
        this.checkinFromPostPhone = checkinFromPostPhone;
    }

    /**
     * @return the cUserName
     */
    public String getcUserName() {
        return cUserName;
    }

    /**
     * @param cUserName the cUserName to set
     */
    public void setcUserName(String cUserName) {
        this.cUserName = cUserName;
    }

    /**
     * @return the cPassword
     */
    public String getcPassword() {
        return cPassword;
    }

    /**
     * @param cPassword the cPassword to set
     */
    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }

    @Override
    public String getName() {
        return this.getClientName();
    }

    @Override
    public Integer getId() {
        return this.getClientId();
    }

    /**
     * @return the clientAddressObj
     */
    public Address getClientAddressObj() {
        return clientAddressObj;
    }

    /**
     * @param clientAddressObj the clientAddressObj to set
     */
    public void setClientAddressObj(Address clientAddressObj) {
        this.clientAddressObj = clientAddressObj;
    }

    /**
     * @return the dmAssociatedWithAccount
     */
    public User getDmAssociatedWithAccount() {
        return dmAssociatedWithAccount;
    }

    /**
     * @param dmAssociatedWithAccount the dmAssociatedWithAccount to set
     */
    public void setDmAssociatedWithAccount(User dmAssociatedWithAccount) {
        this.dmAssociatedWithAccount = dmAssociatedWithAccount;
    }

    /**
     * @return the notifyDmLateCheckoutMinutes
     */
    public Integer getNotifyDmLateCheckoutMinutes() {
        return notifyDmLateCheckoutMinutes;
    }

    /**
     * @param notifyDmLateCheckoutMinutes the notifyDmLateCheckoutMinutes to set
     */
    public void setNotifyDmLateCheckoutMinutes(Integer notifyDmLateCheckoutMinutes) {
        this.notifyDmLateCheckoutMinutes = notifyDmLateCheckoutMinutes;
    }

    /**
     * @return the logIntoRoute
     */
    public Boolean getLogIntoRoute() {
        return logIntoRoute;
    }

    /**
     * @param logIntoRoute the logIntoRoute to set
     */
    public void setLogIntoRoute(Boolean logIntoRoute) {
        this.logIntoRoute = logIntoRoute;
    }

    /**
     * @return the uskedIdentifier
     */
    public String getUskedIdentifier() {
        return uskedIdentifier;
    }

    /**
     * @param uskedIdentifier the uskedIdentifier to set
     */
    public void setUskedIdentifier(String uskedIdentifier) {
        this.uskedIdentifier = uskedIdentifier;
    }

    /**
     * @return the lastRating
     */
    public Integer getLastRating() {
        return lastRating;
    }

    /**
     * @param lastRating the lastRating to set
     */
    public void setLastRating(Integer lastRating) {
        this.lastRating = lastRating;
    }

    /**
     * @return the clientIndustry
     */
    public String getClientIndustry() {
        return clientIndustry;
    }

    /**
     * @param clientIndustry the clientIndustry to set
     */
    public void setClientIndustry(String clientIndustry) {
        this.clientIndustry = clientIndustry;
    }

    /**
     * @return the nonCachedContacts
     */
    public ArrayList<ClientContact> getNonCachedContacts() {
        if (nonCachedContacts == null) {
            nonCachedContacts = new ArrayList<ClientContact>();
        }
        return nonCachedContacts;
    }

    /**
     * @param nonCachedContacts the nonCachedContacts to set
     */
    public void setNonCachedContacts(ArrayList<ClientContact> nonCachedContacts) {
        this.nonCachedContacts = nonCachedContacts;
    }

    /**
     * @return the displayClientInCallQueue
     */
    public Boolean getDisplayClientInCallQueue() {
        return displayClientInCallQueue;
    }

    /**
     * @param displayClientInCallQueue the displayClientInCallQueue to set
     */
    public void setDisplayClientInCallQueue(Boolean displayClientInCallQueue) {
        this.displayClientInCallQueue = displayClientInCallQueue;
    }

    /**
     * @return the assignedClientEquipmentIds
     */
    public ArrayList<Integer> getAssignedClientEquipmentIds() {
        return assignedClientEquipmentIds;
    }

    /**
     * @param assignedClientEquipmentIds the assignedClientEquipmentIds to set
     */
    public void setAssignedClientEquipmentIds(ArrayList<Integer> assignedClientEquipmentIds) {
        this.assignedClientEquipmentIds = assignedClientEquipmentIds;
    }

    /**
     * @return the reportTime
     */
    public String getReportTime() {
        return reportTime;
    }

    /**
     * @param reportTime the reportTime to set
     */
    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }
}
