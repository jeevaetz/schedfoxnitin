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
import schedfoxlib.model.ClientRateTemplates;
import rmischeduleserver.mysqlconnectivity.queries.client.get_client_rate_templates_query;
import rmischeduleserver.mysqlconnectivity.queries.client.save_client_rate_template_query;

/**
 *
 * @author user
 */
public class ClientRateTemplatesController {

    private String companyId;

    private ClientRateTemplatesController(String companyId) {
        this.companyId = companyId;
    }
    
    public static ClientRateTemplatesController getInstance(String companyId) {
        return new ClientRateTemplatesController(companyId);
    }

    public ArrayList<ClientRateTemplates> getActiveTemplates() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientRateTemplates> retVal = new ArrayList<ClientRateTemplates>();
        get_client_rate_templates_query clientRateQuery = new get_client_rate_templates_query();

        clientRateQuery.setCompany(companyId);
        clientRateQuery.setPreparedStatement(new Object[]{});
        try {
            Record_Set rst = conn.executeQuery(clientRateQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new ClientRateTemplates(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public void saveTemplate(ClientRateTemplates template) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_client_rate_template_query clientRateQuery = new save_client_rate_template_query();

        clientRateQuery.update(template);
        clientRateQuery.setCompany(companyId);
        try {
            conn.executeUpdate(clientRateQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
}
