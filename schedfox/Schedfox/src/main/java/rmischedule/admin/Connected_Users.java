/**
 *  FileName:  Connected_Users.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  06/10/2010
 *  Date Last Modified:  06/11/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Purpose of File:  File contains a class for a GUI that allows a root user
 *      to view who all is connected to schedfox at that moment.
 */

//  package declaration
package rmischedule.admin;

//  import declarations
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JPanel;
import rmischedule.components.List_View;
import rmischedule.components.graphicalcomponents.myToolBarIcons;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.connected_users.get_users_logged_in_query;


/**
 *  Class Name:  Connected_Users
 *  Purpose of Class:  a class for a GUI that allows a root user to view who
 *      all is connected to schedfox at that moment.
 *  Extends:  JInternalFrame
 */
public class Connected_Users extends javax.swing.JInternalFrame
{
    //  private variable declarations
    private static final String WINDOW_TITLE = "Users currently connected";
    private Connection myConnection;
    private Internal_List_View myListView;

    //  protected icon declarations
    protected myToolBarIcons myResetIcon;
    protected myToolBarIcons myExitIcon;

    //  private method implementations
    /**
     *  Method Name:  getData
     *  Purpose of Method:  hits the database for needed data, dumping it into
     *      a record set
     *  Arguments:  none
     *  Returns:  a record set containing all needed data
     *  Preconditions:  data in DB, unknown to class
     *  Postconditions:  data known by class
     */
    private Record_Set getData()
    {
        //  declarations of Record_Set to return
        Record_Set rs = new Record_Set();

        //  create and prep query
        get_users_logged_in_query query = new get_users_logged_in_query();
        myConnection.prepQuery(query);

        
        //  execute query
        try
        {
            rs = myConnection.executeQuery(query);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        //  return record set
        return rs;
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
     *  Method Name:  setUpToolBar
     *  Purpose of Method:  sets up the JToolBar myToolBar
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  myToolBar initialized in initComponents
     *  Postcondition:  myToolBar set up with all icons and proper handling
     */
    protected void setUpToolBar()
    {
        //  create new icons
        myResetIcon = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
                reset();
            }
        };
        myExitIcon = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
                setFormVisible(false);
            }
        };

        //  set icon text
        myResetIcon.setToolTipText("Reset List");
        myResetIcon.setText(getResetString(), new Font("Dialog", Font.BOLD, 14));
        myExitIcon.setToolTipText("Exit this window");
        myExitIcon.setText(getExitString(), new Font("Dialog", Font.BOLD, 14));

        //  set icons
        myResetIcon.setIcon(Main_Window.Reset_Message_Icon_36x36);
        myExitIcon.setIcon(Main_Window.Exit_Message_Icon_36x36);

        //  set size of icons
        myResetIcon.setSize(new Dimension(120, 50));
        myExitIcon.setSize(new Dimension(120, 25));

        //  add icons to tool bar in proper format
        myToolBar.add(myResetIcon);
        myToolBar.add(createSpacerPanel());
        myToolBar.add(myExitIcon);

    }

    public void setFormVisible(boolean visible) {
        this.setVisible(visible);
    }

     /**
     *  Method Name:  getResetString
     *  Purpose of Method:  returns the string to be displayed on reset icon
     *  Arguments:  none
     *  Returns:  a string describing the text to be displayed on reset icon
     *  Precondition:  a piece of code desires the text to be display on reset
     *      icon, text already known by method
     *  Postcondition:  string returned containing text to be displayed on reset
     *      icon
     */
    protected String getResetString()
    {
        //  create, set text to be returned
        StringBuffer returnString = new StringBuffer();
        returnString.append("Reset");

        //  return string
        return returnString.toString();
    }

    /**
     *  Method Name:  getExitString
     *  Purpose of Method:  returns the string to be displayed on exit icon
     *  Arguments:  none
     *  Returns:  a string describing the text to be displayed on exit icon
     *  Precondition:  a piece of code desires the text to be display on exit
     *      icon, text already known by method
     *  Postcondition:  string returned containing text to be displayed on exit
     *      icon
     */
    protected String getExitString()
    {
        //  create, set text to be returned
        StringBuffer returnString = new StringBuffer();
        returnString.append("Exit");

        //  return string
        return returnString.toString();
    }

    //  protected method implementations
    /**
     *  Method Name:  buildData
     *  Purpose of Method:  calls getData to retrieve information from DB, then
     *      loadData from List_View class to load into list_view
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  List_View not populated, data unknown from DB
     *  Postconditions:  data from DB known, List_View populated
     */
    protected void buildData()
    {
        //declare new record_set, go get data
        Record_Set rs = getData();

        //  load listview
        if(rs.length() > 0)
            myListView.populate(rs);
    }

    /**
     *  Method Name:  reset
     *  Purpose of Method:  reset runs when the reset icon is clicked in the
     *      tool bar
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  user has clicked reset button
     *  Postcondition:  List_View reloaded
     */
    protected void reset()
    {
        //  reset listview
        myListView.resetListView();

        //  rebuild list
        buildData();
    }

    //  public method implementations
    /**
     *  Method Name:  Connected_Users
     *  Purpose of Method:  creates a default instance of this class
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, variables initialized, gui initialized
     */
    public Connected_Users()
    {
        //  initialize class variables
        myConnection = new Connection();
        myListView = new Internal_List_View();
       
        initComponents();
        setTitle(WINDOW_TITLE);
        buildData();
        listViewPanel.add(myListView.getMyForm());
        setUpToolBar();
    }

    //  internal List_View class for the ability to return a JPanel
    private class Internal_List_View extends JPanel
    {
        //  private variable declarations
        private List_View listView;

        //  private method implementations
        private String formatTime(String tempTime)
        {
            //  declare returnString, formatString
            StringBuilder formatString = new StringBuilder();
            StringBuilder returnString = new StringBuilder();
            formatString.setLength(0);
            returnString.setLength(0);
            formatString.append(tempTime);
            formatString.trimToSize();

            //  format time
            returnString.append(formatString.substring(11, 16));
            returnString.append("   ");
            returnString.append(formatString.substring(5,7));
            returnString.append("/");
            returnString.append(formatString.substring(8,10));
            returnString.append("/");
            returnString.append(formatString.substring(0, 4));

            //  return properly formatted string
            return returnString.toString();
        }

        /**
         *  Method Name:  Internal_List_View
         *  Purpose of Method:  creates instance of Internal_Liew_View
         *  Arguments:  none
         *  Returns:  none
         *  Precondition:  object DNE
         *  Postcondition:  object exists, initial variables set
         */
        public Internal_List_View()
        {
            //  set initial variables
            listView = new List_View();

            //  initialize List_View components
            //  initialize List_View
            setLayout(new BorderLayout());
            listView.addColumn("Name",
                List_View.STRING, false, true, 30);
            listView.addColumn("Company", List_View.STRING,
                true, true, 30);
            listView.addColumn("Last Connected", List_View.STRING,
                false, true, 100);
            listView.buildTable();
            listView.maximizeTable();

            //  add the formatted tab to panel, data loaded serparately
            add(listView.myScrollPane, BorderLayout.CENTER);
        }

        /**
         *  Method Name:  getMyForm
         *  Purpose of Method:  returns this class as a JPanel
         *  Arguments:  none
         *  Returns:  this class as a JPanel
         *  Precondition:  another piece of code requires this class
         *  Postcondition:  this class returned
         */
        public JPanel getMyForm()
        {
            return this;
        }

        /**
         *  Method Name:  populate
         *  Purpose of Method:  populates the list_view with data
         *  Arguments:  the employeeListVector containg all relevant data
         *  Returns:  void
         *  Preconditions:  data contained in vector, list_view not
         *      populated
         *  Postconditions:  list_view populated
         */
        public void populate(Record_Set rs)
        {
            //  ensure listView is empty for no duplicate entries
            listView.clearRows();

            //  iterate through the the record_set, load into List_View
            do
            {
                //  format name for display
                StringBuffer name = new StringBuffer();
                name.append(rs.getString("firstname") + " ");
                name.append(rs.getString("middleinitial") + " ");
                name.append(rs.getString("lastname"));
                
                //  format company/branch
                StringBuffer companyBranch = new StringBuffer();
                //  check to see if person is root user
                if(rs.getString("company").length() < 2 &&
                    rs.getString("branch").length() < 2)
                    companyBranch.append(rs.getString("company") + " - " + rs.getString("branch"));
                else
                {
                    companyBranch.append(rs.getString("company"));
                }
                
                //  format timestamp
                StringBuffer time = new StringBuffer();
                time.append(formatTime(rs.getString("time")));

                //  load into list_view
                Object[] nextRow = new Object[3];
                nextRow[0] = name.toString();
                nextRow[1] = companyBranch.toString();
                nextRow[2] = time.toString();

                //  add object to listview
                listView.addRow(nextRow);
            }
            while(rs.moveNext());
           
            //  let the program know the data inside the List_View has changed
            listView.fireTableDataChanged();
        }

       /** Method Name:  resetListView
         *  Purpose of Method:  clears out the List_View
         *  Arguments:  none
         *  Returns:  void
         *  Precondition:  List_View altered, needs to be reset
         *  Postcondition:  List_View cleared
        */
        public void resetListView()
        {
            listView.clearRows();
        }
    }

    //  Netbeans generated code for gui
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myToolBar = new javax.swing.JToolBar();
        listViewPanel = new javax.swing.JPanel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMinimumSize(new java.awt.Dimension(76, 33));
        setPreferredSize(new java.awt.Dimension(226, 67));

        myToolBar.setRollover(true);
        myToolBar.setMaximumSize(new java.awt.Dimension(1000, 50));
        myToolBar.setMinimumSize(new java.awt.Dimension(10, 10));
        myToolBar.setPreferredSize(new java.awt.Dimension(10, 50));
        getContentPane().add(myToolBar, java.awt.BorderLayout.PAGE_END);

        listViewPanel.setLayout(new java.awt.GridLayout());
        getContentPane().add(listViewPanel, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-866)/2, (screenSize.height-506)/2, 866, 506);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel listViewPanel;
    private javax.swing.JToolBar myToolBar;
    // End of variables declaration//GEN-END:variables
};
