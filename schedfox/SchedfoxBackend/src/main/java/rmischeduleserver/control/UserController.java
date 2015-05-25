/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import schedfoxlib.controller.UserControllerInterface;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.login.get_active_users_by_login_query;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.login.login_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_branches_for_user_company_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_groups_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_user_by_email_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_user_by_group_and_branches_query;
import schedfoxlib.model.Contact;
import schedfoxlib.model.User;
import rmischeduleserver.mysqlconnectivity.queries.user.get_user_by_group_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_user_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_user_groups_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_user_next_seq_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_users_by_company_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_users_with_email_passwords_query;
import rmischeduleserver.mysqlconnectivity.queries.user.save_and_clear_user_branch_company_query;
import rmischeduleserver.mysqlconnectivity.queries.user.save_user_branch_company_query;
import rmischeduleserver.mysqlconnectivity.queries.user.save_user_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_client_branch_by_management_query;
import schedfoxlib.controller.exceptions.NoUserException;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.*;

/**
 *
 * @author user
 */
public class UserController implements UserControllerInterface {

    private static UserController myInstance;
    private String companyId;

    private static HashMap<Integer, User> userCache;
    
    public UserController(String companyId) {
        this.companyId = companyId;
        
        if (userCache == null) {
            userCache = new HashMap<Integer, User>();
        }
    }

