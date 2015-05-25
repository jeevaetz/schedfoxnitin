/*
 * Jar_Images.java
 *
 * Created on July 21, 2004, 1:19 PM
 */

package rmischedule.components;

import java.net.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.image.*;
import rmischedule.main.Main_Window;
import rmischedule.components.*;
/**
 *
 * @author  jason.allen
 *
 * Modified: December 09, 2004 by Ira Juneau,
 *           getImageFromJAR will now check to see if it has already loaded the image
 *           if so will return image immediately from structure rather than keep reloading
 *           it, might speed things up a tad...
 */
public class Jar_Images {
    Main_Window parent;
    Vector imageData; 
    
    /** Creates a new instance of Jar_Images */
    public Jar_Images(Main_Window p) {
        parent = p;
        imageData = new Vector();
    }
    
    public BufferedImage getBufferedImageFromJAR(String fileName){
        if( fileName == null ) return null;
//        int pos = getIndexOfObject(fileName);
        
//       if (pos >= 0) {
//          return ((JARImage)imageData.get(pos)).myImage;
//      }
        BufferedImage image = null;
//        byte[] thanksToNetscape = null;
        
        try{
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            URL url = parent.getClass().getResource("/rmischedule/" + fileName);
            image = javax.imageio.ImageIO.read(url);
        } catch(Exception exc){
            System.out.println( exc +" getting resource " +fileName );
            //image = getImageFromFile(fileName);
        }
        if (image != null) {
            //imageData.add(new JARImage(fileName, image));
        }
        return image;        
    }
    
    public Image getImageFromJAR(String fileName){
        if( fileName == null ) return null;
        int pos = getIndexOfObject(fileName);
        
        if (pos >= 0) {
            return ((JARImage)imageData.get(pos)).myImage;
        }
        Image image = null;
        byte[] thanksToNetscape = null;
        
        try{
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            URL url = parent.getClass().getResource("/rmischedule/" + fileName);
            image = toolkit.createImage(url);
        } catch(Exception exc){
            System.out.println( exc +" getting resource " +fileName );
            image = getImageFromFile(fileName);
        }
        if (image != null) {
            imageData.add(new JARImage(fileName, image));
        }
        return image;
    }
    
    public Image getImageFromFile(String fileName){
      if( fileName == null ) return null;
      Toolkit toolkit;
      try
      {
        byte[] thanksToNetscape = null;
        toolkit = Toolkit.getDefaultToolkit();
        // this is just incase I'm in my editor --jason
        Image image  = toolkit.createImage("C:/Inetpub/wwwroot/sched/rmischedule/"+fileName);

         // Use a media tracker object to wait until all the pixels         
         // have been retrieved.
         /*
         MediaTracker tracker = new MediaTracker(this);
         tracker.addImage(image, 0);
         tracker.waitForID(0);
         */
         if(image == null){System.out.println("file");}
         return image ;
      }catch (Exception e){
          
         e.printStackTrace();
         System.out.println("here");
         return new BufferedImage(10,10,BufferedImage.TYPE_3BYTE_BGR) ;
      }
    }
    
    public int getIndexOfObject(String fileName) {
        for (int i = 0; i < imageData.size(); i++) {
            if (fileName.compareTo(((JARImage)imageData.get(i)).myFileName) == 0) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Created by Ira Juneau used to contain data for Image files...
     */
    private class JARImage extends Object{
        public String myFileName;
        public Image myImage;
        public JARImage(String fileName, Image image) {
            myImage = image;
            myFileName = fileName;
        }
        
    }
    
}
