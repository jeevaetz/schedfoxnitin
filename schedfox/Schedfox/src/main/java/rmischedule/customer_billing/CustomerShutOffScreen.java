/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CustomerShutOffScreen.java
 *
 * Created on Jul 20, 2010, 12:24:13 PM
 */

package rmischedule.customer_billing;

import rmischedule.main.Main_Window;

/**
 *
 * @author user
 */
public class CustomerShutOffScreen extends javax.swing.JDialog {

    /** Creates new form CustomerShutOffScreen */
    public CustomerShutOffScreen(java.awt.Frame parent, boolean modal, int company_id) {
        super(parent, modal);
        initComponents();

        iconLabel.setIcon(Main_Window.Load_Image);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        iconLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        iconLabel.setMaximumSize(new java.awt.Dimension(630, 160));
        iconLabel.setMinimumSize(new java.awt.Dimension(500, 160));
        iconLabel.setPreferredSize(new java.awt.Dimension(630, 160));
        getContentPane().add(iconLabel);

        jPanel1.setMaximumSize(new java.awt.Dimension(8000000, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(8000, 255));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("Your bill is past due!");
        jLabel1.setMaximumSize(new java.awt.Dimension(500, 35));
        jLabel1.setMinimumSize(new java.awt.Dimension(500, 35));
        jLabel1.setPreferredSize(new java.awt.Dimension(500, 35));
        jPanel3.add(jLabel1);

        jPanel1.add(jPanel3);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setMinimumSize(new java.awt.Dimension(400, 22));
        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 220));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Schedfox will continue to hold all of your scheduling information.  You will not be unable to access most features until you make a valid payment.  Please click on the icon below to start a payment.  ");
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setOpaque(false);
        jPanel2.add(jTextArea1);

        jPanel4.add(jPanel2);

        jPanel1.add(jPanel4);

        getContentPane().add(jPanel1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-567)/2, (screenSize.height-338)/2, 567, 338);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel iconLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}
