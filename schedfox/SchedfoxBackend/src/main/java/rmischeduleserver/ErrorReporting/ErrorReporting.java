/*
 * ErrorReporting.java
 *
 * Created on June 9, 2006, 8:02 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.ErrorReporting;
import java.net.*;
import java.io.*;

/**
 *
 * @author shawn
 */
public class ErrorReporting {
    
    public static void sendErrorReport(Exception error, String userName) {
        try {
            HttpURLConnection urlConnection;

            urlConnection = (HttpURLConnection)(new URL("http://192.168.1.107/SchedFox/SchedFoxError.php?error=" +
                                                        URLEncoder.encode(error.toString(), "UTF-8") + "&user=" +
                                                        URLEncoder.encode(userName, "UTF-8")).openConnection());
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");           
            urlConnection.getResponseMessage();
            
            
            String trace = getTraceString(error);
            urlConnection = (HttpURLConnection)(new URL("http://192.168.1.107/SchedFox/SchedFoxTrace.php?trace=" + 
                                                        URLEncoder.encode(trace, "UTF-8") + "&user=" +
                                                        URLEncoder.encode(userName, "UTF-8")).openConnection());
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");           
            urlConnection.getResponseMessage();                      
        }
        catch(Exception ex) { }
    }
    
    private static String getTraceString(Exception error) {      
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteStream, true);
        error.printStackTrace(ps);
        
        return byteStream.toString();
    }
}
