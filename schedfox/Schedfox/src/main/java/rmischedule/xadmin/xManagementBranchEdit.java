/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * xManagementBranchEdit.java
 *
 * Created on Nov 15, 2010, 11:49:02 AM
 */
package rmischedule.xadmin;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TimeZone;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import rmischedule.components.graphicalcomponents.GenericEditForm;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Branch;
import schedfoxlib.model.BranchInfo;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_next_branch_seq_query;
import rmischeduleserver.mysqlconnectivity.queries.admin.save_branch_query;
import rmischeduleserver.mysqlconnectivity.queries.util.GenericQuery;
import rmischeduleserver.mysqlconnectivity.queries.util.get_branch_by_management_query;
import rmischeduleserver.mysqlconnectivity.queries.util.save_branch_information_query;

/**
 *
 * @author user
 */
public class xManagementBranchEdit extends GenericEditSubForm {

    private GenericEditForm myParent;
    private ArrayList<Branch> branches;
    private Object[] timeZones;
    private BranchTableModel branchTableModel = new BranchTableModel();
    private Font headerFont = new Font("Times New Roman", Font.BOLD, 14);

    private static final String usTimeZonesStr = "U.S. Timezones";
    private static final String otherTimeZonesStr = "Other Timezones";


    /** Creates new form xManagementBranchEdit */
    public xManagementBranchEdit(GenericEditForm myParent) {
        String[] allTimeZones = TimeZone.getAvailableIDs();
        ArrayList<String> usTimeZones = new ArrayList<String>();
        ArrayList<String> otherTimeZones = new ArrayList<String>();
        ArrayList<String> totalTimeZones = new ArrayList<String>();

        for (int z = 0; z < allTimeZones.length; z++) {
            if (allTimeZones[z].contains("US/")) {
                usTimeZones.add(allTimeZones[z]);
            } else {
                otherTimeZones.add(allTimeZones[z]);
            }
        }
        Collections.sort(usTimeZones);
        Collections.sort(otherTimeZones);

        totalTimeZones.add(usTimeZonesStr);
        totalTimeZones.addAll(usTimeZones);
        totalTimeZones.add(otherTimeZonesStr);
        totalTimeZones.addAll(otherTimeZones);
        timeZones = totalTimeZones.toArray();

        initComponents();

        this.branches = new ArrayList<Branch>();
        this.myParent = myParent;
        branchTable.getSelectionModel().addListSelectionListener(new BranchSelectionHandler());

        timeZoneCombo.setSelectedItem("US/Central");

        timeZoneCombo.setRenderer(new DefaultListCellRenderer() {
            
            public Component getListCellRendererComponent(JList list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                
                if (value.equals(usTimeZonesStr) || value.equals(otherTimeZonesStr)) {
                    super.getListCellRendererComponent(list, value, index, false, cellHasFocus);

                    this.setFont(headerFont);
                } else {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    this.setBorder(new EmptyBorder(0, 25, 0, 0));
                }

                return this;
            }
        });

        TableColumn column = null;
        for (int i = 0; i < branchTable.getColumnCount(); i++) {
            column = branchTable.getColumnModel().getColumn(i);
            if (i == 3) {
                column.setPreferredWidth(45); //third column is bigger
            } else if (i == 4) {
                column.setPreferredWidth(50);
            }
        }
    }

