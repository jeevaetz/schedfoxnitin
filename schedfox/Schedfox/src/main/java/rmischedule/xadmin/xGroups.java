/*
 * xGroups.java
 *
 * Created on September 5, 2005, 6:08 PM
 */

package rmischedule.xadmin;
import schedfoxlib.model.util.Record_Set;
import rmischedule.data_connection.*;
import rmischedule.main.*;
import rmischedule.components.graphicalcomponents.*;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.data_connection_types.*;

import javax.swing.*;
import java.util.*;
/**
 *
 * @author  Owner
 */
public class xGroups extends javax.swing.JInternalFrame implements parentFormInterface {
    
    private ListAccessForGroupOrUserPanel myAccessLevels;
    private Vector<groups> myGroups;
    private ListOfUsersPanel myListParent;
    private String currManagement;
    private String currentGroupId;
    
    /** Creates new form xGroups */
    public xGroups() {                      
        initComponents();
        myListParent = new ListOfUsersPanel(this);
        ListOfGroups.add(myListParent);
        myAccessLevels = new ListAccessForGroupOrUserPanel(true);
        AccessPanel.add(myAccessLevels);
        populateUsersForManagementCo(Main_Window.parentOfApplication.getManagementId());
        currentGroupId = "";
        this.myAccessLevels.clearData();

    }  
    
    
    public void runOnClickUser(Object obj) {
        groups myGroup = (groups)obj;
        groupName.setText(myGroup.Name);
        Record_Set rs = new Record_Set();
        GeneralQueryFormat myQuery = myAccessLevels.getQuery(myGroup.ManageId, myGroup.Id);
        try {
            rs = (new Connection().executeQuery(myQuery));
        } catch (Exception e) {}
        myAccessLevels.setData(rs);
        this.currentGroupId = myGroup.Id;
    }
    
    
    /**
     * Pass in string of managment Company it will build list of groups for that management company..
     */
    public void populateUsersForManagementCo(String managementid) {
        myListParent.removeAll();
        groupName.setText("");
        get_groups_management_query myQuery = new get_groups_management_query();
        currManagement = managementid;
        myQuery.update(managementid);
        Record_Set rs = new Record_Set();
        myGroups = new Vector();
        GraphicalListComponent myComp;
        try {
            rs = (new Connection().executeQuery(myQuery));
        } catch (Exception e) {}
        for (int i = 0; i < rs.length(); i++) {
            myGroups.add(new groups(rs.getString("groups_name"), rs.getString("groups_id"), rs.getString("groups_management_id")));
            myComp = new GraphicalListComponent(myGroups.get(i), myListParent, myGroups.get(i).Name);
            rs.moveNext();
            myListParent.add(myComp);
        }
        myAccessLevels.setData(null);
        myListParent.revalidate();
        myListParent.repaint();
    }
    
    
    public class groups {
        public String Name;
        public String Id;
        public String ManageId;
        
        public groups(String name, String id, String manageid) {
            Name = name;
            Id = id;
            ManageId = manageid;
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        ListOfGroups = new javax.swing.JPanel();
        InfoPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        groupName = new javax.swing.JTextField();
        AccessPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        SaveBtn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        newButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        deleteButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        ExitBtn = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Add/Edit Groups");
        setFrameIcon(null);
        ListOfGroups.setLayout(new java.awt.GridLayout(1, 0));

        ListOfGroups.setMaximumSize(new java.awt.Dimension(150, 32767));
        ListOfGroups.setMinimumSize(new java.awt.Dimension(150, 10));
        ListOfGroups.setPreferredSize(new java.awt.Dimension(150, 10));
        getContentPane().add(ListOfGroups, java.awt.BorderLayout.WEST);

        InfoPanel.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Group Name");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel1.add(jLabel1);

        groupName.setMaximumSize(new java.awt.Dimension(180, 2147483647));
        groupName.setMinimumSize(new java.awt.Dimension(180, 19));
        groupName.setPreferredSize(new java.awt.Dimension(180, 19));
        jPanel1.add(groupName);

        InfoPanel.add(jPanel1, java.awt.BorderLayout.NORTH);

        AccessPanel.setLayout(new java.awt.GridLayout(1, 0));

        InfoPanel.add(AccessPanel, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));

        SaveBtn.setText("Save");
        SaveBtn.setMaximumSize(new java.awt.Dimension(80, 23));
        SaveBtn.setMinimumSize(new java.awt.Dimension(80, 23));
        SaveBtn.setPreferredSize(new java.awt.Dimension(80, 23));
        SaveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveBtnActionPerformed(evt);
            }
        });

        jPanel3.add(SaveBtn);

        jPanel3.add(jPanel4);

        newButton.setText("New");
        newButton.setMaximumSize(new java.awt.Dimension(80, 23));
        newButton.setMinimumSize(new java.awt.Dimension(80, 23));
        newButton.setPreferredSize(new java.awt.Dimension(80, 23));
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        jPanel3.add(newButton);

        jPanel3.add(jPanel2);

        deleteButton.setText("Delete");
        deleteButton.setMaximumSize(new java.awt.Dimension(80, 23));
        deleteButton.setMinimumSize(new java.awt.Dimension(80, 23));
        deleteButton.setPreferredSize(new java.awt.Dimension(80, 23));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        jPanel3.add(deleteButton);

        jPanel3.add(jPanel5);

        ExitBtn.setText("Exit");
        ExitBtn.setMaximumSize(new java.awt.Dimension(80, 23));
        ExitBtn.setMinimumSize(new java.awt.Dimension(80, 23));
        ExitBtn.setPreferredSize(new java.awt.Dimension(80, 23));
        ExitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitBtnActionPerformed(evt);
            }
        });

        jPanel3.add(ExitBtn);

        InfoPanel.add(jPanel3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(InfoPanel, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-600)/2, (screenSize.height-350)/2, 600, 350);
    }// </editor-fold>//GEN-END:initComponents

    private void ExitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitBtnActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_ExitBtnActionPerformed

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        try {
            for(int i = 0; i < this.myListParent.getNumberOfComponents(); i++) {
                this.myListParent.getListComponentAt(i).setSelected(false);
            }
        }
        catch(Exception ex) {}
        
        this.groupName.setText("");
        this.currentGroupId = "";
        this.myAccessLevels.clearData();

    }//GEN-LAST:event_newButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        try {
            if(this.currentGroupId == "")
                return;
            
            if(JOptionPane.showConfirmDialog(this, "Are you sure you wish to delete this group?", "Delete Group?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                new Connection().executeUpdate(new GenericQuery("DELETE FROM control_db.groups WHERE groups_id = " + this.currentGroupId + ";"));
                this.currentGroupId = "";
                populateUsersForManagementCo(currManagement);
                this.myAccessLevels.clearData();
            }
        }
        catch(Exception e) { e.printStackTrace(); }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void SaveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveBtnActionPerformed
        try {
            new Connection().executeQuery(myAccessLevels.getSaveDataQuery(groupName.getText()));
            populateUsersForManagementCo(currManagement);
            this.myAccessLevels.clearData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_SaveBtnActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AccessPanel;
    private javax.swing.JButton ExitBtn;
    private javax.swing.JPanel InfoPanel;
    private javax.swing.JPanel ListOfGroups;
    private javax.swing.JButton SaveBtn;
    private javax.swing.JButton deleteButton;
    private javax.swing.JTextField groupName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton newButton;
    // End of variables declaration//GEN-END:variables
    
}