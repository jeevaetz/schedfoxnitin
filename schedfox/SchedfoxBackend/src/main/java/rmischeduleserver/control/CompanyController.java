/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import rmischeduleserver.IPLocationFile;
import schedfoxlib.controller.CompanyControllerInterface;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_company_view_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.new_user.get_existing_schemas_for_co;
import rmischeduleserver.mysqlconnectivity.queries.util.check_if_time_to_run_report_for_company_query;
import rmischeduleserver.mysqlconnectivity.queries.util.check_if_time_to_run_report_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_all_companies_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_branch_by_company_query;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Company;
import rmischeduleserver.mysqlconnectivity.queries.util.get_branch_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_company_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_next_company_seq_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_next_management_client_seq_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_patrol_pro_companies_query;
import rmischeduleserver.mysqlconnectivity.queries.util.save_company_query;
import rmischeduleserver.mysqlconnectivity.queries.util.save_management_info_query;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.CompanyObj;
import schedfoxlib.model.CompanyView;
import schedfoxlib.model.ManagementClient;
import static schedfoxlib.services.SchedfoxLibServiceVariables.companyId;

/**
 *
 * @author user
 */
public class CompanyController implements CompanyControllerInterface {

    public CompanyController() {
    }

    public CompanyController(String companyId) {

    }

    public static CompanyController getInstance() {
        return new CompanyController();
    }

    
    
