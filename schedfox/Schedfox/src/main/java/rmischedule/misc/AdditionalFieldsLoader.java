/**
 *  FileName:  AdditionalFieldsLoader.java
 *  Date Created:  12/15/2010
 *  @author Jeffrey N. Davis
 *  Last Revision:
 */

//  package declaration
package rmischedule.misc;

//  import declarations
import schedfoxlib.model.util.FileLoader;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.util.IOUtils;
import rmischeduleserver.IPLocationFile;



/**
 *  Class Name:  AdditionalFieldsLoader
 *  @author jdavis
 *  Purpose of Class:  a class containing static methods for communicating
 *      with ImageServer for Additional Fields
 */
public class AdditionalFieldsLoader
{
    //  private method implementations
    /**
     *  Purpose of Method:  splits the file name provided in the params into
     *      a prefix and a sufix
     *  @param fileName -  a string representing the file name
     *  @return prefixSufix - a String array with two elements, the prefix of
     *      the file name and the sufix
     *  @see <code>AdditionalFieldsFileLoader.getFile()</code> for call
     */
    private static String[] getPrefixSufix(String fileName)
    {
        /*  parse out prefix sufix, return  */
        String[] prefixSufix = new String[2];
        Pattern pattern = Pattern.compile("\\.");
        String[] splitFileName = pattern.split(fileName);
        StringBuilder prefix = new StringBuilder();
        for(int idx = 0;idx < splitFileName.length;idx ++)
        {
            if( (idx + 2) <= splitFileName.length)
                prefix.append(splitFileName[idx]);
        }

        prefixSufix[0] = prefix.toString();
        String sufix = "." + splitFileName[splitFileName.length - 1];
        prefixSufix[1] = sufix;
        
        return prefixSufix;
    }

    /** Create Default instance */
    public AdditionalFieldsLoader()  {}

    /** Override toString */
    @Override
    public String toString()    {return "a class containing static methods " +
            "for communicating with ImageServer for Additional Fields";}

