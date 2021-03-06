/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ViewResultsEmployeeInsertFrame.java
 *
 * Created on Sep 17, 2010, 9:38:29 AM
 */

package rmischedule.importexportData.client;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.Client;

/**
 *
 * @author user
 */
public class ViewResultsClientInsertFrame extends javax.swing.JInternalFrame {

    private ArrayList<Client> insertedClients;
    private ArrayList<Client> updatedClients;
    private ArrayList<Client> errorClients;

    /** Creates new form ViewResultsEmployeeInsertFrame */
    public ViewResultsClientInsertFrame(ArrayList<Client> insertedClients, ArrayList<Client> updatedClients,
            ArrayList<Client> errorClients) {
        this.insertedClients = insertedClients;
        this.updatedClients = updatedClients;
        this.errorClients = errorClients;

        initComponents();
    }

    private class ClientTableModel extends AbstractTableModel {
        private ArrayList<Client> clis;

        public ClientTableModel(ArrayList<Client> clis) {
            this.clis = clis;
        }

        public int getRowCount() {
            return this.clis.size();
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex == 0) {
                return "Name";
            } else if (columnIndex == 1) {
                return "Phone";
            } else if (columnIndex == 2) {
                return "Phone 2";
            } else if (columnIndex == 3) {
                return "Address";
            } else if (columnIndex == 4) {
                return "City";
            } else if (columnIndex == 5) {
                return "State";
            } else {
                return "Zip";
            }
        }

        public int getColumnCount() {
            return 7;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Client currClient = clis.get(rowIndex);
            if (columnIndex == 0) {
                return currClient.getClientName();
            } else if (columnIndex == 1) {
                return currClient.getClientPhone();
            } else if (columnIndex == 2) {
                return currClient.getClientPhone2();
            } else if (columnIndex == 3) {
                return currClient.getClientAddress();
            } else if (columnIndex == 4) {
                return currClient.getClientCity();
            } else if (columnIndex == 5) {
                return currClient.getClientState();
            } else {
                return currClient.getClientZip();
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Inserted Clients"));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jTable1.setModel(new ClientTableModel(insertedClients));
        jScrollPane1.setViewportView(jTable1);

        jPanel2.add(jScrollPane1);

        getContentPane().add(jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Updated Clients"));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jTable2.setModel(new ClientTableModel(updatedClients));
        jScrollPane2.setViewportView(jTable2);

        jPanel3.add(jScrollPane2);

        getContentPane().add(jPanel3);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Errors (The following could not be inserted)"));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jTable3.setModel(new ClientTableModel(errorClients));
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
