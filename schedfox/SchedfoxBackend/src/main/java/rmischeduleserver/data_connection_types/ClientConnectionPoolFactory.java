/*
 * ClientConnectionPool.java
 *
 * Created on September 14, 2005, 11:58 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.data_connection_types;
import java.util.*;
import java.sql.*;
import rmischeduleserver.ErrorReporting.ErrorReporting;
import rmischeduleserver.mysqlconnectivity.sqldriver;
/**
 *
 * @author Ira Juneau
 */
public class ClientConnectionPoolFactory {
    
    private Vector<LockedConnection> vectorOfConnections;

    private int initialSize = 4;
    private int growBySize = 2;
    private int killMeSize = 20;

    private sqldriver driver;
    
    /** Creates a new instance of ClientConnectionPool */
    public ClientConnectionPoolFactory() { }

    public void initializeFactory(sqldriver driver, long amountToWait) {
        vectorOfConnections = new Vector(initialSize);
        this.driver = driver;
        for (int i = 0; i < initialSize; i++) {
            vectorOfConnections.add(new LockedConnection(generateConnection(), driver));
            try {
                Thread.sleep(amountToWait); //Have to wait for good ol Oracle...
            } catch (Exception e) {}
        }
    }
    
    
    public void close() { }
    
    
    public LockedConnection getFreeConnection() {
        for (int i = 0; i < vectorOfConnections.size(); i++) {
            if (vectorOfConnections.get(i).isFreeConnection()) {
                return vectorOfConnections.get(i);
            }
        }
        
        for (int i = 0; i < growBySize; i++) {
            vectorOfConnections.add(new LockedConnection(generateConnection(), driver));
            if (vectorOfConnections.size() == killMeSize) {
                ErrorReporting.sendErrorReport(new Exception("Too many Database connections! (" + vectorOfConnections.size() + ")  Shutting down"), "Server");
                System.exit(1);
            }
        }
        System.out.println("Number of DB connections in vector: " + vectorOfConnections.size());
        
        return getFreeConnection();
    }
    
    
    public Connection generateConnection() {
        try {
            return driver.generateConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