    public ArrayList<Company> getCompaniesForUser(Integer userId) throws RetrieveDataException {
        ArrayList<Company> companies = new ArrayList<Company>();
        HashMap<Integer, Company> compHash = new HashMap<Integer, Company>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_client_branch_by_management_query myCompQuery = new get_client_branch_by_management_query();
        myCompQuery.update(userId.toString(), "", "user");
        try {
            Record_Set comps = conn.executeQuery(myCompQuery, "");

            for (int r = 0; r < comps.length(); r++) {
                int myCompanyId = comps.getInt("company_id");
                if (compHash.get(myCompanyId) == null) {
                    Company newCompany = new Company(comps);
                    compHash.put(myCompanyId, newCompany);
                }
                Company existingComp = compHash.get(myCompanyId);

                Branch newBranch = new Branch(comps);
                existingComp.addBranch(newBranch);
                comps.moveNext();

            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        Iterator<Company> companiesIterator = compHash.values().iterator();
        while (companiesIterator.hasNext()) {
            companies.add(companiesIterator.next());
        }
        return companies;
    }

    public User getUserByLogin(String userName, String password) throws RetrieveDataException, NoUserException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            login_query myLoginQuery = new login_query();
            myLoginQuery.update(userName, password);
            Record_Set rs = conn.executeQuery(myLoginQuery, "");
            if (rs.length() == 0) {
                throw new NoUserException();
            }
            return new User(new Date(), rs);
        } catch (NoUserException ne) {
            throw new NoUserException();
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
    }

 
    public ArrayList<User> getUsersByTypes(String type, ArrayList<Integer> branches) throws SQLException {
        ArrayList<User> retVal = new ArrayList<User>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_user_by_group_and_branches_query groupQuery = new get_user_by_group_and_branches_query();
        groupQuery.update(Integer.parseInt(this.companyId), type, branches);

        Record_Set rst = conn.executeQuery(groupQuery, "");
        for (int r = 0; r < rst.length(); r++) {
            retVal.add(new User(new Date(), rst));
            rst.moveNext();
        }
        return retVal;
    }
    
    @Override
    public ArrayList<User> getUsersByTypes(String type, String branch) throws SQLException {
        ArrayList<User> retVal = new ArrayList<User>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_user_by_group_query groupQuery = new get_user_by_group_query();
        groupQuery.setPreparedStatement(new Object[]{type, Integer.parseInt(this.companyId), Integer.parseInt(branch), Integer.parseInt(branch)});
        Record_Set rst = conn.executeQuery(groupQuery, "");
        for (int r = 0; r < rst.length(); r++) {
            retVal.add(new User(new Date(), rst));
            rst.moveNext();
        }
        return retVal;
    }

    @Override
    public User getUserByEmail(String email) throws SQLException {
        User retVal = new User();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_user_by_email_query groupQuery = new get_user_by_email_query();
        groupQuery.setPreparedStatement(new Object[]{email});
        Record_Set rst = conn.executeQuery(groupQuery, "");
        for (int r = 0; r < rst.length(); r++) {
            retVal = new User(new Date(), rst);
            rst.moveNext();
        }
        return retVal;
    }

    public User getUserByIdFromHash(Integer userId) throws SQLException {
        if (userCache.get(userId) == null) {
            userCache.put(userId, getUserById(userId));
        }
        return userCache.get(userId);
    }
    
    @Override
    public User getUserById(Integer userid) throws SQLException {
        User retVal = new User();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_user_by_id_query groupQuery = new get_user_by_id_query();
        groupQuery.setPreparedStatement(new Object[]{userid});
        Record_Set rst = conn.executeQuery(groupQuery, "");
        for (int r = 0; r < rst.length(); r++) {
            retVal = new User(new Date(), rst);
            rst.moveNext();
        }
        return retVal;
    }

    /**
     * Grabs users by type.
     *
     * @param type
     * @return
     */
    @Override
    public ArrayList<Contact> getUsersByType(String type, String branch) throws SQLException {
        ArrayList<Contact> retVal = new ArrayList<Contact>();
        ArrayList<User> myVal = this.getUsersByTypes(type, branch);
        for (int v = 0; v < myVal.size(); v++) {
            retVal.add(myVal.get(v));
        }
        return retVal;
    }

    public ArrayList<Group> getGroups() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Group> retVal = new ArrayList<Group>();
        get_groups_query groupQuery = new get_groups_query();
        groupQuery.setPreparedStatement(new Object[]{});
        try {
            Record_Set rst = conn.executeQuery(groupQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Group(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<Group> getGroupsForUser(int user_id) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Group> retVal = new ArrayList<Group>();
        get_user_groups_query groupQuery = new get_user_groups_query();
        groupQuery.setPreparedStatement(new Object[]{user_id});
        try {
            Record_Set rst = conn.executeQuery(groupQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Group(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
    public ArrayList<Branch> getBranchesForUserAndCompany(Integer userId, Integer companyId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Branch> retVal = new ArrayList<Branch>();
        get_branches_for_user_company_query groupQuery = new get_branches_for_user_company_query();
        groupQuery.setPreparedStatement(new Object[]{userId, companyId});
        try {
            Record_Set rst = conn.executeQuery(groupQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Branch(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
    public void clearForCompanyAndSaveBranchInfoForUser(Integer userId, Integer companyId, ArrayList<Integer> branches) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_and_clear_user_branch_company_query saveUserBranchCompany = new save_and_clear_user_branch_company_query();
        saveUserBranchCompany.update(userId, companyId, branches);
        try {
            conn.executeUpdate(saveUserBranchCompany, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUserToBranchAndCompany(Integer userId, Integer branchId, Integer companyId) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_user_branch_company_query groupQuery = new save_user_branch_company_query();
        groupQuery.setPreparedStatement(new Object[]{userId, companyId, branchId});
        try {
            conn.executeUpdate(groupQuery, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Integer saveUser(User user, boolean clearPassword) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Integer userId = user.getId();
        boolean isInsert = false;
        if (user.getUserId() == null) {
            try {
                get_user_next_seq_query seqQuery = new get_user_next_seq_query();
                seqQuery.setPreparedStatement(new Object[]{});
                Record_Set rst = conn.executeQuery(seqQuery, "");
                userId = rst.getInt(0);
                isInsert = true;
                user.setUserId(userId);
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
        save_user_query groupQuery = new save_user_query();
        groupQuery.update(user, isInsert, clearPassword);
        try {
            conn.executeUpdate(groupQuery, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    public ArrayList<User> getUsersWithEmailPassword() throws RetrieveDataException {
        ArrayList<User> retVal = new ArrayList<User>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_users_with_email_passwords_query groupQuery = new get_users_with_email_passwords_query();
        groupQuery.setPreparedStatement(new Object[]{});
        try {
            Record_Set rst = conn.executeQuery(groupQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new User(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<User> getUsersByLogin(String userName) throws RetrieveDataException, NoUserException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<User> retVal = new ArrayList<User>();
        try {
            get_active_users_by_login_query myLoginQuery = new get_active_users_by_login_query();
            myLoginQuery.update(userName);
            Record_Set rs = conn.executeQuery(myLoginQuery, "");
            if (rs.length() == 0) {
                throw new NoUserException();
            }
            for (int r = 0; r < rs.length(); r++) {
                retVal.add(new User(new Date(), rs));
                rs.moveNext();
            }
        } catch (NoUserException ne) {
            throw new NoUserException();
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<User> getUsersByCompany(int companyId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<User> retVal = new ArrayList<User>();
        try {
            get_users_by_company_query getUsersQuery = new get_users_by_company_query();
            getUsersQuery.setPreparedStatement(new Object[]{companyId});
            Record_Set rs = conn.executeQuery(getUsersQuery, "");
            for (int r = 0; r < rs.length(); r++) {
                retVal.add(new User(new Date(), rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
