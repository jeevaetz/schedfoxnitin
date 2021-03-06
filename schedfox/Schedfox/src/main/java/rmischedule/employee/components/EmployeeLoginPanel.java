/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EmployeeLoginPanel.java
 *
 * Created on May 20, 2010, 4:11:44 PM
 */
package rmischedule.employee.components;

import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JPanel;
import javax.swing.event.ListDataListener;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import rmischedule.employee.xEmployeeEdit;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_login_information_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.save_employee_login_info_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.security.check_user_login_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.security.grab_security_for_employee_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.security.save_security_group_for_employee_query;
import schedfoxlib.model.Employee;

/**
 *
 * @author user
 */
public class EmployeeLoginPanel extends GenericEditSubForm {

    private xEmployeeEdit parentForm;

    private String oPass;
    private String oLogin;
    private Vector modelVector;
    private UserSecurityModel securityModel = new UserSecurityModel();

    int numOfRecordSets = 0;

    /**
     * Creates new form EmployeeLoginPanel
     */
    public EmployeeLoginPanel(xEmployeeEdit parentForm) {
        initComponents();
        this.parentForm = parentForm;
        this.modelVector = new Vector();

        userText.setEnabled(this.allowUserToChangeLogin());
    }

    /**
     * Should this user be allowed to change the login name
     */
    private boolean allowUserToChangeLogin() {
        boolean empLoggedIn = Main_Window.parentOfApplication.isEmployeeLoggedIn();
        if (!empLoggedIn) {
            return true;
        } else {
            return true;
            //return security_detail.doesEmployeeHaveAccess(security_detail.EMPLOYEE_SEC.ALLOW_CHANGE_USER_LOGIN.getVal());
        }
    }

