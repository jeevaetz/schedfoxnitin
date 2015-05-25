//  package declaration
package rmischedule.messaging.client_email.models;

//  import declarations
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JRadioButton;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContact;
import rmischeduleserver.mysqlconnectivity.queries.messaging.client_email.get_clients_of_branch_contact_data;

/**
 *  This class is designed to represent a branch in the Client_Email system.
 *      It contains the specific branch name and id, as well as a data structure
 *      containing {@code ClientOfBranchContactData}
 * @author jdavis
 */
public final class BranchContactData extends JRadioButton implements Comparable<BranchContactData> {
    /*  private variable declarations   */

    private static final String DATABASE_COMPANY_ID = "2";  //  assigned to 2 for Champion National Security Only
    private int branchId;
    private String branchName;
    Map<Integer, Client> clientMap;

    /*  private method implementations  */
    /**
     *  This method loads all relevant client information per branch into
     *      a linked list, sorts the list, then places them within {@code clientMap}
     *  @return returnMap a map containing all clients per branch
     */
    private LinkedHashMap<Integer, Client> getClients() {
        LinkedHashMap<Integer, Client> returnMap = new LinkedHashMap<Integer, Client>();
        Connection myConnection = new Connection();
        Record_Set rs = null;
        get_clients_of_branch_contact_data query = new get_clients_of_branch_contact_data();
        query.setPreparedStatement(new Object[]{
                    this.branchId
                });
        myConnection.setCompany(DATABASE_COMPANY_ID);
        myConnection.setBranch(Integer.toString(this.branchId));
        myConnection.prepQuery(query);

        try {
            rs = myConnection.executeQuery(query);
            if (rs.length() > 0) {
                do {
                    int clientId = rs.getInt("clientid");
                    if (returnMap.get(clientId) == null) {
                        Client myClient = new Client(new Date(), rs);
                        returnMap.put(clientId, myClient);
                    }
                    Client myClient = returnMap.get(clientId);
                    int clientContactId = rs.getInt("client_contact_id");
                    if (clientContactId != -1) {
                        ClientContact clientContact = new ClientContact(rs);
                        clientContact.setClient(myClient);
                        myClient.getContactsWOFetch().add(clientContact);
                    }
                } while (rs.moveNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("An error has occurred parsing the record set for clients.");
        }

        return returnMap;
    }

    /**
     *  Default construction of this object not allowed.
     *  @throws UnsupportedOperationException
     */
    private BranchContactData() {
        throw new UnsupportedOperationException("Default Construction of this object not allowed.");
    }

    /*  public method implementations   */
    public BranchContactData(int branchId, String branchName) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.clientMap = this.getClients();
    }

    /*  mutators    */
    public int getBranchId() {
        return this.branchId;
    }

    public String getBranchName() {
        return this.branchName;
    }

    public Map<Integer, Client> getClientMap() {
        return this.clientMap;
    }

    /**
     *  This method calls each {@code ClientOfBranchContactData} and tells it
     *      to clear their maps.
     */
    public void clearClientOfBranchContactMaps() {
        Collection<Client> collection = this.clientMap.values();
        for (Client element : collection) {
            element.getContactsWOFetch().clear();
        }

        this.clientMap.clear();
    }

    /**
     *  This method calls upon {@code ClientofBranchContactData.fillSendDataStructures}
     *      contained within {@code this.clientMap}
     *  @param contactList a List of {@code Contact_Data}
     *  @param isActiveSelected a boolean describing if the Select Active Button has been pressed
     *  @param isInactiveSelected a boolean describing if the Select Inactive Button has been pressed
     */
    public void fillSendDataStructures(List<ClientContact> contactList, boolean isActiveSelected, boolean isInactiveSelected) {
        Collection<Client> collection = this.clientMap.values();

        if (isActiveSelected && isInactiveSelected) {
            for (Client client : collection) {
                for (ClientContact clientContact : client.getContactsWOFetch()) {
                    contactList.add(clientContact);
                }
            }
        }
        if (isActiveSelected && !isInactiveSelected) {
            for (Client client : collection) {
                if (client.getClientIsDeleted() != 1) {
                    for (ClientContact clientContact : client.getContactsWOFetch()) {
                        contactList.add(clientContact);
                    }
                }
            }
        }
        if (!isActiveSelected && isInactiveSelected) {
            for (Client client : collection) {
                if (client.getClientIsDeleted() == 1) {
                    for (ClientContact clientContact : client.getContactsWOFetch()) {
                        contactList.add(clientContact);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return this.branchName;
    }

    public int compareTo(BranchContactData t) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.branchName, t.branchName);
    }

    @Override
    public boolean equals(Object obj) {
        /*
         *  Method checks branch_id -> branch name.  Duplicate entries should
         *      not occur unless there has been a data error at the database
         *      level.
         */
        if (!(obj instanceof BranchContactData)) {
            return false;
        }

        final BranchContactData other = (BranchContactData) obj;

        return (this.branchId == other.branchId //  test branch id
                && (this.branchName == null ? other.branchName == null : this.branchName.equals(other.branchName)) //  test branch name
                );
    }

    @Override
    public int hashCode() {
        int hash = 71;

        hash = 71 * hash + this.branchId;
        hash = 71 * hash + (this.branchName != null ? this.branchName.hashCode() : 0);

        return hash;
    }
};
