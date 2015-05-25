//  package declaration
package rmischedule.reports.email.controllers;

//  import declarations
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.apache.commons.validator.EmailValidator;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import rmischedule.reports.email.models.BranchEmailReportData;
import rmischedule.reports.email.models.ClientEmailReportData;
import rmischedule.reports.email.models.EmailReportCompanyComboBoxModel;
import rmischedule.reports.email.models.EmployeeEmailReportData;
import rmischedule.reports.email.views.InvalidClientEmailReportDiag;
import rmischedule.reports.email.views.EmployeeEmailReportDiag;
import rmischedule.reports.email.views.InactiveClientEmailReportDiag;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Branch;
import rmischeduleserver.mysqlconnectivity.queries.reports.email_report.get_client_contact_data_email_report_query;
import rmischeduleserver.mysqlconnectivity.queries.reports.email_report.get_clients_of_branch_email_report_query;
import rmischeduleserver.mysqlconnectivity.queries.reports.email_report.get_employee_contact_data_email_report_query;

/**
 *  This object is a singleton controller for the Email Report System.
 *  @author Jeffrey N. Davis
 *  @since 04/21/2011
 */
public class EmailReportController 
{
    /*  object variable declarations    */
    private InvalidClientEmailReportDiag invalidClientView;
    private EmployeeEmailReportDiag employeeView;
    private InactiveClientEmailReportDiag inactiveClientView;
    private List<Company> companyList;
    private List<BranchEmailReportData> branchList;
    private Map<Integer, ClientEmailReportData> clientReportMap;
    private Map<Integer, EmployeeEmailReportData> employeeReportMap;
    
    /*  singleton code  */
    private static final EmailReportController INSTANCE = new EmailReportController();
    
    /**
     *  Default instantiation of this object.
     */
    private EmailReportController()
    {
        
    }
    
    /**
     *  Returns the one true instance of {@code EmailReportController}
     *  @return INSTANCE
     */
    public static EmailReportController getInstance()   {  return INSTANCE;  }
    
    /*  private method implementations  */
    private void resetSingleton()
    {
        if ( this.companyList!= null && !this.companyList.isEmpty() )
        {
            this.companyList.clear();
            this.companyList = null;
        }
        if ( this.branchList != null && !this.branchList.isEmpty() )
        {
            this.branchList.clear();
            this.branchList = null;
        }
        if ( this.clientReportMap != null && !this.clientReportMap.isEmpty() )
        {
            this.clientReportMap.clear();
            this.clientReportMap = null;
        }
        if ( this.employeeReportMap != null && !this.employeeReportMap.isEmpty() )
        {
            this.employeeReportMap.clear();
            this.employeeReportMap = null;
        }
        
        this.branchList = new LinkedList<BranchEmailReportData>();
        this.clientReportMap = new LinkedHashMap<Integer, ClientEmailReportData>();
        this.employeeReportMap = new LinkedHashMap<Integer, EmployeeEmailReportData>();
    }
    
    private void convertCompanyVector ( Vector<Company> activeVectorCompany )
    {
        this.companyList = new LinkedList<Company>();
        
        for ( Company element: activeVectorCompany )
            this.companyList.add( element );        
    }
    
    private void initializeInvalidClientView()
    {
        EmailReportCompanyComboBoxModel companyComboBoxModel = new EmailReportCompanyComboBoxModel ( this.companyList );
        this.invalidClientView.getCompaniesComboBox().setModel( companyComboBoxModel );
        this.invalidClientView.getCompaniesComboBox().setSelectedIndex( 0 );
        this.invalidClientCompanyComboBoxAction();
    }
    
    private void initializeEmployeeView()
    {
        EmailReportCompanyComboBoxModel companyComboBoxModel = new EmailReportCompanyComboBoxModel ( this.companyList );
        this.employeeView.getCompaniesComboBox().setModel( companyComboBoxModel );
        this.employeeView.getCompaniesComboBox().setSelectedIndex( 0 );
        this.employeeCompanyComboBoxAction();
    }
    
