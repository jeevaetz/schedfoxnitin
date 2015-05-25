/*
 * xSaveBranchCompanyAccessQuery.java
 *
 * Created on November 22, 2005, 12:18 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class xSaveBranchCompanyAccessQuery extends GeneralQueryFormat {
    
    private String Bid;
    private String Cid;
    private String Uid;
    private boolean hasaccess;
    
    /** Creates a new instance of xSaveBranchCompanyAccessQuery */
    public xSaveBranchCompanyAccessQuery() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String uid, String bid, String cid, boolean hasAccess) {
        Bid = bid;
        Cid = cid;
        Uid = uid;
        hasaccess = hasAccess;
    }
    
    public String toString() {
        StringBuilder myBuilder = new StringBuilder();
        myBuilder.append("DELETE FROM user_branch_company WHERE user_id = " + Uid + " AND branch_id = " + Bid + " AND " +
                "company_id = " + Cid + ";");
        if (hasaccess) {
            myBuilder.append("INSERT INTO user_branch_company (user_id, branch_id, company_id) VALUES (" + Uid + ", " + Bid + ", " +
                Cid + ");");
        }
        return myBuilder.toString();
    }
    
}
