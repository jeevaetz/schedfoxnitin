/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.UserControllerInterface;
import schedfoxlib.controller.exceptions.NoUserException;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Company;
import schedfoxlib.model.Contact;
import schedfoxlib.model.Group;
import schedfoxlib.model.User;

/**
 *
 * @author user
 */
public class UserService implements UserControllerInterface {

    private static String location = "User/";
    private String companyId;

    public static void main(String args[]) {
        UserService userService = new UserService();
        try {
            User tempUser = userService.getUserById(1404);
            userService.getUserByLogin("ijuneau", "adfew");
            
            ArrayList<Group> groups = userService.getGroupsForUser(1404);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    public UserService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public UserService(String companyId) {
        this.companyId = companyId;
    }

    //Login for user
    @Override
    public User getUserById(Integer userid) throws SQLException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getuserbyid/" + userid);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), User.class);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Contact> getUsersByType(String type, String branch) throws SQLException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getuserbytype/" + type + "/" + branch);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Contact>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<User> getUsersByTypes(String type, String branch) throws SQLException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getusersbytypes/" + type + "/" + branch);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<User>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Group> getGroupsForUser(int user_id) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getgroupsforuser/" + user_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<Group>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public User getUserByLogin(String userName, String password) throws RetrieveDataException, NoUserException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getuserbylogin/" + URLEncoder.encode(userName, "UTF-8")
                    + "/" + URLEncoder.encode(password, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), User.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public ArrayList<Company> getCompaniesForUser(Integer userId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getcompaniesbyuser/" + userId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Company>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public User getUserByEmail(String email) throws SQLException {
        try {
            ClientResource cr = new ClientResource(Method.GET, getLocation() + "getuserbyemail/" + URLEncoder.encode(email, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            try {
                Gson gson = SchedfoxLibServiceVariables.getGson();
                return gson.fromJson(cr.get().getReader(), User.class);
            } catch (Exception exe) {
                return null;
            } finally {
                cr.getResponse().release();
            }
        } catch (Exception exe) {
            return null;
        }
    }

    @Override
    public Integer saveUser(User user, boolean isClearPassword) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveuser/" + isClearPassword);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.post(new JsonRepresentation(gson.toJson(user))).getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<User> getUsersWithEmailPassword() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getuserswithemailpassword/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            try {
                Type collectionType = new TypeToken<Collection<User>>() {
            }.getType();
                Gson gson = SchedfoxLibServiceVariables.getGson();
                return gson.fromJson(cr.get().getReader(), collectionType);
            } catch (Exception exe) {
                return null;
            } finally {
                cr.getResponse().release();
            }
        } catch (Exception exe) {
            return null;
        }
    }

    @Override
    public ArrayList<User> getUsersByLogin(String userName) throws RetrieveDataException, NoUserException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getusersbylogin/" + URLEncoder.encode(userName, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            try {
                Type collectionType = new TypeToken<Collection<User>>() {
            }.getType();
                Gson gson = SchedfoxLibServiceVariables.getGson();
                return gson.fromJson(cr.get().getReader(), collectionType);
            } catch (Exception exe) {
                return null;
            } finally {
                cr.getResponse().release();
            }
        } catch (Exception exe) {
            return null;
        }
    }

    @Override
    public void saveUserToBranchAndCompany(Integer userId, Integer branchId, Integer companyIdInt) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveusertobranchandcompany/" + userId + "/" + branchId + "/" + companyIdInt);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            cr.get();
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<User> getUsersByCompany(int companyId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getusersbycompany/" + companyId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, this.companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            try {
                Type collectionType = new TypeToken<Collection<User>>() {
            }.getType();
                Gson gson = SchedfoxLibServiceVariables.getGson();
                return gson.fromJson(cr.get().getReader(), collectionType);
            } catch (Exception exe) {
                return null;
            } finally {
                cr.getResponse().release();
            }
        } catch (Exception exe) {
            return null;
        }
    }

    @Override
    public ArrayList<Branch> getBranchesForUserAndCompany(Integer userId, Integer companyId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getbranchesforuserandcompany/" + userId + "/" + companyId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, this.companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            try {
                Type collectionType = new TypeToken<Collection<Branch>>() {
            }.getType();
                Gson gson = SchedfoxLibServiceVariables.getGson();
                return gson.fromJson(cr.get().getReader(), collectionType);
            } catch (Exception exe) {
                return null;
            } finally {
                cr.getResponse().release();
            }
        } catch (Exception exe) {
            return null;
        }
    }

    @Override
    public void clearForCompanyAndSaveBranchInfoForUser(Integer userId, Integer companyId, ArrayList<Integer> branches) throws SaveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "clearforcompanyandsavebranchinfoforuser/" + userId + "/" + companyId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, this.companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            try {
                Gson gson = SchedfoxLibServiceVariables.getGson();
                cr.post(new JsonRepresentation(gson.toJson(branches)));
            } catch (Exception exe) {
                throw new SaveDataException();
            } finally {
                cr.getResponse().release();
            }
        } catch (Exception exe) {
        }
    }
}
