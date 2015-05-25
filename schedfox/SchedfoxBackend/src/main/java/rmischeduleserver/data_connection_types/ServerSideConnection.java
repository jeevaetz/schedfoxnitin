/*
 * ServerSideConnection.java
 *
 * Created on January 13, 2005, 4:07 PM
 */

package rmischeduleserver.data_connection_types;
import schedfoxlib.model.util.Record_Set;
import java.io.*;
import java.net.*;
import org.w3c.dom.*;

import rmischeduleserver.*;
/**
 *
 * @author ira
 */
public class ServerSideConnection implements Serializable {
    String base = new String("http://schedsoft.com/sched/");
    
    /** Creates a new instance of ServerSideConnection */
    public ServerSideConnection() {
        
    }
    
    private URLConnection setUpMyURL(String location) {
        URL                 url;
        URLConnection       urlConn;
        try {
            url = new URL(base + location);
            urlConn = url.openConnection();
            
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded"
                    );
            return urlConn;
        } catch (Exception e) {
            
        }
        return null;
    }
        
    public Record_Set getInfo(String location, String data, String element) {
        if(data != null){
            data = "?&" + data;
        }
        System.out.println("Calling getInfo with " + location + data);
        return getInfoFromDatabase(location, data, element);
    }
    
    public void setInfo(String location, String data, DataInputStream dis) {
        if(data != null){
            data = "&" + data;
        }
        URLConnection       urlConn;
        DataOutputStream    printout;
        DataInputStream     input;
        
        try{
            urlConn = setUpMyURL(location);
            printout = new DataOutputStream (urlConn.getOutputStream ());
            printout.writeBytes(data);
            printout.flush();
            printout.close();
        }catch(Exception ex) {
            ex.printStackTrace();
        }

    }
    
    private Record_Set getInfoFromDatabase(String location, String data, String element) {
        URLConnection       urlConn;
        DataOutputStream    printout;
        DataInputStream     input;
        XMLReader myReader = new XMLReader();
        
        try{
            urlConn = setUpMyURL(location);
            printout = new DataOutputStream (urlConn.getOutputStream ());
            printout.writeBytes(data);
            printout.flush();
            printout.close();
            DataInputStream dis = new DataInputStream (urlConn.getInputStream ());
            myReader.parseStream(dis);
            
            if(myReader.checkError() == null){
                myReader.checkStatus();
                NodeList nl = myReader.getElementsByTagName(element);
                //Record_Set myReturnRecordSet = new Record_Set(myReader, nl);
                //return myReturnRecordSet;
                return new Record_Set();
            }
            
            return null;
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
}