    /**
     *  Method Name:  saveFile
     *  Purpose of Method:  communicates w/ ImageServer to save a file for a
     *      specific additional field
     *  @param companySchema - a string describing the company schema
     *  @param key - a string describing the employee number
     *  @param fieldType - a string describing the additional field type
     *  @param file - the file to be saved
     *  @param fileName - the name of the file to be saved
     *  @return isSuccess - a boolean describing if the save was successfull
     */
    public static boolean saveFile(String companySchema, String key,
        String fieldType, File fileToSave, String fileName)
    {
        boolean isSuccess = true;

        //  setup client/post
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(IPLocationFile.getLOCATION_OF_IMAGE_SERVER() + "SaveAdditionalFieldsFileServlet");

        //  set multipart
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        //  add parts
        try
        {
            reqEntity.addPart("company", new StringBody(companySchema));
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            reqEntity.addPart("fieldType", new StringBody(fieldType));
        }
        catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            reqEntity.addPart("fullFile", new StringBody(fileName));
        }
        catch(UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            reqEntity.addPart("key", new StringBody(key));
        }
        catch(UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileBody bin = new FileBody(fileToSave);
        reqEntity.addPart("attachment_field", bin);

        //  set entity
        httppost.setEntity(reqEntity);

        //  execute
        Object response = null;
        try
        {
            response = httpclient.execute(httppost);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  check result
        String result = response.toString();
        if(result.length() == 0 || result.substring(0, 6).matches("Error:"))
                isSuccess = false;

        return isSuccess;
    }

    /**
     *  Purpose of Method:  hits the ImageServer to discover if a file is
     *      present for the additional field that calls this method
     *  @param companySchema - a string representing the company schema
     *  @param key - a string representing the employee ID
     *  @param fieldType - a string representing the additional field type
     *  @return hasFile - a boolean describing if the image server has the file
     *  @see CheckForFileServlet on ImageServer
     */
    public static boolean checkForFile(String companySchema, String key, String fieldType)
    {
        boolean hasFile = false;
        String params = null;

        //  set params
        try
        {
            params = IPLocationFile.getLOCATION_OF_IMAGE_SERVER() + "CheckAdditionalFieldsFileServlet" +
                    "?company=" + URLEncoder.encode(companySchema, "UTF-8") +
                    "&key=" + URLEncoder.encode(key, "UTF-8") +
                    "&fieldType=" + URLEncoder.encode(fieldType, "UTF-8");

        }
        catch (UnsupportedEncodingException ex)
        {
                Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  create URL, set
        URL checkForAdditionalFieldsFileServlet = null;
        try
        {
            checkForAdditionalFieldsFileServlet = new URL(params);
        }
        catch(MalformedURLException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
            hasFile = false;
        }

        //  create HTTP connection, set
        HttpURLConnection urlConn = null;
        try
        {
            urlConn = (HttpURLConnection) checkForAdditionalFieldsFileServlet.openConnection();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
            hasFile = false;
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);

        //  connect
        try
        {
            urlConn.setRequestMethod("GET");
        }
        catch (ProtocolException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
            hasFile = false;
        }
        urlConn.setRequestProperty("Connection", "Keep-Alive");
        try
        {
            urlConn.connect();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(FileLoader.class.getName()).log(Level.SEVERE, null, ex);
            hasFile = false;
        }
        
        //  read output from servlet
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int numBytesRead = 0;
        BufferedInputStream iStream = null;
        try
        {
            iStream = new BufferedInputStream(urlConn.getInputStream());
            while((numBytesRead = iStream.read(buffer)) > -1)
                oStream.write(buffer, 0, numBytesRead);

            String temp = new String(oStream.toByteArray());
            if(temp.length() == 0)
                hasFile = true;
            
        }
        catch (IOException ex)
        {
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
            hasFile = false;
        }
        

        return hasFile;
    }

    /**
     *  Purpose of Method:  hits image server for a file within a directory;
     *      there can only be one file in this directory
     *  @param companySchema - a string representing the company schema
     *  @param key - a string representing the employee ID
     *  @param fieldType - a string representing the additional field type
     *  @param fileName - a string representing the file name
     *  @return returnFile - a file object downloaded from Image Server
     *  @see GetAdditionalFieldsFileServlet on ImageServer
     */
    public static File getFile(String companySchema, String key, String fieldType, String fileName)
    {
        File retrievedFile = null;
        String[] prefixSufix = AdditionalFieldsLoader.getPrefixSufix(fileName);
        String prefix = prefixSufix[0];
        String sufix = prefixSufix[1];

        //  set params
        StringBuilder params = new StringBuilder();
        params.append(IPLocationFile.getLOCATION_OF_IMAGE_SERVER());
        params.append("GetAdditionalFieldsFileServlet");
        try
        {
            params.append("?company=" + URLEncoder.encode(companySchema, "UTF-8"));
            params.append("&key=" + URLEncoder.encode(key, "UTF-8"));
            params.append("&fieldType=" + URLEncoder.encode(fieldType, "UTF-8"));
            params.append("&fullFile=" + URLEncoder.encode(fileName, "UTF-8"));
        }
        catch (UnsupportedEncodingException ex)
        {
                Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  set HTTP connection
        HttpURLConnection urlConn = null;
        try
        {
            URL fileServerRetrieval = new URL(params.toString());
            urlConn = (HttpURLConnection) fileServerRetrieval.openConnection();
        }
        catch (IOException ex)
        {
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        try
        {
            urlConn.setRequestMethod("GET");
        }
        catch (ProtocolException ex)
        {
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlConn.setRequestProperty("Connection", "Keep-Alive");

        //  conncet
        try
        {
            urlConn.connect();
        }
        catch (IOException ex)
        {
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  read input from server
        BufferedInputStream iStream = null;
        byte[] byteArray = null;
        try
        {
            iStream = new BufferedInputStream(urlConn.getInputStream());
            byteArray = IOUtils.toByteArray(iStream);
        }
        catch (IOException ex)
        {
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            retrievedFile = File.createTempFile(prefix, sufix);
            retrievedFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(retrievedFile.getAbsolutePath());
            fos.write(byteArray);
            fos.flush();
            fos.close();
            iStream.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return retrievedFile;
    }

    /**
     *  Purpose of Method:  hits the Image Server to remove a file
     *  @param companySchema - a string representing the company schema
     *  @param key - a string representing the employee ID
     *  @param fieldType - a string representing the additional field type
     *  @param userId -  a string representing the current userId
     *  @param fileName - a string representing the fileName to remove
     *  @return removeSuccessfull - a boolean describing if the file was removed
     *  @see <code>RemoveAdditionalFieldsFileServlet</code> in Image Server for back
     *      end work
     */
    public static boolean removeFile(String companySchema, String key, String fieldType,
        String userId, String fileName)
    {
        boolean removeSuccessfull = true;
        String params = null;

        //  set params
        try
        {
            params = IPLocationFile.getLOCATION_OF_IMAGE_SERVER() + "RemoveAdditionalFieldsFileServlet" +
                    "?company=" + URLEncoder.encode(companySchema, "UTF-8") +
                    "&key=" + URLEncoder.encode(key, "UTF-8") +
                    "&fieldType=" + URLEncoder.encode(fieldType, "UTF-8") +
                    "&key=" + URLEncoder.encode(key, "UTF-8") +
                    "&user=" + URLEncoder.encode(userId, "UTF-8") +
                    "&fullFile=" + URLEncoder.encode(fileName, "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
                Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  create URL, set
        URL removeAdditionalFilesServer = null;
        try
        {
            removeAdditionalFilesServer = new URL(params);
        }
        catch(MalformedURLException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
            removeSuccessfull = false;
        }

        //  create HTTP connection, set
        HttpURLConnection urlConn = null;
        try
        {
            urlConn = (HttpURLConnection) removeAdditionalFilesServer.openConnection();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
            removeSuccessfull = false;
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);

        //  connect
        try
        {
            urlConn.setRequestMethod("GET");
        }
        catch (ProtocolException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
            removeSuccessfull = false;
        }
        urlConn.setRequestProperty("Connection", "Keep-Alive");
        try
        {
            urlConn.connect();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
            removeSuccessfull = false;
        }

        //  read output from servlet
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int numBytesRead = 0;
        BufferedInputStream iStream = null;
        try
        {
            iStream = new BufferedInputStream(urlConn.getInputStream());
            while((numBytesRead = iStream.read(buffer)) > -1)
                oStream.write(buffer, 0, numBytesRead);

            String temp = new String(oStream.toByteArray());
        }
        catch (IOException ex)
        {
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
            removeSuccessfull = false;
        }

        return removeSuccessfull;
    }

    /**
     *  Purpose of Method:  hits the ImageServer to determine all file names
     *      associated with a specific <code> DynamicFieldAdditionalFileType<code>
     *  @param companySchema - a string representing the company schema
     *  @param key - a string representing the employee ID
     *  @param fieldType - a string representing the additional field type
     *  @param fileType - a string representing the file type sought (ImageServer definition)
     *  @return fileNamesList - an array list of strings representing the file names
     *      associated with this fieldType
     */
    public static ArrayList<String> getFileNames(String companySchema, String key,
        String fieldType, String fileType)
    {
        ArrayList<String> fileNamesList = new ArrayList<String>();

        //  set params
        StringBuilder params = new StringBuilder();
        try
        {
            params.append(IPLocationFile.getLOCATION_OF_IMAGE_SERVER());
            params.append("GetAdditionalFieldsFileNamesAssociatedServlet");
            params.append("?company=" + URLEncoder.encode(companySchema, "UTF-8"));
            params.append("&key=" + URLEncoder.encode(key, "UTF-8"));
            params.append("&fieldType=" + URLEncoder.encode(fieldType, "UTF-8"));
            params.append("&type=" + URLEncoder.encode(fileType, "UTF-8"));
        }
        catch (UnsupportedEncodingException ex)
        {
                Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  set URl
        URL fileNamesServerRetrieval = null;
        try
        {
            fileNamesServerRetrieval = new URL(params.toString());
        }
        catch(MalformedURLException ex)
        {
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  set HTTP connection
        HttpURLConnection urlConn = null;
        try
        {
            urlConn = (HttpURLConnection) fileNamesServerRetrieval.openConnection();
        }
        catch(IOException ex)
        {
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        try
        {
            urlConn.setRequestMethod("GET");
        }
        catch(ProtocolException ex)
        {
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlConn.setRequestProperty("Connection", "Keep-Alive");

        //  connect
        try
        {
            urlConn.connect();
        }
        catch(IOException ex)
        {
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  read input from server
        try
        {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int numBytesRead = 0;
            BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
            while ((numBytesRead = iStream.read(buffer)) > -1)
            {
                oStream.write(buffer, 0, numBytesRead);
            }

            String temp = new String(oStream.toByteArray());
            StringTokenizer myToken = new StringTokenizer(temp, "\r\n");
            while (myToken.hasMoreElements())
            {
                fileNamesList.add(myToken.nextToken());
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(AdditionalFieldsLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fileNamesList;
    }
};
