/*
 * Employee_Note.java
 *
 * Created on August 12, 2004, 10:05 AM
 */

package rmischedule.employee.components;

import rmischedule.main.Main_Window;

import java.awt.*;
import java.util.ArrayList;
import rmischedule.security.*;
import rmischedule.components.graphicalcomponents.*;

import javax.swing.*;
import schedfoxlib.model.util.Record_Set;
import rmischedule.components.List_View;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
/**
 *
 * @author  jason.allen
 */
public class Employee_Restrictions extends GenericEditSubForm {  
    
    private List_View restList;
    private List_View bannedList;
    
    /** Creates a new instance of Employee_Note */
    public Employee_Restrictions(){
        setLayout(new GridLayout(0,1));

        JPanel jpRest = new JPanel(new BorderLayout());
        //jpRest.add(new JLabel("Restrictions"), BorderLayout.NORTH);
        
        restList = new List_View();
        restList.addColumn("id"    , List_View.STRING, false, false);
        restList.addColumn("Location", List_View.STRING);        
        restList.addColumn("Restricted", List_View.BOOLEAN, true);        
        restList.buildTable();
        
        //jpRest.add(restList.myScrollPane, BorderLayout.CENTER);

        JPanel jpBanned = new JPanel(new BorderLayout());
        
        bannedList = new List_View();
        bannedList.addColumn("id"    ,      List_View.STRING, false, false);
        bannedList.addColumn("Banned",      List_View.BOOLEAN, true, true, 40);    
        bannedList.addColumn("Location",    List_View.STRING, true, true, 400);        
            
        
        bannedList.buildTable();
        bannedList.maximizeTable();
        registerListView(bannedList);
        
        jpBanned.add(bannedList.myScrollPane, BorderLayout.CENTER);
        
        //add(jpRest);
        add(jpBanned);
    }    


    public void loadData(Record_Set rs) {
        bannedList.clearRows();
        do{
            Object[] o = new Object[3];
            o[0] = rs.getString("cid");
            o[2] = rs.getString("cname");
            o[1] = new Boolean(rs.getString("isbanned"));
            bannedList.addRow(o);
        }while(rs.moveNext());
        bannedList.fireTableDataChanged();
    }

    public boolean needsMoreRecordSets() {
        return false;
    }

    public String getMyTabTitle() {
        return "Banned";
    }

    public JPanel getMyForm() {
        return this;
    }

    public boolean userHasAccess() {
        return Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.EMPLOYEE_EDIT);
    }

    public void doOnClear() {
        bannedList.clearRows();
        bannedList.fireTableDataChanged();
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getQuery(boolean isSelected) {
        employee_banned_list_query myBannedQuery = new employee_banned_list_query();
        myBannedQuery.update(myparent.getMyIdForSave());
        return myBannedQuery;
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getSaveQuery(boolean isNewData) {
        ban_clients_for_employee_update myUpdateQuery = new ban_clients_for_employee_update();
        ArrayList myList = new ArrayList();
        for (int row = 0; row < bannedList.getRowCount(); row++) {
            try {
                if ((Boolean)bannedList.getTrueValueAt(row, 1)) {
                    myList.add(((String)bannedList.getTrueValueAt(row, 0)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        myUpdateQuery.update(myList, myparent.getMyIdForSave());
        try {
            myparent.getConnection().executeQuery(myUpdateQuery);
        } catch (Exception e) {}
        //Done so heartbeat invokes properly
        return null;
    }

}
