/*
 * GenericEditSubForm.java
 *
 * Created on September 7, 2005, 11:21 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package rmischedule.components.graphicalcomponents;
import rmischedule.components.*;
import rmischedule.client.components.*;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.util.Record_Set;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import rmischedule.data_connection.Connection;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
/**
 *
 * @author Ira Juneau
 */
public abstract class GenericEditSubForm extends JPanel {
    
    protected Vector<JTextComponent> myVectorOfTextFields;
    protected Vector<List_View> myVectorOfListViews;
    protected Vector<Client_Worksite_Item> myVectorOfWorksites;
    protected Vector<JComboBox> myVectorOfCombobox;
    protected Vector<JTable> myVectorOfJTable;
    protected String originalLoadString;
    protected GenericEditForm myparent;
    
    public void GenericEditSubForm() {
        originalLoadString = new String();
    }
    
    public void setMyParent(GenericEditForm parent) {
        myparent = parent;
    }

    public GenericEditForm getMyParent() {
        return myparent;
    }

    public void reloadData() {
        this.clearData();
        GeneralQueryFormat myFormat = this.getQuery(true);
        Connection myConn = myparent.getConnection();
        myConn.prepQuery(myFormat);
        RunQueriesEx myQueries = new RunQueriesEx();
        myQueries.add(myFormat);
        myConn.prepQuery(myQueries);
        ArrayList<Record_Set> rst = myConn.executeQueryEx(myQueries);
        for (int r = 0; r < rst.size(); r++) {
            this.loadData(rst.get(r));
        }
    }

    /**
     * Override if you want this form to check data...and not save query if condition is
     * not met.
     */
    public String checkData() {
        return null;
    }
    
    public void registerListView(List_View lv) {
        if (myVectorOfListViews == null) {
            myVectorOfListViews  = new Vector();
        }
        try {
            lv.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e){
                    myparent.checkIfSubFormsChanged();
                }
            });
            lv.addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    myparent.checkIfSubFormsChanged();
                }
            });
            myVectorOfListViews.add(lv);
        } catch (Exception e) {}
    }
    
    public void registerWorksite(Client_Worksite_Item cliWork) {
        if (myVectorOfWorksites == null) {
            myVectorOfWorksites = new Vector();
        }
        cliWork.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e) {
                myparent.checkIfSubFormsChanged();
            }
        });
        myVectorOfWorksites.add(cliWork);
    }
    
    private void initializeVectorOfText() {
        myVectorOfTextFields = new Vector();
        myVectorOfWorksites = new Vector();
        myVectorOfCombobox = new Vector();
        myVectorOfJTable = new Vector();
        if (myVectorOfListViews == null) {
            myVectorOfListViews  = new Vector();
        }
        addChildrenToVector(this);
        for (int i = 0; i < myVectorOfTextFields.size(); i++) {
            myVectorOfTextFields.get(i).addKeyListener(new myTextListener());
        }
        for (int i = 0; i < myVectorOfCombobox.size(); i++) {
            myVectorOfCombobox.get(i).addItemListener(new myComboListener());
        }
    }
    
    private void getOriginalLoadString() {
        originalLoadString = new String();
        if (myVectorOfTextFields == null) {
            initializeVectorOfText();
        }
        for (int i = 0; i < myVectorOfTextFields.size(); i++) {
            originalLoadString = originalLoadString + myVectorOfTextFields.get(i).getText();
        }
        for (int i = 0; i < myVectorOfListViews.size(); i++) {
            originalLoadString = originalLoadString + myVectorOfListViews.get(i).toString();
        }
        for (int i = 0; i < myVectorOfCombobox.size(); i++) {
            try {
                originalLoadString = originalLoadString + myVectorOfCombobox.get(i).getSelectedItem().toString();
            } catch (Exception e) {}
        }
    }
    
    /**
     * Has the form data changed at all from initial load?
     */
    public boolean hasChanged() {
        String compareString = new String();
        if (myVectorOfTextFields == null) {
            initializeVectorOfText();
        }
        for (int i = 0; i < myVectorOfTextFields.size(); i++) {
            compareString = compareString + myVectorOfTextFields.get(i).getText();
        }
        for (int i = 0; i < myVectorOfListViews.size(); i++) {
            compareString = compareString + myVectorOfListViews.get(i).toString();
        }
        for (int i = 0; i < myVectorOfWorksites.size(); i++) {
            compareString = compareString + myVectorOfWorksites.get(i).toString();
        }
        for (int i = 0; i < myVectorOfCombobox.size(); i++) {
            try {
                compareString = compareString + myVectorOfCombobox.get(i).getSelectedItem().toString();
            } catch (Exception e) {

            }
        }
        return !compareString.equals(originalLoadString);
    }
    
    private void addChildrenToVector(JPanel parent) {
        Component currComp;
        for (int i = 0; i < parent.getComponentCount(); i++) {
            currComp = parent.getComponent(i);
            try {
                myVectorOfTextFields.add((JTextComponent)currComp);
            } catch (Exception e) {
                try {
                    myVectorOfCombobox.add((JComboBox)currComp);
                } catch (Exception exe) {
                    try {
                        myVectorOfCombobox.add(((myComboBoxInterface)currComp).getComboBox());
                    } catch (Exception exec) {
                        try {
                            myVectorOfJTable.add((JTable)currComp);
                        } catch (Exception execept) {
                            try {
                                addChildrenToVector((JPanel)currComp);
                            } catch (Exception x) {
                            }
                        }
                    }
                }
                
            }
        }
    }
    
    public void loadDataAndProcess(Record_Set rs) {
        loadData(rs);
        getOriginalLoadString();
        
        this.revalidate();
        this.repaint();
    }
    
    public void clearData() {
        if (myVectorOfTextFields == null) {
            initializeVectorOfText();
        }
        for (int i = 0; i < myVectorOfTextFields.size(); i++) {
            myVectorOfTextFields.get(i).setText("");
        }
        
        doOnClear();
        getOriginalLoadString();
    }
    
    public abstract GeneralQueryFormat getQuery(boolean isSelected);
    public abstract GeneralQueryFormat getSaveQuery(boolean isNewData);
    public abstract void loadData(Record_Set rs);
    public abstract boolean needsMoreRecordSets();
    public abstract String getMyTabTitle();
    public abstract JPanel getMyForm();
    public abstract void doOnClear();
    /**
     * Does user have access as defined by security_detail, if not tab disabled!
     */
    public abstract boolean userHasAccess();
    
    private class myComboListener implements ItemListener {
        public myComboListener() {
            
        }
        
        public void itemStateChanged(ItemEvent e) {
            myparent.checkIfSubFormsChanged();
        }
    }
    
    /**
     * Listener to check all text boxes components, check if all fields are empty
     */
    private class myTextListener extends KeyAdapter {
        public myTextListener() {
            
        }
        
        public void keyReleased(KeyEvent e) {
            myparent.checkIfSubFormsChanged();
        }
    }
}
