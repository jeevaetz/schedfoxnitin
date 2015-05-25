/*
 * GeneralQueryFormat.java
 *
 * Created on January 18, 2005, 11:40 AM
 */
package rmischeduleserver.mysqlconnectivity.queries;

import java.io.*;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import rmischeduleserver.IPLocationFile;
import rmischeduleserver.mysqlconnectivity.sqldriver;

/**
 *
 * @author ira
 */
public abstract class GeneralQueryFormat implements Serializable {

    private String md5;
    private String userId;
    private String lastUpdated;
    private sqldriver driver;
    private String Branch;
    protected String Company;
    private String whenRan;
    private String managementDatabase;
    private String managementId;
    private boolean hasBranchFlag;
    private boolean hasManagementFlag;
    protected boolean shouldUpdateFlag;
    private static final long serialVersionUID = 2L; //Serialization Version for class should speed things up
    //Should be used as the string that contains query info    
    protected String myReturnString;
    private boolean stripSemiColons = true;
    protected Object[] parametersForPreparedStmt;

    /*
     * These are the tag flags
     */
    private static final String BRANCH_TAG = "<branch>";
    private static final String COMPNAY_TAG = "<company>";
    private static final String MANAGEMENT_TAG = "<Management>";
    public static final int UPDATE_SCHEDULE = 1;
    public static final int UPDATE_CHECK_IN = 2;
    public static final int UPDATE_CLIENT_LIST = 3;
    public static final int UPDATE_EMPLOYEE_LIST = 4;
    public static final int UPDATE_AVAILABILITY = 5;
    public static final int UPDATE_BANNED = 6;
    public static final int KILL_CLIENT = 8;
    public static final int UPDATE_CLIENT_CERT = 10;
    public static final int UPDATE_EMPLOYEE_CERT = 11;

    public GeneralQueryFormat() {
        md5 = new String();
        userId = new String();
        Branch = new String();
        Company = new String();
        managementDatabase = new String();

        hasBranchFlag = false;
        hasManagementFlag = false;
        shouldUpdateFlag = true;
        whenRan = new String();
    }

    public void setRanTime(String ran) {
        whenRan = ran;
    }

    public boolean getStripSemiColonsForOracle() {
        return stripSemiColons;
    }

    public void setStripSemiColonsForOracle(boolean val) {
        stripSemiColons = val;
    }

    /**
     * Used by our hearbeat...
     */
    public void updateTime(String LU) {
    }

    public boolean isCertificationQuery() {
        return false;
    }

    public boolean isEmployeeCertQuery() {
        return false;
    }

    public boolean isTableCreationQuery() {
        return false;
    }

    /**
     * Returns if current query is the Schedule Query, ie:
     * new_assembly_complete_query this is used to see if we should associate
     * this query with the client, so that the heartbeat knows what queries each
     * client needs run on an update schedule deal...yes it is complicated....
     */
    public boolean isScheduleQuery() {
        return false;
    }

    /**
     * Returns if current query is the Check In Query, ie:
     * complete_check_in_query this is used to see if we should associate this
     * query with the client, so that the heartbeat knows what queries each
     * client needs run on an update check in deal...hmmm sounds familiar....
     */
    public boolean isCheckInQuery() {
        return false;
    }

    /**
     * Returns if current query has touched our banned stuff so that the
     * schedule can get the latest list of banned employees
     */
    public boolean isBannedQuery() {
        return false;
    }

    /**
     * Does this use prepared statements
     *
     * @return boolean
     */
    public boolean hasPreparedStatement() {
        return false;
    }

    /**
     * Returns the prepared statement settings
     */
    public Object[] getPreparedStatementObjects() {
        return this.parametersForPreparedStmt;
    }

    /**
     * Updates the prepared statement.
     *
     * @param params
     */
    public void setPreparedStatement(Object... params) {
        this.parametersForPreparedStmt = params;
    }

    public String getPreparedStatementString() {
        return null;
    }

    public int setPreparedStatement(PreparedStatement pstmt, int start) throws SQLException {
        for (int p = 0; p < this.parametersForPreparedStmt.length; p++) {
            if (parametersForPreparedStmt[p] == null) {
                pstmt.setNull(start++, 0);
            } else if (parametersForPreparedStmt[p] instanceof String) {
                pstmt.setString(start++, (String) parametersForPreparedStmt[p]);
            } else if (parametersForPreparedStmt[p] instanceof Boolean) {
                pstmt.setBoolean(start++, (Boolean) parametersForPreparedStmt[p]);
            } else if (parametersForPreparedStmt[p] instanceof Integer) {
                pstmt.setInt(start++, (Integer) parametersForPreparedStmt[p]);
            } else if (parametersForPreparedStmt[p] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(start++, (java.sql.Timestamp) parametersForPreparedStmt[p]);
            } else if (parametersForPreparedStmt[p] instanceof java.util.Date) {
                pstmt.setDate(start++, new java.sql.Date(((java.util.Date) parametersForPreparedStmt[p]).getTime()));
            } else if (parametersForPreparedStmt[p] instanceof int[]) {
                pstmt.setObject(start++, parametersForPreparedStmt[p], java.sql.Types.ARRAY);
            } else if (parametersForPreparedStmt[p] instanceof byte[]) {
                pstmt.setBytes(start++, (byte[]) parametersForPreparedStmt[p]);
            } else if (parametersForPreparedStmt[p] instanceof Array) {
                pstmt.setArray(start++, (Array)parametersForPreparedStmt[p]);
            } else if (parametersForPreparedStmt[p] instanceof ArrayList) {
                Object[] data = ((ArrayList)parametersForPreparedStmt[p]).toArray();
            } else {
                pstmt.setObject(start++, parametersForPreparedStmt[p]);
            }
        }
        return start;
    }

