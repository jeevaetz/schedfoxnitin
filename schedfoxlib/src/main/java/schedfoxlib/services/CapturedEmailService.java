/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.google.gson.Gson;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.CapturedEmailInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.CapturedEmail;

/**
 *
 * @author ira
 */
public class CapturedEmailService implements CapturedEmailInterface {
    private static String location = "CapturedEmail/";
    private String companyId = "2";

    public CapturedEmailService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public CapturedEmailService(String companyId) {
        this.companyId = companyId;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    public static void main(String args[]) {
        CapturedEmail capt = new CapturedEmail();
        capt.setCapturedEmail("irajuneau79@yahoo.com");
        
        CapturedEmailService service = new CapturedEmailService();
        try {
            service.saveCapturedemail(capt);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }
    
    @Override
    public void saveCapturedemail(CapturedEmail email) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "savecapturedemail/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(email)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
}
