/*
 * importColumnData.java
 *
 * Created on February 20, 2006, 1:35 PM
 */

package rmischedule.data_import.graphicalimportClasses;

/**
 *
 * @author  Ira Juneau
 */
public class importColumnData extends javax.swing.JPanel {
    
    /** Creates new form importColumnData */
    public importColumnData() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setLayout(new java.awt.GridLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 18));
        jPanel1.setMinimumSize(new java.awt.Dimension(10, 18));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 18));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel1.setText("Column Name");
        jPanel1.add(jLabel1);

        add(jPanel1);

        jPanel2.setLayout(new java.awt.GridLayout());

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 18));
        jPanel2.setMinimumSize(new java.awt.Dimension(10, 18));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 18));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(jComboBox1);

        add(jPanel2);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
    
}
