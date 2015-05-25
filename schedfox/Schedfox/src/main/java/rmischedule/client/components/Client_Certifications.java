/**
 *  FileName:  Client_Certifications.java
 *  Autheor:  Ira Juneau
 *  Date Created:  2005
 *  Date Last Modified:  06/07/2010
 *  Last Modified By:  Jeffrey N. Davis
 *  Reason for Modification:  allows the display of the List_View when no
 *      location has been selected, in the same vein as Store_Services
 *  Purpose of File:  Display the Certifications held by a particular location
 *      in a List_View
 */

//  package declaration
package rmischedule.client.components;

//  import declarations
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JPanel;
import rmischedule.client.data_components.Client_Certifications_Data;
import rmischedule.client.xClientEdit;
import rmischedule.components.List_View;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import rmischedule.main.Main_Window;
import rmischedule.security.security_detail;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.client.client_certification_update;
import rmischeduleserver.mysqlconnectivity.queries.client.client_certifications_list_initial_query;
import rmischeduleserver.mysqlconnectivity.queries.client.client_certifications_list_query;


/**
 *  Class Name:  Client_Certifications
 *  Purpose of Class:  handles the sub tab Client_Certifications
 */
public class Client_Certifications extends GenericEditSubForm
{

    //  private variable declarations
    private List_View clientCertificationsListView;
    private int recordSetNumber;
    private Vector<Client_Certifications_Data> clientCertificationsVector;
    private xClientEdit myParent;

    //  private method implemenations
    /**
     *  Method Name:  loadListView
     *  Purpose of Method:  loads the list_view
     *  Arguments:  a record set containing all the needed data
     *  Returns:  void
     *  Preconditions:  class has retrieved data from db via record_set
     *  Postconditions:  data from record set loaded into List_View
     */
    private void loadClientCertificationsListView()
    {
        //  ensure that List_View is empty
        clientCertificationsListView.clearRows();
        
        //  check to see if clientCertificationsVector contains data; if not,
        //      load the vector
        if(clientCertificationsVector.size() == 0)
            populateClientCertificationsVector();
        
        //  iterate through clientCertificationsVector, load into List_View
        for(int i = 0;i < clientCertificationsVector.size();i ++)
        {
            //  get data from vector
            Client_Certifications_Data tempCCD =
                new Client_Certifications_Data();
            tempCCD = clientCertificationsVector.get(i);

            //  create object form which List_View is populated
            Object[] nextRow = new Object[4];
            nextRow[0] = tempCCD.getCertificationId();
            nextRow[1] = tempCCD.getIsRequried();
            nextRow[2] = tempCCD.getCertificationName();
            nextRow[3] = tempCCD.getDescription();

            //  add data to List_View
            clientCertificationsListView.addRow(nextRow);
        }

        //  let the List_View class know that the internal data has changed
        clientCertificationsListView.fireTableDataChanged();
    }

    /**
     *  Method Name:  populateClientCertificationsVector
     *  Purpose of Vector:  gets the information pertaining to Client
     *      Certifications from the database and loads it into the i
     *      nternal data structure, the clientCertificationsVector
     *  Arguements:  none
     *  Returns:  void
     *  Precondition:  list of client certifications unknown from database
     *  Postcondition:  all information loaded from database
     */
    private void populateClientCertificationsVector()
    {
        //  define new query and record set, then go get data
        client_certifications_list_initial_query myInitialListQuery =
            new client_certifications_list_initial_query();
        myParent.getConnection().prepQuery(myInitialListQuery);
        Record_Set rs = myParent.getConnection().executeQuery(myInitialListQuery);
        
        //  iterate through record set, placing data within vector
        if(rs.length() > 0)
        {
            do
            {
                //  create temporary data object, assign it data pulled from
                //      Record_Set
                Client_Certifications_Data tempCCD =
                    new Client_Certifications_Data();
                tempCCD.setCertificationId(rs.getString("cert_id"));
                tempCCD.setIsRequired(false);
                tempCCD.setCertificationName(rs.getString("cert_name"));
                tempCCD.setDescription(rs.getString("cert_desc"));

                //  add temporary data object to vector
                clientCertificationsVector.add(tempCCD);
            }
            while(rs.moveNext());
        }
    }

