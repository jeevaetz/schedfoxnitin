/*
 * xAvailabilitySpecifyTimes.java
 *
 * Created on August 22, 2005, 8:07 AM
 */

package rmischedule.employee.components.xavailability;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JLayeredPane;

import rmischedule.main.*;
import rmischedule.components.graphicalcomponents.*;

/**
 *
 * @author  Ira Juneau
 */
public class xAvailabilitySpecifyTimes extends javax.swing.JPanel {

    //private static Color myBackgroundColor = new Color(186,186,222,190);
    //private static Color myBackgroundColor = new Color(246,237,225, 190);
    private static Color myBackgroundColor = new Color(255,255,255, 190);
    private xFormInterface parent;
    private static Dimension mySize = new Dimension(270, 115);

    private TimeEditTextField myEntryTextField;
    private TimeEditTextField myExitTextField;

    private boolean isVisible;
    private xIndividualDayPanel activeDay;

    /** Creates new form xAvailabilitySpecifyTimes */
    public xAvailabilitySpecifyTimes(xFormInterface myParent) {
        initComponents();
        isVisible = false;
        parent = myParent;
        setBackground(myBackgroundColor);
        myEntryTextField = new TimeEditTextField();
        myExitTextField = new TimeEditTextField();
        EndTimeLabel.add(myExitTextField);
        StartTimeLabel.add(myEntryTextField);
    }

    /**
     * Gets this panels size...
     */
    public Dimension getMySize() {
        return mySize;
    }

    /**
     * Sets bounds to the center of the given panel and passes back info to given day
     */
    public void showMe(JPanel panelToCenterOn, xIndividualDayPanel myDay, String myTitle) {
        if (!isVisible) {
            isVisible = true;
            setBounds((panelToCenterOn.getBounds().width / 2 - getMySize().width / 2),
                    ((panelToCenterOn.getParent().getBounds().height) / 2 - getMySize().height / 2),
                    getMySize().width, getMySize().height);
            TitleLabel.setText(myTitle);
            activeDay = myDay;
            parent.getLayeredPane().revalidate();
            parent.getLayeredPane().setLayer(this, JLayeredPane.MODAL_LAYER);
            parent.getLayeredPane().moveToFront(this);
            myEntryTextField.setValue(myDay.getStartDBase());
            myExitTextField.setValue(myDay.getEndDBase());
            if (!myDay.isAvail()) {
                DeleteBtn.setVisible(true);
            } else {
                DeleteBtn.setVisible(false);
            }
            try {
                myEntryTextField.requestFocus();
            } catch (Exception e) {}
        }
    }

