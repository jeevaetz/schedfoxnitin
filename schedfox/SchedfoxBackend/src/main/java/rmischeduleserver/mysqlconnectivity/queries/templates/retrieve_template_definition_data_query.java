//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.templates;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  A simple query object designed to retrieve template definitions from
 *      {@code control_db.template_definitions} via template_id.  This class extends {@code GeneralQueryFormat}
 *  <p><b>NOTE:  </b> this query object uses <i>Prepared Statements</i>
 *  @author Jeffrey N. Davis
 *  @since 03/07/2011
 */
public class retrieve_template_definition_data_query extends GeneralQueryFormat
{
    /*  private variable declarations   */
    private StringBuilder sqlQuery;

    /**
     *  Default construction of this object not allowed
     *  @throws UnsupportedOperationException
     */
    private retrieve_template_definition_data_query()
    {
        throw new UnsupportedOperationException("Default construction of this object not allowed.");
    }

    /**
     *  Create instance of this object
     */
    public retrieve_template_definition_data_query( int template_id )
    {
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
        sqlQuery.append( "SELECT * " );
        sqlQuery.append( "FROM template_definitions " );
        sqlQuery.append( "WHERE template_definitions.template_id = ? ;");

        return this.sqlQuery.toString();
    }

    @Override
    public boolean hasPreparedStatement()   { return true;}

    /** Describes whether this object has database access
     *  <p><b>Default:  </b><i>TRUE</i>
     *  @return hasAccess a boolean describing if this object has access to database
     */
    @Override
    public boolean hasAccess() {    return true; }
};
