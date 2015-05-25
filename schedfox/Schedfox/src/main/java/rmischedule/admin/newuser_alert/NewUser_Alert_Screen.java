/**
 *  FileName:  NewUser_Alert_Screen.java
 *  Created by:  Jeffrey N. Davis
 *  Date Created:  08/03/2010
 *  Purpose of File:  File contains a class designed to view and edit 
 *      information related recieving New User Email Alerts.  This screen is
 *      accessible under the Admin tab if you are a root user.
 *  Modification Information:
 */

//  package declaration
package rmischedule.admin.newuser_alert;

//  import declarations
import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import rmischedule.components.graphicalcomponents.GenericListContainer;
import rmischedule.components.graphicalcomponents.GraphicalListComponent;
import rmischedule.components.graphicalcomponents.myToolBarIcons;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.new_user_alert.delete_user_to_alert_query;
import rmischeduleserver.mysqlconnectivity.queries.new_user_alert.get_users_to_alert_query;
import rmischeduleserver.mysqlconnectivity.queries.new_user_alert.save_users_to_alert_query;


/**
 *  Class Name:  NewUser_Alert_Screen
 *  Purpose of Class:   class designed to view and edit
 *      information related recieving New User Email Alerts.
 */
public class NewUser_Alert_Screen extends javax.swing.JInternalFrame
{
    //  private variable declarations
    private myToolBarIcons myHeaderIcon;
    private myToolBarIcons myNewIcon;
    private myToolBarIcons mySaveIcon;
    private myToolBarIcons myRemoveIcon;
    private myToolBarIcons myExitIcon;
    private NewUser_Alert_SubForm mySubForm;
    private GenericListContainer myListContainer;
    private Vector<GraphicalListComponent> myListComponents;
    private Connection myConnection;
    private static final String WINDOW_TITLE = "New User Alert Window";
    private Object currentlySelectedObject;
    private boolean isSaveValid;

