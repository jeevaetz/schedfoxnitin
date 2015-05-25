//  package declaration
package rmischedule.messaging.client_email.models;

//  import declarations
/**
 *  A simple data object designed to hold invalid email addresses, and
 *      relevant contact information about them.
 *  <p><b>NOTE: </b> this object uses a {@code static Builder} object for construction
 *      due to the number of parameters.
 *  @author Jeffrey N. Davis
 *  @see rmischedule.messaging.client_email.controllers.Client_Email_Controller
 *  @since 03/25/2011
 */
public class Invalid_Email_Data implements Comparable<Invalid_Email_Data> {
    /* object variable definitions  */

    private String emailAddress;
    private String branchName;
    private String clientName;
    private String activeClient;
    private String contactPrimary;
    private String clientPhone;
    private String contactPhone;
    private String contactTitle;
    private String contactName;

    /**
     *  Default instantiation of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    public Invalid_Email_Data() {
    }

    /*  public method implementations   */
    /**
     *  Method constructs the message to print to the report file, returns it
     *  @return reportLine a string representing the line to be printed to the report
     */
    public String getReportLine() {
        StringBuilder reportLine = new StringBuilder();
        int sbLargeLength = 35;
        int sbSmallLength = 25;
        char tab = '\t';
        reportLine.setLength(0);

        StringBuilder emailAddressSb = new StringBuilder();
        emailAddressSb.append("Email Address:  ");
        emailAddressSb.append(this.emailAddress);
        emailAddressSb.setLength(sbLargeLength);
        StringBuilder contactNameSb = new StringBuilder();
        contactNameSb.append("Contact Name:  ");
        contactNameSb.append(this.contactName);
        contactNameSb.setLength(sbLargeLength);
        StringBuilder contactTitleSb = new StringBuilder();
        contactTitleSb.append("Contact Title:  ");
        contactTitleSb.append(this.contactTitle);
        contactTitleSb.setLength(sbLargeLength);
        StringBuilder contactPrimarySb = new StringBuilder();
        contactPrimarySb.append("Contact Primary:  ");
        contactPrimarySb.append(this.contactPrimary);
        contactPrimarySb.setLength(sbLargeLength);
        StringBuilder activeClientSb = new StringBuilder();
        activeClientSb = new StringBuilder();
        activeClientSb.append("Active Client:  ");
        activeClientSb.append(this.activeClient);
        activeClientSb.setLength(sbSmallLength);
        StringBuilder clientNameSb = new StringBuilder();
        clientNameSb.append("Client Name:  ");
        clientNameSb.append(this.clientName);
        clientNameSb.setLength(sbLargeLength);
        StringBuilder branchNameSb = new StringBuilder();
        branchNameSb.append("Branch Name:  ");
        branchNameSb.append(this.branchName);
        branchNameSb.setLength(sbSmallLength);

        reportLine.append(emailAddressSb);
        reportLine.append(tab);
        reportLine.append(contactNameSb);
        reportLine.append(tab);
        reportLine.append(clientNameSb);
        reportLine.append(tab);
        reportLine.append(branchNameSb);
        reportLine.append(tab);
        reportLine.append(activeClientSb);
        reportLine.append(tab);
        reportLine.append(contactTitleSb);
        reportLine.append(tab);
        reportLine.append(contactPrimarySb);

        return reportLine.toString();
    }

    /*  accessors   */
    public String getActiveClient() {
        return activeClient;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getContactPrimary() {
        return contactPrimary;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    /*  methods common to all objects   */
    @Override
    public String toString() {
        return "Branch:  " + this.branchName + "  Client:  " + this.clientName
                + "  Email Address:  " + emailAddress + "  Contact Name:  " + this.contactName;
    }

    @Override
    public int compareTo(Invalid_Email_Data other) {
        //  check branch
        int branch = String.CASE_INSENSITIVE_ORDER.compare(this.branchName, other.branchName);
        if (branch != 0) {
            return branch;
        }

        //  check client
        int client = String.CASE_INSENSITIVE_ORDER.compare(this.clientName, other.clientName);
        if (client != 0) {
            return client;
        }

        //  check name
        int name = String.CASE_INSENSITIVE_ORDER.compare(this.contactName, other.contactName);
        if (name != 0) {
            return name;
        }

        //  check email address
        return String.CASE_INSENSITIVE_ORDER.compare(this.emailAddress, other.emailAddress);
    }

    @Override
    public boolean equals(Object obj) {
        /*
         *  Method checks emailAddres -> contactName -> clientName -> contactTitle
         *      -> contactPhone -> clientPhone -> activeClient -> contactPrimary ->
         *      branchName
         */
        if (!(obj instanceof Invalid_Email_Data)) {
            return false;
        }

        final Invalid_Email_Data other = (Invalid_Email_Data) obj;

        return ((this.emailAddress == null ? other.emailAddress == null : this.emailAddress.equals(other.emailAddress)) //  test emailAddress
                && (this.contactName == null ? other.contactName == null : this.contactName.equals(other.contactName)) //  test contactName
                && (this.clientName == null ? other.clientName == null : this.clientName.equals(other.clientName)) //  test clientName
                && (this.contactTitle == null ? other.contactTitle == null : this.contactTitle.equals(other.contactTitle)) //  test contactTitle
                && (this.contactPhone == null ? other.contactPhone == null : this.contactPhone.equals(other.contactPhone)) //  test contactPhone
                && (this.clientPhone == null ? other.clientPhone == null : this.clientPhone.equals(other.clientPhone)) //  test clientPhone
                && (this.activeClient == null ? other.activeClient == null : this.activeClient.equals(other.activeClient)) //  test activeClient
                && (this.contactPrimary == null ? other.contactPrimary == null : this.contactPrimary.equals(other.contactPrimary)) //  test contactPrimary
                && (this.branchName == null ? other.branchName == null : this.branchName.equals(other.branchName)) //  test branchName
                );
    }

    @Override
    public int hashCode() {
        int hash = 71;

        hash = 71 * hash + (this.emailAddress != null ? this.emailAddress.hashCode() : 0);
        hash = 71 * hash + (this.contactName != null ? this.contactName.hashCode() : 0);
        hash = 71 * hash + (this.clientName != null ? this.clientName.hashCode() : 0);
        hash = 71 * hash + (this.clientName != null ? this.clientName.hashCode() : 0);
        hash = 71 * hash + (this.contactTitle != null ? this.contactTitle.hashCode() : 0);
        hash = 71 * hash + (this.contactPhone != null ? this.contactPhone.hashCode() : 0);
        hash = 71 * hash + (this.clientPhone != null ? this.clientPhone.hashCode() : 0);
        hash = 71 * hash + (this.activeClient != null ? this.activeClient.hashCode() : 0);
        hash = 71 * hash + (this.contactPrimary != null ? this.contactPrimary.hashCode() : 0);
        hash = 71 * hash + (this.branchName != null ? this.branchName.hashCode() : 0);

        return hash;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @param branchName the branchName to set
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @param activeClient the activeClient to set
     */
    public void setActiveClient(String activeClient) {
        this.activeClient = activeClient;
    }

    /**
     * @param contactPrimary the contactPrimary to set
     */
    public void setContactPrimary(String contactPrimary) {
        this.contactPrimary = contactPrimary;
    }

    /**
     * @param clientPhone the clientPhone to set
     */
    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    /**
     * @param contactPhone the contactPhone to set
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * @param contactTitle the contactTitle to set
     */
    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    /**
     * @param contactName the contactName to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
};
