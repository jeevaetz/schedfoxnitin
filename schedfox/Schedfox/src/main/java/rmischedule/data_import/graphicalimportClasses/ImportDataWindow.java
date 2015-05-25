/*
 * ImportDataWindow.java
 *
 * Created on February 20, 2006, 1:23 PM
 */

package rmischedule.data_import.graphicalimportClasses;

import rmischedule.data_import.graphicalimportClasses.usked.*;
import java.util.*;
import java.io.*;
import java.awt.CardLayout;
/**
 *
 * @author  Ira Juneau
 */
public class ImportDataWindow extends javax.swing.JInternalFrame {
    
    
    private ArrayList<ImportGenericType> myPanels;
    private String myDir;
    int currPos;
    
    private int myUpdateType;
    
    public static int INSERTONLY = 0;
    public static int UPDATEONLY = 1;
    public static int CLEANINSERTONLY = 2;
    
    /** Creates new form ImportDataWindow */
    public ImportDataWindow(String mainUskedDir) {
        initComponents();
        myDir = mainUskedDir;
        myPanels = new ArrayList();
        ImportUpdateOrCleanAndUpdatePanel myPanel1 = new ImportUpdateOrCleanAndUpdatePanel();
        UskedProcessDirectoryPanel myPanel2 = new UskedProcessDirectoryPanel();
        myPanel1.setParent(this);
        myPanel2.setParent(this);
        myPanels.add(myPanel1);
        myPanels.add(myPanel2);
        
        
        for (int i = 0; i < myPanels.size(); i++) {
            controlPanel.add(myPanels.get(i).getComponent(), "" + i);
        }
        CancelBtn.setText("Cancel");
        currPos = 0;
        //processDir(mainUskedDir);
        //addComponentsForUsked();
    }
    
    public String getDirectory() {
        return myDir;
    }

    /**
     * Are we just importing..updating existing, or clearing then importing...
     */
    public void setImportType(int importType) {
        myUpdateType = importType;
    }
    
    public static void main(String args[]) {
        ImportDataWindow myWindow = new ImportDataWindow("c:\\ultra32\\ultra32\\");
        myWindow.setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        CancelBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        NextBtn = new javax.swing.JButton();
        myScrollPane = new javax.swing.JScrollPane();
        controlPanel = new javax.swing.JPanel();

        setMinimumSize(new java.awt.Dimension(550, 33));
        setPreferredSize(new java.awt.Dimension(600, 600));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 24));
        jPanel2.setMinimumSize(new java.awt.Dimension(10, 24));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 24));
        CancelBtn.setText("Previous");
        CancelBtn.setMaximumSize(new java.awt.Dimension(80, 24));
        CancelBtn.setMinimumSize(new java.awt.Dimension(80, 24));
        CancelBtn.setPreferredSize(new java.awt.Dimension(80, 24));
        CancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disposeMe(evt);
            }
        });

        jPanel2.add(CancelBtn);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 494, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 24, Short.MAX_VALUE)
        );
        jPanel2.add(jPanel3);

        NextBtn.setText("Next");
        NextBtn.setMaximumSize(new java.awt.Dimension(80, 24));
        NextBtn.setMinimumSize(new java.awt.Dimension(80, 24));
        NextBtn.setPreferredSize(new java.awt.Dimension(80, 24));
        NextBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveNext(evt);
            }
        });

        jPanel2.add(NextBtn);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        controlPanel.setLayout(new java.awt.CardLayout());

        myScrollPane.setViewportView(controlPanel);

        getContentPane().add(myScrollPane, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-660)/2, (screenSize.height-355)/2, 660, 355);
    }// </editor-fold>//GEN-END:initComponents

    private void moveNext(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveNext
        CardLayout myLayout = (CardLayout)controlPanel.getLayout();
        myPanels.get(currPos).doOnNext();
        currPos++;
        try {
            myPanels.get(currPos).showMe();
        } catch (Exception e) {
            currPos--;
        }
        myLayout.show(controlPanel, "" + currPos);
        CancelBtn.setText("Previous");
    }//GEN-LAST:event_moveNext

    private void disposeMe(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disposeMe
        CardLayout myLayout = (CardLayout)controlPanel.getLayout();
        currPos--;
        if (currPos < 0) {
            dispose();
        }
        try {
            myPanels.get(currPos).showMe();
        } catch (Exception e) {}
        if (currPos == 0) {
            CancelBtn.setText("Cancel");
        } else {
            CancelBtn.setText("Previous");
        }
        myLayout.show(controlPanel, "" + currPos);
    }//GEN-LAST:event_disposeMe
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelBtn;
    private javax.swing.JButton NextBtn;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane myScrollPane;
    // End of variables declaration//GEN-END:variables
    
}