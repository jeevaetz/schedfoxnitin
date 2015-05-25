/*
 * SignupScreenOne.java
 *
 * Created on February 2, 2008, 10:20 AM
 */
package rmischedule.new_user;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import rmischedule.data_connection.Connection;
import rmischedule.login.Login_Frame;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.new_user.check_if_user_exists_query;
import rmischeduleserver.mysqlconnectivity.queries.new_user.create_new_schema_for_company;
import rmischeduleserver.mysqlconnectivity.queries.new_user.create_new_user_query;
import rmischeduleserver.mysqlconnectivity.queries.new_user.does_schema_exist_query;

/**
 *
 * @author  Ira
 */
public class SignupScreenOne extends javax.swing.JDialog {

    private Connection myConnection;
    private int originalHeight;
    private int currHeight;
    private Hashtable<JComponent, Border> hashOfOriginalBorders;
    private Login_Frame parentFrame;
    
    
    /** Creates new form SignupScreenOne */
    public SignupScreenOne(java.awt.Frame parent, boolean modal, Login_Frame parentFrame) {
        super(parent, modal);
        initComponents();
        jProgressBar1.setVisible(false);
        originalHeight = this.getSize().height;
        hashOfOriginalBorders = new Hashtable();
        myConnection = new Connection();
        this.disableLaterTabs();
        this.parentFrame = parentFrame;
    }

    private String getValidSchemaName(String company) {
        String retVal = "";
        int i = 0;
        
        String zCompany = company.replace(',', '_');

        zCompany = zCompany.replace('.', '_');
        zCompany = zCompany.replace('&', '_');
        zCompany = zCompany.replace('\'', '_');                
        zCompany = zCompany.replace('"', '_');
        zCompany = zCompany.replace('+', '_');
        zCompany = zCompany.replace('-', '_');
        zCompany = zCompany.replace('=', '_');
        zCompany = zCompany.replace('(', '_');
        zCompany = zCompany.replace(')', '_');
        zCompany = zCompany.replace('*', '_');
        zCompany = zCompany.replace('@', '_');
        zCompany = zCompany.replace('$', '_');
        zCompany = zCompany.replace('^', '_');
        zCompany = zCompany.replace('!', '_');
        
        while (retVal == "") {
            does_schema_exist_query checkCompany = new does_schema_exist_query();
            checkCompany.update(zCompany + i++ + "_db");
            try {
                this.myConnection.prepQuery(checkCompany);
                Record_Set rs = myConnection.executeQuery(checkCompany);
                if (rs.getInt("schema_count") == 0) {
                    retVal = checkCompany.getSchemaName();
                }
            } catch (Exception e) {
                this.addError("Could not create your account, please contact customer service!", null);
            }
        }
        return retVal;
    }

    private boolean createNewSchemaForCompany(String company) {
        boolean retVal = false;
        try {
            String response = this.myConnection.createNewDb(company);
            if(response.equals("error")) {
                retVal = false;
            }else{
                retVal = true;
            }
        } catch (Exception e) {
            this.addError("Could not create your account, please contact customer service!", null);
        }
        return retVal;
    }

    private boolean completeSignupProcess() {
        boolean retVal = true;
        String schema = this.getValidSchemaName(this.companyNameText.getText());
        create_new_user_query new_user_query = new create_new_user_query();
        new_user_query.update(this.firstNameText.getText(), this.lastNameText.getText(),
                "", this.emailText.getText(), new String(this.passwordText.getPassword()),
                this.companyNameText.getText(), this.addressLine1Text.getText(),
                this.addressLine2Text.getText(), this.cityText.getText(), this.stateText.getText(),
                this.zipText.getText(), this.companyPhoneText.getText(), schema);
        try {
            this.myConnection.prepQuery(new_user_query);
            myConnection.executeUpdate(new_user_query);
            this.createNewSchemaForCompany(schema);
            String[] fakeArray = new String[0];
            Login_Frame.goLogin(emailText.getText(), new String(passwordText.getPassword()), myConnection, fakeArray, parentFrame);
            this.dispose();
        } catch (Exception e) {
            this.addError("Could not create your account, please contact customer service!", null);
            retVal = false;
        }
        return retVal;
    }

