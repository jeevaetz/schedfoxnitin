/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewUserScreen1.java
 *
 * Created on Jun 24, 2010, 12:07:03 PM
 */

package rmischedule.new_user;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import rmischedule.admin.newuser_alert.Send_New_User_Alerts;
import rmischedule.data_connection.Connection;
import rmischedule.login.Login_Frame;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.CompanyController;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.new_user.create_company_query;
import rmischeduleserver.mysqlconnectivity.queries.new_user.create_user_query;
import rmischeduleserver.mysqlconnectivity.queries.new_user.get_billing_sequence_id_query;
import rmischeduleserver.mysqlconnectivity.queries.new_user.save_billing_modules_query;
import rmischeduleserver.mysqlconnectivity.queries.new_user.save_modules_to_billing_query;
import rmischeduleserver.mysqlconnectivity.queries.new_user.update_branch_info_query;

/**
 *
 * @author user
 */
public class NewUserScreen extends javax.swing.JInternalFrame {

    private ArrayList<NewUserSubScreen> subScreens;
    private int subScreenSelected = 0;
    private JInternalFrame myParent;
    private Hashtable<String, Object> myNewUserValues;

    private static String cancelString = "Cancel";
    private static String finishString = "Finish";

    /** Creates new form NewUserScreen1 */
    public NewUserScreen(JInternalFrame parent) {
        initComponents();

        myParent = parent;
        subScreens = new ArrayList<NewUserSubScreen>();
        myNewUserValues = new Hashtable<String, Object>();

        this.gradWhichScreensToDisplay();
        this.initializeMyCardLayout();

        updateScreen(subScreenSelected);
    }

    private void setupUser(NewUser user, NewCompany company, ArrayList<Integer> selectedBillingModules, String schemaName) {
        create_company_query newCompanyQuery = new create_company_query();
        newCompanyQuery.update(company.getCompanyName(), company.getCompanyAddress(),
                "", company.getCompanyCity(), company.getCompanyState(), company.getCompanyZip(),
                company.getCompanyPhone(), user.getEmail(), schemaName);
        RunQueriesEx myQueryEx = new RunQueriesEx();
        myQueryEx.add(newCompanyQuery);
        Connection myConn = new Connection();
        ArrayList<Record_Set> sets = myConn.executeQueryEx(myQueryEx);
        Record_Set rst = sets.get(0);
        int companyId = rst.getInt(0);

        get_billing_sequence_id_query billingQuery = new get_billing_sequence_id_query();
        Record_Set billingRecord = myConn.executeQuery(billingQuery);
        int companyBillingId = billingRecord.getInt("id");
        save_billing_modules_query saveModulesQuery = new save_billing_modules_query();
        saveModulesQuery.setPreparedStatement(new Object[]{companyBillingId, companyId});
        myConn.executeQuery(saveModulesQuery);

        save_modules_to_billing_query saveModules = new save_modules_to_billing_query();
        saveModules.update(companyBillingId, selectedBillingModules);
        myConn.executeQuery(saveModules);

        create_user_query myUserQuery = new create_user_query();
        myUserQuery.update(user.getFirstName(), user.getLastName(), "",
                user.getUserName(), user.getPassWord(), user.getEmail(), companyId);
        myConn.executeQuery(myUserQuery);

    }

    /**
     *  Method Name:  sendOutEmailAlert
     *  Purpose of Method:  method currently
     */

