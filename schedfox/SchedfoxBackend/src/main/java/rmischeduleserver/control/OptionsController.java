/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.HashMap;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_company_view_options_query;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class OptionsController {

    private String companyId;

    private OptionsController(String companyId) {
        this.companyId = companyId;
    }

    public static OptionsController getInstance(String companyId) {
        return new OptionsController(companyId);
    }

    public HashMap<Integer, Integer> getCompaniesToStartWeek() throws RetrieveDataException {
        HashMap<Integer, Integer> retVal = new HashMap<Integer, Integer>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            get_company_view_options_query viewQuery = new get_company_view_options_query();
            viewQuery.update(Integer.parseInt(companyId), 9);
            viewQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(viewQuery, "");
            retVal.put(Integer.parseInt(companyId), rst.getInt("myvalue"));
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public HashMap<Integer, Integer> getMinutesRoundingForCompany() throws RetrieveDataException {
        HashMap<Integer, Integer> retVal = new HashMap<Integer, Integer>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            get_company_view_options_query viewQuery = new get_company_view_options_query();
            viewQuery.update(Integer.parseInt(companyId), 11);
            viewQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(viewQuery, "");
            retVal.put(Integer.parseInt(companyId), rst.getInt("myvalue"));
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
