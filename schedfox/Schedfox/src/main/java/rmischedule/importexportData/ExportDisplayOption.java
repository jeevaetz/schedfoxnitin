/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ExportEmployeeDisplayOption.java
 *
 * Created on Sep 15, 2010, 11:17:39 AM
 */
package rmischedule.importexportData;

import rmischeduleserver.control.model.GenericExportWithDynamicFields;
import rmischeduleserver.control.model.DBMapper;
import rmischeduleserver.control.model.DataMappingClass;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import rmischedule.components.graphicalcomponents.myToolBarIcons;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;

/**
 *
 * @author vnguyen
 */
public class ExportDisplayOption extends javax.swing.JInternalFrame {

    Connection myConn = new Connection();
    private LinkedHashMap<JRadioButton, DataMappingClass> userOptions;
    private String co_id;
    private String br_id;
    //used to tell when user clicks or activated when sellect all breaks
    boolean trigger = false;
    int x = 0;
    int y = 0;
    private myToolBarIcons mySaveIcon;
    private myToolBarIcons myCancelIcon;
    private boolean isUskedExport = true;
    private DBMapper myMapper;

    public ExportDisplayOption(String co, String branch, DBMapper myMapper) {
        initComponents();
        this.myMapper = myMapper;

        //  set icons, tool bar
        this.jActionPanel.revalidate();
        this.jActionPanel.repaint();
        this.setUpToolBar();

        this.userOptions = new LinkedHashMap<JRadioButton, DataMappingClass>();
        this.co_id = co;
        this.br_id = branch;
        this.myConn.setCompany(co_id);
        this.setDefaultColumnHeader();
        this.addCheckBoxes();
    }

