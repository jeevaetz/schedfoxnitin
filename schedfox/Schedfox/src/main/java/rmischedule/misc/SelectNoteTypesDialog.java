/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.misc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.JCheckBox;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.ireports.viewer.IReportViewer;
import rmischedule.main.Main_Window;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.control.NoteController;
import schedfoxlib.model.Company;
import schedfoxlib.model.NoteType;

/**
 *
 * @author ira
 */
public class SelectNoteTypesDialog extends javax.swing.JDialog {

    private String companyId;
    private Boolean isEmployee;
    private Integer entityId;
    private HashMap<JCheckBox, Integer> checkboxValues;

    /**
     * Creates new form SelectNoteTypesDialog
     */
    public SelectNoteTypesDialog(java.awt.Frame parent, boolean modal, String companyId, Integer entityId, boolean isEmployee) {
        super(parent, modal);
        initComponents();
        this.companyId = companyId;
        this.isEmployee = isEmployee;
        this.entityId = entityId;
        checkboxValues = new HashMap<JCheckBox, Integer>();
        this.populateNoteTypes();
    }

    private void populateNoteTypes() {
        NoteController noteController = NoteController.getInstance(companyId);
        try {
            ArrayList<NoteType> types = noteController.getNoteTypes();
            for (int t = 0; t < types.size(); t++) {
                final JCheckBox newCheck = new JCheckBox(types.get(t).getNoteTypeName());
                checkboxValues.put(newCheck, types.get(t).getNoteTypeId());
                newCheck.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (newCheck.isSelected()) {
                            boolean allSelected = true;
                            for (int c = 0; c < checkboxPanel.getComponentCount(); c++) {
                                if (checkboxPanel.getComponent(c) instanceof JCheckBox && !checkboxPanel.getComponent(c).equals(selectAllChk)) {
                                    if (!((JCheckBox) checkboxPanel.getComponent(c)).isSelected()) {
                                        allSelected = false;
                                    }
                                }
                            }
                            selectAllChk.setSelected(allSelected);
                        } else {
                            selectAllChk.setSelected(false);
                        }
                    }
                });
                checkboxPanel.add(newCheck);
            }
        } catch (Exception e) {
        }
    }

    public void printNoteType() {
        Hashtable parameters = new Hashtable();
        InputStream reportStream = null;
        if (isEmployee) {
            try {
                reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/employee_note_report.jasper");
                parameters.put("employee_id", entityId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/client_note_report.jasper");
                parameters.put("client_id", entityId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Company companyInfo = Main_Window.parentOfApplication.getCompanyById(companyId);

            StringBuilder noteTypeIds = new StringBuilder();
            noteTypeIds.append("(");
            for (int c = 0; c < checkboxPanel.getComponentCount(); c++) {
                if (checkboxPanel.getComponent(c) instanceof JCheckBox && !checkboxPanel.getComponent(c).equals(selectAllChk)) {
                    if (((JCheckBox) checkboxPanel.getComponent(c)).isSelected()) {
                        if (noteTypeIds.length() > 1) {
                            noteTypeIds.append(",");
                        }
                        noteTypeIds.append(checkboxValues.get((JCheckBox) checkboxPanel.getComponent(c)) + "");
                    }
                }
            }
            noteTypeIds.append(")");

            parameters.put("active_db", companyInfo.getDB());
            parameters.put("note_types", noteTypeIds.toString());

            JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, RMIScheduleServerImpl.getConnection().generateConnection());
            IReportViewer viewer = new IReportViewer(report);
            Main_Window.parentOfApplication.desktop.add(viewer);
            viewer.showForm();
        } catch (Exception exe) {
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

        jScrollPane1 = new javax.swing.JScrollPane();
        checkboxPanel = new javax.swing.JPanel();
        selectAllChk = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        checkboxPanel.setMaximumSize(new java.awt.Dimension(500, 5000));
        checkboxPanel.setLayout(new javax.swing.BoxLayout(checkboxPanel, javax.swing.BoxLayout.Y_AXIS));

        selectAllChk.setText("Select all note types");
        selectAllChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllChkActionPerformed(evt);
            }
        });
        checkboxPanel.add(selectAllChk);

        jScrollPane1.setViewportView(checkboxPanel);

        getContentPane().add(jScrollPane1);

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel1.setMinimumSize(new java.awt.Dimension(0, 32));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 32));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jButton1.setText("Close");
        jPanel1.add(jButton1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3);

        jButton2.setText("Print");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        getContentPane().add(jPanel1);

        setSize(new java.awt.Dimension(383, 329));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void selectAllChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllChkActionPerformed
        for (int c = 0; c < checkboxPanel.getComponentCount(); c++) {
            if (checkboxPanel.getComponent(c) instanceof JCheckBox) {
                ((JCheckBox) checkboxPanel.getComponent(c)).setSelected(selectAllChk.isSelected());
            }
        }
    }//GEN-LAST:event_selectAllChkActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        printNoteType();
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel checkboxPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox selectAllChk;
    // End of variables declaration//GEN-END:variables
}
