/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
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
public interface CompanyControllerInterface {

    public Branch getBranchById(Integer branchId) throws RetrieveDataException;

    public ArrayList<CompanyObj> getCompaniesForPatrolPro() throws RetrieveDataException;
    
    public ArrayList<CompanyObj> getAllCompanies() throws RetrieveDataException ;
    
    public Company getCompanyById(Integer companyId) throws RetrieveDataException;
    
    public CompanyObj getCompanyObjById(Integer companyId) throws RetrieveDataException;

    public CompanyView getCompanyView(int company_view_id) throws RetrieveDataException;
    
    public ArrayList<Branch> getBranchesForCompany(Integer companyId) throws RetrieveDataException;
    
    public Integer saveCompanyObj(CompanyObj company) throws SaveDataException;
    
    public Integer saveManagementClient(ManagementClient managementClient) throws SaveDataException;
    
    public String setupCompanySchema(String schemaName, String sourceSchema) throws Exception;
    
}
