/*
 * GenericEditForm.java
 *
 * Created on September 7, 2005, 10:15 AM
 */
package rmischedule.components.graphicalcomponents;

import schedfoxlib.model.util.Record_Set;
import rmischedule.data_connection.*;
import rmischedule.main.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.*;
import rmischeduleserver.data_connection_types.*;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author Ira Juneau
 */
public abstract class GenericEditForm extends javax.swing.JInternalFrame {

    protected GenericListContainer myListContainer;
    protected Vector<GenericEditSubForm> mySubPanels;
    protected Vector<GraphicalListComponent> myListComponents;
    protected int spaceBetweenToolBarIcons = 10;
    protected boolean isAddingNew;
    protected boolean hasChanged;
    protected Object currentSelectedObject;
    protected myToolBarIcons myToggleDeletedIcon;
    protected myToolBarIcons mySaveIcon;
    protected myToolBarIcons myAddIcon;
    protected myToolBarIcons myDeleteIcon;
    protected myToolBarIcons myExitIcon;
    protected myToolBarIcons myApprovalIcon;
    protected myToolBarIcons mySendIcon;
    protected myToolBarIcons myResetIcon;
    protected myToolBarIcons myOtherIcon;
    protected myToolBarIcons myOther2Icon;

    /**
     * Creates new form GenericEditForm
     */
    public GenericEditForm() {
        initComponents();
        if (Main_Window.parentOfApplication.loginType.equalsIgnoreCase("NewEmployee")) {
            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        }
        currentSelectedObject = null;
        hasChanged = false;
        isAddingNew = true;
        mySubPanels = new Vector();
        myListContainer = this.createListContainer();
        ContainerPanel.add(myListContainer);
        addMyMenu(myMenu);
        getData();
        setUpToolBar();
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                if (hasChanged) {
                    promptUserToSave();
                }
            }

