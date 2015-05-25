/*
 * save_branch_comp_group_access_query.java
 *
 * Created on April 19, 2005, 9:43 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.util.ArrayList;
/**
 *
 * @author ira
 */
public class save_branch_comp_group_access_query extends GeneralQueryFormat {
    
    private ArrayList Branch;
    private ArrayList Comp;
    private ArrayList Group;
    private ArrayList Access;
    private String userId;
    
    /** Creates a new instance of save_branch_comp_group_access_query */
    public save_branch_comp_group_access_query() {
        myReturnString = new String();
    }
    
    public void update(String uid, ArrayList branch, ArrayList comp, ArrayList group, ArrayList access) {
        Branch = branch;
        Comp = comp;
        Group = group;
        Access = access;
        userId = uid;
        if (userId.equals("0")) {
            userId = "(SELECT CASE WHEN (SELECT max(user_id) FROM " + getManagementSchema() + ".user) is not null THEN (SELECT max(user_id) FROM " + getManagementSchema() + ".user) ELSE 0 END)";
        }
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        StringBuilder branchString = new StringBuilder();
        StringBuilder companyString = new StringBuilder();   
        StringBuilder groupsString = new StringBuilder();
        StringBuilder accessString = new StringBuilder();
        String deleteString = new String();
        if (Branch != null) {
            deleteString = deleteString + "DELETE FROM " + getManagementSchema() + ".user_branch WHERE user_id  = " + userId + ";";
            for (int i = 0; i < Branch.size(); i++) {
                branchString.append("INSERT INTO " + getManagementSchema() + ".user_branch (user_id, branch_id) " +
                        "VALUES (" + userId + ", " + (String)Branch.get(i) +");");
            }
        }
        if (Comp != null) {
            deleteString = deleteString + "DELETE FROM " + getManagementSchema() + ".user_company WHERE user_id = " + userId + ";";
            for (int i = 0; i < Comp.size(); i++) {
                companyString.append("INSERT INTO " + getManagementSchema() + ".user_company (user_id, company_id) " +
                        "VALUES (" + userId + ", " + (String)Comp.get(i) +");");
            }
        }
        if (Group != null) {
            deleteString = deleteString + "DELETE FROM " + getManagementSchema() + ".user_groups WHERE user_id  = " + userId + ";";
            for (int i = 0; i < Group.size(); i++) {
                groupsString.append("INSERT INTO " + getManagementSchema() + ".user_groups (user_id, groups_id) " +
                        "VALUES (" + userId + ", " + (String)Group.get(i) +");");
            }
        }
        if (Access != null) {
            deleteString = deleteString + "DELETE FROM " + getManagementSchema() + ".user_access WHERE user_id  = " + userId + ";";
            for (int i = 0; i < Access.size(); i++) {
                accessString.append("INSERT INTO " + getManagementSchema() + ".user_access (user_id, access_id) " +
                        "VALUES (" + userId + ", " + (String)Access.get(i) +");");
            }
        }
        
        return deleteString +
               branchString.toString() + companyString.toString() + groupsString.toString() + accessString.toString();
               
    }
          
    
}
