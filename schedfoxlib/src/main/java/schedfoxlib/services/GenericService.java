package schedfoxlib.services;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;

import org.restlet.resource.ClientResource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import schedfoxlib.controller.GenericControllerInterface;
import schedfoxlib.model.IncidentReport;

public class GenericService implements GenericControllerInterface {

    private static String location = "Generic/";
    private String companyId;

    public static void main(String args[]) {
        GenericService cliService = new GenericService("2");
        try {
            SchedfoxLibServiceVariables.init();
            long value = cliService.getCurrentTimeMillis();
            Date myDate = new Date(value);
            System.out.println(myDate);
        } catch (Exception e) {
        }
    }

    public GenericService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public GenericService(String companyId) {
        this.companyId = companyId;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    @Override
    public long getCurrentTimeMillis() {
        ClientResource cr = new ClientResource(getLocation() + "getcurrenttimemillis/");
        try {
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), long.class);
        } catch (Exception exe) {
            return new Date().getTime();
        } finally {
            cr.getResponse().release();
        }
    }
}
