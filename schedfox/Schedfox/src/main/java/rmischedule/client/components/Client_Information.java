/*
 * Client_Information.java
 *
 * Created on August 23, 2004, 1:50 PM
 */
package rmischedule.client.components;

import rmischeduleserver.util.StaticDateTimeFunctions;
import javax.swing.event.ChangeEvent;
import rmischedule.utility.TextField;
import rmischedule.components.*;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.security.*;
import rmischedule.main.*;
import java.util.Calendar;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.event.ChangeListener;
import rmischedule.client.*;
import rmischedule.components.Management_LB;
import rmischedule.components.other_functions;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import rmischedule.components.jcalendar.*;
import rmischedule.employee.components.DynamicFormPanel;
import rmischedule.employee.components.DynamicPanelParent;
import rmischedule.employee.xEmployeeEdit;
import rmischedule.messaging.email.SchedfoxEmail;
import rmischeduleserver.control.AddressController;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.ClientRatingController;
import rmischeduleserver.control.UserController;
import schedfoxlib.model.CheckOutOptions;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.ClientRating;
import schedfoxlib.model.EmailContact;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in.get_check_out_options_query;
import rmischeduleserver.mysqlconnectivity.queries.util.GenericQuery;

/**
 *
 * @author jason.allen
 */
public class Client_Information extends GenericEditSubForm implements DynamicPanelParent {

    private Management_LB managementLb;
    private Vector<JTextField> myComponents;
    private Rate_Code_LB myRCLB;
    private xClientEdit myParent;
    private String worksite;
    private JCalendarComboBox calStartDate;
    private JCalendarComboBox calEndDate;
    private JCheckBox markClientNonBillableChk;
    // to detect if combo box needs to be populated
    private boolean initialLoad = true;
    HashMap<String, Integer> storeInt = new HashMap<String, Integer>();
    HashMap<Integer, String> intStore = new HashMap<Integer, String>();
    private DynamicFormPanel dynamicForm;
    private RatingPanel ratingPanel;

    /**
     * Creates new form Client_Information
     */
    public Client_Information(xClientEdit parent) {
        initComponents();
        worksite = "0";
        myParent = parent;
        BrakPanel.setVisible(false);
        calStartDate = new JCalendarComboBox();
        calEndDate = new JCalendarComboBox();
        markClientNonBillableChk = new JCheckBox("Default new shifts to non-billable");
        StartDate.add(calStartDate);
        EndDate.add(calEndDate);
        if (!Main_Window.parentOfApplication.showRatesForClients()) {
            ClientRateCodePnl.setVisible(false);
        }
        dynamicForm = new DynamicFormPanel(myParent, this,
                1, true, false);
        additionalPanel.add(this.dynamicForm);

        ratingPanel = new RatingPanel(10, 24);
        ratingPanel.addChangeListener(new RatingChangeListener());
        ratingContainerPanel.add(ratingPanel);
    }

    public void doOnClear() {

        //If it is the first load, then populates the combo box by callinga  query and populating with the result set
        if (this.initialLoad) {
            initialLoad = false;
        }
        ratingPanel.clearRating();

        //Resets default state for the rest of the form
        try {
            myRCLB.setRateCode("0");
        } catch (Exception e) {
        }

        worksite = "0";
        this.activeCheckBox.setSelected(true);

        try {
            markClientNonBillableChk.setSelected(false);
        } catch (Exception e) {
        }

        callQueueChk.setSelected(true);
        
        calStartDate.setCalendar(Calendar.getInstance());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 20);
        calEndDate.setCalendar(cal);

