/*
 * NewImportForm.java
 *
 * Created on June 13, 2006, 12:43 PM
 */

/**
 * NOTE:  update commented out for speed pushtrough on Target, WILL NOT
 *     WORK FOR USCHED APPLICATIONS
 */


package rmischedule.data_import.graphicalimportClasses;

import schedfoxlib.model.Company;
import java.io.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.awt.Font;
import java.awt.Dimension;

import org.apache.poi.hssf.usermodel.*;

import rmischedule.main.Main_Window.*;
import schedfoxlib.model.Branch;

import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_save_query;

/**
 *
 * @author  shawn
 */
public class NewImportForm extends javax.swing.JInternalFrame {
    
    private HSSFWorkbook workBook;
    private HSSFSheet workSheet;
    
    private Company company;
    private Branch branch;
    
    //a list for each table column we could be importing too... yeah this is ugly, but it works.
    //names correspond directly to the table column
    private ArrayList<String> employee_first_name       = new ArrayList<String>();
    private ArrayList<String> employee_last_name        = new ArrayList<String>();
    private ArrayList<String> employee_middle_initial   = new ArrayList<String>();
    private ArrayList<String> employee_ssn              = new ArrayList<String>();
    private ArrayList<String> employee_address          = new ArrayList<String>();
    private ArrayList<String> employee_address2         = new ArrayList<String>();
    private ArrayList<String> employee_city             = new ArrayList<String>();
    private ArrayList<String> employee_state            = new ArrayList<String>();
    private ArrayList<String> employee_zip              = new ArrayList<String>();
    private ArrayList<String> employee_phone            = new ArrayList<String>();
    private ArrayList<String> employee_phone2           = new ArrayList<String>();
    private ArrayList<String> employee_pager            = new ArrayList<String>();
    private ArrayList<String> employee_cell             = new ArrayList<String>();
    private ArrayList<String> employee_email            = new ArrayList<String>();
    private ArrayList<String> employee_birthdate        = new ArrayList<String>();
    private ArrayList<String> employee_hire_date        = new ArrayList<String>();
    
    //a list that contains all of the above lists
    ArrayList<ArrayList<String>> allLists;
    
    /** Creates new form NewImportForm */
    public NewImportForm(Company co, Branch br, File excelFile) {
        initComponents();
        this.company = co;
        this.branch = br;
        
        try {
            workBook = new HSSFWorkbook(new FileInputStream(excelFile));
            workSheet = workBook.getSheetAt(0);          
            MyTableModel tableModel = new MyTableModel();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
            
            for(int i = 0; i <= workSheet.getLastRowNum(); i++) {
                HSSFRow row = workSheet.getRow(i);
                Object[] rowData = new Object[row.getLastCellNum()];
                
                if(row.getLastCellNum() > tableModel.getColumnCount())
                    tableModel.setColumnCount(row.getLastCellNum());
                
                for(short c = 0; c < row.getLastCellNum(); c++) {
                    int cellType = row.getCell(c).getCellType();
                    switch(cellType) {
                        
                        case HSSFCell.CELL_TYPE_STRING:
                            rowData[c] = row.getCell(c).getStringCellValue();
                            break;
                            
                            
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            double value = row.getCell(c).getNumericCellValue();
                            
                            if(HSSFDateUtil.isValidExcelDate(value))
                                rowData[c] = dateFormatter.format(row.getCell(c).getDateCellValue());
                            else
                                rowData[c] = value;                          
                            break;
                            
                            
                        case HSSFCell.CELL_TYPE_BOOLEAN:
                            rowData[c] = row.getCell(c).getBooleanCellValue();
                            break;
                            
                            
                        case HSSFCell.CELL_TYPE_FORMULA:
                            rowData[c] = row.getCell(c).getCellFormula();
                            break;
                            
                            
                        case HSSFCell.CELL_TYPE_ERROR:
                            rowData[c] = row.getCell(c).getErrorCellValue();
                            break;
                            
                            
                        default:
                            rowData[c] = "";
                            break;
                    }
                }
                tableModel.addRow(rowData);
            }            
            
            this.dataTable.setModel(tableModel);
            for(int i = 0; i < this.dataTable.getColumnCount(); i++) {
                this.dataTable.getColumnModel().getColumn(i).setHeaderValue("Unassigned");
                this.dataTable.getColumnModel().getColumn(i).setMinWidth(150);
            }
            
            this.dataTable.setMinimumSize(new Dimension(this.dataTable.getColumnCount()*150, 32000));
            this.dataTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 11));
            this.dataTable.revalidate();
            this.importButton.setEnabled(true);
            
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Unable to load data from file!\n" + ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE); }
        
