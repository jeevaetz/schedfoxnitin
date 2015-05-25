/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EmployeeLogin.java
 *
 * Created on Jul 30, 2010, 2:12:40 PM
 */
package rmischedule.employee.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import rmischedule.data_connection.Connection;
import rmischedule.employee.dashboard.FirstTimeEmployeePasswordChange;
import rmischedule.main.Main_Window;
import rmischedule.utility.TextField;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.login.first_time_employee_login_query;
import rmischeduleserver.mysqlconnectivity.queries.login.login_as_employee_query;

/**
 *
 * @author user
 */
public class EmployeeLogin extends javax.swing.JInternalFrame {

    private String companyDB;
    public static int headerHeight = 136;
    public static int headerWidth = 630;

    /** Creates new form EmployeeLogin */
    public EmployeeLogin(String companyDB) {
        initComponents();
        this.companyDB = companyDB;
        setTitle(Main_Window.compBranding.getLoginWindowText());

        //  set header icon based off companyDB
        setHeaderIcon(companyDB);

        //  set initial password field to blank
        loggedInPasswordTxt.setText("");
    }

    /**
     *  Method Name:  setHeaderIcon
     *  Purpose of Method:  determines and set the appropriate header icon based
     *      off the company DB; if there is no corresponding Icon for the 
     *      header it defaults to SchedFox logo
     *  Arguments:  a string containing the companyDB name
     *  Returns:  void
     *  Preconditions:  headerIcon not set
     *  Postconditions:  headerIcon set
     */
    private void setHeaderIcon(String companyDB) {
        headerLabel.setIcon(Main_Window.compBranding.getLoginHeader());
    }

