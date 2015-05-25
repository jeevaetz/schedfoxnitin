/*
 * xUserDetail.java
 *
 * Created on September 15, 2005, 5:08 AM
 */
package rmischedule.xadmin;

import rmischeduleserver.mysqlconnectivity.queries.util.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import schedfoxlib.model.util.Record_Set;
import rmischedule.main.*;
import rmischedule.components.graphicalcomponents.*;

import javax.swing.*;
import java.util.*;
import static org.apache.commons.codec.digest.DigestUtils.md5;
import schedfoxlib.model.User;

/**
 *
 * @author Ira Juneau
 */
public class xUserDetail extends GenericEditSubForm {

    private boolean isRootUser;
    private String origPw;
    private xUsersForm myPar;
    private Vector<UserPanelInterface> mySubPanels;
    private String[][] managementCompanies;

    /**
     * Creates new form xUserDetail
     */
    public xUserDetail(xUsersForm par) {
        initComponents();
        myPar = par;
        origPw = "";
        buildForm();
    }

    /**
     * Finishes customization of form...
     */
    public void buildForm() {
        mySubPanels = new Vector();
        if (Main_Window.parentOfApplication.isRootUser()) {
            this.manageCmbo.setVisible(true);
            this.companyLabel.setVisible(true);
        } else {
            this.manageCmbo.setVisible(false);
            this.companyLabel.setVisible(false);
        }

        mySubPanels.add(new ListGroupAccessPanel(this));
        mySubPanels.add(new xListCompanyBranchAccessPanel(this));

        for (int i = 0; i < mySubPanels.size(); i++) {
            myTabbedPane.addTab(mySubPanels.get(i).getTitleOfTab(), mySubPanels.get(i).getComponent());
            registerListView(mySubPanels.get(i).getListView());
        }
    }

    private void grabManagementList() {
        management_list_query myManagementQuery = new management_list_query();
        myManagementQuery.update();
        Record_Set manage = new Record_Set();
        try {
            manage = myparent.getConnection().executeQuery(myManagementQuery);
        } catch (Exception e) {
        }
        managementCompanies = new String[2][manage.length()];
        manageCmbo.removeAllItems();
        for (int x = 0; x < manage.length(); x++) {
            manageCmbo.addItem(manage.getString("name"));
            managementCompanies[0][x] = manage.getString("name");
            managementCompanies[1][x] = manage.getString("id");
            if (manage.getString("id").equals(myPar.currmanageid)) {
                manageCmbo.setSelectedIndex(x);
            }
            manage.moveNext();
        }
    }

    public void loadData(Record_Set rs) {
        fname.setText(rs.getString("user_first_name"));
        mname.setText(rs.getString("user_middle_initial"));
        lname.setText(rs.getString("user_last_name"));
        login.setText(rs.getString("user_login"));
        password.setText(rs.getString("user_password"));
        confirm.setText(rs.getString("user_password"));
        origPw = rs.getString("user_password");
        email.setText(rs.getString("email"));
        try {
            ssnChk.setSelected(rs.getBoolean("can_view_ssn"));
        } catch (Exception exe) {
            ssnChk.setSelected(false);
        }

        if (rs.getInt("user_is_deleted") == 0) {
            this.activeCheckBox.setSelected(true);
        } else {
            this.activeCheckBox.setSelected(false);
        }

        RunQueriesEx myQuery = new RunQueriesEx();
        for (int i = 0; i < mySubPanels.size(); i++) {
            myQuery.add(mySubPanels.get(i).getQuery(myPar.currmanageid, myparent.getMyIdForSave()));
        }
        ArrayList data = new ArrayList();
        try {
            data = myparent.getConnection().executeQueryEx(myQuery);
        } catch (Exception e) {
        }
        for (int i = 0; i < mySubPanels.size(); i++) {
            mySubPanels.get(i).setData((Record_Set) data.get(i));
        }
    }

    public GeneralQueryFormat getQuery(boolean isSelected) {
        list_users_query myListQuery = new list_users_query();
        myListQuery.update(true, myPar.currmanageid, super.myparent.getMyIdForSave());
        return myListQuery;
    }

