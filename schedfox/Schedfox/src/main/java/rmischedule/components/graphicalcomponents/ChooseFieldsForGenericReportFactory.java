/*
 * ChooseFieldsForGenericReportFactory.java
 *
 * Created on November 3, 2005, 11:38 AM
 */

package rmischedule.components.graphicalcomponents;
import javax.swing.JCheckBox;
import java.util.*;
import rmischedule.xprint.templates.genericreportcomponents.*;
/**
 *
 * @author  Ira Juneau
 * Little class designed specifically for our generic report class, used for the user to 
 * specify what rows they want returned by a specific query.....
 */
public class ChooseFieldsForGenericReportFactory extends javax.swing.JDialog {
    
    private String[] fieldDispNames;
    private String[] fieldDispData;
    private formatterClass[] myFormatter;
    private int[] fieldDispSizes;
    private JCheckBox[] myFields;
    private ArrayList myReturnFields;
    
    /** Creates new form ChooseFieldsForGenericReportFactory */
    public ChooseFieldsForGenericReportFactory(java.awt.Frame parent, boolean modal, String[] availFieldDispNames, String[] availFieldDataNames, int[] fieldSizes, formatterClass[] myFormatters) {
        super(parent, modal);
        initComponents();
        fieldDispData = availFieldDataNames;
        fieldDispNames = availFieldDispNames;
        fieldDispSizes = fieldSizes;
        myFormatter = myFormatters;
        myFields = new JCheckBox[fieldDispNames.length];
        for (int i = 0; i < fieldDispNames.length; i++) {
            myFields[i] = new JCheckBox(fieldDispNames[i]);
            CheckBoxPanel.add(myFields[i]);
        }
        myReturnFields = new ArrayList();
    }
    
    public ArrayList displayFormAndReturnData() {
        super.setVisible(true);
        return myReturnFields;
    }
    
    public void setUpDataByWhatCheckBoxesSelected() {
        int arrayLength = 0;
        for (int i = 0; i < myFields.length; i++) {
            if (myFields[i].isSelected()) {
                arrayLength++;
            }
        }
        String[] whatFieldsSelNames = new String[arrayLength];
        String[] whatFieldsSelData = new String[arrayLength];
        int[] whatFieldsSelSize = new int[arrayLength];
        formatterClass[] whatFieldsFormatter = new formatterClass[arrayLength];
        int currPos = 0;
        for (int i = 0; i < myFields.length; i++) {
            if (myFields[i].isSelected()) {
                whatFieldsSelNames[currPos] =(fieldDispNames[i]);
                whatFieldsSelData[currPos] = (fieldDispData[i]);
                whatFieldsSelSize[currPos] = (fieldDispSizes[i]);
                whatFieldsFormatter[currPos] = (myFormatter[i]);
                currPos++;
            }
        }
        myReturnFields.add(whatFieldsSelNames);
        myReturnFields.add(whatFieldsSelData);
        myReturnFields.add(whatFieldsSelSize);
        myReturnFields.add(whatFieldsFormatter);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CheckBoxPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jLabel1.setText("Please select what information you would like displayed on the report.");
        getContentPane().add(jLabel1, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        CheckBoxPanel.setLayout(new javax.swing.BoxLayout(CheckBoxPanel, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane1.setViewportView(CheckBoxPanel);

        jPanel1.add(jScrollPane1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        jButton1.setText("OK");
        jButton1.setMaximumSize(new java.awt.Dimension(80, 20));
        jButton1.setMinimumSize(new java.awt.Dimension(80, 20));
        jButton1.setPreferredSize(new java.awt.Dimension(80, 20));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton1);

        jPanel2.add(jPanel3);

        jButton3.setText("Cancel");
        jButton3.setMaximumSize(new java.awt.Dimension(80, 20));
        jButton3.setMinimumSize(new java.awt.Dimension(80, 20));
        jButton3.setPreferredSize(new java.awt.Dimension(80, 20));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton3);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-430)/2, (screenSize.height-374)/2, 430, 374);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        setUpDataByWhatCheckBoxesSelected();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CheckBoxPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
}
