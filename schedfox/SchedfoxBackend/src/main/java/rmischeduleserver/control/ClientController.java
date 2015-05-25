/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import schedfoxlib.controller.ClientControllerInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.ClientContract;
import schedfoxlib.model.ClientContractType;
import schedfoxlib.model.ClientExport;
import schedfoxlib.model.ClientInvoice;
import schedfoxlib.model.ClientNotes;
import rmischeduleserver.mysqlconnectivity.queries.communication.get_client_communication_assoc_query;
import rmischeduleserver.mysqlconnectivity.queries.importing.GetNextClientIDQuery;
import rmischeduleserver.mysqlconnectivity.queries.login.login_as_client_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_client_contact_id_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_cross_schema_logins_for_clients_query;
import schedfoxlib.model.*;

/**
 *
 * @author user
 */
public class ClientController implements ClientControllerInterface {

    private String companyId;

    private ClientController(String companyId) {
        this.companyId = companyId;
    }

    public static ClientController getInstance(String companyId) {
        return new ClientController(companyId);
    }
    
    public void toggleClientViewableInContact(Integer clientId) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        
        try {
            toggle_client_contact_by_id_query clientContactQuery = new toggle_client_contact_by_id_query();
            clientContactQuery.setPreparedStatement(new Object[]{clientId});
            clientContactQuery.setCompany(companyId);
            conn.executeUpdate(clientContactQuery, "");
        } catch (Exception exe) {
        }
    }

    public ArrayList<ClientDisplayContact> getClientsToBeContacted(Integer numberOfCorporateDays, Integer userId, boolean isCorporateUser) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientDisplayContact> retVal = new ArrayList<ClientDisplayContact>();

        GeneralQueryFormat myListQuery = null;
        if (isCorporateUser) {
            myListQuery = new get_clients_to_contact_for_corporate_query();
            //myListQuery.setPreparedStatement(new Object[]{
            //    numberOfCorporateDays, "Corporate User", "Courtesy Call Manager", numberOfCorporateDays});
            myListQuery.setPreparedStatement(new Object[]{});
        } else {
            myListQuery = new get_clients_to_contact_for_dm_query();
            myListQuery.setPreparedStatement(new Object[]{userId, userId, userId, userId, userId});

        }
        if (myListQuery != null) {
            try {
                myListQuery.setCompany(companyId);
                Record_Set rs = conn.executeQuery(myListQuery, "");
                for (int r = 0; r < rs.length(); r++) {
                    ClientDisplayContact tempClient = new ClientDisplayContact(new Date(), rs);
                    retVal.add(tempClient);
                    rs.moveNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return retVal;
    }
    
    @Override
    public ArrayList<ClientRoute> getRoutesWithClientTieIn() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientRoute> retVal = new ArrayList<ClientRoute>();
        try {
            get_routes_with_client_info_query getContactQuery = new get_routes_with_client_info_query();
            getContactQuery.setPreparedStatement(new Object[]{});
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            LinkedHashMap<Integer, ClientRoute> routeHash = new LinkedHashMap<Integer, ClientRoute>();
            
            for (int r = 0; r < rst.length(); r++) {
                ClientRoute clientRoute = new ClientRoute(rst);
                if (routeHash.get(clientRoute.getClientRouteId()) == null) {
                    clientRoute.setClientIds(new ArrayList<Integer>());
                    routeHash.put(clientRoute.getClientRouteId(), clientRoute);
                    if (rst.getInt("client_id") != 0) {
                        clientRoute.getClientIds().add(rst.getInt("client_id"));
                    }
                } else {
                    routeHash.get(clientRoute.getClientRouteId()).getClientIds().add(rst.getInt("client_id"));
                }
                rst.moveNext();

            }
            Collection<ClientRoute> routes = routeHash.values();
            retVal.addAll(routes);
        } catch (Exception e) {
        }
        
        return retVal;
    }

    public ArrayList<Client> getChildrenClientsForClient(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            get_children_of_client_query getContactQuery = new get_children_of_client_query();
            getContactQuery.setPreparedStatement(new Object[]{clientId});
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                Client tempClient = new Client(new Date(), rst);
                retVal.add(tempClient);
                rst.moveNext();
            }
        } catch (Exception e) {
        }
        return retVal;
    }

    public ArrayList<Client> searchClientsByParam(String params, ArrayList<Integer> branches) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            get_clients_by_search_params_query getContactQuery = new get_clients_by_search_params_query();
            getContactQuery.update(params, branches);
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                Client tempClient = new Client(new Date(), rst);
                retVal.add(tempClient);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public void loadDMsAssignedToClients(ArrayList<Client> clients) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            get_corporate_communicator_contact_for_all_query getContactQuery = new get_corporate_communicator_contact_for_all_query();
            ArrayList<Integer> clientids = new ArrayList<Integer>();
            for (int c = 0; c < clients.size(); c++) {
                clientids.add(clients.get(c).getClientId());
            }
            getContactQuery.update(clientids);
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            HashMap<Integer, User> clientIdsToUser = new HashMap<Integer, User>();
            for (int r = 0; r < rst.length(); r++) {
                User tempUser = new User(new Date(), rst);
                clientIdsToUser.put(rst.getInt("client_id"), tempUser);
                rst.moveNext();
            }

            for (int c = 0; c < clients.size(); c++) {
                if (clientIdsToUser.get(clients.get(c).getClientId()) != null) {
                    clients.get(c).setDmAssociatedWithAccount(clientIdsToUser.get(clients.get(c).getClientId()));
                }
            }
        } catch (Exception e) {
        }
    }

    public void assignClientsToRoutes(ArrayList<Integer> clientIds, Integer routeId) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            assign_clients_to_client_route_query myQuery = new assign_clients_to_client_route_query();
            myQuery.update(routeId, clientIds);
            myQuery.setCompany(companyId);
            conn.executeUpdate(myQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }

    }

    public User getDMAssignedToClient(int client_id) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        User retVal = new User();
        try {
            get_corporate_communicator_contact_query getContactQuery = new get_corporate_communicator_contact_query();
            getContactQuery.setPreparedStatement(new Object[]{client_id});
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            int user_id = 0;
            for (int r = 0; r < rst.length(); r++) {
                user_id = rst.getInt("user_id");
                rst.moveNext();

            }

            UserController userFactory = new UserController(companyId);
            retVal = userFactory.getUserById(user_id);
        } catch (Exception e) {
        }
        return retVal;
    }

    public ArrayList<ClientRoute> getRoutesAssociatedWithClient(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_routes_by_client_id_query mySelectQuery = new get_routes_by_client_id_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{clientId});

        ArrayList<ClientRoute> retVal = new ArrayList<ClientRoute>();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientRoute(rst));

                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<Client> getClientsWithRecentReports() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_clients_with_report_times_query mySelectQuery = new get_clients_with_report_times_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{});

        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));

                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public ArrayList<Client> getClientsByDuplicateLogin(int client_id) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_clients_id_by_login_info_query mySelectQuery = new get_clients_id_by_login_info_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{client_id, client_id});

        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));

                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public ArrayList<Client> getClientsByBranchNoAddress(Integer branchId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_clients_by_branch_query mySelectQuery = new get_clients_by_branch_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{branchId});

        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));

                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
    @Override
    public ArrayList<Client> getClientsByBranchWithDeviceInfo(Integer branchId, Boolean showDeleted) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_clients_by_branches_with_device_info_query mySelectQuery = new get_clients_by_branches_with_device_info_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{branchId, showDeleted, showDeleted});

        LinkedHashMap<Integer, Client> clientHash = new LinkedHashMap<Integer, Client>();
        
        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                Client tempCli = new Client(new Date(), rst);
                if (clientHash.get(tempCli.getClientId()) == null) {
                    clientHash.put(tempCli.getClientId(), tempCli);
                    if (rst.getInt("client_equipment_id") > 0) {
                        tempCli.getAssignedClientEquipmentIds().add(rst.getInt("client_equipment_id"));
                    }
                } else {
                    clientHash.get(tempCli.getClientId()).getAssignedClientEquipmentIds().add(rst.getInt("client_equipment_id"));
                }
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        retVal.addAll(clientHash.values());
        return retVal;
    }

    public ArrayList<Client> getClientsByBranch(ArrayList<Integer> branches) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_clients_by_branches_query mySelectQuery = new get_clients_by_branches_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.update(branches);

        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));

                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public ArrayList<Client> getClientsByBranch(Integer branchId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_clients_id_by_branch_query mySelectQuery = new get_clients_id_by_branch_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{branchId});

        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));

                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
    @Override
    public ArrayList<CrossCompanyClient> getCrossSchemaPatrolProClientsByLogin(String userName, String password) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        RunQueriesEx fakeMultiQuery = new RunQueriesEx();
        
        get_cross_schema_logins_for_clients_query mySelectQuery = new get_cross_schema_logins_for_clients_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{userName, password});

        fakeMultiQuery.add(mySelectQuery);
        
        ArrayList<CrossCompanyClient> retVal = new ArrayList<CrossCompanyClient>();
        try {
            ArrayList<Record_Set> rsts = conn.executeQueryEx(fakeMultiQuery, "");
            if (rsts.size() > 0) {
                Record_Set rst = rsts.get(1);
                for (int r = 0; r < rst.length(); r++) {
                    retVal.add(new CrossCompanyClient(new Date(), rst));

                    rst.moveNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public ArrayList<Client> getClientsByLogin(String userName, String password, int branchId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_clients_by_login_info_query mySelectQuery = new get_clients_by_login_info_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{branchId, branchId, userName, password});

        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));

                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public ClientExport getClientExport(int clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        client_export_usked_query mySelectQuery = new client_export_usked_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{clientId});

        ClientExport retVal = new ClientExport();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.setUskedClientId(rst.getInt("usked_client_id"));
                retVal.setClientId(rst.getInt("client_id"));
                retVal.setClientBranch(rst.getInt("client_branch"));
                retVal.setUskedCliId(rst.getString("usked_cli_id"));
                retVal.setUskedWsId(rst.getString("usked_ws_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public void saveClientExport(ClientExport clientExport, String branchId) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        client_export_usked_update myUpdateQuery = new client_export_usked_update();

        if (clientExport.getUskedWsId() != null) {
            myUpdateQuery.update(clientExport.getUskedCliId(), clientExport.getClientId() + "", clientExport.getUskedWsId());
        } else {
            myUpdateQuery.update(clientExport.getUskedCliId(), clientExport.getClientId() + "");
        }

        myUpdateQuery.setBranch(branchId);
        myUpdateQuery.setCompany(companyId);

        try {
            conn.executeUpdate(myUpdateQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public void saveClientContact(ClientContact contact, boolean isUpdate) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_client_contact_query saveClientContactQuery = new save_client_contact_query(contact, isUpdate);

        saveClientContactQuery.setCompany(companyId);

        try {
            if (!isUpdate && (contact.getClientContactId() == null || contact.getClientContactId() == 0)) {
                get_client_contact_id_query getContactQuery = new get_client_contact_id_query();
                getContactQuery.setCompany(companyId);
                getContactQuery.setPreparedStatement(new Object[]{});
                Record_Set rst = conn.executeQuery(getContactQuery, "");
                contact.setClientContactId(rst.getInt(0));
            }
            conn.executeUpdate(saveClientContactQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public ArrayList<Client> getActiveClientsWithUsked() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        ArrayList<Client> retVal = new ArrayList<Client>();
        get_all_active_clients_with_usked_query getActiveClients = new get_all_active_clients_with_usked_query();

        getActiveClients.setPreparedStatement(new Object[]{});
        getActiveClients.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(getActiveClients, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<Client> getClientsWithExtrasById(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        HashMap<Integer, Client> clientCache = new HashMap<Integer, Client>();

        ArrayList<Client> retVal = new ArrayList<Client>();
        get_client_with_extras_and_contacts_query getActiveClients = new get_client_with_extras_and_contacts_query();

        getActiveClients.setPreparedStatement(new Object[]{clientId});
        getActiveClients.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(getActiveClients, "");
            for (int r = 0; r < rst.length(); r++) {
                Client cli = null;
                if (clientCache.get(rst.getInt("client_id")) != null) {
                    cli = clientCache.get(rst.getInt("client_id"));
                } else {
                    cli = new Client(new Date(), rst);
                    clientCache.put(rst.getInt("client_id"), cli);
                }
                ClientContact contact = new ClientContact(rst);
                if (contact.getClientContactId() != null && contact.getClientContactId() != 0) {
                    cli.getNonCachedContacts().add(contact);
                }
                rst.moveNext();
            }
            Iterator<Integer> keys = clientCache.keySet().iterator();
            while (keys.hasNext()) {
                retVal.add(clientCache.get(keys.next()));
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<Client> getNumberActiveClientsWithExtras(Integer numberToLoad) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        HashMap<Integer, Client> clientCache = new HashMap<Integer, Client>();

        ArrayList<Client> retVal = new ArrayList<Client>();
        get_all_active_clients_with_extras_and_contacts_query getActiveClients = new get_all_active_clients_with_extras_and_contacts_query();

        getActiveClients.setPreparedStatement(new Object[]{numberToLoad});
        getActiveClients.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(getActiveClients, "");
            for (int r = 0; r < rst.length(); r++) {
                Client cli = null;
                if (clientCache.get(rst.getInt("client_id")) != null) {
                    cli = clientCache.get(rst.getInt("client_id"));
                } else {
                    cli = new Client(new Date(), rst);
                    clientCache.put(rst.getInt("client_id"), cli);
                }
                ClientContact contact = new ClientContact(rst);
                if (contact.getClientContactId() != null && contact.getClientContactId() != 0) {
                    cli.getNonCachedContacts().add(contact);
                }
                rst.moveNext();
            }
            Iterator<Integer> keys = clientCache.keySet().iterator();
            while (keys.hasNext()) {
                retVal.add(clientCache.get(keys.next()));
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<Client> getActiveClientsWithExtras() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        ArrayList<Client> retVal = new ArrayList<Client>();
        get_all_active_clients_with_extras_query getActiveClients = new get_all_active_clients_with_extras_query();

        getActiveClients.setPreparedStatement(new Object[]{});
        getActiveClients.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(getActiveClients, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<Client> getActiveClientsWithRecentInstantForms() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        ArrayList<Client> retVal = new ArrayList<Client>();
        get_all_active_clients_with_instant_query getActiveClients = new get_all_active_clients_with_instant_query();

        getActiveClients.setPreparedStatement(new Object[]{});
        getActiveClients.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(getActiveClients, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    @Override
    public ArrayList<Client> getActiveClients() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        ArrayList<Client> retVal = new ArrayList<Client>();
        get_all_active_clients_query getActiveClients = new get_all_active_clients_query();

        getActiveClients.setPreparedStatement(new Object[]{});
        getActiveClients.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(getActiveClients, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public void loadContactInformationForClients(ArrayList<Client> clients) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        load_all_client_contact_query loadAllQuery = new load_all_client_contact_query();
        loadAllQuery.update(clients);
        loadAllQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(loadAllQuery, "");
            ArrayList<ClientContact> contacts = new ArrayList<ClientContact>();
            for (int r = 0; r < rst.length(); r++) {
                contacts.add(new ClientContact(rst));
                rst.moveNext();
            }
            for (int c = 0; c < contacts.size(); c++) {
                for (int cli = 0; cli < clients.size(); cli++) {
                    if (clients.get(cli).getClientId().equals(contacts.get(c).getClientId())) {
                        ArrayList<ClientContact> cliContacts = clients.get(cli).getContactsWOFetch();
                        if (cliContacts == null) {
                            cliContacts = new ArrayList<ClientContact>();
                        }
                        cliContacts.add(contacts.get(c));
                        clients.get(cli).setContacts(cliContacts);
                    }
                }
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public void runInvoicingForClient(ClientContract clientContract) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        run_invoicing_query saveClientContract = new run_invoicing_query();
        saveClientContract.setPreparedStatement(new Object[]{clientContract.getClientId(), null, true});
        saveClientContract.setCompany(companyId);

        try {
            conn.executeUpdate(saveClientContract, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }

    }

    @Override
    public void saveClientContract(ClientContract clientContract) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        save_client_contract_query saveClientContract = new save_client_contract_query();
        saveClientContract.update(clientContract);
        saveClientContract.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(saveClientContract, "");
            clientContract.setClientContractId(rst.getInt(0));
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    public void updateClientContact(int[] clients, boolean value) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        StringBuilder data = new StringBuilder();
        data.append("{");
        for (int c = 0; c < clients.length; c++) {
            if (c > 0) {
                data.append(",");
            }
            data.append(clients[c]);
        }
        data.append("}");

        update_client_contact_query saveClientContract = new update_client_contact_query();
        saveClientContract.setPreparedStatement(value, data.toString());
        saveClientContract.setCompany(companyId);

        try {
            conn.executeUpdate(saveClientContract, "");
        } catch (Exception e) {
            e.printStackTrace();
            throw new SaveDataException();
        }
    }

    @Override
    public ArrayList<ClientContract> getAllClientContract() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientContract> retVal = new ArrayList<ClientContract>();

        get_all_client_contract_query query = new get_all_client_contract_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                ClientContract clientContract = new ClientContract(rst);
                clientContract.setClient(new Client(new Date(), rst));
                retVal.add(clientContract);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public ArrayList<ClientContract> getContactClientContract() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientContract> retVal = new ArrayList<ClientContract>();

        get_contact_client_contract_query query = new get_contact_client_contract_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                ClientContract clientContract = new ClientContract(rst);
                clientContract.setClient(new Client(new Date(), rst));
                retVal.add(clientContract);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public ArrayList<ClientContract> getNoContactClientContract() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientContract> retVal = new ArrayList<ClientContract>();

        get_no_contact_client_contract_query query = new get_no_contact_client_contract_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                ClientContract clientContract = new ClientContract(rst);
                clientContract.setClient(new Client(new Date(), rst));
                retVal.add(clientContract);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    @Override
    public ArrayList<ClientContract> getExpiringClientContract() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientContract> retVal = new ArrayList<ClientContract>();

        get_expiring_client_contract_query query = new get_expiring_client_contract_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{});

        try {
            Record_Set rst = conn.executeQuery(query, "");
            for (int r = 0; r < rst.length(); r++) {
                ClientContract clientContract = new ClientContract(rst);
                clientContract.setClient(new Client(new Date(), rst));
                retVal.add(clientContract);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public void deleteClientNote(ClientNotes clientNote) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        delete_client_note_query query = new delete_client_note_query();
        query.setCompany(companyId);
        query.update(clientNote);

        try {
            conn.executeUpdate(query, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public Integer saveClientRoute(ClientRoute clientRoute) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        boolean isNewClient = false;
        if (clientRoute.getClientRouteId() == null || clientRoute.getClientRouteId() == 0) {
            isNewClient = true;
            get_next_client_route_id_query routeSequenceQuery = new get_next_client_route_id_query();
            routeSequenceQuery.setCompany(companyId);
            try {
                Record_Set rst = conn.executeQuery(routeSequenceQuery, "");
                int nextClientId = rst.getInt(0);
                clientRoute.setClientRouteId(nextClientId);
            } catch (Exception e) {
                throw new SaveDataException();
            }
        }

        save_client_route_query query = new save_client_route_query();
        query.setCompany(companyId);
        query.update(clientRoute, isNewClient);

        try {
            conn.executeUpdate(query, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
        return clientRoute.getClientRouteId();
    }

    @Override
    public void saveClientNote(ClientNotes clientNote) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        save_client_note_query query = new save_client_note_query();
        query.setCompany(companyId);
        query.update(clientNote);

        try {
            conn.executeUpdate(query, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }

    }

    @Override
    public void renewContractByDefault(ClientContract clientContract) {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        renew_client_contract_by_default_query query = new renew_client_contract_by_default_query();
        query.setCompany(companyId);
        query.setPreparedStatement(new Object[]{clientContract.getClientContractId()});

        try {
            conn.executeUpdate(query, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteClientContract(ClientContract clientContract) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        delete_client_contract_query saveClientContract = new delete_client_contract_query();
        saveClientContract.setPreparedStatement(new Object[]{clientContract.getClientContractId()});
        saveClientContract.setCompany(companyId);

        try {
            conn.executeQuery(saveClientContract, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public ClientContractType getClientContractType(Integer contractTypeId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ClientContractType retVal = new ClientContractType();

        get_client_contract_type_by_query contractTypeQuery = new get_client_contract_type_by_query();
        contractTypeQuery.setPreparedStatement(new Object[]{contractTypeId});
        contractTypeQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(contractTypeQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientContractType(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<String> getClientValidPostPhoneNumbers(int client_id) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<String> retVal = new ArrayList<String>();

        get_valid_client_phone_numbers_query clientPhoneQuery = new get_valid_client_phone_numbers_query();
        clientPhoneQuery.setPreparedStatement(new Object[]{client_id, client_id, client_id, client_id});
        clientPhoneQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(clientPhoneQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(rst.getString(1));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<ClientContractType> getClientContractTypes() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientContractType> retVal = new ArrayList<ClientContractType>();

        client_contract_type_list_query contractTypeQuery = new client_contract_type_list_query();
        contractTypeQuery.setPreparedStatement(new Object[]{});
        contractTypeQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(contractTypeQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientContractType(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<ClientInvoice> getClientInvoices(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientInvoice> retVal = new ArrayList<ClientInvoice>();

        get_client_invoices_query clientContractsQuery = new get_client_invoices_query();
        clientContractsQuery.setPreparedStatement(new Object[]{clientId});
        clientContractsQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(clientContractsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientInvoice(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<ClientContract> getClientContract(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientContract> retVal = new ArrayList<ClientContract>();

        get_client_contracts_query clientContractsQuery = new get_client_contracts_query();
        clientContractsQuery.setPreparedStatement(new Object[]{clientId});
        clientContractsQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(clientContractsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientContract(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public String generateNewUskedId(Client client) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        String retVal = "";
        String cName = client.getClientName();
        cName = cName.replaceAll(" ", "").toUpperCase();
        if (cName.length() > 8) {
            cName = cName.substring(0, 8);
        }
        boolean exists = true;
        int i = 0;
        while (exists) {
            check_if_client_usked_id_exists_query checkUskedIdQuery = new check_if_client_usked_id_exists_query();
            checkUskedIdQuery.setPreparedStatement(cName + i);
            checkUskedIdQuery.setCompany(companyId);
            try {
                Record_Set rst = conn.executeQuery(checkUskedIdQuery, "");
                String uskedId = rst.getString("usked_cli_id");
                if (uskedId.length() == 0) {
                    exists = false;
                    retVal = cName + i;
                }
            } catch (Exception e) {
                throw new RetrieveDataException();
            }
            i++;
        }
        return retVal;
    }

    public ArrayList<Client> getClientsById(ArrayList<Integer> clientIds) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Client> retVal = new ArrayList<Client>();

        get_clients_by_id_query clientContractsQuery = new get_clients_by_id_query();
        clientContractsQuery.setPreparedStatement(clientIds.toArray());
        clientContractsQuery.setCompany(companyId);
        clientContractsQuery.update(clientIds.size());

        try {
            Record_Set rst = conn.executeQuery(clientContractsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public Client getClientById(int clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Client retVal = new Client(new Date());

        get_client_by_id_query clientContractsQuery = new get_client_by_id_query();
        clientContractsQuery.setPreparedStatement(new Object[]{clientId});
        clientContractsQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(clientContractsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Client(new Date(), rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public void saveClient(Client client) throws SaveDataException, RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        boolean isNewClient = false;
        if (client.getClientId() == null || client.getClientId() == 0) {
            isNewClient = true;
            GetNextClientIDQuery clientSequenceQuery = new GetNextClientIDQuery();
            clientSequenceQuery.setCompany(companyId);
            try {
                Record_Set rst = conn.executeQuery(clientSequenceQuery, "");
                int nextClientId = rst.getInt(0);
                client.setClientId(nextClientId);
            } catch (Exception e) {
                throw new RetrieveDataException();
            }
        }

        SaveClientObjectQuery saveClientQuery = new SaveClientObjectQuery();
        saveClientQuery.updateWithClient(client, isNewClient);
        saveClientQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveClientQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    public Client getClientByCommunicationSource(CommunicationSource communicationSource) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_client_communication_assoc_query myQuery = new get_client_communication_assoc_query();
        myQuery.update(communicationSource);
        myQuery.setCompany(companyId);
        Client retVal = new Client(new Date());
        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Client(new Date(), rst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<Client> getClientsByEmailAndLogin(String email, String password) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_clients_by_email_login_info_query mySelectQuery = new get_clients_by_email_login_info_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{email, password});

        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));

                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public Client getClientByName(String clientName) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Client retVal = new Client(new Date());

        get_client_by_name_query clientContractsQuery = new get_client_by_name_query();
        clientContractsQuery.setPreparedStatement(new Object[]{clientName});
        clientContractsQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(clientContractsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Client(new Date(), rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    public ArrayList<Client> getNewClients() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Client> retVal = new ArrayList<Client>();

        get_new_clients_query clientContractsQuery = new get_new_clients_query();
        clientContractsQuery.setPreparedStatement(new Object[]{});
        clientContractsQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(clientContractsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<ClientRoute> getClientRoutes() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientRoute> retVal = new ArrayList<ClientRoute>();
        try {
            get_client_routes_query getContactQuery = new get_client_routes_query();
            getContactQuery.setPreparedStatement(new Object[]{});
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientRoute(rst));
                rst.moveNext();

            }
        } catch (Exception e) {
        }
        return retVal;
    }

    @Override
    public ArrayList<Client> getClientsForRoute(Integer clientRouteId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            get_clients_for_route_query getContactQuery = new get_clients_for_route_query();
            getContactQuery.setPreparedStatement(new Object[]{clientRouteId});
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));
                rst.moveNext();

            }
        } catch (Exception e) {
        }
        return retVal;
    }

    @Override
    public ClientRoute getClientRouteById(Integer clientRouteId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ClientRoute retVal = new ClientRoute();
        try {
            get_client_route_by_id_query getContactQuery = new get_client_route_by_id_query();
            getContactQuery.setPreparedStatement(new Object[]{clientRouteId});
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientRoute(rst);
                rst.moveNext();

            }
        } catch (Exception e) {
        }
        return retVal;
    }

    @Override
    public ArrayList<TerminationReason> getTerminationReasons() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<TerminationReason> retVal = new ArrayList<TerminationReason>();
        try {
            get_termination_reasons_query getTerminationQuery = new get_termination_reasons_query();
            getTerminationQuery.setPreparedStatement(new Object[]{});
            getTerminationQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getTerminationQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new TerminationReason(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<ClientTerminationReason> getClientTerminationReasons(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientTerminationReason> retVal = new ArrayList<ClientTerminationReason>();
        try {
            get_client_termination_reasons_query getTerminationQuery = new get_client_termination_reasons_query();
            getTerminationQuery.setPreparedStatement(new Object[]{clientId});
            getTerminationQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getTerminationQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientTerminationReason(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public void saveClientTerminationReason(ClientTerminationReason reason) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            save_client_termination_reason_query saveTerminationQuery = new save_client_termination_reason_query();
            boolean isUpdate = true;
            if (reason.getClientTerminationReasonId() == null) {
                isUpdate = false;
            }
            saveTerminationQuery.update(reason, isUpdate);
            saveTerminationQuery.setCompany(companyId);
            conn.executeUpdate(saveTerminationQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public Date getClientInvoiceStartDate(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Date retVal = null;
        try {
            get_invoice_start_commisions_query getContactQuery = new get_invoice_start_commisions_query();
            getContactQuery.setPreparedStatement(new Object[]{clientId});
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal = rst.getDate("due_on");
                rst.moveNext();

            }

        } catch (Exception e) {
        }
        return retVal;
    }

    @Override
    public void deleteClientRoute(Integer clientRoute) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            delete_client_route_query saveTerminationQuery = new delete_client_route_query();
            boolean isUpdate = true;
            saveTerminationQuery.setPreparedStatement(new Object[]{clientRoute, clientRoute});
            saveTerminationQuery.setCompany(companyId);
            conn.executeUpdate(saveTerminationQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public ArrayList<Client> getClientsByBranch(Integer branchId, Boolean showActive) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_clients_by_branch_with_active_query mySelectQuery = new get_clients_by_branch_with_active_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{showActive, showActive, branchId});

        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));

                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public Client getClientAndAddressById(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Client retVal = new Client(new Date());

        get_client_by_id_with_address_query clientContractsQuery = new get_client_by_id_with_address_query();
        clientContractsQuery.setPreparedStatement(new Object[]{clientId});
        clientContractsQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(clientContractsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Client(new Date(), rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public Client getCientByLogin(String userName, String password) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Client retVal = new Client(new Date());

        CompanyController compController = new CompanyController();
        Company comp = compController.getCompanyById(Integer.parseInt(this.companyId));

        login_as_client_query clientContractsQuery = new login_as_client_query();
        clientContractsQuery.update(comp.getCompDB(), userName, password);
        clientContractsQuery.setCompany(companyId);

        try {
            Record_Set rst = conn.executeQuery(clientContractsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Client(new Date(), rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<Client> getActiveClientsWithAddress() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        get_active_clients_with_address_query mySelectQuery = new get_active_clients_with_address_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{});

        ArrayList<Client> retVal = new ArrayList<Client>();
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Client(new Date(), rst));

                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public ArrayList<ClientRoute> getRoutesAssociatedWithClientWithMinimalClientData(Integer clientId) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
