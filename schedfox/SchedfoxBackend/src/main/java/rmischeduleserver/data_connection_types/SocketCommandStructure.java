/*
 * SocketCommandStructure.java
 *
 * Created on February 10, 2005, 8:19 AM
 */

package rmischeduleserver.data_connection_types;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.io.Serializable;
import java.util.*;
/**
 *
 * @author ira
 */
public class SocketCommandStructure implements Serializable {
    
    public int Command;
    public String Branch;
    public String Company;
    public String Management_DB;
    public String MD5;
    public String userId;
    public String message;
    public String lastUpdate;
    private ArrayList myRecordSet;
    
    /** Creates a new instance of SocketCommandStructure */
    public SocketCommandStructure(GeneralQueryFormat myQuery) {
        Command = myQuery.getUpdateStatus();
        Branch = myQuery.getBranch();
        Company = myQuery.getCompany();
        Management_DB = myQuery.getManagementSchema();
        MD5 = myQuery.getMD5();
        userId = myQuery.getUser();
        lastUpdate = myQuery.getRanTime();
        myRecordSet = new ArrayList();
    }
    
    /**
     * Ok why do we have this, well this was a huge problem before I was using the same
     * SocketCommandStructure to send to all clients, hmm the problem is the Record_Set was being 
     * cleared and also being written to sockets at the same time...so this constructor should 
     * be used to create a new SocketCommandStructure for each client...
     */
    public SocketCommandStructure(SocketCommandStructure cloneme) {
        Command = cloneme.Command;
        Branch = cloneme.Branch;
        Company = cloneme.Company;
        Management_DB = cloneme.Management_DB;
        MD5 = cloneme.MD5;
        lastUpdate = cloneme.lastUpdate;
        myRecordSet = new ArrayList();
        message = cloneme.message;
    }
    
    public SocketCommandStructure(String mymessage, String md5) {
        Command = 7;
        MD5 = md5;
        message = mymessage;
        Branch = "";
        Company = "";
        Management_DB = "";
        myRecordSet = new ArrayList();
    }

    public SocketCommandStructure(int command, String md5) {
        Command = command;
        MD5 = md5;
        message = "";
        Branch = "";
        Company = "";
        Management_DB = "";
        myRecordSet = new ArrayList();
    }
    
    public SocketCommandStructure(int command) {
        Command = command;
        MD5 = "";
        message = "";
        Branch = "";
        Company = "";
        Management_DB = "";
        myRecordSet = null;
    }
    
    public SocketCommandStructure(String mess) {
        Command = 7;
        message = mess;
        MD5 = "";
        Branch = "";
        Company = "";
        Management_DB = "";
        myRecordSet = null;
    }
    
    public void setRecordSet(ArrayList newrs) {
        myRecordSet.add(newrs);
    }
    
    public void clearRecordSet() {
        myRecordSet = new ArrayList();
    }
    
    public ArrayList getRecordSet(int pos) {
        return (ArrayList)myRecordSet.get(pos);
    }
    
    public String toString() {
        return "Command: " + Command + " Branch: " + Branch + " Company: " + Company + " Management_DB: " + Management_DB + " MD5: " + MD5;
    }
    
}