    /**
     * Method to login the existing employee.
     */
    private void loginExistingEmployee() {
        String userName = loggedInEmpNumberTxt.getText().trim().replace("'", "''");
        String password = loggedInPasswordTxt.getText().trim().replace("'", "''");
        if (userName.length() == 0) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "Please enter a Employee Number or SSN!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (password.length() == 0) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "PLease enter a password!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Connection myConn = new Connection();
            try {
                if (companyDB != null && companyDB.length() > 0) {
                    login_as_employee_query loginQuery = new login_as_employee_query();
                    loginQuery.update(companyDB, userName, password);
                    Record_Set empLogin = myConn.executeQuery(loginQuery);
                    if (empLogin.getString("employee_id").length() > 0) {
                        Main_Window.parentOfApplication.setUser(empLogin.getString("employee_id"), companyDB);
                        //Main_Window.getEmpDashboard().setVisible(true);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                                "Invalid Login Information!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                            "Your company does not appear to have been setup to use employee login, please "
                            + "contact Schedfox for more information", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.out.println("Invalid employee login for: " + userName);
            }
        }
    }

    private void loginNewEmployee() {
        String lastName = newEmpLastNameTxt.getText().trim().replace("'", "''");
        String ssn = newEmpSSNTxt.getText().toString().trim().replace("'", "''");
        Integer employeeId = 0;
        try {
            employeeId = Integer.parseInt(newEmpIDTxt.getText().toString().trim().replace("'", "''"));
        } catch (Exception e) {}
        ssn = ssn.replaceAll("-", "");

        //  ensure properly formatted SSN
        Pattern ssnPattern = Pattern.compile("\\d{9}");
        Matcher ssnMatcher = ssnPattern.matcher(ssn);

        if (!ssnMatcher.matches() && ssn.replaceAll("_", "").length() > 0) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "This does not appear to be a valid SSN / Employee Num.",
                    "Error!", JOptionPane.ERROR_MESSAGE);
        } else if (lastName.length() == 0) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "Please enter in a last name!",
                    "Error!", JOptionPane.ERROR_MESSAGE);
        } else {
            first_time_employee_login_query loginQuery = new first_time_employee_login_query();
            loginQuery.update(companyDB, lastName, ssn, employeeId);
            System.out.println(loginQuery.toString());
            Connection myConn = new Connection();
            Record_Set rs = myConn.executeQuery(loginQuery);
            if (rs.length() == 0) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                        "Invalid Login Information! \r\nPlease try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else if (rs.length() > 1) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                        "Unexpected Error Encountered: Too many records for login.\r\n"
                        + "Please contact Schedfox.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int employee_id = rs.getInt("employee_id");
                if (rs.getString("employee_login").trim().length() == 0 && rs.getString("employee_password").trim().length() == 0) {
                    int value = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication,
                            "It appears that you have not set up this account with a user name an password.\r\n"
                            + "Would you like to do so now?",
                            "Error", JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.YES_OPTION) {
                        //Login
                        FirstTimeEmployeePasswordChange dialog = new FirstTimeEmployeePasswordChange(Main_Window.parentOfApplication,
                                true, this.companyDB, employee_id);
                        dialog.setVisible(true);
                        this.dispose();
                        return;
                    }
                }
                Main_Window.parentOfApplication.setUser(employee_id + "", companyDB);
                this.dispose();
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        headerLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        loggedInEmpNumberTxt = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        loggedInPasswordTxt = new javax.swing.JPasswordField();
        jPanel11 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        loggedInEmpLoginBtn = new javax.swing.JButton();
        jButtonNamePassword = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        newEmpLastNameTxt = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        newEmpSSNTxt = TextField.getSSNNewTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        newEmpIDTxt = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        loggedInNwEmpLoginBtn = new javax.swing.JButton();

        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel2.setMinimumSize(new java.awt.Dimension(0, 100));
        jPanel2.setPreferredSize(new java.awt.Dimension(634, 100));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        headerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        headerLabel.setMaximumSize(new java.awt.Dimension(9000, 900));
        jPanel2.add(headerLabel);

        getContentPane().add(jPanel2);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(15, 10, 0, 10)));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel1.setText("I have a Username / Password....");
        jPanel7.add(jLabel1);

        jPanel4.add(jPanel7);

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanel6.setLayout(new java.awt.GridLayout(0, 1));

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.Y_AXIS));

        jLabel3.setText("Login Name");
        jLabel3.setMaximumSize(new java.awt.Dimension(120, 20));
        jLabel3.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel3.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel9.add(jLabel3);

        loggedInEmpNumberTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        jPanel9.add(loggedInEmpNumberTxt);

        jPanel6.add(jPanel9);

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.Y_AXIS));

        jLabel4.setText("Password");
        jLabel4.setMaximumSize(new java.awt.Dimension(120, 20));
        jLabel4.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel4.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel10.add(jLabel4);

        loggedInPasswordTxt.setMaximumSize(new java.awt.Dimension(216456451, 28));
        loggedInPasswordTxt.setPreferredSize(new java.awt.Dimension(20, 28));
        jPanel10.add(loggedInPasswordTxt);

        jPanel6.add(jPanel10);

        jPanel4.add(jPanel6);

        jPanel11.setPreferredSize(new java.awt.Dimension(296, 900));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel11);

        loggedInEmpLoginBtn.setText("Login");
        loggedInEmpLoginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loggedInEmpLoginBtnActionPerformed(evt);
            }
        });

        jButtonNamePassword.setText("Forgot Name/Password?");
        jButtonNamePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNamePasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jButtonNamePassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(loggedInEmpLoginBtn)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loggedInEmpLoginBtn)
                    .addComponent(jButtonNamePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 23, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel4.add(jPanel5);

        jPanel1.add(jPanel4);

        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(15, 10, 0, 10)));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("I do not have a Username / Password....");
        jPanel8.add(jLabel2);

        jPanel3.add(jPanel8);

        jPanel12.setLayout(new java.awt.GridLayout(0, 1));

        jPanel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 0, 2, 0));
        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.Y_AXIS));

        jLabel6.setText("Last Name");
        jLabel6.setMaximumSize(new java.awt.Dimension(150, 20));
        jLabel6.setMinimumSize(new java.awt.Dimension(150, 20));
        jLabel6.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel14.add(jLabel6);

        newEmpLastNameTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        jPanel14.add(newEmpLastNameTxt);

        jPanel12.add(jPanel14);

        jPanel3.add(jPanel12);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(23, 92, 168));
        jLabel8.setText("And enter one of the following");
        jPanel18.add(jLabel8);

        jPanel3.add(jPanel18);

        jPanel17.setBorder(null);
        jPanel17.setMaximumSize(new java.awt.Dimension(32767, 200));
        jPanel17.setPreferredSize(new java.awt.Dimension(37, 200));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jPanel20.setLayout(new javax.swing.BoxLayout(jPanel20, javax.swing.BoxLayout.LINE_AXIS));

        jPanel15.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 0, 2, 0));
        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.Y_AXIS));

        jLabel7.setText("Social Sec No.");
        jLabel7.setMaximumSize(new java.awt.Dimension(150, 20));
        jLabel7.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel7.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel15.add(jLabel7);

        newEmpSSNTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        jPanel15.add(newEmpSSNTxt);

        jPanel20.add(jPanel15);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(23, 92, 168));
        jLabel5.setText("- Or -");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        jLabel5.setMaximumSize(new java.awt.Dimension(37, 50));
        jPanel20.add(jLabel5);

        jPanel19.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 0, 2, 0));
        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.Y_AXIS));

        jLabel9.setText("Employee ID");
        jLabel9.setMaximumSize(new java.awt.Dimension(150, 20));
        jLabel9.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel9.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel19.add(jLabel9);

        newEmpIDTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        jPanel19.add(newEmpIDTxt);

        jPanel20.add(jPanel19);

        jPanel17.add(jPanel20, java.awt.BorderLayout.NORTH);

        jPanel3.add(jPanel17);

        loggedInNwEmpLoginBtn.setText("Login");
        loggedInNwEmpLoginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loggedInNwEmpLoginBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(230, Short.MAX_VALUE)
                .addComponent(loggedInNwEmpLoginBtn)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loggedInNwEmpLoginBtn)
                .addContainerGap())
        );

        jPanel3.add(jPanel16);

        jPanel1.add(jPanel3);

        getContentPane().add(jPanel1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-644)/2, (screenSize.height-339)/2, 644, 339);
    }// </editor-fold>//GEN-END:initComponents

    private void loggedInEmpLoginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loggedInEmpLoginBtnActionPerformed
        loginExistingEmployee();
    }//GEN-LAST:event_loggedInEmpLoginBtnActionPerformed

    private void loggedInNwEmpLoginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loggedInNwEmpLoginBtnActionPerformed
        loginNewEmployee();
    }//GEN-LAST:event_loggedInNwEmpLoginBtnActionPerformed

    private void jButtonNamePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNamePasswordActionPerformed
        JOptionPane.showMessageDialog(null, "Resetting Login Name / Password functionality has not been added yet.", "Forgot Name/Password Button", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonNamePasswordActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel headerLabel;
    private javax.swing.JButton jButtonNamePassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton loggedInEmpLoginBtn;
    private javax.swing.JTextField loggedInEmpNumberTxt;
    private javax.swing.JButton loggedInNwEmpLoginBtn;
    private javax.swing.JPasswordField loggedInPasswordTxt;
    private javax.swing.JTextField newEmpIDTxt;
    private javax.swing.JTextField newEmpLastNameTxt;
    private javax.swing.JTextField newEmpSSNTxt;
    // End of variables declaration//GEN-END:variables
}
