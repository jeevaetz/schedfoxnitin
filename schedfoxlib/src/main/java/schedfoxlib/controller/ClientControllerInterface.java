/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.*;

/**
 *
 * @author user
 */
public interface ClientControllerInterface {
    
    public ArrayList<ClientRoute> getRoutesAssociatedWithClientWithMinimalClientData(Integer clientId) throws RetrieveDataException;
    
    public ArrayList<CrossCompanyClient> getCrossSchemaPatrolProClientsByLogin(String userName, String password) throws RetrieveDataException;
    
    public ArrayList<Client> getClientsByBranchWithDeviceInfo(Integer branchId, Boolean showDeleted) throws RetrieveDataException;
    
    public ArrayList<ClientRoute> getRoutesWithClientTieIn() throws RetrieveDataException;
    
    public ArrayList<Client> getClientsWithExtrasById(Integer clientId) throws RetrieveDataException;
    
    public ArrayList<Client> getNumberActiveClientsWithExtras(Integer numberToLoad) throws RetrieveDataException;
    
    public ArrayList<Client> getActiveClientsWithExtras() throws RetrieveDataException;
    
    public User getDMAssignedToClient(int client_id) throws RetrieveDataException;
    
    public ArrayList<Client> getChildrenClientsForClient(Integer clientId) throws RetrieveDataException;
    
    public ArrayList<ClientRoute> getRoutesAssociatedWithClient(Integer clientId) throws RetrieveDataException;
    
    public Client getClientAndAddressById(Integer clientId) throws RetrieveDataException;

    public ArrayList<TerminationReason> getTerminationReasons() throws RetrieveDataException;
    
    public ArrayList<ClientTerminationReason> getClientTerminationReasons(Integer clientId) throws RetrieveDataException;
    
    public void saveClientTerminationReason(ClientTerminationReason reason) throws SaveDataException;
    
    public Integer saveClientRoute(ClientRoute clientRoute) throws SaveDataException;
    
    public void deleteClientRoute(Integer clientRoute) throws SaveDataException;
    
    public Client getCientByLogin(String userName, String password) throws RetrieveDataException;
            
    public ArrayList<ClientRoute> getClientRoutes() throws RetrieveDataException;
    
    public void assignClientsToRoutes(ArrayList<Integer> clientIds, Integer routeId) throws SaveDataException;
    
    public ArrayList<Client> getClientsForRoute(Integer clientRouteId) throws RetrieveDataException;
    
    public ClientRoute getClientRouteById(Integer clientRouteId) throws RetrieveDataException;
    
    public ArrayList<Client> getClientsByBranch(Integer branchId) throws RetrieveDataException;
    
    public ArrayList<Client> getActiveClientsWithAddress() throws RetrieveDataException;
    
    public ArrayList<Client> getClientsByBranch(Integer branchId, Boolean showActive) throws RetrieveDataException;
    
    public ArrayList<Client> getClientsByBranchNoAddress(Integer branchId) throws RetrieveDataException;
    
    void deleteClientContract(ClientContract clientContract) throws SaveDataException;
    
    public Date getClientInvoiceStartDate(Integer clientId) throws RetrieveDataException;

    String generateNewUskedId(Client client) throws RetrieveDataException;

    ArrayList<Client> getActiveClients() throws RetrieveDataException;
    
    public ArrayList<Client> getActiveClientsWithUsked() throws RetrieveDataException;

    ArrayList<ClientContract> getAllClientContract() throws RetrieveDataException;

    public Client getClientByName(String clientName) throws RetrieveDataException;
    
    public ArrayList<Client> getNewClients() throws RetrieveDataException;
    
    Client getClientById(int clientId) throws RetrieveDataException;

    ArrayList<ClientContract> getClientContract(Integer clientId) throws RetrieveDataException;

    ClientContractType getClientContractType(Integer contractTypeId) throws RetrieveDataException;

    ArrayList<ClientContractType> getClientContractTypes() throws RetrieveDataException;

    ClientExport getClientExport(int clientId) throws RetrieveDataException;

    ArrayList<ClientInvoice> getClientInvoices(Integer clientId) throws RetrieveDataException;

    ArrayList<String> getClientValidPostPhoneNumbers(int client_id) throws RetrieveDataException;

    ArrayList<Client> getClientsByDuplicateLogin(int client_id) throws RetrieveDataException;

    ArrayList<Client> getClientsByLogin(String userName, String password, int branchId) throws RetrieveDataException;
    
    ArrayList<Client> getClientsByEmailAndLogin(String email, String password) throws RetrieveDataException;

    ArrayList<ClientContract> getExpiringClientContract() throws RetrieveDataException;

    void renewContractByDefault(ClientContract clientContract);

    void runInvoicingForClient(ClientContract clientContract) throws SaveDataException;

    void saveClient(Client client) throws SaveDataException, RetrieveDataException;

    void saveClientContact(ClientContact contact, boolean isUpdate) throws SaveDataException;
    
    void saveClientContract(ClientContract clientContract) throws SaveDataException;

    void saveClientExport(ClientExport clientExport, String branchId) throws SaveDataException;

    void saveClientNote(ClientNotes clientNote) throws SaveDataException;
    
    public Client getClientByCommunicationSource(CommunicationSource communicationSource) throws RetrieveDataException;
    
}
