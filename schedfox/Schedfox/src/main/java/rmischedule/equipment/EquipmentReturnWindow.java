/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EquipmentReturnWindow.java
 *
 * Created on May 27, 2011, 9:51:51 AM
 */
package rmischedule.equipment;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import rmischedule.components.jcalendar.JCalendarComboBox;
import rmischedule.main.Main_Window;
import rmischedule.security.User;
import rmischeduleserver.control.EquipmentController;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Company;
import schedfoxlib.model.EmployeeEquipment;
import schedfoxlib.model.Equipment;

/**
 *
 * @author user
 */
public class EquipmentReturnWindow extends javax.swing.JInternalFrame implements EquipmentRefreshWindow {

    private EquipmentComboModel equipmentComboModel = new EquipmentComboModel();
    private EmployeeEquipmentModel equipmentModel = new EmployeeEquipmentModel(this);

    private String tempCompany;
    private HashMap<Integer, JCheckBox> branchChecks;
    private HashMap<Integer, Branch> branches;
    
    private JCalendarComboBox begCal;
    private JCalendarComboBox endCal;

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    /**
     * Creates new form EquipmentReturnWindow
     */
    public EquipmentReturnWindow() {
        initComponents();

        this.getRootPane().setDefaultButton(searchBtn);
        
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.MONTH, -12);
        
        begCal = new JCalendarComboBox(startCalendar);
        endCal = new JCalendarComboBox(endCalendar);
        
        startPanel.add(begCal);
        endPanel.add(endCal);

        equipmentTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        equipmentTable.getColumnModel().getColumn(0).setMinWidth(100);
        equipmentTable.getColumnModel().getColumn(0).setMaxWidth(100);

        equipmentTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        equipmentTable.getColumnModel().getColumn(2).setMinWidth(100);
        equipmentTable.getColumnModel().getColumn(2).setMaxWidth(100);

        equipmentTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        equipmentTable.getColumnModel().getColumn(3).setMinWidth(120);
        equipmentTable.getColumnModel().getColumn(3).setMaxWidth(120);

        equipmentTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        equipmentTable.getColumnModel().getColumn(4).setMinWidth(70);
        equipmentTable.getColumnModel().getColumn(4).setMaxWidth(70);

        equipmentTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        equipmentTable.getColumnModel().getColumn(5).setMinWidth(80);
        equipmentTable.getColumnModel().getColumn(5).setMaxWidth(80);

        equipmentTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        equipmentTable.getColumnModel().getColumn(6).setMinWidth(80);
        equipmentTable.getColumnModel().getColumn(6).setMaxWidth(80);

        equipmentTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        equipmentTable.getColumnModel().getColumn(7).setMinWidth(80);
        equipmentTable.getColumnModel().getColumn(7).setMaxWidth(80);

        equipmentTable.getColumnModel().getColumn(8).setPreferredWidth(80);
        equipmentTable.getColumnModel().getColumn(8).setMinWidth(80);
        equipmentTable.getColumnModel().getColumn(8).setMaxWidth(80);

