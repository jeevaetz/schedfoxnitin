/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.ClientContactControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author user
 */
public class ClientContact implements Serializable, Comparable, EmailContact {

    private static final long serialVersionUID = 1L;
    private Integer clientContactId;
    private int clientId;
    private Integer clientContactType;
    private int clientContactIsPrimary;
    private String clientContactTitle;
    private String clientContactFirstName;
    private String clientContactLastName;
    private String clientContactMiddleName;
    private String clientContactAddress;
    private String clientContactAddress2;
    private String clientContactCity;
    private String clientContactState;
    private String clientContactZip;
    private String clientContactPhone;
    private String clientContactCell;
    private String clientContactFax;
    private String clientContactEmail;
    private String clientContactPager;
    private short clientContactIsDeleted;
    private Boolean clientContactEmailOnLogin;
    private Boolean clientContactEmailOnIncident;
    private Boolean clientContactIncludeMassEmail;
    private Boolean clientContactIncludeDailyOdr;
    private Boolean clientContactIncludeDailyTracking;
    
    private ClientContactType clientContactType1;

    private Client client;

    public ClientContact() {
    }

    public ClientContact(Record_Set rst) {
        if (rst.hasColumn("id")) {
            this.clientContactId = rst.getInt("id");
        } else {
            this.clientContactId = rst.getInt("client_contact_id");
        }
        if (rst.hasColumn("client_id")) {
            this.clientId = rst.getInt("client_id");
        }
        if (rst.hasColumn("client_contact_type")) {
            this.clientContactType = rst.getInt("client_contact_type");
        }
        if (rst.hasColumn("fname")) {
            this.clientContactFirstName = rst.getString("fname");
        } else {
            this.clientContactFirstName = rst.getString("client_contact_first_name");
        }
        if (rst.hasColumn("lname")) {
            this.clientContactLastName = rst.getString("lname");
        } else {
            this.clientContactLastName = rst.getString("client_contact_last_name");
        }
        if (rst.hasColumn("mname")) {
            this.clientContactMiddleName = rst.getString("mname");
        } else {
            this.clientContactMiddleName = rst.getString("client_contact_middle_name");
        }
        if (rst.hasColumn("phone")) {
            this.clientContactPhone = rst.getString("phone");
        } else {
            this.clientContactPhone = rst.getString("client_contact_phone");
        }
        if (rst.hasColumn("cell")) {
            this.clientContactCell = rst.getString("cell");
        } else {
            this.clientContactCell = rst.getString("client_contact_cell");
        }
        if (rst.hasColumn("fax")) {
            this.clientContactFax = rst.getString("fax");
        } else {
            this.clientContactFax = rst.getString("client_contact_fax");
        }
        if (rst.hasColumn("email")) {
            this.clientContactEmail = rst.getString("email");
        } else {
            this.clientContactEmail = rst.getString("client_contact_email");
        }
        this.clientContactPager = rst.getString("client_contact_pager");
        this.clientContactIsPrimary = rst.getInt("isprimary");
        this.clientContactAddress = rst.getString("address1");
        this.clientContactAddress2 = rst.getString("address2");
        this.clientContactCity = rst.getString("city");
        this.clientContactState = rst.getString("state");
        this.clientContactZip = rst.getString("zip");
        this.clientContactType = rst.getInt("type");
        if (rst.hasColumn("title")) {
            this.clientContactTitle = rst.getString("title");
        } else {
            this.clientContactTitle = rst.getString("client_contact_title");
        }
        this.clientContactEmailOnLogin = rst.getBoolean("client_contact_email_on_login");
        this.clientContactEmailOnIncident = rst.getBoolean("client_contact_email_on_incident");
        try {
            this.clientContactIncludeMassEmail = rst.getBoolean("client_contact_include_mass_email");
        } catch (Exception exe) {
            this.clientContactIncludeMassEmail = true;
        }
        try {
            this.clientContactIncludeDailyOdr = rst.getBoolean("client_contact_include_daily_odr");
        } catch (Exception exe) {
            this.clientContactIncludeDailyOdr = false;
        }
        try {
            this.clientContactIncludeDailyTracking = rst.getBoolean("client_contact_include_daily_tracking");
        } catch (Exception exe) {
            this.clientContactIncludeDailyTracking = false;
        }
    }

    public ClientContact(Integer clientContactId) {
        this.clientContactId = clientContactId;
    }

