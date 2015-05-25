/*
 * save_branch_info_query.java
 *
 * Created on September 30, 2005, 12:25 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.*;
/**
 *
 * @author Ira Juneau
 */
public class save_branch_info_query extends GeneralQueryFormat {
    
    private ArrayList BranchId; 
    private ArrayList BranchPhone;
    private ArrayList BranchAddress1;
    private ArrayList BranchAddress2;
    private ArrayList BranchCity;
    private ArrayList BranchState;
    private ArrayList BranchZip;
    
    /** Creates a new instance of save_branch_info_query */
    public save_branch_info_query() {
        myReturnString = new String();
        BranchId = new ArrayList();
        BranchPhone = new ArrayList();
        BranchAddress1 = new ArrayList();
        BranchAddress2 = new ArrayList();
        BranchCity = new ArrayList();
        BranchState = new ArrayList();
        BranchZip = new ArrayList();
    }
    
    public void update(String branchid, String branchPhone, String branchAddress1, 
            String branchAddress2, String branchCity, String branchState, String branchZip) {
        BranchId.add(branchid);
        BranchPhone.add(branchPhone.replaceAll("'", "''"));
        BranchAddress1.add(branchAddress1.replaceAll("'", "''"));
        BranchAddress2.add(branchAddress2.replaceAll("'", "''"));
        BranchCity.add(branchCity.replaceAll("'", "''"));
        BranchState.add(branchState.replaceAll("'", "''"));
        BranchZip.add(branchZip.replaceAll("'", "''"));
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        StringBuffer myQuery = new StringBuffer();
        for (int i = 0; i < BranchId.size(); i++) {
            if (BranchId.get(i).equals("0")) {
                String newId = "(SELECT MAX(branch_id) FROM branch)";
                myQuery.append("INSERT INTO " + getManagementSchema() + ".branch_info (branch_id, address, address2, city, state, zip, phone) VALUES (" +
                               newId + ",'" + (String)BranchAddress1.get(i) + "','" + (String)BranchAddress2.get(i) + "','" + (String)BranchCity.get(i) +
                               "','" + (String)BranchState.get(i) + "','" + (String)BranchZip.get(i) + "','" + (String)BranchPhone.get(i) + "');");
            }
            else {
                myQuery.append( "UPDATE " + getManagementSchema() + ".branch_info SET address = '" + (String)BranchAddress1.get(i) + "', address2 = '" + (String)BranchAddress2.get(i) + "', city = '" + (String)BranchCity.get(i) + "', " +
                                "state = '" + (String)BranchState.get(i) + "', zip = '" + (String)BranchZip.get(i) + "', phone = '" + (String)BranchPhone.get(i) + "' WHERE branch_id = " + BranchId.get(i) + ";");
            }
        }
        return myQuery.toString();
    }
    
}
