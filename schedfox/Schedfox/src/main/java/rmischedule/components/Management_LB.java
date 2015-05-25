/*
 * Branch_LB.java
 *
 * Created on February 9, 2004, 10:17 AM
 */

package rmischedule.components;

import rmischedule.main.Main_Window;
import rmischedule.components.*;
import rmischedule.components.graphicalcomponents.*;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.util.ArrayList;

import javax.swing.*;
import org.w3c.dom.*;
import rmischeduleserver.data_connection_types.JWebServiceData;
import schedfoxlib.model.util.Record_Set;
import rmischedule.data_connection.Connection;
import rmischeduleserver.mysqlconnectivity.queries.util.management_list_query;
/**
 *
 * @author  jason.allen
 */
public class Management_LB extends javax.swing.JPanel implements myComboBoxInterface{
    
    private JPanel    jpCombo;
    private JComboBox lbNoteType;
    
    private JPanel     jpAdd;
    private JTextField efNoteType;
    
    private String[] managementId;
    private String[] managementName;
    
    private String editmanagementId;
    
    private Connection myConn;
    
    private ActionListener myActionListener = null;
    private management_list_query myListQuery;
    
    /** Creates a new instance of branch_lb */
    public Management_LB(Connection conn){
        int c, i;
        myListQuery = new management_list_query();
        myConn = conn;
        
        jpCombo = new JPanel(new BorderLayout());
        JPanel comboButton  = new JPanel(new BorderLayout());
        JButton pbComboAdd  = new JButton("Add");
        JButton pbComboEdit = new JButton("Edit");
        comboButton.add(pbComboAdd, BorderLayout.WEST);
        comboButton.add(pbComboEdit, BorderLayout.EAST);
        
        setLayout(new BorderLayout());
        jpCombo.add(new JLabel("Management Co.: "), BorderLayout.WEST);
        
        lbNoteType = new JComboBox();
        jpCombo.add(lbNoteType, BorderLayout.CENTER);
        jpCombo.add(comboButton, BorderLayout.EAST);
        
        loadNoteType();
        
        lbNoteType.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(myActionListener != null){
                        myActionListener.actionPerformed(e);
                    }
                }
            }
        );
        
        add(jpCombo, BorderLayout.CENTER);

        jpAdd               = new JPanel(new BorderLayout());
        efNoteType          = new JTextField();
        JPanel addButton  = new JPanel(new BorderLayout());
        JButton pbAddOk     = new JButton("OK");
        JButton pbAddCancel = new JButton("Cancel");

        jpAdd.add(new JLabel("Management Co.: "), BorderLayout.WEST);
        jpAdd.add(efNoteType, BorderLayout.CENTER);
        
        addButton.add(pbAddOk, BorderLayout.WEST);
        addButton.add(pbAddCancel, BorderLayout.EAST);
        jpAdd.add(addButton, BorderLayout.EAST);
        
        pbComboEdit.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    addButtonAction(false);
                }
            }
        );

        pbComboAdd.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    addButtonAction(true);
                }
            }
        );

        pbAddOk.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    addNoteType();
                }
            }
        );

        pbAddCancel.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    cancelButtonAction();
                }
            }
        );
    }   
    
    public void loadNoteType(){
        int i, c;
        
        lbNoteType.removeAllItems();
        myListQuery.update();
        Record_Set rs = new Record_Set();
        try {
            rs = myConn.executeQuery(myListQuery);
        } catch (Exception e) {}

        c = 1;
        if(rs.length() > 0){
            c = rs.length() + 1;
            managementId   = new String[c];
            managementName = new String[c];
            i = 1;
            
            do{
                  managementId[i] = rs.getString("id");
                managementName[i] = rs.getString("name");
                i++;
            }while(rs.moveNext());
            
        }else{
            managementId   = new String[1];
            managementName = new String[1];            
        }
        
          managementId[0] = "0";
        managementName[0] = "None";
        
        for(i=0;i<c;i++){
            lbNoteType.addItem(managementName[i]);
        }
        
        
    }
    
    
    public void setNoteType(String bId){
        int c, i;
        c = managementName.length;
        for(i=0;i<c;i++){
            if(managementId[i].equals(bId)){
               lbNoteType.setSelectedItem(managementName[i]);
               break;
            }
        }
    }
    
    public int getLength(){
        return managementId.length;
    }
    
    public void addActionListener(ActionListener al){
        myActionListener = al;
    }
    
    public int getSelectedIndex(){
        return lbNoteType.getSelectedIndex();
    }
    
    public String getSelectedItem(){
        return (String) lbNoteType.getSelectedItem();
    }
    
    public String getSelectedmanagementId(){
        return managementId[lbNoteType.getSelectedIndex()];
    }
    
    public String getSelectedmanagementName(){
        return managementName[lbNoteType.getSelectedIndex()];
    }    
    
    public void setNoteTypeByName(String name){
        lbNoteType.setSelectedItem(name);
    }
    
    public void addNoteType(){
        JWebServiceData jwsd = new JWebServiceData();
        jwsd.addData("name", efNoteType.getText());
        jwsd.addData("id", editmanagementId);
        
//        myConn.getInfo(
//            "util/management_update.php?", jwsd, "notetype"
//        );
        
        loadNoteType();
        setNoteTypeByName(efNoteType.getText());
        cancelButtonAction();
    }
    
    public void addButtonAction(boolean add){
        if(add){
            editmanagementId = "0";
        }else{
            editmanagementId = getSelectedmanagementId();
            efNoteType.setText(getSelectedmanagementName());
        }
        remove(jpCombo);
        add(jpAdd, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    public void cancelButtonAction(){        
        remove(jpAdd);
        add(jpCombo, BorderLayout.CENTER);
        revalidate();
        repaint();
        editmanagementId = "0";
        efNoteType.setText("");
    }

    public JComboBox getComboBox() {
        return lbNoteType;
    }
}
