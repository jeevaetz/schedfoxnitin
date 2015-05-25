/*
 * JWebServiceData.java
 *
 * Created on February 3, 2004, 8:43 AM
 */

package rmischeduleserver.data_connection_types;

import java.util.ArrayList;
import java.net.URLEncoder;
import java.io.*;
/**
 * hgmmmm
 * @author  jason.allen
 */
public class JWebServiceData implements Serializable {
    private ArrayList Data;
    
    /** Creates a new instance of JWebServiceData */
    public JWebServiceData(){
        Data = new ArrayList();
    }
    
    public void addData(String Name, String Value){
        Data.add(new JWebServiceDataPair(Name, Value));
    }

    public void addData(String Name, int Value){
        addData(Name,Integer.toString(Value));
    }
    
    public String getParm(){
        String str = "";
        JWebServiceDataPair jwsdp;
        int c;
        int i = Data.size();
        for(c=0;c<i;c++){
            if(str != ""){
                str += "&";
            }
            jwsdp = (JWebServiceDataPair) Data.get(c);
            str += UTF8Encoder(jwsdp.getName()) + "=" 
                 + UTF8Encoder(jwsdp.getValue());
        }
        return str;
    }
    
    private String UTF8Encoder(String str){
        try{
            return URLEncoder.encode(str, "UTF-8");
        }catch(java.io.UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }
        
    /**
     * Useful for debugging information, especially in the debugger... Will now display all information
     * in the JWebServiceData object...
     */
    public String toString() {
        String returnS = new String();
        for (int i = 0; i < Data.size(); i++) {
            JWebServiceDataPair temp = ((JWebServiceDataPair)Data.get(i));
            returnS = returnS + " " + temp.getName() + ": " + temp.getValue();
        }
        return returnS;
    }
    
    class JWebServiceDataPair implements Serializable{
        private String name;
        private String value;
        
        public JWebServiceDataPair(String Name,String Value){
            name = Name;
            value = Value;
        }
        
        public String getName(){return name;}
        public String getValue(){return value;}
        
        public void setName(String Name ){name = Name;}
        public void setValue(String Value){value= Value;}
    }
}


