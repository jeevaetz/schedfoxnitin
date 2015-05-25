/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.CompanyControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Company;
import schedfoxlib.model.CompanyObj;
import schedfoxlib.model.CompanyView;
import schedfoxlib.model.ManagementClient;

/**
 *
 * @author user
 */
public class CompanyService implements CompanyControllerInterface {

    private static String location = "Company/";
    
    public CompanyService() {
        
    }
    
    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }
    
    @Override
    public Branch getBranchById(Integer branchId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getbranchbyid/" + branchId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Branch.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Company getCompanyById(Integer companyId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getcompanybyid/" + companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Company.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public CompanyView getCompanyView(int company_view_id) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getcompanyview/" + company_view_id);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), CompanyView.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Branch> getBranchesForCompany(Integer companyId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getbranchesforcompany/" + companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
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
    public Integer saveCompanyObj(CompanyObj company) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savecompany/");
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.post(new JsonRepresentation(gson.toJson(company))).getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Integer saveManagementClient(ManagementClient managementClient) throws SaveDataException {
       ClientResource cr = new ClientResource(getLocation() + "savemanagementclient/");
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.post(new JsonRepresentation(gson.toJson(managementClient))).getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public String setupCompanySchema(String schemaName, String sourceSchema) throws Exception {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "setupcompanyschema/" + URLEncoder.encode(schemaName, "UTF-8") + "/" + URLEncoder.encode(sourceSchema, "UTF-8"));
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), String.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<CompanyObj> getCompaniesForPatrolPro() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getcompaniesforpatrolpro/");
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<CompanyObj>>() {
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
    public ArrayList<CompanyObj> getAllCompanies() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getallcompanies/");
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<CompanyObj>>() {
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
    public CompanyObj getCompanyObjById(Integer companyId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getcompanyobjbyid/" + companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), CompanyObj.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
    
}
