/*
 *  Filename:  Employee_state_cert.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  05/07/2010
 *  Date last modified:  06/16/2010
 *  Last modified by:  Jeffrey N. Davis
 *  Reason for Modification on 06/16/2010:  Tab title changed per Jim's request
 *  Pupose of File:  File contains the Employee_state_cert class which is
 *      designed for a tabbed pane when adding an employee.  The class contains
 *      50 boolean values for 'yes/no' that describes the employee's
 *      certification in that state
 */
//  package declaration
package rmischedule.employee.components;

//  import declarations
import java.awt.BorderLayout;
import rmischedule.employee.*;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.security.*;
import rmischedule.main.*;
import schedfoxlib.model.util.Record_Set;
import java.util.*;
import javax.swing.AbstractListModel;
import rmischedule.components.List_View;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_info_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_state_certification_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_state_certification_save_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_state_noncertification_query;

/*
 *  Class Name:  Employee_State_Cert
 *  Purpose of class:  a class designed for a tabbed pane when adding an
 *      employee's state certifications.  The class utilizes the List_View
 *      class used to design and implement other similar tabs
 *  Extends:  GenericEditSubForm
 */
public class Employee_State_Cert extends GenericEditSubForm
{
    //  private variable declarations
    private List_View stateCertificationList;
    private xEmployeeEdit myParent;
    private StateListNonCertified stateListNonCertified;
    private StateListCertified stateListCertified;
    private int recordSetNumber;

    public Employee_State_Cert() {
        initComponents();
    }

    //  private method implementations
    /*
     *  Method Name:  loadTable
     *  Purpose of Table:  takes the data pulled from the database and already
     *      loaded into internal data structures and displays them using the
     *      List_View class
     *  Aruments:  none
     *  Returns:  void
     *  Precondition:  data from database already known
     *  Postcondition:  data loaded into List_View table, though not displayed
     *      yet
     */
    private void loadTable()
    {
        //  clear previous information, if there is no data loaded from
        //      database, go get it
        stateCertificationList.clearRows();
        if(stateListNonCertified.getSize() == 0)
            populateStateNonCertified();
            
        //  iterate through the the data, load into List_View
        for(int i = 0;i < stateListNonCertified.getSize();i ++)
        {
            //  run through all 50 states, getting information

            Employee_State_Certification_Data tempESCD0 =
                new Employee_State_Certification_Data();
            tempESCD0 = (Employee_State_Certification_Data)
                    stateListNonCertified.getElementAt(i);
            Object[] nextRow = new Object[2];

            //  run through the list of certified states (which may be zero)
            for(int j = 0;j < stateListCertified.getSize();j ++)
            {
                Employee_State_Certification_Data tempESCD1 =
                    new Employee_State_Certification_Data();
                tempESCD1 = (Employee_State_Certification_Data) 
                      stateListCertified.getElementAt(j);
                if(tempESCD0.getState().matches(tempESCD1.getState()))
                    tempESCD0.setIsCertified(true);
            }
            
            //  add data to List_View for each state
            nextRow[0] = tempESCD0.getIsCertified();
            nextRow[1] = tempESCD0.getState();
            stateCertificationList.addRow(nextRow);
        }

        //  let the List_View class know that the internal data has changed
        stateCertificationList.fireTableDataChanged();
    }

    /*
     *  Method Name:  populateStateNonCertified
     *  Purpose of Method:  gets the list of 50 states from the database
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  list of 50 states unknown
     *  Postcondition:  all 50 states loaded from database into internal
     *      data structure
     */
    private void populateStateNonCertified()
    {
        //  define new query and record set, then go get the data
        employee_state_noncertification_query stateQuery =
            new employee_state_noncertification_query();
        myParent.getConnection().prepQuery(stateQuery);
        Record_Set rs0 = myParent.getConnection().executeQuery(stateQuery);
        
        //  iterate through record set, which contains the 50 states information
        do
        {
            //  pull the data from record set
            Employee_State_Certification_Data tempESCD =
                new Employee_State_Certification_Data();
            tempESCD.setState(rs0.getString("state_name"));
            tempESCD.setStateAB(rs0.getString("state_abrev"));
            tempESCD.setStateID(rs0.getInt("state_id"));
            
            //  add to internal vector
            stateListNonCertified.addState(tempESCD);
        }
        while(rs0.moveNext());
    }