            public void internalFrameOpened(InternalFrameEvent e) {
                addData();
            }
        });
        setTitle(getWindowTitle());
        myToggleDeletedIcon = new myToolBarIcons(getDeletedUpIcon(), getDeletedDownIcon(), "", "") {
            @Override
            protected void runOnClick() {
                myToggleDeletedIcon.setPressed(!myToggleDeletedIcon.getPressed());
                showDeleted(myToggleDeletedIcon.getPressed());
            }
        };

        ToggleDeletePanel.add(myToggleDeletedIcon);
        if (!getToggleDeleted()) {
            ToggleDeletePanel.setVisible(false);
        }
    }

    public void setCurrentSelectedObjectDirectly(Object obj) {
        this.currentSelectedObject = obj;
    }

    /**
     * Override if you want to change the generic list container behaviour
     *
     * @return GenericListContainer
     */
    protected GenericListContainer createListContainer() {
        return new GenericListContainer(this);
    }

    protected void hideEmpList() {
        MainContainerPanel.setVisible(false);
    }

    protected void showApprovalButton() {
        myToolBar.removeAll();
        myToolBar.add(myApprovalIcon);
        myToolBar.add(createSpacerPanel());
        myToolBar.add(myExitIcon);
    }

    protected void showDeleted(boolean isPressed) {
    }

    protected ImageIcon getDeletedUpIcon() {
        return null;
    }

    protected ImageIcon getDeletedDownIcon() {
        return null;
    }

    protected String getAddString() {
        return "NEW FORM";
    }

    protected String getSaveString() {
        return "SAVE";
    }

    protected String getDeletedString() {
        return "DELETE";
    }

    protected String getUndeleteString() {
        return "DELETE";
    }

    protected String getOtherButton2String() {
        return "";
    }

    protected String getOtherButtonString() {
        return "";
    }

    protected ImageIcon getOtherButton2Icon() {
        return Main_Window.EmailReport_RunReport_16x16px;
    }

    protected ImageIcon getOtherButtonIcon() {
        return Main_Window.EmailReport_RunReport_16x16px;
    }

    protected boolean hasAddData() {
        return true;
    }

    protected boolean hasDelete() {
        return true;
    }

    protected void setUpToolBar() {
        mySaveIcon = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
                saveData();
            }
        };
        myAddIcon = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
                addData();
            }
        };
        myDeleteIcon = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
                deleteData();
            }
        };
        myExitIcon = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
                setFormVisible(false);
            }
        };
        myApprovalIcon = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
                saveData();
            }
        };

        myAddIcon.setToolTipText("Add New");
        myAddIcon.setText(getAddString(), new Font("Dialog", Font.BOLD, 14));
        mySaveIcon.setToolTipText("Save Data");
        mySaveIcon.setText(getSaveString(), new Font("Dialog", Font.BOLD, 14));
        myDeleteIcon.setToolTipText("Delete Data");
        myDeleteIcon.setText(getDeletedString(), new Font("Dialog", Font.BOLD, 14));
        myExitIcon.setToolTipText("Exit");
        myExitIcon.setText("EXIT", new Font("Dialog", Font.BOLD, 14));
        myApprovalIcon.setText("Approve", new Font("Dialog", Font.BOLD, 14));
        mySaveIcon.setIcon(Main_Window.Save_Data_24x24);
        myAddIcon.setIcon(Main_Window.Add_Data_24x24);
        myDeleteIcon.setIcon(Main_Window.Delete_Data_24x24);
        myExitIcon.setIcon(Main_Window.Exit_Form_Icon);
        myApprovalIcon.setIcon(Main_Window.Emp_Approval_Icon);

        //myToolBar.add(createSpacerPanel());
        myToolBar.add(mySaveIcon);
        myToolBar.add(createSpacerPanel());
        if (this.hasAddData()) {
            myToolBar.add(myAddIcon);
        }
        if (!Main_Window.parentOfApplication.loginType.equalsIgnoreCase("NewEmployee")) {
            myToolBar.add(createSpacerPanel());
            if (this.hasDelete()) {
                myToolBar.add(myDeleteIcon);
            }
            if (this.getOtherButtonString().trim().length() > 0) {
                myOtherIcon = new myToolBarIcons() {
                    @Override
                    protected void runOnClick() {
                        otherAction();
                    }
                };
                myOtherIcon.setText(this.getOtherButtonString(), new Font("Dialog", Font.BOLD, 14));
                myOtherIcon.setToolTipText(this.getOtherButtonString());
                myOtherIcon.setIcon(getOtherButtonIcon());
                myOtherIcon.setSize(new Dimension(120, 25));
                myToolBar.add(createSpacerPanel());
                myToolBar.add(myOtherIcon);
            }

            if (this.getOtherButton2String().trim().length() > 0) {
                myOther2Icon = new myToolBarIcons() {
                    @Override
                    protected void runOnClick() {
                        other2Action();
                    }
                };
                myOther2Icon.setText(this.getOtherButton2String(), new Font("Dialog", Font.BOLD, 14));
                myOther2Icon.setToolTipText(this.getOtherButton2String());
                myOther2Icon.setIcon(getOtherButton2Icon());
                myOther2Icon.setSize(new Dimension(120, 25));
                myToolBar.add(createSpacerPanel());
                myToolBar.add(myOther2Icon);
            }
            myToolBar.add(createSpacerPanel());
            myToolBar.add(myExitIcon);
        }

        myAddIcon.setSize(new Dimension(120, 25));
        mySaveIcon.setSize(new Dimension(120, 25));
        myDeleteIcon.setSize(new Dimension(120, 25));

    }

    private JPanel createSpacerPanel() {
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        return spacer;
    }

    public void addData() {
        clearData();
        isAddingNew = true;
    }

    public void setFormVisible(boolean isVisible) {
        this.setVisible(isVisible);
    }

    /**
     * Gives you a arrayList of all errors encountered by each sub panel and
     * displays errors to the user why the save can not be run...
     */
    public void displayError(ArrayList errors) {
        String errorString = new String();
        for (int i = 0; i < errors.size(); i++) {
            errorString = (i + 1) + ") " + errorString + errors.get(i) + " \n";
        }
        JOptionPane.showMessageDialog(this, "Could Not Save Data For Following Reasons! \n" + errorString, "Error Saving Data!", JOptionPane.ERROR_MESSAGE);
    }

    protected void other2Action() {
    }

    protected void otherAction() {
    }

    public void saveData() {
        ArrayList errors = checkData();
        if (errors.size() > 0) {
            displayError(errors);
            hasChanged = false;
            return;
        }
        RunQueriesEx myQueriesEx = new RunQueriesEx();
        for (int i = 0; i < mySubPanels.size(); i++) {
            GeneralQueryFormat myCurrQuery = mySubPanels.get(i).getSaveQuery(isAddingNew);
            if (myCurrQuery != null) {
                myQueriesEx.add(myCurrQuery);
                getConnection().prepQuery(myCurrQuery);
            }
        }
        try {
            getConnection().executeQueryEx(myQueriesEx);
        } catch (Exception e) {
        }
        getData();
        JOptionPane.showMessageDialog(this, "Information Saved", "Save Successful", JOptionPane.OK_OPTION);
        hasChanged = false;
    }

    /**
     * Returns a String ArrayList of errors if any occur in the sub forms while
     * saving otherwise it returns empty ArrayList to signify nothing went
     * wrong..
     */
    private ArrayList checkData() {
        ArrayList myStringArray = new ArrayList();
        for (int i = 0; i < mySubPanels.size(); i++) {
            String check = mySubPanels.get(i).checkData();
            if (check != null) {
                myStringArray.add(check);
            }
        }
        return myStringArray;
    }

    /**
     * Clears our list of all graphical components...
     */
    public void clearList() {
        myListContainer.removeAll();
        myListContainer.revalidate();
        myListContainer.repaint();
        setTitle(getWindowTitle());
    }

    /**
     *
     *
     * @return
     */
    public Vector<GraphicalListComponent> getVectorOfObjects() {
        return this.myListContainer.getAddedComponents();
    }

    /**
     * Clears data for all sub forms as defined by the clearData in each form
     */
    public void clearData() {
        currentSelectedObject = null;
        for (int i = 0; i < mySubPanels.size(); i++) {
            mySubPanels.get(i).clearData();
        }
        myListContainer.runOnClear();
        try {
            checkIfSubFormsChanged();
        } catch (Exception e) {
        }
        hasChanged = false;
        setTitle(getWindowTitle());
        try {
            myDeleteIcon.setText(getDeletedString(), new Font("Dialog", Font.BOLD, 14));
        } catch (Exception e) {
        }
    }

    /**
     * Get Panel to add child to overload if you want to add to different panel
     */
    public JComponent getParentPanel() {
        return MainPanel;
    }

    public void addSubForm(GenericEditSubForm myNewForm) {
        getParentPanel().add(myNewForm.getMyForm());
        mySubPanels.add(myNewForm);
        myNewForm.setMyParent(this);
    }

    public Vector getListOfObjects() {
        return myListComponents;
    }
    
    /**
     * Searches through our list of components till it finds one that is equal
     * to the passed in object...as defined by the equals method of the
     * object...
     */
    public void setSelected(Object obj) {
        if (myListComponents == null || myListComponents.isEmpty()) {
            myListComponents = new Vector();
            GraphicalListComponent myComp = new GraphicalListComponent(obj, myListContainer, getDisplayNameForObject(obj));
            myListContainer.add(myComp);
            myListComponents.add(myComp);
        }
        for (int i = 0; i < myListComponents.size(); i++) {
            try {
                if (myListComponents.get(i).getObject().equals(obj)) {
                    runOnClickUser(myListComponents.get(i).getObject(), true);
                    myListComponents.get(i).setSelected(true);
                    final GraphicalListComponent glc = myListComponents.get(i);
                    Thread scrollThread = new Thread() {
                        public void run() {
                            scrollToSelected(glc);
                        }
                    };
                    scrollThread.start();
                    break;
                }
            } catch (Exception e) {
                //Has blank entry in list...
            }
        }
        setTitle(getWindowTitle());
    }

    private void scrollToSelected(GraphicalListComponent glc) {
        JScrollBar sb = this.myListContainer.getVerticalScrollBar();
        sb.setValue(sb.getMinimum());
        sb.validate();
        while (!glc.isValid() && this.isVisible()) {
            try {
                Thread.sleep(10);
            } catch (Exception ex) {
            }
        }

        if (this.isVisible()) {
            this.myListContainer.getViewport().scrollRectToVisible(glc.getBounds());
        }
    }

    public Object getObjectById(String id) {
        for (int i = 0; i < myListComponents.size(); i++) {
            try {
                if (myListComponents.get(i).getObject().equals(id)) {
                    return (myListComponents.get(i).getObject());
                }
            } catch (Exception e) {
            }
        }
        return null;
    }
    
    public void runAfterLoad() {
        
    }

    /**
     * Populates our list given a record set that must be generated by child
     * classes...keeps a vector of objects too for use by children...
     */
    public void populateList(Record_Set rs) {
        populateList(rs, "", "");
    }

    public void populateList(Record_Set rs, String isDeletedInfo, String whenDeleted) {
        myListComponents = new Vector();
        clearList();
        GraphicalListComponent myComp;
        for (int i = 0; i < rs.length(); i++) {
            Object o = createObjectForList(rs);
            if (!isSubComponent(o)) {
                myComp = new GraphicalListComponent(o, myListContainer, getDisplayNameForObject(o));
                myListContainer.add(myComp);
            } else {
                GraphicalListSubComponent mysComp = new GraphicalListSubComponent(o, myListContainer, getDisplayNameForObject(o));
                if (getSubParents(o) != null) {
                    getSubParents(o).addSubComp(mysComp);
                }
                myComp = mysComp;
            }
            if (isDeletedInfo.length() > 0) {
                if (rs.getString(isDeletedInfo).equals(whenDeleted)) {
                    myComp.setBackground(true);
                }
            }
            myListComponents.add(myComp);
            rs.moveNext();
        }

        Object lastSelected = this.currentSelectedObject;
        clearData();

        if (lastSelected != null) {
            this.setSelected(lastSelected);
        }
        //If we only have one item lets select it to make things less confusing
        if (myListComponents.size() == 1) {
            try {
                this.setSelected(myListComponents.get(0).getObject());
            } catch (Exception e) {
                System.out.println("Exception: Could not set selected object to first item in list!");
            }
        }
        runAfterLoad();
    }

    /**
     * If extended classes use sub components extends this method to return when
     * you want a sub component created ie: when the worksite id > 0 for the
     * client_edit example...
     */
    public boolean isSubComponent(Object o) {
        return false;
    }

    public void setEnabledTab(Component select, boolean enable) {
    }

    /**
     * Used when you have sub components like for worksites pass in object
     * extended class will return what GraphicalListComp to add sub to...
     */
    public GraphicalListComponent getSubParents(Object o) {
        return null;
    }

    /**
     * Checks all forms to see if data has changed since initial display...
     */
    public void checkIfSubFormsChanged() {
        hasChanged = false;
        for (int i = 0; i < mySubPanels.size(); i++) {
            if (mySubPanels.get(i).hasChanged()) {
                hasChanged = true;
            }
        }
    }

    protected void promptUserToSave() {
        if (JOptionPane.showConfirmDialog(this, "Data has not been saved. Save current data?",
                "Save Changes?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            saveData();
        }
    }

    /**
     * Returns selected object or null if nothing is selected
     */
    public Object getSelectedObject() {
        return currentSelectedObject;
    }

    public void setVisible(boolean show) {
        super.setVisible(show);
        if (show) {
            try {
                this.setSelected(true);
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Is Selected Component dark? which means it is deleted...
     */
    public boolean selectedIsMarkedDeleted() {
        for (int i = 0; i < myListComponents.size(); i++) {
            if (this.currentSelectedObject == myListComponents.get(i).getObject()) {
                if (myListComponents.get(i).isMarkedDeleted()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Click on a user loads data for all sub forms as defined by there
     * getQuery, and loadData methods...
     */
    public void runOnClickUser(Object objectContained, boolean selected) {
        if (hasChanged) {
            promptUserToSave();
        }
        try {
            //  set cursor
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);

            clearData();
            currentSelectedObject = objectContained;
            if (selectedIsMarkedDeleted()) {
                myDeleteIcon.setText(getUndeleteString(), new Font("Dialog", Font.BOLD, 14));
            } else {
                myDeleteIcon.setText(getDeletedString(), new Font("Dialog", Font.BOLD, 14));
            }

            RunQueriesEx myCompleteQuery = new RunQueriesEx();
            isAddingNew = false;


            for (int i = 0; i < mySubPanels.size(); i++) {
                GeneralQueryFormat myCurrQuery = mySubPanels.get(i).getQuery(selected);
                getConnection().prepQuery(myCurrQuery);
                myCompleteQuery.add(myCurrQuery);
            }
            ArrayList myArrayOfRecordSets = new ArrayList();
            try {

                myArrayOfRecordSets = getConnection().executeQueryEx(myCompleteQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int dataPos = 0;
            for (int i = 0; i < mySubPanels.size(); i++) {
                do {
                    mySubPanels.get(i).loadDataAndProcess((Record_Set) myArrayOfRecordSets.get(dataPos));
                    dataPos++;
                } while (mySubPanels.get(i).needsMoreRecordSets());
            }
            checkIfSubFormsChanged();
            setTitle(getWindowTitle());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //  reset cursor
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
        }
    }

    public abstract String getWindowTitle();

    public abstract Connection getConnection();

    public abstract void getData();

    public abstract String getDisplayNameForObject(Object o);

    public abstract Object createObjectForList(Record_Set input);

    public abstract void addMyMenu(JMenuBar myMenu);

    public abstract String getMyIdForSave();

    public abstract void deleteData();

    /**
     * Override if you want to allow user to toggle between view all and view
     * blah...
     */
    public boolean getToggleDeleted() {
        return false;
    }

    //  awful hack by Ira Juneau to allow custumized closing in messaging
    //      PS:  really, this is Jeff Davis.  Damn, I suck.
    protected void windowHidden() {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainContainerPanel = new javax.swing.JPanel();
        ToggleDeletePanel = new javax.swing.JPanel();
        ContainerPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        MainPanel = new javax.swing.JPanel();
        myToolBar = new javax.swing.JToolBar();
        myMenu = new javax.swing.JMenuBar();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setFrameIcon(null);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                closingWindow(evt);
            }
        });

        MainContainerPanel.setPreferredSize(new java.awt.Dimension(200, 0));
        MainContainerPanel.setLayout(new javax.swing.BoxLayout(MainContainerPanel, javax.swing.BoxLayout.Y_AXIS));

        ToggleDeletePanel.setMaximumSize(new java.awt.Dimension(32767, 24));
        ToggleDeletePanel.setMinimumSize(new java.awt.Dimension(10, 24));
        ToggleDeletePanel.setPreferredSize(new java.awt.Dimension(10, 24));
        ToggleDeletePanel.setLayout(new java.awt.GridLayout(1, 0));
        MainContainerPanel.add(ToggleDeletePanel);

        ContainerPanel.setLayout(new java.awt.GridLayout(1, 0));
        MainContainerPanel.add(ContainerPanel);

        getContentPane().add(MainContainerPanel, java.awt.BorderLayout.WEST);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        MainPanel.setLayout(new java.awt.GridLayout(1, 0));
        jPanel1.add(MainPanel);

        myToolBar.setFloatable(false);
        myToolBar.setMargin(new java.awt.Insets(0, 10, 0, 0));
        myToolBar.setMaximumSize(new java.awt.Dimension(1000, 34));
        myToolBar.setMinimumSize(new java.awt.Dimension(10, 34));
        myToolBar.setPreferredSize(new java.awt.Dimension(10, 34));
        jPanel1.add(myToolBar);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        setJMenuBar(myMenu);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-889)/2, (screenSize.height-506)/2, 889, 506);
    }// </editor-fold>//GEN-END:initComponents

    private void closingWindow(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_closingWindow
        windowHidden();

    }//GEN-LAST:event_closingWindow
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ContainerPanel;
    private javax.swing.JPanel MainContainerPanel;
    protected javax.swing.JPanel MainPanel;
    protected javax.swing.JPanel ToggleDeletePanel;
    private javax.swing.JPanel jPanel1;
    protected javax.swing.JMenuBar myMenu;
    protected javax.swing.JToolBar myToolBar;
    // End of variables declaration//GEN-END:variables
}