        equipmentTable.getColumnModel().getColumn(4).setCellRenderer(new CurrencyTableCellRenderer());

    }

    public void setInformation(String tempCompany) {
        this.tempCompany = tempCompany;

        this.setTitle("Equipment Return");

        equipmentModel.setCompanyId(tempCompany);

        Vector<Company> companies = Main_Window.parentOfApplication.getActiveListOfCompanies();
        branchChecks = new HashMap<Integer, JCheckBox>();
        branches = new HashMap<Integer, Branch>();
        branchPanel.removeAll();
        final JCheckBox allChecks = new JCheckBox("Select / Deselect All");
        allChecks.setSelected(true);
        allChecks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator<Integer> keys = branchChecks.keySet().iterator();
                while (keys.hasNext()) {
                    JCheckBox key = branchChecks.get(keys.next());
                    key.setSelected(allChecks.isSelected());
                }
                refreshData();
            }
        });
        branchPanel.add(allChecks);

        for (int c = 0; c < companies.size(); c++) {
            if (companies.get(c).getCompId().equals(tempCompany)) {
                Vector<Branch> tmpBranches = companies.get(c).getBranches();
                for (int b = 0; b < tmpBranches.size(); b++) {
                    Branch currBranch = tmpBranches.get(b);
                    if (branchChecks.get(currBranch.getBranchId()) == null) {
                        JCheckBox myCheck = new JCheckBox(currBranch.getBranchName());
                        branchChecks.put(currBranch.getBranchId(), myCheck);
                        branches.put(currBranch.getBranchId(), currBranch);
                        myCheck.setSelected(true);
                        myCheck.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                refreshData();
                            }
                        });
                        branchPanel.add(myCheck);
                    }
                }
            }
        }

        this.refreshData();

        try {
            equipTypeCombo.setSelectedIndex(0);
        } catch (Exception e) {
        }
    }

    private void refreshData() {
        EquipmentController equipmentController = EquipmentController.getInstance(tempCompany);
        equipmentModel.setEquipmentController(equipmentController);
        equipmentComboModel.clearItems();
        try {
            ArrayList<Integer> selectedBranches = new ArrayList<Integer>();
            Iterator<Integer> keys = this.branchChecks.keySet().iterator();
            while (keys.hasNext()) {
                Integer key = keys.next();
                if (this.branchChecks.get(key).isSelected()) {
                    selectedBranches.add(key);
                }
            }

            ArrayList<Equipment> equipment = equipmentController.getEquipment();
            for (int e = 0; e < equipment.size(); e++) {
                equipmentComboModel.addItem(equipment.get(e));
            }
            int selectedEquipment = 0;
            Object sel = equipmentComboModel.getSelectedItem();
            if (sel instanceof Equipment) {
                selectedEquipment = ((Equipment) sel).getEquipmentId();
            }
            String search = null;
            if (filterTxt.getText().trim().length() > 0) {
                search = filterTxt.getText();
            }
            Integer numContacts = -1;
            try {
                numContacts = (Integer)numberSpin.getValue();
                if (!filterByChk.isSelected()) {
                    numContacts = -1;
                }
            } catch (Exception exe) {}
            
            
            ArrayList<EmployeeEquipment> empEquipment
                    = equipmentController.getEmployeeEquipment(
                            selectedEquipment, search, showAllChk.isSelected(), selectedBranches, 
                            begCal.getCalendar().getTime(), endCal.getCalendar().getTime(),
                            numContacts);
            equipmentModel.clearEquipment();
            for (int e = 0; e < empEquipment.size(); e++) {
                equipmentModel.addEquipment(empEquipment.get(e));
            }

            numberOfEquipmentLbl.setText("Number of employees: " + empEquipment.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        equipmentTable = new javax.swing.JTable();
        controlPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        equipTypeCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        filterTxt = new javax.swing.JTextField();
        showAllChk = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        startPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        endPanel = new javax.swing.JPanel();
        filterByChk = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        numberSpin = new javax.swing.JSpinner();
        jPanel4 = new javax.swing.JPanel();
        searchBtn = new javax.swing.JButton();
        branchPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        numberOfEquipmentLbl = new javax.swing.JLabel();
        selectAllChk = new javax.swing.JCheckBox();
        emailSelectedEmployeesBtn = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setMaximizable(true);
        setResizable(true);
        setTitle("Equipment Return Window");

        mainPanel.setLayout(new java.awt.GridLayout(1, 0));

        equipmentTable.setAutoCreateRowSorter(true);
        equipmentTable.setModel(equipmentModel);
        equipmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                equipmentTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(equipmentTable);

        mainPanel.add(jScrollPane1);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        controlPanel.setMaximumSize(new java.awt.Dimension(2147483647, 145));
        controlPanel.setMinimumSize(new java.awt.Dimension(100, 145));
        controlPanel.setPreferredSize(new java.awt.Dimension(582, 145));
        controlPanel.setLayout(new javax.swing.BoxLayout(controlPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Equipment Type");
        jLabel1.setMaximumSize(new java.awt.Dimension(110, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(110, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(110, 16));
        jPanel2.add(jLabel1);

        equipTypeCombo.setModel(equipmentComboModel);
        equipTypeCombo.setMaximumSize(new java.awt.Dimension(140, 26));
        equipTypeCombo.setMinimumSize(new java.awt.Dimension(140, 22));
        equipTypeCombo.setPreferredSize(new java.awt.Dimension(140, 22));
        equipTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                equipTypeComboActionPerformed(evt);
            }
        });
        jPanel2.add(equipTypeCombo);

        jLabel2.setText("Filter");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 0));
        jLabel2.setMaximumSize(new java.awt.Dimension(50, 16));
        jLabel2.setMinimumSize(new java.awt.Dimension(50, 16));
        jLabel2.setPreferredSize(new java.awt.Dimension(50, 16));
        jPanel2.add(jLabel2);

        filterTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        filterTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                filterTxtKeyTyped(evt);
            }
        });
        jPanel2.add(filterTxt);

        showAllChk.setText("Show employees with returned uniforms");
        showAllChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAllChkActionPerformed(evt);
            }
        });
        jPanel2.add(showAllChk);

        controlPanel.add(jPanel2);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setText("Start Date");
        jLabel3.setMaximumSize(new java.awt.Dimension(110, 16));
        jLabel3.setMinimumSize(new java.awt.Dimension(110, 16));
        jLabel3.setPreferredSize(new java.awt.Dimension(110, 16));
        jPanel3.add(jLabel3);

        startPanel.setMaximumSize(new java.awt.Dimension(140, 28));
        startPanel.setMinimumSize(new java.awt.Dimension(140, 28));
        startPanel.setPreferredSize(new java.awt.Dimension(140, 28));
        startPanel.setLayout(new java.awt.GridLayout(1, 0));
        jPanel3.add(startPanel);

        jLabel4.setText("End Date");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 0));
        jLabel4.setMaximumSize(new java.awt.Dimension(110, 16));
        jLabel4.setMinimumSize(new java.awt.Dimension(110, 16));
        jLabel4.setPreferredSize(new java.awt.Dimension(110, 16));
        jPanel3.add(jLabel4);

        endPanel.setMaximumSize(new java.awt.Dimension(140, 28));
        endPanel.setMinimumSize(new java.awt.Dimension(140, 28));
        endPanel.setPreferredSize(new java.awt.Dimension(140, 28));
        endPanel.setLayout(new java.awt.GridLayout(1, 0));
        jPanel3.add(endPanel);

        filterByChk.setText("Filter by ");
        filterByChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterByChkActionPerformed(evt);
            }
        });
        jPanel3.add(filterByChk);

        jLabel5.setText("# of messages sent");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 0));
        jLabel5.setMaximumSize(new java.awt.Dimension(130, 16));
        jLabel5.setMinimumSize(new java.awt.Dimension(130, 16));
        jLabel5.setPreferredSize(new java.awt.Dimension(130, 16));
        jPanel3.add(jLabel5);

        numberSpin.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        numberSpin.setEnabled(false);
        numberSpin.setMaximumSize(new java.awt.Dimension(50, 26));
        numberSpin.setMinimumSize(new java.awt.Dimension(50, 22));
        numberSpin.setPreferredSize(new java.awt.Dimension(50, 22));
        jPanel3.add(numberSpin);

        jPanel4.setMaximumSize(new java.awt.Dimension(2000, 10));
        jPanel3.add(jPanel4);

        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });
        jPanel3.add(searchBtn);

        controlPanel.add(jPanel3);

        branchPanel.setMaximumSize(new java.awt.Dimension(70000, 25100));
        branchPanel.setLayout(new java.awt.GridLayout(3, 0));
        controlPanel.add(branchPanel);

        getContentPane().add(controlPanel, java.awt.BorderLayout.PAGE_START);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 35));
        jPanel1.setMinimumSize(new java.awt.Dimension(10, 35));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 35));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        numberOfEquipmentLbl.setMaximumSize(new java.awt.Dimension(70000, 16));
        jPanel1.add(numberOfEquipmentLbl);

        selectAllChk.setText("Select / Deselect All Employees");
        selectAllChk.setMaximumSize(new java.awt.Dimension(230, 25));
        selectAllChk.setMinimumSize(new java.awt.Dimension(230, 25));
        selectAllChk.setPreferredSize(new java.awt.Dimension(230, 25));
        selectAllChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllChkActionPerformed(evt);
            }
        });
        jPanel1.add(selectAllChk);

        emailSelectedEmployeesBtn.setText("Message Selected Employees ");
        emailSelectedEmployeesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailSelectedEmployeesBtnActionPerformed(evt);
            }
        });
        jPanel1.add(emailSelectedEmployeesBtn);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        setBounds(0, 0, 880, 481);
    }// </editor-fold>//GEN-END:initComponents

    private void equipTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_equipTypeComboActionPerformed
        this.refreshData();
    }//GEN-LAST:event_equipTypeComboActionPerformed

    private void filterTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filterTxtKeyTyped

    }//GEN-LAST:event_filterTxtKeyTyped

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        this.refreshData();
    }//GEN-LAST:event_searchBtnActionPerformed

    private void showAllChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAllChkActionPerformed
        this.refreshData();
    }//GEN-LAST:event_showAllChkActionPerformed

    private void equipmentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_equipmentTableMouseClicked
        EmployeeEquipment empEquip = equipmentModel.getEmployeeEquipmentAt(equipmentTable.getSelectedRow());
        if (evt.getClickCount() > 1) {
            Main_Window.parentOfApplication.
                    getEmployeeEditWindow().setInformation(tempCompany, empEquip.getEmployee(tempCompany).getBranchId() + "", empEquip.getEmployeeId() + "");
            if (empEquip.getEmployee(tempCompany).getEmployeeIsDeleted() == 1) {
                Main_Window.parentOfApplication.getEmployeeEditWindow().showDeleted(true);
            } else {
                Main_Window.parentOfApplication.getEmployeeEditWindow().showDeleted(false);
            }
            Main_Window.getEmployeeEditWindow().setVisible(true);
        }
    }//GEN-LAST:event_equipmentTableMouseClicked

    private void emailSelectedEmployeesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailSelectedEmployeesBtnActionPerformed
        ArrayList<EmployeeEquipment> employeeEquip = this.equipmentModel.getEquipmentToBeEmailed();
        ArrayList<EmployeeEquipment> validEmployeeEquip = new ArrayList<EmployeeEquipment>();
        for (int e = 0; e < employeeEquip.size(); e++) {
            if (employeeEquip.get(e).getDateReturned() == null) {
                validEmployeeEquip.add(employeeEquip.get(e));
            }
        }

        int numberOfInvalidEntries = employeeEquip.size() - validEmployeeEquip.size();
        int numberOfValidEntries = validEmployeeEquip.size();
        if (numberOfValidEntries - numberOfInvalidEntries != numberOfValidEntries) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "You have selected " + numberOfInvalidEntries + " officers that have returned their uniforms\r\n"
                    + "We are not including them on the emails and will instead be sending to " + numberOfValidEntries + " officers.", "Invalid Officers Selected!", JOptionPane.ERROR_MESSAGE);
        }
        if (numberOfValidEntries > 0) {
            EquipmentTemplateSelectionWindow equipmentTemplateWindow = new EquipmentTemplateSelectionWindow(Main_Window.parentOfApplication, true, tempCompany, validEmployeeEquip);
            equipmentTemplateWindow.setVisible(true);
        }
    }//GEN-LAST:event_emailSelectedEmployeesBtnActionPerformed

    private void selectAllChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllChkActionPerformed
        equipmentModel.toggleSelected(selectAllChk.isSelected());
    }//GEN-LAST:event_selectAllChkActionPerformed

    private void filterByChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterByChkActionPerformed
        numberSpin.setEnabled(filterByChk.isSelected());
    }//GEN-LAST:event_filterByChkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel branchPanel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JButton emailSelectedEmployeesBtn;
    private javax.swing.JPanel endPanel;
    private javax.swing.JComboBox equipTypeCombo;
    private javax.swing.JTable equipmentTable;
    private javax.swing.JCheckBox filterByChk;
    private javax.swing.JTextField filterTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel numberOfEquipmentLbl;
    private javax.swing.JSpinner numberSpin;
    private javax.swing.JButton searchBtn;
    private javax.swing.JCheckBox selectAllChk;
    private javax.swing.JCheckBox showAllChk;
    private javax.swing.JPanel startPanel;
    // End of variables declaration//GEN-END:variables

    public void refreshEquipmentData() {
        this.refreshData();
    }

    public void displayEquipmentReturnWindow(EmployeeEquipment empEquip, String companyId) {
        EquipmentReturnConditionDialog equipmentReturnDialog
                = new EquipmentReturnConditionDialog(Main_Window.parentOfApplication,
                        true, empEquip, companyId);
        equipmentReturnDialog.setVisible(true);
    }

    @Override
    public HashMap<Integer, Branch> getBranches() {
        return this.branches;
    }

    private class CurrencyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component res = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof BigDecimal) {
                setHorizontalAlignment(JLabel.RIGHT);
                setText(currencyFormat.format(value));
            }
            return res;
        }
    }

}