    /*
     *  Method Name:  buildCertifiedList
     *  Purpose of Method:  builds the vector containing any states that the
     *      employee is certified in from the BUI
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  certified states unknown
     *  Postcondition: certified states added to internal data structure
     *  Note:  This allows the user to click/unclick as many times as desired
     *      to increase performance.  The method only runs when the user wishes
     *      to save the data.
     */
    private void buildCertifiedList()
    {
        //  clear internal data structure for new employee
        stateListCertified.removeAll();

        //  iterate through the table to check each row to see if the user has
        //      selected that state
        for(int i = 0;i < stateCertificationList.getRowCount();i ++)
        {
            Object isCertified = stateCertificationList.getValueAt(i, 0);

            //  if the user has selected that state, get all information about
            //      that row
            if(isCertified.toString().matches("true"))
            {
                String newCertifiedState = (String) stateCertificationList.
                    getValueAt(i,1);
                Employee_State_Certification_Data tempESCD0 =
                    new Employee_State_Certification_Data();
                tempESCD0.setState(newCertifiedState);

                //  get StateID from corresponding states lsit (containing all
                //      50 states . . . needed for DB
                for(int j = 0;j < stateListNonCertified.getSize();j ++)
                {
                    Employee_State_Certification_Data tempESCD1 =
                        new Employee_State_Certification_Data();
                    tempESCD1 = (Employee_State_Certification_Data)
                        stateListNonCertified.getElementAt(j);
                    if(tempESCD0.getState().matches(tempESCD1.getState()))
                        tempESCD0.setStateID(tempESCD1.getStateID());
                }

                //  add all data to vector containing certified states
                stateListCertified.addState(tempESCD0);
            }
        }
    }


    //  public method implementations
    /*
     *  Method Name:  Employee_State_Cert
     *  Purpose of Method:  Constructor method for the Employee_State_Cert
     *      class.
    */
    public Employee_State_Cert(xEmployeeEdit main)
    {
        initComponents();

        myParent = main;
        stateListNonCertified = new StateListNonCertified();
        stateListCertified = new StateListCertified();
        recordSetNumber = 0;
        stateCertificationList = new List_View();


        stateCertificationList.addColumn("Mark As Certified", List_View.BOOLEAN,
                true, true, 90);
        stateCertificationList.addColumn("States", List_View.STRING, false,
                true, 380);
        stateCertificationList.buildTable();
        stateCertificationList.maximizeTable();
        
        //  set list to be sorted by state name, not boolean value
        stateCertificationList.sort(1);

        //  add the formatted tab to panel, data loaded seperately
        containerPanel.add(stateCertificationList.myScrollPane, BorderLayout.CENTER);
    }

