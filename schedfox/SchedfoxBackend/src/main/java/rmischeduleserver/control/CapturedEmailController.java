/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.captured_email.save_captured_email_query;
import schedfoxlib.controller.CapturedEmailInterface;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.CapturedEmail;

/**
 *
 * @author ira
 */
public class CapturedEmailController implements CapturedEmailInterface {
    private String companyId;

    private CapturedEmailController(String companyId) {
        this.companyId = companyId;
    }

    public static CapturedEmailController getInstance(String companyId) {
        return new CapturedEmailController(companyId);
    }

    public void saveCapturedemail(CapturedEmail email) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        save_captured_email_query saveQuery = new save_captured_email_query();
        saveQuery.setCompany(companyId);
        saveQuery.update(email);
        try {
            conn.executeUpdate(saveQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }
}
