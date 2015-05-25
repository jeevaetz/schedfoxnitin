/*
 * GenericTabbedEditForm.java
 *
 * Created on September 7, 2005, 11:16 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.components.graphicalcomponents;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Ira Juneau  
 */
public abstract class GenericTabbedEditForm extends GenericEditForm {
    
    protected JTabbedPane myTabbedPane;
    
    /** Creates a new instance of GenericTabbedEditForm */
    public GenericTabbedEditForm() {
        myTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        super.MainPanel.add(myTabbedPane);
    }
    

    public void hideEmpList(){
        super.hideEmpList();
    }
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }
    
    public void runOnClickUser(Object objectContained, boolean selected) {
        super.runOnClickUser(objectContained, selected);
        
    }
    
    public void setSelectedTab(Component select) {
        myTabbedPane.setSelectedComponent(select);
    }

    public int getSelectedTabIndex() {
        return myTabbedPane.getSelectedIndex();
    }
    
    public void setEnabledTab(Component select, boolean enable) {
        int position = 0;
        for (int i = 0; i < myTabbedPane.getTabCount(); i++) {
            if (myTabbedPane.getComponentAt(i) == select) {
                position = i;
            }
        }
        myTabbedPane.setEnabledAt(position, enable);
    }
    
    public void saveData() {
        super.saveData();
    }

    /**
     * Clears out the tabs and the associated data
     */
    public void clearSubForms() {
        getParentPanel().removeAll();
        mySubPanels.removeAllElements();
    }

    public void addSubForm(GenericEditSubForm myNewForm, boolean isVisible) {
        if (myNewForm.userHasAccess()) {
            getParentPanel().addTab(myNewForm.getMyTabTitle(), myNewForm.getMyForm());
            getParentPanel().setEnabledAt(getParentPanel().getTabCount() - 1, true);
            mySubPanels.add(myNewForm);
            myNewForm.setMyParent(this);
            //myNewForm.setVisible(isVisible);
        } 
    }

    public void addSubForm(GenericEditSubForm myNewForm) {
        this.addSubForm(myNewForm, true);
    }

    public JTabbedPane getParentPanel() {
        return myTabbedPane;
    }
}
