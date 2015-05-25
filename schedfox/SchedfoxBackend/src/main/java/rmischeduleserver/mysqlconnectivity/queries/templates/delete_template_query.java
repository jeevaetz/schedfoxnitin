//  package declarations
package rmischeduleserver.mysqlconnectivity.queries.templates;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  A simple query object designed to delete templates from {@code <schema>.templates}
 *      via {@code template_id}.  This class extends {@code GeneralQueryFormat}
 *  <p><b>NOTE:  </b> this query object uses <i>Prepared Statements</i>
 *  @author Jeffrey N. Davis
 *  @since 03/07/2011
 */
public class delete_template_query extends GeneralQueryFormat
{
    /*  private variable declarations   */
    private StringBuilder sqlQuery;

    /**
     *  Default construction of this object not allowed
     *  @throws UnsupportedOperationException
     */
    private delete_template_query()
    {
        throw new UnsupportedOperationException("Default construction of this object not allowed.");
    }

    public delete_template_query ( int template_id )
    {
        /*  ensure valid argument   */
        if ( template_id < 1 )
            throw new IllegalArgumentException( "Invalid template_id passed as argument." );

        /*  Initialize class variables  */
        this.sqlQuery = new StringBuilder();

        /*  set prepared statement  */
        super.setPreparedStatement( new Object[] {
            template_id
        });
    }

    /**
     *  Constructs and returns the prepared statement.
     *  @return sqlQuery a string containing the prepared statement
     */
    @Override
    public String getPreparedStatementString()
    {
        sqlQuery.append( "DELETE FROM templates " );
        sqlQuery.append( "WHERE templates.template_id = ? ;");

        return this.sqlQuery.toString();
    }

    @Override
    public boolean hasPreparedStatement()   { return true;}

    /** Describes whether this object has database access
     *  <p><b>Default:  </b><i>TRUE</i>
     *  @return hasAccess a boolean describing if this object has access to database
     */
    @Override
    public boolean hasAccess() { return true; }
};
