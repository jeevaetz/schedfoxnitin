/*
 * LogClass.java
 *
 * Created on May 24, 2005, 2:31 PM
 */

package rmischeduleserver.ActionLogging;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.io.*;
/**
 *
 * @author ira
 */
public class LogClass implements java.io.Serializable {
    
    private String userid;
    private String branch;
    private String company;
    private String sql;
    private String message;
    private long time;
    private String classString;
    
    /** Creates a new instance of LogClass */
    public LogClass(GeneralQueryFormat gqf) {
       time = System.currentTimeMillis();
       sql = gqf.toString();
       userid = gqf.getUser();
       branch = gqf.getBranch();
       company = gqf.getCompany();
       message = gqf.getLoggingMessage();
       classString = gqf.getClass().toString();
    }
    
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        userid = (String)stream.readObject();
        branch = (String)stream.readObject();
        company = (String)stream.readObject();
        sql = (String)stream.readObject();
        message = (String)stream.readObject();
        classString = (String)stream.readObject();
        time = stream.readLong();
    }
    
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeChars(userid);
        stream.writeChars(branch);
        stream.writeChars(company);
        stream.writeChars(sql);
        stream.writeChars(message);
        stream.writeChars(classString);
        stream.writeLong(time);
    }
    
}
