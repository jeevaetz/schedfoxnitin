/**
 *  FileName:  SchemaServletLoader.java
 *  @author Jeffrey Davis
 *  Date Created:  11/18/2010
 *  Purpose of File:  file contains a class with static method for
 *      communicating with the Schema servlets
 */

//  package declarations
package rmischedule.misc;

//  import declarations
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import rmischeduleserver.IPLocationFile;

/**
 *  Class Name:  SchemaServletLoader
 *  Purpose of Class:  a class with static method for
 *      communicating with the Schema servlets
 */
public class SchemaServletLoader
{
    /** Create default instance */
    public SchemaServletLoader() {}

    /**
     *  Method Name:  getFileName
     *  Purpose of Method:  hits the schema server, executing a servlet which
     *      returns all filenames
     *  @return fileNames - an ArrayList of strings representing all filenames
     *  @see GetFileNamesServlet 
     */
    public static ArrayList<String> getFileNames()
    {
        //  create fileName to return, param
        ArrayList<String> fileNames = new ArrayList<String>();
        
        //  set URL
        URL schemaServerRetrieval = null;
        try
        {
            schemaServerRetrieval = new URL(IPLocationFile.getLOCATION_OF_SCHEMA_SERVER() + "GetFileNames");
        }
        catch(MalformedURLException ex)
        {
            ex.printStackTrace();
        }

        //  set HTTP connection
        HttpURLConnection urlConn = null;
        try
        {
            urlConn = (HttpURLConnection) schemaServerRetrieval.openConnection();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        try
        {
            urlConn.setRequestMethod("GET");
        }
        catch(ProtocolException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setRequestProperty("connection", "Keep-Alive");

        //  connect
        try
        {
            urlConn.connect();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        //  parse return value
        try
        {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int numBytesRead = 0;
            BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
            while((numBytesRead = iStream.read(buffer)) > -1)
            {
                oStream.write(buffer, 0, numBytesRead);
            }

            String temp = new String(oStream.toByteArray());
            StringTokenizer myToken = new StringTokenizer(temp, "\r\n");
            while(myToken.hasMoreElements())
            {
               fileNames.add(myToken.nextToken());
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return fileNames;
    }

    /**
     *  Method Name:  saveSchema
     *  Purpose of Method:  hits the schema server, executing a servlet
     *      which saves the schema
     *  @param schema - a string representing the schema to be saved
     *  @return isSuccess - a boolean describing if the servlet was executed
     *      successfully
     *  @see CopySchemaServlet
     */
    public static boolean saveSchema(String schema)
    {
        boolean isSuccess = false;
        String params = IPLocationFile.getLOCATION_OF_SCHEMA_SERVER() + "CopySchema"
            + "?schema=" + schema;

         //  set URL
        URL schemaServerRetrieval = null;
        try
        {
            schemaServerRetrieval = new URL(params);
        }
        catch(MalformedURLException ex)
        {
            ex.printStackTrace();
        }

        //  set HTTP connection
        HttpURLConnection urlConn = null;
        try
        {
            urlConn = (HttpURLConnection) schemaServerRetrieval.openConnection();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        try
        {
            urlConn.setRequestMethod("GET");
        }
        catch(ProtocolException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setRequestProperty("connection", "Keep-Alive");

        //  connect
        try
        {
            urlConn.connect();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        //  parse return value
        try
        {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int numBytesRead = 0;
            BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
            while((numBytesRead = iStream.read(buffer)) > -1)
            {
                oStream.write(buffer, 0, numBytesRead);
            }

            String temp = new String(oStream.toByteArray());
            StringTokenizer myToken = new StringTokenizer(temp, "\r\n");
            if(myToken.countTokens() == 0 )
                isSuccess = true;
            else
                isSuccess = false;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    /**
     *  Method Name:  restoreSchema
     *  Purpose of Method:  hits the schema server, executing a servlet
     *      which restores the schema
     *  @param filename - a string representing the fileName to be restored
     *  @return isSuccess - a boolean describing if the servlet was executed
     *      successfully
     *  @see RestoreSchemaServlet
     */
    public static boolean restoreSchema(String fileName)
    {
        boolean isSuccess = false;
        String params = IPLocationFile.getLOCATION_OF_SCHEMA_SERVER() + "RestoreSchema"
            + "?fileName=" + fileName;

         //  set URL
        URL schemaServerRetrieval = null;
        try
        {
            schemaServerRetrieval = new URL(params);
        }
        catch(MalformedURLException ex)
        {
            ex.printStackTrace();
        }

        //  set HTTP connection
        HttpURLConnection urlConn = null;
        try
        {
            urlConn = (HttpURLConnection) schemaServerRetrieval.openConnection();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        try
        {
            urlConn.setRequestMethod("GET");
        }
        catch(ProtocolException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setRequestProperty("connection", "Keep-Alive");

        //  connect
        try
        {
            urlConn.connect();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        //  parse return value
        try
        {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int numBytesRead = 0;
            BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
            while((numBytesRead = iStream.read(buffer)) > -1)
            {
                oStream.write(buffer, 0, numBytesRead);
            }

            String temp = new String(oStream.toByteArray());
            StringTokenizer myToken = new StringTokenizer(temp, "\r\n");
            if(myToken.countTokens() == 0 )
                isSuccess = true;
            else
                isSuccess = false;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    /**
     *  Method Name:  removeSchema
     *  Purpose of Method:  hits the schema server, executing a servlet
     *      which deletes the schema
     *  @param schema - a string representing the scheam to be deleted
     *  @return isSuccess - a boolean describing if the servlet was executed
     *      successfully
     *  @see DeleteSchemaServer
     */
    public static boolean removeSchema(String schema)
    {
        boolean isSuccess = false;
        String params = IPLocationFile.getLOCATION_OF_SCHEMA_SERVER() + "RemoveSchema"
            + "?schema=" + schema;

         //  set URL
        URL schemaServerRetrieval = null;
        try
        {
            schemaServerRetrieval = new URL(params);
        }
        catch(MalformedURLException ex)
        {
            ex.printStackTrace();
        }

        //  set HTTP connection
        HttpURLConnection urlConn = null;
        try
        {
            urlConn = (HttpURLConnection) schemaServerRetrieval.openConnection();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        try
        {
            urlConn.setRequestMethod("GET");
        }
        catch(ProtocolException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setRequestProperty("connection", "Keep-Alive");

        //  connect
        try
        {
            urlConn.connect();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        //  parse return value
        try
        {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int numBytesRead = 0;
            BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
            while((numBytesRead = iStream.read(buffer)) > -1)
            {
                oStream.write(buffer, 0, numBytesRead);
            }

            String temp = new String(oStream.toByteArray());
            StringTokenizer myToken = new StringTokenizer(temp, "\r\n");
            if(myToken.countTokens() == 0 )
                isSuccess = true;
            else
                isSuccess = false;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    /**
     *  Method Name:  permDeleteSchema
     *  Purpose of Method:  hits the schema server to delete a backup file
     *  @param fileName - a string representing the file to be deleted
     *  @return isSuccess - a boolean describing if the servlet was executed
     *      successfully
     *  @see PermDeleteSchema
     */
    public static boolean permDeleteSchema(String fileName)
    {
        boolean isSuccess = false;
        String params = IPLocationFile.getLOCATION_OF_SCHEMA_SERVER() + "PermDeleteSchema"
            + "?filename=" + fileName;

         //  set URL
        URL schemaServerRetrieval = null;
        try
        {
            schemaServerRetrieval = new URL(params);
        }
        catch(MalformedURLException ex)
        {
            ex.printStackTrace();
        }

        //  set HTTP connection
        HttpURLConnection urlConn = null;
        try
        {
            urlConn = (HttpURLConnection) schemaServerRetrieval.openConnection();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        try
        {
            urlConn.setRequestMethod("GET");
        }
        catch(ProtocolException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setRequestProperty("connection", "Keep-Alive");

        //  connect
        try
        {
            urlConn.connect();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        //  parse return value
        try
        {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int numBytesRead = 0;
            BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
            while((numBytesRead = iStream.read(buffer)) > -1)
            {
                oStream.write(buffer, 0, numBytesRead);
            }

            String temp = new String(oStream.toByteArray());
            StringTokenizer myToken = new StringTokenizer(temp, "\r\n");
            if(myToken.countTokens() == 0 )
                isSuccess = true;
            else
                isSuccess = false;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    /**
     *  Hits the <code>Schema Server</code> to retrieve the <b>DO NOT DISTURBE</b>
     *      list
     *  <p>This method hits the <code>GetDoNotDisturbListServlet</code>, which
     *      reads a file contained server side describing which schemas cannot
     *      be touched.  The servlet returns an <code>ArrayList<String></code>
     *      which each element being a scheman that cannot be touched
     *  @return dndList an <code>ArrayList<String></code> describing which schemas
     *      cannot be touched
     */
    public static ArrayList<String> getDndList()
    {
        ArrayList<String> dndList =  new ArrayList<String>();

        //  set URL
        URL schemaServerRetrieval = null;
        try
        {
            schemaServerRetrieval = new URL(IPLocationFile.getLOCATION_OF_SCHEMA_SERVER() + "GetDNDList");
        }
        catch(MalformedURLException ex)
        {
            ex.printStackTrace();
        }

        //  set HTTP connection
        HttpURLConnection urlConn = null;
        try
        {
            urlConn = (HttpURLConnection) schemaServerRetrieval.openConnection();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        try
        {
            urlConn.setRequestMethod("GET");
        }
        catch(ProtocolException ex)
        {
            ex.printStackTrace();
        }
        urlConn.setRequestProperty("connection", "Keep-Alive");

        //  connect
        try
        {
            urlConn.connect();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        //  parse return value
        try
        {
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int numBytesRead = 0;
            BufferedInputStream iStream = new BufferedInputStream(urlConn.getInputStream());
            while((numBytesRead = iStream.read(buffer)) > -1)
            {
                oStream.write(buffer, 0, numBytesRead);
            }

            String temp = new String(oStream.toByteArray());
            StringTokenizer myToken = new StringTokenizer(temp, "\r\n");
            while(myToken.hasMoreElements())
            {
               dndList.add(myToken.nextToken());
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return dndList;
    }
};
