/*
 * NewImportDialog.java
 *
 * Created on June 13, 2006, 10:36 AM
 */

package rmischedule.data_import.graphicalimportClasses;

import javax.swing.*;
import java.io.*;
import java.lang.Thread;
import java.util.*;

import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import rmischedule.main.Main_Window.*;
import schedfoxlib.model.Branch;

/**
 *
 * @author  shawn
 */
public class NewImportDialog extends javax.swing.JDialog {
    
    public static final int OK_OPTION = 1;
    public static final int CANCEL_OPTION = -1;
    public static final int NONE_OPTION = 0;
    
    private File            selectedFile;
    private Vector<Company> companies;
    private Vector<Branch>  branches;
    private Company         selectedCompany;
    private Branch          selectedBranch;
    private JFileChooser    fchooser;
    protected int           selectedOption = NONE_OPTION;
    
    
    public NewImportDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.selectedCompany    = null;
        this.selectedBranch     = null;
        this.selectedFile       = null;
        this.companies          = Main_Window.parentOfApplication.getListOfCompanies();
        this.fchooser           = new JFileChooser();
        fchooser.setDialogTitle ("Choose a database to import from");
        fchooser.setFileFilter  (new ExcelFileFilter());
        this.setTitle           ("Import Data");
        fchooser.setAcceptAllFileFilterUsed (false);
 
        for(int i = 0; i < this.companies.size(); i++) {
            this.companyComboBox.addItem(this.companies.get(i).getName());
        }
    }
    
    public int showDialog() {
        this.setVisible(true);
        while(this.isVisible()) {
            try { Thread.sleep(100); }
            catch(InterruptedException ex) { }
        }
        
        return this.selectedOption;
    }
    
    public File     getSelectedFile()       { return this.selectedFile; }
    public Company  getSelectedCompany()    { return this.selectedCompany; }
    public Branch   getSelectedBranch()     { return this.selectedBranch; }
    
    
    private class ExcelFileFilter extends javax.swing.filechooser.FileFilter {
     
        public boolean accept(File f) {
            if(f.isDirectory() || f.getName().endsWith(".xls"))
                return true;
            
            return false;
        }
        
        public String getDescription() {
            return "Microsoft Excel SpreadSheet";
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        companyLabel = new javax.swing.JLabel();
        companyComboBox = new javax.swing.JComboBox();
        databaseLabel = new javax.swing.JLabel();
        databaseTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        branchLabel = new javax.swing.JLabel();
        branchComboBox = new javax.swing.JComboBox();

        setResizable(false);
        companyLabel.setText("Company to import to:");

        companyComboBox.setPreferredSize(new java.awt.Dimension(55, 18));
        companyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyComboBoxActionPerformed(evt);
            }
        });

        databaseLabel.setText("Database to import from:");

        databaseTextField.setEditable(false);

        browseButton.setText("Browse...");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        branchLabel.setText("Branch to import to:");

        branchComboBox.setMaximumSize(new java.awt.Dimension(32767, 18));
        branchComboBox.setPreferredSize(new java.awt.Dimension(55, 18));
        branchComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                branchComboBoxActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(companyLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(branchLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(databaseLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(4, 4, 4)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(databaseTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(browseButton))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(okButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelButton))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, companyComboBox, 0, 205, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, branchComboBox, 0, 205, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(companyLabel)
                    .add(companyComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(branchLabel)
                    .add(branchComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(browseButton)
                    .add(databaseLabel)
                    .add(databaseTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 22, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(okButton))
                .addContainerGap())
        );
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-400)/2, (screenSize.height-180)/2, 400, 180);
    }// </editor-fold>//GEN-END:initComponents

    private void branchComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_branchComboBoxActionPerformed
        if(this.branchComboBox.getSelectedIndex() >= 0)
            this.selectedBranch = this.branches.get(this.branchComboBox.getSelectedIndex());
    }//GEN-LAST:event_branchComboBoxActionPerformed

    private void companyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyComboBoxActionPerformed
        this.selectedCompany = this.companies.get(this.companyComboBox.getSelectedIndex());
        
        this.branchComboBox.removeAllItems();
        this.branches = this.selectedCompany.getBranches();
        for(int i = 0; i < this.branches.size(); i++) {
            this.branchComboBox.addItem(this.branches.get(i).getBranchName());
        }
    }//GEN-LAST:event_companyComboBoxActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.selectedOption     = CANCEL_OPTION;
        this.setVisible(false);
        
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if(this.selectedCompany == null) {
            JOptionPane.showMessageDialog(this, "You must select a company", "No Company Selected!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(this.selectedBranch == null) {
            JOptionPane.showMessageDialog(this, "You must select a branch", "No Branch Selected!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(this.selectedFile == null) {
            JOptionPane.showMessageDialog(this, "You must select a database to import from", "No Database Selected!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        this.selectedOption = OK_OPTION;        
        this.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        if(fchooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.selectedFile = fchooser.getSelectedFile();
            this.databaseTextField.setText(this.selectedFile.getName());
        }
    }//GEN-LAST:event_browseButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox branchComboBox;
    private javax.swing.JLabel branchLabel;
    private javax.swing.JButton browseButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox companyComboBox;
    private javax.swing.JLabel companyLabel;
    private javax.swing.JLabel databaseLabel;
    private javax.swing.JTextField databaseTextField;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    
}
