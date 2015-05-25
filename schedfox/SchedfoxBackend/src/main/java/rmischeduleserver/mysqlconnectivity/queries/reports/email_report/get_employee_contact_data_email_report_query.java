/*  package declaration */
package rmischeduleserver.mysqlconnectivity.queries.reports.email_report;

/*  import declarations */
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  A query object to retrieve all contacts based on @{@code branchId}
 *  <p><b>NOTE: </b> this query object uses <i>PreparedStatements</i>
 *  <p><b>NOTE: </b> extends {@code GeneralQueryFormat}
 *  @author Jeffrey N. Davis
 *  @see rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat
 *  @since 04/25/2011
 */
public class get_employee_contact_data_email_report_query extends GeneralQueryFormat
{
    /*  object variable declarations    */
    private StringBuilder sqlQuery;
    private final int branchId;
    
    /*  object instantiation code   */
    /**
     *  Default instantiation of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    private get_employee_contact_data_email_report_query()
    {
        throw new UnsupportedOperationException ( "Default instantiation of the object get_employee_contact_data_email_report_query is not allowed." );
    }
    
    /**
     *  Constructs a valid instance of this object
     *  @param clientId an int describing the clientId
     */
    public get_employee_contact_data_email_report_query ( int branchId )
    {
        /*  assign class variables  */
        this.branchId = branchId;
        
        /*  set prepared statement  */
        super.setPreparedStatement( new Object[] {
            this.branchId
        });
        
        /*  construct query */
        this.sqlQuery = this.constructPreparedStatement();
    }
    
    private StringBuilder constructPreparedStatement()
    {
        StringBuilder returnQuery = new StringBuilder();
        
        returnQuery.append ( "SELECT " );
        returnQuery.append ( "employee_first_name AS first, " );
        returnQuery.append ( "employee_last_name AS last, " );
        returnQuery.append ( "employee_address AS address, " );
        returnQuery.append ( "employee_phone AS phone, " );
        returnQuery.append ( "employee_cell AS cell, " );
        returnQuery.append ( "employee_email AS email, " );
        returnQuery.append ( "employee_city AS city, " );
        returnQuery.append ( "employee_state AS state, " );
        returnQuery.append ( "employee_is_deleted AS active " );
        returnQuery.append ( "FROM " );
        returnQuery.append ( "employee " );
        returnQuery.append ( "WHERE " );
        returnQuery.append ( "employee.branch_id = ? ;" );       
        
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
