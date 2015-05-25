/*
 * IPLocationFile.java
 *
 * Created on May 9, 2005, 7:50 AM
 */
package rmischeduleserver;

/**
 *
 * @Author ira
 * Stupid dummy class to hold our IP's
 */

/**
 *
 * @author jdavis
 * @see added IMAGE_SERVER_FILE_SEPARATOR string to distinguish between Linux and Windows servers
 */
public class IPLocationFile {

    //production
    private static int[] portSocketNumbersToTry = {5000, 5001, 5002, 5003};
    private static String DATABASE_NAME = "unique_manage";
    private static String LOCATION_OF_POSTGRES_SERVER = "//schedfoxdb.schedfox.com/";
//    private static String LOCATION_OF_POSTGRES_SERVER = "//192.168.1.19/";
    private static String LOCATION_OF_DATABASE_SETUP_SERVER = "http://schedfoxdb.schedfox.com:8080/BackendDatabaseManipulations";
    private static String LOCATION_OF_IMAGE_SERVER = "http://schedfoximage.schedfox.com/ImageServer/";
    private static String SMS_MODEM = "http://sms1.champ.net";
    private static String TRAINING_IP = "http://training.schedfox.com";
    private static String LOCATION_OF_SCHEMA_SERVER = "http://schedfoxdb.schedfox.com:8080/SchemaServer/";
    private static String IMAGE_SERVER_FILE_SEPARATOR = "/";
    private static String LOCATION_OF_RMI_SERVER = "127.0.0.1";
    private static String SMS_PORT = "4000";
    private static String EMAIL_HOST = "mail2.champ.net";
    private static String EMAIL_PORT = "587";
    private static String EMAIL_FROM = "sched.scheduler@gmail.com";
    private static String EMAIL_USER = "schedfox";
    private static String EMAIL_PASSWORD = "Sch3dF0x4m3";
    private static String EMAIL_SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static String EMAIL_REC_PORT = "995";
    private static String EMAIL_REC_PROTOCOL = "pop3";
    private static String EMAIL_REC_HOST = "pop.gmail.com";

    //Development
//    public static String LOCATION_OF_POSTGRES_SERVER =  "//192.168.1.24/";
    
    //Good ol ISS
//    private static String LOCATION_OF_POSTGRES_SERVER = "//207.238.56.51/"; //ISS
//    private static String LOCATION_OF_IMAGE_SERVER = "http://69.15.21.188:8080/ImageServer/";

    //Internal Development
//    public static String DATABASE_NAME = "SchedData";
//    public static String LOCATION_OF_POSTGRES_SERVER =  "//192.168.1.35/";
//    public static String LOCATION_OF_DATABASE_SETUP_SERVER = "http://schedfoxdb.schedfox.com:8080/BackendDatabaseManipulations";
////    public static String LOCATION_OF_IMAGE_SERVER = "http://67.152.98.8:8080/ImageServer/";
//    public static String SMS_MODEM = "http://207.238.56.23";

    //External Development
//    public static String DATABASE_NAME = "SchedData";
//    public static String LOCATION_OF_POSTGRES_SERVER = "//207.238.56.35/";
//    public static String LOCATION_OF_IMAGE_SERVER = "http://localhost:8080/ImageServer/";
////    public static String IMAGE_SERVER_FILE_SEPARATOR =  "\\";
//    public static String LOCATION_OF_DATABASE_SETUP_SERVER = "http://207.238.56.23:8080/BackendDatabaseManipulations";
//    public static String SMS_MODEM = "http://207.238.56.23";
//    public static String TRAINING_IP = "http://training.schedfox.com";
  //  public static String LOCATION_OF_SCHEMA_SERVER = "http://192.168.1.25:80/SchemaServer/";

    //Local Development
//    public static String DATABASE_NAME = "SchedData";
//    public static String LOCATION_OF_POSTGRES_SERVER = "//127.0.0.1/";
//    public static String LOCATION_OF_IMAGE_SERVER = "http://localhost:8080/ImageServer/";
//    public static String LOCATION_OF_DATABASE_SETUP_SERVER = "http://localhost:8080/BackendDatabaseManipulations";
//    public static String SMS_MODEM = "http://127.0.0.1/";



    /**
     * @return the portSocketNumbersToTry
     */
    public static int[] getPortSocketNumbersToTry() {
        return portSocketNumbersToTry;
    }

    /**
     * @return the DATABASE_NAME
     */
    public static String getDATABASE_NAME() {
        return DATABASE_NAME;
    }

