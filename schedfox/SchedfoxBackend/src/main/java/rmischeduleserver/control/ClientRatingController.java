/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control;

import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.ClientRating;
import rmischeduleserver.mysqlconnectivity.queries.client.get_last_rating_for_client_query;
import rmischeduleserver.mysqlconnectivity.queries.client.save_client_rating_query;
import schedfoxlib.controller.ClientRatingControllerInterface;

/**
 *
 * @author user
 */
public class ClientRatingController implements ClientRatingControllerInterface {
    private String companyId;

    private ClientRatingController(String companyId) {
        this.companyId = companyId;
    }

    public static ClientRatingController getInstance(String companyId) {
        return new ClientRatingController(companyId);
    }

    public ClientRating getLastClientRating(int client_id) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ClientRating retVal = new ClientRating();
        get_last_rating_for_client_query getQuery = new get_last_rating_for_client_query();
        getQuery.setCompany(companyId);
        getQuery.setPreparedStatement(new Object[]{client_id});

        try {
            Record_Set rst = conn.executeQuery(getQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new ClientRating(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public void saveRating(ClientRating clientRating) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        save_client_rating_query saveQuery = new save_client_rating_query();
        saveQuery.setCompany(companyId);
        saveQuery.update(clientRating);
        try {
            conn.executeUpdate(saveQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }
}
