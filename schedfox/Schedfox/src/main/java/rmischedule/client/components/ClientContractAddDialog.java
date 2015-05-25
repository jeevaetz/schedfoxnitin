/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClientContractAddDialog.java
 *
 * Created on Jul 8, 2011, 10:44:02 AM
 */
package rmischedule.client.components;

import java.awt.Cursor;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import rmischedule.client.xClientEdit;
import rmischedule.components.jcalendar.JCalendarComboBox;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import rmischedule.timeoff.SetupTimeOffIntervalDialog;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.CommissionController;
import rmischeduleserver.control.UserController;
import schedfoxlib.model.util.FileLoader;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContract;
import schedfoxlib.model.ClientContractType;
import schedfoxlib.model.Commission;
import schedfoxlib.model.Contact;
import schedfoxlib.model.User;

/**
 *
 * @author user
 */
public class ClientContractAddDialog extends javax.swing.JDialog {

    private JCalendarComboBox begCal;
    private JCalendarComboBox endCal;
    private JCalendarComboBox renewalCal;
    private JCalendarComboBox contractStartDate;
    private xClientEdit clientEdit;
    private ClientContract contract;
    private ClientContractTypeModel contractTypeModel = new ClientContractTypeModel();
    private File contractFile;

    /** Creates new form ClientContractAddDialog */
    public ClientContractAddDialog(java.awt.Frame parent, boolean modal,
            xClientEdit clientEdit, ClientContract contract) {
        super(parent, modal);
        initComponents();

        this.contract = contract;

        try {
            ClientController clientController =
                    ClientController.getInstance(clientEdit.getConnection().myCompany);
            UserController userController = new UserController(clientEdit.getConnection().myCompany);
            ArrayList<ClientContractType> clientContractTypes = clientController.getClientContractTypes();
            for (int c = 0; c < clientContractTypes.size(); c++) {
                contractTypeModel.add(clientContractTypes.get(c));
            }
            ArrayList<Contact> sales = userController.getUsersByType("Sales", "-1");
            User selectOne = new User();
            selectOne.setUserId(-1);
            selectOne.setUserFirstName("Please select one");
            selectOne.setUserLastName("");

            salesPersonCmb.addItem(selectOne);
            for (int s = 0; s < sales.size(); s++) {
                salesPersonCmb.addItem(sales.get(s));
            }

            salesMgrPersonCmb.addItem(selectOne);
            ArrayList<Contact> salesManager = userController.getUsersByType("Sales Manager", "-1");
            for (int s = 0; s < salesManager.size(); s++) {
                salesMgrPersonCmb.addItem(salesManager.get(s));
            }

            Commission selectCommission = new Commission();
            selectCommission.setCommission_id(-1);
            selectCommission.setCommission_name("Please select one");
            CommissionController commissionController = CommissionController.getInstance(clientEdit.getConnection().myCompany);
            ArrayList<Commission> commissions = commissionController.getCommissions();
            commissionCmb.addItem(selectCommission);
            for (int c = 0; c < commissions.size(); c++) {
                commissionCmb.addItem(commissions.get(c));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar renewalDate = Calendar.getInstance();
        Calendar startCommissionDate = Calendar.getInstance();

        endCal = new JCalendarComboBox();
        renewalCal = new JCalendarComboBox();
        contractStartDate = new JCalendarComboBox();
        
        if (this.contract != null) {
            startDate.setTime(this.contract.getStartDate());
            endDate.setTime(this.contract.getEndDate());
            try {
                startCommissionDate.setTime(this.contract.getStartCommissionDate());
            } catch (Exception exe) {}
            increaseSpinner.setValue(this.contract.getProjectedIncrease().doubleValue());
            autoRenewChk.setSelected(this.contract.getAutoRenew());
            try {
                String unitStr = contract.getRenewalPeriod().substring(contract.getRenewalPeriod().indexOf(" ")).trim();
                String renew = SetupTimeOffIntervalDialog.convertRange(unitStr);
                Integer unit = Integer.parseInt(contract.getRenewalPeriod().substring(0, contract.getRenewalPeriod().indexOf(" ")));
                timePeriodCombo.setSelectedItem(renew);
                spinnerVal.setValue(unit);
            } catch (Exception e) {
            }
            try {
                contractTypeModel.setSelectedItem(contract.getClientContractType(clientEdit.getConnection().myCompany));
            } catch (Exception e) {
            }

            try {
                User selectedUser = new User();
                selectedUser.setUserId(contract.getSalesPerson());
                salesPersonCmb.setSelectedItem(selectedUser);
            } catch (Exception e) {

            }

            try {
                User selectedUser = new User();
                selectedUser.setUserId(contract.getSalesManager());
                salesMgrPersonCmb.setSelectedItem(selectedUser);
            } catch (Exception e) {

            }

            try {
                Commission selectedCommission = new Commission();
                selectedCommission.setCommission_id(contract.getSalesCommission());
                commissionCmb.setSelectedItem(selectedCommission);
            } catch (Exception e) {

            }

            endCal.setCalendar(endDate);
            try {
                renewalDate.setTime(this.contract.getLastRenewed());
                renewalCal.setCalendar(renewalDate);
                renewalDatePanel.add(renewalCal);
            } catch (Exception e) {}

            try {
                tempAccountChk.setSelected(contract.getTempAccount());
            } catch (Exception e) {}

        } else {
            Client selectedClient = (Client) clientEdit.getSelectedObject();
            Integer numMonths = selectedClient.getNumMonths();
            this.contract = new ClientContract();
            endDate.add(Calendar.MONTH, numMonths);
            try {
                endCal.setCalendar(endDate);
            } catch (Exception e) {
            }
        }

        this.clientEdit = clientEdit;

        begCal = new JCalendarComboBox();
        

        try {
            commissionStartDatePanel.add(contractStartDate);
            this.contractStartDate.setCalendar(startCommissionDate);
        } catch (Exception e) {
        
        }
        try {
            begCal.setCalendar(startDate);
        } catch (Exception e) {
        }

        startDatePanel.add(begCal);
        endDatePanel.add(endCal);
        
    }

    private void saveFileToServer(File fileToSave) {
        Client selClient = (Client) clientEdit.getSelectedObject();

        Company myCompany = Main_Window.parentOfApplication.getCompanyById(clientEdit.getConnection().myCompany);

        boolean isSuccesfull = FileLoader.saveAdditionalFile("location_additional_files",
                myCompany.getDB(), "con" + selClient.getClientId() + "_" + contract.getClientContractId(),
                fileToSave.getName(), fileToSave);

        //  tell user if succesful
        if (isSuccesfull) {
            JOptionPane.showMessageDialog(this, "The file was saved successfully to the server.",
                    "Save file to server", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error:  file was not saved to server.",
                    "Save file to server", JOptionPane.ERROR_MESSAGE);
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

        topPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        startDatePanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        uploadBtn = new javax.swing.JButton();
        topPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        startDatePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        endDatePanel = new javax.swing.JPanel();
        renewalPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        renewalDatePanel = new javax.swing.JPanel();
        autoRenewChk = new javax.swing.JCheckBox();
        topPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        startDatePanel1 = new javax.swing.JPanel();
        increaseSpinner = new javax.swing.JSpinner();
        tempAccountChk = new javax.swing.JCheckBox();
        topPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        startDatePanel4 = new javax.swing.JPanel();
        spinnerVal = new javax.swing.JSpinner();
        timePeriodCombo = new javax.swing.JComboBox();
        topPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        startDatePanel3 = new javax.swing.JPanel();
        commissionCmb = new javax.swing.JComboBox();
        commissionStartPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        commissionStartDatePanel = new javax.swing.JPanel();
        salesPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        startDatePanel7 = new javax.swing.JPanel();
        salesPersonCmb = new javax.swing.JComboBox();
        salesManagerPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        startDatePanel6 = new javax.swing.JPanel();
        salesMgrPersonCmb = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();
        closeBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        topPanel2.setMaximumSize(new java.awt.Dimension(1000, 30));
        topPanel2.setMinimumSize(new java.awt.Dimension(100, 30));
        topPanel2.setPreferredSize(new java.awt.Dimension(382, 30));
        topPanel2.setLayout(new javax.swing.BoxLayout(topPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setText("Contract Type");
        jLabel4.setMaximumSize(new java.awt.Dimension(140, 16));
        jLabel4.setMinimumSize(new java.awt.Dimension(140, 16));
        jLabel4.setPreferredSize(new java.awt.Dimension(140, 16));
        topPanel2.add(jLabel4);

        startDatePanel2.setLayout(new java.awt.GridLayout(1, 0));

        jComboBox1.setModel(contractTypeModel);
        startDatePanel2.add(jComboBox1);

        uploadBtn.setText("Upload Contract");
        uploadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadBtnActionPerformed(evt);
            }
        });
        startDatePanel2.add(uploadBtn);

        topPanel2.add(startDatePanel2);

        getContentPane().add(topPanel2);

        topPanel.setMaximumSize(new java.awt.Dimension(1000, 30));
        topPanel.setMinimumSize(new java.awt.Dimension(100, 30));
        topPanel.setPreferredSize(new java.awt.Dimension(382, 30));
        topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Start Date");
        jLabel1.setMaximumSize(new java.awt.Dimension(140, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(140, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(140, 16));
        topPanel.add(jLabel1);

        startDatePanel.setLayout(new java.awt.GridLayout(1, 0));
        topPanel.add(startDatePanel);

        jLabel2.setText("End Date");
        topPanel.add(jLabel2);

        endDatePanel.setLayout(new java.awt.GridLayout(1, 0));
        topPanel.add(endDatePanel);

        getContentPane().add(topPanel);

        renewalPanel.setMaximumSize(new java.awt.Dimension(1000, 30));
        renewalPanel.setMinimumSize(new java.awt.Dimension(100, 30));
        renewalPanel.setPreferredSize(new java.awt.Dimension(382, 30));
        renewalPanel.setLayout(new javax.swing.BoxLayout(renewalPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel8.setText("Renewal Date");
        jLabel8.setMaximumSize(new java.awt.Dimension(140, 16));
        jLabel8.setMinimumSize(new java.awt.Dimension(140, 16));
        jLabel8.setPreferredSize(new java.awt.Dimension(140, 16));
        renewalPanel.add(jLabel8);

        renewalDatePanel.setLayout(new javax.swing.BoxLayout(renewalDatePanel, javax.swing.BoxLayout.LINE_AXIS));
        renewalPanel.add(renewalDatePanel);

        autoRenewChk.setSelected(true);
        autoRenewChk.setText("Auto Renew?");
        renewalPanel.add(autoRenewChk);

        getContentPane().add(renewalPanel);

        topPanel1.setMaximumSize(new java.awt.Dimension(1000, 30));
        topPanel1.setMinimumSize(new java.awt.Dimension(100, 30));
        topPanel1.setPreferredSize(new java.awt.Dimension(382, 30));
        topPanel1.setLayout(new javax.swing.BoxLayout(topPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setText("Projected Increase");
        jLabel3.setMaximumSize(new java.awt.Dimension(140, 16));
        jLabel3.setMinimumSize(new java.awt.Dimension(140, 16));
        jLabel3.setPreferredSize(new java.awt.Dimension(140, 16));
        topPanel1.add(jLabel3);

        startDatePanel1.setLayout(new java.awt.GridLayout(1, 0));

        increaseSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(0.5d)));
        startDatePanel1.add(increaseSpinner);

        tempAccountChk.setText("Temporary Account");
        startDatePanel1.add(tempAccountChk);

        topPanel1.add(startDatePanel1);

        getContentPane().add(topPanel1);

        topPanel4.setMaximumSize(new java.awt.Dimension(1000, 30));
        topPanel4.setMinimumSize(new java.awt.Dimension(100, 30));
        topPanel4.setPreferredSize(new java.awt.Dimension(382, 30));
        topPanel4.setLayout(new javax.swing.BoxLayout(topPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setText("Renewal Period");
        jLabel6.setMaximumSize(new java.awt.Dimension(140, 16));
        jLabel6.setMinimumSize(new java.awt.Dimension(140, 16));
        jLabel6.setPreferredSize(new java.awt.Dimension(140, 16));
        topPanel4.add(jLabel6);

        startDatePanel4.setLayout(new javax.swing.BoxLayout(startDatePanel4, javax.swing.BoxLayout.LINE_AXIS));

        spinnerVal.setMaximumSize(new java.awt.Dimension(60, 32767));
        spinnerVal.setMinimumSize(new java.awt.Dimension(60, 28));
        spinnerVal.setPreferredSize(new java.awt.Dimension(60, 28));
        spinnerVal.setValue(1);
        startDatePanel4.add(spinnerVal);

        timePeriodCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Day", "Week", "Month", "Year" }));
        startDatePanel4.add(timePeriodCombo);

        topPanel4.add(startDatePanel4);

        getContentPane().add(topPanel4);

        topPanel3.setMaximumSize(new java.awt.Dimension(1000, 30));
        topPanel3.setMinimumSize(new java.awt.Dimension(100, 30));
        topPanel3.setPreferredSize(new java.awt.Dimension(382, 30));
        topPanel3.setLayout(new javax.swing.BoxLayout(topPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setText("Commission");
        jLabel5.setMaximumSize(new java.awt.Dimension(140, 16));
        jLabel5.setMinimumSize(new java.awt.Dimension(140, 16));
        jLabel5.setPreferredSize(new java.awt.Dimension(140, 16));
        topPanel3.add(jLabel5);

        startDatePanel3.setLayout(new java.awt.GridLayout(1, 0));

        startDatePanel3.add(commissionCmb);

        topPanel3.add(startDatePanel3);

        getContentPane().add(topPanel3);

        commissionStartPanel.setMaximumSize(new java.awt.Dimension(1000, 30));
        commissionStartPanel.setMinimumSize(new java.awt.Dimension(100, 30));
        commissionStartPanel.setPreferredSize(new java.awt.Dimension(382, 30));
        commissionStartPanel.setLayout(new javax.swing.BoxLayout(commissionStartPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel7.setText("Commission Start Date");
        jLabel7.setMaximumSize(new java.awt.Dimension(140, 16));
        jLabel7.setMinimumSize(new java.awt.Dimension(140, 16));
        jLabel7.setPreferredSize(new java.awt.Dimension(140, 16));
        commissionStartPanel.add(jLabel7);

        commissionStartDatePanel.setLayout(new java.awt.GridLayout(1, 0));
        commissionStartPanel.add(commissionStartDatePanel);

        getContentPane().add(commissionStartPanel);

        salesPanel.setMaximumSize(new java.awt.Dimension(1000, 30));
        salesPanel.setMinimumSize(new java.awt.Dimension(100, 30));
        salesPanel.setPreferredSize(new java.awt.Dimension(382, 30));
        salesPanel.setLayout(new javax.swing.BoxLayout(salesPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel10.setText("BDM");
        jLabel10.setMaximumSize(new java.awt.Dimension(140, 16));
        jLabel10.setMinimumSize(new java.awt.Dimension(140, 16));
        jLabel10.setPreferredSize(new java.awt.Dimension(140, 16));
        salesPanel.add(jLabel10);

        startDatePanel7.setLayout(new javax.swing.BoxLayout(startDatePanel7, javax.swing.BoxLayout.LINE_AXIS));

        startDatePanel7.add(salesPersonCmb);

        salesPanel.add(startDatePanel7);

        getContentPane().add(salesPanel);

        salesManagerPanel.setMaximumSize(new java.awt.Dimension(1000, 30));
        salesManagerPanel.setMinimumSize(new java.awt.Dimension(100, 30));
        salesManagerPanel.setPreferredSize(new java.awt.Dimension(382, 30));
        salesManagerPanel.setLayout(new javax.swing.BoxLayout(salesManagerPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel9.setText("Dir. Business Dev");
        jLabel9.setMaximumSize(new java.awt.Dimension(140, 16));
        jLabel9.setMinimumSize(new java.awt.Dimension(140, 16));
        jLabel9.setPreferredSize(new java.awt.Dimension(140, 16));
        salesManagerPanel.add(jLabel9);

        startDatePanel6.setLayout(new javax.swing.BoxLayout(startDatePanel6, javax.swing.BoxLayout.LINE_AXIS));

        startDatePanel6.add(salesMgrPersonCmb);

        salesManagerPanel.add(startDatePanel6);

        getContentPane().add(salesManagerPanel);

        saveBtn.setText("Save");
        saveBtn.setMaximumSize(new java.awt.Dimension(75, 28));
        saveBtn.setMinimumSize(new java.awt.Dimension(75, 28));
        saveBtn.setPreferredSize(new java.awt.Dimension(75, 28));
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        closeBtn.setText("Close");
        closeBtn.setMaximumSize(new java.awt.Dimension(75, 28));
        closeBtn.setMinimumSize(new java.awt.Dimension(75, 28));
        closeBtn.setPreferredSize(new java.awt.Dimension(75, 28));
        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(256, Short.MAX_VALUE)
                .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);

        setSize(new java.awt.Dimension(443, 357));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        Client selClient = (Client) clientEdit.getSelectedObject();

        try {
            if (!begCal.getCalendar().before(endCal.getCalendar())) {
                throw new Exception("The end date must be after the start date!");
            }

            Double projected = (Double) increaseSpinner.getValue();

            contract.setClientId(selClient.getClientId());
            contract.setProjectedIncrease(new BigDecimal(projected));
            contract.setStartDate(begCal.getCalendar().getTime());
            contract.setEndDate(endCal.getCalendar().getTime());
            contract.setAutoRenew(autoRenewChk.isSelected());
            contract.setTempAccount(tempAccountChk.isSelected());

            try {
                Contact sales = (Contact)salesPersonCmb.getSelectedItem();
                contract.setSalesPerson(sales.getPrimaryId());
            } catch (Exception e) {
                
            }

            try {
                Contact salesManager = (Contact)salesMgrPersonCmb.getSelectedItem();
                contract.setSalesManager(salesManager.getPrimaryId());
            } catch (Exception e) {

            }

            try {
                Commission selCommission = (Commission)commissionCmb.getSelectedItem();
                contract.setSalesCommission(selCommission.getCommission_id());
            } catch (Exception e) {

            }

            int numberOfUnits = 0;
            String units = "";
            try {
                numberOfUnits = (Integer) spinnerVal.getValue();
                units = timePeriodCombo.getSelectedItem().toString();
                if (units.equals("Month")) {
                    units = "mon";
                }
                String interval = numberOfUnits + " " + units;
                if (numberOfUnits > 1) {
                    interval = interval + "s";
                }
                contract.setRenewalPeriod(interval);
            } catch (Exception e) {
            }

            if (contract.getClientContractId() != null) {
                contract.setLastRenewed(renewalCal.getCalendar().getTime());
            } else {
                Calendar renewal = begCal.getCalendar();
                Date today = new Date(new Connection().getServerTimeMillis());
                if (numberOfUnits > 0) {
                    while (renewal.getTime().compareTo(today) < 0) {
                        if (units.equalsIgnoreCase("Year")) {
                            renewal.add(Calendar.YEAR, numberOfUnits);
                        } else if (units.equalsIgnoreCase("mon")) {
                            renewal.add(Calendar.MONTH, numberOfUnits);
                        } else if (units.equalsIgnoreCase("Week")) {
                            renewal.add(Calendar.WEEK_OF_YEAR, numberOfUnits);
                        } else if (units.equalsIgnoreCase("Day")) {
                            renewal.add(Calendar.DAY_OF_YEAR, numberOfUnits);
                        } else {
                            renewal.setTime(today);
                        }
                    }
                    contract.setLastRenewed(renewal.getTime());
                }
            }
            
            try {
                ClientContractType clientContract = (ClientContractType) contractTypeModel.getElementAt(jComboBox1.getSelectedIndex());
                contract.setClientContractTypeId(clientContract.getClientContractTypeId());
            } catch (Exception e) {
            }
            final ClientController clientController =
                    ClientController.getInstance(clientEdit.getConnection().myCompany);

            try {
                clientController.saveClientContract(contract);
                Thread runInvoicing = new Thread() {
                    @Override
                    public void run() {
                        try {
                            clientController.runInvoicingForClient(contract);
                        } catch (Exception exe) {}
                    }
                };
                runInvoicing.start();
                
                if (this.contractFile != null) {
                    this.saveFileToServer(this.contractFile);
                }
            } catch (Exception e) {
                throw new Exception("Could not save this client contract!");
            }
        } catch (Exception exe) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    exe.getMessage(), "Error Saving", JOptionPane.ERROR_MESSAGE);
        }

        this.dispose();
    }//GEN-LAST:event_saveBtnActionPerformed

    private void uploadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadBtnActionPerformed
        //  setup file chooser
        JFileChooser fchooser = new JFileChooser();
        fchooser.setDialogTitle("Select a file to upload for contract.");
        fchooser.setAcceptAllFileFilterUsed(true);
        fchooser.setMultiSelectionEnabled(false);

        Client selClient = (Client) clientEdit.getSelectedObject();
        String key = selClient.getClientId().toString();
        if (key.length() > 10) {
            JOptionPane.showMessageDialog(this, "Error saving file:  no location selected.",
                    "Save Additional File", JOptionPane.ERROR_MESSAGE);
        } else {
            if (fchooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                    setCursor(hourglassCursor);
                    contractFile = fchooser.getSelectedFile();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "There was an error saving your file.",
                            "FileServer Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    //  reset cursor
                    Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                    setCursor(normalCursor);
                }   // finally
            }   //  if
        }  // else
    }//GEN-LAST:event_uploadBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autoRenewChk;
    private javax.swing.JButton closeBtn;
    private javax.swing.JComboBox commissionCmb;
    private javax.swing.JPanel commissionStartDatePanel;
    private javax.swing.JPanel commissionStartPanel;
    private javax.swing.JPanel endDatePanel;
    private javax.swing.JSpinner increaseSpinner;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel renewalDatePanel;
    private javax.swing.JPanel renewalPanel;
    private javax.swing.JPanel salesManagerPanel;
    private javax.swing.JComboBox salesMgrPersonCmb;
    private javax.swing.JPanel salesPanel;
    private javax.swing.JComboBox salesPersonCmb;
    private javax.swing.JButton saveBtn;
    private javax.swing.JSpinner spinnerVal;
    private javax.swing.JPanel startDatePanel;
    private javax.swing.JPanel startDatePanel1;
    private javax.swing.JPanel startDatePanel2;
    private javax.swing.JPanel startDatePanel3;
    private javax.swing.JPanel startDatePanel4;
    private javax.swing.JPanel startDatePanel6;
    private javax.swing.JPanel startDatePanel7;
    private javax.swing.JCheckBox tempAccountChk;
    private javax.swing.JComboBox timePeriodCombo;
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel topPanel1;
    private javax.swing.JPanel topPanel2;
    private javax.swing.JPanel topPanel3;
    private javax.swing.JPanel topPanel4;
    private javax.swing.JButton uploadBtn;
    // End of variables declaration//GEN-END:variables
}
