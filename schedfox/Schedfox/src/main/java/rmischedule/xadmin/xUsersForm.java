/*
 * xUsersForm.java
 *
 * Created on September 15, 2005, 4:53 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.xadmin;
import schedfoxlib.model.util.Record_Set;
import javax.swing.event.InternalFrameEvent;
import rmischedule.data_connection.*;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.main.Main_Window;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
import rmischeduleserver.mysqlconnectivity.queries.util.get_available_management_co_query;
import rmischeduleserver.data_connection_types.*;
import javax.swing.*;
import java.awt.event.*;
/**
 *
 * @author Ira Juneau
 */
public class xUsersForm extends GenericEditForm {
    
    private Main_Window parent;
    private boolean isRootUser;
    public String currmanageid;
    private xUserDetail myDetailForm;
    private String deleted;
    
    /** Creates a new instance of xUsersForm */
    public xUsersForm() {
        parent = Main_Window.parentOfApplication;
        deleted = "0";
        myDetailForm = new xUserDetail(this);
        super.addSubForm(myDetailForm);
    }
    
    public Connection getConnection() {
        return new Connection();
    }
    
    public String getDisplayNameForObject(Object o) {
        return ((User)o).fname + " " + ((User)o).lname;
    }
    
    public void getData() {
        list_users_query myListQuery = new list_users_query();
        if (currmanageid == null) {
            currmanageid = Main_Window.parentOfApplication.getManagementId();
        }
        boolean showDeleted = false;
        if (deleted == null) {
            deleted = "0";
        }
        if (deleted.equals("1")) {
            showDeleted = true; 
        }
        myListQuery.update(showDeleted, currmanageid);
        Record_Set rs = new Record_Set();
        try {
            rs = new Connection().executeQuery(myListQuery);
        } catch (Exception e) {}
        super.populateList(rs, "user_is_deleted", "1");
    }

    public boolean getToggleDeleted() {
        return true;
    }
    
    protected ImageIcon getDeletedUpIcon() {
        return Main_Window.Active_Users_Icon;
    }
    
    protected ImageIcon getDeletedDownIcon() {
        return Main_Window.All_Users_Icon;
}
    
    public String getMyIdForSave() {
        if (currentSelectedObject == null) {
            return "0";
        }
        return ((User)currentSelectedObject).id;
    }
    
    public Object createObjectForList(Record_Set rs) {
        return new User(rs.getString("user_id"), rs.getString("user_first_name"), rs.getString("user_last_name"),
                    rs.getString("user_middle_initial"), rs.getString("user_login"), rs.getString("user_password"),
                    rs.getString("user_management_id"), rs.getString("user_md5"));
    }
    
    public String getWindowTitle() {
        if (currentSelectedObject == null) {
            return "Adding New User";
        }
        return "Editing Information For " + getDisplayNameForObject(currentSelectedObject);
    }
    
    public void addMyMenu(JMenuBar myMenu) {
        get_available_management_co_query myManagementQuery;
        myManagementQuery = new get_available_management_co_query();
        Record_Set rs = new Record_Set();
        if (Main_Window.parentOfApplication.isRootUser()) {
            isRootUser = true;
        } else {
            isRootUser = false;
        }
        JMenu myParentMenu = new JMenu("Display Users From...");
        try {
            rs = getConnection().executeQuery(myManagementQuery);
        } catch (Exception e) {}
        if (!isRootUser) {
            myMenu.setVisible(false);
        } else {
            for (int i = 0; i < rs.length(); i++) {
                JMenuItem currentItem = new JMenuItem(rs.getString("management_client_name"));
                currentItem.addActionListener(new changeManagement(rs.getString("management_id")));
                myParentMenu.add(currentItem);
                rs.moveNext();
            }
        }
        myMenu.add(new HierarchicalMenu(myParentMenu, true));
    }

    protected void showDeleted(boolean isPressed) {
        if (isPressed) {
            deleted = "1";
        } else {
            deleted = "0";
        }
        getData();
    }
    
    public void deleteData() {
        if(this.currentSelectedObject == null)
            return;
        
        if(this.selectedIsMarkedDeleted()) {
        if (JOptionPane.showConfirmDialog(this, "Are you SURE you want to delete this user? (THIS CANNONT BE UNDONE!)", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String userId = ((User)currentSelectedObject).id;
            GenericQuery deleteQuery = new GenericQuery("DELETE FROM control_db.user WHERE user_id = " + userId + ";");
            try { getConnection().executeUpdate(deleteQuery); }
            catch(Exception e) {}
            getData();
        }
        }
        else
            JOptionPane.showMessageDialog(this, "You can only delete inactive users!", "Unable to delete user", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private class changeManagement implements ActionListener{
        private String myId;
        
        public changeManagement(String manageId) {
            myId = manageId;
        }
        
        public void actionPerformed(ActionEvent e){
            currmanageid = myId;
            clearData();
            getData();
        }
    }
    
    /**
     * Small private class to encapsulate user information
     */
    public class User {
        public String id;
        public String fname;
        public String lname;
        public String login;
        public String pw;
        public String mInitial;
        public String manageId;
        public String md5;
        
        public User(String ID, String FNAME, String LNAME, String MNAME, String LOGIN, String PW, String MANAGE, String MD5) {
            id = ID;
            fname = FNAME;
            lname = LNAME;
            mInitial = MNAME;
            pw = PW;
            login = LOGIN;
            manageId = MANAGE;
            md5 = MD5;
        }
        
        public String getName() {
            return fname + " " + lname;
        }
        
        public boolean equals(Object o) {
            try { return ((User)o).id.equals(this.id); }
            catch(Exception e) {}
            
            return false;
        }
        
    }
    
}