    public ClientContact(Integer clientContactId, int clientId, Integer clientContactType, int clientContactIsPrimary, String clientContactTitle, String clientContactFirstName, String clientContactLastName, String clientContactMiddleName, String clientContactAddress, String clientContactAddress2, String clientContactCity, String clientContactState, String clientContactZip, String clientContactPhone, String clientContactCell, String clientContactFax, String clientContactEmail, short clientContactIsDeleted) {
        this.clientContactId = clientContactId;
        this.clientId = clientId;
        this.clientContactType = clientContactType;
        this.clientContactIsPrimary = clientContactIsPrimary;
        this.clientContactTitle = clientContactTitle;
        this.clientContactFirstName = clientContactFirstName;
        this.clientContactLastName = clientContactLastName;
        this.clientContactMiddleName = clientContactMiddleName;
        this.clientContactAddress = clientContactAddress;
        this.clientContactAddress2 = clientContactAddress2;
        this.clientContactCity = clientContactCity;
        this.clientContactState = clientContactState;
        this.clientContactZip = clientContactZip;
        this.clientContactPhone = clientContactPhone;
        this.clientContactCell = clientContactCell;
        this.clientContactFax = clientContactFax;
        this.clientContactEmail = clientContactEmail;
        this.clientContactIsDeleted = clientContactIsDeleted;
    }

    public Integer getClientContactId() {
        return clientContactId;
    }

