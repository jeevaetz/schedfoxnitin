/*
 * XMLReader.java
 *
 * Created on January 18, 2004, 7:14 AM
 */

package rmischeduleserver.data_connection_types;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.*;
import java.net.URLEncoder;
import java.net.URLDecoder;
import javax.swing.*;
import java.awt.*;
import java.io.*; 


/**
 *
 * @author  oracle
 */
public class XMLReader implements Serializable{
    /* 
     *  We will have a few static names for the more common stuff
     *  We will also (hopefully) have some object that this service will
     *  auto create for easier use.
     */
    
   public final static String ERROR         = "error";
   public final static String MD5           = "md5";
   public final static String STATUS        = "status";
   public final static String STATUS_CODE   = "statuscode";
   
   public Document document;
   private boolean hasError;
   private String statusCode;
   
   int holderForStartAndLength[] = new int[2];
   
    /** Creates a new instance of XMLReader */
    public XMLReader() {

    }

    public void parseStream(DataInputStream is){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            document = factory.newDocumentBuilder().parse(is);
        }catch(java.io.IOException e){
            e.printStackTrace();
        }catch(javax.xml.parsers.ParserConfigurationException e){
            e.printStackTrace();
        }catch(org.xml.sax.SAXException e){
            e.printStackTrace();
        }
    }

    public void parseStream(JFrame f, DataInputStream is){
    /*    try{
            InputStream in = new BufferedInputStream(new ProgressMonitorInputStream(f, "Reading Information", is));
            //ProgressMonitorInputStream progressIn = 
                //new ProgressMonitorInputStream(f, "Reading Information", is);            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //f.setVisible(true);
            //f.setSize(100, 100);
            //document = factory.newDocumentBuilder().parse(progressIn);
            //document = factory.newDocumentBuilder().parse(in);
        }catch(java.io.IOException e){
            e.printStackTrace();
        }catch(javax.xml.parsers.ParserConfigurationException e){
            e.printStackTrace();
        }catch(org.xml.sax.SAXException e){
            e.printStackTrace();
        } */
    }
    
    public String checkError(){
        NodeList error = getElementsByTagName(XMLReader.ERROR);
        if(error.getLength() == 0){
            return null;
        }else{
            hasError = true;
            return getNodeContents((Element) error.item(0));
        }
    }
    
    public String checkError(java.awt.Component f){
        String myErrorText = checkError();
        if(myErrorText != null){
            hasError = true;
            JOptionPane.showMessageDialog(f, myErrorText, "Error", JOptionPane.WARNING_MESSAGE);
        }
        return myErrorText;
    }

        
    public String checkStatus(){
        NodeList status_code = getElementsByTagName(XMLReader.STATUS_CODE);
        if(status_code.getLength() == 0){
            return null;
        }else{
            return getNodeContents((Element) status_code.item(0));
        }
    }
    
    public String checkStatus(java.awt.Component f){
        String myStatusCode = checkStatus();
        
        NodeList status = getElementsByTagName(XMLReader.STATUS);        
        if(status.getLength() > 0){
            String myStatusText = getNodeContents((Element) status.item(0));
            JOptionPane.showMessageDialog(f, myStatusText, "Status", JOptionPane.INFORMATION_MESSAGE);
        }
        return myStatusCode;
    }
    
    public NodeList getElementsByTagName(String el){
        return document.getElementsByTagName(el);
    }

    public Element getElementById(String id){
        return document.getElementById(id);
    }
    
    public String getValueById(String id){
        return getNodeContents(document.getElementById(id));
    }
    
    /* 
     *  use this ONLY if there is only 1 tag with said name
     *  this will only get the first tag 
     */
    public String getValueByTagName(String id){
        NodeList n = document.getElementsByTagName(id);
        return getNodeContents((Element) n.item(0));
    }
    
    public static String getNodeContents(Element elt) {        
        NodeList pc = elt.getChildNodes();
        Node node;
        String data = new String();
        for (int i2 = 0; i2 < pc.getLength(); i2++) {
            data = data + pc.item(i2).getNodeValue();
        }
        return data;
    }        
    
    public String UTF8Encoder(String str){
        try{
            return URLEncoder.encode(str, "UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }

    public String UTF8Decoder(String str){
        try{
            return URLDecoder.decode(str, "UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean hasError(){
        return hasError;
    }
    public String getStatusCode(){
        return statusCode;
    }
}
