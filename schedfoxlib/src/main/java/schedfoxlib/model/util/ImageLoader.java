/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 *
 * @author user
 */
public class ImageLoader {

    private static String getLocationOfImageServer() {
        return "http://schedfoximage.schedfox.com:8080/ImageServer/";
    }
    
    public static ArrayList<String> getImageNames(String prefix, String fileExtension, String fileSeperator, String companySchema, String typeOfImage) throws Exception {
        ArrayList<String> retVal = new ArrayList<String>();

        String params = "?filePrefix=" + prefix + "&fileExtension=" + fileExtension
                + "&company=" + companySchema + "&type=" + typeOfImage + "&fileSeperator=" + fileSeperator;

        URL imageServerRetrieval = new URL(getLocationOfImageServer() + "GetImagesAssociatedServlet" + params);
        HttpURLConnection urlConn = (HttpURLConnection) imageServerRetrieval.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setRequestMethod("GET");
        urlConn.setRequestProperty("Connection", "Keep-Alive");

        urlConn.connect();
        BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
        String imageNames = null;
        try {
            byte[] buf = new byte[1024];
            int numRead = 0;
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            while ((numRead = iStream.read(buf)) > -1) {
                oStream.write(buf, 0, numRead);
            }
            imageNames = new String(oStream.toByteArray());
            if (imageNames.startsWith("Error:")) {
                throw new Exception("No Images");
            }
        } catch (Exception e) {
            //Fail silently, this just means there is probably no image associated w/ these stats.
        }
        return retVal;
    }

    public static String getImageURL(String fileName, String companySchema, String typeOfImage) {
        String params = "?file=" + fileName + "&company=" + companySchema + "&type=" + typeOfImage;
        return getLocationOfImageServer() + "RetrieveImageServlet" + params;
    }

    public static ImageIcon getImage(String fileName, String companySchema, String typeOfImage) throws Exception {
        ImageIcon retVal = null;

        URL imageServerRetrieval = new URL(getImageURL(fileName, companySchema, typeOfImage));
        HttpURLConnection urlConn = (HttpURLConnection) imageServerRetrieval.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setRequestMethod("GET");
        urlConn.setRequestProperty("Connection", "Keep-Alive");

        urlConn.connect();
        BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
        try {
            retVal = new ImageIcon(ImageIO.read(iStream));
        } catch (Exception e) {
            //System.out.println("No image for: " + urlConn);
            //Fail silently, this just means there is probably no image associated w/ these stats.
        }
        return retVal;
    }

    public static void saveFile(String fileName, String companySchema, String typeOfImage, File tempFile) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(getLocationOfImageServer() + "SaveImageServlet");

        MultipartEntity reqEntity = new MultipartEntity(
                HttpMultipartMode.BROWSER_COMPATIBLE);

        reqEntity.addPart("file",
                new StringBody(fileName));

        reqEntity.addPart("company",
                new StringBody(companySchema));

        reqEntity.addPart("type",
                new StringBody(typeOfImage));


        FileBody bin = new FileBody(tempFile);
        reqEntity.addPart("attachment_field", bin);


        httppost.setEntity(reqEntity);