    /**
     *  Method Name:  updateUserAlteredListView
     *  Purpose of Method:  rebuilds the vector to contain the information the
     *      user has chosen on the GUI
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  data changed on List_View, internal data structure not
     *      updated
     *  Postcondition:  internal vector altered to represent the data the user
     *      has inputted onscreen
     */
    private void updateUserAlteredListView()
    {
        //  iterate through table and reset vector
        for(int i = 0;i < clientCertificationsListView.getRowCount();i ++)
        {
            //  get data from each row in table
            Object tempIsRequired =
                clientCertificationsListView.getValueAt(i,0);
            Object tempCertName = clientCertificationsListView.getValueAt(i, 1);

            //  run through vector, looking for corresponding
            //      Store_Services_Data object
            for(int j = 0;j < clientCertificationsVector.size();j ++)
            {
                //  retrieve object held at position j
                Client_Certifications_Data tempCCD =
                    new Client_Certifications_Data();
                tempCCD = clientCertificationsVector.get(j);

                //  look to see if data object matches data from table; if not,
                //      loop falls through to next iteration doing nothing
                if(tempCCD.getCertificationName().matches(
                    tempCertName.toString()))
                {
                    //  define local boolean to update vector, check value
                    //      of isRequired in table
                    boolean isRequiredValue;
                    if(tempIsRequired.toString().matches("true"))
                        isRequiredValue = true;
                    else
                        isRequiredValue = false;

                    //  update data object in vector
                    clientCertificationsVector.get(j).
                        setIsRequired(isRequiredValue);
                }
            }
        }
    }
    

    //  public method implemenations
    /**
     *  Method Name:  Client_Certifications
     *  Purpose of Method:  creates a default instance of this class
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, List_View initialized
     */
    public Client_Certifications(xClientEdit main)
    {
        //  initialize class variables
        recordSetNumber = 0;
        clientCertificationsListView = new List_View();
        clientCertificationsVector = new Vector<Client_Certifications_Data>();
        myParent = main;

        //  initialize List_View
        setLayout(new BorderLayout());
        clientCertificationsListView.addColumn("ID", List_View.BOOLEAN, true, false, 40);
        clientCertificationsListView.addColumn("Required?", List_View.BOOLEAN, true, true, 40);
        clientCertificationsListView.addColumn("Cert Name", List_View.STRING, 80);
        clientCertificationsListView.addColumn("Description", List_View.STRING, 400);
        clientCertificationsListView.sort(2);
        clientCertificationsListView.buildTable();
        clientCertificationsListView.maximizeTable();
        add(clientCertificationsListView.myScrollPane, BorderLayout.CENTER);
    }

    //  abstract method implementations
    /**
     *  Method Name:  getQuery
     *  Purpose of Method:  constructs a query to retrieve information from db
     *  Arguments:  a boolean descrbing whether the information is new
     *  Returns:  a query of GeneralQueryFormat
     *  Precondition:  query not yet constructed
     *  Postcondition:  query returned in proper format
     *  Implements:  getQuery from GenericTabEditSubForm
     */
    @Override
    public GeneralQueryFormat getQuery(boolean isSelected)
    {
        client_certifications_list_query myListQuery =
            new client_certifications_list_query();
        myListQuery.update(myParent.getMyIdForSave(), true);
        return myListQuery;
    }