    public String checkData() {
        check_user_login_query checkLoginQuery = new check_user_login_query();
        String errors = null;
        try {
            checkLoginQuery.update(Integer.parseInt(parentForm.getMyIdForSave()), userText.getText());

            if (!this.confirmPasswordText.getText().equals(this.passwordText.getText())) {
                errors = "Passwords do not match!";
            }
            Record_Set rs = parentForm.getConnection().executeQuery(checkLoginQuery);
            try {
                for (int i = 0; i < rs.length(); i++) {
                    if (rs.getString("employee_id") != null && rs.getString("employee_id").length() > 0) {
                        //errors = "This login name is already in use!";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            checkLoginQuery.update(0, userText.getText());
        }

        return errors;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        prefixLabel1 = new javax.swing.JLabel();
        loginTypeCombo = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        prefixLabel = new javax.swing.JLabel();
        userText = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        Password = new javax.swing.JLabel();
        prefixLabel2 = new javax.swing.JLabel();
        passwordText = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        Password1 = new javax.swing.JLabel();
        prefixLabel3 = new javax.swing.JLabel();
        confirmPasswordText = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();

        setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Login Options"));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setMaximumSize(new java.awt.Dimension(100000, 26));
        jPanel4.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel4.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel4.setLayout(new java.awt.GridLayout(1, 0));
        jPanel1.add(jPanel4);

        jPanel6.setMaximumSize(new java.awt.Dimension(100000, 26));
        jPanel6.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel6.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("Login Type");
        jLabel2.setMaximumSize(new java.awt.Dimension(130, 18));
        jLabel2.setMinimumSize(new java.awt.Dimension(130, 18));
        jLabel2.setPreferredSize(new java.awt.Dimension(130, 18));
        jPanel6.add(jLabel2);

        prefixLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        prefixLabel1.setMaximumSize(new java.awt.Dimension(40, 14));
        prefixLabel1.setMinimumSize(new java.awt.Dimension(40, 14));
        prefixLabel1.setPreferredSize(new java.awt.Dimension(40, 14));
        jPanel6.add(prefixLabel1);

        loginTypeCombo.setModel(securityModel);
        loginTypeCombo.setMaximumSize(new java.awt.Dimension(32767, 22));
        loginTypeCombo.setMinimumSize(new java.awt.Dimension(23, 22));
        loginTypeCombo.setPreferredSize(new java.awt.Dimension(28, 22));
        jPanel6.add(loginTypeCombo);

        jPanel1.add(jPanel6);

        jPanel3.setMaximumSize(new java.awt.Dimension(100000, 26));
        jPanel3.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel3.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("User Name");
        jLabel1.setMaximumSize(new java.awt.Dimension(130, 18));
        jLabel1.setMinimumSize(new java.awt.Dimension(130, 18));
        jLabel1.setPreferredSize(new java.awt.Dimension(130, 18));
        jPanel3.add(jLabel1);

        prefixLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        prefixLabel.setMaximumSize(new java.awt.Dimension(40, 14));
        prefixLabel.setMinimumSize(new java.awt.Dimension(40, 14));
        prefixLabel.setPreferredSize(new java.awt.Dimension(40, 14));
        jPanel3.add(prefixLabel);

        userText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        userText.setMinimumSize(new java.awt.Dimension(6, 22));
        userText.setPreferredSize(new java.awt.Dimension(6, 22));
        jPanel3.add(userText);

        jPanel1.add(jPanel3);

        jPanel2.setMaximumSize(new java.awt.Dimension(100000, 26));
        jPanel2.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel2.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        Password.setText("Password");
        Password.setMaximumSize(new java.awt.Dimension(130, 18));
        Password.setMinimumSize(new java.awt.Dimension(130, 18));
        Password.setPreferredSize(new java.awt.Dimension(130, 18));
        jPanel2.add(Password);

        prefixLabel2.setMaximumSize(new java.awt.Dimension(40, 14));
        prefixLabel2.setMinimumSize(new java.awt.Dimension(40, 14));
        prefixLabel2.setPreferredSize(new java.awt.Dimension(40, 14));
        jPanel2.add(prefixLabel2);

        passwordText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        passwordText.setMinimumSize(new java.awt.Dimension(6, 22));
        passwordText.setPreferredSize(new java.awt.Dimension(6, 22));
        jPanel2.add(passwordText);

        jPanel1.add(jPanel2);

        jPanel7.setMaximumSize(new java.awt.Dimension(100000, 26));
        jPanel7.setMinimumSize(new java.awt.Dimension(10, 26));
        jPanel7.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        Password1.setText("Confirm Password");
        Password1.setMaximumSize(new java.awt.Dimension(130, 18));
        Password1.setMinimumSize(new java.awt.Dimension(130, 18));
        Password1.setPreferredSize(new java.awt.Dimension(130, 18));
        jPanel7.add(Password1);

        prefixLabel3.setMaximumSize(new java.awt.Dimension(40, 14));
        prefixLabel3.setMinimumSize(new java.awt.Dimension(40, 14));
        prefixLabel3.setPreferredSize(new java.awt.Dimension(40, 14));
        jPanel7.add(prefixLabel3);

        confirmPasswordText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        confirmPasswordText.setMinimumSize(new java.awt.Dimension(6, 22));
        confirmPasswordText.setPreferredSize(new java.awt.Dimension(6, 22));
        jPanel7.add(confirmPasswordText);

        jPanel1.add(jPanel7);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 523, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 255, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel5);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Password;
    private javax.swing.JLabel Password1;
    private javax.swing.JTextField confirmPasswordText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JComboBox loginTypeCombo;
    private javax.swing.JTextField passwordText;
    private javax.swing.JLabel prefixLabel;
    private javax.swing.JLabel prefixLabel1;
    private javax.swing.JLabel prefixLabel2;
    private javax.swing.JLabel prefixLabel3;
    private javax.swing.JTextField userText;
    // End of variables declaration//GEN-END:variables

    @Override
    public GeneralQueryFormat getQuery(boolean isSelected) {
        RunQueriesEx myCompleteQuery = new RunQueriesEx();
        employee_login_information_query login_query = new employee_login_information_query();
        try {
            login_query.update(Integer.parseInt(this.parentForm.getMyIdForSave()));
            myCompleteQuery.add(login_query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        grab_security_for_employee_query login_query_sec = new grab_security_for_employee_query();
        try {
            login_query_sec.update(Integer.parseInt(this.parentForm.getMyIdForSave()));
            myCompleteQuery.add(login_query_sec);
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return myCompleteQuery;
    }

    @Override
    public GeneralQueryFormat getSaveQuery(boolean isNewData) {
        RunQueriesEx completeSave = new RunQueriesEx();
        String newUserName = userText.getText();
        String newPassword = passwordText.getText();
        try {
            Employee currEmployee = (Employee) this.myparent.getSelectedObject();
            if (userText.getText().startsWith("XXXXX")) {
                newUserName = currEmployee.getEmployeeSsn();
            }
            if (passwordText.getText().startsWith("XXXXX")) {
                newPassword = currEmployee.getEmployeeSsn();
            }

            save_employee_login_info_query saveQuery = new save_employee_login_info_query();
            try {
                saveQuery.update(Integer.parseInt(parentForm.getMyIdForSave()), this.oLogin, newUserName, this.oPass, newPassword, true);
            } catch (Exception e) {
                saveQuery.newEmp(this.oLogin, userText.getText(), this.oPass, passwordText.getText(), true);
            }
            completeSave.add(saveQuery);

        } catch (Exception exe) {
        }
        save_security_group_for_employee_query save = new save_security_group_for_employee_query();
        try {
            save.update(parentForm.getMyIdForSave(), ((UserSecurity) securityModel.getSelectedItem()).user_security_id + "");
            completeSave.add(save);
        } catch (Exception e) {
        }
        return completeSave;
    }

    @Override
    public void loadData(Record_Set rs) {
        if (numOfRecordSets == 0) {
            boolean isLoginSSN = false;
            boolean isPasswordSSN = false;

            prefixLabel.setText(rs.getString("employee_login_prefix") + "-");
            userText.setText(rs.getString("employee_login"));
            String pass = rs.getString("employee_password");
            passwordText.setText(rs.getString("employee_password"));
            confirmPasswordText.setText(rs.getString("employee_password"));
            this.oPass = rs.getString("employee_password");
            this.oLogin = rs.getString("employee_login");

            try {
                Employee currEmployee = (Employee) this.myparent.getSelectedObject();
                isLoginSSN = currEmployee.getEmployeeSsn().equals(userText.getText());
                isPasswordSSN = currEmployee.getEmployeeSsn().equals(passwordText.getText());
            } catch (Exception exe) {
            }

            if (isLoginSSN && !Main_Window.parentOfApplication.getUser().getCanViewSsn()) {
                try {
                    userText.setText("XXXXX" + userText.getText().substring(userText.getText().length() - 4));
                } catch (Exception exe) {
                }
            };
            if (isPasswordSSN && !Main_Window.parentOfApplication.getUser().getCanViewSsn()) {
                try {
                    passwordText.setText("XXXXX" + passwordText.getText().substring(passwordText.getText().length() - 4));
                    confirmPasswordText.setText("XXXXX" + confirmPasswordText.getText().substring(confirmPasswordText.getText().length() - 4));
                } catch (Exception exe) {
                }
            };
        } else {
            modelVector.clear();
            int selectedIndex = -1;
            int i = 0;
            if (rs.length() > 0) {
                do {
                    UserSecurity userSec = new UserSecurity();
                    userSec.setUserSecName(rs.getString("name"));
                    userSec.setUser_security_id(rs.getInt("id"));
                    if (rs.getString("isselected").equals("t")) {
                        selectedIndex = i;
                    }
                    i++;
                    modelVector.add(userSec);
                } while (rs.moveNext());
            }
            this.securityModel.setUserSec(modelVector);
            this.securityModel.setSelectedIndex(selectedIndex);
        }
        System.out.println("");
    }

    @Override
    public boolean needsMoreRecordSets() {
        if (numOfRecordSets < 1) {
            numOfRecordSets++;
            return true;
        }
        numOfRecordSets = 0;
        return false;
    }

    @Override
    public String getMyTabTitle() {
        return "Login Info";
    }

    @Override
    public JPanel getMyForm() {
        return this;
    }

    @Override
    public void doOnClear() {

    }

    @Override
    public boolean userHasAccess() {
        return true;
    }

    private class UserSecurityModel implements ComboBoxModel {

        Vector<UserSecurity> userSec = new Vector<UserSecurity>();
        int selectedIndex = -1;

        public UserSecurityModel() {

        }

        public void setUserSec(Vector<UserSecurity> userSecurity) {
            this.userSec = userSecurity;
        }

        public int getSize() {
            return userSec.size();
        }

        public Object getElementAt(int index) {
            return userSec.get(index);
        }

        public void setSelectedIndex(int index) {
            this.selectedIndex = index;
        }

        public void setSelectedItem(Object anItem) {
            selectedIndex = this.userSec.indexOf(anItem);
        }

        public Object getSelectedItem() {
            if (selectedIndex < userSec.size() && selectedIndex >= 0) {
                return this.userSec.get(selectedIndex);
            } else {
                return "";
            }
        }

        public void addListDataListener(ListDataListener l) {

        }

        public void removeListDataListener(ListDataListener l) {

        }
    }

    public class UserSecurity {

        private int user_security_id;
        private String userSecName;

        public UserSecurity() {
            userSecName = new String();
        }

        /**
         * @return the user_security_id
         */
        public int getUser_security_id() {
            return user_security_id;
        }

        /**
         * @param user_security_id the user_security_id to set
         */
        public void setUser_security_id(int user_security_id) {
            this.user_security_id = user_security_id;
        }

        /**
         * @return the userSecName
         */
        public String getUserSecName() {
            return userSecName;
        }

        /**
         * @param userSecName the userSecName to set
         */
        public void setUserSecName(String userSecName) {
            this.userSecName = userSecName;
        }

        public boolean equals(Object obj) {
            boolean retVal = false;
            if (obj instanceof UserSecurity) {
                UserSecurity userObj = (UserSecurity) obj;
                retVal = this.user_security_id == userObj.getUser_security_id();
            }
            return retVal;
        }

        public String toString() {
            return userSecName;
        }
    }

}
