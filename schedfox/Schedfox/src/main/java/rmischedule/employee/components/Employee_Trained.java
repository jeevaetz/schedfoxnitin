/*
 * Employee_Note.java
 *
 * Created on August 12, 2004, 10:05 AM
 */

package rmischedule.employee.components;

import rmischedule.main.Main_Window;
import rmischedule.components.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import rmischedule.components.List_View_Edit_Action;
import schedfoxlib.model.util.Record_Set;
import rmischedule.employee.*;
import rmischedule.security.*;
import rmischedule.main.*;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.components.List_View;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.training.save_trained_or_not_query;
/**
 *
 * @author  jason.allen
 */
public class Employee_Trained extends GenericEditSubForm {   
    private List_View trainedList;
    private String employeeId;
    private employee_trained_list_query myTrainedQuery;
    private Record_Set rs;
    
    /** Creates a new instance of Employee_Note */
    public Employee_Trained(){
        setLayout(new BorderLayout());
        myTrainedQuery = new employee_trained_list_query();
        rs = new Record_Set();

        trainedList = new List_View();
        trainedList.addColumn("id"    , List_View.STRING, false, false);
        trainedList.addColumn("Mark As Trained", List_View.BOOLEAN, true, true, 90);
        trainedList.addColumn("Trained By Hours", List_View.BOOLEAN, false, true,90);    
        trainedList.addColumn("Location", List_View.STRING, false, true, 380);        
        
        trainedList.buildTable();
        trainedList.maximizeTable();
        //registerListView(trainedList);
        trainedList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                saveValue();
            }
        }
        );
        add(trainedList.myScrollPane, BorderLayout.CENTER);
    }    
    
    public void saveValue() {
        save_trained_or_not_query myUpdateQuery = new save_trained_or_not_query();
        myUpdateQuery.update(((String)trainedList.getSelectedCell(0)), myparent.getMyIdForSave(), ((Boolean)trainedList.getSelectedCell(1)));
        try {
            myparent.getConnection().executeUpdate(myUpdateQuery);
        } catch (Exception e) {}
    }
    
    public void saveData(int row){
        employee_trained_update EmpUpdateQuery = new employee_trained_update();
        EmpUpdateQuery.update(employeeId, 
                             (((Boolean)trainedList.getTrueValueAt(row, 2))).toString(),
                             ((String)trainedList.getTrueValueAt(row, 0)));
        try {
            myparent.getConnection().executeUpdate(EmpUpdateQuery);
        } catch (Exception e) {}  
    }

    public GeneralQueryFormat getQuery(boolean isSelected) {
        myTrainedQuery.update(myparent.getMyIdForSave());
        return myTrainedQuery;
    }

    public void loadData(Record_Set rs) {
        trainedList.clearRows();
        for (int i = 0; i < rs.length(); i++) {
            Object[] nextRow = new Object[4];
            nextRow[0] = rs.getString("id");
            Boolean isOverride = false;
            if (rs.getString("override").equals("1")) {
                isOverride = true;
            }
            nextRow[1] = isOverride;
            Boolean isTrained = false;
            if (rs.getString("tot").equals("t") || rs.getString("clienttime").equals("0")) {
                isTrained = true;
            }
            nextRow[2] = isTrained;
            nextRow[3] = rs.getString("cname");
            trainedList.addRow(nextRow);
            rs.moveNext();
        }
        trainedList.fireTableDataChanged();
    }

    public boolean needsMoreRecordSets() {
        return false;
    }

    public String getMyTabTitle() {
        return "Trained";
    }

    public JPanel getMyForm() {
        return this;
    }
    
    public void doOnClear() {
        trainedList.clearRows();
        trainedList.fireTableDataChanged();
    }

    public GeneralQueryFormat getSaveQuery(boolean isNew) {
        return null;
    }
    
    public boolean userHasAccess() {
        return Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.EMPLOYEE_EDIT);
    }

}
