package rmischeduleserver.util;

import rmischeduleserver.IPLocationFile;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vnguyen
 * Settings for mail classes, containter object for data
 * class pulls information for email server settings
 */
public class Mail {

    public static String host = IPLocationFile.getEMAIL_HOST();
    public static String port = IPLocationFile.getEMAIL_PORT();
    public static String from = IPLocationFile.getEMAIL_FROM();
    public static String user = IPLocationFile.getEMAIL_USER();
    public static String password = IPLocationFile.getEMAIL_PASSWORD();
    public static String SSL_FACTORY = IPLocationFile.getEMAIL_SSL_FACTORY();
    public static String recPort = IPLocationFile.getEMAIL_REC_PORT();
    public static String recProtocol = IPLocationFile.getEMAIL_REC_PROTOCOL();
    static String recHost = IPLocationFile.getEMAIL_REC_HOST();
    /* static String host = "champ.net";
    static String port = "25";
    static String from = "vnguyen@champ.net";
    static String user = "vnguyen@champ.net";
    static String password = "Pass:123";
    static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
     */

    /**
     * @return the host
     */
    public static String getHost() {
        return host;
    }

    /**
     * @param aHost the host to set
     */
    public static void setHost(String aHost) {
        host = aHost;
    }

    /**
     * @return the port
     */
    public static String getPort() {
        return port;
    }

    /**
     * @param aPort the port to set
     */
    public static void setPort(String aPort) {
        port = aPort;
    }

    /**
     * @return the from
     */
    public static String getFrom() {
        return from;
    }

    /**
     * @param aFrom the from to set
     */
    public static void setFrom(String aFrom) {
        from = aFrom;
    }

    /**
     * @return the user
     */
    public static String getUser() {
        return user;
    }

    /**
     * @param aUser the user to set
     */
    public static void setUser(String aUser) {
        user = aUser;
    }

    /**
     * @return the password
     */
    public static String getPassword() {
        return password;
    }

    /**
     * @param aPassword the password to set
     */
    public static void setPassword(String aPassword) {
        password = aPassword;
    }

    /**
     * @return the SSL_FACTORY
     */
    public static String getSSL_FACTORY() {
        return SSL_FACTORY;
    }

    /**
     * @param aSSL_FACTORY the SSL_FACTORY to set
     */
    public static void setSSL_FACTORY(String aSSL_FACTORY) {
        SSL_FACTORY = aSSL_FACTORY;
    }
}
