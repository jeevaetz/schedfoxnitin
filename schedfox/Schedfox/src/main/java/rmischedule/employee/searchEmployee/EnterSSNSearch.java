/*
 * EnterSSNSearch.java
 *
 * Created on January 6, 2006, 12:39 PM
 */

package rmischedule.employee.searchEmployee;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Company;
import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;

import rmischedule.employee.*;
import rmischedule.data_connection.*;
import rmischedule.main.*;
import rmischedule.components.*;

import java.awt.event.*;
import java.util.*;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
/**
 *
 * @author  Ira Juneau
 */
public class EnterSSNSearch extends javax.swing.JDialog {
    
    private Connection myConn;
    private xEmployeeEdit myparent;
    private List_View myReturnedEmployees;
    
    private MaskFormatter dateMask;
    
    /** Creates new form EnterSSNSearch */
    public EnterSSNSearch(java.awt.Frame parent, boolean modal, Connection parentConn, xEmployeeEdit myParent) {
        super(parent, modal);
        
        try {
            dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
        } catch (Exception e) {}

        initComponents();
        myReturnedEmployees = new List_View();
        myReturnedEmployees.addColumn("id", List_View.STRING, false, false);
        myReturnedEmployees.addColumn("cid", List_View.STRING, false, false);
        myReturnedEmployees.addColumn("bid", List_View.STRING, false, false);
        myReturnedEmployees.addColumn("del", List_View.STRING, false, false);
        myReturnedEmployees.addColumn("Company", List_View.STRING);
        myReturnedEmployees.addColumn("Branch", List_View.STRING);
        myReturnedEmployees.addColumn("First Name", List_View.STRING);
        myReturnedEmployees.addColumn("Last Name", List_View.STRING);
        myReturnedEmployees.buildTable();
        myReturnedEmployees.addMouseListener(new mySelectionListener());
        resultsPane.add(myReturnedEmployees.myScrollPane);
        myConn = parentConn;
        myparent = myParent;

        

        this.getRootPane().setDefaultButton(FindEmps);
        
    }
    