        Object result = httpclient.execute(httppost);
    }

    /**
     * Pushes the image up to the image server.
     * @param fileName
     * @param companySchema
     * @param typeOfImage
     * @param imageIcon
     */
    public static void saveImage(String fileName, String companySchema, String typeOfImage, ImageIcon imageIcon) throws Exception {
        
        File tempFile = File.createTempFile("TempFile", fileName);
        BufferedImage buffered = new BufferedImage(
                imageIcon.getIconWidth(),
                imageIcon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffered.createGraphics();
        g.drawImage(imageIcon.getImage(), 0, 0, null);
        g.dispose();

        ImageIO.write(buffered, "jpg", tempFile);

        ImageLoader.saveFile(fileName, companySchema, typeOfImage, tempFile);
    }

    // This method returns a buffered image with the contents of an image
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean hasAlpha = false;

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    /**
     *  modifications by Jeffrey Davis for picture preview on 09/07/2010
     */
    public static ArrayList<String> getFileNames(String key, String companySchema,
            String fileExtension, String prefix, String typeOfImage, String fileSeperator) {
        ArrayList<String> returnList = new ArrayList<String>();
        String params = "?company=" + companySchema + "&key=" + key + "&fileExtension=" + fileExtension +
                "&filePrefix=" + prefix + "&type=" + typeOfImage +  "&fileSeperator=" + fileSeperator;

        URL imageServerRetrieval = null;
        try {
            imageServerRetrieval = new URL(getLocationOfImageServer() + "GetImagesAssociatedServlet" + params);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpURLConnection urlConn = null;
        try {
            urlConn = (HttpURLConnection) imageServerRetrieval.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        try {
            urlConn.setRequestMethod("GET");
        } catch (ProtocolException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlConn.setRequestProperty("Connection", "Keep-Alive");
        try {
            urlConn.connect();
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int numBytesRead = 0;
            BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
            while((numBytesRead = iStream.read(buffer)) > -1) {
                oStream.write(buffer, 0, numBytesRead);
            }

            String temp = new String(oStream.toByteArray());
            StringTokenizer myToken = new StringTokenizer(temp, "\r\n");
            while(myToken.hasMoreElements()) {
                returnList.add(myToken.nextToken());
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return returnList;
    }
    /**
     *  modifications by jeffrey Davis for picture preview on 09/07/2010 complete
     */

     public static ArrayList<ImageIcon> getImagePreviews(ArrayList<String> imageLocations,
             String companySchema, String typeOfImage, String key, String type) {
         ArrayList<ImageIcon> returnList = new ArrayList<ImageIcon>();


         for(int idx = 0;idx < imageLocations.size();idx ++) {
             ImageIcon retrievedImage = null;
             String params = getLocationOfImageServer() + "RetrievePreviewImageServlet" +
                     "?company=" + companySchema + "&file=" + imageLocations.get(idx) +
                     "&key=" + key + "&type=" + type;

           
             URL imageServerRetrieval = null;
             try {
                imageServerRetrieval = new URL(params);
             } catch (MalformedURLException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
             }
             HttpURLConnection urlConn = null;
             try {
                urlConn = (HttpURLConnection) imageServerRetrieval.openConnection();
             } catch (IOException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
             }
             urlConn.setDoInput(true);
             urlConn.setDoOutput(true);
             try {
                urlConn.setRequestMethod("GET");
             } catch (ProtocolException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
             }
             urlConn.setRequestProperty("Connection", "Keep-Alive");
             try {
                urlConn.connect();
             } catch (IOException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
             }
             BufferedInputStream iStream = null;
             try {
                iStream = new BufferedInputStream(urlConn.getInputStream());
             } catch (IOException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 retrievedImage = new ImageIcon(ImageIO.read(iStream));
             }
             catch (Exception ex) {
                 
             }
             if(retrievedImage != null) {
                 returnList.add(retrievedImage);
             }
         }

         return returnList;
     }

     public static void saveAdditionalImages(String companySchema, String type,
             String filePrefix, String fileExtension, String fileSeperator,
             String key, ImageIcon imageIcon) {
        
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(getLocationOfImageServer()
                + "SaveAdditionalImagesServlet");
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        
        try {
            File tempFile = File.createTempFile("TempFile".trim(), filePrefix.trim());
        
        BufferedImage buffered = new BufferedImage(
                imageIcon.getIconWidth(),
                imageIcon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffered.createGraphics();
        g.drawImage(imageIcon.getImage(), 0, 0, null);
        g.dispose();

        ImageIO.write(buffered, "jpg".trim(), tempFile);

        reqEntity.addPart("company", new StringBody(companySchema.trim()));
        reqEntity.addPart("type", new StringBody(type.trim()));
        reqEntity.addPart("filePrefix", new StringBody(filePrefix.trim()));
        reqEntity.addPart("fileExtension", new StringBody(fileExtension.trim()));
        reqEntity.addPart("fileSeperator", new StringBody(fileSeperator.trim()));
        reqEntity.addPart("key", new StringBody(key.trim()));

        
        FileBody bin = new FileBody(tempFile);
        reqEntity.addPart("attachment_field", bin);
        httppost.setEntity(reqEntity);

        Object result = httpclient.execute(httppost);
        result.toString();
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean removeImage(String fileName, String companySchema,
            String imageType, String key, String userId, String fileSeperator) {
        boolean success = true;

        //  create params
        String params = getLocationOfImageServer() + "RemoveImagesServlet"
                + "?company=" + companySchema + "&fullFile=" + fileName
                + "&type=" + imageType + "&key=" + key + "&user=" + userId
                + "&fileSeperator=" + fileSeperator;

        // create URL, set
        URL removeImageServer = null;
        try {
            removeImageServer = new URL(params);
        }
        catch(MalformedURLException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }

        //  create HTTP connection, set
        HttpURLConnection urlConn = null;
        try {
            urlConn = (HttpURLConnection) removeImageServer.openConnection();
        }
        catch(IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        urlConn.setDoOutput(true);
        urlConn.setDoInput(true);

        //  connect
        try {
            urlConn.setRequestMethod("GET");
        } catch (ProtocolException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        urlConn.setRequestProperty("Connection", "Keep-Alive");
        try {
            urlConn.connect();
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int numBytesRead = 0;
        BufferedInputStream iStream = null;
        try {
            iStream = new BufferedInputStream(urlConn.getInputStream());
            while((numBytesRead = iStream.read(buffer)) > -1) {
                oStream.write(buffer, 0, numBytesRead);
            }

            String temp = new String(oStream.toByteArray());
            if(temp.length() != 0 && temp.substring(0, 6).matches("Error:")) {
                success = false;
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }

       return success;
    }

}
