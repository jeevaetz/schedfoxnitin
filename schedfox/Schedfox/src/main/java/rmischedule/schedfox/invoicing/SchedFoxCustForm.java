/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PricingForm.java
 *
 * Created on Mar 10, 2011, 2:40:37 PM
 */

package rmischedule.schedfox.invoicing;
import rmischedule.main.Main_Window;
import java.sql.ResultSet;
/**
 *
 * @author hal
 */
public class SchedFoxCustForm extends javax.swing.JInternalFrame {

    private SchedFoxCustomers schedFoxPrice;
    /** Creates new form PricingForm */
    public SchedFoxCustForm() {
        initComponents();
        schedFoxPrice=new SchedFoxCustomers();
        schedFoxPrice.getPricingData();
        schedFoxPrice.getNextPrice();
        populateFields();
        if(Main_Window.parentOfApplication!=null){
//            java.awt.Rectangle rec=Main_Window.parentOfApplication.getBounds();
//            rec.setRect(rec.getX()+200,rec.getY()+200,600.0,350.0);
//            rec.setRect(200,200,600.0,350.0);
            setBounds(200,200,600,400);
        }
    }
    private void populateFields(){
        pricingType.setText(String.valueOf(schedFoxPrice.getType()));
        pricingDescription.setText(schedFoxPrice.getDescription());
        stdPrice.setText(String.valueOf(schedFoxPrice.getStdPrice()));
        addEmpPrice.setText(String.valueOf(schedFoxPrice.getAddEmpPrice()));
        textPrice.setText(String.valueOf(schedFoxPrice.getTextPrice()));
        storagePrice.setText(String.valueOf(schedFoxPrice.getStoragePrice()));
        taxRate.setText(String.valueOf(schedFoxPrice.getTaxRate()));
    }
private void save(){
        schedFoxPrice.type=Integer.parseInt(pricingType.getText());
        schedFoxPrice.typeDescription=pricingDescription.getText();
        schedFoxPrice.stdPrice=Float.parseFloat(stdPrice.getText());
        schedFoxPrice.addEmpPrice=Float.valueOf(addEmpPrice.getText());
        schedFoxPrice.textPrice=Float.valueOf(textPrice.getText());
        schedFoxPrice.storagePrice=Float.valueOf(storagePrice.getText());
        schedFoxPrice.taxRate=Float.valueOf(taxRate.getText());
        schedFoxPrice.saveData();
        schedFoxPrice.getNextPrice();
        populateFields();
}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        typeLabel = new javax.swing.JLabel();
        pricingType = new javax.swing.JTextField();
        pricingDescription = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        priceLabel = new javax.swing.JLabel();
        stdPrice = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        addEmpLabel = new javax.swing.JLabel();
        addEmpPrice = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        addEmpLabel1 = new javax.swing.JLabel();
        textPrice = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        addEmpLabel2 = new javax.swing.JLabel();
        storagePrice = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        addEmpLabel3 = new javax.swing.JLabel();
        taxRate = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel4.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 36));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("SchedFox Pricing");
        jLabel4.setPreferredSize(new java.awt.Dimension(300, 50));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 527, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 74, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel6, gridBagConstraints);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 37));
        jPanel1.setMinimumSize(new java.awt.Dimension(144, 20));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 10));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        typeLabel.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 14));
        typeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        typeLabel.setText("Pricing Type");
        typeLabel.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel1.add(typeLabel);

        pricingType.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pricingType.setMargin(new java.awt.Insets(0, 5, 0, 5));
        pricingType.setPreferredSize(new java.awt.Dimension(60, 27));
        jPanel1.add(pricingType);

        pricingDescription.setMargin(new java.awt.Insets(0, 5, 0, 5));
        pricingDescription.setPreferredSize(new java.awt.Dimension(300, 27));
        jPanel1.add(pricingDescription);

        jPanel4.add(jPanel1);

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 37));
        jPanel2.setMinimumSize(new java.awt.Dimension(144, 37));
        jPanel2.setPreferredSize(new java.awt.Dimension(215, 37));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        priceLabel.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 14));
        priceLabel.setText("Price");
        priceLabel.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel2.add(priceLabel);

        stdPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        stdPrice.setMargin(new java.awt.Insets(0, 5, 0, 5));
        stdPrice.setPreferredSize(new java.awt.Dimension(60, 27));
        jPanel2.add(stdPrice);

        jPanel4.add(jPanel2);

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 37));
        jPanel3.setMinimumSize(new java.awt.Dimension(144, 37));
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 37));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        addEmpLabel.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 14));
        addEmpLabel.setText("Additional Employess");
        addEmpLabel.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel3.add(addEmpLabel);

        addEmpPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        addEmpPrice.setMargin(new java.awt.Insets(0, 5, 0, 5));
        addEmpPrice.setPreferredSize(new java.awt.Dimension(60, 27));
        jPanel3.add(addEmpPrice);

        jPanel4.add(jPanel3);

        jPanel7.setMaximumSize(new java.awt.Dimension(32767, 37));
        jPanel7.setMinimumSize(new java.awt.Dimension(144, 37));
        jPanel7.setPreferredSize(new java.awt.Dimension(100, 37));
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        addEmpLabel1.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 14));
        addEmpLabel1.setText("Texting");
        addEmpLabel1.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel7.add(addEmpLabel1);

        textPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textPrice.setMargin(new java.awt.Insets(0, 5, 0, 5));
        textPrice.setPreferredSize(new java.awt.Dimension(60, 27));
        jPanel7.add(textPrice);

        jPanel4.add(jPanel7);

        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 37));
        jPanel8.setMinimumSize(new java.awt.Dimension(144, 37));
        jPanel8.setPreferredSize(new java.awt.Dimension(100, 37));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        addEmpLabel2.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 14));
        addEmpLabel2.setText("Storage");
        addEmpLabel2.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel8.add(addEmpLabel2);

        storagePrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        storagePrice.setMargin(new java.awt.Insets(0, 5, 0, 5));
        storagePrice.setPreferredSize(new java.awt.Dimension(60, 27));
        jPanel8.add(storagePrice);

        jPanel4.add(jPanel8);

        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 37));
        jPanel9.setMinimumSize(new java.awt.Dimension(144, 37));
        jPanel9.setPreferredSize(new java.awt.Dimension(100, 37));
        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        addEmpLabel3.setFont(new java.awt.Font("DejaVu LGC Sans", 0, 14)); // NOI18N
        addEmpLabel3.setText("Tax Rate");
        addEmpLabel3.setPreferredSize(new java.awt.Dimension(150, 17));
        jPanel9.add(addEmpLabel3);

        taxRate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        taxRate.setMargin(new java.awt.Insets(0, 5, 0, 5));
        taxRate.setPreferredSize(new java.awt.Dimension(60, 27));
        jPanel9.add(taxRate);

        jPanel4.add(jPanel9);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel4, gridBagConstraints);

        jPanel5.setPreferredSize(new java.awt.Dimension(175, 40));

        saveButton.setText("Save");
        saveButton.setPreferredSize(new java.awt.Dimension(80, 29));
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jPanel5.add(saveButton);

        previousButton.setText("Previous");
        previousButton.setPreferredSize(new java.awt.Dimension(80, 29));
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });
        jPanel5.add(previousButton);

        nextButton.setText("Next");
        nextButton.setPreferredSize(new java.awt.Dimension(80, 29));
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        jPanel5.add(nextButton);

        cancelButton.setText("Cancel");
        cancelButton.setPreferredSize(new java.awt.Dimension(80, 29));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel5.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel5, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
        
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        save();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        schedFoxPrice.getNextPrice();
        populateFields();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        schedFoxPrice.getPreviousPrice();
        populateFields();
    }//GEN-LAST:event_previousButtonActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SchedFoxCustForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addEmpLabel;
    private javax.swing.JLabel addEmpLabel1;
    private javax.swing.JLabel addEmpLabel2;
    private javax.swing.JLabel addEmpLabel3;
    private javax.swing.JTextField addEmpPrice;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JTextField pricingDescription;
    private javax.swing.JTextField pricingType;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField stdPrice;
    private javax.swing.JTextField storagePrice;
    private javax.swing.JTextField taxRate;
    private javax.swing.JTextField textPrice;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables

}
