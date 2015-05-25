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
import schedfoxlib.model.UserCommissionCaps;
import rmischeduleserver.mysqlconnectivity.queries.user.get_user_commission_cap_by_user_and_year_query;
import rmischeduleserver.mysqlconnectivity.queries.user.get_user_commission_cap_query;
import rmischeduleserver.mysqlconnectivity.queries.user.save_user_commission_cap_query;

/**
 *
 * @author user
 */
public class UserCommissionCapsController {
    private String companyId;

    private UserCommissionCapsController(String companyId) {
        this.companyId = companyId;
    }

    public static UserCommissionCapsController getInstance(String companyId) {
        return new UserCommissionCapsController(companyId);
    }

    public UserCommissionCaps getCommissionCapByUserAndYear(int userId, int year) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        UserCommissionCaps retVal = new UserCommissionCaps();
        get_user_commission_cap_by_user_and_year_query getQuery = new get_user_commission_cap_by_user_and_year_query();
        getQuery.setCompany(companyId);
        getQuery.setPreparedStatement(new Object[]{userId, year});

        try {
            Record_Set rst = conn.executeQuery(getQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new UserCommissionCaps(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public void saveCommissionCaps(UserCommissionCaps caps) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        save_user_commission_cap_query getQuery = new save_user_commission_cap_query();
        getQuery.setCompany(companyId);
        getQuery.upate(caps);

        try {
            conn.executeUpdate(getQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    public ArrayList<UserCommissionCaps> getCommissionCapsForUser(int userId) throws RetrieveDataException {
        ArrayList<UserCommissionCaps> retVal = new ArrayList<UserCommissionCaps>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        get_user_commission_cap_query getQuery = new get_user_commission_cap_query();
        getQuery.setCompany(companyId);
        getQuery.setPreparedStatement(new Object[]{userId});

        try {
            Record_Set rst = conn.executeQuery(getQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new UserCommissionCaps(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
