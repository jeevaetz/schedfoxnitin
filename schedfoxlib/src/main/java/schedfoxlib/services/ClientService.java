package schedfoxlib.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URLEncoder;
import java.util.Date;

import schedfoxlib.controller.ClientControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.ClientContract;
import schedfoxlib.model.ClientContractType;
import schedfoxlib.model.ClientExport;
import schedfoxlib.model.ClientInvoice;
import schedfoxlib.model.ClientNotes;
import schedfoxlib.model.ClientRoute;
import schedfoxlib.model.ClientTerminationReason;
import schedfoxlib.model.CommunicationSource;
import schedfoxlib.model.CrossCompanyClient;
import schedfoxlib.model.TerminationReason;
import schedfoxlib.model.User;

public class ClientService implements ClientControllerInterface {

    private static String location = "Clients/";
    private String companyId = "2";

    public static void main(String args[]) {
        ClientService cliService = new ClientService("1852");
        try {
            ClientRoute clientRoute = cliService.getClientRouteById(5837);
            System.out.println("Here");
            //String id = cliService.generateNewUskedId(clients);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public ClientService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public ClientService(String companyId) {
        this.companyId = companyId;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    @Override
    public void deleteClientContract(ClientContract clientContact)
            throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getclientbycommunicationsource");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(clientContact)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public String generateNewUskedId(Client client) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "generatenewid");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
//            com.sun.jersey.api.client.Client c = com.sun.jersey.api.client.Client.create();
            String json = SchedfoxLibServiceVariables.getGson().toJson(client);
//            c.addFilter(new LoggingFilter(System.out));
//            WebResource r = c.resource(getLocation() + "generatenewid/");
//            ClientResponse response = r.type("application/json").post(ClientResponse.class, json);

            
            Representation rep = cr.post(new JsonRepresentation(json));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), String.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Client> getActiveClients() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getallactiveclients/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientContract> getAllClientContract()
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getallclientcontract/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<ClientContract>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Client getClientByCommunicationSource(CommunicationSource commSource)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getclientbycommunicationsource");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(commSource)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), Client.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Client getClientById(int clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclient/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return (Client) gson.fromJson(cr.get().getReader(), Client.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientContract> getClientContract(Integer clientId)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientcontract/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<ClientContract>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ClientContractType getClientContractType(Integer contract_type_id)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientcontracttype/" + contract_type_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), ClientContractType.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientContractType> getClientContractTypes()
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "listclientcontracttypes/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<ClientContractType>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ClientExport getClientExport(int clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientexport/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), ClientExport.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientInvoice> getClientInvoices(Integer clientId)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientinvoice/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientInvoice>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<String> getClientValidPostPhoneNumbers(int clientId)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientvalidpostphones/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<String>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Client> getClientsByDuplicateLogin(int clientId)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientduplicatelogin/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Client> getClientsByLogin(String userName, String password,
            int branchId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientbylogin/" + 
                URLEncoder.encode(userName, "UTF-8") + "/" + URLEncoder.encode(password, "UTF-8") +
                "/" + branchId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<ClientContract> getExpiringClientContract()
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getexpiringcontracts/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientContract>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void renewContractByDefault(ClientContract clientContract) {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "renewcontract");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(clientContract)));
        } catch (Exception exe) {
            
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void runInvoicingForClient(ClientContract clientContract)
            throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "runinvoicing");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(clientContract)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveClient(Client client) throws SaveDataException,
            RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "saveclient");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(client)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveClientContact(ClientContact arg0, boolean arg1)
            throws SaveDataException {
        // TODO Auto-generated method stub
    }

    @Override
    public void saveClientContract(ClientContract arg0)
            throws SaveDataException {
        // TODO Auto-generated method stub
    }

    @Override
    public void saveClientExport(ClientExport arg0, String arg1)
            throws SaveDataException {
        // TODO Auto-generated method stub
    }

    @Override
    public void saveClientNote(ClientNotes arg0) throws SaveDataException {
        // TODO Auto-generated method stub
    }

    @Override
    public ArrayList<Client> getClientsByEmailAndLogin(String email, String password) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientbyemailandlogin/" + 
                URLEncoder.encode(email, "UTF-8") + "/" + URLEncoder.encode(password, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<Client> getClientsByBranch(Integer branchId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientbybranch/" + branchId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<Client> getClientsByBranchNoAddress(Integer branchId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientbybranchaddress/" + branchId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public Client getClientByName(String clientName) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientbyname/" + clientName);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Client.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<Client> getNewClients() throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<ClientRoute> getClientRoutes() throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientroutes/");
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientRoute>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<Client> getClientsForRoute(Integer clientRouteId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientsforroute/" + clientRouteId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ClientRoute getClientRouteById(Integer clientRouteId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientroutebyid/" + clientRouteId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), ClientRoute.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<TerminationReason> getTerminationReasons() throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getterminationreasons/");
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<TerminationReason>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<ClientTerminationReason> getClientTerminationReasons(Integer clientId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientterminationreasons/" + clientId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientTerminationReason>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public void saveClientTerminationReason(ClientTerminationReason reason) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "saveclientterminationreason");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(reason)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Date getClientInvoiceStartDate(Integer clientId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientinvoicestartdate/" + clientId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Date.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public Integer saveClientRoute(ClientRoute clientRoute) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveclientroute/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.post(new JsonRepresentation(gson.toJson(clientRoute))).getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void assignClientsToRoutes(ArrayList<Integer> clientIds, Integer routeId) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "assignclientstoroutes/" + routeId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(clientIds)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void deleteClientRoute(Integer clientRoute) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "deleteclientroute/" + clientRoute);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(clientRoute)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Client> getClientsByBranch(Integer branchId, Boolean showActive) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientsbybranchandactive/" + branchId + "/" + showActive);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public Client getClientAndAddressById(Integer clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientwithaddress/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return (Client) gson.fromJson(cr.get().getReader(), Client.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientRoute> getRoutesAssociatedWithClient(Integer clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getroutesassociatedwithclient/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientRoute>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Client getCientByLogin(String userName, String password) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientbylogin/" + URLEncoder.encode(userName, "UTF-8") + "/" + URLEncoder.encode(password, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Client.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Client> getChildrenClientsForClient(Integer clientId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getchildrenclientsforclient/" + clientId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public User getDMAssignedToClient(int client_id) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getdmassignedtoclient/" + client_id);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), User.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Client> getActiveClientsWithUsked() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getallactiveclientswithusked/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Client> getActiveClientsWithExtras() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getactiveclientswithextras/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Client> getActiveClientsWithAddress() throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getactiveclientswithaddress/");
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<Client> getNumberActiveClientsWithExtras(Integer numberToLoad) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getnumberactiveclientswithextras/" + numberToLoad);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<Client> getClientsWithExtrasById(Integer clientId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientwithextras/" + clientId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<ClientRoute> getRoutesWithClientTieIn() throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getrouteswithclienttiein/");
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientRoute>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<Client> getClientsByBranchWithDeviceInfo(Integer branchId, Boolean showDeleted) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getclientbybranchwithdeviceinfo/" + branchId + "/" + showDeleted);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Client>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<CrossCompanyClient> getCrossSchemaPatrolProClientsByLogin(String userName, String password) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getcrossschemapatrolproclientbylogin/" + 
                URLEncoder.encode(userName, "UTF-8") + "/" + URLEncoder.encode(password, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<CrossCompanyClient>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public ArrayList<ClientRoute> getRoutesAssociatedWithClientWithMinimalClientData(Integer clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getroutesassociatedwithclientwithminimalclientdata/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientRoute>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
}
