/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.schedule.components;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import rmischedule.main.Main_Window;
import rmischedule.personnel.PersonnelChangeForm;
import rmischedule.schedule.ScheduleToolBar;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.data_components.AlertModel;
import rmischeduleserver.control.GenericController;
import rmischeduleserver.control.PersonnelChangeReasonController;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import schedfoxlib.model.PersonnelChange;

/**
 *
 * @author ira
 */
public class AlertDialog extends javax.swing.JDialog {

    private AlertTableModel alertModel = new AlertTableModel();
    private Schedule_View_Panel myParent;
    private ScheduleToolBar.AlertThread alertThread;
    private ScheduleToolBar toolBar;

    /**
     * Creates new form AlertDialog
     */
    public AlertDialog(java.awt.Frame parent, boolean modal, Schedule_View_Panel myParent, ScheduleToolBar.AlertThread alertThread, final ScheduleToolBar toolBar) {
        super(parent, modal);
        initComponents();
        this.myParent = myParent;
        this.toolBar = toolBar;

        centerTable.setRowHeight(35);

        centerTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        centerTable.getColumnModel().getColumn(1).setMaxWidth(120);
        centerTable.getColumnModel().getColumn(1).setMinWidth(120);

        final Schedule_View_Panel iParent = myParent;
        
        centerTable.setDefaultRenderer(JButton.class, new AlertButtonRenderer());
        centerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = centerTable.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / centerTable.getRowHeight();
                if (row < centerTable.getRowCount() && row >= 0) {
                    AlertModel event = alertModel.getAlertAt(row);
                    if (column == 1) {
                        int result = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "Are you sure you want to mark this as dismissed? "
                                + "This should only be used if the employee was a temporary employee and the client does not need to be notifed.", "Confirm?", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            PersonnelChangeReasonController personnelFactory = PersonnelChangeReasonController.getInstance(iParent.getCompany());

                            PersonnelChange change = new PersonnelChange();
                            change.setReasonId(-1);
                            try {
                                change.setClientId(event.getClient().getClientId());
                            } catch (Exception exe) {
                            }
                            try {
                                change.setEmployeeId(event.getEmployee().getEmployeeId());
                            } catch (Exception exe) {
                            }
                            change.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
                            change.setReasonText("");
                            try {
                                Date currTime = new Date(GenericController.getInstance(iParent.getCompany()).getCurrentTimeMillis());
                                change.setDateOfChange(currTime);
                                change.setDateSent(currTime);
                            } catch (Exception exe) {
                                change.setDateOfChange(new Date());
                                change.setDateSent(new Date());
                            }
                            try {
                                personnelFactory.savePersonnelChange(change);
                                Iterator<Employee> empIterator = toolBar.getEmployeeAndClient().keySet().iterator();
                                while (empIterator.hasNext()) {
                                    Employee currEmp = empIterator.next();
                                    if (currEmp.getEmployeeId().equals(event.getEmployee().getEmployeeId())) {
                                        Client currClient = toolBar.getEmployeeAndClient().get(currEmp);
                                        if (currClient.getClientId().equals(event.getClient().getClientId())) {
                                            toolBar.getEmployeeAndClient().remove(currEmp);
                                        }
                                    }
                                }
                            } catch (Exception exe) {
                                exe.printStackTrace();
                            }
                            refreshData();
                        }
                    } else {
                        displayAlertDialog();
                    }
                }
            }
        });
    }

    public void refreshData() {
        ArrayList<AlertModel> alertModels = new ArrayList<AlertModel>();
        Iterator<Employee> empIterator = toolBar.getEmployeeAndClient().keySet().iterator();
        alertModel.clearData();
        while (empIterator.hasNext()) {
            AlertModel alertModel = new AlertModel();
            final Employee emp = empIterator.next();
            final Client client = toolBar.getEmployeeAndClient().get(emp);
            alertModel.setAlertText(emp.getFullName() + " has been removed from " + client.getName());
            alertModel.setClient(client);
            alertModel.setEmployee(emp);
            alertModels.add(alertModel);
        }

        for (int a = 0; a < alertModels.size(); a++) {
            alertModel.addData(alertModels.get(a));
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

        centerScrollPane = new javax.swing.JScrollPane();
        centerTable = new javax.swing.JTable();
        bottomPanel = new javax.swing.JPanel();
        closeBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        centerTable.setModel(alertModel);
        centerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                centerTableMouseClicked(evt);
            }
        });
        centerScrollPane.setViewportView(centerTable);

        getContentPane().add(centerScrollPane);

        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        closeBtn.setText("Close");
        closeBtn.setMaximumSize(new java.awt.Dimension(70, 25));
        closeBtn.setMinimumSize(new java.awt.Dimension(70, 25));
        closeBtn.setPreferredSize(new java.awt.Dimension(70, 25));
        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });
        bottomPanel.add(closeBtn);

        getContentPane().add(bottomPanel);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-644)/2, (screenSize.height-304)/2, 644, 304);
    }// </editor-fold>//GEN-END:initComponents

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeBtnActionPerformed

    private void centerTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_centerTableMouseClicked

    }//GEN-LAST:event_centerTableMouseClicked

    private void displayAlertDialog() {
        try {
            AlertModel model = alertModel.getAlertAt(centerTable.getSelectedRow());
            PersonnelChangeForm personnelForm = new PersonnelChangeForm(Main_Window.parentOfApplication,
                    model.getClient(), model.getEmployee(), Integer.parseInt(myParent.getConnection().myCompany), Integer.parseInt(myParent.getConnection().myBranch), true);
            personnelForm.setVisible(true);
            refreshData();
            alertThread.setRefreshSoon();
        } catch (Exception exe) {
        }
    }

    private class AlertButtonRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object color,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (column == 1) {
                return new JButton("Dismiss");
            }
            return super.getTableCellRendererComponent(table, color, isSelected, hasFocus, row, column);
        }
    }

    private class AlertTableModel extends AbstractTableModel {

        private ArrayList<AlertModel> alertData;

        public AlertTableModel() {
            alertData = new ArrayList<AlertModel>();
        }

        public AlertModel getAlertAt(int row) {
            return alertData.get(row);
        }

        public void clearData() {
            alertData.clear();
            super.fireTableDataChanged();
        }

        public void addData(AlertModel alertModel) {
            alertData.add(alertModel);
            super.fireTableDataChanged();
        }
        
        public void removeDataAt(int row) {
            try {
                alertData.remove(row);
                super.fireTableDataChanged();
            } catch (Exception exe) {}
        }

        public int getRowCount() {
            return alertData.size();
        }

        public int getColumnCount() {
            return 2;
        }

        @Override
        public Class<?> getColumnClass(int col) {
            if (col == 0) {
                return String.class;
            } else {
                return JButton.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 0) {
                return false;
            }
            return true;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            AlertModel alertModel = alertData.get(rowIndex);
            if (columnIndex == 0) {
                return alertModel.getAlertText();
            } else {
                return alertModel.getAlertAction();
            }
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex == 0) {
                return "Alert";
            } else {
                return "Action";
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JScrollPane centerScrollPane;
    private javax.swing.JTable centerTable;
    private javax.swing.JButton closeBtn;
    // End of variables declaration//GEN-END:variables
}
