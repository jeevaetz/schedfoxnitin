//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.client;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  A query object to delete a Problem Solver
 *
 *  <p>This class extends <code>GeneralQueryFormat</code> and is designed to
 *      contain a query that deletes a Problem Solver.  This class contains
 *      a <i>PREPARED STATEMENT</i>.
 *  <p><b>NOTE:  </b>this query was redesigned by the current author as its
 *      previous implementation did not work.
 *  <p><b>Previous Date Written:  </b> <i>August 15, 2006</i>
 *  <p><b>Previous Author:  </b> <i>Shawn</i>
 *  @author Jeffrey N. Davis
 *  @see rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat
 *  @since 02/10/2011
 */
public class delete_problem_solver_query extends GeneralQueryFormat
{
    //  class variables
    private StringBuilder sqlQuery;
    private final int psId;

    /**
     *  Default construction of this object not allowed
     *  @throws UnsupportedOperationexception
     */
    private delete_problem_solver_query()
    {
        throw new UnsupportedOperationException("Default construction of this object not allowed.");
    }

    //  public method implementations
    /**
     *  Constructs an instance of this object
     *
     *  <p>Constructs an instance of this query by setting
     *      <code>GeneralQueryFormat.setPreparedStatement</code>.
     *  @param psId an int describing the value of ps_id
     *  @throws IllegalArgumentException
     */
    public delete_problem_solver_query ( int psId )
    {
        //  check argument, assing class variables
        if ( !( psId > 0) )
            throw new IllegalArgumentException("Invalid ps_id.");
        else
            this.psId = psId;
        this.sqlQuery = new StringBuilder();
        
        //  set prepared statement
        super.setPreparedStatement( new Object[] {
            this.psId
        });
    }

    /**
     *  Constructs and returns the prepared statement.
     *  @return sqlQuery a string containing the prepared statement
     */
    @Override
    public String getPreparedStatementString()
    {
        this.sqlQuery.append("DELETE FROM problemsolver ");
        this.sqlQuery.append("WHERE ps_id = ? ");
        this.sqlQuery.append(";");

        return this.sqlQuery.toString();
    }

    @Override
    public boolean hasPreparedStatement()   {return true;}

    /**
     *  Describes whether this object has database access.
     *  <p><b>Default:  </b><i>TRUE</i>
     *  @return hasAccess a boolean describing if this object has access to the
     *      database
     */
    @Override
    public boolean hasAccess()  {return true;}
};