    //  private method implementations
    /**
     *  Method Name:  setUpToolBar
     *  Purpose of Method:  sets up the tool bar with icons and their 
     *      affiliated actions
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  tool bar not set up
     *  Postconditions:  tool bar and actions set up
     */
    private void setUpToolBar()
    {
        //  create new icons
        myNewIcon = new myToolBarIcons();
        mySaveIcon = new myToolBarIcons();
        myRemoveIcon = new myToolBarIcons();
        myExitIcon = new myToolBarIcons();

        //  set icon text
        myNewIcon.setToolTipText(getNewToolTipString());
        myNewIcon.setText(getNewString(), new Font("Dialog", Font.BOLD, 14));
        mySaveIcon.setToolTipText(getSaveToolTipString());
        mySaveIcon.setText(getSaveString(), new Font("Dialog", Font.BOLD, 14));
        myRemoveIcon.setToolTipText(getRemoveToolTipString());
        myRemoveIcon.setText(getRemoveString(),  new Font("Dialog", Font.BOLD, 14));
        myExitIcon.setToolTipText(getExitToolTipString());
        myExitIcon.setText(getExitString(),  new Font("Dialog", Font.BOLD, 14));

        //  set icons
        myNewIcon.setIcon(Main_Window.New_User_Icon_Aero_32x32px);
        mySaveIcon.setIcon(Main_Window.Save_User_Icon_Aero_32x32px);
        myRemoveIcon.setIcon(Main_Window.Remove_User_Icon_Aero_32x32px);
        myExitIcon.setIcon(Main_Window.Exit_Icon_Aero_32x32px);

        //  set size of icons
        myNewIcon.setSize(new Dimension (120, 25));
        mySaveIcon.setSize(new Dimension (120, 25));
        myRemoveIcon.setSize(new Dimension (120, 25));
        myExitIcon.setSize(new Dimension (120, 25));

        //  add icons to tool bar in proper format
        jMyToolBar.add(myNewIcon);
        jMyToolBar.add(createSpacerPanel());
        jMyToolBar.add(mySaveIcon);
        jMyToolBar.add(createSpacerPanel());
        jMyToolBar.add(myRemoveIcon);
        jMyToolBar.add(createSpacerPanel());
        jMyToolBar.add(myExitIcon);

        //  add mouse listeners
        /**
         *  myNewIcon mouse listener implementation
         */
        myNewIcon.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event)
            {
                createNewForm();
            }
        });
        /**
         *  mySaveIcon mouse listener implementation
         */
        mySaveIcon.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event)
            {
                save();
            }
        });
        /**
         *  myRemoveIcon mouse listener implementation
         */
        myRemoveIcon.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event)
            {
                remove();
            }
        });
         /**
         *  myExitIcon mouse listener implementation
         */
        myExitIcon.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event)
            {
                exit();
            }
        });
    }
    
    /**
     *  Method Name:  setUpHeaderPanel
     *  Purpose of Method:  sets the icon for the header panel
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  header panel not setup
     *  Postconditions:  header panel setup
     */
    private void setUpHeaderPanel()
    {
        //  add icon to header panel
        myHeaderIcon = new myToolBarIcons();
        myHeaderIcon.setIcon(Main_Window.New_User_Alert_Panel);
        jHeaderIconPanel.add(myHeaderIcon);
    }

    /**
     *  Method Name:  createSpacePanel
     *  Purpose of Method:  returns a "space" panel to separate icons
     *  Arguments:  none
     *  Returns:  a JPanel for space creation
     *  Precondition:  spacer panel needed
     *  Postcondition:  spacer panel created and returned
     */
    private JPanel createSpacerPanel()
    {
        //  create spacer panel, set properties
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);

        //  return spacer panel
        return spacer;
    }

    /**
     *  Method Name:  getData
     *  Purpose of Method:  method hits the DB with a query, then loads
     *      the data retrieved into the classes data object
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  data from DB not known
     *  Postconditions:  data from DB known, loaded into data structure
     */
    private void getData()
    {
        //  ensure empty list container
        clearList();

        //  create/prep new query, create record set
        get_users_to_alert_query query = new get_users_to_alert_query();
        myConnection.prepQuery(query);
        Record_Set rs = new Record_Set();

        //  execute query
        try
        {
            rs = myConnection.executeQuery(query);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        //  load record set into class data structure
        if(rs.length() > 0)
            loadList(rs);
       
        //  refresh list
        refreshUserList();
    }

    /**
     *  Method Name:  loadList
     *  Purpose of Method:  takes a record set with data and loads that data
     *      into the classes data component
     *  Arguments:  a record set containing the data from DB
     *  Returns:  void
     *  Preconditions:  data retrieved from DB, not loaded into data structure
     *  Postconditions:  data loaded into data structure
     */
    private void loadList(Record_Set rs)
    {
        clearList();
        GraphicalListComponent myComp;
        for (int i = 0; i < rs.length(); i++)
        {
            Object o = createObjectForList(rs);
            myComp = new GraphicalListComponent(o, myListContainer, getDisplayNameForObject(o));
            myListContainer.add(myComp);
            myListComponents.add(myComp);
            rs.moveNext();
        }

        //  sort list
        sortListContainer();
    }

    /**
     *  Method Name:  clearList
     *  Purpose of Method:  clears out the list container
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  list must be cleared out
     *  Postconditions:  list cleared out
     */
    private void clearList()
    {
        myListContainer.removeAll();
        myListContainer.revalidate();
        myListContainer.repaint();
        setTitle(WINDOW_TITLE);
    }

    /**
     *  Method Name:  informError
     *  Purpose of Method:  method informs the user via pop up window that an
     *      error has occurred that has forced this window to not work
     *      properly.
     *  Arguments:  a string containing an error code
     *  Returns:  void
     *  Preconditions:  error at some point in this class, user not informed
     *  Postconditions:  user informed, window hidden
     */
    private void informError(String errorCode)
    {
        JOptionPane.showMessageDialog(null, "Schedfox has encountered and error, " +
            "and must close this screen.  Please contact SchedFox.com.  Error code:  " +
            errorCode, "ERROR IN NEW USER ALERT WINDOW", JOptionPane.ERROR_MESSAGE);
    }

    /**
     *  Method Name:  createObjectForList
     *  Purpose of Method:  loads data into data object, returns the object
     *      to be added to the data structure
     *  Arguments:  a record set containing the data
     *  Returns:  an object
     *  Preconditions:  Data structure needs data parsed into data object
     *  Postconditions:  data from one row of record set loaded into data object
     */
    private Object createObjectForList(Record_Set input)
    {
        //  declaration of data object; object will be returned
        NewUser_Alert_Data data = new NewUser_Alert_Data();

        //  load data into data object
        data.setSSN(input.getInt("user_ssn"));
        data.setFirstName(input.getString("user_first_name"));
        data.setLastName(input.getString("user_last_name"));
        data.setCompany(input.getString(("user_company")));
        data.setPrimaryEmail(input.getString("user_primary_email"));
        data.setAlternateEmail(input.getString("user_alternate_email"));
        data.setTextNumber(input.getString("user_text_number"));
        //  booleans exhibit bizarre behavior from DB
        try
        {
            data.setIsSendText(input.getBoolean("user_send_text"));
            data.setUseAlternateEmail(input.getBoolean("user_use_alternate_email"));
            data.setUseBothEmail(input.getBoolean("user_use_both_email"));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            informError("Record Set failed to recover booleans from database.");
        }

        //  return data object
        return data;
    }

    /**
     *  Method Name:  getDisplayNameForObject
     *  Purpose of Method  looks into the data object and returns a string
     *      represent they display name for the object; in this instance,
     *      a formatted name
     *  Arguments:  an object
     *  Returns:  a string with a formatted name for display
     *  Preconditions:  object being added to data structure, needs display
     *      name for visual representation
     *  Postconditions:  properly formatted string returned containing a name
     */
    private String getDisplayNameForObject(Object object)
    {
        //  declaration of variables
        StringBuffer returnString = new StringBuffer(0);
        NewUser_Alert_Data data =  (NewUser_Alert_Data) object;

        //  format display
        returnString.append(data.getLastName());
        returnString.append(", ");
        returnString.append(data.getFirstName());

        //  return properly formatted string
        return returnString.toString();
    }

    /**
     *  Method Name:  refreshUserList
     *  Purpose of Method:  refreshes list of users and sets them visible
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  list must be refreshed by another piece of code
     *  Postconditions:  list refreshed
     */
    private void refreshUserList()
    {
        for(int i = 0;i < getVectorOfObjects().size();i ++)
        {
            GraphicalListComponent graphicObject = (GraphicalListComponent) getVectorOfObjects().get(i);
            graphicObject.setVisible(true);
        }
    }

    /**
     *  Method Name:  getVectorOfObjects
     *  Purpose of Method:  returns the vecotr of objects within the list
     *      container
     *  Arguments:  none
     *  Returns:  a vector containing the data added components of the list
     *      container
     *  Preconditions:  another piece of code requires the vector
     *  Postconditions:  vector returned
     */
    private Vector<GraphicalListComponent> getVectorOfObjects()
    {
        return this.myListContainer.getAddedComponents();
    }

    /**
     *  Method Name:  hasChanged
     *  Purpose of Method:  checks to see if the data in the subForm has changed
     *      from its orginal values
     *  Arguments:  none
     *  Returns:  a boolean describing if the data has changed
     *  Preconditions:  program needs to check if data has changed
     *  Postconditions:  data check performed
     */
    private boolean hasChanged()
    {
        //  declaration of boolean to return
        boolean hasChanged = false;

        //  get data to compare
        NewUser_Alert_Data dataToCheck = new NewUser_Alert_Data();
        dataToCheck = mySubForm.getFormData();
        NewUser_Alert_Data originalData = (NewUser_Alert_Data) currentlySelectedObject;
        
        //  perform checks
        if(originalData.getSSN() != dataToCheck.getSSN())
            hasChanged = true;
        else if(!originalData.getFirstName().matches(dataToCheck.getFirstName()))
            hasChanged = true;
        else if(!originalData.getLastName().matches(dataToCheck.getLastName()))
            hasChanged = true;
        else if(!originalData.getCompany().matches(dataToCheck.getCompany()))
            hasChanged = true;
        else if(!originalData.getPrimaryEmail().matches(dataToCheck.getPrimaryEmail()))
            hasChanged = true;
        else if(!originalData.getAlternateEmail().matches(dataToCheck.getAlternateEmail()))
        {
            System.out.println("original:  " + originalData.getAlternateEmail());
            System.out.println("dataToCheck:  " + dataToCheck.getAlternateEmail());
            hasChanged = true;
           
        }
        else if(!originalData.getTextNumber().matches(dataToCheck.getTextNumber()))
            hasChanged = true;
        else if(originalData.isIsSendText() != dataToCheck.isIsSendText())
            hasChanged = true;
         else if(originalData.isUseAlternateEmail() != dataToCheck.isUseAlternateEmail())
            hasChanged = true;
        else if(originalData.isUseBothEmail() != dataToCheck.isUseBothEmail())
            hasChanged = true;

        //  return
        return hasChanged;
    }

    /**
     *  Method Name:  createNewForm
     *  Purpose of Method:  creates a new form when the user clicks new
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  user has pressed new button, form not created
     *  Postconditions:  new form created, data saved if needed
     */
    private void createNewForm()
    {
        //  check if data on subForm has changed
        if(currentlySelectedObject != null && hasChanged())
            promptUserToSave();
        
        if(isSaveValid)
        {
            //  reset subForm
            mySubForm.reset();
        
            //  reset currentlySelectedObject
            currentlySelectedObject = null;

            //  deselect all list components
            for(int i = 0;i < getVectorOfObjects().size();i ++)
            {
                GraphicalListComponent graphicalComponent = getVectorOfObjects().get(i);
                graphicalComponent.setSelected(false);
            }

            //  reset isSafeValid
            isSaveValid = true;
        }
    }

    /**
     *  Method Name:  promptUserToSave
     *  Purpose of Method:  method is run when user cliicks away from current
     *      subForm and data has changed.  Asks the user if they wish to save;
     *      if yes, save method run
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  data has changed, program needs to know if data should
     *      be saved
     *  Postconditions:  if answer is yes, data saved; if not, method completes
     */
    private void promptUserToSave()
    {
        if (JOptionPane.showConfirmDialog(this, "Data has not been saved. Save current data?",
                "Save Changes?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            save();
        
    }

    /**
     *  Method Name:  save
     *  Purpose of Method:  saves the data from the subForm to the db
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  user has selected a save action, data not saved to DB
     *  Postconditions:  data saved to DB
     */
    private void save()
    {
        if(mySubForm.isDataSafe())
        {
            //  get data from subForm
            NewUser_Alert_Data data = new NewUser_Alert_Data();
            data = mySubForm.getFormData();

            //  create/update/prep query
            save_users_to_alert_query query = new save_users_to_alert_query();
            query.updateQuery(data.getSSN(), data.getFirstName(), data.getLastName(),
                data.getCompany(),data.getPrimaryEmail(), data.getAlternateEmail(),
                data.getTextNumber(), data.isIsSendText(), data.isUseAlternateEmail(),
                data.isUseBothEmail());
            myConnection.prepQuery(query);

            //  execute query
            try
            {
                myConnection.executeQuery(query);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "There was an error saving the user " +
                    "to the DB.  Please contact Schedfox for more information.", "Error Saving",
                    JOptionPane.ERROR_MESSAGE);
            }

            //  resetSubForm, currently selected object, isSaveValid
            mySubForm.reset();
            currentlySelectedObject = null;
            isSaveValid = true;

            //  update list container
            getData();
        }
        else
            isSaveValid = false;
    }

    /**
     *  Method Name:  exit
     *  Purpose of Method:  resets everything, exits this form
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  user has attempted to leave this form
     *  Postconditions:  data reset, form set non-visible
     */
    private void exit()
    {
        //  check to ensure data not changed
        if(currentlySelectedObject != null && hasChanged())
            promptUserToSave();

        //  reset
        mySubForm.reset();
        currentlySelectedObject = null;

        //  clear out list
        clearList();

        //  exit form
        this.setVisible(false);
        
    }

    /**
     *  Method Name:  remove
     *  Purpose of Method:  removes an employee from database, or simply
     *      resets subform if employee is not in DB
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  user has clicked remove button, no action taken
     *  Postconditions:  user removed from DB if present; else subForm reset
     */
    private void remove()
    {
        if (JOptionPane.showConfirmDialog(this, "Remove current user?",
                "Remove user?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            if(currentlySelectedObject == null)
                mySubForm.reset();
            else
            {
                //  reset subForm
                mySubForm.reset();

                //  get currently selected employee for deletion
                NewUser_Alert_Data data = (NewUser_Alert_Data) currentlySelectedObject;

                //  create/prep query for deletion
                delete_user_to_alert_query query = new delete_user_to_alert_query();
                    query.updateQuery(data.getSSN());
                myConnection.prepQuery(query);

                //  execute query
                try
                {
                    myConnection.executeQuery(query);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "There was an error removing the selected user.  " +
                        "Please contact SchedFox for more details.", "Error removing user.",
                        JOptionPane.ERROR_MESSAGE);
                }

                //  reset currentlySelectedObject
                currentlySelectedObject = null;

                //  reset list containter
                getData();
            }
        }
    }

    /**
     *  Method Name:  sortListContainer
     *  Purpose of Method:  sorts the list container by last name
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  list container loaded, not sorted
     *  Postconditions:  list container sorted
     */
    private void sortListContainer()
    {
        
    }

    private GenericListContainer createListContainer()
    {
        return new GenericListContainer(this);
    }

    //  icon text return methods
    private String getNewToolTipString()
    {
        return "Creates a new form for a new user to receive alerts";
    }

    private String getNewString()
    {
        return "New";
    }

    private String getSaveToolTipString()
    {
        return "Save the current information for the user";
    }

    private String getSaveString()
    {
        return "Save";
    }

    private String getRemoveToolTipString()
    {
        return "Removes the currently selected user from receiving alerts";
    }

    private String getRemoveString()
    {
        return "Remove";
    }

    private String getExitToolTipString()
    {
        return "Exits this window";
    }

    private String getExitString()
    {
        return "Exit";
    }

    //  public method implementation
    /**
     *  Method Name:  NewUser_Alert_Screen
     *  Purpose of Method:  creates an instance of this object, sets initial
     *      variables
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, initial variables set
     */
    public NewUser_Alert_Screen()
    {
        //  initialize class variables
        mySubForm = new NewUser_Alert_SubForm();
        myConnection = new Connection();
        myListComponents = new Vector();
        currentlySelectedObject = null;
        isSaveValid = true;

        
        //  initialize java swing components
        initComponents();

        //  hard coded setup methods for header panel, tool bar
        this.setUpHeaderPanel();
        this.setUpToolBar();

        //  set up methods for list container
        this.myListContainer = createListContainer();
        this.jContainerPanel.add(myListContainer);

        //  add subForm panel
        jSubFormPanel.add(mySubForm.getMyForm());

        //  set window title
        setTitle(WINDOW_TITLE);

        //  load list
        getData();
    }

    /**
     *  Method Name:  runOnClickUser
     *  Purpose of Method:  implements the actions that occur when a user is
     *      selected in the container list
     *  Arguments:
     *  Returns:  void
     *  Preconditions:  program user has clicked on a user in the container
     *      list
     *  Postconditions:  user in container list set selected, data from that
     *      use loaded into subform, or user in container list set unselected,
     *      new form shown on subform
     */
    public void runOnClickUser(Object objectContained, boolean isSelected)
    {
        if(currentlySelectedObject != null && currentlySelectedObject != objectContained && hasChanged())
            promptUserToSave();
        currentlySelectedObject = objectContained;

        //  load data into subform
        NewUser_Alert_Data data = (NewUser_Alert_Data) currentlySelectedObject;
        mySubForm.reset();
        mySubForm.loadUserData(data);

        //  reset isSaveValid
        isSaveValid = true;
    }

    /**
     *  Method Name:  reload
     *  Purpose of Method:  form is being reset visible, so all data needs
     *      to be reloaded
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  form has been cleared, needs to be reloaded
     *  Postconditions:  form reloaded
     */
     public void reload()
     {
         // ensure subForm clear
         mySubForm.reset();

         // ensure save is valid
         isSaveValid = true;

         // load list
         getData();
     }
     
    //  java swing code
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMainContainerPanel = new javax.swing.JPanel();
        jHeaderIconPanel = new javax.swing.JPanel();
        jContainerPanel = new javax.swing.JPanel();
        jMainPanel = new javax.swing.JPanel();
        jSubFormPanel = new javax.swing.JPanel();
        jMyToolBar = new javax.swing.JToolBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setPreferredSize(new java.awt.Dimension(226, 67));

        jMainContainerPanel.setMinimumSize(new java.awt.Dimension(10, 24));
        jMainContainerPanel.setPreferredSize(new java.awt.Dimension(200, 0));
        jMainContainerPanel.setLayout(new javax.swing.BoxLayout(jMainContainerPanel, javax.swing.BoxLayout.Y_AXIS));

        jHeaderIconPanel.setMinimumSize(new java.awt.Dimension(10, 30));
        jHeaderIconPanel.setPreferredSize(new java.awt.Dimension(10, 30));
        jHeaderIconPanel.setLayout(new java.awt.GridLayout(1, 0));
        jMainContainerPanel.add(jHeaderIconPanel);

        jContainerPanel.setPreferredSize(new java.awt.Dimension(200, 447));
        jContainerPanel.setLayout(new javax.swing.BoxLayout(jContainerPanel, javax.swing.BoxLayout.LINE_AXIS));
        jMainContainerPanel.add(jContainerPanel);

        getContentPane().add(jMainContainerPanel, java.awt.BorderLayout.WEST);

        jMainPanel.setMinimumSize(new java.awt.Dimension(10, 34));
        jMainPanel.setPreferredSize(new java.awt.Dimension(10, 34));
        jMainPanel.setLayout(new javax.swing.BoxLayout(jMainPanel, javax.swing.BoxLayout.Y_AXIS));

        jSubFormPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        jSubFormPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        jSubFormPanel.setLayout(new java.awt.GridLayout(1, 0));
        jMainPanel.add(jSubFormPanel);

        jMyToolBar.setFloatable(false);
        jMyToolBar.setMargin(new java.awt.Insets(0, 10, 0, 0));
        jMyToolBar.setMaximumSize(new java.awt.Dimension(1000, 34));
        jMyToolBar.setMinimumSize(new java.awt.Dimension(10, 34));
        jMyToolBar.setPreferredSize(new java.awt.Dimension(10, 34));
        jMainPanel.add(jMyToolBar);
        jMyToolBar.getAccessibleContext().setAccessibleParent(jMainPanel);

        getContentPane().add(jMainPanel, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-853)/2, (screenSize.height-531)/2, 853, 531);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jContainerPanel;
    private javax.swing.JPanel jHeaderIconPanel;
    private javax.swing.JPanel jMainContainerPanel;
    private javax.swing.JPanel jMainPanel;
    private javax.swing.JToolBar jMyToolBar;
    private javax.swing.JPanel jSubFormPanel;
    // End of variables declaration//GEN-END:variables
};
