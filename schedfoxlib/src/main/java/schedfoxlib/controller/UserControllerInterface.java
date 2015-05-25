/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.sql.SQLException;
import java.util.ArrayList;
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
public interface UserControllerInterface {
    
    public void clearForCompanyAndSaveBranchInfoForUser(Integer userId, Integer companyId, ArrayList<Integer> branches) throws SaveDataException;

    public ArrayList<Company> getCompaniesForUser(Integer userId) throws RetrieveDataException;
    
    public ArrayList<Branch> getBranchesForUserAndCompany(Integer userId, Integer companyId) throws RetrieveDataException;
    
    public User getUserById(Integer userid) throws SQLException;
    
    public ArrayList<Contact> getUsersByType(String type, String branch) throws SQLException;

    public ArrayList<User> getUsersByTypes(String type, String branch) throws SQLException;
    
    public ArrayList<User> getUsersByCompany(int companyId) throws RetrieveDataException;
    
    public ArrayList<Group> getGroupsForUser(int user_id) throws RetrieveDataException;
    
    public User getUserByLogin(String userName, String password) throws RetrieveDataException, NoUserException;
    
    public ArrayList<User> getUsersByLogin(String userName) throws RetrieveDataException, NoUserException;
    
    public User getUserByEmail(String email) throws SQLException;
    
    public ArrayList<User> getUsersWithEmailPassword() throws RetrieveDataException;
    
    public Integer saveUser(User user, boolean clearPassword) throws SaveDataException;
    
    public void saveUserToBranchAndCompany(Integer userId, Integer branchId, Integer companyId) throws SaveDataException;
}
