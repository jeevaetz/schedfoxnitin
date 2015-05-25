/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EmployeeLogin.java
 *
 * Created on Jul 30, 2010, 2:12:40 PM
 */

package rmischedule.client.login;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.login.login_as_client_query;
import rmischeduleserver.mysqlconnectivity.queries.client.save_client_query;
import rmischeduleserver.mysqlconnectivity.queries.client.client_login_information_query;
/**
 *
 * @author user
 */
public class ClientLogin extends javax.swing.JInternalFrame {

    private String companyDB;
    private Connection myConn;
    private String userId;
    private String url;
    private String userName;
    public static int headerHeight = 100;
    public static int headerWidth = 630;

    /** Creates new form EmployeeLogin */
    public ClientLogin(String companyDB) {
        initComponents();
        confirmPasswordTxt.setVisible(false);
        confirmPasswordLabel.setVisible(false);
        cancelButton.setVisible(false);
        this.companyDB = companyDB;
        setTitle(Main_Window.compBranding.getLoginWindowText());

        //  set header icon based off companyDB
        setHeaderIcon(companyDB);

        //  set initial password field to blank
        loggedInPasswordTxt.setText("");

        this.getRootPane().setDefaultButton(loggedInEmpLoginBtn);
    }
    public ClientLogin(String companyDB,boolean change) {
        this(companyDB);
        if(change){
            confirmPasswordTxt.setVisible(true);
            confirmPasswordLabel.setVisible(true);
            jLabel4.setText("New Password");
            cancelButton.setVisible(true);
            loggedInEmpLoginBtn.setText("Change");
            loggedInEmpLoginBtn.setActionCommand("Change");

            myConn=new Connection();
            myConn.setCompany(Main_Window.parentOfApplication.compBranding.getCompany().getId());

            rmischedule.security.User user=Main_Window.parentOfApplication.getUser();
            String userId=user.getUserId();

            client_login_information_query clientLogin=new client_login_information_query();
            clientLogin.update(Integer.parseInt(userId));
            Record_Set rs=new Record_Set();
            rs=myConn.executeQuery(clientLogin);
            url=new String(rs.getString("url"));
            String userName=new String(rs.getString("cusername"));
            loggedInEmpNumberTxt.setText(userName);
        }
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
    private void loginExistingClient() {
        String userName = loggedInEmpNumberTxt.getText().trim().replace("'", "''");
        String password = loggedInPasswordTxt.getText().trim().replace("'", "''");
        if (userName.length() == 0) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "Please enter in your login name!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (password.length() == 0) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "PLease enter a password!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Connection myConn = new Connection();
            try {
                if (companyDB != null && companyDB.length() > 0) {
                    login_as_client_query loginQuery = new login_as_client_query();
                    loginQuery.update(companyDB, userName, password);
                    Record_Set clientLogin = myConn.executeQuery(loginQuery);
                    if (clientLogin.getString("client_id").length() > 0) {
                        Main_Window.parentOfApplication.setUser(clientLogin.getString("client_id"), companyDB);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                                "Invalid Login Information!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                            "Your company does not appear to have been setup to use client login, please " +
                            "contact Schedfox for more information", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.out.println("Invalid client login for: " + userName);
            }
        }
    }

    private void changeLogin(){
        userName = loggedInEmpNumberTxt.getText().trim().replace("'", "''");
        String password=new String(confirmPasswordTxt.getPassword());
        String confirmPassword=new String(loggedInPasswordTxt.getPassword());

        if(userName.length()==0){
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "Please Enter User Name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(password.length()==0){
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "Please Enter Password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!confirmPassword.equalsIgnoreCase(password)){
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "Passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        rmischedule.security.User user=Main_Window.parentOfApplication.getUser();
        userId=user.getUserId();
        myConn=new Connection();
        myConn.setCompany(Main_Window.parentOfApplication.compBranding.getCompany().getId());

        if(checkForDuplicates(userName)){
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "This Login is already being used.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        save_client_query clientSave=new save_client_query();
        clientSave.update(userId,url,userName,password);

        myConn.executeUpdate(clientSave);
        JOptionPane.showMessageDialog(this, "Login Information Changed");
        this.dispose();

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private boolean checkForDuplicates(String login){
        client_login_information_query qry=new client_login_information_query();
        qry.update(login);

        Record_Set rs=myConn.executeQuery(qry);
        while(rs.getEOF()==false){
            if(rs.getInt("client_id")!=Integer.parseInt(userId)
                &&(rs.getString("cusername").equalsIgnoreCase(login))){
                    rs=null;
                    return(true);
            }
            rs.moveNext();
        }
        
        rs=null;
        return(false);

    }
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
        confirmPasswordLabel = new javax.swing.JLabel();
        confirmPasswordTxt = new javax.swing.JPasswordField();
        jPanel5 = new javax.swing.JPanel();
        loggedInEmpLoginBtn = new javax.swing.JButton();
        jButtonNamePassword = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

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

        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createEmptyBorder(15, 10, 0, 10)));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel1.setText("Welcome,");
        jPanel7.add(jLabel1);

        jPanel4.add(jPanel7);

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanel6.setLayout(new java.awt.GridLayout(0, 1));

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setText("Login Name");
        jLabel3.setMaximumSize(new java.awt.Dimension(120, 20));
        jLabel3.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel3.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel9.add(jLabel3);

        loggedInEmpNumberTxt.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        jPanel9.add(loggedInEmpNumberTxt);

        jPanel6.add(jPanel9);

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setText("Password");
        jLabel4.setMaximumSize(new java.awt.Dimension(120, 20));
        jLabel4.setMinimumSize(new java.awt.Dimension(120, 20));
        jLabel4.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel10.add(jLabel4);

        loggedInPasswordTxt.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        loggedInPasswordTxt.setPreferredSize(new java.awt.Dimension(20, 6));
        jPanel10.add(loggedInPasswordTxt);

        jPanel6.add(jPanel10);

        jPanel11.setPreferredSize(new java.awt.Dimension(296, 900));
        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        confirmPasswordLabel.setText("Confirm Password");
        confirmPasswordLabel.setMaximumSize(new java.awt.Dimension(120, 20));
        confirmPasswordLabel.setMinimumSize(new java.awt.Dimension(120, 20));
        confirmPasswordLabel.setPreferredSize(new java.awt.Dimension(120, 20));
        jPanel11.add(confirmPasswordLabel);

        confirmPasswordTxt.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        confirmPasswordTxt.setPreferredSize(new java.awt.Dimension(20, 6));
        jPanel11.add(confirmPasswordTxt);

        jPanel6.add(jPanel11);

        jPanel4.add(jPanel6);

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

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jButtonNamePassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 156, Short.MAX_VALUE)
                .addComponent(cancelButton)
                .addGap(26, 26, 26)
                .addComponent(loggedInEmpLoginBtn)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loggedInEmpLoginBtn)
                    .addComponent(jButtonNamePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        jPanel4.add(jPanel5);

        jPanel1.add(jPanel4);

        getContentPane().add(jPanel1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-512)/2, (screenSize.height-331)/2, 512, 331);
    }// </editor-fold>//GEN-END:initComponents

    private void loggedInEmpLoginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loggedInEmpLoginBtnActionPerformed
        if(evt.getActionCommand().equalsIgnoreCase("Login"))
            loginExistingClient();
        else if(evt.getActionCommand().equalsIgnoreCase("Change"))
            changeLogin();
    }//GEN-LAST:event_loggedInEmpLoginBtnActionPerformed

    private void jButtonNamePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNamePasswordActionPerformed
       JOptionPane.showMessageDialog(null, "Resetting Login Name / Password functionality has not been added yet.", "Forgot Name/Password Button", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonNamePasswordActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel confirmPasswordLabel;
    private javax.swing.JPasswordField confirmPasswordTxt;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JButton jButtonNamePassword;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton loggedInEmpLoginBtn;
    private javax.swing.JTextField loggedInEmpNumberTxt;
    private javax.swing.JPasswordField loggedInPasswordTxt;
    // End of variables declaration//GEN-END:variables

}
