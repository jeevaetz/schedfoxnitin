/*  package declaration */
package rmischeduleserver.mysqlconnectivity.queries.reports.email_report;

/*  import declarations */
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  A query object to retrieve all contacts based on @{@code clientId}
 *  <p><b>NOTE: </b> this query object uses <i>PreparedStatements</i>
 *  <p><b>NOTE: </b> extends {@code GeneralQueryFormat}
 *  @author Jeffrey N. Davis
 *  @see rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat
 *  @since 04/22/2011
 */
public final class get_client_contact_data_email_report_query extends GeneralQueryFormat
{
    /*  private variable declarations   */
    private StringBuilder sqlQuery;
    private final int clientId;
    
    /*  object instantiation code   */
    /**
     *  Default instantiation of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    private get_client_contact_data_email_report_query()
    {
        throw new UnsupportedOperationException ( "Default instantiation of the object get_client_contact_data_email_report_query is not allowed." );
    }
    
    /**
     *  Constructs a valid instance of this object
     *  @param clientId an int describing the clientId
     */
    public get_client_contact_data_email_report_query ( int clientId )
    {
        /*  assign class variables  */
        this.clientId = clientId;
        
        /*  set prepared statement  */
        super.setPreparedStatement( new Object[] {
            this.clientId
        });
        
        /*  construct query */
        this.sqlQuery = this.constructPreparedStatement();
    }
    
    private StringBuilder constructPreparedStatement()
    {
        StringBuilder returnQuery = new StringBuilder();
        
        returnQuery.append ( "SELECT " );
        returnQuery.append ( "client_contact_is_primary AS primary, " );
        returnQuery.append ( "client_contact_title AS title, " );
        returnQuery.append ( "client_contact_first_name AS first, " );
        returnQuery.append ( "client_contact_last_name AS last, " );
        returnQuery.append ( "client_contact_city AS city, " );
        returnQuery.append ( "client_contact_state AS state, " );
        returnQuery.append ( "client_contact_email AS email, " );
        returnQuery.append ( "client_contact_is_deleted AS deleted " );
        returnQuery.append ( "FROM client_contact " );
        returnQuery.append ( "WHERE client_id = ? ;" );
        
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