    public void setClientContactId(Integer clientContactId) {
        this.clientContactId = clientContactId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Integer getClientContactType() {
        return clientContactType;
    }

    public void setClientContactType(Integer clientContactType) {
        this.clientContactType = clientContactType;
    }

    public int getClientContactIsPrimary() {
        return clientContactIsPrimary;
    }

    public void setClientContactIsPrimary(int clientContactIsPrimary) {
        this.clientContactIsPrimary = clientContactIsPrimary;
    }

    public String getClientContactTitle() {
        return clientContactTitle;
    }

    public void setClientContactTitle(String clientContactTitle) {
        this.clientContactTitle = clientContactTitle;
    }

    public String getClientContactFirstName() {
        return clientContactFirstName;
    }

    public void setClientContactFirstName(String clientContactFirstName) {
        this.clientContactFirstName = clientContactFirstName;
    }

    public String getClientContactLastName() {
        return clientContactLastName;
    }

    public void setClientContactLastName(String clientContactLastName) {
        this.clientContactLastName = clientContactLastName;
    }

    public String getClientContactMiddleName() {
        return clientContactMiddleName;
    }

    public void setClientContactMiddleName(String clientContactMiddleName) {
        this.clientContactMiddleName = clientContactMiddleName;
    }

    public String getClientContactAddress() {
        return clientContactAddress;
    }

    public void setClientContactAddress(String clientContactAddress) {
        this.clientContactAddress = clientContactAddress;
    }

    public String getClientContactAddress2() {
        return clientContactAddress2;
    }

    public void setClientContactAddress2(String clientContactAddress2) {
        this.clientContactAddress2 = clientContactAddress2;
    }

    public String getClientContactCity() {
        return clientContactCity;
    }

    public void setClientContactCity(String clientContactCity) {
        this.clientContactCity = clientContactCity;
    }

    public String getClientContactState() {
        return clientContactState;
    }

    public void setClientContactState(String clientContactState) {
        this.clientContactState = clientContactState;
    }

    public String getClientContactZip() {
        return clientContactZip;
    }

    public void setClientContactZip(String clientContactZip) {
        this.clientContactZip = clientContactZip;
    }

    public String getClientContactPhone() {
        return clientContactPhone;
    }

    public void setClientContactPhone(String clientContactPhone) {
        this.clientContactPhone = clientContactPhone;
    }

    public String getClientContactCell() {
        return clientContactCell;
    }

    public void setClientContactCell(String clientContactCell) {
        this.clientContactCell = clientContactCell;
    }

    public String getClientContactFax() {
        return clientContactFax;
    }

    public void setClientContactFax(String clientContactFax) {
        this.clientContactFax = clientContactFax;
    }

    public String getClientContactEmail() {
        return clientContactEmail;
    }

    public void setClientContactEmail(String clientContactEmail) {
        this.clientContactEmail = clientContactEmail;
    }

    public short getClientContactIsDeleted() {
        return clientContactIsDeleted;
    }

    public void setClientContactIsDeleted(short clientContactIsDeleted) {
        this.clientContactIsDeleted = clientContactIsDeleted;
    }

    public ClientContactType getClientContactTypeObj(String companyId) {
        if (this.clientContactType1 == null) {
            ClientContactControllerInterface clientContractController = ControllerRegistryAbstract.getClientContractController(companyId);
            try {
                clientContactType1 = clientContractController.getClientContactById(clientContactType);
            } catch (Exception e) {
                clientContactType1 = new ClientContactType();
            }
        }
        return clientContactType1;
    }

    public void setClientContactType1(ClientContactType clientContactType1) {
        this.clientContactType1 = clientContactType1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientContactId != null ? clientContactId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientContact)) {
            return false;
        }
        ClientContact other = (ClientContact) object;
        if ((this.clientContactId == null && other.clientContactId != null) || (this.clientContactId != null && !this.clientContactId.equals(other.clientContactId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ClientContact[clientContactId=" + clientContactId + "]";
    }

    public int compareTo(Object o) {
        int retVal = 1;
        if (o instanceof ClientContact) {
            ClientContact compContact = (ClientContact)o;
            return this.clientContactFirstName.compareTo(compContact.clientContactFirstName);
        }
        return retVal;
    }

    public String getFullName() {
        if ((this.getClientContactFirstName() + " " + this.getClientContactLastName()).trim().length() == 0) {
             return this.getClientContactMiddleName() + " " + this.getClientContactLastName();
        } else {
            return this.getClientContactFirstName() + " " + this.getClientContactLastName();
        }
    }

    public String getEmailAddress() {
        return this.getClientContactEmail();
    }

    public int getPrimaryId() {
        return this.getClientContactId();
    }

    public String getType() {
        return "ClientContact";
    }

    /**
     * @return the clientContactPager
     */
    public String getClientContactPager() {
        return clientContactPager;
    }

    /**
     * @param clientContactPager the clientContactPager to set
     */
    public void setClientContactPager(String clientContactPager) {
        this.clientContactPager = clientContactPager;
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

    /**
     * @return the clientContactEmailOnLogin
     */
    public Boolean getClientContactEmailOnLogin() {
        return clientContactEmailOnLogin;
    }

    /**
     * @param clientContactEmailOnLogin the clientContactEmailOnLogin to set
     */
    public void setClientContactEmailOnLogin(Boolean clientContactEmailOnLogin) {
        this.clientContactEmailOnLogin = clientContactEmailOnLogin;
    }

    /**
     * @return the clientContactEmailOnIncident
     */
    public Boolean getClientContactEmailOnIncident() {
        return clientContactEmailOnIncident;
    }

    /**
     * @param clientContactEmailOnIncident the clientContactEmailOnIncident to set
     */
    public void setClientContactEmailOnIncident(Boolean clientContactEmailOnIncident) {
        this.clientContactEmailOnIncident = clientContactEmailOnIncident;
    }

    /**
     * @return the clientContactIncludeMassEmail
     */
    public Boolean getClientContactIncludeMassEmail() {
        return clientContactIncludeMassEmail;
    }

    /**
     * @param clientContactIncludeMassEmail the clientContactIncludeMassEmail to set
     */
    public void setClientContactIncludeMassEmail(Boolean clientContactIncludeMassEmail) {
        this.clientContactIncludeMassEmail = clientContactIncludeMassEmail;
    }

    /**
     * @return the clientContactIncludeDailyOdr
     */
    public Boolean getClientContactIncludeDailyOdr() {
        return clientContactIncludeDailyOdr;
    }

    /**
     * @param clientContactIncludeDailyOdr the clientContactIncludeDailyOdr to set
     */
    public void setClientContactIncludeDailyOdr(Boolean clientContactIncludeDailyOdr) {
        this.clientContactIncludeDailyOdr = clientContactIncludeDailyOdr;
    }

    /**
     * @return the clientContactIncludeDailyTracking
     */
    public Boolean getClientContactIncludeDailyTracking() {
        return clientContactIncludeDailyTracking;
    }

    /**
     * @param clientContactIncludeDailyTracking the clientContactIncludeDailyTracking to set
     */
    public void setClientContactIncludeDailyTracking(Boolean clientContactIncludeDailyTracking) {
        this.clientContactIncludeDailyTracking = clientContactIncludeDailyTracking;
    }
}
