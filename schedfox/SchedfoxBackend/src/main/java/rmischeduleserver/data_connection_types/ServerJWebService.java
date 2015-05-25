/*
 * JWebService.java
 *
 * Created on January 17, 2004, 11:52 AM
 */

package rmischeduleserver.data_connection_types;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author  oracle
 */
public class ServerJWebService implements Serializable {
    private String base;
    
    public ServerJWebService(String myBase) {
        this.base = myBase;
    }
    
    public void setInformation(String service, String parm, DataInputStream dis) {
        URL                 url;
        URLConnection       urlConn;
        DataOutputStream    printout;
        DataInputStream     input;

        try{
            url = new URL(this.base + service);
            urlConn = url.openConnection();

            urlConn.setDoInput (true);
            urlConn.setDoOutput (true);
            urlConn.setUseCaches (false);
            urlConn.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded"
            );
            printout = new DataOutputStream(urlConn.getOutputStream());
            //Pass in our parameters into php file
            printout.writeBytes(parm);
            printout.flush();
            printout.close();
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception ex) {}

    }
    
    
    public DataInputStream getInformation(String service, String parm){
        URL                 url;
        URLConnection       urlConn;
        DataOutputStream    printout;
        DataInputStream     input;

        try{
            url = new URL(this.base + service);
            urlConn = url.openConnection();

            urlConn.setDoInput (true);
            urlConn.setDoOutput (true);
            urlConn.setUseCaches (false);
            urlConn.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded"
            );
            printout = new DataOutputStream (urlConn.getOutputStream ());
            printout.writeBytes(parm);
            printout.flush ();
            printout.close ();

               input = new DataInputStream (urlConn.getInputStream ());

           return input;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception ex) {}
        
        return null;
    } 
}
