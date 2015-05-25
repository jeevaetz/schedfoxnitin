//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.problem_solver;

//  import declarations

import schedfoxlib.model.ProblemsolverTemplate;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  A query object to delete a template used in Corporate Communicator
 *
 *  <p>This class extends <code>GeneralQueryFormat</code> and is designed to
 *      contain a query that <i>deletes</i> a template saved in the databause;
 *      this template is used by Corporate Communicator.
 *  <p><b>NOTE:  </b>this query utilizes a <i>prepared statement</i>.
 *  @author Jeffrey N. Davis
 *  @see rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat
 *  @since 02/05/2011
 */
public class delete_problemsolver_template_query extends GeneralQueryFormat
{
    //  class variables
    private final ProblemsolverTemplate templateToDelete;
    private final StringBuilder sqlQuery;
    
    //  private method implementations
    /**
     *  Checks to ensure the argument <code>templateToDelete</code> is fully
     *      valid and ready to delete from the database
     *  @param template an object to check
     *  @return isValid a boolean describing if the object is valid for deletion
     */
    private final boolean isArgValid ( ProblemsolverTemplate template)
    {
        if ( !( template instanceof ProblemsolverTemplate ))
            return false;
        if ( template.getProblemsolverTemplateId() < 1 )
            return false;
        
        return true;
    }

    /**
     *  Default construction of this object not allowed
     *  @throws UnsupportedOperationException
     */
    private delete_problemsolver_template_query()
    {
        throw new UnsupportedOperationException("Default construction of this object not allowed.");
    }

    /**
     *  Constructs a proper instance of this object
     *
     *  <p>Constructs an instance of this object by setting <code>GeneralQueryFormat.setPreparedStatement</code>.
     *  @param templateToDelete an instance of the <code>ProblemsolverTemplate</code> to delete
     *  @throws IllegalArgumentException is the instance of <code>ProblemsolverTemplate</code>
     *      isn't consistent enough for deletion operation
     */
    public delete_problemsolver_template_query ( ProblemsolverTemplate templateToDelete )
    {
        if ( !this.isArgValid( templateToDelete ) )
            throw new IllegalArgumentException("The instance of ProblemsolverTemplate passed to this object isn't valid for a save.");
        this.templateToDelete = templateToDelete;
        this.sqlQuery = new StringBuilder();

        //  set prepared statement
        super.setPreparedStatement( new Object[] {
            this.templateToDelete.getProblemsolverTemplateId()
        });
    }

    /**
     *  Constructs and returns the prepared statement.
     *  @return sqlQuery a string containing the prepared statement
     */
    @Override
    public String getPreparedStatementString()
    {
        this.sqlQuery.append("DELETE FROM problemsolver_template ");
        this.sqlQuery.append("WHERE problemsolver_template_id = ? ");
        this.sqlQuery.append(";");
        
        return this.sqlQuery.toString();
    }
    
    @Override
    public boolean hasPreparedStatement()   { return true;}

    /** Describes whether this object has database access
     *  <p><b>Default:  </b><i>TRUE</i>
     *  @return hasAccess a boolean describing if this object has access to database
     */
    @Override
    public boolean hasAccess()  { return true; }
};