        this.setTitle("Importing Employees into " + this.company.getName() + ", " + this.branch.getBranchName());
        
        this.allLists = new ArrayList<ArrayList<String>>();
        this.allLists.add(employee_first_name);
        this.allLists.add(employee_last_name);
        this.allLists.add(employee_middle_initial);
        this.allLists.add(employee_ssn);
        this.allLists.add(employee_address);
        this.allLists.add(employee_address2);
        this.allLists.add(employee_city);
        this.allLists.add(employee_state);
        this.allLists.add(employee_zip);
        this.allLists.add(employee_phone);
        this.allLists.add(employee_phone2);
        this.allLists.add(employee_pager);
        this.allLists.add(employee_cell);
        this.allLists.add(employee_email);
        this.allLists.add(employee_birthdate);
    }
    
    
    //returns a set of queries that should handle the entire import
    private RunQueriesEx setupQueries() {
        RunQueriesEx queries = new RunQueriesEx();
        
        //for each column in our table, update one of our lists
        for(int i = 0; i < this.dataTable.getColumnCount(); i++) {
            this.updateList(i);
        }
        
        updateEmptyLists(); //pads any lists that didn't get updates with empty strings
        
        //add a new query for each row in the table
        for(int i = 0; i < this.dataTable.getRowCount(); i++) {
            queries.add(this.createQueryFromRow(i));
        }
        
        return queries;
    }
    
    
    //Copies the table data from the given column number into one of our lists
    private void updateList(int colNum) {
        TableColumn column = this.dataTable.getColumnModel().getColumn(colNum);
        String header = column.getHeaderValue().toString();
        ArrayList<String> currentList = null;
        
        //More ugliness... should be one entry here for every private list member above
             if(header.equals("employee_first_name"))       currentList = employee_first_name;
        else if(header.equals("employee_last_name"))        currentList = employee_last_name;
        else if(header.equals("employee_middle_initial"))   currentList = employee_middle_initial;
        else if(header.equals("employee_ssn"))              currentList = employee_ssn;
        else if(header.equals("employee_address"))          currentList = employee_address;
        else if(header.equals("employee_address2"))         currentList = employee_address2;
        else if(header.equals("employee_city"))             currentList = employee_city;
        else if(header.equals("employee_state"))            currentList = employee_state;
        else if(header.equals("employee_zip"))              currentList = employee_zip;
        else if(header.equals("employee_phone"))            currentList = employee_phone;
        else if(header.equals("employee_phone2"))           currentList = employee_phone2;
        else if(header.equals("employee_pager"))            currentList = employee_pager;
        else if(header.equals("employee_cell"))             currentList = employee_cell;
        else if(header.equals("employee_email"))            currentList = employee_email;
        else if(header.equals("employee_birthdate"))        currentList = employee_birthdate;
        else if(header.equals("employee_hire_date"))        currentList = employee_hire_date;
        
        if(currentList == null)
            return;
        
        for(int i = 0; i < this.dataTable.getRowCount(); i++) {
            currentList.add(this.dataTable.getModel().getValueAt(i, colNum).toString());
        }
    }
    
    
    //Fills any of our lists that didn't get updates with empty strings
    private void updateEmptyLists() {
        
        for(int i = 0; i < this.allLists.size(); i++) {
            if(this.allLists.get(i).size() == 0) {
                for(int c = 0; c < this.dataTable.getRowCount(); c++) {
                    this.allLists.get(i).add("");
                }
            }
        }
    }
    
    
    //Function that creates a single insertion query for the given table row number
    private GeneralQueryFormat createQueryFromRow(int rowNum) {
        String fname    = this.employee_first_name.get(rowNum);
        String lname    = this.employee_last_name.get(rowNum);
        String mname    = this.employee_middle_initial.get(rowNum);
        String ssn      = this.employee_ssn.get(rowNum);
        String address  = this.employee_address.get(rowNum);
        String address2 = this.employee_address2.get(rowNum);
        String city     = this.employee_city.get(rowNum);
        String state    = this.employee_state.get(rowNum);
        String zip      = this.employee_zip.get(rowNum);
        String phone    = this.employee_phone.get(rowNum);
        String phone2   = this.employee_phone2.get(rowNum);
        String pager    = this.employee_pager.get(rowNum);
        String cell     = this.employee_cell.get(rowNum);
        String email    = this.employee_email.get(rowNum);
        String bdate    = this.employee_birthdate.get(rowNum);
        String branchId = this.branch.getBranchId() + "";
        String hireDate = this.employee_hire_date.get(rowNum);
        
        ssn = ssn.replaceAll("-", "");
        if(bdate.equals("")) bdate = "01/01/1000";
        if(hireDate.equals("")) hireDate = "NOW()";
        
        employee_save_query query = new employee_save_query();
        query.setCompany(company.getName());
        //query.update(fname, lname, mname, phone, phone2, cell, pager, address, address2, city, state, zip, ssn, email, hireDate, "2100-10-10",
        //            "(CASE WHEN (SELECT (MAX(employee_id) + 1) From employee) IS NULL THEN 1 ELSE (SELECT (MAX(employee_id) + 1) From employee) END)",
         //           "0", false, bdate, this.branch.getId());
        return query;
    }
    
    
    /* 
     * Validation function called when the user clicks the import button.  Just checks to see if they've
     * assigned something, and that they haven't assigned the same thing to more than one column.  Returns
     * true if everything is ok, false if there's a problem
     */
    private boolean checkAssignments() {
        ArrayList headerNames = new ArrayList();
        boolean assigned = false;
        
        for(int i = 0; i < this.dataTable.getColumnCount(); i++) {
            Object columnName = this.dataTable.getColumnModel().getColumn(i).getHeaderValue();
            
            if(!columnName.toString().equals("Unassigned")) {
                if(!headerNames.contains(columnName)) {
                    headerNames.add(columnName);
                    assigned = true;
                } else {
                    JOptionPane.showMessageDialog(this, columnName.toString() + " has been assigned more than once!", "Duplicate Assignment!", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        
        if(!assigned) {
            JOptionPane.showMessageDialog(this, "You must assign at least one column!", "Nothing assigned!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        dataTablePopUp = new javax.swing.JPopupMenu();
        directMenu = new javax.swing.JMenu();
        fnameMenuItem = new javax.swing.JMenuItem();
        lnameMenuItem = new javax.swing.JMenuItem();
        mnameMenuItem = new javax.swing.JMenuItem();
        ssnMenuItem = new javax.swing.JMenuItem();
        addressMenuItem = new javax.swing.JMenuItem();
        address2MenuItem = new javax.swing.JMenuItem();
        cityMenuItem = new javax.swing.JMenuItem();
        stateMenuItem = new javax.swing.JMenuItem();
        zipMenuItem = new javax.swing.JMenuItem();
        phoneMenuItem = new javax.swing.JMenuItem();
        phone2MenuItem = new javax.swing.JMenuItem();
        pagerMenuItem = new javax.swing.JMenuItem();
        cellMenuItem = new javax.swing.JMenuItem();
        emailMenuItem = new javax.swing.JMenuItem();
        dobMenuItem = new javax.swing.JMenuItem();
        hiredateMenuItem = new javax.swing.JMenuItem();
        functionMenu = new javax.swing.JMenu();
        clearMenuItem = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        cancelButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();

        directMenu.setText("Direct Assign");
        fnameMenuItem.setText("employee_first_name");
        fnameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(fnameMenuItem);

        lnameMenuItem.setText("employee_last_name");
        lnameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(lnameMenuItem);

        mnameMenuItem.setText("employee_middle_initial");
        mnameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(mnameMenuItem);

        ssnMenuItem.setText("employee_ssn");
        ssnMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(ssnMenuItem);

        addressMenuItem.setText("employee_address");
        addressMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(addressMenuItem);

        address2MenuItem.setText("employee_address2");
        address2MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(address2MenuItem);

        cityMenuItem.setText("employee_city");
        cityMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(cityMenuItem);

        stateMenuItem.setText("employee_state");
        stateMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(stateMenuItem);

        zipMenuItem.setText("employee_zip");
        zipMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(zipMenuItem);

        phoneMenuItem.setText("employee_phone");
        phoneMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(phoneMenuItem);

        phone2MenuItem.setText("employee_phone2");
        phone2MenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(phone2MenuItem);

        pagerMenuItem.setText("employee_pager");
        pagerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(pagerMenuItem);

        cellMenuItem.setText("employee_cell");
        cellMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(cellMenuItem);

        emailMenuItem.setText("employee_email");
        emailMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(emailMenuItem);

        dobMenuItem.setText("employee_birthdate");
        dobMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(dobMenuItem);

        hiredateMenuItem.setText("employee_hire_date");
        hiredateMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directMenuClick(evt);
            }
        });

        directMenu.add(hiredateMenuItem);

        dataTablePopUp.add(directMenu);

        functionMenu.setText("Assign Function");
        dataTablePopUp.add(functionMenu);

        clearMenuItem.setText("Unassign Column");
        clearMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearMenuItemActionPerformed(evt);
            }
        });

        dataTablePopUp.add(clearMenuItem);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setFrameIcon(null);
        jScrollPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jScrollPane1ComponentResized(evt);
            }
        });

        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        dataTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        dataTable.setColumnSelectionAllowed(true);
        dataTable.setRowSelectionAllowed(false);
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dataTablePopupHandler(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                dataTablePopupHandler(evt);
            }
        });

        jScrollPane1.setViewportView(dataTable);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        importButton.setText("Import");
        importButton.setEnabled(false);
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(244, Short.MAX_VALUE)
                .add(importButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cancelButton)
                .addContainerGap())
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(importButton))
                .addContainerGap())
        );
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-620)/2, (screenSize.height-400)/2, 620, 400);
    }// </editor-fold>//GEN-END:initComponents

    private void jScrollPane1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jScrollPane1ComponentResized
        if(evt.getComponent().getSize().getWidth() > this.dataTable.getColumnCount()*150)
            this.dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        else
            this.dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }//GEN-LAST:event_jScrollPane1ComponentResized

    private void clearMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearMenuItemActionPerformed
        this.dataTable.getColumnModel().getColumn(this.dataTable.getSelectedColumn()).setHeaderValue("Unassigned");
        this.dataTable.getTableHeader().repaint();
    }//GEN-LAST:event_clearMenuItemActionPerformed

    private void directMenuClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directMenuClick
        JMenuItem menuItem = (JMenuItem)evt.getSource();
        
        this.dataTable.getColumnModel().getColumn(this.dataTable.getSelectedColumn()).setHeaderValue(menuItem.getText());
        this.dataTable.getTableHeader().repaint();
    }//GEN-LAST:event_directMenuClick

    private void dataTablePopupHandler(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTablePopupHandler
        if(evt.isPopupTrigger()) {
            TableColumnModel colModel = this.dataTable.getColumnModel();
            int viewIndex = colModel.getColumnIndexAtX(evt.getX());
            int modelIndex = this.dataTable.convertColumnIndexToModel(viewIndex);

            this.dataTable.changeSelection(0, modelIndex, false, false);
            this.dataTablePopUp.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_dataTablePopupHandler

    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        if(!this.checkAssignments()) {
            return;
        }
        
        rmischedule.data_connection.Connection conn = new rmischedule.data_connection.Connection();
        conn.myBranch  = this.branch.getBranchId() + "";
        conn.myCompany = this.company.getId();
        conn.executeQueryEx(this.setupQueries());
    }//GEN-LAST:event_importButtonActionPerformed

    
    
    
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem address2MenuItem;
    private javax.swing.JMenuItem addressMenuItem;
    private javax.swing.JButton cancelButton;
    private javax.swing.JMenuItem cellMenuItem;
    private javax.swing.JMenuItem cityMenuItem;
    private javax.swing.JMenuItem clearMenuItem;
    private javax.swing.JTable dataTable;
    private javax.swing.JPopupMenu dataTablePopUp;
    private javax.swing.JMenu directMenu;
    private javax.swing.JMenuItem dobMenuItem;
    private javax.swing.JMenuItem emailMenuItem;
    private javax.swing.JMenuItem fnameMenuItem;
    private javax.swing.JMenu functionMenu;
    private javax.swing.JMenuItem hiredateMenuItem;
    private javax.swing.JButton importButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem lnameMenuItem;
    private javax.swing.JMenuItem mnameMenuItem;
    private javax.swing.JMenuItem pagerMenuItem;
    private javax.swing.JMenuItem phone2MenuItem;
    private javax.swing.JMenuItem phoneMenuItem;
    private javax.swing.JMenuItem ssnMenuItem;
    private javax.swing.JMenuItem stateMenuItem;
    private javax.swing.JMenuItem zipMenuItem;
    // End of variables declaration//GEN-END:variables
 
    
    //Stupid class to make our cell data uneditable
    private class MyTableModel extends DefaultTableModel {

        public boolean isCellEditable(int r, int c) {
            return false;
        }
    }
}
