//  package declaration
package rmischedule.messaging.client_email.models;

//  import declarations
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.messaging.client_email.get_client_contact_data;

/**
 *  This object is designed to hold general contact information about a client.
 *      It is held by {@code BranchContactData}.  This object also contains
 *      a data structure containing {@code ClientContactData Objects}.
 *  <p><b>NOTE: </b> this object uses a {@code static Builder} object for construction
 *      due to the number of parameters.  Proper construction is checked;
 *      if object is not valid, a {@code RuntimeOperationException} is thrown
 *      detailing the issue.
 *  @author Jeffrey N. Davis
 *  @see rmischedule.messaging.client_email.models.BranchContactData
 *  @see rmischedule.messaging.client_email.models.ClientContactData
 *  @see rmischedule.messaging.client_email.controllers.Client_Email_Controller
 *  @since 03/24/2011
 */
public class ClientOfBranchContactData implements Comparable<ClientOfBranchContactData> {
    /*  private variable declarations   */

    private static String DATABASE_COMPANY_ID = "2";  //  assigned to 2 for Champion National Security Only
    private int clientId;
    private int branchId;
    private String clientName;
    private String clientPhonePrimary;
    private String clientPhoneSecondary;
    private String clientAddressPrimary;
    private String clientAddressSecondary;
    private String clientCity;
    private String clientState;
    private String clientZip;
    private boolean isClientActive;
    private Map<Integer, ClientContactData> contactMap;

    /**
     *  Initializes this object by assigning member variables to corresponding
     *      {@code Builder} variables.  After assignment, each variable is checked
     *      for validity
     *  @param builder an instance of {@code Builder}
     */
    public ClientOfBranchContactData() {
        /*  set object variables    */

        try {
            this.areParamsValid();
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("This object was not constructed in a valid state.");
        }

        this.contactMap = this.getContacts();
    }

    /**
     *  Checks to ensure the object has been constructed in a valid state for use.
     *      It is possible that some fields such as {@code this.clientAddressSecondary
     *      or this.clientPhonePrimary} will not have valid data in the database.
     *  <p><b>NOTE:  </b> checks {@code clientId -> clientName} for validity
     *  @throws RuntimeException if either {@code clientId || clientName} is invalid
     */
    private void areParamsValid() throws RuntimeException {
        if (this.clientId < 1 || this.clientName.length() == 0) {
            throw new RuntimeException();
        }
    }