    public GeneralQueryFormat getSaveQuery(boolean isnew) {
        return getSaveQuery(false, false);
    }

    /**
     * Checks to see if this User Name is being used already...
     */
    public String checkData() {
        if (activeCheckBox.isSelected()) {
            check_for_duplicate_user_name_query checkQuery = new check_for_duplicate_user_name_query();
            checkQuery.update(login.getText(), myparent.getMyIdForSave());
            Record_Set results = new Record_Set();
            try {
                results = myparent.getConnection().executeQuery(checkQuery);
            } catch (Exception e) {
            }
            if (results.length() > 0) {
                return "This login name is already being used. Please select a new login.";
            }
        }
        return null;
    }

    public GeneralQueryFormat getSaveQuery(boolean isnew, boolean isDeleted) {
        RunQueriesEx myQuery = new RunQueriesEx();

        if (!new String(password.getPassword()).equals(new String(confirm.getPassword()))) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Password does not match, please re-enter", "Error Saving", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        if (((fname.getText().trim().length() + lname.getText()).trim().length()) == 0 && !isDeleted) {
            return null;
        }

        String manage = "";
        try {
            manage = managementCompanies[1][manageCmbo.getSelectedIndex()];
        } catch (Exception e) {
            manage = myPar.currmanageid;
        }
        boolean isMaskedPW = false;

        String pw = new String(password.getPassword());
        if (origPw.equals(pw)) {
            isMaskedPW = true;
        }
        String md5 = "md5('" + login.getText() + "')";
        
        save_user_info_query saveQuery = new save_user_info_query();
        
        User tempUser = new User();
        tempUser.setActive(activeCheckBox.isSelected());
        tempUser.setCanViewSsn(ssnChk.isSelected());
        tempUser.setUserEmail(email.getText().trim());
        tempUser.setUserFirstName(fname.getText().trim());
        tempUser.setUserLastName(lname.getText().trim());
        tempUser.setUserLogin(login.getText().trim());
        tempUser.setUserManagementId((short)Integer.parseInt(manage));
        tempUser.setUserMiddleInitial(mname.getText().trim());
        tempUser.setUserPassword(pw);
        tempUser.setUserMd5(login.getText());
        try {
            tempUser.setUserId(Integer.parseInt(myparent.getMyIdForSave()));
        } catch (Exception exe) {}
        saveQuery.update(tempUser, isMaskedPW);

        myparent.getConnection().executeUpdate(saveQuery);
        