    private void initializeInactiveClientView()
    {
        EmailReportCompanyComboBoxModel companyComboBoxModel = new EmailReportCompanyComboBoxModel ( this.companyList );
        this.inactiveClientView.getCompaniesComboBox().setModel( companyComboBoxModel );
        this.inactiveClientView.getCompaniesComboBox().setSelectedIndex( 0 );
        this.inactiveClientCompanyComboBoxAction();
    }
    
    private boolean isInvalidClientReady()
    {
        boolean isReady = true;
        
        /*  check to see if any branches are selected   */
        boolean isAnySelected = false;
        for ( BranchEmailReportData element:  this.branchList )
        {
            if ( element.isSelected() )
                isAnySelected = true;
        }
        if ( !isAnySelected )
        {
            JOptionPane.showMessageDialog( this.invalidClientView, "You must select at least one branch to run this report.", "Email Reports", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        /*  check to see if Invalid Email or Empty Email is selected    */
        if ( !this.invalidClientView.getInvalidCheckBox().isSelected() && ! this.invalidClientView.getEmptyCheckBox().isSelected() )
        {
            JOptionPane.showMessageDialog( this.invalidClientView, "You must select which report to run, Invalid or Empty", "Email Reports", JOptionPane.ERROR_MESSAGE );
            return false;
        }
        
        return isReady;
    }
    
    private boolean isEmployeeReady()
    {
        boolean isReady = true;
        
        /*  check to see if any branches are selected   */
        boolean isAnySelected = false;
        for ( BranchEmailReportData element:  this.branchList )
        {
            if ( element.isSelected() )
                isAnySelected = true;
        }
        if ( !isAnySelected )
        {
            JOptionPane.showMessageDialog( this.employeeView, "You must select at least one branch to run this report.", "Email Reports", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        /*  check to see if Invalid Email or Empty Email is selected    */
        if ( !this.employeeView.getInvalidCheckBox().isSelected() && ! this.employeeView.getEmptyCheckBox().isSelected() )
        {
            JOptionPane.showMessageDialog( this.employeeView, "You must select which report to run, Invalid or Empty", "Email Reports", JOptionPane.ERROR_MESSAGE );
            return false;
        }
        
        return isReady;
    }
    
    private void informError()
    {
        JOptionPane.showMessageDialog( Main_Window.parentOfApplication, "An error occurred while running the report.  Please contact SchedFox administrators.",
                "Error running Email Reports", JOptionPane.ERROR_MESSAGE);
    }
    
    private boolean isEmailAddressValid( String emailAddress )
    {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid( emailAddress );
    }
    
    private void runClientInactiveReport()
    {
        try
        {
            File reportFile = File.createTempFile( "Inactive Client Email Addresses Report", ".txt");
            PrintWriter writer = new PrintWriter ( reportFile );
            writer.println( "This is an Inactive Client Email Address Report. ");
            Collection<ClientEmailReportData> collection = this.clientReportMap.values();
            for ( ClientEmailReportData element:  collection )
            {
                if ( element.getEmail().startsWith( "zzz") )
                    writer.println( element.getReportLine() );
            }
            writer.flush();
            writer.close();

            Desktop.getDesktop().open( reportFile );        
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
            this.informError();
        }
    }
    
    private void runClientInvalidReport()
    {
       try
       {
            File reportFile = File.createTempFile( "Invalid Client Email Addresses Report", ".txt" );
            PrintWriter writer = new PrintWriter ( reportFile );
            writer.println( "This is a Invalid Client Email Address Report. ");
            Collection<ClientEmailReportData> collection = this.clientReportMap.values();
            for ( ClientEmailReportData element:  collection )
            {
                if ( element.getEmail().length() > 1 )
                    writer.println( element.getReportLine() );
            }
            writer.flush();
            writer.close();

            Desktop.getDesktop().open( reportFile );
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        } 
    }
    
    private void runClientEmptyReport()
    {
       try
       {
            File reportFile = File.createTempFile( "Empty Client Email Addresses Report", ".txt" );
            PrintWriter writer = new PrintWriter ( reportFile );
            writer.println( "This is a Empty Client Email Address Report. ");
            Collection<ClientEmailReportData> collection = this.clientReportMap.values();
            for ( ClientEmailReportData element:  collection )
            {
                if ( element.getEmail().length() < 2 )
                    writer.println( element.getReportLine() );
            }
            writer.flush();
            writer.close();

            Desktop.getDesktop().open( reportFile );
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        } 
    }
    
    private void runEmployeeInvalidReport()
    {
        try
        {
            boolean useExEmployees = this.employeeView.getCheckExEmployees().isSelected();
            File reportFile = File.createTempFile( "Invalid Employee Email Addresses Report", ".txt" );
            PrintWriter writer = new PrintWriter ( reportFile );
            writer.println( "This is a Invalid Employee Email Address Report. ");
            Collection<EmployeeEmailReportData> collection = this.employeeReportMap.values();
            for ( EmployeeEmailReportData element:  collection )
            {
                if ( useExEmployees )
                {
                    if ( element.getEmail().length() > 1 )
                        writer.println( element.getReportLine() );
                }
                else
                {
                    if ( element.getBranch().length() > 1 && element.isActive() )
                        writer.println( element.getReportLine() );
                }                
            }
            writer.flush();
            writer.close();

            Desktop.getDesktop().open( reportFile );
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }
    }
    
    private void runEmployeeEmptyReport()
    {
       try
       {
            boolean useExEmployees = this.employeeView.getCheckExEmployees().isSelected();
            File reportFile = File.createTempFile( "Empty Employee Email Addresses Report", ".txt" );
            PrintWriter writer = new PrintWriter ( reportFile );
            writer.println( "This is a Empty Employee Email Address Report. ");
            Collection<EmployeeEmailReportData> collection = this.employeeReportMap.values();
            for ( EmployeeEmailReportData element:  collection )
            {
                if ( useExEmployees )
                {
                    if ( element.getEmail().length() < 2 )
                        writer.println( element.getReportLine() );
                }
                else
                {
                    if ( element.getEmail().length() < 2 && element.isActive() )
                        writer.println( element.getReportLine() );
                }
                
            }
            writer.flush();
            writer.close();

            Desktop.getDesktop().open( reportFile );
        }
        catch ( IOException ex )
        {
            ex.printStackTrace();
        }
    }
    
    /*  public method implementations   */
    /**
     *  Initializes this controller/models/views for <b>CLIENT</b> email reporting.
     *  @param activeVectorCompany a vector of companies to run reports on
     */
    public void initInvalidClientEmailSystem( Vector<Company> activeVectorCompany)
    {
        this.resetSingleton();
        this.convertCompanyVector( activeVectorCompany );
        this.invalidClientView = new InvalidClientEmailReportDiag();
        this.initializeInvalidClientView();
        this.invalidClientView.pack();
        this.invalidClientView.setVisible(true);
    }
    
    /**
     *  Initializes this controller/models/views for <b>EMPLOYEE</b> email reporting.
     *  @param activeVectorCompany a vector of companies to run reports on
     */
    public void initEmployeeEmailSystem( Vector<Company> activeVectorCompany )
    {
        this.resetSingleton();
        this.convertCompanyVector( activeVectorCompany );
        this.employeeView = new EmployeeEmailReportDiag();
        this.initializeEmployeeView();
        this.employeeView.pack();
        this.employeeView.setVisible(true);
    }
    
    /**
     *  Initializes this controller/models/views for <b>INACTIVE CLIENT</b> email reporting.
     *  @param activeVectorCompany a vector of companies to run reports on
     */
    public void initInactiveClientEmailSystem ( Vector<Company> activeVectorCompany )
    {
        this.resetSingleton();
        this.convertCompanyVector(activeVectorCompany);
        this.inactiveClientView = new InactiveClientEmailReportDiag();
        this.initializeInactiveClientView();
        this.inactiveClientView.pack();
        this.inactiveClientView.setVisible(true);        
    }
    
    /*  INVALID Client View Controls    */
    /**
     *  Controls the action when a company is selected from the drop down menu
     *      in the client view.
     */
    public void invalidClientCompanyComboBoxAction()
    {
        Company company = ( Company ) this.invalidClientView.getCompaniesComboBox().getSelectedItem();
        /*  transform vector to linkedlist  */
        Vector<Branch> vector = company.getBranches();
        this.branchList.clear();
        for ( Branch element:  vector )
        {
            int companyId = Integer.parseInt( company.getId() );
            int branchId = element.getBranchId();
            BranchEmailReportData data = new BranchEmailReportData ( companyId, branchId, element.getBranchName() );
            this.branchList.add( data );
        }
        
        /*  ensure all are not selected initially   */
        for ( BranchEmailReportData element:  this.branchList )
            element.setSelected( false );
        
        /*  add components with constraints */
        this.invalidClientView.getBranchPanel().removeAll();
        GridBagConstraints c = new GridBagConstraints();
        int idx = 0;
        for ( BranchEmailReportData element:  this.branchList )
        {
            /*  set constraints */
            c.gridx = idx % 4;
            c.gridy = idx / 4;
            c.weightx = .25;
            c.weighty = .25;
            c.anchor = GridBagConstraints.WEST;
            idx ++;
            
            /*  add button to panel */
            element.setText ( element.getBranchName() );
            this.invalidClientView.getBranchPanel().add( element, c );
        }
        
        this.invalidClientView.getBranchPanel().revalidate();
        this.invalidClientView.getBranchPanel().repaint();
    }
    
    /**
     *  Controls the action when the Select All button is pressed in the client view.
     */
    public void invalidClientSelectAllAction()
    {
        boolean isSelected = this.invalidClientView.getSelectAllButton().isSelected();
        for ( BranchEmailReportData element:  this.branchList )
            element.setSelected( isSelected );
    }
    
    /**
     *  Controls the Run Report action from {@link rmischedule.reports.views.InvalidClientEmailReportDiag}
     */
    public void invalidClientRunReportAction()
    {
        if ( this.isInvalidClientReady() )
        {
            List<ClientEmailReportData> list = new LinkedList<ClientEmailReportData>();
            for ( BranchEmailReportData element:  this.branchList )
            {
                if ( element.isSelected() )
                {
                    String branchId = Integer.toString(element.getBranchId());
                    String companyId = Integer.toString(element.getCompanyId());
                    
                    /*  create connection, prep query   */
                    Connection clientConnection = new Connection();
                    clientConnection.setCompany(companyId);
                    clientConnection.setBranch(branchId);
                    get_clients_of_branch_email_report_query clientQuery = new get_clients_of_branch_email_report_query ( element.getBranchId() );
                    clientConnection.prepQuery(clientQuery);
                    Record_Set clientRs = null;
                    
                    /*  execute client query    */
                    try
                    {
                        clientRs = clientConnection.executeQuery(clientQuery);
                    }
                    catch ( Exception ex )
                    {
                        ex.printStackTrace();
                        this.informError();
                    }
                    
                    /*  parse record set    */
                    if ( clientRs.length() > 0 )
                    {
                        while ( clientRs.moveNext() )
                        {
                            int clientId = clientRs.getInt( "client_id" );
                            String clientName = clientRs.getString( "client_name" );
                            boolean clientDeleted = ( clientRs.getInt( "client_is_deleted") == 0 ) ? true : false;
                            
                            /*  create connection, prep query   */
                            Connection contactConnection = new Connection();
                            contactConnection.setCompany(companyId);
                            contactConnection.setBranch(branchId);
                            get_client_contact_data_email_report_query contactQuery = new get_client_contact_data_email_report_query( clientId );
                            contactConnection.prepQuery(contactQuery);
                            Record_Set contactRs = null;
                            
                            /*  execute contact query   */
                            try
                            {
                                contactRs = contactConnection.executeQuery(contactQuery);
                            }
                            catch ( Exception ex )
                            {
                                ex.printStackTrace();
                                this.informError();
                            }
                            
                            /*  parse contactRs */
                            if ( contactRs.length() > 0 )
                            {
                                while ( contactRs.moveNext() )
                                {
                                    String emailAddress = contactRs.getString( "email" );
                                    if ( !this.isEmailAddressValid ( emailAddress ) )
                                    {
                                        boolean isPrimary = ( contactRs.getInt ("primary") == 0) ? true : false;
                                        ClientEmailReportData data = new ClientEmailReportData.Builder()
                                            .email( emailAddress )
                                            .firstName( contactRs.getString( "first" ))
                                            .lastName( contactRs.getString( "last" ))
                                            .branch( element.getBranchName() )
                                            .city( contactRs.getString( "city" ))
                                            .state( contactRs.getString( "state" ))
                                            .contactTitle( contactRs.getString( "title" ))
                                            .clientName( clientName )
                                            .isActive(clientDeleted)
                                            .isPrimary( isPrimary )
                                            .build();
                                        
                                        list.add(data);
                                    }   //  end innermost if
                                }   //  end while
                            }   //  end if
                        }   //  end out while
                    }   //  end outer if
                    else
                        this.informError();
                }
            }
            
            /*  sort, place in map  */
            Collections.sort(list);
            for ( ClientEmailReportData element:  list )
                this.clientReportMap.put( element.hashCode(), element );
                        
            /*  run reports */
            if ( this.invalidClientView.getInvalidCheckBox().isSelected() )
                this.runClientInvalidReport();
            if ( this.invalidClientView.getEmptyCheckBox().isSelected() )
                this.runClientEmptyReport();
        }
    }
    
    /*  employee view controls  */
    public void employeeCompanyComboBoxAction()
    {
        Company company = ( Company ) this.employeeView.getCompaniesComboBox().getSelectedItem();
        /*  transform vector to linkedlist  */
        Vector<Branch> vector = company.getBranches();
        this.branchList.clear();
        for ( Branch element:  vector )
        {
            int companyId = Integer.parseInt( company.getId() );
            int branchId = element.getBranchId();
            BranchEmailReportData data = new BranchEmailReportData ( companyId, branchId, element.getBranchName() );
            this.branchList.add( data );
        }
        
        /*  ensure all are not selected initially   */
        for ( BranchEmailReportData element:  this.branchList )
            element.setSelected( false );
        
        /*  add components with constraints */
        this.employeeView.getBranchPanel().removeAll();
        GridBagConstraints c = new GridBagConstraints();
        int idx = 0;
        for ( BranchEmailReportData element:  this.branchList )
        {
            /*  set constraints */
            c.gridx = idx % 4;
            c.gridy = idx / 4;
            c.weightx = .25;
            c.weighty = .25;
            c.anchor = GridBagConstraints.WEST;
            idx ++;
            
            /*  add button to panel */
            element.setText ( element.getBranchName() );
            this.employeeView.getBranchPanel().add( element, c );
        }
        
        this.employeeView.getBranchPanel().revalidate();
        this.employeeView.getBranchPanel().repaint();
    }
    
    public void employeeSelectAllAction()
    {
        boolean isSelected = this.employeeView.getSelectAllButton().isSelected();
        for ( BranchEmailReportData element:  this.branchList )
            element.setSelected( isSelected );
    }
    
    public void employeeRunReportAction()
    {
        if ( this.isEmployeeReady() )
        {
            List<EmployeeEmailReportData> list = new LinkedList<EmployeeEmailReportData>();
            for ( BranchEmailReportData element:  this.branchList )
            {
                String branchId = Integer.toString( element.getBranchId() );
                String companyId = Integer.toString( element.getCompanyId() );
                
                /*  create connection, prep query   */
                Connection employeeConnection = new Connection();
                employeeConnection.setCompany(companyId);
                employeeConnection.setBranch(branchId);
                get_employee_contact_data_email_report_query query = new get_employee_contact_data_email_report_query ( element.getBranchId() );
                employeeConnection.prepQuery(query);
                Record_Set rs = null;
                
                try
                {
                    rs = employeeConnection.executeQuery(query);
                }
                catch ( Exception ex )
                {
                    ex.printStackTrace();
                    this.informError();
                }
                
                /*  parse record set    */
                if ( rs.length() > 0 )
                {
                    while ( rs.moveNext() )
                    {
                        String emailAddress = rs.getString( "email" );
                        if ( !this.isEmailAddressValid( emailAddress ))
                        {
                            boolean isActive = ( rs.getInt( "active" ) == 0 ) ? true : false;
                            EmployeeEmailReportData data = new EmployeeEmailReportData.Builder()
                                    .email( emailAddress )
                                    .isActive(isActive)
                                    .firstName( rs.getString( "first" ))
                                    .lastName( rs.getString( "last" ))
                                    .branch( element.getBranchName() )
                                    .city( rs.getString( "city" ))
                                    .state( rs.getString( "state" ))
                                    .address( rs.getString( "address" ))
                                    .phone( rs.getString ( "phone" ))
                                    .cell( rs.getString ( "cell" ))
                                    .build();
                            
                            list.add( data );
                        }   //  end invalidEmail if
                    }   //  end record set while iterator
                }   //  end rs length if check
            }   //  end branch iteration for loop
            
            /*  sort, place in map  */
            Collections.sort(list);
            for ( EmployeeEmailReportData element:  list )
                this.employeeReportMap.put( element.hashCode(), element);
            
            /*  run reports */
            if ( this.employeeView.getInvalidCheckBox().isSelected() )
                this.runEmployeeInvalidReport();
            if ( this.employeeView.getEmptyCheckBox().isSelected() )
                this.runEmployeeEmptyReport();
        }   //  end initial if statement
    }   //  end method block
    
    /*  INACTIVE Client View Controls   */
    public void inactiveClientSelectAllAction()
    {
        boolean isSelected = this.inactiveClientView.getSelectAllButton().isSelected();
        for ( BranchEmailReportData element:  this.branchList )
            element.setSelected( isSelected );
    }
    
    public void inactiveClientCompanyComboBoxAction()
    {
        Company company = ( Company ) this.inactiveClientView.getCompaniesComboBox().getSelectedItem();
        /*  transform vector to linkedlist  */
        Vector<Branch> vector = company.getBranches();
        this.branchList.clear();
        for ( Branch element:  vector )
        {
            int companyId = Integer.parseInt( company.getId() );
            int branchId = element.getBranchId();
            BranchEmailReportData data = new BranchEmailReportData ( companyId, branchId, element.getBranchName() );
            this.branchList.add( data );
        }
        
        /*  ensure all are not selected initially   */
        for ( BranchEmailReportData element:  this.branchList )
            element.setSelected( false );
        
        /*  add components with constraints */
        this.inactiveClientView.getBranchPanel().removeAll();
        GridBagConstraints c = new GridBagConstraints();
        int idx = 0;
        for ( BranchEmailReportData element:  this.branchList )
        {
            /*  set constraints */
            c.gridx = idx % 4;
            c.gridy = idx / 4;
            c.weightx = .25;
            c.weighty = .25;
            c.anchor = GridBagConstraints.WEST;
            idx ++;
            
            /*  add button to panel */
            element.setText ( element.getBranchName() );
            this.inactiveClientView.getBranchPanel().add( element, c );
        }
        
        this.inactiveClientView.getBranchPanel().revalidate();
        this.inactiveClientView.getBranchPanel().repaint();
    }
    
    public void inactiveClientRunReportAction()
    {
        List<ClientEmailReportData> list = new LinkedList<ClientEmailReportData>();
        for ( BranchEmailReportData element:  this.branchList )
        {
            if ( element.isSelected() )
            {
                String branchId = Integer.toString(element.getBranchId());
                String companyId = Integer.toString(element.getCompanyId());
                    
                /*  create connection, prep query   */
                Connection clientConnection = new Connection();
                clientConnection.setCompany(companyId);
                clientConnection.setBranch(branchId);
                get_clients_of_branch_email_report_query clientQuery = new get_clients_of_branch_email_report_query ( element.getBranchId() );
                clientConnection.prepQuery(clientQuery);
                Record_Set clientRs = null;
                   
                /*  execute client query    */
                try
                {
                    clientRs = clientConnection.executeQuery(clientQuery);
                }
                catch ( Exception ex )
                {
                    ex.printStackTrace();
                    this.informError();
                }
                    
                /*  parse record set    */
                if ( clientRs.length() > 0 )
                {
                    while ( clientRs.moveNext() )
                    {
                        int clientId = clientRs.getInt( "client_id" );
                        String clientName = clientRs.getString( "client_name" );
                        boolean clientDeleted = ( clientRs.getInt( "client_is_deleted") == 0 ) ? true : false;
                         
                        /*  create connection, prep query   */
                        Connection contactConnection = new Connection();
                        contactConnection.setCompany(companyId);
                        contactConnection.setBranch(branchId);
                        get_client_contact_data_email_report_query contactQuery = new get_client_contact_data_email_report_query( clientId );
                        contactConnection.prepQuery(contactQuery);
                        Record_Set contactRs = null;
                            
                        /*  execute contact query   */
                        try
                        {
                            contactRs = contactConnection.executeQuery(contactQuery);
                        }
                        catch ( Exception ex )
                        {
                            ex.printStackTrace();
                            this.informError();
                        }
                            
                        /*  parse contactRs */
                        if ( contactRs.length() > 0 )
                        {
                            do
                            {
                                String emailAddress = contactRs.getString( "email" );
                                if ( this.isEmailAddressValid ( emailAddress ) )
                                {
                                    boolean isPrimary = ( contactRs.getInt ("primary") == 0) ? true : false;
                                    ClientEmailReportData data = new ClientEmailReportData.Builder()
                                        .email( emailAddress )
                                        .firstName( contactRs.getString( "first" ))
                                        .lastName( contactRs.getString( "last" ))
                                        .branch( element.getBranchName() )
                                        .city( contactRs.getString( "city" ))
                                        .state( contactRs.getString( "state" ))
                                        .contactTitle( contactRs.getString( "title" ))
                                        .clientName( clientName )
                                        .isActive(clientDeleted)
                                        .isPrimary( isPrimary )
                                        .build();
                                        
                                    list.add(data);
                                 }   //  end innermost if
                            }  while ( contactRs.moveNext() );
                        }   //  end if
                   }   //  end out while
                }   //  end outer if
                else
                    this.informError();
            }
        }
            
        /*  sort, place in map  */
        Collections.sort(list);
        for ( ClientEmailReportData element:  list )
            this.clientReportMap.put( element.hashCode(), element );
                        
        /*  run report */
        this.runClientInactiveReport();   
    }
    
    public boolean isInactiveClientReady()
    {
        /*  check to see if any branches are selected   */
        boolean isAnySelected = false;
        for ( BranchEmailReportData element:  this.branchList )
            if ( element.isSelected() )
                isAnySelected = true;
        
        if ( !isAnySelected )
            JOptionPane.showMessageDialog( this.employeeView, "You must select at least one branch to run this report.", "Email Reports", JOptionPane.ERROR_MESSAGE);
                    
        return isAnySelected;
    }
};  //  end class block