    private class mySelectionListener extends MouseAdapter {
        public mySelectionListener() {
            
        }
        
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 1) {
                if (((String)myReturnedEmployees.getTrueValueAt(myReturnedEmployees.getSelectedRow(), 3)).equals("1")) {
                    myparent.deleted = "1";
                } else {
                    myparent.deleted = "0";
                }
                myparent.setInformation(((String)myReturnedEmployees.getTrueValueAt(myReturnedEmployees.getSelectedRow(), 1)),
                        ((String)myReturnedEmployees.getTrueValueAt(myReturnedEmployees.getSelectedRow(), 2)),
                        (String)myReturnedEmployees.getTrueValueAt(myReturnedEmployees.getSelectedRow(), 0));
                myparent.setVisible(true);
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myTabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        firstTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lastTxt = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        ssnTxt = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        hireTxt = new JFormattedTextField(dateMask);
        jLabel6 = new javax.swing.JLabel();
        termTxt = new JFormattedTextField(dateMask);
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cityTxt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        zipTxt = new javax.swing.JFormattedTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        phoneTxt = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        emailTxt = new javax.swing.JFormattedTextField();
        FindEmps = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        resultsPane = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Search Employees");
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel3.setMaximumSize(new java.awt.Dimension(0, 200));
        jPanel3.setMinimumSize(new java.awt.Dimension(0, 100));
        jPanel3.setPreferredSize(new java.awt.Dimension(503, 150));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jPanel5.setMaximumSize(new java.awt.Dimension(2147483647, 32));
        jPanel5.setMinimumSize(new java.awt.Dimension(146, 32));
        jPanel5.setPreferredSize(new java.awt.Dimension(503, 32));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("First Name");
        jLabel2.setMaximumSize(new java.awt.Dimension(90, 14));
        jLabel2.setMinimumSize(new java.awt.Dimension(90, 14));
        jLabel2.setPreferredSize(new java.awt.Dimension(90, 14));
        jPanel5.add(jLabel2);

        firstTxt.setMaximumSize(new java.awt.Dimension(140, 28));
        firstTxt.setMinimumSize(new java.awt.Dimension(140, 28));
        firstTxt.setPreferredSize(new java.awt.Dimension(140, 18));
        jPanel5.add(firstTxt);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Last Name");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 1));
        jLabel3.setMaximumSize(new java.awt.Dimension(90, 14));
        jLabel3.setMinimumSize(new java.awt.Dimension(90, 14));
        jLabel3.setPreferredSize(new java.awt.Dimension(90, 14));
        jPanel5.add(jLabel3);

        lastTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        jPanel5.add(lastTxt);

        jPanel3.add(jPanel5);

        jPanel6.setMaximumSize(new java.awt.Dimension(2147483647, 32));
        jPanel6.setMinimumSize(new java.awt.Dimension(435, 32));
        jPanel6.setPreferredSize(new java.awt.Dimension(503, 32));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        jLabel4.setText("SSN");
        jLabel4.setMaximumSize(new java.awt.Dimension(90, 14));
        jLabel4.setMinimumSize(new java.awt.Dimension(90, 14));
        jLabel4.setPreferredSize(new java.awt.Dimension(90, 14));
        jPanel6.add(jLabel4);

        ssnTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        ssnTxt.setMinimumSize(new java.awt.Dimension(100, 28));
        ssnTxt.setPreferredSize(new java.awt.Dimension(100, 28));
        jPanel6.add(ssnTxt);

        jLabel5.setText("Hire Date");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 1));
        jPanel6.add(jLabel5);

        hireTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        hireTxt.setMinimumSize(new java.awt.Dimension(75, 28));
        hireTxt.setPreferredSize(new java.awt.Dimension(75, 28));
        jPanel6.add(hireTxt);

        jLabel6.setText("Term Date");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 1));
        jPanel6.add(jLabel6);

        termTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        termTxt.setMinimumSize(new java.awt.Dimension(75, 28));
        termTxt.setPreferredSize(new java.awt.Dimension(75, 28));
        jPanel6.add(termTxt);

        jPanel3.add(jPanel6);

        jPanel4.setMaximumSize(new java.awt.Dimension(2147483647, 32));
        jPanel4.setMinimumSize(new java.awt.Dimension(237, 32));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        jLabel7.setText("City");
        jLabel7.setMaximumSize(new java.awt.Dimension(90, 14));
        jLabel7.setMinimumSize(new java.awt.Dimension(90, 14));
        jLabel7.setPreferredSize(new java.awt.Dimension(90, 14));
        jPanel4.add(jLabel7);

        cityTxt.setMaximumSize(new java.awt.Dimension(140, 28));
        cityTxt.setMinimumSize(new java.awt.Dimension(140, 28));
        cityTxt.setPreferredSize(new java.awt.Dimension(140, 28));
        jPanel4.add(cityTxt);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setText("Zip");
        jLabel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 1));
        jLabel8.setMaximumSize(new java.awt.Dimension(90, 14));
        jLabel8.setMinimumSize(new java.awt.Dimension(90, 14));
        jLabel8.setPreferredSize(new java.awt.Dimension(90, 14));
        jPanel4.add(jLabel8);

        zipTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        zipTxt.setMinimumSize(new java.awt.Dimension(100, 28));
        zipTxt.setPreferredSize(new java.awt.Dimension(100, 28));
        jPanel4.add(zipTxt);

        jPanel3.add(jPanel4);

        jPanel7.setMaximumSize(new java.awt.Dimension(2147483647, 32));
        jPanel7.setMinimumSize(new java.awt.Dimension(237, 32));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        jLabel9.setText("Phone Number");
        jLabel9.setMaximumSize(new java.awt.Dimension(90, 14));
        jLabel9.setMinimumSize(new java.awt.Dimension(90, 14));
        jLabel9.setPreferredSize(new java.awt.Dimension(90, 14));
        jPanel7.add(jLabel9);

        phoneTxt.setMaximumSize(new java.awt.Dimension(140, 28));
        phoneTxt.setMinimumSize(new java.awt.Dimension(140, 28));
        phoneTxt.setPreferredSize(new java.awt.Dimension(140, 18));
        jPanel7.add(phoneTxt);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setText("Email");
        jLabel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 3, 0, 1));
        jLabel10.setMaximumSize(new java.awt.Dimension(90, 14));
        jLabel10.setMinimumSize(new java.awt.Dimension(90, 14));
        jLabel10.setPreferredSize(new java.awt.Dimension(90, 14));
        jPanel7.add(jLabel10);

        emailTxt.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        emailTxt.setMinimumSize(new java.awt.Dimension(100, 28));
        emailTxt.setPreferredSize(new java.awt.Dimension(100, 28));
        jPanel7.add(emailTxt);

        jPanel3.add(jPanel7);

        FindEmps.setText("Find Employees");
        FindEmps.setMaximumSize(new java.awt.Dimension(120, 26));
        FindEmps.setMinimumSize(new java.awt.Dimension(120, 26));
        FindEmps.setPreferredSize(new java.awt.Dimension(120, 26));
        FindEmps.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FindEmpsActionPerformed(evt);
            }
        });

        jButton2.setText("CANCEL");
        jButton2.setMaximumSize(new java.awt.Dimension(120, 26));
        jButton2.setMinimumSize(new java.awt.Dimension(120, 26));
        jButton2.setPreferredSize(new java.awt.Dimension(120, 26));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 210, Short.MAX_VALUE)
                        .add(FindEmps, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(29, 29, 29)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(FindEmps, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(83, 83, 83))
        );

        myTabbedPane.addTab("Search", jPanel1);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("Double click on an employees name to bring up employee.");
        jPanel2.add(jLabel1);

        resultsPane.setLayout(new java.awt.GridLayout(1, 0));
        jPanel2.add(resultsPane);

        myTabbedPane.addTab("Results", jPanel2);

        getContentPane().add(myTabbedPane);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-511)/2, (screenSize.height-294)/2, 511, 294);
    }// </editor-fold>//GEN-END:initComponents

    
    private void FindEmpsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FindEmpsActionPerformed
        find_employees_by_characteristics_query mySearchSSNQuery = new find_employees_by_characteristics_query();
        String mySocial = ssnTxt.getText();
        String myfname = firstTxt.getText();
        String mylname = lastTxt.getText();
        String myterm = "";
        try {
            myterm = termTxt.getValue().toString();
        } catch (Exception e) {}
        String myhire = "";
        try {
            myhire = hireTxt.getValue().toString();
        } catch (Exception e) {}
        String mycity = cityTxt.getText();
        String myzip = zipTxt.getText();
        String myemail = emailTxt.getText();
        String myphone = phoneTxt.getText();
        
        mySearchSSNQuery.update(mySocial, myfname, mylname, myhire, myterm, mycity, myzip, myemail, myphone);
        Record_Set rs = new Record_Set();
        Vector<Company> myListOfCompanies = Main_Window.parentOfApplication.getListOfCompanies();
        myReturnedEmployees.clearRows();
        for (int i = 0; i < myListOfCompanies.size(); i++) {
            Connection myConnection = new Connection();
            myConnection.setCompany(myListOfCompanies.get(i).getId());
            try {
                rs = myConnection.executeQuery(mySearchSSNQuery);
            } catch (Exception e) {}
            
            for (int x = 0; x < rs.length(); x++) {
                String[] myVals = new String[8];
                myVals[0] = rs.getString("employee_id");
                myVals[1] = myConnection.myCompany;
                myVals[2] = rs.getString("branch_id");
                myVals[3] = rs.getString("employee_is_deleted");
                myVals[4] = Main_Window.parentOfApplication.getCompanyById(myConnection.myCompany).getName();
                myVals[5] = Main_Window.parentOfApplication.getBranchNameById(rs.getString("branch_id"));
                myVals[6] = rs.getString("employee_first_name");
                myVals[7] = rs.getString("employee_last_name");
                myReturnedEmployees.addRow(myVals);
                
                rs.moveNext();
            }
            myReturnedEmployees.fireTableDataChanged();
            myTabbedPane.revalidate();
            myTabbedPane.setSelectedIndex(1);   
        }
    }//GEN-LAST:event_FindEmpsActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton FindEmps;
    private javax.swing.JTextField cityTxt;
    private javax.swing.JFormattedTextField emailTxt;
    private javax.swing.JTextField firstTxt;
    private javax.swing.JFormattedTextField hireTxt;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTextField lastTxt;
    private javax.swing.JTabbedPane myTabbedPane;
    private javax.swing.JFormattedTextField phoneTxt;
    private javax.swing.JPanel resultsPane;
    private javax.swing.JFormattedTextField ssnTxt;
    private javax.swing.JFormattedTextField termTxt;
    private javax.swing.JFormattedTextField zipTxt;
    // End of variables declaration//GEN-END:variables
    
}
