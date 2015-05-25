/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control;

import java.util.ArrayList;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Branch;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_all_branch_query;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_all_branches_for_company_query;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_branches_for_management_query;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_next_branch_seq_query;
import rmischeduleserver.mysqlconnectivity.queries.util.save_branch_query;
import schedfoxlib.controller.BranchControllerInterface;

import schedfoxlib.controller.exceptions.SaveDataException;

/**
 *
 * @author user
 */
public class BranchController implements BranchControllerInterface {
    private String companyId;

    private BranchController(String companyId) {
        this.companyId = companyId;
    }

    public static BranchController getInstance(String companyId) {
        return new BranchController(companyId);
    }

    public Integer saveBranch(Branch branch) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Integer retVal = new Integer(0);

        boolean isUpdate = true;
        if (branch.getBranchId() == null) {
            isUpdate = false;
            try {
                get_next_branch_seq_query branchSeq = new get_next_branch_seq_query();
                branchSeq.setPreparedStatement(new Object[]{});
                branchSeq.setCompany(companyId);
                Record_Set rst = conn.executeQuery(branchSeq, "");
                retVal = rst.getInt(0);
                
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        } else {
            retVal = branch.getBranchId();
        }
        
        save_branch_query branchQuery = new save_branch_query();
        branch.setBranchId(retVal);
        branchQuery.update(branch, isUpdate);
        
        branchQuery.setCompany(companyId);
        try {
            conn.executeUpdate(branchQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
        return retVal;
    }
    
    public ArrayList<Branch> getBranches() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Branch> retVal = new ArrayList<Branch>();

        get_all_branch_query branchQuery = new get_all_branch_query();
        branchQuery.setPreparedStatement(new Object[]{});
        branchQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(branchQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Branch(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    public ArrayList<Branch> getBranchesForManagement(int managementId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Branch> retVal = new ArrayList<Branch>();

        get_branches_for_management_query branchQuery = new get_branches_for_management_query();
        branchQuery.setPreparedStatement(new Object[]{managementId});
        branchQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(branchQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Branch(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<Branch> getBranchesForCompany() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Branch> retVal = new ArrayList<Branch>();

        get_all_branches_for_company_query branchQuery = new get_all_branches_for_company_query();
        branchQuery.setPreparedStatement(new Object[]{Integer.parseInt(this.companyId)});
        branchQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(branchQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Branch(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }
}
