/*
 * Employee_Branch.java
 *
 * Created on June 5, 2006, 1:36 PM
 */

package rmischedule.client.components;

import schedfoxlib.model.util.Record_Set;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.security.*;
import rmischedule.main.*;
import rmischeduleserver.mysqlconnectivity.queries.util.*;


import java.util.*;
import rmischedule.client.xClientEdit;
import schedfoxlib.model.Branch;

/**
 *
 * @author  shawn
 */
public class Client_Branch extends GenericEditSubForm {
    
    private xClientEdit myParent;
    private Vector<Branch> myBranches;
    
    /** Creates new form Employee_Branch */
    public Client_Branch(xClientEdit main) {
        myParent = main;
        initComponents();
        myBranches = new Vector();
    }
    
    public String getBranch() {
        try { return myBranches.get(this.branchComboBox.getSelectedIndex()).getBranchId() + ""; }
        catch(Exception ex) { return "-1"; }
    }
    
    public boolean userHasAccess() {
        return Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.EMPLOYEE_EXPORT);
    }
    
    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getQuery(boolean isSelected) {        
        get_branch_by_company_query myQuery = new get_branch_by_company_query(myParent.getConnection().myCompany);

        return myQuery;
    }
    
    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getSaveQuery(boolean isNewData) {
        return null;
    }
    
    public String checkData() {
        return null;
    }
    
    public void loadData(Record_Set rs) {
        
        this.branchComboBox.removeAllItems();
        myBranches.clear();
        for(int i = 0; i < rs.length(); i++) {
            try {
                myBranches.add(new Branch(rs));
                this.branchComboBox.addItem(myBranches.lastElement().toString());
                
                if(myBranches.lastElement().getBranchId().toString().equals(myParent.getConnection().myBranch))
                    this.branchComboBox.setSelectedIndex(i);
                
                rs.moveNext();
            }
            catch(Exception ex) {  }
        }
    }
    
    public boolean needsMoreRecordSets() {
        return false;
    }
    
    public String getMyTabTitle() {
        return "Branch";
    }
    
    public javax.swing.JPanel getMyForm() {
        return this;
    }
    
    public void doOnClear() {
        this.branchComboBox.removeAllItems();
        myBranches.clear();
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        branchLabel = new javax.swing.JLabel();
        branchComboBox = new javax.swing.JComboBox();

        setMaximumSize(new java.awt.Dimension(32767, 30));
        setMinimumSize(new java.awt.Dimension(10, 30));
        setPreferredSize(new java.awt.Dimension(100, 30));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 40));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 40));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        branchLabel.setText("Branch");
        jPanel1.add(branchLabel);

        branchComboBox.setMaximumSize(new java.awt.Dimension(200, 20));
        branchComboBox.setMinimumSize(new java.awt.Dimension(51, 20));
        branchComboBox.setPreferredSize(new java.awt.Dimension(55, 20));
        jPanel1.add(branchComboBox);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox branchComboBox;
    private javax.swing.JLabel branchLabel;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    
}