    /**
     *  This method loads all contact information per client contact per client.
     *      All instances of {@code ClientContactData} are loaded into and unsorted
     *      linked list, then sorted, then placed into a LinkedHashMap to be be returned
     *  @return returnMap a valid {@code LinkedHashMap} to assign to {@code contactMap}
     */
    private LinkedHashMap<Integer, ClientContactData> getContacts() {
        List<ClientContactData> unsortedList = new LinkedList<ClientContactData>();
        LinkedHashMap<Integer, ClientContactData> returnMap = new LinkedHashMap<Integer, ClientContactData>();
        Connection myConnection = new Connection();
        Record_Set rs = null;
        get_client_contact_data query = new get_client_contact_data(this.clientId);
        myConnection.setCompany(DATABASE_COMPANY_ID);
        myConnection.setBranch(Integer.toString(this.branchId));
        myConnection.prepQuery(query);

        /*  execute query, load unsorted list   */
        try {
            rs = myConnection.executeQuery(query);
            if (rs.length() > 0) {
                do {
                    int clientContactId = rs.getInt("clientcontactid");
                    int rsClientId = rs.getInt("clientid");
                    String clientContactTitle = rs.getString("clientcontacttitle");
                    String contactFirstName = rs.getString("clientcontactfirstname");
                    String contactLastName = rs.getString("clientcontactlastname");
                    String contactPhone = rs.getString("clientcontactphone");
                    String contactCell = rs.getString("clientcontactcell");
                    String contactEmailAddress = rs.getString("clientcontactemail");
                    boolean isContactPrimary = (rs.getInt("isprimary") == 0) ? false : true;

                    ClientContactData element = new ClientContactData.Builder(clientContactId, isContactPrimary).clientId(rsClientId).clientContactTitle(clientContactTitle).contactFirstName(contactFirstName).contactLastName(contactLastName).contactPhone(contactPhone).contactCell(contactCell).contactEmailAddress(contactEmailAddress).build();

                    unsortedList.add(element);
                } while (rs.moveNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*  sort list, load class data structure    */
        Collections.sort(unsortedList);
        for (ClientContactData element : unsortedList) {
            returnMap.put(element.hashCode(), element);
        }

        return returnMap;
    }

    /**
     *  This method nulls out all references contained within the LinkedHashMap
     */
    protected void clearClientContactData() {
        this.contactMap.clear();
    }

    /**
     *  This method fills both {@code validList<Valid_Email_Data> invalidList<Invalid_Email_Data>
     *      from data held within {@code this.contactMap}
     *  @param contactList a List of {@code contactData}
     *  @param branchName a string describing the name of the branch
     *  @param isClientActive a boolean describing it the client is active
     */
    protected void fillSendDataStructures(List<Contact_Data> contactList, String branchName, boolean isClientActive) {
        Collection<ClientContactData> collection = this.contactMap.values();
        for (ClientContactData element : collection) {
            String activeClient = isClientActive ? "ACTIVE" : "INACTIVE";
            String contactPrimary = element.isIsContactPrimary() ? "PRIMARY" : "NOT PRIMARY";
            Contact_Data data = new Contact_Data.Builder(element.getContactEmailAddress()).branchName(branchName).clientName(this.clientName).activeClient(activeClient).contactPrimary(contactPrimary).clientPhone(this.getClientPhonePrimary()).contactPhone(element.getContactPhone()).contactTitle(element.getClientContactTitle()).contactName(element.getContactFirstName() + " " + element.getContactLastName()).build();
            contactList.add(data);
        }
    }

    /*  public method implementations   */
    /*  mutators    */
    public String getClientAddressPrimary() {
        return clientAddressPrimary;
    }

    public String getClientAddressSecondary() {
        return clientAddressSecondary;
    }

    public String getClientCity() {
        return clientCity;
    }

    public int getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPhonePrimary() {
        return clientPhonePrimary;
    }

    public String getClientPhoneSecondary() {
        return clientPhoneSecondary;
    }

    public String getClientState() {
        return clientState;
    }

    public String getClientZip() {
        return clientZip;
    }

    public boolean isIsClientActive() {
        return isClientActive;
    }

    public Map<Integer, ClientContactData> getContactMap() {
        return this.contactMap;
    }

    @Override
    public String toString() {
        return this.clientName;
    }

    @Override
    public int compareTo(ClientOfBranchContactData t) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.clientName, t.clientName);
    }

    @Override
    public boolean equals(Object obj) {
        /*
         *  Method checks clientId -> clientName -> clientAddressPrimary
         */
        if (!(obj instanceof ClientOfBranchContactData)) {
            return false;
        }

        final ClientOfBranchContactData other = (ClientOfBranchContactData) obj;

        return (this.clientId == other.clientId //  test client id
                && (this.clientName == null ? other.clientName == null : this.clientName.equals(other.clientName)) //  test client name
                && (this.clientAddressPrimary == null ? other.clientAddressPrimary == null : this.clientAddressPrimary.equals(other.clientAddressPrimary)) //  test client address primary
                );
    }

    @Override
    public int hashCode() {
        int hash = 71;

        hash = 71 * hash + this.clientId;
        hash = 71 * hash + (this.clientName != null ? this.clientName.hashCode() : 0);
        hash = 71 * hash + (this.clientAddressPrimary != null ? this.clientAddressPrimary.hashCode() : 0);

        return hash;
    }
};
