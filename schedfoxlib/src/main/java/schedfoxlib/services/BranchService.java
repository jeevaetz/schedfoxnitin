package schedfoxlib.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.BranchControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Branch;

public class BranchService implements BranchControllerInterface {

    private static String location = "Branch/";
    private String companyId = "2";

    public BranchService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public BranchService(String companyId) {
        this.companyId = companyId;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    @Override
    public ArrayList<Branch> getBranches() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getbranches/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Branch>>() {
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
    public ArrayList<Branch> getBranchesForManagement(int managementId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getbranchesformanagement/" + managementId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Branch>>() {
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
    public Integer saveBranch(Branch branch) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "savebranch");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(branch)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Branch> getBranchesForCompany() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getbranchesforcompany/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Branch>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }


   
}