    /**
     * @return the LOCATION_OF_POSTGRES_SERVER
     */
    public static String getLOCATION_OF_POSTGRES_SERVER() {
        return LOCATION_OF_POSTGRES_SERVER;
    }

    /**
     * @return the LOCATION_OF_DATABASE_SETUP_SERVER
     */
    public static String getLOCATION_OF_DATABASE_SETUP_SERVER() {
        return LOCATION_OF_DATABASE_SETUP_SERVER;
    }

    /**
     * @return the LOCATION_OF_IMAGE_SERVER
     */
    public static String getLOCATION_OF_IMAGE_SERVER() {
        return LOCATION_OF_IMAGE_SERVER;
    }

    /**
     * @return the SMS_MODEM
     */
    public static String getSMS_MODEM() {
        return SMS_MODEM;
    }

    /**
     * @return the TRAINING_IP
     */
    public static String getTRAINING_IP() {
        return TRAINING_IP;
    }

    /**
     * @return the LOCATION_OF_SCHEMA_SERVER
     */
    public static String getLOCATION_OF_SCHEMA_SERVER() {
        return LOCATION_OF_SCHEMA_SERVER;
    }

    /**
     * @return the IMAGE_SERVER_FILE_SEPARATOR
     */
    public static String getIMAGE_SERVER_FILE_SEPARATOR() {
        return IMAGE_SERVER_FILE_SEPARATOR;
    }

    /**
     * @return the LOCATION_OF_RMI_SERVER
     */
    public static String getLOCATION_OF_RMI_SERVER() {
        return LOCATION_OF_RMI_SERVER;
    }

    /**
     * @return the SMS_PORT
     */
    public static String getSMS_PORT() {
        return SMS_PORT;
    }

    /**
     * @return the EMAIL_HOST
     */
    public static String getEMAIL_HOST() {
        return EMAIL_HOST;
    }

    /**
     * @return the EMAIL_PORT
     */
    public static String getEMAIL_PORT() {
        return EMAIL_PORT;
    }

    /**
     * @return the EMAIL_FROM
     */
    public static String getEMAIL_FROM() {
        return EMAIL_FROM;
    }

    /**
     * @return the EMAIL_USER
     */
    public static String getEMAIL_USER() {
        return EMAIL_USER;
    }

    /**
     * @return the EMAIL_PASSWORD
     */
    public static String getEMAIL_PASSWORD() {
        return EMAIL_PASSWORD;
    }

    /**
     * @return the EMAIL_SSL_FACTORY
     */
    public static String getEMAIL_SSL_FACTORY() {
        return EMAIL_SSL_FACTORY;
    }

    /**
     * @return the EMAIL_REC_PORT
     */
    public static String getEMAIL_REC_PORT() {
        return EMAIL_REC_PORT;
    }

    /**
     * @return the EMAIL_REC_PROTOCOL
     */
    public static String getEMAIL_REC_PROTOCOL() {
        return EMAIL_REC_PROTOCOL;
    }

    /**
     * @return the EMAIL_REC_HOST
     */
    public static String getEMAIL_REC_HOST() {
        return EMAIL_REC_HOST;
    }

    //noreply@schedfox.com email credentials
    /*
    public static String EMAIL_HOST = "mail.schedfox.com";
    public static String EMAIL_PORT = "465";
    public static String EMAIL_FROM = "noreply@schedfox.com";
    public static String EMAIL_USER = "noreply";
    public static String EMAIL_PASSWORD = "1616gate";
    public static String EMAIL_SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    public static String EMAIL_REC_PORT = "995";
    public static String EMAIL_REC_PROTOCOL = "pop3";
    public static String EMAIL_REC_HOST = "pop.gmail.com";
    */
    /** Creates a new instance of IPLocationFile */
    public IPLocationFile() {
    }

    public static String getServerDescription() {
        if (getLOCATION_OF_RMI_SERVER().equals("127.0.0.1")) {
            return "Connected to Local Computer";
        } else if (getLOCATION_OF_RMI_SERVER().equals("192.168.1.6")) {
            return "Connected to Test Server 192.168.1.6";
        } else if (getLOCATION_OF_RMI_SERVER().equals("67.152.98.8")) {
            return "Connected to SchedFox Production Server";
        } else {
            System.out.println("------- IPLocation --:" + getLOCATION_OF_RMI_SERVER());
            return "Connected to unknown server at: " + getLOCATION_OF_RMI_SERVER();

        }
    }
}