    /**
     *  method name:  getSaveQuery
     *  Purpose of Method:  constructs a query to save information from the tab
     *  Arguments:  a boolean describing whether the information is new
     *  Returns:  a query of GeneralFormatQuery
     *  Precondition:  information changed within tab, not saved yet
     *  Postcondition:  Query returned in proper format to save new data
     *  Implements:  getSaveQuery from GenericTabEditSubForm
     */
    @Override
    public GeneralQueryFormat getSaveQuery(boolean isNewData)
    {
        //  update vector to reflect changes within tab
        updateUserAlteredListView();
        
        ArrayList mySelectedCerts = new ArrayList();
        String cid = null;
        client_certification_update myUpdateQuery = new client_certification_update();
        for (int i = 0; i < clientCertificationsListView.getRowCount(); i++) {
            try {
                if ((Boolean)clientCertificationsListView.getTrueValueAt(i, 1)) {
                    mySelectedCerts.add(((String)clientCertificationsListView.getTrueValueAt(i, 0)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        myUpdateQuery.update(myparent.getMyIdForSave(), mySelectedCerts);        
        try {
            myparent.getConnection().prepQuery(myUpdateQuery);
            myparent.getConnection().executeQuery(myUpdateQuery);
        } catch (Exception e) { }
        
        return null;
    }

    /**
     *  Method Name:  loadData
     *  Purpose of Method:  takes the data contained within Record_Set and
     *      loads it into List_View
     *  Arguements:  Record_Set
     *  Returns:  void
     *  Precondition:  data inside Record_Set, not loaded into internal data
     *      structure
     *  Postcondition:  Record_Set iterated through, all relevant data extracted
     *  Implements:  loadData from GenericEditSubForm
     */
    @Override
    public void loadData(Record_Set rs)
    {
        //  checks to make sure Record_Set is valid, then pulls the pulls the
        //      data from Record_Set
        if(recordSetNumber == 0 && rs.length() > 0 
            && clientCertificationsVector.size() != 0 ) {
            do {
                String certId = rs.getString("cid");
                for (int c = 0; c < clientCertificationsVector.size(); c++) {
                    if (clientCertificationsVector.get(c).getCertificationId().equals(certId)) {
                        clientCertificationsVector.get(c).setIsRequired(rs.getString("iscert").equals("t") || rs.getString("iscert").equals("true"));
                    }
                }
            }
            while(rs.moveNext());
        }

        //  load list view
        loadClientCertificationsListView();
    }

    /**
     *  Method Name:  needsMorerecordSets
     *  Purpose of Method:  returns a boolean describing whether there is more
     *      relevant information within the Record_Set
     *  Arguments:  none
     *  Returns:  a boolean describing whether there is more information to be
     *      obtained with the Record_Set
     *  Precondition:  data inside Record_Set, unknown if that data is related
     *  Postcondition:  program knows whether more relevant information is
     *      contained within the Record_Set
     *  Implements:  needsMoreRecordSets from GenericEditSubForm
     */
    @Override
    public boolean needsMoreRecordSets()
    {
        return false;
    }

    /**
     *  Method Name:  getMyTabTitle
     *  Purpose of Method:  returns the tab panel title
     *  Arguments:  none
     *  Returns:  a string containing the name of the tab
     *  Precondition:  Tab title known internally, not known outside of class
     *  Postcondition:  tab title returned
     *  Implements:  getMyTabTitle from GenericEditSubForm
     */
    @Override
    public String getMyTabTitle()
    {
        return "Certs";
    }

   /**
     *  Method Name:  getMyForm
     *  Purpose of method:  returns the JPanel constructed by this class
     *  Arguments:  none
     *  Returns:  JPanel
     *  Preconditon:  JPanel constructed internally, not known outside of class
     *  Postcondition:  JPanel returned outside of class to be kisplayed
     *  Implements:  getMyForm from GenericEditSubForm
     */
    @Override
    public JPanel getMyForm()
    {
        return this;
    }

    /**
     *  Method Name:  doOnClear
     *  Purpose of Method:  details the actions to be taken each time the user
     *      does one of the following:  1.)  loads the store screen initially
     *      2.)  clicks away from the tab
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  internal tab initially set
     *  Postcondition:  internal tab completely set
     *  Implements:  doOnClear from GenericEditSubForm
     */
    @Override
    public void doOnClear()
    {

        //  clear internal data structures
        clientCertificationsListView.clearRows();
        clientCertificationsVector.clear();

        //  reload clientCertificationsListView
        loadClientCertificationsListView();
    }

    /**
     *  Method Name:  userHasAccess
     *  Purpose of Method:  returns the security level that this class is
     *      eligible
     *  Arguments:  none
     *  Returns: the security boolean
     *  Precondition:  security level unknown
     *  Postcondition:  security level known, returned
     *  Implements:  userHasAccess from GernericEditSubForm
     */
    @Override
    public boolean userHasAccess()
    {
         return Main_Window.mySecurity.checkSecurity(security_detail.MODULES.CLIENT_INFORMATION);
    }
};