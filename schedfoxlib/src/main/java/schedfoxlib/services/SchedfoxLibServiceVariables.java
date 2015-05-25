package schedfoxlib.services;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;

import schedfoxlib.controller.registry.ControllerRegistryAbstract;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Method;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.restlet.data.Form;

import org.restlet.engine.Engine;
import org.restlet.engine.security.DefaultSslContextFactory;

import org.restlet.ext.httpclient.HttpClientHelper;
import org.restlet.resource.ClientResource;

public class SchedfoxLibServiceVariables {

    public static String companyId = "2";
    public static String serverLocation = "http://ws.patrolpro.com:8080/SchedfoxWebServices/resources/";
//    public static String serverLocation = "http://localhost:8080/SchedfoxWebServices/resources/";
//    public static String serverLocation = "http://50.180.255.12:8080/SchedfoxWebServices/resources/";
    private static final String dateFormat = "MM/dd/yyyy hh:mm:ss a";
    private static SimpleDateFormat format = new SimpleDateFormat(dateFormat);
    private static GsonBuilder gsonBuilder;
    private static Client httpClient;

    public static void init() {
        ControllerRegistryAbstract.setAddressController(AddressService.class);
        //ControllerRegistryAbstract.setBillingController(BillingController.class);
        //ControllerRegistryAbstract.setClientContractController(ClientContractController.class);
        ControllerRegistryAbstract.setClientController(ClientService.class);
        //ControllerRegistryAbstract.setCompanyController(CompanyController.class);
        ControllerRegistryAbstract.setEmployeeController(EmployeeService.class);
        ControllerRegistryAbstract.setGenericController(GenericService.class);
        ControllerRegistryAbstract.setProblemSolverInterface(ProblemSolverService.class);
        ControllerRegistryAbstract.setMobileFormInterface(MobileFormService.class);
        ControllerRegistryAbstract.setOfficerDailyReportInterface(OfficerDailyReportService.class);
        ControllerRegistryAbstract.setUserController(UserService.class);
        ControllerRegistryAbstract.setIncidentReportInterface(IncidentService.class);
        ControllerRegistryAbstract.setSalesInterface(SalesService.class);
        //ControllerRegistryAbstract.setHealthInterface(null);
        ControllerRegistryAbstract.setScheduleInterface(ScheduleService.class);
        ControllerRegistryAbstract.setScheduleInterface(OfficerDailyReportService.class);
        ControllerRegistryAbstract.setGPSInterface(GPSService.class);
        ControllerRegistryAbstract.setMessagingInterface(MessagingService.class);
        ControllerRegistryAbstract.setGeoFenceInterface(GeoFenceService.class);
    }

    public static Client getClient() {
        if (httpClient == null) {
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            DefaultSslContextFactory sslContextFactory = new DefaultSslContextFactory(); 
            sslContextFactory.setKeyStorePath(null);
            sslContextFactory.setTrustStorePath(null);

            Engine.getInstance().getRegisteredClients().clear();
            Context restletContext = new Context();
            restletContext.getParameters().add("tcpNoDelay", "true");
            restletContext.getParameters().set("maxTotalConnections", "48");
            restletContext.getParameters().set("maxConnectionsPerHost", "12");
            restletContext.getAttributes().put("sslContextFactory", sslContextFactory); 
            Engine.getInstance().getRegisteredClients().add(new HttpClientHelper(null));
            httpClient = new Client(restletContext, Protocol.HTTP);

            restletContext.setClientDispatcher(httpClient);

        }
        return httpClient;
    }

    public static Gson getGson() {
        if (gsonBuilder == null) {
            gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new SchedfoxLibServiceVariables.DateTimeDeserializer());
            gsonBuilder.registerTypeAdapter(Date.class, new SchedfoxLibServiceVariables.DateTimeSerializer());
            gsonBuilder.registerTypeAdapter(byte[].class, new SchedfoxLibServiceVariables.ByteArrayDeserializer());
        }
        return gsonBuilder.create();
    }

    public static void setRequestHeaders(ClientResource cr, String companyId) {
        Form headers = (Form) cr.getRequestAttributes().get("org.restlet.http.headers");
        if (headers == null) {
            headers = new Form();
            cr.getRequestAttributes().put("org.restlet.http.headers", headers);
        }
        headers.set("companyId", companyId);
    }

    public static class ByteArrayDeserializer implements JsonDeserializer<byte[]> {

        @Override
        public byte[] deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            try {
                if (json.getAsString().length() > 0) {
                    return com.sun.jersey.core.util.Base64.decode(json.getAsString());
                } else {
                    return new byte[0];
                }
            } catch (Exception e) {
                try {
                    Class normBase64 =  Class.forName("sun.misc.BASE64Decoder");
                    Object newInstance = normBase64.newInstance();
                    Method normDecode = normBase64.getMethod("decodeBuffer", String.class);
                    return (byte[]) normDecode.invoke(newInstance, json.getAsString());
                } catch (Exception exe) {
                    exe.printStackTrace();
                }
            }
            return null;
        }

    }

    public static class DateTimeDeserializer implements JsonDeserializer<Date> {

        private static SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private static SimpleDateFormat myFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String data = json.getAsString();
            try {
                if (data.contains("-")) {
                    try {
                        return myFormat.parse(data);
                    } catch (Exception exe) {
                        try {
                            return myFormat2.parse(data);
                        } catch (Exception exee) {
                            throw new JsonParseException("Invalid date: " + data);
                        }
                    }
                } else {
                    long time = Long.parseLong(data);
                    return new Date(time);
                }
            } catch (Exception exe) {
                return new Date(0);
            }
        }
    }

    public static class DateTimeSerializer implements JsonSerializer<Date> {

        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }
}