        dynamicForm.clear();
    }

    public javax.swing.JPanel getMyForm() {
        return this;
    }

    public java.lang.String getMyTabTitle() {
        return "General";
    }

    /**
     * This notifies our sales people that an account was deleted.
     */
    private void notifySalesPeopleOfAccountDeletion() {
        UserController userController = new UserController(myParent.getConnection().myCompany);
        ArrayList<EmailContact> sales = null;
        try {
            sales = new ArrayList<EmailContact>();
            sales.addAll(userController.getUsersByTypes("Sales", "-1"));
        } catch (Exception e) {
            sales = new ArrayList<EmailContact>();
        }
        if (sales.size() > 0) {
            String subject = "Move account to leads";
            Client client = (Client) myParent.getSelectedObject();
            StringBuilder body = new StringBuilder();
            try {
                body.append(Main_Window.parentOfApplication.myUser.getFirstName() + " " + Main_Window.parentOfApplication.myUser.getLastName());
                body.append(" has marked this account as inactive.<br/>");
            } catch (Exception e) {
            }
            body.append("This is no longer a current account with the company.  Please put this back on the lead list <br/><br/>");
            body.append(efName.getText().trim() + "<br/>");
            body.append(efPhone.getText().trim() + "<br/>");
            body.append(efPhone_2.getText().trim() + "<br/>");
            body.append(efFax.getText().trim() + "<br/>");
            body.append(efAddress.getText().trim() + "<br/>");
            body.append(efAddress_2.getText().trim() + "<br/>");
            body.append(efCity.getText().trim() + "<br/>");
            body.append(efState.getText().trim() + "<br/>");
            body.append(efZip.getText().trim() + "<br/><br/>");
            body.append("Additional Contact Info<br/>");
            ArrayList<ClientContact> contacts = client.getContacts(myParent.getConnection().myCompany);

            for (int c = 0; c < contacts.size(); c++) {
                body.append("Name: " + contacts.get(c).getFullName() + "<br/>");
                body.append("Phone: " + contacts.get(c).getClientContactPhone() + "<br/>");
                body.append("Address: " + contacts.get(c).getClientContactAddress() + "<br/>");
                body.append("City: " + contacts.get(c).getClientContactCity() + "<br/>");
                body.append("State: " + contacts.get(c).getClientContactState() + "<br/>");
                body.append("Zip: " + contacts.get(c).getClientContactZip() + "<br/>");
                body.append("Email: " + contacts.get(c).getClientContactEmail() + "<br/><br/>");
            }

            String[] contactArray = new String[sales.size()];
            for (int vc = 0; vc < sales.size(); vc++) {
                contactArray[vc] = sales.get(vc).getEmailAddress();
            }
            try {
                new SchedfoxEmail(subject, body.toString(), contactArray);
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Notified sales of account deletion.",
                        "Sent CC", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Could not send email notifying sales of account deletion!");
            }
        }
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getSaveQuery(boolean isNewData, boolean isdeleted) {
        Client currentClient = (Client) myparent.getSelectedObject();
        if (currentClient == null) {
            currentClient = new Client(new Date());
        }

        if (efName.getText().trim().length() == 0) {
            return null;
        }

        String endDate = "";
        String startDate = "";
        try {
            endDate = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(calEndDate.getCalendar());
        } catch (Exception e) {
        }

        startDate = "";
        try {
            startDate = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(calStartDate.getCalendar());
        } catch (Exception e) {
        }

        if (startDate.equals("2100-10-10") || startDate.equals("1000-10-10")) {
            startDate = "";
        }
        if (endDate.equals("2100-10-10") || endDate.equals("1000-10-10")) {
            endDate = "";
        }

        int rate = 0;
        try {
            rate = Integer.parseInt(myRCLB.getSelectedRateCodeId());
        } catch (Exception e) {
        }

        int manageId = 0;
        if (managementLb != null) {
            manageId = Integer.parseInt(managementLb.getSelectedmanagementId());
        }

        currentClient.setClientName(efName.getText().trim());
        try {
            currentClient.setBranchId(Integer.parseInt(((xClientEdit) myparent).getBranchFromChild()));
        } catch (Exception e) {
            currentClient.setBranchId(Integer.parseInt(myparent.getConnection().myBranch));
        }
        currentClient.setClientPhone(efPhone.getText().trim());
        currentClient.setClientPhone2(efPhone_2.getText().trim());
        currentClient.setClientFax(efFax.getText().trim());
        currentClient.setClientAddress(efAddress.getText().trim());
        currentClient.setClientAddress2(efAddress_2.getText().trim());
        currentClient.setClientCity(efCity.getText().trim());
        currentClient.setClientState(efState.getText().trim());
        currentClient.setClientZip(efZip.getText().trim());
        currentClient.setClientStartDate(calStartDate.getCalendar().getTime());
        currentClient.setClientEndDate(calEndDate.getCalendar().getTime());
        currentClient.setDisplayClientInCallQueue(callQueueChk.isSelected());
        try {
            currentClient.setCheckOutOptionId(((CheckOutOptions) checkoutCombo.getSelectedItem()).getCheckOutOptionId());
        } catch (Exception e) {
        }
        currentClient.setManagementId(manageId);
        currentClient.setRateCodeId(rate);
        currentClient.setClientIsDeletedShort(activeCheckBox.isSelected() ? (short) 0 : (short) 1);
        currentClient.setClientWorksite(Integer.parseInt(this.worksite));
        currentClient.setDefaultNonBillable(markClientNonBillableChk.isSelected());
        currentClient.setCheckinFromPostPhone(postPhoneChk.isSelected());

        if (currentClient.getClientIsDeleted() == 1) {
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            endDate = myFormat.format(calEndDate.getCalendar().getTime());

            try {
                int clientId = currentClient.getClientId();
                refresh_schedules_for_client_query refreshQuery = new refresh_schedules_for_client_query();
                refreshQuery.setPreparedStatement(new Object[]{clientId, clientId});
                this.myParent.getConnection().executeQuery(refreshQuery);
            } catch (Exception e) {
            }
        }
        ClientController clientController = ClientController.getInstance(myParent.getConnection().myCompany);
        try {
            clientController.saveClient(currentClient);
            myparent.setCurrentSelectedObjectDirectly(currentClient);
        } catch (Exception e) {
            return null;
        }

        try {
            AddressController addressController = AddressController.getInstance(myParent.getConnection().myCompany);
            boolean exists = addressController.checkIfAddressLatLongExists(currentClient.getAddressObj());
            if (!exists) {
                try {
                    addressController.fetchLatLong(currentClient.getAddressObj());
                    addressController.persistAddressLatLong(currentClient.getAddressObj());
                } catch (Exception exe) {
                    exe.printStackTrace();
                }
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }

        dynamicForm.save();

        return new GenericQuery("Select NOW();");
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getSaveQuery(boolean isNewData) {
        return getSaveQuery(isNewData, false);
    }

    private void loadLastClientRating() {
        try {
            ClientRatingController ratingController = ClientRatingController.getInstance(this.myParent.getConnection().myCompany);
            ClientRating clientRating = ratingController.getLastClientRating(Integer.parseInt(myparent.getMyIdForSave()));
            if (clientRating.getCustomer_rating() > 0) {
                ratingPanel.setRating(clientRating.getCustomer_rating());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadClientStats() {

        try {
            Runnable statsRunnable = new Runnable() {

                public void run() {
                    get_client_stats_query statsQuery = new get_client_stats_query();
                    int id = Integer.parseInt(myparent.getMyIdForSave());
                    statsQuery.setPreparedStatement(new Object[]{"{" + id + "}", id});

                    myparent.getConnection().prepQuery(statsQuery);
                    Record_Set rst = myparent.getConnection().executeQuery(statsQuery);
                    try {
                        final int average = Integer.parseInt(rst.getString("average")) / 60;
                        Runnable setTextRunnable = new Runnable() {

                            public void run() {
                                hoursSchedLabel.setText(average + " Average Hours Scheduled");
                            }
                        };
                        try {
                            SwingUtilities.invokeAndWait(setTextRunnable);
                        } catch (Exception e) {
                        }

                    } catch (Exception exe) {
                        hoursSchedLabel.setText("0 Average Hours Scheduled");
                    }
                }
            };
            new Thread(statsRunnable).start();
        } catch (Exception e) {
            hoursSchedLabel.setText("0 Average Hours Scheduled");
        }
    }

    private void loadCheckoutOptions(Record_Set rs) {
        get_check_out_options_query checkOutQuery = new get_check_out_options_query();
        checkOutQuery.setPreparedStatement(new Object[]{});
        myparent.getConnection().prepQuery(checkOutQuery);
        Record_Set rst = myparent.getConnection().executeQuery(checkOutQuery);
        checkoutCombo.removeAllItems();
        for (int r = 0; r < rst.length(); r++) {
            CheckOutOptions option = new CheckOutOptions(rst);
            checkoutCombo.addItem(option);
            rst.moveNext();
        }

        CheckOutOptions selectedOption = new CheckOutOptions();
        selectedOption.setCheckOutOptionId(rs.getInt("check_out_option_id"));
        checkoutCombo.setSelectedItem(selectedOption);
    }

    public void loadData(Record_Set rs) {
        loadCheckoutOptions(rs);
        loadClientStats();
        loadLastClientRating();
        dynamicForm.loadData();
        String id = myparent.getMyIdForSave();
        myRCLB = new Rate_Code_LB(myParent.cpny);
        ClientRateCodePnl.removeAll();
        ClientRateCodePnl.add(myRCLB);
        ClientRateCodePnl.add(markClientNonBillableChk);
        managementLb = new Management_LB(myparent.getConnection());
        efName.setText(rs.getString("name"));

        try {
            calStartDate.setCalendar(StaticDateTimeFunctions.setCalendarToString(rs.getString("client_start_date")));
        } catch (Exception e) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -1);
            calStartDate.setCalendar(cal);
        }

        try {
            calEndDate.setCalendar(StaticDateTimeFunctions.setCalendarToString(rs.getString("client_end_date")));
        } catch (Exception e) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 20);
            calEndDate.setCalendar(cal);
        }

        efAddress.setText(rs.getString("address"));
        efAddress_2.setText(rs.getString("address2"));
        worksite = rs.getString("ws");
        efCity.setText(rs.getString("city"));
        efState.setText(rs.getString("state"));
        efZip.setText(rs.getString("zip"));
        efPhone.setText(rs.getString("phone"));
        efPhone_2.setText(rs.getString("phone2"));
        efFax.setText(rs.getString("fax"));
        try {
            callQueueChk.setSelected(rs.getBoolean("display_client_in_call_queue"));
        } catch (Exception exe) {}
        postPhoneChk.setSelected(rs.getBoolean("checkin_from_post_phone"));

        if (rs.getString("deleted").equals("false")) {
            this.activeCheckBox.setSelected(true);
            jpEndDate.setVisible(false);
        } else {
            this.activeCheckBox.setSelected(false);
            jpEndDate.setVisible(true);
        }

        managementLb.setNoteType(rs.getString("management"));
        try {
            myRCLB.setRateCode(rs.getString("rate_code_id"));
        } catch (Exception e) {
        }

        try {
            markClientNonBillableChk.setSelected(rs.getBoolean("default_non_billable"));
        } catch (Exception e) {
            System.out.println("Could not read mark non billable column from database.");
        }
    }

    public boolean needsMoreRecordSets() {
        return false;
    }

    public boolean userHasAccess() {
        return Main_Window.mySecurity.checkSecurity(security_detail.MODULES.CLIENT_INFORMATION);
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getQuery(boolean val) {
        client_info_query myInfoQuery = new client_info_query();
        myInfoQuery.update(myparent.getMyIdForSave());
        myparent.getConnection().prepQuery(myInfoQuery);
        return myInfoQuery;
    }

    public JPanel getPanelForFileDynamicForms() {
        return null;
    }

    private class RatingChangeListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            Client client = (Client) myParent.getSelectedObject();

            ClientRating clientRating = new ClientRating();
            clientRating.setClientId(client.getClientId());
            clientRating.setCustomer_rating(ratingPanel.getRating());
            clientRating.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));

            ClientRatingController controller = ClientRatingController.getInstance(myParent.getConnection().myCompany);
            try {
                controller.saveRating(clientRating);
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                        "Rating is saved!", "Saved", JOptionPane.OK_OPTION);
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BrakPanel = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        MainDisplayPanel = new javax.swing.JPanel();
        ContactInfoPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        NamePanel = new javax.swing.JPanel();
        lbName = new javax.swing.JLabel();
        efName = new javax.swing.JTextField();
        activeCheckBox = new javax.swing.JCheckBox();
        hoursSchedLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        AddressPanel = new javax.swing.JPanel();
        Address1 = new javax.swing.JPanel();
        lbAddress = new javax.swing.JLabel();
        efAddress = new javax.swing.JTextField();
        Address2 = new javax.swing.JPanel();
        lbAddress1 = new javax.swing.JLabel();
        efAddress_2 = new javax.swing.JTextField();
        CityStateZip = new javax.swing.JPanel();
        lbCity = new javax.swing.JLabel();
        efCity = new javax.swing.JTextField();
        lbState = new javax.swing.JLabel();
        efState = TextField.getStateTextField();
        lbZip = new javax.swing.JLabel();
        try {
            javax.swing.text.MaskFormatter myZipFormatter = new javax.swing.text.MaskFormatter("*****"); myZipFormatter.setValidCharacters("1234567890");myZipFormatter.setPlaceholderCharacter('_');
            efZip = TextField.getZipTextField();
        } catch (Exception e) {}
        SpacerPanel1 = new javax.swing.JPanel();
        PhonePanel = new javax.swing.JPanel();
        Phone1 = new javax.swing.JPanel();
        lbPhone = new javax.swing.JLabel();
        efPhone = TextField.getPhoneTextField() ;
        Phone2 = new javax.swing.JPanel();
        lbPhone1 = new javax.swing.JLabel();
        efPhone_2 = TextField.getPhoneTextField() ;
        Fax = new javax.swing.JPanel();
        lbFax = new javax.swing.JLabel();
        efFax = TextField.getPhoneTextField() ;
        DateTrainingPanel = new javax.swing.JPanel();
        TermActiveDatePanel = new javax.swing.JPanel();
        checkinPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        checkoutCombo = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        postPhoneChk = new javax.swing.JCheckBox();
        additionalPanel = new javax.swing.JPanel();
        pnRateCode = new javax.swing.JPanel();
        ClientRateCodePnl = new javax.swing.JPanel();
        DatePanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jpStartDate = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lbStartDate = new javax.swing.JLabel();
        StartDate = new javax.swing.JPanel();
        jpEndDate = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lbEndDate = new javax.swing.JLabel();
        EndDate = new javax.swing.JPanel();
        ratingContainerPanelParent = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ratingContainerPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        callQueueChk = new javax.swing.JCheckBox();
        requiredPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        ControlPanel = new javax.swing.JPanel();

        BrakPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Breaks"));
        BrakPanel.setLayout(new javax.swing.BoxLayout(BrakPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.LINE_AXIS));
        BrakPanel.add(jPanel10);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 5));

        jLabel2.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        jLabel2.setText("Client requires ");
        jPanel7.add(jLabel2);

        jTextField1.setMaximumSize(new java.awt.Dimension(30, 22));
        jTextField1.setMinimumSize(new java.awt.Dimension(30, 22));
        jTextField1.setPreferredSize(new java.awt.Dimension(30, 22));
        jPanel7.add(jTextField1);

        jLabel3.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        jLabel3.setText("min break every");
        jPanel7.add(jLabel3);

        jComboBox1.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" }));
        jComboBox1.setMaximumSize(new java.awt.Dimension(50, 22));
        jComboBox1.setMinimumSize(new java.awt.Dimension(34, 22));
        jComboBox1.setPreferredSize(new java.awt.Dimension(38, 22));
        jPanel7.add(jComboBox1);

        jLabel4.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        jLabel4.setText("hours");
        jPanel7.add(jLabel4);

        BrakPanel.add(jPanel7);

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        jCheckBox1.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        jCheckBox1.setText("Unpaid Break");
        jPanel11.add(jCheckBox1);

        jCheckBox2.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        jCheckBox2.setText("Unbilled Break");
        jPanel11.add(jCheckBox2);
        jPanel11.add(jPanel12);

        BrakPanel.add(jPanel11);

        setBackground(new java.awt.Color(186, 186, 222));
        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3), javax.swing.BorderFactory.createEtchedBorder()));
        setMinimumSize(new java.awt.Dimension(300, 311));
        setPreferredSize(new java.awt.Dimension(306, 310));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        MainDisplayPanel.setLayout(new javax.swing.BoxLayout(MainDisplayPanel, javax.swing.BoxLayout.Y_AXIS));

        ContactInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "General Information"));
        ContactInfoPanel.setLayout(new javax.swing.BoxLayout(ContactInfoPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        NamePanel.setMaximumSize(new java.awt.Dimension(32767, 27));
        NamePanel.setMinimumSize(new java.awt.Dimension(10, 27));
        NamePanel.setPreferredSize(new java.awt.Dimension(10, 27));
        NamePanel.setLayout(new javax.swing.BoxLayout(NamePanel, javax.swing.BoxLayout.LINE_AXIS));

        lbName.setText("Location Name:");
        lbName.setMaximumSize(new java.awt.Dimension(90, 14));
        lbName.setMinimumSize(new java.awt.Dimension(90, 14));
        lbName.setPreferredSize(new java.awt.Dimension(105, 14));
        NamePanel.add(lbName);

        efName.setBackground(new java.awt.Color(255, 255, 204));
        efName.setMaximumSize(new java.awt.Dimension(2147483647, 23));
        efName.setPreferredSize(new java.awt.Dimension(6, 27));
        efName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                efNameFocusLost(evt);
            }
        });
        efName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efNameKeyTyped(evt);
            }
        });
        NamePanel.add(efName);

        activeCheckBox.setSelected(true);
        activeCheckBox.setText("Active");
        activeCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8));
        activeCheckBox.setFocusPainted(false);
        activeCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        activeCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activeCheckBoxActionPerformed(evt);
            }
        });
        NamePanel.add(activeCheckBox);

        hoursSchedLabel.setText("Average Hours Scheduled");
        NamePanel.add(hoursSchedLabel);

        jPanel3.add(NamePanel);

        ContactInfoPanel.add(jPanel3);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        AddressPanel.setLayout(new javax.swing.BoxLayout(AddressPanel, javax.swing.BoxLayout.Y_AXIS));

        Address1.setMaximumSize(new java.awt.Dimension(32767, 23));
        Address1.setMinimumSize(new java.awt.Dimension(67, 23));
        Address1.setPreferredSize(new java.awt.Dimension(67, 23));
        Address1.setLayout(new javax.swing.BoxLayout(Address1, javax.swing.BoxLayout.LINE_AXIS));

        lbAddress.setText("Street 1");
        lbAddress.setMaximumSize(new java.awt.Dimension(90, 14));
        lbAddress.setMinimumSize(new java.awt.Dimension(90, 14));
        lbAddress.setPreferredSize(new java.awt.Dimension(105, 14));
        Address1.add(lbAddress);

        efAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efAddressKeyTyped(evt);
            }
        });
        Address1.add(efAddress);

        AddressPanel.add(Address1);

        Address2.setMaximumSize(new java.awt.Dimension(32767, 23));
        Address2.setMinimumSize(new java.awt.Dimension(21, 23));
        Address2.setPreferredSize(new java.awt.Dimension(21, 23));
        Address2.setLayout(new javax.swing.BoxLayout(Address2, javax.swing.BoxLayout.LINE_AXIS));

        lbAddress1.setText("Street 2");
        lbAddress1.setMaximumSize(new java.awt.Dimension(90, 14));
        lbAddress1.setMinimumSize(new java.awt.Dimension(90, 14));
        lbAddress1.setPreferredSize(new java.awt.Dimension(105, 14));
        Address2.add(lbAddress1);

        efAddress_2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efAddress_2KeyTyped(evt);
            }
        });
        Address2.add(efAddress_2);

        AddressPanel.add(Address2);

        CityStateZip.setMaximumSize(new java.awt.Dimension(32767, 23));
        CityStateZip.setMinimumSize(new java.awt.Dimension(134, 23));
        CityStateZip.setPreferredSize(new java.awt.Dimension(134, 23));
        CityStateZip.setLayout(new javax.swing.BoxLayout(CityStateZip, javax.swing.BoxLayout.LINE_AXIS));

        lbCity.setText("City:");
        lbCity.setMaximumSize(new java.awt.Dimension(90, 14));
        lbCity.setMinimumSize(new java.awt.Dimension(90, 14));
        lbCity.setPreferredSize(new java.awt.Dimension(105, 14));
        CityStateZip.add(lbCity);

        efCity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efCityKeyTyped(evt);
            }
        });
        CityStateZip.add(efCity);

        lbState.setText("State:");
        CityStateZip.add(lbState);

        efState.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efStateKeyTyped(evt);
            }
        });
        CityStateZip.add(efState);

        lbZip.setText("Zip:");
        CityStateZip.add(lbZip);

        efZip.setMaximumSize(new java.awt.Dimension(2147483647, 52));
        efZip.setMinimumSize(new java.awt.Dimension(6, 52));
        efZip.setNextFocusableComponent(efPhone);
        efZip.setPreferredSize(new java.awt.Dimension(6, 52));
        CityStateZip.add(efZip);

        AddressPanel.add(CityStateZip);

        jPanel4.add(AddressPanel);

        SpacerPanel1.setMaximumSize(new java.awt.Dimension(50, 10));
        SpacerPanel1.setMinimumSize(new java.awt.Dimension(20, 10));
        SpacerPanel1.setPreferredSize(new java.awt.Dimension(20, 10));
        jPanel4.add(SpacerPanel1);

        PhonePanel.setMaximumSize(new java.awt.Dimension(150, 90));
        PhonePanel.setMinimumSize(new java.awt.Dimension(150, 57));
        PhonePanel.setPreferredSize(new java.awt.Dimension(150, 57));
        PhonePanel.setLayout(new javax.swing.BoxLayout(PhonePanel, javax.swing.BoxLayout.Y_AXIS));

        Phone1.setMaximumSize(new java.awt.Dimension(2147483647, 23));
        Phone1.setMinimumSize(new java.awt.Dimension(56, 23));
        Phone1.setPreferredSize(new java.awt.Dimension(56, 23));
        Phone1.setLayout(new javax.swing.BoxLayout(Phone1, javax.swing.BoxLayout.LINE_AXIS));

        lbPhone.setText("Phone 1");
        lbPhone.setMaximumSize(new java.awt.Dimension(50, 14));
        lbPhone.setMinimumSize(new java.awt.Dimension(50, 14));
        lbPhone.setPreferredSize(new java.awt.Dimension(50, 14));
        Phone1.add(lbPhone);

        efPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                efPhoneActionPerformed(evt);
            }
        });
        efPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efPhoneKeyTyped(evt);
            }
        });
        Phone1.add(efPhone);

        PhonePanel.add(Phone1);

        Phone2.setMaximumSize(new java.awt.Dimension(2147483647, 23));
        Phone2.setMinimumSize(new java.awt.Dimension(56, 23));
        Phone2.setPreferredSize(new java.awt.Dimension(56, 23));
        Phone2.setLayout(new javax.swing.BoxLayout(Phone2, javax.swing.BoxLayout.LINE_AXIS));

        lbPhone1.setText("Phone 2");
        lbPhone1.setMaximumSize(new java.awt.Dimension(50, 14));
        lbPhone1.setMinimumSize(new java.awt.Dimension(50, 14));
        lbPhone1.setPreferredSize(new java.awt.Dimension(50, 14));
        Phone2.add(lbPhone1);

        efPhone_2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efPhone_2KeyTyped(evt);
            }
        });
        Phone2.add(efPhone_2);

        PhonePanel.add(Phone2);

        Fax.setMaximumSize(new java.awt.Dimension(2147483647, 23));
        Fax.setMinimumSize(new java.awt.Dimension(56, 23));
        Fax.setPreferredSize(new java.awt.Dimension(56, 23));
        Fax.setLayout(new javax.swing.BoxLayout(Fax, javax.swing.BoxLayout.LINE_AXIS));

        lbFax.setText("Fax");
        lbFax.setMaximumSize(new java.awt.Dimension(50, 14));
        lbFax.setMinimumSize(new java.awt.Dimension(50, 14));
        lbFax.setPreferredSize(new java.awt.Dimension(50, 14));
        Fax.add(lbFax);

        efFax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                efFaxKeyTyped(evt);
            }
        });
        Fax.add(efFax);

        PhonePanel.add(Fax);

        jPanel4.add(PhonePanel);

        ContactInfoPanel.add(jPanel4);

        MainDisplayPanel.add(ContactInfoPanel);

        DateTrainingPanel.setMinimumSize(new java.awt.Dimension(370, 70));
        DateTrainingPanel.setPreferredSize(new java.awt.Dimension(312, 70));
        DateTrainingPanel.setLayout(new java.awt.GridLayout(1, 0));

        TermActiveDatePanel.setLayout(new java.awt.GridLayout(0, 1));

        checkinPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Checkin / Checkout Settings"));
        checkinPanel.setLayout(new java.awt.GridLayout(2, 1));

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setText("Checkout When");
        jLabel5.setMaximumSize(new java.awt.Dimension(10000, 16));
        jPanel8.add(jLabel5);

        jPanel8.add(checkoutCombo);

        checkinPanel.add(jPanel8);

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.LINE_AXIS));

        postPhoneChk.setText("Employees must checkin from post phone");
        jPanel9.add(postPhoneChk);

        checkinPanel.add(jPanel9);

        TermActiveDatePanel.add(checkinPanel);

        additionalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Additional Data"));
        additionalPanel.setLayout(new java.awt.GridLayout(1, 0));
        TermActiveDatePanel.add(additionalPanel);

        DateTrainingPanel.add(TermActiveDatePanel);

        pnRateCode.setMaximumSize(new java.awt.Dimension(260, 32827));
        pnRateCode.setMinimumSize(new java.awt.Dimension(260, 0));
        pnRateCode.setPreferredSize(new java.awt.Dimension(260, 60));
        pnRateCode.setLayout(new javax.swing.BoxLayout(pnRateCode, javax.swing.BoxLayout.Y_AXIS));

        ClientRateCodePnl.setBorder(javax.swing.BorderFactory.createTitledBorder("Default Rate Code"));
        ClientRateCodePnl.setMaximumSize(new java.awt.Dimension(32767, 100));
        ClientRateCodePnl.setMinimumSize(new java.awt.Dimension(12, 100));
        ClientRateCodePnl.setPreferredSize(new java.awt.Dimension(12, 100));
        ClientRateCodePnl.setLayout(new java.awt.GridLayout(0, 1));
        pnRateCode.add(ClientRateCodePnl);

        DatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Active Dates", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(51, 51, 51)));
        DatePanel.setLayout(new javax.swing.BoxLayout(DatePanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        jpStartDate.setLayout(new javax.swing.BoxLayout(jpStartDate, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel1.setMinimumSize(new java.awt.Dimension(35, 14));
        jPanel1.setPreferredSize(new java.awt.Dimension(35, 14));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        lbStartDate.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        lbStartDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbStartDate.setText("Start:");
        lbStartDate.setMaximumSize(new java.awt.Dimension(2500, 14));
        lbStartDate.setRequestFocusEnabled(false);
        jPanel1.add(lbStartDate);

        jpStartDate.add(jPanel1);

        StartDate.setMaximumSize(new java.awt.Dimension(32767, 20));
        StartDate.setMinimumSize(new java.awt.Dimension(10, 20));
        StartDate.setPreferredSize(new java.awt.Dimension(10, 20));
        StartDate.setLayout(new java.awt.GridLayout(1, 0));
        jpStartDate.add(StartDate);

        jPanel6.add(jpStartDate);

        jpEndDate.setLayout(new javax.swing.BoxLayout(jpEndDate, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 14));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        lbEndDate.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 10)); // NOI18N
        lbEndDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbEndDate.setText("End:");
        lbEndDate.setMaximumSize(new java.awt.Dimension(2200, 14));
        jPanel2.add(lbEndDate);

        jpEndDate.add(jPanel2);

        EndDate.setMaximumSize(new java.awt.Dimension(32767, 20));
        EndDate.setMinimumSize(new java.awt.Dimension(10, 20));
        EndDate.setPreferredSize(new java.awt.Dimension(10, 20));
        EndDate.setLayout(new java.awt.GridLayout(1, 0));
        jpEndDate.add(EndDate);

        jPanel6.add(jpEndDate);

        DatePanel.add(jPanel6);

        pnRateCode.add(DatePanel);

        DateTrainingPanel.add(pnRateCode);

        MainDisplayPanel.add(DateTrainingPanel);

        add(MainDisplayPanel);

        ratingContainerPanelParent.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 3, 1));
        ratingContainerPanelParent.setMaximumSize(new java.awt.Dimension(32767, 30));
        ratingContainerPanelParent.setMinimumSize(new java.awt.Dimension(10, 30));
        ratingContainerPanelParent.setPreferredSize(new java.awt.Dimension(100, 30));
        ratingContainerPanelParent.setLayout(new javax.swing.BoxLayout(ratingContainerPanelParent, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Client Rating");
        jLabel1.setMaximumSize(new java.awt.Dimension(90, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(90, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(90, 16));
        ratingContainerPanelParent.add(jLabel1);

        ratingContainerPanel.setLayout(new java.awt.GridLayout(1, 0));
        ratingContainerPanelParent.add(ratingContainerPanel);

        jPanel5.setMaximumSize(new java.awt.Dimension(300, 32767));
        jPanel5.setMinimumSize(new java.awt.Dimension(300, 10));
        jPanel5.setPreferredSize(new java.awt.Dimension(300, 100));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 2));

        callQueueChk.setText("Show client in call queue");
        callQueueChk.setMaximumSize(new java.awt.Dimension(600, 25));
        jPanel5.add(callQueueChk);

        ratingContainerPanelParent.add(jPanel5);

        add(ratingContainerPanelParent);

        requiredPanel.setMaximumSize(new java.awt.Dimension(32767, 14));
        requiredPanel.setMinimumSize(new java.awt.Dimension(152, 14));
        requiredPanel.setPreferredSize(new java.awt.Dimension(152, 14));
        requiredPanel.setLayout(new java.awt.GridLayout(1, 0));

        jLabel7.setBackground(new java.awt.Color(255, 255, 204));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Highlighted fields are required.");
        jLabel7.setOpaque(true);
        requiredPanel.add(jLabel7);

        add(requiredPanel);

        ControlPanel.setMaximumSize(new java.awt.Dimension(32767, 23));
        ControlPanel.setLayout(new java.awt.GridLayout(1, 0));
        add(ControlPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void activeCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activeCheckBoxActionPerformed
        if (!this.activeCheckBox.isSelected()) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to deactivate this location?", "Deactivate Location?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                ClientTerminationConfirmation clientTermination =
                        new ClientTerminationConfirmation(Main_Window.parentOfApplication, 
                                true, this.myParent.getConnection().myCompany, 
                                ((Client)this.myParent.getSelectedObject()).getClientId());
                clientTermination.setVisible(true);
                if (clientTermination.getTerminationDate() != null) {
                    calEndDate.setCalendar(clientTermination.getTerminationDate());
                    notifySalesPeopleOfAccountDeletion();
                    this.myparent.saveData();
                }
            } else {
                this.activeCheckBox.setSelected(true);
            }
        } else {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to activate this location?", "Activate Location?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.myparent.saveData();
            } else {
                this.activeCheckBox.setSelected(false);
            }
        }
    }//GEN-LAST:event_activeCheckBoxActionPerformed

    private void efStateKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efStateKeyTyped
        other_functions.maxlength(efState, 2);
    }//GEN-LAST:event_efStateKeyTyped

    private void efCityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efCityKeyTyped
        other_functions.maxlength(efAddress, 50);
    }//GEN-LAST:event_efCityKeyTyped

    private void efFaxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efFaxKeyTyped
        other_functions.maxlength(efFax, 20);
    }//GEN-LAST:event_efFaxKeyTyped

    private void efPhone_2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efPhone_2KeyTyped
        other_functions.maxlength(efPhone, 20);
    }//GEN-LAST:event_efPhone_2KeyTyped

    private void efPhoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efPhoneKeyTyped
        other_functions.maxlength(efPhone, 20);
    }//GEN-LAST:event_efPhoneKeyTyped

    private void efAddress_2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efAddress_2KeyTyped
        other_functions.maxlength(efAddress_2, 50);
    }//GEN-LAST:event_efAddress_2KeyTyped

    private void efAddressKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efAddressKeyTyped
        other_functions.maxlength(efAddress, 50);
    }//GEN-LAST:event_efAddressKeyTyped

    private void efNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_efNameKeyTyped
        other_functions.maxlength(efName, 100);
    }//GEN-LAST:event_efNameKeyTyped

    private void efPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_efPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_efPhoneActionPerformed

    private void efNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_efNameFocusLost
        myParent.getIdForm().setLoginName(efName.getText());
    }//GEN-LAST:event_efNameFocusLost
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Address1;
    private javax.swing.JPanel Address2;
    private javax.swing.JPanel AddressPanel;
    private javax.swing.JPanel BrakPanel;
    private javax.swing.JPanel CityStateZip;
    private javax.swing.JPanel ClientRateCodePnl;
    private javax.swing.JPanel ContactInfoPanel;
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JPanel DatePanel;
    private javax.swing.JPanel DateTrainingPanel;
    private javax.swing.JPanel EndDate;
    private javax.swing.JPanel Fax;
    private javax.swing.JPanel MainDisplayPanel;
    private javax.swing.JPanel NamePanel;
    private javax.swing.JPanel Phone1;
    private javax.swing.JPanel Phone2;
    private javax.swing.JPanel PhonePanel;
    private javax.swing.JPanel SpacerPanel1;
    private javax.swing.JPanel StartDate;
    private javax.swing.JPanel TermActiveDatePanel;
    private javax.swing.JCheckBox activeCheckBox;
    private javax.swing.JPanel additionalPanel;
    private javax.swing.JCheckBox callQueueChk;
    private javax.swing.JPanel checkinPanel;
    private javax.swing.JComboBox checkoutCombo;
    private javax.swing.JTextField efAddress;
    private javax.swing.JTextField efAddress_2;
    private javax.swing.JTextField efCity;
    private javax.swing.JTextField efFax;
    private javax.swing.JTextField efName;
    private javax.swing.JTextField efPhone;
    private javax.swing.JTextField efPhone_2;
    private javax.swing.JTextField efState;
    private javax.swing.JFormattedTextField efZip;
    private javax.swing.JLabel hoursSchedLabel;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel jpEndDate;
    private javax.swing.JPanel jpStartDate;
    private javax.swing.JLabel lbAddress;
    private javax.swing.JLabel lbAddress1;
    private javax.swing.JLabel lbCity;
    private javax.swing.JLabel lbEndDate;
    private javax.swing.JLabel lbFax;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbPhone;
    private javax.swing.JLabel lbPhone1;
    private javax.swing.JLabel lbStartDate;
    private javax.swing.JLabel lbState;
    private javax.swing.JLabel lbZip;
    private javax.swing.JPanel pnRateCode;
    private javax.swing.JCheckBox postPhoneChk;
    private javax.swing.JPanel ratingContainerPanel;
    private javax.swing.JPanel ratingContainerPanelParent;
    private javax.swing.JPanel requiredPanel;
    // End of variables declaration//GEN-END:variables
}
