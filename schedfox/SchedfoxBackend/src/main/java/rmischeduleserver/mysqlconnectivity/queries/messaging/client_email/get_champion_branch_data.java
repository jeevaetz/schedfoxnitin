//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.messaging.client_email;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  A simple query object designed to retrieve branch data from
 *      {@code control_db.branch} via {@code branch.branch_management_id}.
 *      This class extends {@code GeneralQueryFormat}
 *  <p><b>NOTE: </b> this query object uses <i>Prepared Statements</i>
 *  <p><b>NOTE: </b> this query only seeks Champion National Security Branches,
 *      and the {@code branch.branch_management_id} has been defined as <b>1</b>
 *  @author Jeffrey N. Davis
 *  @since 03/24/2011
 *  @see rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat
 */
public final class get_champion_branch_data extends GeneralQueryFormat
{
    /*  private variable declarations   */
    private StringBuilder sqlQuery;
    private static final int CHAMPION_NATIONAL_SECURITY_BRANCH_MANAGEMENT_ID = 1;

    /*  private method implementations  */
    private StringBuilder constructPreparedStatement()
    {
        StringBuilder returnStringBuilder = new StringBuilder();

        returnStringBuilder.append("SELECT branch_id, branch_name ");
        returnStringBuilder.append("FROM branch ");
        returnStringBuilder.append("WHERE branch_management_id = ? ;");

        return returnStringBuilder;
    }

    /*  public method implementations   *?
    /**
     *  Standard creation of this object.
     */
    public get_champion_branch_data()
    {
        /*  Initialize class variables  */
        this.sqlQuery = new StringBuilder();

        /*  set prepared statement  */
        super.setPreparedStatement( new Object[] {
            get_champion_branch_data.CHAMPION_NATIONAL_SECURITY_BRANCH_MANAGEMENT_ID
        });

        /*  construct query */
        this.sqlQuery = this.constructPreparedStatement();
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