        myQuery.add(mySubPanels.get(0).getSaveDataQuery(null));
        return myQuery;
    }

    public boolean userHasAccess() {
        return true;
    }

    public void doOnClear() {
        ListGroupAccessPanel groupPanel = (ListGroupAccessPanel) mySubPanels.get(0);
        xListCompanyBranchAccessPanel branchPanel = (xListCompanyBranchAccessPanel) mySubPanels.get(1);

        groupPanel.setUserId("0");
        groupPanel.clearListView();
        branchPanel.setUserId("0");
        branchPanel.clearCompanyList();
        this.activeCheckBox.setSelected(true);
        this.grabManagementList();

    }

    public JPanel getMyForm() {
        return this;
    }

    public String getMyTabTitle() {
        return "";
    }

    public boolean needsMoreRecordSets() {
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        miscPanel = new javax.swing.JPanel();
        generalInfoPanel = new javax.swing.JPanel();
        BasicInfoPanel = new javax.swing.JPanel();
        nameLabelPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        spacerPanel3 = new javax.swing.JPanel();
        NameInfoPanel = new javax.swing.JPanel();
        fname = new javax.swing.JTextField();
        mname = new javax.swing.JTextField();
        lname = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        spacerPanel4 = new javax.swing.JPanel();
        ssnChk = new javax.swing.JCheckBox();
        spacerPanel1 = new javax.swing.JPanel();
        loginLabelPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        companyPanel = new javax.swing.JPanel();
        companyLabel = new javax.swing.JLabel();
        spacerPanel5 = new javax.swing.JPanel();
        LoginPanel = new javax.swing.JPanel();
        login = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        confirm = new javax.swing.JPasswordField();
        comboPanel = new javax.swing.JPanel();
        manageCmbo = new javax.swing.JComboBox();
        activeCheckBox = new javax.swing.JCheckBox();
        SecurityPanel = new javax.swing.JPanel();
        myTabbedPane = new javax.swing.JTabbedPane();

        miscPanel.setAlignmentX(1.0F);
        miscPanel.setLayout(new javax.swing.BoxLayout(miscPanel, javax.swing.BoxLayout.LINE_AXIS));

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        generalInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("General Information"));
        generalInfoPanel.setMaximumSize(new java.awt.Dimension(65772, 170));
        generalInfoPanel.setMinimumSize(new java.awt.Dimension(538, 170));
        generalInfoPanel.setPreferredSize(new java.awt.Dimension(538, 170));
        generalInfoPanel.setLayout(new javax.swing.BoxLayout(generalInfoPanel, javax.swing.BoxLayout.Y_AXIS));

        BasicInfoPanel.setLayout(new javax.swing.BoxLayout(BasicInfoPanel, javax.swing.BoxLayout.LINE_AXIS));

        nameLabelPanel.setLayout(new javax.swing.BoxLayout(nameLabelPanel, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("First Name:");
        jLabel1.setMaximumSize(new java.awt.Dimension(90, 24));
        jLabel1.setMinimumSize(new java.awt.Dimension(90, 24));
        jLabel1.setPreferredSize(new java.awt.Dimension(90, 24));
        nameLabelPanel.add(jLabel1);

        jLabel2.setText("Middle Initial:");
        jLabel2.setMaximumSize(new java.awt.Dimension(90, 24));
        jLabel2.setMinimumSize(new java.awt.Dimension(90, 24));
        jLabel2.setPreferredSize(new java.awt.Dimension(90, 24));
        nameLabelPanel.add(jLabel2);

        jLabel3.setText("Last Name:");
        jLabel3.setMaximumSize(new java.awt.Dimension(90, 24));
        jLabel3.setMinimumSize(new java.awt.Dimension(90, 24));
        jLabel3.setPreferredSize(new java.awt.Dimension(90, 24));
        nameLabelPanel.add(jLabel3);

        jLabel8.setText(" E-Mail:");
        jLabel8.setMaximumSize(new java.awt.Dimension(90, 24));
        jLabel8.setMinimumSize(new java.awt.Dimension(90, 24));
        jLabel8.setPreferredSize(new java.awt.Dimension(90, 24));
        nameLabelPanel.add(jLabel8);

        spacerPanel3.setAlignmentX(0.0F);
        spacerPanel3.setMaximumSize(new java.awt.Dimension(30, 19));
        spacerPanel3.setMinimumSize(new java.awt.Dimension(30, 19));
        spacerPanel3.setPreferredSize(new java.awt.Dimension(30, 19));
        nameLabelPanel.add(spacerPanel3);

        BasicInfoPanel.add(nameLabelPanel);

        NameInfoPanel.setLayout(new javax.swing.BoxLayout(NameInfoPanel, javax.swing.BoxLayout.Y_AXIS));

        fname.setAlignmentX(0.0F);
        fname.setMaximumSize(new java.awt.Dimension(32767, 24));
        fname.setMinimumSize(new java.awt.Dimension(150, 24));
        fname.setPreferredSize(new java.awt.Dimension(150, 24));
        NameInfoPanel.add(fname);

        mname.setAlignmentX(0.0F);
        mname.setMaximumSize(new java.awt.Dimension(40, 24));
        mname.setMinimumSize(new java.awt.Dimension(40, 24));
        mname.setPreferredSize(new java.awt.Dimension(40, 24));
        mname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mnameKeyTyped(evt);
            }
        });
        NameInfoPanel.add(mname);

        lname.setAlignmentX(0.0F);
        lname.setMaximumSize(new java.awt.Dimension(32767, 24));
        lname.setMinimumSize(new java.awt.Dimension(150, 24));
        lname.setPreferredSize(new java.awt.Dimension(150, 24));
        NameInfoPanel.add(lname);

        email.setAlignmentX(0.0F);
        email.setMaximumSize(new java.awt.Dimension(200, 24));
        email.setMinimumSize(new java.awt.Dimension(12, 24));
        email.setPreferredSize(new java.awt.Dimension(12, 24));
        NameInfoPanel.add(email);

        spacerPanel4.setAlignmentX(0.0F);
        spacerPanel4.setMaximumSize(new java.awt.Dimension(3000, 19));
        spacerPanel4.setMinimumSize(new java.awt.Dimension(30, 19));
        spacerPanel4.setPreferredSize(new java.awt.Dimension(30, 19));
        spacerPanel4.setLayout(new java.awt.GridLayout());

        ssnChk.setText("Can View SSN?");
        ssnChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ssnChkActionPerformed(evt);
            }
        });
        spacerPanel4.add(ssnChk);

        NameInfoPanel.add(spacerPanel4);

        BasicInfoPanel.add(NameInfoPanel);

        spacerPanel1.setMaximumSize(new java.awt.Dimension(30, 10));
        spacerPanel1.setMinimumSize(new java.awt.Dimension(30, 10));
        spacerPanel1.setPreferredSize(new java.awt.Dimension(30, 10));
        BasicInfoPanel.add(spacerPanel1);

        loginLabelPanel.setLayout(new javax.swing.BoxLayout(loginLabelPanel, javax.swing.BoxLayout.Y_AXIS));

        jLabel4.setText("Login Name:");
        jLabel4.setMaximumSize(new java.awt.Dimension(90, 24));
        jLabel4.setMinimumSize(new java.awt.Dimension(90, 24));
        jLabel4.setPreferredSize(new java.awt.Dimension(90, 24));
        loginLabelPanel.add(jLabel4);

        jLabel5.setText("Password:");
        jLabel5.setMaximumSize(new java.awt.Dimension(90, 24));
        jLabel5.setMinimumSize(new java.awt.Dimension(90, 24));
        jLabel5.setPreferredSize(new java.awt.Dimension(90, 24));
        loginLabelPanel.add(jLabel5);

        jLabel6.setText("Confirm PW:");
        jLabel6.setMaximumSize(new java.awt.Dimension(90, 24));
        jLabel6.setMinimumSize(new java.awt.Dimension(90, 24));
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 24));
        loginLabelPanel.add(jLabel6);

        companyPanel.setAlignmentX(0.0F);
        companyPanel.setMaximumSize(new java.awt.Dimension(90, 19));
        companyPanel.setMinimumSize(new java.awt.Dimension(90, 19));
        companyPanel.setPreferredSize(new java.awt.Dimension(90, 19));
        companyPanel.setLayout(new javax.swing.BoxLayout(companyPanel, javax.swing.BoxLayout.LINE_AXIS));

        companyLabel.setText("Company:");
        companyLabel.setMaximumSize(new java.awt.Dimension(90, 24));
        companyLabel.setMinimumSize(new java.awt.Dimension(90, 24));
        companyLabel.setPreferredSize(new java.awt.Dimension(90, 24));
        companyPanel.add(companyLabel);

        loginLabelPanel.add(companyPanel);

        spacerPanel5.setAlignmentX(0.0F);
        spacerPanel5.setMaximumSize(new java.awt.Dimension(30, 19));
        spacerPanel5.setMinimumSize(new java.awt.Dimension(30, 19));
        spacerPanel5.setPreferredSize(new java.awt.Dimension(30, 19));
        loginLabelPanel.add(spacerPanel5);

        BasicInfoPanel.add(loginLabelPanel);

        LoginPanel.setLayout(new javax.swing.BoxLayout(LoginPanel, javax.swing.BoxLayout.Y_AXIS));

        login.setAlignmentX(0.0F);
        login.setMaximumSize(new java.awt.Dimension(32767, 24));
        login.setMinimumSize(new java.awt.Dimension(150, 24));
        login.setPreferredSize(new java.awt.Dimension(150, 24));
        LoginPanel.add(login);

        password.setAlignmentX(0.0F);
        password.setMaximumSize(new java.awt.Dimension(32767, 24));
        password.setMinimumSize(new java.awt.Dimension(150, 24));
        password.setPreferredSize(new java.awt.Dimension(150, 24));
        LoginPanel.add(password);

        confirm.setAlignmentX(0.0F);
        confirm.setMaximumSize(new java.awt.Dimension(32767, 24));
        confirm.setMinimumSize(new java.awt.Dimension(150, 24));
        confirm.setPreferredSize(new java.awt.Dimension(150, 24));
        LoginPanel.add(confirm);

        comboPanel.setAlignmentX(0.0F);
        comboPanel.setMaximumSize(new java.awt.Dimension(32767, 24));
        comboPanel.setMinimumSize(new java.awt.Dimension(150, 19));
        comboPanel.setPreferredSize(new java.awt.Dimension(150, 19));
        comboPanel.setLayout(new javax.swing.BoxLayout(comboPanel, javax.swing.BoxLayout.LINE_AXIS));

        manageCmbo.setAlignmentX(0.0F);
        manageCmbo.setMaximumSize(new java.awt.Dimension(32767, 28));
        manageCmbo.setMinimumSize(new java.awt.Dimension(150, 24));
        manageCmbo.setPreferredSize(new java.awt.Dimension(150, 24));
        comboPanel.add(manageCmbo);

        LoginPanel.add(comboPanel);

        activeCheckBox.setSelected(true);
        activeCheckBox.setText("Active");
        activeCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        activeCheckBox.setDoubleBuffered(true);
        activeCheckBox.setFocusPainted(false);
        activeCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        activeCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        activeCheckBox.setMaximumSize(new java.awt.Dimension(32767, 19));
        activeCheckBox.setMinimumSize(new java.awt.Dimension(150, 19));
        activeCheckBox.setPreferredSize(new java.awt.Dimension(150, 19));
        activeCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activeCheckBoxActionPerformed(evt);
            }
        });
        LoginPanel.add(activeCheckBox);

        BasicInfoPanel.add(LoginPanel);

        generalInfoPanel.add(BasicInfoPanel);

        add(generalInfoPanel);

        SecurityPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Security Access"));
        SecurityPanel.setLayout(new java.awt.GridLayout(1, 0));
        SecurityPanel.add(myTabbedPane);

        add(SecurityPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void mnameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mnameKeyTyped
        if (this.mname.getText().length() > 1) {
            evt.consume();
        }
    }//GEN-LAST:event_mnameKeyTyped

    private void activeCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activeCheckBoxActionPerformed
        if (!this.activeCheckBox.isSelected()) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to deactivate this user?", "Deactivate User?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                myPar.saveData();
            } else {
                this.activeCheckBox.setSelected(true);
            }
        } else {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to activate this user?", "Activate User?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                myPar.saveData();
            } else {
                this.activeCheckBox.setSelected(false);
            }
        }
    }//GEN-LAST:event_activeCheckBoxActionPerformed

    private void ssnChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ssnChkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ssnChkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BasicInfoPanel;
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JPanel NameInfoPanel;
    private javax.swing.JPanel SecurityPanel;
    private javax.swing.JCheckBox activeCheckBox;
    private javax.swing.JPanel comboPanel;
    private javax.swing.JLabel companyLabel;
    private javax.swing.JPanel companyPanel;
    private javax.swing.JPasswordField confirm;
    private javax.swing.JTextField email;
    private javax.swing.JTextField fname;
    private javax.swing.JPanel generalInfoPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField lname;
    private javax.swing.JTextField login;
    private javax.swing.JPanel loginLabelPanel;
    private javax.swing.JComboBox manageCmbo;
    private javax.swing.JPanel miscPanel;
    private javax.swing.JTextField mname;
    private javax.swing.JTabbedPane myTabbedPane;
    private javax.swing.JPanel nameLabelPanel;
    private javax.swing.JPasswordField password;
    private javax.swing.JPanel spacerPanel1;
    private javax.swing.JPanel spacerPanel3;
    private javax.swing.JPanel spacerPanel4;
    private javax.swing.JPanel spacerPanel5;
    private javax.swing.JCheckBox ssnChk;
    // End of variables declaration//GEN-END:variables

}