    public void finishSetup() {
        //  send out new user alerts
        Send_New_User_Alerts sendAlerts = new Send_New_User_Alerts();
        sendAlerts.loadNewUserInformation(myNewUserValues);
        sendAlerts.sendSMSAlerts();
        sendAlerts.sendEmailAlerts();

        //  construct new database
        Iterator<String> keys = myNewUserValues.keySet().iterator();
        nextBtn.setEnabled(false);
        NewUser user = null;
        NewCompany company = null;
        ArrayList<Integer> selectedBillingModules = null;
        while (keys.hasNext()) {
            String key = keys.next();
            if (myNewUserValues.get(key) instanceof NewUser) {
                user = (NewUser)myNewUserValues.get(key);
            } else if (myNewUserValues.get(key) instanceof NewCompany) {
                company = (NewCompany)myNewUserValues.get(key);
            } else if (key.equalsIgnoreCase("selectedBillingMod")) {
                selectedBillingModules = (ArrayList<Integer>)myNewUserValues.get(key);;
            }
        }
        try {
            CompanyController companyService = new CompanyController();
            String sourceSchema = "template0_db";
            if (myNewUserValues.get("database") != null) {
                sourceSchema = myNewUserValues.get("database").toString();
            }
            String schema = companyService.setupCompanySchema(company.getBasicCompanySchemaName(), sourceSchema);
            this.setupUser(user, company, selectedBillingModules, schema);
            
            try {
                Thread.sleep(10000);
            } catch (Exception e) {}
            new Connection().relaodClientInfoOnServer();
            (new FixBranchThread(schema)).start();
            
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Just a friendly reminder, your username is: \r\n\r\n"
                    + user.getUserName(), "", JOptionPane.INFORMATION_MESSAGE);
            
            Login_Frame.goLogin(user.getUserName(), user.getPassWord(), new Connection(), new String[0], null);

            

            this.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, e.getMessage());
        }
    }

    public void nextScreen() {
        ArrayList<String> currentScreenErrors =
                subScreens.get(subScreenSelected).getValidationString();
        if (currentScreenErrors.size() == 0) {
            myNewUserValues.putAll(subScreens.get(subScreenSelected).getValues());
            if (nextBtn.getText().equals(finishString)) {
                this.finishSetup();
            } else {
                subScreenSelected++;
                updateScreen(subScreenSelected);
            }
        } else {
            StringBuffer errors = new StringBuffer();
            errors.append("You have the following errors:\r\n\r\n");
            for (int e = 0; e < currentScreenErrors.size(); e++) {
                errors.append("          " + currentScreenErrors.get(e) + "\r\n");
            }
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, errors.toString(), "Error(s)!", JOptionPane.ERROR_MESSAGE,
                    Main_Window.Large_Error_Icon);
        }
    }

    public void previousScreen() {
        if (previousBtn.getText().equals(cancelString)) {
            myParent.setVisible(true);
            this.setVisible(false);
        } else {
            subScreenSelected--;
            updateScreen(subScreenSelected);
        }
    }

    /**
     * Later will be used to determine what screens to show.
     */
    public void gradWhichScreensToDisplay() {
        subScreens.add(new BasicUserInformationSubScreen());
        subScreens.add(new BasicCompanyInformationSubScreen());
        //subScreens.add(new ChooseTemplateSubScreen());
        subScreens.add(new BillingModulesSubScreen());
    }

    public void initializeMyCardLayout() {
        for (int s = 0; s < subScreens.size(); s++) {
            mainPanel.add(subScreens.get(s).getContainer(), "Panel" + s);
        }
    }

    public void updateScreen(int val) {
        CardLayout panelLayout = (CardLayout)mainPanel.getLayout();
        panelLayout.show(mainPanel, "Panel" + val);
        statusLabel.setText("Step " + (val + 1) + " of " + (subScreens.size()));
        progressBar.setValue(new Double(((double)val) / ((double)subScreens.size()) * 100).intValue());
        if (val <= 0) {
            previousBtn.setText(cancelString);
        } else {
            previousBtn.setText("Previous");
        }
        if (val == subScreens.size() - 1) {
            nextBtn.setText(finishString);
        } else {
            nextBtn.setText("Next Step");
        }
    }

    private class FixBranchThread extends Thread {

        private String companyDB;

        public FixBranchThread(String companyDB) {
            this.companyDB = companyDB;
        }

        @Override
        public void run() {
            try {
                sleep(2000);
            } catch (Exception e) {}
            update_branch_info_query updateSchemaQuery = new update_branch_info_query();
            updateSchemaQuery.update(companyDB);
            (new Connection()).executeUpdate(updateSchemaQuery);
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

        jLabel2 = new javax.swing.JLabel();
        topPanel = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        bottomPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        previousBtn = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        jPanel5 = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        nextBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        jLabel2.setFont(new java.awt.Font("SansSerif", 2, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Welcome to Schedfox");

        setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        topPanel.setMaximumSize(new java.awt.Dimension(32767, 40));
        topPanel.setMinimumSize(new java.awt.Dimension(40, 40));
        topPanel.setPreferredSize(new java.awt.Dimension(564, 40));
        topPanel.setLayout(new java.awt.GridLayout(1, 0));
        getContentPane().add(topPanel);

        mainPanel.setMaximumSize(new java.awt.Dimension(50000000, 500000));
        mainPanel.setLayout(new java.awt.CardLayout());
        getContentPane().add(mainPanel);

        bottomPanel.setMaximumSize(new java.awt.Dimension(32767, 50));
        bottomPanel.setMinimumSize(new java.awt.Dimension(0, 50));
        bottomPanel.setPreferredSize(new java.awt.Dimension(564, 50));
        bottomPanel.setLayout(new javax.swing.BoxLayout(bottomPanel, javax.swing.BoxLayout.LINE_AXIS));

        jPanel3.setMaximumSize(new java.awt.Dimension(5, 32767));
        jPanel3.setMinimumSize(new java.awt.Dimension(5, 0));
        jPanel3.setPreferredSize(new java.awt.Dimension(5, 40));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        bottomPanel.add(jPanel3);

        previousBtn.setText("Previous Step");
        previousBtn.setMaximumSize(new java.awt.Dimension(100, 23));
        previousBtn.setMinimumSize(new java.awt.Dimension(100, 23));
        previousBtn.setPreferredSize(new java.awt.Dimension(100, 23));
        previousBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousScreen(evt);
            }
        });
        bottomPanel.add(previousBtn);

        statusPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 8, 3, 8));
        statusPanel.setMaximumSize(new java.awt.Dimension(5000, 80));
        statusPanel.setMinimumSize(new java.awt.Dimension(4, 40));
        statusPanel.setLayout(new javax.swing.BoxLayout(statusPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 12));
        jPanel6.setMinimumSize(new java.awt.Dimension(40, 12));
        jPanel6.setPreferredSize(new java.awt.Dimension(0, 12));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        statusPanel.add(jPanel6);

        jPanel4.setLayout(new java.awt.GridLayout(1, 0));
        jPanel4.add(progressBar);

        statusPanel.add(jPanel4);

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel5.setMinimumSize(new java.awt.Dimension(40, 20));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        statusLabel.setText("Step 1 of 3");
        jPanel5.add(statusLabel);

        statusPanel.add(jPanel5);

        bottomPanel.add(statusPanel);

        nextBtn.setText("Next Step");
        nextBtn.setMaximumSize(new java.awt.Dimension(100, 23));
        nextBtn.setMinimumSize(new java.awt.Dimension(100, 23));
        nextBtn.setPreferredSize(new java.awt.Dimension(100, 23));
        nextBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextScreen(evt);
            }
        });
        bottomPanel.add(nextBtn);

        jPanel2.setMaximumSize(new java.awt.Dimension(5, 32767));
        jPanel2.setMinimumSize(new java.awt.Dimension(5, 0));
        jPanel2.setPreferredSize(new java.awt.Dimension(5, 40));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        bottomPanel.add(jPanel2);

        getContentPane().add(bottomPanel);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-564)/2, (screenSize.height-399)/2, 564, 399);
    }// </editor-fold>//GEN-END:initComponents

    private void nextScreen(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextScreen
        this.nextScreen();
    }//GEN-LAST:event_nextScreen

    private void previousScreen(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousScreen
        this.previousScreen();
    }//GEN-LAST:event_previousScreen


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton nextBtn;
    private javax.swing.JButton previousBtn;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables

}
