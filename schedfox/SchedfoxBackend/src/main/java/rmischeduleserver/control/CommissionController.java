/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control;

import java.util.ArrayList;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Commission;
import rmischeduleserver.mysqlconnectivity.queries.commissions.get_commissions_query;
import rmischeduleserver.mysqlconnectivity.queries.commissions.save_commissions_query;

/**
 *
 * @author user
 */
public class CommissionController {
    private String companyId;

    private CommissionController(String companyId) {
        this.companyId = companyId;
    }

    public static CommissionController getInstance(String companyId) {
        return new CommissionController(companyId);
    }

    public void saveCommission(Commission commission) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        save_commissions_query saveQuery = new save_commissions_query();
        saveQuery.setCompany(companyId);
        saveQuery.update(commission);
        try {
            conn.executeUpdate(saveQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    public ArrayList<Commission> getCommissions() throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<Commission> retVal = new ArrayList<Commission>();

        get_commissions_query commissionsQuery = new get_commissions_query();
        commissionsQuery.setCompany(companyId);
        commissionsQuery.setPreparedStatement(new Object[]{});
        try {
            Record_Set rst = conn.executeQuery(commissionsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Commission(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