    /*
     *  Method Name:  userHasAccess
     *  Purpose of Method:  returns the security level that this class is 
     *      eligible
     *  Arguments:  none
     *  Returns:  the security boolean
     *  Postcondition:  security level unknown
     *  Postcondition:  security level known, returned
     *  Implements:  userHasAccess from GenericEditSubForm
     */
    public boolean userHasAccess()
    {
        return Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.EMPLOYEE_EDIT);
    }

    /*
     *  Method Name:  doOnClear
     *  Purpose of Method:  details the actions to be taken each time the user
     *      either:  1.)  clicks New Form 2.)  clicks away from the tab
     *  Arguments:  none
     *  Returns:  void
     *  Postcondition:  internal tab display information set
     *  Postcondition:  internal tab display information reset
     *  Implements:  doOnClear from GenericEditSubForm
     */
    public void doOnClear()
    {
        //  clear list, clear internal data structures
        stateCertificationList.clearRows();
        stateListNonCertified.removeAll();
        stateListCertified.removeAll();

        //  reload table
        loadTable();
    }

    /*
     *  Method Name:  getMyForm
     *  Purpose of Method:  returns the JPanel constructed by this class
     *  Arguments:  none
     *  Returns:  JPanel
     *  Postcondition:  JPanel constructed internally, not known outside of
     *      class
     *  Postcondition:  JPanel returned outside of class to be displayed
     *  Implements:  getMyForm from GenericEditSubForm
     */
    public javax.swing.JPanel getMyForm()
    {
        return this;
    }

    /*
     *  Method Name:  getMyTabTitle
     *  Purpose of Method:  returns the tab panel title
     *  Arguments:  none
     *  Returns:  a string containing the name of the tab
     *  Postcondition:  Tab title known internally, not known outside of class
     *  Postcondition:  tab title returned
     *  Implements:  getMyTabTitle from GenericEditSubForm
     */
    public String getMyTabTitle()
    {
        //  tab title changed by Jeffrey Davis on 06/16/2010 per Jim's request
       return "State Lic.";
    }

    /*
     *  Method Name:  needsMoreRecordSets
     *  Purpose of Method:  returns a boolean describing whether there is more
     *      relevanet information within the Record_Set
     *  Arguments:  none
     *  Returns:  a boolean describing whether this more information needed
     *      within the record set
     *  Postcondition:  data inside Record_Set, unknown if that data is related
     *      to this class
     *  Postcondition:  program knows whether more relevant information is
     *      contained within record_set
     *  Implements:  needsMoreRecordSets from GenericEditSubForm
     */
    public boolean needsMoreRecordSets()
    {
        recordSetNumber++;
        if (recordSetNumber <= 1)
            return true;
        recordSetNumber = 0;
        return false;
    }

    /*
     *  Method Name:  loadData
     *  Purpose of Method:  takes the data contained within Record_Set and loads
     *      it into internal data structures
     *  Arguments:  Record_Set
     *  Returns: void
     *  Postcondition:  data inside Record_Set, not loaded into internal data
     *      structures
     *  Postcondition: Record_Set iterated through, all releveant data extracted
     *  Implements:  loadData from GenericEditSubForm
     */
     public void loadData(Record_Set rs)
     {
        //  checks to make sure Record_Set is valid, then pulls the first 
        //      portion of Record_Set, the 50 states
        if(recordSetNumber == 0 && rs.length() > 0 &&
            stateListNonCertified.getSize() != 50)
        {
            //  iterate through Record_Set, extracting all data
            do
            {
                //  get information from each block of record_set
                Employee_State_Certification_Data tempESCD =
                    new Employee_State_Certification_Data();
                tempESCD.setState(rs.getString("state_name"));
                tempESCD.setStateAB(rs.getString("state_abrev"));
                tempESCD.setStateID(rs.getInt("state_id"));

                //  add state to internal vector
                stateListNonCertified.addState(tempESCD);
            }
            while(rs.moveNext());
        }

        //  checks to make sure Record_Set is valid, then pulls the second
        //      portion of Record_Set, which of the 50 states the employee
        //      is certified in
        else if(recordSetNumber == 1 && rs.length() > 0)
        {
            //  iterate through Record_Set, extracting all data
            do
            {
                //  get information from each block of record_set
                Employee_State_Certification_Data tempESCD =
                   new Employee_State_Certification_Data();
                tempESCD.setState(rs.getString("state_name"));
                
                //  add state to internal vector
                stateListCertified.addState(tempESCD);
            }
            while(rs.moveNext());
        }

        //  if the method has completely iterated through Record_Set, load
        //      the table
        if(recordSetNumber == 1)
            loadTable();
    }

   //   query methods
   /*
    *   Method Name:  getSaveQuery
    *   Purpose of Method:  constructs a query to save information from the tab
    *   Arguments:  a boolean describing whether the information is new
    *   Returns:  a query in GeneralQueryFormat
    *   Precondition:   information changed within tab, not saved yet
    *   Postcondition:  Query returned in proper format
    */
    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat
        getSaveQuery(boolean isNew)
    {
        //  check to see what information has changed on the tab
        buildCertifiedList();

        ArrayList<Integer> statesSelected = new ArrayList<Integer>();
        for(int i = 0;i < stateListCertified.getSize();i ++)
         {
             Employee_State_Certification_Data tempESCD =
                new Employee_State_Certification_Data();
             tempESCD = (Employee_State_Certification_Data)
                stateListCertified.getElementAt(i);
             statesSelected.add(tempESCD.getStateID());
         }

        //  create save query
        employee_state_certification_save_query mySaveQuery =
            new employee_state_certification_save_query(
            myparent.getMyIdForSave(), statesSelected);


        //  return properly formatted query
        return mySaveQuery;
    }

    /*
    *   Method Name:  getQuery
    *   Purpose of Method:  constructs a query to retrieve informationi from db
    *   Arguments:  a boolean describing whether the information is new
    *   Returns:  a query in GeneralQueryFormat
    *   Precondition:   query not yet constructed
    *   Postcondition:  Query returned in proper format
    */
    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat
          getQuery(boolean isNew)
    {
        //  create new query
        RunQueriesEx myCompleteQuery = new RunQueriesEx();
        employee_state_certification_query testQuery =
            new employee_state_certification_query();
        
        //  add employee_ID to query
        testQuery.addEmpID( myparent.getMyIdForSave());

        //  add both formatted queries
        myCompleteQuery.add(new employee_state_noncertification_query());
        myCompleteQuery.add(testQuery);

        //  return formatted query
        return myCompleteQuery;
    }