    /**
     * Sets the text of this window...
     */
    public void setText(String text) {
        TitleLabel.setText(text);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        TitleLabel = new javax.swing.JLabel();
        StartPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        StartTimeLabel = new javax.swing.JPanel();
        EndPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        EndTimeLabel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        OKBtn = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        DeleteBtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        CancelBtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(186, 186, 222));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setLayout(new java.awt.BorderLayout());

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel6.setMinimumSize(new java.awt.Dimension(41, 26));
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(41, 26));
        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        TitleLabel.setText("jLabel1");
        jPanel6.add(TitleLabel);

        jPanel1.add(jPanel6);

        StartPanel.setMaximumSize(new java.awt.Dimension(32847, 26));
        StartPanel.setMinimumSize(new java.awt.Dimension(80, 26));
        StartPanel.setOpaque(false);
        StartPanel.setPreferredSize(new java.awt.Dimension(80, 26));
        StartPanel.setLayout(new javax.swing.BoxLayout(StartPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Start Time:");
        jLabel1.setMaximumSize(new java.awt.Dimension(80, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(80, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(80, 14));
        StartPanel.add(jLabel1);

        StartTimeLabel.setMaximumSize(new java.awt.Dimension(32767, 30));
        StartTimeLabel.setOpaque(false);
        StartTimeLabel.setLayout(new java.awt.GridLayout(1, 0));
        StartPanel.add(StartTimeLabel);

        jPanel1.add(StartPanel);

        EndPanel.setMaximumSize(new java.awt.Dimension(32847, 26));
        EndPanel.setMinimumSize(new java.awt.Dimension(80, 26));
        EndPanel.setOpaque(false);
        EndPanel.setPreferredSize(new java.awt.Dimension(80, 26));
        EndPanel.setLayout(new javax.swing.BoxLayout(EndPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("End Time :");
        jLabel2.setMaximumSize(new java.awt.Dimension(80, 14));
        jLabel2.setMinimumSize(new java.awt.Dimension(80, 14));
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 14));
        EndPanel.add(jLabel2);

        EndTimeLabel.setMaximumSize(new java.awt.Dimension(32767, 30));
        EndTimeLabel.setOpaque(false);
        EndTimeLabel.setLayout(new java.awt.GridLayout(1, 0));
        EndPanel.add(EndTimeLabel);

        jPanel1.add(EndPanel);

        jPanel7.setOpaque(false);
        jPanel1.add(jPanel7);

        jPanel4.setOpaque(false);
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

        OKBtn.setText("OK");
        OKBtn.setMaximumSize(new java.awt.Dimension(80, 23));
        OKBtn.setMinimumSize(new java.awt.Dimension(80, 23));
        OKBtn.setPreferredSize(new java.awt.Dimension(80, 23));
        OKBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKBtnActionPerformed(evt);
            }
        });
        jPanel4.add(OKBtn);

        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanel9.setOpaque(false);
        jPanel4.add(jPanel9);

        DeleteBtn.setText("Delete");
        DeleteBtn.setMaximumSize(new java.awt.Dimension(80, 23));
        DeleteBtn.setMinimumSize(new java.awt.Dimension(80, 23));
        DeleteBtn.setPreferredSize(new java.awt.Dimension(80, 23));
        DeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteBtnActionPerformed(evt);
            }
        });
        jPanel4.add(DeleteBtn);

        jPanel5.setMaximumSize(new java.awt.Dimension(32767, 10));
        jPanel5.setOpaque(false);
        jPanel4.add(jPanel5);

        CancelBtn.setText("Cancel");
        CancelBtn.setMaximumSize(new java.awt.Dimension(80, 23));
        CancelBtn.setMinimumSize(new java.awt.Dimension(80, 23));
        CancelBtn.setPreferredSize(new java.awt.Dimension(80, 23));
        CancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelBtnActionPerformed(evt);
            }
        });
        jPanel4.add(CancelBtn);

        jPanel1.add(jPanel4);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void DeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteBtnActionPerformed
        activeDay.setStartEndTimes(Main_Window.AVAILABLE, (String)myEntryTextField.getValue(), (String)myExitTextField.getValue(), activeDay.getShiftId(), "0");
        isVisible = false;
        parent.hideSpecifyTimes(this);
        parent.loadData();
    }//GEN-LAST:event_DeleteBtnActionPerformed

    private void OKBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKBtnActionPerformed
        int newType = Main_Window.NON_AVAILABLE;
        if (activeDay.getShiftType() != Main_Window.AVAILABLE) {
            newType = activeDay.getShiftType();
        }
        activeDay.setStartEndTimes(newType, (String)myEntryTextField.getValue(), (String)myExitTextField.getValue(), activeDay.getShiftId(), "0");
        isVisible = false;
        parent.hideSpecifyTimes(this);
    }//GEN-LAST:event_OKBtnActionPerformed

    private void CancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelBtnActionPerformed
        isVisible = false;
        parent.hideSpecifyTimes(this);
    }//GEN-LAST:event_CancelBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelBtn;
    private javax.swing.JButton DeleteBtn;
    private javax.swing.JPanel EndPanel;
    private javax.swing.JPanel EndTimeLabel;
    private javax.swing.JButton OKBtn;
    private javax.swing.JPanel StartPanel;
    private javax.swing.JPanel StartTimeLabel;
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    // End of variables declaration//GEN-END:variables

}