    private void disableLaterTabs() {
        int currentSelection = tabbedPane.getSelectedIndex() + 1;
        for (int i = currentSelection; i <= 2; i++) {
            tabbedPane.setEnabledAt(i, false);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        stepOnePanel = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        informationPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        firstNameText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        lastNameText = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        emailText = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        confirmEmailText = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        passwordText = new javax.swing.JPasswordField();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        confirmPasswordText = new javax.swing.JPasswordField();
        stepTwoPanel = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        companyInfoPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        companyNameText = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        addressLine1Text = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        addressLine2Text = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cityText = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        stateText = new javax.swing.JFormattedTextField();
        jPanel14 = new javax.swing.JPanel();
        zipText = new javax.swing.JFormattedTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        companyPhoneText = new javax.swing.JFormattedTextField();
        companySetupPanel = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        loginNameLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jcbAgree = new javax.swing.JCheckBox();
        displayPanelContainer = new javax.swing.JPanel();
        displayPanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New User Registration"); // NOI18N
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        tabbedPane.setMaximumSize(new java.awt.Dimension(32767, 350));
        tabbedPane.setMinimumSize(new java.awt.Dimension(452, 300));
        tabbedPane.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabbedPaneFocusGained(evt);
            }
        });

        stepOnePanel.setLayout(new javax.swing.BoxLayout(stepOnePanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        jPanel12.setMaximumSize(new java.awt.Dimension(32767, 35));
        jPanel12.setMinimumSize(new java.awt.Dimension(0, 35));
        jPanel12.setPreferredSize(new java.awt.Dimension(100, 35));
        jPanel12.setLayout(new java.awt.GridLayout(1, 0));

        jLabel17.setFont(new java.awt.Font("Arial Black", 0, 24));
        jLabel17.setForeground(new java.awt.Color(50, 56, 87));
        jLabel17.setText("Free Signup"); // NOI18N
        jPanel12.add(jLabel17);

        stepOnePanel.add(jPanel12);

        informationPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        informationPanel.setPreferredSize(new java.awt.Dimension(412, 300));
        informationPanel.setLayout(new javax.swing.BoxLayout(informationPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setMaximumSize(new java.awt.Dimension(300000, 30));
        jPanel4.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel4.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.X_AXIS));

        jLabel1.setText("First Name"); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(160, 22));
        jLabel1.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel1.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel4.add(jLabel1);

        firstNameText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        jPanel4.add(firstNameText);

        jLabel2.setText("    Last Name"); // NOI18N
        jLabel2.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel2.setMinimumSize(new java.awt.Dimension(100, 14));
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 14));
        jPanel4.add(jLabel2);

        lastNameText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        jPanel4.add(lastNameText);

        informationPanel.add(jPanel4);

        jPanel1.setMaximumSize(new java.awt.Dimension(300000, 30));
        jPanel1.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jLabel4.setText("Email Address"); // NOI18N
        jLabel4.setMaximumSize(new java.awt.Dimension(160, 22));
        jLabel4.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel4.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel1.add(jLabel4);

        emailText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        emailText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextActionPerformed(evt);
            }
        });
        jPanel1.add(emailText);

        informationPanel.add(jPanel1);

        jPanel8.setMaximumSize(new java.awt.Dimension(300000, 30));
        jPanel8.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel8.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.X_AXIS));

        jLabel8.setText("Confirm Email"); // NOI18N
        jLabel8.setMaximumSize(new java.awt.Dimension(160, 22));
        jLabel8.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel8.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel8.add(jLabel8);

        confirmEmailText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        jPanel8.add(confirmEmailText);

        informationPanel.add(jPanel8);

        jPanel6.setMaximumSize(new java.awt.Dimension(300000, 30));
        jPanel6.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel6.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.X_AXIS));

        jLabel6.setText("Password"); // NOI18N
        jLabel6.setMaximumSize(new java.awt.Dimension(160, 22));
        jLabel6.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel6.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel6.add(jLabel6);

        passwordText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        jPanel6.add(passwordText);

        informationPanel.add(jPanel6);

        jPanel7.setMaximumSize(new java.awt.Dimension(300000, 30));
        jPanel7.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel7.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.X_AXIS));

        jLabel7.setText("Confirm Password"); // NOI18N
        jLabel7.setMaximumSize(new java.awt.Dimension(160, 22));
        jLabel7.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel7.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel7.add(jLabel7);

        confirmPasswordText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        jPanel7.add(confirmPasswordText);

        informationPanel.add(jPanel7);

        stepOnePanel.add(informationPanel);

        tabbedPane.addTab("Basic Information", stepOnePanel);

        stepTwoPanel.setLayout(new javax.swing.BoxLayout(stepTwoPanel, javax.swing.BoxLayout.Y_AXIS));

        jLabel18.setFont(new java.awt.Font("Arial Black", 0, 24));
        jLabel18.setForeground(new java.awt.Color(50, 56, 87));
        jLabel18.setText("Your Company Information"); // NOI18N
        stepTwoPanel.add(jLabel18);

        companyInfoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        companyInfoPanel.setLayout(new javax.swing.BoxLayout(companyInfoPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(300000, 30));
        jPanel2.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        jLabel3.setText("Company Name"); // NOI18N
        jLabel3.setMaximumSize(new java.awt.Dimension(150, 22));
        jLabel3.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 22));
        jPanel2.add(jLabel3);

        companyNameText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        jPanel2.add(companyNameText);

        companyInfoPanel.add(jPanel2);

        jPanel5.setMaximumSize(new java.awt.Dimension(300000, 30));
        jPanel5.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel5.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.X_AXIS));

        jLabel5.setText("Address Line 1"); // NOI18N
        jLabel5.setMaximumSize(new java.awt.Dimension(150, 22));
        jLabel5.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 22));
        jPanel5.add(jLabel5);

        addressLine1Text.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        addressLine1Text.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressLine1TextActionPerformed(evt);
            }
        });
        jPanel5.add(addressLine1Text);

        companyInfoPanel.add(jPanel5);

        jPanel9.setMaximumSize(new java.awt.Dimension(300000, 30));
        jPanel9.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel9.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.X_AXIS));

        jLabel9.setText("Address Line 2"); // NOI18N
        jLabel9.setMaximumSize(new java.awt.Dimension(150, 22));
        jLabel9.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel9.setPreferredSize(new java.awt.Dimension(100, 22));
        jPanel9.add(jLabel9);

        addressLine2Text.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        addressLine2Text.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressLine2TextActionPerformed(evt);
            }
        });
        jPanel9.add(addressLine2Text);

        companyInfoPanel.add(jPanel9);

        jPanel10.setMaximumSize(new java.awt.Dimension(300000, 30));
        jPanel10.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel10.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.X_AXIS));

        jLabel10.setText("City, State Zip"); // NOI18N
        jLabel10.setMaximumSize(new java.awt.Dimension(150, 22));
        jLabel10.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel10.setPreferredSize(new java.awt.Dimension(100, 22));
        jPanel10.add(jLabel10);

        cityText.setMargin(new java.awt.Insets(1, 5, 2, 10));
        cityText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        jPanel10.add(cityText);

        jPanel13.setMaximumSize(new java.awt.Dimension(5, 32767));
        jPanel13.setMinimumSize(new java.awt.Dimension(5, 100));
        jPanel13.setPreferredSize(new java.awt.Dimension(5, 100));

        org.jdesktop.layout.GroupLayout jPanel13Layout = new org.jdesktop.layout.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 5, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 30, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel13);

        try {
            stateText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("??")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        stateText.setMaximumSize(new java.awt.Dimension(40, 22));
        stateText.setMinimumSize(new java.awt.Dimension(40, 19));
        stateText.setPreferredSize(new java.awt.Dimension(30, 19));
        jPanel10.add(stateText);

        jPanel14.setMaximumSize(new java.awt.Dimension(5, 32767));
        jPanel14.setMinimumSize(new java.awt.Dimension(5, 100));
        jPanel14.setPreferredSize(new java.awt.Dimension(5, 100));

        org.jdesktop.layout.GroupLayout jPanel14Layout = new org.jdesktop.layout.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 5, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 30, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel14);

        try {
            zipText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        zipText.setMaximumSize(new java.awt.Dimension(60, 22));
        zipText.setMinimumSize(new java.awt.Dimension(60, 19));
        zipText.setPreferredSize(new java.awt.Dimension(60, 22));
        jPanel10.add(zipText);

        companyInfoPanel.add(jPanel10);

        jPanel11.setMaximumSize(new java.awt.Dimension(300000, 30));
        jPanel11.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel11.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.X_AXIS));

        jLabel11.setText("Contact Phone No."); // NOI18N
        jLabel11.setMaximumSize(new java.awt.Dimension(150, 22));
        jLabel11.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel11.setPreferredSize(new java.awt.Dimension(100, 22));
        jPanel11.add(jLabel11);

        try {
            companyPhoneText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(###)###-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        companyPhoneText.setMaximumSize(new java.awt.Dimension(2147483647, 22));
        companyPhoneText.setMinimumSize(new java.awt.Dimension(11, 19));
        companyPhoneText.setPreferredSize(new java.awt.Dimension(126, 19));
        jPanel11.add(companyPhoneText);

        companyInfoPanel.add(jPanel11);

        stepTwoPanel.add(companyInfoPanel);

        tabbedPane.addTab("Company Information", stepTwoPanel);

        companySetupPanel.setLayout(new javax.swing.BoxLayout(companySetupPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel15.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1));
        jPanel15.setLayout(new java.awt.GridLayout(1, 0));

        jLabel19.setFont(new java.awt.Font("Arial Black", 0, 24));
        jLabel19.setForeground(new java.awt.Color(50, 56, 87));
        jLabel19.setText("Terms and Conditions"); // NOI18N
        jPanel15.add(jLabel19);

        companySetupPanel.add(jPanel15);

        jLabel12.setText("Important: Your email you have provided will be your login name."); // NOI18N

        jLabel15.setText("Login Name:"); // NOI18N

        loginNameLabel.setMaximumSize(new java.awt.Dimension(0, 20));
        loginNameLabel.setMinimumSize(new java.awt.Dimension(0, 18));
        loginNameLabel.setPreferredSize(new java.awt.Dimension(0, 18));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTextPane1.setContentType("text/html"); // NOI18N
        jTextPane1.setEditable(false);
        jTextPane1.setText("\n<b>AGREEMENT FOR SCHEDFOX SOFTWARE</b><br><br>\n\nThis agreement is between SchedFox, Inc., a Texas corporation, and your company. This Agreement applies to your use of the SchedFox Online Employee Scheduling Program and internet service (the \"Service\"), regardless of which domain name was used to access the service. Our current domain names include \"http://www.schedfox.com\", and \"http://www.schedsoft.com\".  By using the Service you are automatically agreeing to this Agreement, which can be changed by SchedFox at any time.<br><br>\n\nThe SchedFox Online Employee Scheduling Program is a customizable employee scheduling software solution that enables schedulers to efficiently create schedules, track overtime, manage conflicts, and report on employee schedules via the Internet anytime, anywhere.\n<br><br>\n\nFREE TRIAL <br><br>\n\nSchedFox offers a free trial period, which is covered by this Agreement. Simply sign up, and use the service for thirty days, free of charge.  If you discontinue the service within the first thirty days, your access will be terminated.  You will not be billed.  At the end of thirty days, you will not be billed unless you continue using the program.  If you discontinue the use of Schedfox you company will not be invoiced.   Schedfox only invoices active companies.  SchedFox is not responsible for contacting you to let you know your thirty-day free trial is over, however we may send you emails to alert you of the expiration date.  Companies are only eligible for one free trial. <br><br>\n\nCONTRACT TERMS<br><br>\n\nSchedFox only offers a monthly contract; no long term commitment is required.  Any time you are not satisfied or you no longer need the Service, simply stop using Schedfox or  terminate this Agreement as provided below.   <br><br> \n\nAfter your free trails for the Service, you agree to pay SchedFox a minimum of $25.00 per month for up to 25 employees.  <br><br>\n\nSchedFox invoices based on the average number of employees listed in the Service for the month.  There is no need to send SchedFox any notice of the increase or decrease in your employees.  As your business needs change, SchedFox changes with you. <br><br>\n\nThis website (excluding linked sites) is controlled by SchedFox from its offices in Richardson, Texas USA. By accessing this website and accepting this Agreement, you agree that all matters relating to your access to, or use of, this website and this Agreement is governed by the statutes and laws of the State of Texas USA, without regard to the conflicts of laws principles thereof. You also agree to submit to the exclusive personal jurisdiction and venue of the courts of Dallas, Dallas County, Texas USA. <br><br>\n\nRegardless of any statute or law to the contrary, any claim or cause of action arising out of or related to use of the website or this Agreement must be filed within one (1) year after such claim or cause of action arose or be forever barred. <br><br>\n\nThe section titles in the Agreement are for convenience only and have no legal or contractual effect. <br><br>\n\nCURRENT PRICES<br><br>\n\nBy signing up for the Service and continuing usage for more than 30 days, you agree to pay SchedFox the current prices for the Service as follows: <br><br>\n\n1 Month Term: There is a minimum cost of $25.00 for the one month term, which covers a Maximum Number of Employees of 25.  If you need more employees, the cost is an additional $.75 per employee for the one month term up to 75 employees.  From 75 to 150 employees, there is an additional $.50 per employee per month above the first 75.  After the first 150 employees, each additional Employee costs $0.25 for the one month term. <br><br>\n\nAll prices are subject to change without notice. SchedFox reserves the right to negotiate prices and terms upon written contract. Prices will not be pro-rated based on a smaller number of employees for any term. Regardless of any software error in the Price Calculator, or any accidentally misquoted price, these prices listed here in the Agreement shall stand as the official price of the service. <br><br>\n\nACCOUNTS AND PASSWORDS<br><br>\n\nYou will receive a company ID, a user ID, and a password to manage the schedule, with special manager rights.  Your Employees will have access to our web interface and will be able to view their schedule.  YOU ARE FULLY RESPONSIBLE FOR ANY AND ALL ACTIONS OF ANY MANAGERS AND/OR EMPLOYEES USING THEIR PASSWORDS AS WELL AS ANY ACTIVITY THAT OCCURS UNDER THOSE ACCOUNTS REGARDLESS OF WHO THE ACTUAL INDIVIDUAL IS WHO IS ACTING UNDER THOSE ACCOUNTS. <br><br>\n\nBILLING <br><br>\n\nSchedFox will send you an invoice for the amount of your Service fees for the first month after your trial period.  Thereafter, Schedfox will invoice on the 1st of each month.  You may choose to pay either by check or by credit card.  All invoices are net 10. <br><br>\n\nFull payment must be received within the first 10 days of invoice date.  If payment is not timely received, SchedFox may terminate your access to the Service without notification.  <br><br>\n\nTRADEMARKS<br><br>\n\nThe trademarks, logos, and service marks (\"Marks\") displayed on this website are the exclusive property of SchedFox or other third parties. You are not permitted to use the Marks without the prior written consent of SchedFox or such third party that may own the Mark. SchedFox and the SchedFox logo are trademarks of SchedFox, Inc. <br><br>\n\nAll right, title to, ownership of and all patent, copyright, trade secret, trademark and all other proprietary rights in the website and the Service remain in SchedFox. You may not remove any product identification, copyright notices, or other legends. You have no right in SchedFox or its third party licensors trademarks in connection with the Service, the software, or with its promotion or publication, without SchedFox's prior written approval. <br><br>\n\nTECHNICAL SUPPORT<br><br>\n\nSchedFox provides limited support via email.  SchedFox will attempt to respond to any request for support by email promptly, but makes no guarantee of any response time. <br><br>\n\nSchedFox does not offer telephone support at this time.  However, SchedFox may, at its sole discretion, offer a limited amount of phone support, in order to answer a quick question or handle a quick problem. <br><br>\n\nSchedFox does not provide consulting services, but has a list of approved consultants that can assist you on a basis. <br><br>\n \nCANCELLATION<br><br>\n\nYour service will be renewed each month as long as you actively continue to use Schedfox.  If your account is not active, then Schedfox will not bill you.   You may also cancel by emailing admin@schedfox.com with your company ID stating that you would like to cancel the Service.  You will be billed for the use during the billing period for the month of termination.  When you cancel your Service, SchedFox will close your account and you will no longer have access to it. <br><br>\n\nIf you do not pay for charges incurred by using the Service within 90 days of the due date, you will be responsible for all costs incurred by SchedFox to collect the money from you, including but not limited to attorney's fees, court costs, other legal costs, and postage. <br><br>\n\nEITHER PARTY MAY CANCEL THIS AGREEMENT AT ANY TIME FOR ANY REASON WITH NOTICE, EITHER IN WRITING OR ELECTRONIC. IF AT ANY TIME YOU ARE DISSATISFIED WITH THE SERVICE FOR ANY REASON, YOU CAN CANCEL. <br><br>\n\nACCURACY OF INFORMATION PROVIDED TO SCHEDFOX<br><br>\n\nYou agree to provide true, accurate, current and complete information about your business and maintain and promptly update the company information to keep it true, accurate, current and complete. <br><br>\n\nMEMBER CONDUCT<br><br>\n\nAs a condition of your use of the Service, you warrant to SchedFox that you will not use the Service for any purpose that is unlawful or prohibited by this Agreement and notices. Any resale of the Service is expressly prohibited. Any type of abuse, including but not limited to attempts to spy or get the passwords or private information of others, spamming, harassment, hacking, reverse engineering of any kind, excessive use with the intention of overworking or overloading the system in any way, or any other behavior which is considered an abuse by modern internet standards will be cause for and serve as reasonable grounds for immediate termination. <br><br>\n\nDISCLAIMERS/LIMITATION OF LIABILITY<br><br>\n\nThe information and services included in or available through the Service may include inaccuracies or typographical errors. Changes are periodically added to the information herein. SchedFox may make improvements and/or changes in the Service at any time. SchedFox does not represent or warrant that the Service will be uninterrupted or error-free, that defects will be corrected, or that the Service or the server that makes it available, are free of viruses or other harmful components. SchedFox does not warrant or represent that the use or the results of the use of the Service or the materials made available as part of the Service will be correct, accurate, timely, or otherwise reliable. You specifically agree that SchedFox shall not be responsible for unauthorized access to or alteration of your data. <br><br>\n\nSCHEDFOX MAKES NO REPRESENTATIONS ABOUT THE RELIABILITY, SUITABILITY, TIMELINESS, AVAILABILITY, AND ACCURACY OF THE SERVICE FOR ANY PURPOSE. THE SERVICE IS PROVIDED \"AS IS\" WITHOUT WARRANTY OF ANY KIND. SCHEDFOX HEREBY DISCLAIMS ALL WARRANTIES AND CONDITIONS WITH REGARD TO THE SERVICE, INCLUDING ALL IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE AND NON-INFRINGEMENT. <br><br>\n\nIN NO EVENT SHALL SCHEDFOX BE LIABLE FOR ANY INDIRECT, DIRECT, PUNITIVE, SPECIAL, INCIDENTAL, CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER INCLUDING, WITHOUT LIMITATION, DAMAGES FOR LOSS OF USE, DATA OR PROFITS, ARISING OUT OF OR IN ANY WAY CONNECTED WITH THE USE OR PERFORMANCE OF THE SERVICE, WITH THE DELAY OR INABILITY TO USE THE SERVICE, THE PROVISION OF OR FAILURE TO PROVIDE SERVICES, OR FOR ANY INFORMATION, SOFTWARE, PRODUCTS, SERVICES AND RELATED GRAPHICS OBTAINED THROUGH THE SERVICE, OR OTHERWISE ARISING OUT OF THE USE OF THE SERVICE, WHETHER BASED ON CONTRACT, NEGLIGENCE, TORT, STRICT LIABILITY OR OTHERWISE, EVEN IF SCHEDFOX HAS BEEN ADVISED OF THE POSSIBILITY OF DAMAGES. IF YOU ARE DISSATISFIED WITH ANY PORTION OF THE SERVICE, OR WITH ANY PART OF THIS AGREEMENT, THEN YOUR SOLE AND EXCLUSIVE REMEDY IS TO CANCEL (SEE \"TERMINATION\") AND DISCONTINUE USING THE SERVICE. <br><br>\n\nGENERAL PROVISIONS<br><br>\n\nThe invalidity or unenforceability of any provision of this Agreement shall not affect the validity or enforceability of any other provisions of this agreement, which shall remain in full force and effect. If any of the covenants or provisions of this Agreement are determined to be unenforceable by reason of their extent, duration, scope, or otherwise, then the parties contemplate that the court making such determination shall reduce such extent, duration, scope or other provision and enforce such term(s) in their reduced form for all purposes contemplated by this Agreement. <br><br>\n\nThis Agreement contain the entire agreement between SchedFox and your company with respect to usage of the Service, and supersedes, merges, and replaces all prior written or oral agreements, negotiations, offers, representations, and warranties with respect to the usage of the Service. No course of dealing between the parties, no usage of trade, and no parole or outside evidence of any nature shall be used to modify, interpret or supplement any provision of this Agreement. <br><br>\n\nYou agree to indemnify and hold SchedFox, its affiliates, officers and employees, harmless from any claim, demand, or damage, including reasonable attorneys' fees, asserted by any third party due to or arising out of your use of the Service. <br><br>\n\nSchedFox may change the terms of this Agreement at any time.  SchedFox will try to keep you advised of changes through revision updates.  When a revised version of the Agreement is posted on the website, you automatically agree to any and all of these changes. <br><br>\n\nUNSOLICTED IDEA SUBMISSION POLICY<br><br>\n\nSCHEDFOX OR ANY OF ITS EMPLOYEES DO NOT ACCEPT OR CONSIDER UNSOLICITED IDEAS, INCLUDING IDEAS FOR NEW PRODUCTS, TECHNOLOGIES OR PROCESSES.  PLEASE DO NOT SEND YOUR UNSOLICITED IDEAS TO SCHEDFOX.  SCHEDFOX MAKES NO ASSURANCES THAT YOUR IDEAS AND MATERIALS WILL BE TREATED AS CONFIDENTIAL OR PROPRIETARY. <br><br>\n"); // NOI18N
        jScrollPane1.setViewportView(jTextPane1);

        jcbAgree.setText("I agree to the terms and conditions"); // NOI18N
        jcbAgree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbAgreeActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel17Layout = new org.jdesktop.layout.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel17Layout.createSequentialGroup()
                .add(jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel12)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 146, Short.MAX_VALUE)
                        .add(jcbAgree))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel17Layout.createSequentialGroup()
                        .add(38, 38, 38)
                        .add(jLabel15)
                        .add(26, 26, 26)
                        .add(loginNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel12)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel15)
                    .add(loginNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(1, 1, 1)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jcbAgree)
                .addContainerGap())
        );

        companySetupPanel.add(jPanel17);

        tabbedPane.addTab("Complete Setup", companySetupPanel);

        getContentPane().add(tabbedPane);

        displayPanelContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 20, 1, 1));
        displayPanelContainer.setLayout(new java.awt.BorderLayout());

        displayPanel.setMaximumSize(new java.awt.Dimension(32767, 500));
        displayPanel.setPreferredSize(new java.awt.Dimension(5000, 6000));
        displayPanel.setLayout(new javax.swing.BoxLayout(displayPanel, javax.swing.BoxLayout.Y_AXIS));
        displayPanelContainer.add(displayPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(displayPanelContainer);

        controlPanel.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        controlPanel.setMinimumSize(new java.awt.Dimension(100, 25));
        controlPanel.setPreferredSize(new java.awt.Dimension(100, 30));
        controlPanel.setLayout(new java.awt.BorderLayout());

        cancelButton.setText("Previous"); // NOI18N
        cancelButton.setMaximumSize(new java.awt.Dimension(90, 24));
        cancelButton.setMinimumSize(new java.awt.Dimension(90, 24));
        cancelButton.setPreferredSize(new java.awt.Dimension(90, 24));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        cancelButton.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                cancelButtonAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        controlPanel.add(cancelButton, java.awt.BorderLayout.WEST);

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 30));

        jProgressBar1.setIndeterminate(true);
        jProgressBar1.setString("Please wait while the account is being created."); // NOI18N
        jProgressBar1.setStringPainted(true);

        org.jdesktop.layout.GroupLayout jPanel16Layout = new org.jdesktop.layout.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel16, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel16, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        controlPanel.add(jPanel3, java.awt.BorderLayout.CENTER);

        okButton.setText("Next"); // NOI18N
        okButton.setMaximumSize(new java.awt.Dimension(90, 24));
        okButton.setMinimumSize(new java.awt.Dimension(90, 24));
        okButton.setPreferredSize(new java.awt.Dimension(90, 24));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        controlPanel.add(okButton, java.awt.BorderLayout.EAST);

        getContentPane().add(controlPanel);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-686)/2, (screenSize.height-374)/2, 686, 374);
    }// </editor-fold>//GEN-END:initComponents
    private void emailTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_emailTextActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if (this.tabbedPane.getSelectedIndex() == 0) {
            this.dispose();
        } else {
            this.tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
        }
}//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (this.tabbedPane.getSelectedIndex() == 0) {
            if (validatePersonalForm()) {
                this.tabbedPane.setEnabledAt(1, true);
                this.tabbedPane.setSelectedIndex(1);
            } else {
                disableLaterTabs();
            }
        } else if (this.tabbedPane.getSelectedIndex() == 1) {
            if (validatePersonalForm() && validateBusinessInformation()) {
                this.tabbedPane.setEnabledAt(2, true);
                this.tabbedPane.setSelectedIndex(2);
            } else {
                disableLaterTabs();
            }
        } else {            
            if (validatePersonalForm() && validateBusinessInformation() && validateAgreement()) {
                jProgressBar1.setVisible(true);
                completeSignupProcess();
            }
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void addressLine1TextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressLine1TextActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_addressLine1TextActionPerformed

    private void addressLine2TextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressLine2TextActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_addressLine2TextActionPerformed

    private void cancelButtonAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_cancelButtonAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_cancelButtonAncestorAdded

    private void jcbAgreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbAgreeActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jcbAgreeActionPerformed

    private void tabbedPaneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabbedPaneFocusGained
        /* Nuts that I have to do this to make it go to the startin position
         * there may be a better way, if so we need to do it
         */
        this.jScrollPane1.getViewport().setViewPosition(new Point(0,0));
    }//GEN-LAST:event_tabbedPaneFocusGained

    private void resetForm() {
        Enumeration errorFieldIterator = this.hashOfOriginalBorders.keys();
        while (errorFieldIterator.hasMoreElements()) {
            JComponent component = (JComponent)errorFieldIterator.nextElement();
            Border border = this.hashOfOriginalBorders.get(component);
            component.setBorder(border);
        }
        currHeight = originalHeight;
        this.displayPanel.removeAll();
        this.validate();
        this.repaint();
    }
    
    private void addError(String error, JComponent highlightThis) {
        JLabel errorLabel = new JLabel("*  " + error);
        errorLabel.setMaximumSize(new Dimension(2000, 18));
        errorLabel.setPreferredSize(new Dimension(2000, 18));
        errorLabel.setForeground(Color.RED);
        this.displayPanel.add(errorLabel);
        currHeight += 18;
        if (this.hashOfOriginalBorders.get(highlightThis) == null) {
            this.hashOfOriginalBorders.put(highlightThis, highlightThis.getBorder());
        } 
        highlightThis.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 0, 0), 1, true));
    }

    private boolean isFieldNull(javax.swing.JTextField textField) {
        boolean retVal = false;
        if (textField.getText().trim().length() <= 0) {
            retVal = true;
        }
        return retVal;
    }

    private boolean isFieldNull(javax.swing.JPasswordField passwordField) {
        boolean retVal = false;
        if (new String(passwordField.getPassword()).trim().length() <= 0) {
            retVal = true;
        }
        return retVal;
    }

    private boolean doesUserNameExist(String userNameRequested) {
        boolean retVal = true;
        check_if_user_exists_query check_user_name = new check_if_user_exists_query();
        check_user_name.update(userNameRequested);
        try {
            this.myConnection.prepQuery(check_user_name);
            Record_Set rs = myConnection.executeQuery(check_user_name);
            if (rs.getInt("number_of_users") > 0) {
                retVal = false;
            }
        } catch (Exception e) {
            this.addError("Could not create your account, please contact customer service!", null);
        }
        return retVal;
    }

    private boolean validateAgreement() {
        if(jcbAgree.isSelected()) {
            return true;
        }
        addError("You must accept the user agreement.", jcbAgree);
        return false;
    }
    
    /**
     * Validate all of the information entered by our user...
     * @return true if the form is considered valid
     */
    private boolean validatePersonalForm() {
        boolean retVal = true;
        this.resetForm();
        if (isFieldNull(firstNameText)) {
            retVal = false;
            addError("First name can not be blank!", firstNameText);
        }
        if (isFieldNull(lastNameText)) {
            retVal = false;
            addError("Last name can not be blank!", lastNameText);
        }

        //Validate User Name / Email
        if (isFieldNull(emailText)) {
            retVal = false;
            addError("Email can not be blank!", emailText);
        } else if (isFieldNull(confirmEmailText)) {
            retVal = false;
            addError("You must confirm your email address!", confirmEmailText);
        } else {
            if (!emailText.getText().equals(confirmEmailText.getText())) {
                retVal = false;
                addError("Your email and the confirmation do not match!", confirmEmailText);
            } else if (!doesUserNameExist(confirmEmailText.getText())) {
                retVal = false;
                addError("This user name (" + confirmEmailText.getText() + ") already exists as a user.", emailText);
            }
        }

        //Validate Password
        if (isFieldNull(passwordText)) {
            retVal = false;
            addError("Password can not be blank!", passwordText);
        } else if (isFieldNull(confirmPasswordText)) {
            retVal = false;
            addError("You must confirm your password!", confirmPasswordText);
        } else if (!new String(passwordText.getPassword()).equals(new String(confirmPasswordText.getPassword()))) {
            retVal = false;
            addError("Your password and confirmation do not match!", confirmPasswordText);
        }

        if (retVal) {
            this.loginNameLabel.setText(this.emailText.getText());
        }
        
        this.setSize(new Dimension(this.getSize().width, currHeight));
        this.setPreferredSize(new Dimension(this.getSize().width, currHeight));
        this.setMinimumSize(new Dimension(this.getSize().width, currHeight));
        this.setMaximumSize(new Dimension(this.getSize().width, currHeight));
        this.validate();
        this.repaint();
        return retVal;
    }

    private boolean validateBusinessInformation() {
        boolean retVal = true;
        this.resetForm();
        if (isFieldNull(companyNameText)) {
            retVal = false;
            addError("Company name can not be blank!", companyNameText);
        }
        if (isFieldNull(addressLine1Text)) {
            retVal = false;
            addError("Address line 1 can not be blank!", addressLine1Text);
        }
        if (isFieldNull(cityText)) {
            retVal = false;
            addError("City can not be blank!", cityText);
        }
        if (isFieldNull(stateText)) {
            retVal = false;
            addError("State can not be blank!", stateText);
        }
        if (isFieldNull(zipText)) {
            retVal = false;
            addError("Zip code can not be blank!", zipText);
        }
        if (isFieldNull(companyPhoneText)) {
            retVal = false;
            addError("Phone number can not be blank!", companyPhoneText);
        }
        
        this.setSize(new Dimension(this.getSize().width, currHeight));
        this.setPreferredSize(new Dimension(this.getSize().width, currHeight));
        this.setMinimumSize(new Dimension(this.getSize().width, currHeight));
        this.setMaximumSize(new Dimension(this.getSize().width, currHeight));
        this.validate();
        this.repaint();
        return retVal;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField addressLine1Text;
    private javax.swing.JTextField addressLine2Text;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField cityText;
    private javax.swing.JPanel companyInfoPanel;
    private javax.swing.JTextField companyNameText;
    private javax.swing.JFormattedTextField companyPhoneText;
    private javax.swing.JPanel companySetupPanel;
    private javax.swing.JTextField confirmEmailText;
    private javax.swing.JPasswordField confirmPasswordText;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JPanel displayPanelContainer;
    private javax.swing.JTextField emailText;
    private javax.swing.JTextField firstNameText;
    private javax.swing.JPanel informationPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JCheckBox jcbAgree;
    private javax.swing.JTextField lastNameText;
    private javax.swing.JLabel loginNameLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JPasswordField passwordText;
    private javax.swing.JFormattedTextField stateText;
    private javax.swing.JPanel stepOnePanel;
    private javax.swing.JPanel stepTwoPanel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JFormattedTextField zipText;
    // End of variables declaration//GEN-END:variables
}
