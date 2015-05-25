/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.EquipmentControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.*;

/**
 *
 * @author user
 */
public class EquipmentService implements EquipmentControllerInterface {

    private static String location = "Equipment/";
    private String companyId = "2";

    public static void main(String args[]) {
        EquipmentService equipmentService = new EquipmentService();
        try {
            ClientEquipment clientEquipment = new ClientEquipment();
            GenericService genericFactory = new GenericService();
            clientEquipment.setClientId(-1);
            clientEquipment.setCost(new BigDecimal(0));
            clientEquipment.setDateIssued(new Date());
            clientEquipment.setEquipmentId(2);
            clientEquipment.setMdn("99000204516232");
            clientEquipment.setPhoneNumber("8178465019");
            clientEquipment.setUniqueId("35f675b39c433226");

            User myUser = new UserService().getUserById(2791);

            SalesDeviceBundle bundle = new SalesDeviceBundle();
            bundle.setUser(myUser);
            bundle.setClientEquipment(clientEquipment);

            equipmentService.associateSalesDeviceWithUser(bundle);

        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public EquipmentService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public EquipmentService(String companyId) {
        this.companyId = companyId;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    @Override
    public void clearReturnEmployeeEquipment(EmployeeEquipment empEquip) throws SaveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<EmployeeEquipment> getEmployeeEquipment(Integer equipmentId, String searchStr, boolean showAll, ArrayList<Integer> selBranches, Date startDate, Date endDate, Integer numberOfContacts) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Equipment> getEquipmentByClientOrEmployee(boolean loadForClient, boolean loadForEmployee) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ClientEquipment getClientEquipmentById(int equipmentId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientequipmentbyid/" + equipmentId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), ClientEquipment.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    public ArrayList<ClientEquipment> getClientEquipmentByType(int equipmentType) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientequipmentbytype/" + equipmentType);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<ClientEquipment>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    public ArrayList<ClientEquipment> getClientEquipmentByTypeAndId(int equipmentType, int clientId, boolean loadDateInfo) throws RetrieveDataException {
        String url = getLocation() + "getequipmentbytypeandid/" + equipmentType + "/" + clientId;
        if (loadDateInfo) {
            url += "?lastKnown=true";
        }
        ClientResource cr = new ClientResource(Method.GET, url);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<ClientEquipment>>() {
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
    public ArrayList<EntityEquipment> getEquipmentByType(int equipmentType, Class<EntityEquipment> entityType) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getequipmentbytype/" + equipmentType);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<EntityEquipment>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            String json = cr.post(new JsonRepresentation(gson.toJson(entityType))).getText();
            return gson.fromJson(json, collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ClientEquipment getEquipmentByIdAndIdentifier(int equipmentId, String identifier) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ClientEquipment getEquipmentByIdentifier(String identifier) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getequipmentbyidentifier/" + identifier);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), ClientEquipment.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Equipment> getEquipment() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getequipment");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<Equipment>>() {
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
    public Equipment getEquipmentById(int equipmentId) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Equipment getEquipmentByName(String name) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void markEquipmentReturned() throws SaveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void saveEmployeeEquipment(EmployeeEquipment empEquip, Integer receivedby) throws SaveDataException {
    }

    @Override
    public Integer saveClientEquipment(ClientEquipment clientEquipment) throws RetrieveDataException, SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "saveclientequipment/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            JsonRepresentation rep = new JsonRepresentation(gson.toJson(clientEquipment));
            Representation ret = cr.post(rep);
            String json = ret.getText();
            
            return gson.fromJson(json, Integer.class);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void returnEmployeeEquipment(EmployeeEquipment empEquip, Integer receivedby) throws SaveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<EntityEquipment> getEquipmentByTypeAndId(int equipmentType, int clientId, Class<EntityEquipment> entityType, boolean lastKnownDate) throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Integer saveClientEquipmentCommand(ClientEquipmentCommand equipmentCommand) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "saveclientequipmentcommand/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            String json = cr.post(new JsonRepresentation(gson.toJson(equipmentCommand))).getText();
            return gson.fromJson(json, Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientMessaging> getClientMessaging(Integer clientEquipmentId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientmessaging/" + clientEquipmentId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<ClientMessaging>>() {
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
    public void saveClientEquipmentContact(ClientEquipmentContact contact) throws SaveDataException, RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "saveclientequipmentcontact/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(contact)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientEquipment> getClientEquipmentGroupedByMdn(int equipmentType, int clientId, boolean loadLastKnownDate) throws RetrieveDataException {
        String url = getLocation() + "getequipmentgroupedbymdn/" + equipmentType + "/" + clientId;
        if (loadLastKnownDate) {
            url += "?loadLastKnown=true";
        }
        ClientResource cr = new ClientResource(Method.GET, url);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<ClientEquipment>>() {
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
    public ArrayList<ClientEquipmentVendor> getClientEquipmentVendor() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientequipmentvendors/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<ClientEquipmentVendor>>() {
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
    public ClientEquipment getEquipmentByClientId(int equipmentClientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getequipmentbyclientid/" + equipmentClientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), ClientEquipment.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public User getUserWithThisEquipment(String mdn) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getuserwiththisequipment/" + mdn);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), User.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void associateSalesDeviceWithUser(SalesDeviceBundle bundle) throws RetrieveDataException, SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "associateSalesDevice/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(bundle)));
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ClientEquipment getEquipmentFromSalesPersonId(Integer userId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getequipmentfromsalesperson/" + userId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), ClientEquipment.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientEquipmentCommand> getPendingCommands(Integer clientEquipmentId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getpendingcommands/" + clientEquipmentId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientEquipmentCommand>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void markCommandReceived(ClientEquipmentCommand command) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "markcommandreceived/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(command)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ClientEquipment getClientEquipmentByMdn(String mdn) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getequipmentbymdn/" + mdn);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), ClientEquipment.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ClientEquipment getEquipmentByPrimaryId(int equipmentClientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getequipmentbyprimaryid/" + equipmentClientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), ClientEquipment.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Employee> getEmployeesAtClient(Integer clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getemployeesatclient/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Employee>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public SalesEquipment getEquipmentByUserId(Integer userId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getequipmentbyuserid/" + userId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), SalesEquipment.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientEquipmentContact> getClientContact(Integer clientEquipmentId, Integer daysToGoBack) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientcontact/" + clientEquipmentId + "/" + daysToGoBack);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientEquipmentContact>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<SalesEquipment> getSalesEquipmentForActiveUsers() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getsalesequipmentforactiveusers/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<SalesEquipment>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ClientEquipment getEquipmentByMdn(String mdn) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getequipmentbymdn/" + mdn);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), ClientEquipment.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Boolean clearSalesEquipment(Integer clientEquipmentId) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "clearsalesequipment/" + clientEquipmentId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            cr.get();
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
        return true;
    }

    @Override
    public ArrayList<ClientEquipmentWithStats> getClientEquipmentWithStats(int equipmentType, int clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientequipmentwithstats/" + equipmentType + "/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientEquipmentWithStats>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void waiveEmployeeEquipmentReturn(EmployeeEquipment empEquip, Integer waivedBy) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "waiveemployeeequipmentreturn/" + waivedBy);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(empEquip))).getText();
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientEquipment> getClientEquipmentByIds(ArrayList<Integer> equipmentIds) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientequipmentbyids/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<ClientEquipment>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            String json = cr.post(new JsonRepresentation(gson.toJson(equipmentIds))).getText();
            
            return gson.fromJson(json, collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
}
