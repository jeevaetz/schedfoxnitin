package schedfoxlib.sms;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author vnguyen
 */
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles creating connection for a http request get or post
 * @author vnguyen
 */
public class  HttpRequestHandler {
    
    private static final String SMS_LOCATION = "http://sms1.champ.net:4000/sendmsg";

       /**
     * Sends an HTTP GET request to a url
     *
     * @param endpoint - The URL of the server. (Example: " http://www.yahoo.com/search")
     * @param requestParameters - all the request parameters (Example: "param1=val1&param2=val2"). Note: This method will add the question mark (?) to the request - DO NOT add it yourself
     * @return - The response from the end point as a String ie web page requested
     */
    public static String sendGetRequest(String endpoint, String requestParameters) {
        String result = null;
        if (endpoint.startsWith("http://")) {
            // Send a GET request to the servlet
            try {
                // String to hold the full http request, url + querystring
                String urlStr = endpoint;

                if (requestParameters != null && requestParameters.length() > 0) {
                    // if request paramaters passed in, add them as the query string
                    urlStr += "?" + requestParameters;
                }
                //create a url object
                URL url = new URL(urlStr);
                //have the object make a request to the web server assign it to a connection object
                URLConnection conn = url.openConnection();
                // Get the response and assign it to a bugffer reader
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                //pull the the response into a stringbuffer
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                result = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //returns
        return result;
    }

    /**
     * Reads data from the data reader and posts it to a server via POST request.
     * data - The data you want to send
     * endpoint - The server's address
     * output - writes the server's response to output
     * @param data
     * @param endpoint
     * @param output
     * @throws Exception
     */
    public static void postData(Reader data, URL endpoint, Writer output) throws Exception {
        HttpURLConnection urlc = null;
        try {
            urlc = (HttpURLConnection) endpoint.openConnection();
            try {
                urlc.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new Exception("Shouldn't happen: HttpURLConnection doesn't support POST??", e);
            }

            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-type", "text/xml; charset=" + "UTF-8");

            OutputStream out = urlc.getOutputStream();
            try {
                Writer writer = new OutputStreamWriter(out, "UTF-8");
                pipe(data, writer);
                writer.close();
            } catch (IOException e) {
                throw new Exception("IOException while posting data", e);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            InputStream in = urlc.getInputStream();
            try {
                Reader reader = new InputStreamReader(in);
                pipe(reader, output);
                reader.close();
            } catch (IOException e) {
                throw new Exception("IOException while reading response", e);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            throw new Exception("Connection error (is server running at " + endpoint + " ?): " + e);
        } finally {
            if (urlc != null) {
                urlc.disconnect();
            }
        }
    }
    
    /**
     *  Method writes specifically to the SMS server; bandaid fix until TCP connections can 
     *      be written.
     *  @param params a string representing the information to be passed to the SMS server
     *  @return result a string representing the result from the sms server
     *  @since 05/26/2011
     */
    public static String sendSmsGetRequest ( String params )
    {
        //  set URL
        String fullParams = SMS_LOCATION + "?" + params;
        URL smsUrl = null;
        try
        {
            smsUrl = new URL ( fullParams );
        }
        catch ( MalformedURLException ex )
        {
            ex.printStackTrace();
        }
        
        //  set HTTP connection
        HttpURLConnection urlConn = null;
        try
        {
            urlConn = ( HttpURLConnection ) smsUrl.openConnection();
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        
       
        try
        {
            urlConn.setRequestMethod("GET");
        }
        catch ( ProtocolException ex )
        {
            ex.printStackTrace();
        }
        urlConn.setRequestProperty("Connection", "Keep-Alive");
        
        //  conncet
        try {
            urlConn.connect();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int numBytesRead = 0;
        BufferedInputStream iStream = null;
        try
        {
            iStream = new BufferedInputStream(urlConn.getInputStream());
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(HttpRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try 
        {
            while ((numBytesRead = iStream.read(buffer)) > -1) 
            {
                    oStream.write(buffer, 0, numBytesRead);
            }
        } catch (IOException ex) 
        {
            Logger.getLogger(HttpRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println ( "Message sent ");
        String temp = new String(oStream.toByteArray());
        
        //String result = null;
        
        return temp;
    }
    
    /**
     * Pipes everything from the reader to the writer via a buffer
     */
    private static void pipe(Reader reader, Writer writer) throws Exception {
        char[] buf = new char[1024];
        int read = 0;
        while ((read = reader.read(buf)) >= 0) {
            writer.write(buf, 0, read);
        }
        writer.flush();
    }
}