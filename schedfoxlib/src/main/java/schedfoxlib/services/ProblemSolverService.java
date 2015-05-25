package schedfoxlib.services;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.restlet.resource.ClientResource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;

import schedfoxlib.controller.ProblemSolverControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.IncidentReport;
import schedfoxlib.model.ProblemSolverContact;
import schedfoxlib.model.ProblemSolverContacts;
import schedfoxlib.model.ProblemSolverType;
import schedfoxlib.model.Problemsolver;
import schedfoxlib.model.ProblemsolverEmail;

public class ProblemSolverService implements ProblemSolverControllerInterface {

    private static String location = "ProblemSolver/";
    private String companyId;

    public ProblemSolverService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public ProblemSolverService(String companyId) {
        this.companyId = companyId;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    @Override
    public ProblemSolverContact getProblemSolverContactByClient(int client_id)
            throws SQLException {
        ClientResource cr = new ClientResource(getLocation() + "getproblemsolvercontact/" + client_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), ProblemSolverContact.class);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ProblemSolverContacts> getProblemSolverContactsForProblem(
            int ps_id, String type) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getproblemsolvercontactsforproblem/" + ps_id + "/" + type);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<ProblemSolverContacts>>() {
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
    public ArrayList<Problemsolver> getProblemsForClient(int client_id)
            throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getproblemsforclient/" + client_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Problemsolver>>() {
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
    public ArrayList<ProblemSolverContacts> getProblemSolverContactById(
            int problem_solver_contact_id) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getproblemsolvercontactbyid/" + problem_solver_contact_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<ProblemSolverContacts>>() {
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
    public Integer getNextPrimaryId() throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getprimaryid/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Integer.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveProblemSolverContactsWithDate(ProblemSolverContact contact,
            ArrayList<ProblemSolverContacts> contacts) throws SQLException {
        // TODO Auto-generated method stub
    }

    @Override
    public void saveReloadableProblemSolverContacts(
            ProblemSolverContact contact,
            ArrayList<ProblemSolverContacts> contacts, String type)
            throws SQLException {
        // TODO Auto-generated method stub
    }

    @Override
    public ArrayList<Problemsolver> getProblemsWithGuardInstructionsWithinXDays(int client_id, int numberDays) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getwithguardinstructionsforxdays/" + client_id + "/" + numberDays);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Problemsolver>>() {
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
    public Integer saveProblemSolver(Problemsolver ps) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "saveproblemsolver");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            String json = SchedfoxLibServiceVariables.getGson().toJson(ps);
            Representation rep = cr.post(new JsonRepresentation(json));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveProblemSolverEmail(ProblemsolverEmail email) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "saveproblemsolveremail");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            String json = SchedfoxLibServiceVariables.getGson().toJson(email);
            cr.post(new JsonRepresentation(json));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ProblemSolverType> getProblemSolverTypes() throws RetrieveDataException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