    /**
     *  Sets up the tool bar and the appropriate action      *
     */
    private void setUpToolBar() {
        //  create new icons
        mySaveIcon = new myToolBarIcons();
        myCancelIcon = new myToolBarIcons();

        //  set icon text
        mySaveIcon.setToolTipText("Save the exported file.");
        mySaveIcon.setText("Save Export", new Font("Dialog", Font.BOLD, 14));
        myCancelIcon.setToolTipText("Cancel export, close this window.");
        myCancelIcon.setText("Cancel", new Font("Dialog", Font.BOLD, 14));

        //  set icons
        mySaveIcon.setIcon(Main_Window.Save_User_Icon_Aero_32x32px);
        myCancelIcon.setIcon(Main_Window.Exit_Icon_Aero_32x32px);

        //  set size of icons
        mySaveIcon.setSize(new Dimension(120, 25));
        myCancelIcon.setSize(new Dimension(120, 25));

        //  add icons to tool bar in proper format
        this.jToolBar.add(this.myCancelIcon);
        this.jToolBar.add(this.createSpacerPanel());
        this.jToolBar.add(this.mySaveIcon);

        //  add mouse listeners
        /**
         *  myCancelIcon mouse listener implementation
         */
        myCancelIcon.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                dispose();
            }
        });

        /**
         *  mySaveIcon mouse listener implementation
         */
        mySaveIcon.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                save();
            }
        });
    }

    /**
     *  returns a "space" panel to separate toolbar icons
     *  @return JPanel spacer
     */
    private JPanel createSpacerPanel() {
        //  create spacer panel, set properties
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);

        //  return spacer panel
        return spacer;
    }

    /**
     *  saves the export
     */
    private void save() {
        LinkedHashMap<String, DataMappingClass> selectedData = this.getHeadersSelected();
        ArrayList<DataMappingClass> selectedColumns = new ArrayList<DataMappingClass>();
        Iterator<String> keys = selectedData.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            selectedColumns.add(selectedData.get(key));
        }
        try {
            GenericExportWithDynamicFields exportData =
                    new GenericExportWithDynamicFields(co_id, selectedColumns, isUskedExport,
                    this.myMapper, new Object[]{Integer.parseInt(this.br_id)});
            Main_Window.setWaitCursor(true);

            JFileChooser fc = new JFileChooser();
            FileFilter ft = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
            fc.addChoosableFileFilter(ft);
            int returnVal = fc.showSaveDialog(Main_Window.parentOfApplication.desktop);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filename = fc.getSelectedFile().getAbsolutePath();
                File excelFile = new File(filename);
                if (!fc.getSelectedFile().exists()) {
                    filename += ".xlsx";
                    excelFile = new File(filename);
                    excelFile.createNewFile();
                }
                exportData.exportExcel(excelFile, true);
                JOptionPane.showConfirmDialog(Main_Window.parentOfApplication.desktop,
                        "Export Complete to " + filename, "Confirmation", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showConfirmDialog(Main_Window.parentOfApplication.desktop, "ERROR: " + ex, "ERROR EXPORTING", JOptionPane.PLAIN_MESSAGE);
        } finally {
            Main_Window.setWaitCursor(false);
        }


        this.dispose();
    }

    private void addColNMapping(DataMappingClass mapping) {
        userOptions.put(new JRadioButton(mapping.getFirstValue()), mapping);


    }

    private void setDefaultColumnHeader() {

        LinkedHashMap<DataMappingClass, Integer> myMap = myMapper.getMappings();
        Iterator<DataMappingClass> dataMap = myMap.keySet().iterator();


        while (dataMap.hasNext()) {
            DataMappingClass nextMap = dataMap.next();


            this.addColNMapping(nextMap);


        }
    }

    private void selectAllAction(boolean sel) {
        Iterator<JRadioButton> keys = userOptions.keySet().iterator();


        while (keys.hasNext()) {
            keys.next().setSelected(sel);


        }
    }

    private void addCheckBoxes() {
        Iterator<JRadioButton> keys = userOptions.keySet().iterator();
        ArrayList<JRadioButton> listOfKeys = new ArrayList<JRadioButton>();

        while (keys.hasNext()) {
            listOfKeys.add(keys.next());
        }
        //Collections.sort(listOfKeys);
        for (int idx = 0; idx
                < listOfKeys.size(); idx++) {
            JRadioButton buttonToPlace = listOfKeys.get(idx);
            buttonToPlace.setVisible(false);


            if (userOptions.get(buttonToPlace).isShouldDisplayInManualExport()) {
                buttonToPlace.setVisible(true);
                buttonToPlace.setSelected(false);

                this.jFieldsPanel.add(buttonToPlace);
            } else if (this.isUskedExport) {
                buttonToPlace.setSelected(true);
            }
        }
        this.jFieldsPanel.revalidate();
        this.jFieldsPanel.repaint();
        this.jNumberFieldsLabel.setText("Number of Fields:  " + listOfKeys.size());
    }

    private LinkedHashMap<String, DataMappingClass> getHeadersSelected() {
        Iterator<JRadioButton> keys = userOptions.keySet().iterator();
        LinkedHashMap<String, DataMappingClass> retVal = new LinkedHashMap<String, DataMappingClass>();


        while (keys.hasNext()) {
            JRadioButton chk = keys.next();
            DataMappingClass dataClass = userOptions.get(chk);


            if (chk.isSelected() || (!dataClass.isShouldDisplayInManualExport() && isUskedExport)) {
                retVal.put(chk.getText(), userOptions.get(chk));


            }
        }
        return retVal;


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jBasePanel = new javax.swing.JPanel();
        jSelectionPanel = new javax.swing.JPanel();
        jActionPanel = new javax.swing.JPanel();
        jSelectAllButton = new javax.swing.JToggleButton();
        jInstructionsLabel = new javax.swing.JLabel();
        jInformationPanel = new javax.swing.JPanel();
        jNumberFieldsLabel = new javax.swing.JLabel();
        jFieldsPanel = new javax.swing.JPanel();
        jToolBar = new javax.swing.JToolBar();

        setClosable(true);
        setResizable(true);
        setTitle("Select Fields you wish to have exported");
        setName("  selectAll.setSelected(true);"); // NOI18N
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        setVisible(true);

        jBasePanel.setLayout(new java.awt.BorderLayout());

        jSelectionPanel.setLayout(new java.awt.GridLayout(1, 2));

        jActionPanel.setLayout(new java.awt.GridBagLayout());

        jSelectAllButton.setText("Select All");
        jSelectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSelectAllButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jActionPanel.add(jSelectAllButton, gridBagConstraints);

        jInstructionsLabel.setText("Please select the fields to be exported:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        jActionPanel.add(jInstructionsLabel, gridBagConstraints);

        jSelectionPanel.add(jActionPanel);

        jInformationPanel.setLayout(new java.awt.GridBagLayout());

        jNumberFieldsLabel.setText("Number of Fields:  ");
        jInformationPanel.add(jNumberFieldsLabel, new java.awt.GridBagConstraints());

        jSelectionPanel.add(jInformationPanel);

        jBasePanel.add(jSelectionPanel, java.awt.BorderLayout.NORTH);

        jFieldsPanel.setMaximumSize(new java.awt.Dimension(300, 200));
        jFieldsPanel.setMinimumSize(new java.awt.Dimension(300, 200));
        jFieldsPanel.setPreferredSize(new java.awt.Dimension(300, 200));
        jFieldsPanel.setLayout(new java.awt.GridLayout(6, 0));
        jBasePanel.add(jFieldsPanel, java.awt.BorderLayout.CENTER);

        jToolBar.setRollover(true);
        jBasePanel.add(jToolBar, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jBasePanel, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-535)/2, (screenSize.height-380)/2, 535, 380);
    }// </editor-fold>//GEN-END:initComponents
        private void jSelectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSelectAllButtonActionPerformed
            this.selectAllAction(this.jSelectAllButton.isSelected());
    }//GEN-LAST:event_jSelectAllButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jActionPanel;
    private javax.swing.JPanel jBasePanel;
    private javax.swing.JPanel jFieldsPanel;
    private javax.swing.JPanel jInformationPanel;
    private javax.swing.JLabel jInstructionsLabel;
    private javax.swing.JLabel jNumberFieldsLabel;
    private javax.swing.JToggleButton jSelectAllButton;
    private javax.swing.JPanel jSelectionPanel;
    private javax.swing.JToolBar jToolBar;
    // End of variables declaration//GEN-END:variables
};
