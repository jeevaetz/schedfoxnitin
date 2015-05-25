/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Database_Operations.java
 *
 * Created on Nov 11, 2010, 10:39:35 AM
 */

package rmischedule.admin.db_operations;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;
import rmischedule.data_connection.Connection;
import rmischedule.misc.SchemaServletLoader;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_company_management_id_schemaOPS_query;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_schema_last_used_query;
import rmischeduleserver.mysqlconnectivity.queries.admin.get_schema_names_query;

/**
 *
 * @author jdavis
 */
public class Database_Operations extends javax.swing.JInternalFrame
{
    //  private variable decarations
    private static final String FILE_EXTENSION = ".tar";
    private Active_Schema_Operations_GUI activeSchemaGUI;
    private Stored_Schemas_Operations_GUI storedSchemaGUI;
    private Inactive_Schema_Operations_GUI inactiveSchemaGUI;
    private static final String ACTIVE_SCHEMAS = "Active Schemas";
    private static final String INACTIVE_SCHEMAS = "Inactive Schemas";
    private static final String STORED_SCHEMAS = "Stored Schemas";
    ArrayList<Schema_Data> schemaList;
    private int storedSchemaCount;

    //  private method implementations
    /**
     *  Method Name:  getDateLastUsed
     *  Purpose of Method:  method takes in a schema name, then hits the DB
     *      to return a date last used
     *  @param schema - a string representing the schema to check last date used
     *  @return date - a string representing an unformatted date from the DB
     *      describing last useage of the schema
     *  @see getData in Database_Operations for call
     */
    private String getDateLastUsed(String schema)
    {
        StringBuffer date = new StringBuffer();

        //  declare connection record set
        Record_Set rs_id = new Record_Set();
        Connection myConnection = new Connection();
        
        //  create, prep initial query
        get_company_management_id_schemaOPS_query idQuery = new
                get_company_management_id_schemaOPS_query(schema);
        myConnection.prepQuery(idQuery);

        try
        {
            rs_id = myConnection.executeQuery(idQuery);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        //  create secondary query
        if(rs_id.length() > 0)
        {
            get_schema_last_used_query lastUsedQuery = new
                get_schema_last_used_query (rs_id.getInt("id"));
            Record_Set rs_last_used = new Record_Set();
            myConnection.prepQuery(lastUsedQuery);

            //  execute secondary query
            try
            {
                rs_last_used = myConnection.executeQuery(lastUsedQuery);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            if(rs_last_used.length() > 0)
                date.append(rs_last_used.getString("get_schedule_master_max_mod"));
            else
                date.append("Error! No date found.");
        }
        else
            date.append("No management ID found.  DO NOT TOUCH!");

        return date.toString();
    }
    
    /**
     *  Method Name:  getFileNames
     *  Purpose of Method:  gets all filanames for stored schemas, loads into
     *      data structure
     *  @see getData in Database_Operations for call
     */
    private void getFileNames()
    {
        //  get names, update count
        ArrayList<String> fileNamesFromServer = SchemaServletLoader.getFileNames();
        this.storedSchemaCount = fileNamesFromServer.size();

        for(Schema_Data element: this.schemaList)
        {
            String original = element.getSchemaName() + Database_Operations.FILE_EXTENSION;
            for(int idx = 0;idx < fileNamesFromServer.size();idx ++)
            {
                if(original.matches(fileNamesFromServer.get(idx)))
                {
                    element.setFileName(fileNamesFromServer.get(idx));
                    element.setIsActive();
                    fileNamesFromServer.remove(idx);
                }
            }
        }

        if(!fileNamesFromServer.isEmpty())
        {
            for(String element: fileNamesFromServer)
            {
                Schema_Data data = new Schema_Data.Builder()
                        .file(element)
                        .company(this.createCompanyName(element))
                        .schema(this.createSchemaName(element))
                        .build();
                schemaList.add(data);
            }
        }
    }

    /**
     *  Method Name:  getFilledDataObject
     *  Purpose of Method:  takes in a schema name, gets the company name
     *      and date last used from DB, creates a new data object, then
     *      returns that to be added into data structure
     *  @param schema -  a string representing the schema
     *  @return dataObject - an instance of Schema_Data to be added to the
     *      class data structure
     *  @see Schema_Data
     */
    private Schema_Data getFilledDataObject(String schema)
    {
        //  declare connection record set
        Record_Set rs_id = new Record_Set();
        Connection myConnection = new Connection();
        
        //  create, prep, execute initial query
        get_company_management_id_schemaOPS_query idQuery = new
                get_company_management_id_schemaOPS_query(schema);
        myConnection.prepQuery(idQuery);
        try
        {
            rs_id = myConnection.executeQuery(idQuery);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        //  create secondary query
        StringBuffer date = new StringBuffer();
        if(rs_id.length() > 0)
        {
            //  get previous rs data
            int id = rs_id.getInt("id");
            String company = rs_id.getString("company");

            //  create, prep, execute secondary query
            get_schema_last_used_query lastUsedQuery = new
                get_schema_last_used_query (id);
            Record_Set rs_last_used = new Record_Set();
            myConnection.prepQuery(lastUsedQuery);
            try
            {
                rs_last_used = myConnection.executeQuery(lastUsedQuery);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            //  get rs data
            if(rs_last_used.length() > 0)
                date.append(rs_last_used.getString("get_schedule_master_max_mod"));
            else
                date.append("Error! No date found.");

            //  create new data object
            return new Schema_Data.Builder().company(company).
                    date(date.toString()).schema(schema).isActive(true).build();
        }
        else
        {
            date.append("No management ID found.  DO NOT TOUCH!");
            return new Schema_Data.Builder().date(date.toString())
                .schema(schema).isActive(true).build();
        }
    }

    /**
     *  Method Name:  getData
     *  Purpose of Method:  hits the DB and gets the data needed to display
     *      information on the the schema table
    */
    private void getData()
    {
        Record_Set rs = new Record_Set();
        Connection myConnection = new Connection();
        get_schema_names_query query = new get_schema_names_query();
        myConnection.prepQuery(query);

        try
        {
            rs = myConnection.executeQuery(query);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        while(rs.moveNext())
        {
            Schema_Data tempData = this.getFilledDataObject(rs.getString("name"));
            schemaList.add(tempData);
        }

        Collections.sort(this.schemaList);
        this.checkPostgresSchemas();
        this.checkDoNotDisturbList();
        this.getFileNames();
    }

    /**
     *  Method Name:  createCompanyName
     *  Purpose of Method:  takes in a filename, splits it and returns a name
     *      for the Company
     *  @param fileName - a string representing the file name
     *  @return companyName - the properly formatted company name
     *  @see getFileNames 
     */
    private String createCompanyName(String fileName)
    {
        StringBuffer companyName = new StringBuffer();

        try
        {
            //  first split, remove .backup
            Pattern period = Pattern.compile("\\.");
            String[] periodSplitArray = period.split(fileName);
            StringBuffer periodSplit = new StringBuffer();
            for(String element:  periodSplitArray)
            {
                if(!element.matches(periodSplitArray[periodSplitArray.length - 1]))
                    periodSplit.append(element);
            }
        
            //  second split, remove _db
            Pattern underscore = Pattern.compile("\\_");
            String[] underscoreSplitArray = underscore.split(periodSplit.toString());
            StringBuffer underscoreSplit = new StringBuffer();
            for(String element:  underscoreSplitArray)
            {
                if(!element.matches(underscoreSplitArray[underscoreSplitArray.length - 1]))
                {
                    underscoreSplit.append(element);
                    underscoreSplit.append(" ");
                }
            }
            companyName.append(underscoreSplit.toString());
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            ex.printStackTrace();
        }

        return companyName.toString();
    }

    /**
     *  Method Name:  createSchemaName
     *  Purpose of Method:  takes ina  filename, splits it and returns the
     *      schema name for the file
     *  @param fileName - a string representing the file name
     *  @return schemaName - the properly formatted company name
     *  @see getFileNames 
     */
    private String createSchemaName(String fileName)
    {
        StringBuffer schemaName = new StringBuffer();
        
        try
        {
            Pattern period = Pattern.compile("\\.");
            String[] periodSplitArray = period.split(fileName);
            String periodSplit = periodSplitArray[0];
            schemaName.append(periodSplit);
        }
        catch(ArrayIndexOutOfBoundsException ex)
        {
            ex.printStackTrace();
        }

        return schemaName.toString();
    }

    /**
     *  Method Name:  setChildrenDataStructures
     *  Purpose of Method:  method creates two data structures for each child,
     *      returns them as two parts of an arraylist
     *  @return arrayOfDataStructures - an array of two data structures to be
     *      passed to the children
     */
    private ArrayList<ArrayList<Schema_Data>> setChildrenDataStructures()
    {
        ArrayList<ArrayList<Schema_Data>> returnList = new ArrayList<ArrayList<Schema_Data>>();
        ArrayList<Schema_Data> activeList = new ArrayList<Schema_Data>();
        ArrayList<Schema_Data> inactiveList = new ArrayList<Schema_Data>();
        ArrayList<Schema_Data> storedList = new ArrayList<Schema_Data>();

        //  create activeList data structure
        for(Schema_Data element: schemaList)
        {
            if(element.isActive())
                activeList.add(element);
        }
        returnList.add(activeList);

        //  create inactiveList data structure
        for(Schema_Data element:  schemaList)
        {
            if(!element.isActive())
                inactiveList.add(element);
        }
        returnList.add(inactiveList);

        //  create storedList
        for(Schema_Data element:  schemaList)
        {
            if(!element.getFileName().isEmpty())
                storedList.add(element);
        }
        returnList.add(storedList);

        return returnList;
    }

    /**
     *  Removes any schemas found in the Do Not Disturb List
     *
     *  <p>This method takes the initial data list and compares it against
     *      a list returned from the <code>SchemaServer</code>.  The list
     *      found server side is a list of <b>DO NOT DISTURB</b> schemas
     *      that should not show in the GUI at all.  If there is a match,
     *      that data object is removed from the list
     *
     */
    private void checkDoNotDisturbList()
    {
        //  get dnd list from server
        ArrayList<String> dndList = SchemaServletLoader.getDndList();

        //  iterate through internal list, removing any object found in the
        //      do no disturb list
        for(Iterator<Schema_Data> i = this.schemaList.iterator(); i.hasNext(); )
        {
            Schema_Data data = i.next();
            boolean isEqual = false;
            int dndIdx = 0;
            while( !isEqual && dndIdx < dndList.size())
            {
                String element = dndList.get(dndIdx);
                if(element.equalsIgnoreCase(data.getSchemaName()))
                    isEqual = true;
                
                dndIdx ++;
            }
            if(isEqual)
                i.remove();            
        }
    }

    /**
     *  Checks for postgres schemas and removes them
     *
     *  <p>This method checks for postgres specific schemas such as <code>pg_temp_<number></code>
     *      and <code>pg_toast_temp_<number></code>.  If there is a match,
     *      that schema is removed from the list so that it cannot be displayed
     */
    private void checkPostgresSchemas()
    {
        for(Iterator<Schema_Data> i = this.schemaList.iterator(); i.hasNext();)
        {
            Schema_Data element = i.next();
            String schema = element.getSchemaName();
            String pg_temp_CHECK = null;
            String pg_toast_temp_CHECK = null;
            boolean isPostgresSchema = false;
            try
            {
                pg_temp_CHECK = schema.substring(0, 7);
                if ( pg_temp_CHECK.equalsIgnoreCase("pg_temp"))
                    isPostgresSchema = true;
            }
            catch(StringIndexOutOfBoundsException ex)
            {
                //  fail silently
            }
            try
            {
                pg_toast_temp_CHECK = schema.substring(0, 8);
                if ( pg_toast_temp_CHECK.equalsIgnoreCase("pg_toast"))
                    isPostgresSchema = true;
            }
            catch(StringIndexOutOfBoundsException ex)
            {
                //  fail silently
            }

            if( isPostgresSchema )
                i.remove();
        }
    }

    /**
     *  Method Name:  getStoredCount
     *  Purpose of Method:  returns the number of stored schemas
     *  @return storedCount - an int describing the number of stored schemas
     */
    protected int getStoredCount()  {return this.storedSchemaCount;}
    
    /** Creates new form Database_Operations */
    public Database_Operations()
    {
        this.storedSchemaCount = 0;
        initComponents();

        schemaList = new ArrayList<Schema_Data>();
        this.getData();
        ArrayList<ArrayList<Schema_Data>> listToChildren = this.setChildrenDataStructures();

        activeSchemaGUI = new Active_Schema_Operations_GUI(this, listToChildren.get(0));
        inactiveSchemaGUI = new Inactive_Schema_Operations_GUI(this, listToChildren.get(1));
        storedSchemaGUI = new Stored_Schemas_Operations_GUI(this, listToChildren.get(2));
        this.jCardComboBox.addItem(ACTIVE_SCHEMAS);
        this.jCardComboBox.addItem(INACTIVE_SCHEMAS);
        this.jCardComboBox.addItem(STORED_SCHEMAS);
        this.jCardPanel.add(activeSchemaGUI, ACTIVE_SCHEMAS);
        this.jCardPanel.add(inactiveSchemaGUI, INACTIVE_SCHEMAS);
        this.jCardPanel.add(storedSchemaGUI, STORED_SCHEMAS);
   }

    public void reload()
    {
        this.setVisible(true);

        this.schemaList.clear();
        this.getData();
        ArrayList<ArrayList<Schema_Data>> listToChildren = this.setChildrenDataStructures();
        activeSchemaGUI.setList(listToChildren.get(0));
        inactiveSchemaGUI.setList(listToChildren.get(1));
        storedSchemaGUI.setList(listToChildren.get(2));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonPanel = new javax.swing.JPanel();
        jCardComboBox = new javax.swing.JComboBox();
        jCardPanel = new javax.swing.JPanel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setResizable(true);
        setTitle("Database Operations");
        setMinimumSize(new java.awt.Dimension(300, 85));
        setNormalBounds(new java.awt.Rectangle(0, 0, 100, 0));
        setPreferredSize(new java.awt.Dimension(300, 85));

        jButtonPanel.setLayout(new java.awt.GridBagLayout());

        jCardComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCardComboBoxActionPerformed(evt);
            }
        });
        jButtonPanel.add(jCardComboBox, new java.awt.GridBagConstraints());

        getContentPane().add(jButtonPanel, java.awt.BorderLayout.NORTH);

        jCardPanel.setLayout(new java.awt.CardLayout());
        getContentPane().add(jCardPanel, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-919)/2, (screenSize.height-550)/2, 919, 550);
    }// </editor-fold>//GEN-END:initComponents

    private void jCardComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCardComboBoxActionPerformed
        CardLayout cl = (CardLayout) (jCardPanel.getLayout());
        cl.show(jCardPanel, (String) jCardComboBox.getSelectedItem());
    }//GEN-LAST:event_jCardComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jButtonPanel;
    private javax.swing.JComboBox jCardComboBox;
    private javax.swing.JPanel jCardPanel;
    // End of variables declaration//GEN-END:variables
};