    @Override
    public ArrayList<CompanyObj> getCompaniesForPatrolPro() throws RetrieveDataException {
        ArrayList<CompanyObj> retVal = new ArrayList<CompanyObj>();
        try {
            get_patrol_pro_companies_query schemaQuery = new get_patrol_pro_companies_query();
            schemaQuery.setPreparedStatement(new Object[]{});
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(schemaQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                retVal.add(new CompanyObj(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public String setupCompanySchema(String schemaName, String sourceSchema) throws Exception {
        get_existing_schemas_for_co schemaQuery = new get_existing_schemas_for_co();
        schemaName = schemaName.replaceAll(",", "").replaceAll("\\.", "");
        schemaQuery.update(schemaName);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Record_Set rs = conn.executeQuery(schemaQuery, "");
        HashMap<Integer, String> myValues = new HashMap<Integer, String>();
        for (int i = 0; i < rs.length(); i++) {
            String currentSchema = rs.getString(1);
            currentSchema = currentSchema.replaceAll(schemaName, "");
            currentSchema = currentSchema.replaceAll("_db", "");
            myValues.put(i, rs.getString(1));
        }

        int freeId = -1;
        for (int i = 0; i < 100; i++) {
            //Check for a free id.
            if (myValues.get(i) == null) {
                freeId = i;
                i = 101;
            }
        }

        if (freeId == -1) {
            throw new Exception("There was a problem setting up your company, please contact us!");
        } else {
            String newSchemaName = schemaName + freeId + "_db";
            try {
                Thread.sleep(700);
            } catch (Exception e) {
            }

            URL setupDBURL = new URL(IPLocationFile.getLOCATION_OF_DATABASE_SETUP_SERVER()
                    + "/CopySchema?from=" + sourceSchema + "&to=" + newSchemaName
                    + "&database=" + IPLocationFile.getDATABASE_NAME());
            URLConnection urlConn = setupDBURL.openConnection();
            InputStream readStream = urlConn.getInputStream();
            readStream.read();

            try {
                Thread.sleep(700);
            } catch (Exception e) {
            }

            return newSchemaName;
        }
    }

    @Override
    public CompanyView getCompanyView(int company_view_id) throws RetrieveDataException {
        CompanyView retVal = new CompanyView();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_company_view_by_id_query viewQuery = new get_company_view_by_id_query();
        viewQuery.setPreparedStatement(new Object[]{company_view_id});
        try {
            Record_Set rst = conn.executeQuery(viewQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal = new CompanyView(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public Company getCompanyById(Integer companyId) throws RetrieveDataException {
        Company retVal = null;
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        get_company_by_id_query companyQuery
                = new get_company_by_id_query();
        companyQuery.setCompany(companyId + "");
        companyQuery.setPreparedStatement(new Object[]{companyId});
        try {
            Record_Set rst = conn.executeQuery(companyQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Company(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<Branch> getBranchesForCompany(Integer companyId) throws RetrieveDataException {
        ArrayList<Branch> retVal = new ArrayList<Branch>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        get_branch_by_company_query companyQuery = new get_branch_by_company_query(companyId + "");
        companyQuery.setCompany(companyId + "");
        try {
            Record_Set rst = conn.executeQuery(companyQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Branch(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public boolean companyShouldRunNow(Integer companyId) throws RetrieveDataException {
        boolean retVal = false;
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        check_if_time_to_run_report_for_company_query companyQuery = new check_if_time_to_run_report_for_company_query();
        companyQuery.setCompany(companyId + "");
        companyQuery.setPreparedStatement(new Object[]{companyId});
        try {
            Record_Set rst = conn.executeQuery(companyQuery, "");
            if (rst.length() > 0) {
                retVal = true;
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public boolean timeToRunReport(Integer branchId) throws RetrieveDataException {
        boolean retVal = false;
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        check_if_time_to_run_report_query companyQuery = new check_if_time_to_run_report_query();
        companyQuery.setCompany(companyId + "");
        try {
            Record_Set rst = conn.executeQuery(companyQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = rst.getBoolean("isrun");
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    @Override
    public Branch getBranchById(Integer branchId) throws RetrieveDataException {
        Branch retVal = null;
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_branch_by_id_query branchQuery
                = new get_branch_by_id_query();
        branchQuery.setCompany(branchId + "");
        branchQuery.setPreparedStatement(new Object[]{branchId});
        try {
            Record_Set rst = conn.executeQuery(branchQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Branch(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public Integer saveCompanyObj(CompanyObj company) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Integer retVal = new Integer(0);
        boolean isUpdate = true;
        try {
            if (company.getCompanyId() == null) {
                get_next_company_seq_query seqQuery = new get_next_company_seq_query();
                seqQuery.setPreparedStatement(new Object[]{});
                Record_Set rst = conn.executeQuery(seqQuery, "");
                retVal = rst.getInt(0);
                company.setCompanyId(retVal);
                isUpdate = false;
            } else {
                retVal = company.getCompanyId();
            }
            save_company_query query = new save_company_query();
            query.update(company, isUpdate);
            conn.executeUpdate(query, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
        return retVal;
    }

    public Integer saveManagementClient(ManagementClient managementClient) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Integer retVal = null;

        if (managementClient.getManagement_id() == null || managementClient.getManagement_id() == 0) {
            get_next_management_client_seq_query nextValQuery = new get_next_management_client_seq_query();
            try {
                nextValQuery.setPreparedStatement(new Object[]{});
                Record_Set rst = conn.executeQuery(nextValQuery, "");
                retVal = rst.getInt(0);
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        } else {
            retVal = managementClient.getManagement_id();
        }

        try {
            save_management_info_query managementQuery = new save_management_info_query();
            managementQuery.update(managementClient);
            managementClient.setManagement_id(retVal);
            conn.executeUpdate(managementQuery, "");
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return retVal;
    }

    @Override
    public ArrayList<CompanyObj> getAllCompanies() throws RetrieveDataException {
        ArrayList<CompanyObj> retVal = new ArrayList<CompanyObj>();
        try {
            get_all_companies_query schemaQuery = new get_all_companies_query();
            schemaQuery.setPreparedStatement(new Object[]{});
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            Record_Set rs = conn.executeQuery(schemaQuery, "");
            for (int i = 0; i < rs.length(); i++) {
                retVal.add(new CompanyObj(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public CompanyObj getCompanyObjById(Integer companyId) throws RetrieveDataException {
        CompanyObj retVal = null;
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        get_company_by_id_query companyQuery
                = new get_company_by_id_query();
        companyQuery.setCompany(companyId + "");
        companyQuery.setPreparedStatement(new Object[]{companyId});
        try {
            Record_Set rst = conn.executeQuery(companyQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new CompanyObj(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
