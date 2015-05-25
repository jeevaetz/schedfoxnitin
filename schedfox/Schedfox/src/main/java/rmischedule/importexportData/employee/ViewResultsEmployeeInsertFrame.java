/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ViewResultsEmployeeInsertFrame.java
 *
 * Created on Sep 17, 2010, 9:38:29 AM
 */

package rmischedule.importexportData.employee;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.Employee;

/**
 *
 * @author user
 */
public class ViewResultsEmployeeInsertFrame extends javax.swing.JInternalFrame {

    private ArrayList<Employee> insertedEmployees;
    private ArrayList<Employee> updatedEmployees;
    private ArrayList<Employee> errorEmployees;

    /** Creates new form ViewResultsEmployeeInsertFrame */
    public ViewResultsEmployeeInsertFrame(ArrayList<Employee> insertedEmployees, ArrayList<Employee> updatedEmployees,
            ArrayList<Employee> errorEmployees) {
        this.insertedEmployees = insertedEmployees;
        this.updatedEmployees = updatedEmployees;
        this.errorEmployees = errorEmployees;

        initComponents();
    }

    private class EmployeeTableModel extends AbstractTableModel {
        private ArrayList<Employee> emps;

        public EmployeeTableModel(ArrayList<Employee> emps) {
            this.emps = emps;
        }

        public int getRowCount() {
            return this.emps.size();
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex == 0) {
                return "First Name";
            } else if (columnIndex == 1) {
                return "MI";
            } else if (columnIndex == 2) {
                return "Last Name";
            } else if (columnIndex == 3) {
                return "Phone";
            } else {
                return "Cell";
            }
        }

        public int getColumnCount() {
            return 5;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Employee currEmployee = emps.get(rowIndex);
            if (columnIndex == 0) {
                return currEmployee.getEmployeeFirstName();
            } else if (columnIndex == 1) {
                return currEmployee.getEmployeeMiddleInitial();
            } else if (columnIndex == 2) {
                return currEmployee.getEmployeeLastName();
            } else if (columnIndex == 3) {
                return currEmployee.getEmployeePhone();
            } else {
                return currEmployee.getEmployeeCell();
            }
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

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        closeBtn = new javax.swing.JButton();

        setTitle("Import Results");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Inserted Employees"));
        jPanel2.setLayout(new java.awt.GridLayout());

        jTable1.setModel(new EmployeeTableModel(insertedEmployees));
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1);

        getContentPane().add(jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Updated Employees"));
        jPanel3.setLayout(new java.awt.GridLayout());

        jTable2.setModel(new EmployeeTableModel(updatedEmployees));
        jScrollPane2.setViewportView(jTable2);

        jPanel3.add(jScrollPane2);

        getContentPane().add(jPanel3);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Errors (The following could not be inserted)"));
        jPanel1.setLayout(new java.awt.GridLayout());

        jTable3.setModel(new EmployeeTableModel(errorEmployees));
        jScrollPane3.setViewportView(jTable3);

        jPanel1.add(jScrollPane3);

        getContentPane().add(jPanel1);

        jPanel4.setMinimumSize(new java.awt.Dimension(100, 35));
        jPanel4.setPreferredSize(new java.awt.Dimension(709, 35));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        closeBtn.setText("Close");
        closeBtn.setMaximumSize(new java.awt.Dimension(70, 23));
        closeBtn.setMinimumSize(new java.awt.Dimension(70, 23));
        closeBtn.setPreferredSize(new java.awt.Dimension(70, 23));
        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });
        jPanel4.add(closeBtn);

        getContentPane().add(jPanel4);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-719)/2, (screenSize.height-456)/2, 719, 456);
    }// </editor-fold>//GEN-END:initComponents

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables

}