//  private classes for each state list
/*
 *  Class Name: StateListNonCertified
 *  Purpose of Class:  A class designed to contain a vector of objects.  Each
 *      object is Employee_State_Certification_Data.  This class will contain
 *      the data retrieved from the "states" table that will list all 50 states,
 *      the state ID, and the a boolean value set to FALSE (for non-certified).
 *  Extends:  AbstractListModel
*/
private class StateListNonCertified extends AbstractListModel
{
    //  private variable declarations
    Vector<Employee_State_Certification_Data> stateList;
           
    //  private method implementations
    /*
     *  Method Name:  sortStateList
     *  Purpose of Method:  this method is designed to sort the list
     *      alphabetically by each states name to ensure the list contains
     *      the states in the proper order
     *  Arguements:  none
     *  Return Value:  void
     *  Precondition:  list created and populated, unsorted
     *  Postcondition:  list sorted by Collections.sort
     */
    private void sortStateList()
    {
        Collections.sort(stateList);
    }

    //  public methods implementation
    /*
     *  Method Name:  StateListNonCertified
     *  Purpose of Method:  Constructor for the StateListNonCertified class
     *  Arguments:  none
     *  Returns:  none
     *  Precondition:  object not created
     *  Postcondition:  object created, initial variables set
     */
    public StateListNonCertified()
    {
        //  create new vector to store Employee_State_Certification_Data objects
        stateList = new Vector<Employee_State_Certification_Data>();
    }
    
    /*
     *  Method Name:  addState
     *  Purpose of Method:  adds a "Employee_State_Certification_Data" object
     *      to the vector of states
     *  Arguments:  a Employee_State_Certification_Data object
     *  Returns:  void
     *  Precondition:  object's internal data set, ready to be added to vector
     *  Postcondition:  object added to vector
     */
    public void addState(Employee_State_Certification_Data tempData)
    {
        //  add object to vector
        stateList.add(tempData);

        //  resort vector to ensure correct format
        sortStateList();

        //  tell listmodel that the internal vector has changed for display
        //      purposes
        this.fireContentsChanged(this, 0, stateList.size());
    }

    //  public remove methods
    /*
     *  Method Name:  removeAt
     *  Purpose of Method:  removes an object of 
     *      Employee_State_Certification_Data from the vector at the specified
     *      location in the vector
     *  Arguments:  an int describing the position in the vector to remove the
     *      object
     *  Returns:  void
     *  Precondition:  position of object in vector to be removed known, object
     *      not removed
     *  Postcondition:  object removed from vector, list resorted
     */
    public void removeAt(int index)
    {
        //  remove object from vector
        stateList.remove(index);

        //  resort vector to ensure correct format
        sortStateList();

        //  tell listmodel that the internal vector has changed for display
        //      purposes
        this.fireContentsChanged(this, 0, stateList.size());
    }

    /*
     *  Method Name:  removeAll
     *  Purpose of Method:  deletes all objects from the vector, sets size to
     *      zero
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  vector contains an unknown quanity of 
     *      Employee_State_Certification_Data objects
     *  Postcondition:  all objects removed from vector
     */
    public void removeAll()
    {
        stateList.clear();
    }

    //  public getter methods
    /*
    *  Method Name:  getElementAt
    *  Purpose of Method:  returns the desired element from the vector
    *  Arguments:  an int describing the position in the vector
    *  Returns:  the object located in the vector at the position in the
    *      vector described by the argument
    *  Precondition:  location of desired object known
    *  Postcondition:  object retrieved from the vector, returned
    *  NOTE:  this method implements "getElementAt" from 
    *      javax.swing.ListModel and may need to be altered if future versions
    *      of this program do not implement Swing
    */
    public Object getElementAt(int position) 
    {
        //  create return Object, get it from vector
        Object returnObject = stateList.get(position);
        
        //  return Object
        return returnObject;
    }

    /*
    *  Method Name:  getSize
    *  Purpose of Method:  returns size of the vector
    *  Arguments:  none
    *  Returns:  an int describing the size of the vector
    *  Precondition:  size of vector unknown
    *  Postcondition:  size of vector known, returned
    *  NOTE:  this method implements "getSize" from javax.swing.ListModel and
    *      may need to be altered if future versions of this program
    *      do not implement Swing
    */
    public int getSize() 
    {
        //  return vector size
        return stateList.size();
    }
}