    private void showBranchInformation(Branch branch) {
        branchTxt.setText(branch.getBranchName());
        phoneTxt.setText(branch.getBranchInfo().getPhone());
        addressTxt.setText(branch.getBranchInfo().getAddress());
        addressTwoTxt.setText(branch.getBranchInfo().getAddress2());
        cityTxt.setText(branch.getBranchInfo().getCity());
        stateTxt.setText(branch.getBranchInfo().getState());
        zipTxt.setText(branch.getBranchInfo().getZip());
        contactNameTxt.setText(branch.getBranchInfo().getContactName());
        contactPhoneTxt.setText(branch.getBranchInfo().getContactPhone());
        contactEmailTxt.setText(branch.getBranchInfo().getContactEmail());
        try {
            if (branch.getTimezone().equals("CST")) {
                branch.setTimezone("US/Central");
            } else if (branch.getTimezone().equals("EST")) {
                branch.setTimezone("US/Eastern");
            }
            timeZoneCombo.setSelectedItem(branch.getTimezone());
            timeZoneCombo.repaint();
        } catch (Exception e) {
        }

        branchIdTxt.setText(branch.getBranchId().toString());
        try {
            branchInfoIdTxt.setText(branch.getBranchInfo().toString());
        } catch (Exception e) {
        }
    }

