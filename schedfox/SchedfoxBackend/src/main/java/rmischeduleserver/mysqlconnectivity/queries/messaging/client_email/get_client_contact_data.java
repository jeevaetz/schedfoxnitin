//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.messaging.client_email;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  A simple query object designed to retrieve contact information per client
 *      from {@code client_contact} via {@code client_contact.client_id}.
 *      This class extends GeneralQueryFormat.
 *  <p><b>NOTE: </b> this query object uses <i>Prepared Statements</i>
 *  @author Jeffrey N. Davis
 *  @since 03/24/2011
 *  @see rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat
 */
public final class get_client_contact_data extends GeneralQueryFormat
{
    /*  private variable declarations   */
    private StringBuilder sqlQuery;
    private int clientId;

    /*  private method implementations  */
    private StringBuilder constructPreparedStatement()
    {
        StringBuilder returnStringBuilder = new StringBuilder();

        returnStringBuilder.append("SELECT ");
        returnStringBuilder.append("client_contact_id AS clientcontactid, ");
        returnStringBuilder.append("client_id AS clientid, ");
        returnStringBuilder.append("client_contact_is_primary AS isprimary, ");
        returnStringBuilder.append("client_contact_title AS clientcontacttitle, ");
        returnStringBuilder.append("client_contact_first_name AS clientcontactfirstname, ");
        returnStringBuilder.append("client_contact_last_name AS clientcontactlastname, ");
        returnStringBuilder.append("client_contact_phone AS clientcontactphone, ");
        returnStringBuilder.append("client_contact_cell AS clientcontactcell, ");
        returnStringBuilder.append("client_contact_email AS clientcontactemail ");
        returnStringBuilder.append("FROM client_contact ");
        returnStringBuilder.append("WHERE ");
        returnStringBuilder.append("client_contact_is_deleted = 0 ");
        returnStringBuilder.append("AND ");
        returnStringBuilder.append("client_id = ? ;");

        return returnStringBuilder;
    }

    /*  object construction code    */
    /**
     *  Default construction of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    private get_client_contact_data()
        { throw new UnsupportedOperationException( "Default construction of this object is not allowed." ); }

    public get_client_contact_data ( int client_id )
    {
        /*  assign class variables  */
        this.clientId = client_id;

        /*  set prepared statement  */
        super.setPreparedStatement( new Object[] {
            this.clientId
        });

        /*  construct query */
        this.sqlQuery = this.constructPreparedStatement();
    }

    /*  public method implemenations    */
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