/*
 *  Class Name: StateListCertified
 *  Purpose of Class:  A class designed to contain a vector of objects.  Each
 *      object is Employee_State_Certification_Data.  This class will contain
 *      the data retrieved from the "emp_cert_states" table that will list any
 *      states the current employee is certified in.  If that numer is 0,
 *      the list is empty.  The boolean value is set to true when certified.
 *  Extends:  AbstractListModel
*/
private class StateListCertified extends AbstractListModel
{
    //  private variable declarations
    Vector<Employee_State_Certification_Data> stateList;
           
    //  private method implementations
    /*
     *  Method Name:  sortStateList
     *  Purpose of Method:  this method is designed to sort the list
     *      alphabetically by each states name to ensure the list contains
     *      the states in the proper order
     *  Arguements:  none
     *  Return Value:  void
     *  Precondition:  list created and populated, unsorted
     *  Postcondition:  list sorted by Collections.sort
     */
    private void sortStateList()
    {
        Collections.sort(stateList);
    }

     //  public methods implementation
    /*
     *  Method Name:  StateListCertified
     *  Purpose of Method:  Constructor for the StateListnCertified class
     *  Arguments:  none
     *  Returns:  none
     *  Precondition:  object not created
     *  Postcondition:  object created, initial variables set
     */
    public StateListCertified()
    {
        //  create new vector to store Employee_State_Certification_Data objects
        stateList = new Vector<Employee_State_Certification_Data>();
    }

    /*
     *  Method Name:  addState
     *  Purpose of Method:  adds a "Employee_State_Certification_Data" object
     *      to the vector of states
     *  Arguments:  a Employee_State_Certification_Data object
     *  Returns:  void
     *  Precondition:  object's internal data set, ready to be added to vector
     *  Postcondition:  object added to vector
     */
    public void addState(Employee_State_Certification_Data tempData)
    {

       //  add object to vector
        stateList.add(tempData);

        //  resort vector to ensure correct format
        sortStateList();

        //  tell listmodel that the internal vector has changed for display
        //      purposes
        this.fireContentsChanged(this, 0, stateList.size());
    }

    //  public remove methods
    /*
     *  Method Name:  removeAt
     *  Purpose of Method:  removes an object of 
     *      Employee_State_Certification_Data from the vector at the specified
     *      location in the vector
     *  Arguments:  an int describing the position in the vector to remove the
     *      object
     *  Returns:  void
     *  Precondition:  position of object in vector to be removed known, object
     *      not removed
     *  Postcondition:  object removed from vector, list resorted
     */
    public void removeAt(int index)
    {
        stateList.remove(index);
        sortStateList();
        this.fireContentsChanged(this, 0, stateList.size());
    }

    /*
     *  Method Name:  removeAll
     *  Purpose of Method:  deletes all objects from the vector, sets size to
     *      zero
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  vector contains an unknown quanity of 
     *      Employee_State_Certification_Data objects
     *  Postcondition:  all objects removed from vector
     */
    public void removeAll()
    {
        stateList.clear();
    }

    //  public getter methods
    /*
    *  Method Name:  getElementAt
    *  Purpose of Method:  returns the desired element from the vector
    *  Arguments:  an int describing the position in the vector
    *  Returns:  the object located in the vector at the position in the
    *      vector described by the argument
    *  Precondition:  location of desired object known
    *  Postcondition:  object retrieved from the vector, returned
    *  NOTE:  this method implements "getElementAt" from
    *      javax.swing.ListModel and may need to be altered if future versions
    *      of this program do not implement Swing
    */
    public Object getElementAt(int position)
    {
       //  create return Object, get it from vector
        Object returnObject = stateList.get(position);

        //  return Object
        return returnObject;
    }

   /*
    *  Method Name:  getSize
    *  Purpose of Method:  returns size of the vector
    *  Arguments:  none
    *  Returns:  an int describing the size of the vector
    *  Precondition:  size of vector unknown
    *  Postcondition:  size of vector known, returned
    *  NOTE:  this method implements "getSize" from javax.swing.ListModel and
    *      may need to be altered if future versions of this program
    *      do not implement Swing
    */
    public int getSize()
    {
        //  return vector size
        return stateList.size();
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        containerPanel = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        containerPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        containerPanel.setLayout(new java.awt.GridLayout(1, 0));
        add(containerPanel);

        getAccessibleContext().setAccessibleName("State Certifications");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel containerPanel;
    // End of variables declaration//GEN-END:variables
}
