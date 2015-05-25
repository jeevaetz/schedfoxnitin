/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.UserControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author user
 */
public class User implements Serializable, Comparable, UserInterface, EmailContact {

    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String userMd5;
    private String userLogin;
    private String userPassword;
    private String userFirstName;
    private String userLastName;
    private String userMiddleInitial;
    private short userIsDeleted;
    private Short userManagementId;
    private String userEmail;
    private String emailPassword;
    private Boolean canViewSsn;
    
    //Lazy Loaded Objects
    private transient ArrayList<Group> groups;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(Date currDate, Record_Set rst) {
        try {
            this.userId = rst.getInt("user_id");
        } catch (Exception e) {
            this.userId = 0;
        }
        try {
            this.userEmail = rst.getString("user_email");
        } catch (Exception e) {
            this.userEmail = "";
        }
        try {
            this.userFirstName = rst.getString("user_first_name");
        } catch (Exception e) {
            this.userFirstName = "";
        }
        try {
            this.userLastName = rst.getString("user_last_name");
        } catch (Exception e) {
            this.userLastName = "";
        }
        try {
            this.userIsDeleted = (short) rst.getInt("user_is_deleted");
        } catch (Exception e) {
            this.userIsDeleted = 0;
        }
        try {
            this.userLogin = rst.getString("user_login");
        } catch (Exception e) {
            this.userLogin = "";
        }
        try {
            this.userManagementId = (short) rst.getInt("user_management_id");
        } catch (Exception e) {
            this.userManagementId = 0;
        }
        try {
            this.userMd5 = rst.getString("user_md5");
        } catch (Exception e) {
            this.userMd5 = "";
        }
        try {
            this.userMiddleInitial = rst.getString("user_middle_initial");
        } catch (Exception e) {
            this.userMiddleInitial = "";
        }
        try {
            this.userPassword = rst.getString("user_password");
        } catch (Exception e) {
            this.userPassword = "";
        }
        try {
            this.emailPassword = rst.getString("email_password");
        } catch (Exception exe) {
            this.emailPassword = "";
        }
        try {
            this.canViewSsn = rst.getBoolean("can_view_ssn");
        } catch (Exception exe) {
            this.canViewSsn = false;
        }
    }

    public User(Integer userId, String userMd5, String userLogin, String userPassword, String userFirstName, String userLastName, short userIsDeleted) {
        this.userId = userId;
        this.userMd5 = userMd5;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userIsDeleted = userIsDeleted;
    }

    public String getUserFullName() {
        return (this.userFirstName == null ? "" : this.userFirstName) + " "
                + (this.userLastName == null ? "" : this.userLastName);
    }

    public Integer getUserId() {
        return userId;
    }

    public boolean isActive() {
        try {
            return this.userIsDeleted != 1;
        } catch (Exception exe) {
            return true;
        }
    }
    
    public void setActive(boolean active) {
        if (!active) {
            this.userIsDeleted = 1;
        } else {
            this.userIsDeleted = 0;
        }
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserMd5() {
        return userMd5;
    }

    public void setUserMd5(String userMd5) {
        this.userMd5 = userMd5;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserMiddleInitial() {
        return userMiddleInitial;
    }

    public void setUserMiddleInitial(String userMiddleInitial) {
        this.userMiddleInitial = userMiddleInitial;
    }

    public short getUserIsDeleted() {
        return userIsDeleted;
    }

    public void setUserIsDeleted(short userIsDeleted) {
        this.userIsDeleted = userIsDeleted;
    }

    public Short getUserManagementId() {
        return userManagementId;
    }

    public void setUserManagementId(Short userManagementId) {
        this.userManagementId = userManagementId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.userFirstName + " " + this.userLastName;
    }

    /**
     * added by Jeffrey Davis for <code>Collections.sort()</code>
     */
    @Override
    public int compareTo(Object argData) {
        if (argData instanceof User) {
            return String.CASE_INSENSITIVE_ORDER.compare(this.userLastName, ((User) argData).getUserLastName());
        }
        return 1;
    }

    @Override
    public Integer getId() {
        return this.userId;
    }

    @Override
    public String getEmailAddress() {
        return this.getUserEmail();
    }

    @Override
    public String getFullName() {
        return this.getUserFullName();
    }

    @Override
    public int getPrimaryId() {
        if (this.getUserId() != null) {
            return this.getUserId();
        } else {
            return 0;
        }
    }

    @Override
    public String getType() {
        return "User";
    }

    /**
     * Returns the associated groups.
     * @return ArrayList<Group>
     */
    public ArrayList<Group> getAssociatedGroups() {
        if (this.groups == null) {
            UserControllerInterface userInterface = ControllerRegistryAbstract.getUserController("");
            try {
                this.groups = userInterface.getGroupsForUser(userId);
            } catch (Exception e) {
                this.groups = new ArrayList<Group>();
            }
        }
        return this.groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setAssociatedGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    /**
     * @return the emailPassword
     */
    public String getEmailPassword() {
        return emailPassword;
    }

    /**
     * @param emailPassword the emailPassword to set
     */
    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    /**
     * @return the canViewSsn
     */
    public Boolean getCanViewSsn() {
        return canViewSsn;
    }

    /**
     * @param canViewSsn the canViewSsn to set
     */
    public void setCanViewSsn(Boolean canViewSsn) {
        this.canViewSsn = canViewSsn;
    }
}
