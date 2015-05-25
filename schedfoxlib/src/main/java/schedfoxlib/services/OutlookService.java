/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Collection;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.OutlookControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.IncidentReport;

/**
 *
 * @author ira
 */
public class OutlookService implements OutlookControllerInterface {

    private static String location = "Outlook/";
    private String companyId;

    public OutlookService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public static void main(String[] args) {
        OutlookService outlookService = new OutlookService();
        outlookService.checkEmailPassword("bdavis@champ.net", "Password01");
    }
    
    public OutlookService(String companyId) {
        this.companyId = companyId;
    }
    
    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }
    
    @Override
    public boolean attemptEmailLogin(Integer userId) {
        ClientResource cr = new ClientResource(getLocation() + "attemptemaillogin/" + userId);
        try {
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Boolean.class);
        } catch (Exception exe) {
            return false;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public boolean checkEmailPassword(String email, String password) {
        ClientResource cr = null;;
        try {
            cr = new ClientResource(getLocation() + "checkemailpassword/" + URLEncoder.encode(email, "UTF-8") + "/" + URLEncoder.encode(password, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Boolean.class);
        } catch (Exception exe) {
            return false;
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public void downloadCalendar(Integer userId) {
        ClientResource cr = new ClientResource(getLocation() + "downloadcal/" + userId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.get();
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
    }
    
}
