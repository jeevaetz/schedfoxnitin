/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditRateCodeDialog.java
 *
 * Created on Mar 17, 2011, 10:11:23 AM
 */

package rmischedule.billing;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.BillingController;
import schedfoxlib.model.EmployeeRateCode;
import schedfoxlib.model.GenericRateCode;
import schedfoxlib.model.HourType;
import schedfoxlib.model.RateCode;

/**
 *
 * @author user
 */
public class EditRateCodeDialog<T extends GenericRateCode> extends javax.swing.JDialog {

    private String companyId;
    private int primary_id;

    private T genericRateCode;

    /** Creates new form EditRateCodeDialog */
    public EditRateCodeDialog(java.awt.Frame parent, boolean modal, String companyId, int client_id, T code) {
        super(parent, modal);
        initComponents();
        this.genericRateCode = code;
        this.companyId = companyId;
        this.primary_id = client_id;
        this.loadRateCode();
        this.loadHourTypes();
        
        if (genericRateCode instanceof EmployeeRateCode) {
            jLabel2.setVisible(false);
            jLabel8.setVisible(false);
            billPanel.setVisible(false);
        } else {
            jLabel2.setVisible(true);
            jLabel8.setVisible(true);
            billPanel.setVisible(true);
        }
    }

    public void loadDataForRateCode(T rateCode) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        idText.setText(rateCode.getPrimaryKey() + "");
        //descriptionTxt.setText(rateCode.getDescription());
        regularPayTxt.setText(currencyFormat.format(rateCode.getPayAmount().doubleValue()));
        overtimePayTxt.setText(currencyFormat.format(rateCode.getOvertimeAmount().doubleValue()));
        regularBillTxt.setText(currencyFormat.format(rateCode.getBillAmount().doubleValue()));
        overtimeBillTxt.setText(currencyFormat.format(rateCode.getOvertimeBill().doubleValue()));
        hourTypeCombo.setSelectedItem(rateCode.getHourTypeObj(Integer.parseInt(companyId)));
        rateCodeCombo.setSelectedItem(rateCode.getRateCode(Integer.parseInt(companyId)));
    }

    public T getRateCode() {
        return this.genericRateCode;
    }

    private void loadRateCode() {
        try {
            rateCodeCombo.removeAllItems();
            RateCode unselectedRateCode = new RateCode();
            unselectedRateCode.setRateCodeId(-1);
            unselectedRateCode.setRateCodeName("Select a rate code");
            rateCodeCombo.addItem(unselectedRateCode);

            BillingController billingController =
                BillingController.getInstance(companyId);
            ArrayList<RateCode> myRates = billingController.getRateCodes();
            for (int r = 0; r < myRates.size(); r++) {
                rateCodeCombo.addItem(myRates.get(r));
            }
        } catch (Exception e) {

        }
    }

    private void loadHourTypes() {
        try {
            hourTypeCombo.removeAllItems();
            HourType unselectedHourType = new HourType();
            unselectedHourType.setHourTypeId(-1);
            unselectedHourType.setHourType("Select an hour type");
            hourTypeCombo.addItem(unselectedHourType);

            BillingController billingController =
                BillingController.getInstance(companyId);
            ArrayList<HourType> myRates = billingController.getHourTypes();
            for (int r = 0; r < myRates.size(); r++) {
                hourTypeCombo.addItem(myRates.get(r));
            }
        } catch (Exception e) {

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

        idText = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        rateCodeCombo = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        hourTypeCombo = new javax.swing.JComboBox();
        billPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        regularBillTxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        overtimeBillTxt = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        regularPayTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        overtimePayTxt = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        closeBtn = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setMaximumSize(new java.awt.Dimension(2000, 30));
        jPanel4.setMinimumSize(new java.awt.Dimension(41, 30));
        jPanel4.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Rate Code");
        jLabel1.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel4.add(jLabel1);

        rateCodeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel4.add(rateCodeCombo);

        getContentPane().add(jPanel4);

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel2.setMinimumSize(new java.awt.Dimension(0, 30));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setText("Hour Type");
        jLabel3.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel3.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel2.add(jLabel3);

        hourTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(hourTypeCombo);

        getContentPane().add(jPanel2);

        billPanel.setMaximumSize(new java.awt.Dimension(32767, 30));
        billPanel.setMinimumSize(new java.awt.Dimension(0, 30));
        billPanel.setPreferredSize(new java.awt.Dimension(400, 30));
        billPanel.setLayout(new javax.swing.BoxLayout(billPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setText("Regular Bill Rate");
        jLabel6.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel6.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel6.setPreferredSize(new java.awt.Dimension(100, 16));
        billPanel.add(jLabel6);
        billPanel.add(regularBillTxt);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("Overtime Bill Rate");
        jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        jLabel7.setMaximumSize(new java.awt.Dimension(120, 16));
        jLabel7.setMinimumSize(new java.awt.Dimension(120, 16));
        jLabel7.setPreferredSize(new java.awt.Dimension(120, 16));
        billPanel.add(jLabel7);
        billPanel.add(overtimeBillTxt);

        getContentPane().add(billPanel);

        jPanel3.setMinimumSize(new java.awt.Dimension(0, 20));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 20));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("* Only use the pay rate if the client has a mandatory minimum pay rate for");
        jPanel3.add(jLabel2);

        getContentPane().add(jPanel3);

        jPanel8.setMinimumSize(new java.awt.Dimension(0, 20));
        jPanel8.setPreferredSize(new java.awt.Dimension(400, 20));
        jPanel8.setLayout(new java.awt.GridLayout(1, 0));

        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("  the employees.  Otherwise, leave this space blank.");
        jPanel8.add(jLabel8);

        getContentPane().add(jPanel8);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 30));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setText("Guard Pay Rate");
        jLabel4.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel4.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel4.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel1.add(jLabel4);

        regularPayTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                regularPayTxtFocusLost(evt);
            }
        });
        jPanel1.add(regularPayTxt);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Guard Overtime Rate");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
        jPanel1.add(jLabel5);
        jPanel1.add(overtimePayTxt);

        getContentPane().add(jPanel1);

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        closeBtn.setText("Close");
        closeBtn.setMaximumSize(new java.awt.Dimension(70, 28));
        closeBtn.setMinimumSize(new java.awt.Dimension(70, 28));
        closeBtn.setPreferredSize(new java.awt.Dimension(70, 28));
        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });
        jPanel5.add(closeBtn);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 31, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel7);

        saveBtn.setText("Save");
        saveBtn.setMaximumSize(new java.awt.Dimension(70, 28));
        saveBtn.setMinimumSize(new java.awt.Dimension(70, 28));
        saveBtn.setPreferredSize(new java.awt.Dimension(70, 28));
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });
        jPanel5.add(saveBtn);

        getContentPane().add(jPanel5);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-428)/2, (screenSize.height-225)/2, 428, 225);
    }// </editor-fold>//GEN-END:initComponents

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        genericRateCode = null;
        this.dispose();
    }//GEN-LAST:event_closeBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        try {
            try {
                genericRateCode.setPrimaryKey(Integer.parseInt(idText.getText()));
            } catch (Exception e) {}
            //clientRateCode.setDescription(descriptionTxt.getText());
            
            RateCode selectedRateCode = null;
            try {
               selectedRateCode = (RateCode)rateCodeCombo.getSelectedItem();
            } catch (Exception e) {
                throw new Exception("Please select a Rate Code!");
            }
            HourType selectedHourType = (HourType)hourTypeCombo.getSelectedItem();
            if (selectedRateCode.getRateCodeId() == -1) {
                throw new Exception("Please select a Rate Code!");
            } else {
                genericRateCode.setRateCodeId(selectedRateCode.getRateCodeId());
            }
            if (selectedHourType.getHourTypeId() == -1) {
                throw new Exception("Please select a Hour Type!");
            } else {
                genericRateCode.setHourType(selectedHourType.getHourTypeId());
            }
            try {
                if (regularPayTxt.getText().trim().length() != 0) {
                    genericRateCode.setPayAmount(new BigDecimal(currencyFormat.parse(regularPayTxt.getText()).doubleValue()));
                }
            } catch (Exception e) {
                try {
                    genericRateCode.setPayAmount(new BigDecimal(numberFormat.parse(regularPayTxt.getText()).doubleValue()));
                } catch (Exception ex) {
                    throw new Exception("Please specify a valid pay amount!");
                }
            }
            try {
                if (overtimePayTxt.getText().trim().length() != 0) {
                    genericRateCode.setOvertimeAmount(new BigDecimal(currencyFormat.parse(overtimePayTxt.getText()).doubleValue()));
                }
            } catch (Exception e) {
                try {
                    genericRateCode.setOvertimeAmount(new BigDecimal(numberFormat.parse(overtimePayTxt.getText()).doubleValue()));
                } catch (Exception ex) {
                    throw new Exception("Please specify a valid overtime pay amount!");
                }
            }
            try {
                if (regularBillTxt.getText().trim().length() != 0) {
                    genericRateCode.setBillAmount(new BigDecimal(currencyFormat.parse(regularBillTxt.getText()).doubleValue()));
                }
            } catch (Exception e) {
                try {
                    genericRateCode.setBillAmount(new BigDecimal(numberFormat.parse(regularBillTxt.getText()).doubleValue()));
                } catch (Exception ex) {
                    throw new Exception("Please specify a valid bill amount!");
                }
            }
            try {
                if (overtimeBillTxt.getText().trim().length() != 0) {
                    genericRateCode.setOvertimeBill(new BigDecimal(currencyFormat.parse(overtimeBillTxt.getText()).doubleValue()));
                }
            } catch (Exception e) {
                try {
                    genericRateCode.setOvertimeBill(new BigDecimal(numberFormat.parse(overtimeBillTxt.getText()).doubleValue()));
                } catch (Exception ex) {
                    throw new Exception("Please specify a valid overtime bill amount!");
                }
            }

            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void regularPayTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_regularPayTxtFocusLost
        try {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            double myVal = 0;
            try {
                myVal = Double.parseDouble(regularPayTxt.getText());
            } catch (Exception exe) {
                myVal = currencyFormat.parse(regularPayTxt.getText()).doubleValue();
            }
            myVal = myVal * 1.5;
            overtimePayTxt.setText(currencyFormat.format(myVal));
        } catch (Exception e) {

        }
    }//GEN-LAST:event_regularPayTxtFocusLost

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel billPanel;
    private javax.swing.JButton closeBtn;
    private javax.swing.JComboBox hourTypeCombo;
    private javax.swing.JTextField idText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTextField overtimeBillTxt;
    private javax.swing.JTextField overtimePayTxt;
    private javax.swing.JComboBox rateCodeCombo;
    private javax.swing.JTextField regularBillTxt;
    private javax.swing.JTextField regularPayTxt;
    private javax.swing.JButton saveBtn;
    // End of variables declaration//GEN-END:variables

}
