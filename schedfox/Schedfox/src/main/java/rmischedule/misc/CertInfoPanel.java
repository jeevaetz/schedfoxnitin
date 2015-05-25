/*
 * CertInfoPanel.java
 *
 * Created on October 12, 2005, 8:52 AM
 */

package rmischedule.misc;
import schedfoxlib.model.util.Record_Set;
import rmischedule.components.graphicalcomponents.*;
import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
import javax.swing.*;
import java.util.StringTokenizer;
/**
 *
 * @author  Ira Juneau
 */
public class CertInfoPanel extends GenericEditSubForm {
    
    /** Creates new form CertInfoPanel */
    public CertInfoPanel() {
        initComponents();
    }
    
    public boolean userHasAccess() {
        return true;
    }
    
    public void doOnClear() {
        DescriptionText.setText("");
        DaySpinner.setModel(new SpinnerNumberModel(10, 1, 1000, 10));
    }
    
    public GeneralQueryFormat getQuery(boolean isNewData) {
        get_certifications_query myQuery = new get_certifications_query();
        myQuery.update(myparent.getMyIdForSave(), false);
        return myQuery;
    }
    
    public void loadData(Record_Set rs) {
        HeaderLabel.setText(rs.getString("certname"));
        DescriptionText.setText(rs.getString("certdesc"));
        String renewal = rs.getString("certrenewal");
        DaySpinner.setModel(new SpinnerNumberModel(10, 1, 1000, 10));
        if (renewal.equals("00:00:00") || renewal.equals("0")) {
            CertPermCheck.setSelected(true);
        } else {
            CertPermCheck.setSelected(false);
            StringTokenizer str = new StringTokenizer(renewal, ":");
            DaySpinner.setValue(Integer.parseInt(str.nextToken()));
            String type = str.nextToken();
            if (type.equals("d")) {
                DayMonthYearCombo.setSelectedItem("Days");
            } else if (type.equals("m")) {
                DayMonthYearCombo.setSelectedItem("Months");
            } else {
                DayMonthYearCombo.setSelectedItem("Years");
            }
        }
        
    }
    
    public GeneralQueryFormat getSaveQuery(boolean newData) {
        save_certification_query mySaveQuery = new save_certification_query();
        String renewalType = "d";
        if (((String)DayMonthYearCombo.getSelectedItem()).equals("Months")) {
            renewalType = "m";
        } else if (((String)DayMonthYearCombo.getSelectedItem()).equals("Years")) {
            renewalType = "y";
        }
        renewalType = ((Integer)DaySpinner.getValue()).toString() + ":" + renewalType;
        if (CertPermCheck.isSelected()) {
            renewalType = "00:00:00";
        }
        mySaveQuery.update(myparent.getMyIdForSave(), HeaderLabel.getText(), DescriptionText.getText(), renewalType);
        return mySaveQuery;
    }
    
    public boolean needsMoreRecordSets() {
        return false;
    }
    
    public JPanel getMyForm() {
        return this;
    }
    
    public String getMyTabTitle() {
        return "";
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        HeaderLabel = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DescriptionText = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        CertPermCheck = new javax.swing.JCheckBox();
        CertRenewalPanel = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        DaySpinner = new javax.swing.JSpinner();
        jPanel10 = new javax.swing.JPanel();
        DayMonthYearCombo = new javax.swing.JComboBox();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanel1.setMinimumSize(new java.awt.Dimension(10, 24));
        jPanel1.setPreferredSize(new java.awt.Dimension(10, 24));
        jLabel2.setText("Certification Name: ");
        jPanel1.add(jLabel2);

        HeaderLabel.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jPanel1.add(HeaderLabel);

        add(jPanel1);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.X_AXIS));

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanel4.setMinimumSize(new java.awt.Dimension(10, 24));
        jPanel4.setPreferredSize(new java.awt.Dimension(10, 24));
        jLabel1.setText("Certification Description ");
        jPanel4.add(jLabel1);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel5.setMinimumSize(new java.awt.Dimension(10, 50));
        jPanel5.setPreferredSize(new java.awt.Dimension(10, 50));
        jScrollPane1.setViewportView(DescriptionText);

        jPanel5.add(jScrollPane1);

        jPanel2.add(jPanel5);

        add(jPanel2);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.Y_AXIS));

        jPanel6.setBorder(new javax.swing.border.TitledBorder("Certification Renewal"));
        jPanel8.setLayout(new java.awt.GridLayout(1, 0));

        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 35));
        jPanel8.setMinimumSize(new java.awt.Dimension(147, 35));
        jPanel8.setPreferredSize(new java.awt.Dimension(147, 35));
        CertPermCheck.setText("Certification is permanent.");
        CertPermCheck.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                hideShowRenewal(evt);
            }
        });

        jPanel8.add(CertPermCheck);

        jPanel6.add(jPanel8);

        CertRenewalPanel.setLayout(new javax.swing.BoxLayout(CertRenewalPanel, javax.swing.BoxLayout.Y_AXIS));

        CertRenewalPanel.setMaximumSize(new java.awt.Dimension(90000, 2300));
        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.X_AXIS));

        jPanel11.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanel11.setMinimumSize(new java.awt.Dimension(10, 24));
        jPanel11.setPreferredSize(new java.awt.Dimension(10, 24));
        jLabel3.setText("Default renewal time.");
        jPanel11.add(jLabel3);

        CertRenewalPanel.add(jPanel11);

        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.X_AXIS));

        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 25));
        DaySpinner.setMaximumSize(new java.awt.Dimension(80, 20));
        DaySpinner.setMinimumSize(new java.awt.Dimension(80, 20));
        DaySpinner.setPreferredSize(new java.awt.Dimension(80, 20));
        jPanel9.add(DaySpinner);

        jPanel10.setMaximumSize(new java.awt.Dimension(20, 32767));
        jPanel9.add(jPanel10);

        DayMonthYearCombo.setMaximumRowCount(3);
        DayMonthYearCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Days", "Months", "Years" }));
        DayMonthYearCombo.setMaximumSize(new java.awt.Dimension(100, 20));
        DayMonthYearCombo.setMinimumSize(new java.awt.Dimension(100, 20));
        DayMonthYearCombo.setPreferredSize(new java.awt.Dimension(100, 20));
        DayMonthYearCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboChanged(evt);
            }
        });

        jPanel9.add(DayMonthYearCombo);

        CertRenewalPanel.add(jPanel9);

        jPanel6.add(CertRenewalPanel);

        jPanel3.add(jPanel6);

        add(jPanel3);

    }
    // </editor-fold>//GEN-END:initComponents

    private void comboChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboChanged
        if (((String)DayMonthYearCombo.getSelectedItem()).equals("Days")) {
            DaySpinner.setModel(new SpinnerNumberModel(10, 1, 1000, 10));
        } else if (((String)DayMonthYearCombo.getSelectedItem()).equals("Months")) {
            DaySpinner.setModel(new SpinnerNumberModel(1, 1, 60, 2));
        } else {
            DaySpinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        }
    }//GEN-LAST:event_comboChanged

    
    private void hideShowRenewal(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_hideShowRenewal
        if (CertPermCheck.isSelected()) {
            CertRenewalPanel.setVisible(false);
        } else {
            CertRenewalPanel.setVisible(true);
        }
    }//GEN-LAST:event_hideShowRenewal
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox CertPermCheck;
    private javax.swing.JPanel CertRenewalPanel;
    private javax.swing.JComboBox DayMonthYearCombo;
    private javax.swing.JSpinner DaySpinner;
    private javax.swing.JTextArea DescriptionText;
    private javax.swing.JTextField HeaderLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
}