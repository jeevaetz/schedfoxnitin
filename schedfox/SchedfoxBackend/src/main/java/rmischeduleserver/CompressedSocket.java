/*
 * CompressedSocket.java
 *
 * Created on November 4, 2005, 9:51 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver;
import java.net.Socket;
import java.io.*;
import java.util.zip.*;
/**
 *
 * @author Ira Juneau
 */
public class CompressedSocket extends Socket {
    
    private InputStream in_str;
    private OutputStream out_str;
  
    /** Creates a new instance of CompressedSocket */
    public CompressedSocket() {
        super();
    }
    
    public CompressedSocket(String host, int port) throws IOException {
        super(host, port);
    }
    
    synchronized public InputStream getInputStream() throws IOException {
        if ( in_str==null ) {
            in_str=new GZIPInputStream(super.getInputStream());
        }
        return in_str;
    }
    
    synchronized public OutputStream getOutputStream() throws IOException {
        if ( out_str==null ) {
            out_str=new GZIPOutputStream(super.getOutputStream());
        }
        return out_str;
    }
    
    public synchronized void close() throws IOException {
        OutputStream o_str=getOutputStream();
        o_str.flush();
        super.close();
    }
}
