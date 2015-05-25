/*
 * LockedConnection.java
 *
 * Created on September 14, 2005, 12:17 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.data_connection_types;
import java.sql.*;
import rmischeduleserver.mysqlconnectivity.mysqldriver;
import rmischeduleserver.mysqlconnectivity.sqldriver;
/**
 *
 * @author Ira Juneau
 */
public class LockedConnection {
    
    private Connection myConnection;
    private sqldriver driver;
    private boolean isInUse;
    private Statement myStatement;
    
    /** Creates a new instance of LockedConnection */
    public LockedConnection(Connection myConn, sqldriver driver) {
        myConnection = myConn;
        this.driver = driver;
        isInUse = false;
    }
    
    public @Deprecated boolean isLocked() {
        return (isInUse);
    }
    
    
    /**
     *  Checks to see if this connection is free or not.  If the connection is free,
     *  it gets flagged as in use, so DO NOT USE THIS AS A TEST ONLY.  Only call this
     *  function when you intend to use the connection if it is free.  This was created
     *  to ensure thread safety in obtaining these connections.  This should really only
     *  ever get used by ClientConnectionPoolFactory
     */
    public synchronized boolean isFreeConnection() {
        if(isInUse) return false;
        
        isInUse = true;
        return true;
    }
    
    
    public void close() {
        isInUse = false;
    }
    
    
    public Statement createStatement() throws SQLException {
        checkConnection();
        if (driver instanceof mysqldriver) {
            myStatement = myConnection.createStatement(ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE);
        } else {
            myStatement = myConnection.createStatement();
        }
        return myStatement;
    }
    
    /**
     * Ok gets a statement object which is pretty much a connection to the database, it will
     * also close any open statements (connections) before creating any new ones. Put here because
     * if you close a Statement while still creating a Record_Set it will close resultset this could
     * cause some problems in a bit!
     */
    public Statement createStatement(int param1, int param2) throws SQLException {
        checkConnection();
        myStatement = myConnection.createStatement(param1, param2);        
        return myStatement;
    }

    public PreparedStatement prepareStatement(String sql, int param1, int param2) throws SQLException {
        checkConnection();
        return myConnection.prepareStatement(sql, param1, param2);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkConnection();
        return myConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public void releaseStatement(CallableStatement stmt) {
        try { myStatement.close(); }
        catch (Exception e) { }
        finally {
            try { myConnection.commit(); }
            catch (Exception ex) { }
            myStatement = null;
            isInUse = false;
        }
    }

    public void releaseStatement(Statement stmt) {
        try { myStatement.close(); }
        catch (Exception e) { } 
        finally {
            try { myConnection.commit(); } 
            catch (Exception ex) { } 
            myStatement = null;            
            isInUse = false;
        }
    }
    
    
    public boolean checkConnection(){
        
        try{
            if(myConnection.isClosed()) {
                myConnection = driver.generateConnection();
            }
        } catch(Exception e) {
            try{ 
               myConnection = driver.generateConnection();
            } catch(Exception ex){ return false; }
        }
        
        return true;
    }
    
}
