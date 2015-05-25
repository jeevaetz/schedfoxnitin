/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Branch;

/**
 *
 * @author ira
 */
public interface BranchControllerInterface {
    public ArrayList<Branch> getBranches() throws RetrieveDataException;
    
    public ArrayList<Branch> getBranchesForCompany() throws RetrieveDataException;
    
    public ArrayList<Branch> getBranchesForManagement(int managementId) throws RetrieveDataException;
    
    public Integer saveBranch(Branch branch) throws SaveDataException;
}