     public static Array convertToPgSqlArray(final Object[] p) {
        if (p == null || p.length < 1) {
            return null;
        }
        Array a = new Array() {

            public String getBaseTypeName() {
                try {
                    if (p[0] instanceof Integer) {
                        return "int4";
                    } else if (p[0] instanceof String) {
                        return "text";
                    } else if (p[0] instanceof Date) {
                        return "date";
                    }
                    return "text";
                } catch (Exception exe) {}
                return "text";
            }

            public int getBaseType() {
                return 0;
            }

            public Object getArray() {
                return null;
            }

            public Object getArray(Map<String, Class<?>> map) {
                return null;
            }

            public Object getArray(long index, int count) {
                return null;
            }

            public Object getArray(long index, int count, Map<String, Class<?>> map) {
                return null;
            }

            public ResultSet getResultSet() {
                return null;
            }

            public ResultSet getResultSet(Map<String, Class<?>> map) {
                return null;
            }

            public ResultSet getResultSet(long index, int count) {
                return null;
            }

            public ResultSet getResultSet(long index, int count,
                    Map<String, Class<?>> map) {
                return null;
            }

            @Override
            public String toString() {
                String fp = "{";
                if (p.length == 0) {
                } else {
                    for (int i = 0; i < p.length - 1; i++) {
                        fp += p[i] + ",";
                    }
                    fp += p[p.length - 1];
                }
                fp += "}";
                return fp;
            }

            public void free() throws SQLException {
                
            }
        };
        return a;
    }
     
    public static Array convertIntegerToPgSqlArray(final int[] p) {
        if (p == null || p.length < 1) {
            return null;
        }
        Array a = new Array() {

            public String getBaseTypeName() {
                return "int4";
            }

            public int getBaseType() {
                return 0;
            }

            public Object getArray() {
                return null;
            }

            public Object getArray(Map<String, Class<?>> map) {
                return null;
            }

            public Object getArray(long index, int count) {
                return null;
            }

            public Object getArray(long index, int count, Map<String, Class<?>> map) {
                return null;
            }

            public ResultSet getResultSet() {
                return null;
            }

            public ResultSet getResultSet(Map<String, Class<?>> map) {
                return null;
            }

            public ResultSet getResultSet(long index, int count) {
                return null;
            }

            public ResultSet getResultSet(long index, int count,
                    Map<String, Class<?>> map) {
                return null;
            }

            @Override
            public String toString() {
                String fp = "{";
                if (p.length == 0) {
                } else {
                    for (int i = 0; i < p.length - 1; i++) {
                        fp += p[i] + ",";
                    }
                    fp += p[p.length - 1];
                }
                fp += "}";
                return fp;
            }

            public void free() throws SQLException {
                
            }
        };
        return a;
    }

    /**
     * Returns if current query is the temp_assemble_query for the
     * schedule....not for employee stuff...
     */
    public boolean isAvailabilityQuery() {
        return false;
    }

    public String getRanTime() {
        return whenRan;
    }

    public abstract boolean hasAccess();

    public void setDriver(sqldriver driver) {
        this.driver = driver;
    }

    public sqldriver getDriver() {
        if (this.driver != null) {
            return this.driver;
        } else {
            try {
                return sqldriver.getNewSQLDriver(null);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public boolean isBatch() {
        return false;
    }

    public String toString() {
        return myReturnString;
    }

    /*
     * These are the get functions
     */
    public int getUpdateStatus() {
        return 0;
    }

    public boolean shouldUpdate() {
        return shouldUpdateFlag;
    }

    public void dontUpdate() {
        shouldUpdateFlag = false;
    }

    public String getMD5() {
        return md5;
    }

    public String getUser() {
        return userId;
    }

    public String getCompany() {
        return Company;
    }

    public String getManagementSchema() {
        return "control_db";
    }

    public String getManagementId() {
        return managementId;
    }

    public String getManagementDB() {
        return IPLocationFile.getDATABASE_NAME();
    }

    public String[] getBatch() {
        return null;
    }

    public String getBranch() {
        return (Branch.length() == 0) ? BRANCH_TAG : Branch;
    }

    //This needs to be overriden by any queries extending this
    public int getSecurityCode() {
        return -1;
    }

    /*
     * These are the set functions
     */
    public void setUser(String User) {
        userId = User;
    }

    public void setMD5(String MD5) {
        md5 = MD5;
    }

    public void setCompany(String cmpy) {
        Company = cmpy;
    }

    /**
     * Sets the managemend id used in the control db file, used to control what
     * branches, companies and so on user can view....wonderful...
     */
    public void setManagementId(String id) {
        managementId = id;
    }

    public void setManagement(String dbase) {
        managementDatabase = dbase;
        replace(hasManagementFlag, MANAGEMENT_TAG, getManagementSchema());
    }

    public void setBranch(String brnch) {
        Branch = brnch;
        replace(hasBranchFlag, BRANCH_TAG, getBranch());
    }

    public void setLastUpdated(String lu) {
        lastUpdated = lu;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void clear() {
    }

    /**
     * Should be overridden by all update queries called by our RMIServer and
     * passed to our logging class...
     */
    public String getLoggingMessage() {
        return "Hey dumbass get a log message for this class....";
    }

    public int getLimitPortionOfQuery() {
        return 0;
    }

    /**
     * A generic replace for our return string
     */
    private void replace(boolean b, String flag, String s) {
        try {
            myReturnString = myReturnString.replaceAll("<branch>", s);
        } catch (Exception e) {
        }
    }
}