    private void clearBranchInformation() {
        branchTxt.setText("");
        phoneTxt.setText("");
        addressTxt.setText("");
        addressTwoTxt.setText("");
        cityTxt.setText("");
        stateTxt.setText("");
        zipTxt.setText("");
        branchIdTxt.setText("");
        branchInfoIdTxt.setText("");
        contactNameTxt.setText("");
        contactPhoneTxt.setText("");
        contactEmailTxt.setText("");

        try {
            timeZoneCombo.setSelectedItem("US/Central");
            timeZoneCombo.repaint();
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

        branchIdTxt = new javax.swing.JLabel();
        branchInfoIdTxt = new javax.swing.JLabel();
        branchPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        branchTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        BranchInfoPanel = new javax.swing.JPanel();
        BranchNamePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        branchTxt = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        deleteBranchButton = new javax.swing.JButton();
        BranchContactPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        phoneTxt = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        addressTxt = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        addressTwoTxt = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        cityTxt = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        stateTxt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        zipTxt = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        contactNameTxt = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        contactPhoneTxt = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        contactEmailTxt = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        AddBranchBtn = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        timeZoneCombo = new javax.swing.JComboBox();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        branchPanel.setMaximumSize(new java.awt.Dimension(32767, 120));
        branchPanel.setMinimumSize(new java.awt.Dimension(0, 120));
        branchPanel.setPreferredSize(new java.awt.Dimension(400, 120));
        branchPanel.setLayout(new java.awt.GridLayout(1, 0));

        branchTable.setModel(branchTableModel);
        jScrollPane1.setViewportView(branchTable);

        branchPanel.add(jScrollPane1);

        add(branchPanel);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        BranchInfoPanel.setLayout(new javax.swing.BoxLayout(BranchInfoPanel, javax.swing.BoxLayout.Y_AXIS));

        BranchNamePanel.setMaximumSize(new java.awt.Dimension(32767, 25));
        BranchNamePanel.setMinimumSize(new java.awt.Dimension(10, 25));
        BranchNamePanel.setPreferredSize(new java.awt.Dimension(350, 25));
        BranchNamePanel.setLayout(new javax.swing.BoxLayout(BranchNamePanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Branch Name    ");
        jLabel1.setMaximumSize(new java.awt.Dimension(120, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(120, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(120, 14));
        BranchNamePanel.add(jLabel1);

        branchTxt.setMaximumSize(new java.awt.Dimension(250, 24));
        branchTxt.setMinimumSize(new java.awt.Dimension(250, 24));
        branchTxt.setPreferredSize(new java.awt.Dimension(250, 24));
        BranchNamePanel.add(branchTxt);
        BranchNamePanel.add(jPanel11);

        deleteBranchButton.setText("Delete Branch");
        deleteBranchButton.setFocusPainted(false);
        deleteBranchButton.setFocusable(false);
        deleteBranchButton.setMaximumSize(new java.awt.Dimension(120, 22));
        deleteBranchButton.setMinimumSize(new java.awt.Dimension(200, 22));
        deleteBranchButton.setPreferredSize(new java.awt.Dimension(120, 23));
        deleteBranchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBranchButtonActionPerformed(evt);
            }
        });
        BranchNamePanel.add(deleteBranchButton);

        BranchInfoPanel.add(BranchNamePanel);

        BranchContactPanel.setLayout(new javax.swing.BoxLayout(BranchContactPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel2.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel2.setPreferredSize(new java.awt.Dimension(350, 25));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jLabel12.setText("Branch Phone");
        jLabel12.setMaximumSize(new java.awt.Dimension(120, 14));
        jLabel12.setMinimumSize(new java.awt.Dimension(120, 14));
        jLabel12.setPreferredSize(new java.awt.Dimension(120, 14));
        jPanel2.add(jLabel12);

        phoneTxt.setMaximumSize(new java.awt.Dimension(250, 24));
        phoneTxt.setMinimumSize(new java.awt.Dimension(250, 24));
        phoneTxt.setPreferredSize(new java.awt.Dimension(250, 24));
        jPanel2.add(phoneTxt);

        BranchContactPanel.add(jPanel2);

        jPanel3.setMaximumSize(new java.awt.Dimension(35000, 25));
        jPanel3.setMinimumSize(new java.awt.Dimension(350, 25));
        jPanel3.setPreferredSize(new java.awt.Dimension(350, 25));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jLabel13.setText("Address Line 1  ");
        jLabel13.setMaximumSize(new java.awt.Dimension(120, 14));
        jLabel13.setMinimumSize(new java.awt.Dimension(120, 14));
        jLabel13.setPreferredSize(new java.awt.Dimension(120, 14));
        jPanel3.add(jLabel13);

        addressTxt.setMaximumSize(new java.awt.Dimension(250, 24));
        addressTxt.setMinimumSize(new java.awt.Dimension(250, 24));
        addressTxt.setPreferredSize(new java.awt.Dimension(250, 24));
        jPanel3.add(addressTxt);

        BranchContactPanel.add(jPanel3);

        jPanel4.setMaximumSize(new java.awt.Dimension(350000, 25));
        jPanel4.setMinimumSize(new java.awt.Dimension(350, 25));
        jPanel4.setPreferredSize(new java.awt.Dimension(350, 25));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jLabel14.setText("Address Line 2  ");
        jLabel14.setMaximumSize(new java.awt.Dimension(120, 14));
        jLabel14.setMinimumSize(new java.awt.Dimension(120, 14));
        jLabel14.setPreferredSize(new java.awt.Dimension(120, 14));
        jPanel4.add(jLabel14);

        addressTwoTxt.setMaximumSize(new java.awt.Dimension(250, 24));
        addressTwoTxt.setMinimumSize(new java.awt.Dimension(250, 24));
        addressTwoTxt.setPreferredSize(new java.awt.Dimension(250, 24));
        jPanel4.add(addressTwoTxt);

        BranchContactPanel.add(jPanel4);

        jPanel5.setMaximumSize(new java.awt.Dimension(33203, 25));
        jPanel5.setMinimumSize(new java.awt.Dimension(446, 25));
        jPanel5.setPreferredSize(new java.awt.Dimension(446, 25));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jLabel15.setText("City                    ");
        jLabel15.setMaximumSize(new java.awt.Dimension(120, 14));
        jLabel15.setMinimumSize(new java.awt.Dimension(120, 14));
        jLabel15.setPreferredSize(new java.awt.Dimension(120, 14));
        jPanel5.add(jLabel15);

        cityTxt.setMaximumSize(new java.awt.Dimension(95, 24));
        cityTxt.setMinimumSize(new java.awt.Dimension(95, 24));
        cityTxt.setPreferredSize(new java.awt.Dimension(95, 24));
        jPanel5.add(cityTxt);

        jLabel16.setText(" State ");
        jPanel5.add(jLabel16);

        stateTxt.setMaximumSize(new java.awt.Dimension(35, 24));
        stateTxt.setMinimumSize(new java.awt.Dimension(35, 24));
        stateTxt.setPreferredSize(new java.awt.Dimension(35, 24));
        jPanel5.add(stateTxt);

        jLabel17.setText(" Zip ");
        jPanel5.add(jLabel17);

        zipTxt.setMaximumSize(new java.awt.Dimension(64, 24));
        zipTxt.setMinimumSize(new java.awt.Dimension(64, 24));
        zipTxt.setPreferredSize(new java.awt.Dimension(64, 24));
        jPanel5.add(zipTxt);
        jPanel5.add(jPanel6);

        BranchContactPanel.add(jPanel5);

        jPanel10.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel10.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel10.setPreferredSize(new java.awt.Dimension(350, 25));
        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.LINE_AXIS));

        jLabel19.setText("Scheduling Manager");
        jLabel19.setMaximumSize(new java.awt.Dimension(120, 14));
        jLabel19.setMinimumSize(new java.awt.Dimension(120, 14));
        jLabel19.setPreferredSize(new java.awt.Dimension(120, 14));
        jPanel10.add(jLabel19);

        contactNameTxt.setMaximumSize(new java.awt.Dimension(250, 24));
        contactNameTxt.setMinimumSize(new java.awt.Dimension(250, 24));
        contactNameTxt.setPreferredSize(new java.awt.Dimension(250, 24));
        jPanel10.add(contactNameTxt);

        BranchContactPanel.add(jPanel10);

        jPanel12.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel12.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel12.setPreferredSize(new java.awt.Dimension(350, 25));
        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.LINE_AXIS));

        jLabel20.setText("Manager Phone");
        jLabel20.setMaximumSize(new java.awt.Dimension(120, 14));
        jLabel20.setMinimumSize(new java.awt.Dimension(120, 14));
        jLabel20.setPreferredSize(new java.awt.Dimension(120, 14));
        jPanel12.add(jLabel20);

        contactPhoneTxt.setMaximumSize(new java.awt.Dimension(250, 24));
        contactPhoneTxt.setMinimumSize(new java.awt.Dimension(250, 24));
        contactPhoneTxt.setPreferredSize(new java.awt.Dimension(250, 24));
        jPanel12.add(contactPhoneTxt);

        BranchContactPanel.add(jPanel12);

        jPanel13.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel13.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel13.setPreferredSize(new java.awt.Dimension(350, 25));
        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.LINE_AXIS));

        jLabel21.setText("Manager Email");
        jLabel21.setMaximumSize(new java.awt.Dimension(120, 14));
        jLabel21.setMinimumSize(new java.awt.Dimension(120, 14));
        jLabel21.setPreferredSize(new java.awt.Dimension(120, 14));
        jPanel13.add(jLabel21);

        contactEmailTxt.setMaximumSize(new java.awt.Dimension(250, 24));
        contactEmailTxt.setMinimumSize(new java.awt.Dimension(250, 24));
        contactEmailTxt.setPreferredSize(new java.awt.Dimension(250, 24));
        jPanel13.add(contactEmailTxt);
        jPanel13.add(jPanel9);

        AddBranchBtn.setText("Save Branch");
        AddBranchBtn.setFocusPainted(false);
        AddBranchBtn.setFocusable(false);
        AddBranchBtn.setMaximumSize(new java.awt.Dimension(120, 23));
        AddBranchBtn.setMinimumSize(new java.awt.Dimension(200, 23));
        AddBranchBtn.setPreferredSize(new java.awt.Dimension(120, 23));
        AddBranchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddBranchBtnActionPerformed(evt);
            }
        });
        jPanel13.add(AddBranchBtn);

        BranchContactPanel.add(jPanel13);

        jPanel7.setMaximumSize(new java.awt.Dimension(90000, 25));
        jPanel7.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel7.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        jLabel18.setText("TimeZone");
        jLabel18.setMaximumSize(new java.awt.Dimension(120, 14));
        jLabel18.setMinimumSize(new java.awt.Dimension(120, 14));
        jLabel18.setPreferredSize(new java.awt.Dimension(120, 14));
        jPanel7.add(jLabel18);

        timeZoneCombo.setModel(new TimeZoneModel());
        timeZoneCombo.setMaximumSize(new java.awt.Dimension(250, 32767));
        timeZoneCombo.setMinimumSize(new java.awt.Dimension(250, 26));
        timeZoneCombo.setPreferredSize(new java.awt.Dimension(250, 26));
        jPanel7.add(timeZoneCombo);

        BranchContactPanel.add(jPanel7);

        BranchInfoPanel.add(BranchContactPanel);

        jPanel1.add(BranchInfoPanel);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void deleteBranchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBranchButtonActionPerformed
        int idx = this.branchTable.getSelectedRow();
        Branch branch = branchTableModel.getBranchInfoAt(idx);
        if (idx >= 0 && idx < this.branchTable.getRowCount()) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + branch.getBranchName() + "?", "Confirm Branch Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    this.myparent.getConnection().executeUpdate(new GenericQuery("DELETE FROM control_db.branch WHERE branch_id = " + branch.getBranchId()));
                    this.myparent.getData();
                } catch (Exception ex) {
                }
            }
        }
}//GEN-LAST:event_deleteBranchButtonActionPerformed

    private void AddBranchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddBranchBtnActionPerformed
        Branch saveBranch = new Branch();
        saveBranch.setBranchName(branchTxt.getText());
        try {
            saveBranch.setBranchId(Integer.parseInt(branchIdTxt.getText()));
        } catch (Exception e) {
        }
        try {
            saveBranch.setBranchManagementId((short) Integer.parseInt(myparent.getMyIdForSave()));
        } catch (Exception e) {
        }
        saveBranch.setTimezone(timeZoneCombo.getSelectedItem().toString());

        BranchInfo infoBranch = new BranchInfo();
        try {
            infoBranch.setBranchId(Integer.parseInt(branchInfoIdTxt.getText()));
        } catch (Exception e) {
        }
        infoBranch.setAddress(addressTxt.getText());
        infoBranch.setAddress2(addressTwoTxt.getText());
        infoBranch.setCity(cityTxt.getText());
        infoBranch.setState(stateTxt.getText());
        infoBranch.setZip(zipTxt.getText());
        infoBranch.setPhone(phoneTxt.getText());
        infoBranch.setContactEmail(contactEmailTxt.getText());
        infoBranch.setContactName(contactNameTxt.getText());
        infoBranch.setContactPhone(contactPhoneTxt.getText());

        save_branch_query saveQuery = new save_branch_query();
        Connection myConn = new Connection();
        if (saveBranch.getBranchId() == null || saveBranch.getBranchId().intValue() == 0) {
            get_next_branch_seq_query nextVal = new get_next_branch_seq_query();
            nextVal.setPreparedStatement(new Object[]{});
            Record_Set rst = myConn.executeQuery(nextVal);
            saveBranch.setBranchId(rst.getInt("next_val"));

            saveQuery.setBranch(saveBranch, true);
        } else {
            saveQuery.setBranch(saveBranch, false);
        }
        myConn.executeQuery(saveQuery);

        infoBranch.setBranchId(saveBranch.getBranchId());
        
        save_branch_information_query saveBranchInfo = new save_branch_information_query();
        saveBranchInfo.update(infoBranch);
        myConn.executeUpdate(saveBranchInfo);
        
        clearBranchInformation();
        JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                "Branch Information Saved!", "Saved", JOptionPane.INFORMATION_MESSAGE);
}//GEN-LAST:event_AddBranchBtnActionPerformed

    @Override
    public GeneralQueryFormat getQuery(boolean isSelected) {
        get_branch_by_management_query myBranchQuery = new get_branch_by_management_query();
        myBranchQuery.update(myParent.getMyIdForSave());
        return myBranchQuery;
    }

    @Override
    public GeneralQueryFormat getSaveQuery(boolean isNewData) {
        return null;
    }

    @Override
    public void loadData(Record_Set branch) {
        this.branches = new ArrayList<Branch>();
        for (int i = 0; i < branch.length(); i++) {
            this.branches.add(new Branch(branch));
            branch.moveNext();
        }
        try {
            ((BranchTableModel) this.branchTable.getModel()).fireTableDataChanged();
        } catch (Exception e) {}
    }

    @Override
    public boolean needsMoreRecordSets() {
        return false;
    }

    @Override
    public String getMyTabTitle() {
        return "Accessible Branches";
    }

    @Override
    public JPanel getMyForm() {
        return this;
    }

    @Override
    public void doOnClear() {
    }

    @Override
    public boolean userHasAccess() {
        return true;
    }

    private class TimeZoneModel implements ComboBoxModel {

        int selectedIndex;

        public void setSelectedItem(Object anItem) {
            for (int i = 0; i < timeZones.length; i++) {
                if (timeZones[i].equals(anItem) && !anItem.equals(usTimeZonesStr) &&
                        !anItem.equals(otherTimeZonesStr)) {
                    selectedIndex = i;
                }
            }
        }

        public Object getSelectedItem() {
            return timeZones[selectedIndex];
        }

        public int getSize() {
            return timeZones.length;
        }

        public Object getElementAt(int index) {
            return timeZones[index];
        }

        public void addListDataListener(ListDataListener l) {
        }

        public void removeListDataListener(ListDataListener l) {
        }
    }

    private class BranchTableModel extends AbstractTableModel {

        @Override
        public String getColumnName(int col) {
            String retVal = "";
            switch (col) {
                case 0:
                    retVal = "Name";
                    break;
                case 1:
                    retVal = "Address";
                    break;
                case 2:
                    retVal = "City";
                    break;
                case 3:
                    retVal = "State";
                    break;
                case 4:
                    retVal = "Zip";
                    break;
                case 5:
                    retVal = "Phone";
                    break;
            }
            return retVal;
        }

        public Branch getBranchInfoAt(int row) {

            return branches.get(row);
        }

        public int getRowCount() {
            return branches.size();
        }

        public int getColumnCount() {
            return 6;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Branch branch = getBranchInfoAt(rowIndex);
            String retVal = "";
            switch (columnIndex) {
                case 0:
                    retVal = branch.getBranchName();
                    break;
                case 1:
                    retVal = branch.getBranchInfo().getAddress();
                    break;
                case 2:
                    retVal = branch.getBranchInfo().getCity();
                    break;
                case 3:
                    retVal = branch.getBranchInfo().getState();
                    break;
                case 4:
                    retVal = branch.getBranchInfo().getZip();
                    break;
                case 5:
                    retVal = branch.getBranchInfo().getPhone();
                    break;
            }
            return retVal;
        }
    }

    private class BranchSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            showBranchInformation(branchTableModel.getBranchInfoAt(branchTable.getSelectedRow()));
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddBranchBtn;
    private javax.swing.JPanel BranchContactPanel;
    private javax.swing.JPanel BranchInfoPanel;
    private javax.swing.JPanel BranchNamePanel;
    private javax.swing.JTextField addressTwoTxt;
    private javax.swing.JTextField addressTxt;
    private javax.swing.JLabel branchIdTxt;
    private javax.swing.JLabel branchInfoIdTxt;
    private javax.swing.JPanel branchPanel;
    private javax.swing.JTable branchTable;
    private javax.swing.JTextField branchTxt;
    private javax.swing.JTextField cityTxt;
    private javax.swing.JTextField contactEmailTxt;
    private javax.swing.JTextField contactNameTxt;
    private javax.swing.JTextField contactPhoneTxt;
    private javax.swing.JButton deleteBranchButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField phoneTxt;
    private javax.swing.JTextField stateTxt;
    private javax.swing.JComboBox timeZoneCombo;
    private javax.swing.JTextField zipTxt;
    // End of variables declaration//GEN-END:variables
}
