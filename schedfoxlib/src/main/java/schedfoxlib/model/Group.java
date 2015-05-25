/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class Group {
    private int groupId;
    private String groupName;
    private int groupManagementId;

    public Group() {
        
    }
    
    public Group(Record_Set rst) {
        groupId = rst.getInt("groups_id");
        groupName = rst.getString("groups_name");
        groupManagementId = rst.getInt("groups_management_id");
    }

    /**
     * @return the groupId
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return the groupManagementId
     */
    public int getGroupManagementId() {
        return groupManagementId;
    }

    /**
     * @param groupManagementId the groupManagementId to set
     */
    public void setGroupManagementId(int groupManagementId) {
        this.groupManagementId = groupManagementId;
    }

    @Override
    public String toString() {
        return this.groupName;
    }
}
