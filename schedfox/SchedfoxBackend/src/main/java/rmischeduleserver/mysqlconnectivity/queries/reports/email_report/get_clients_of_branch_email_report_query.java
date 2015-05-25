/*  package declaration */
package rmischeduleserver.mysqlconnectivity.queries.reports.email_report;

/*  import declarations */
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  A simple query object to retrieve all clients of a particular branch.
 *  <p><b>NOTE:  </b> this query object uses <i>PreparedStatements</i>
 *  @author Jeffrey N. Davis
 *  @see rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat
 *  @since 04/22/2011
 */
public final class get_clients_of_branch_email_report_query extends GeneralQueryFormat
{
    /*  private variable declarations   */
    private StringBuilder sqlQuery;
    private final int branchId;
    
    /*  object instantiation code   */
    /**
     *  Default construction of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    private get_clients_of_branch_email_report_query()
    {
        throw new UnsupportedOperationException ( "Default instantiation of the object get_clients_of_branch_email_report_query is not allowed." );
    }
    
    /**
     *  Constructs an instance of this object.
     *  @param branchId an int describing the branch_id
     */
    public get_clients_of_branch_email_report_query ( int branchId )
    {
        /*  assign class variables  */
        this.branchId = branchId;
        
        /*  set prepared statement  */
        super.setPreparedStatement ( new Object[] {
            this.branchId
        });
        
        /*  construct query */
        this.sqlQuery = this.constructPreparedStatement();
    }
    
    private StringBuilder constructPreparedStatement()
    {
        StringBuilder returnQuery = new StringBuilder();
        
        returnQuery.append ( "SELECT client_id, client_name, client_is_deleted " );
        returnQuery.append ( "FROM client " );
        returnQuery.append ( "WHERE ");
        returnQuery.append ( "branch_id = ? ;");        
        
        returnQuery.trimToSize();
        return returnQuery;
    }
    
    /**
     *  Constructs and returns the prepared statement.
     *  @return sqlQuery a string containing the prepared statement
     */
    @Override
    public String getPreparedStatementString()  { return this.sqlQuery.toString(); }

    @Override
    public boolean hasPreparedStatement()   { return true; }

    /** Describes whether this object has database access
     *  <p><b>Default:  </b><i>TRUE</i>
     *  @return hasAccess a boolean describing if this object has access to database
     */
    @Override
    public boolean hasAccess() {    return true; }

    @Override
    public String toString()    { return this.sqlQuery.toString(); }
};
