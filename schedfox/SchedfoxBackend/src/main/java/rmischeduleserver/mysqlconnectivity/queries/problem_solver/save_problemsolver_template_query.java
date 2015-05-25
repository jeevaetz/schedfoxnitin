//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.problem_solver;

//  import declarations
import schedfoxlib.model.ProblemsolverTemplate;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;


/**
 *  A query object to save new Corporate Communicator Templates
 *
 *  <p>This class extends <code>GeneralQueryFormat</code> and is designed
 *      to contain a query that saves a new template for Corporate Communicator.
 *      This class contains a <i>PREPARED STATEMENT</i>.
 *  @author Jeffrey N. Davis
 *  @see rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat
 *  @since 02/03/2011
 */
public class save_problemsolver_template_query extends GeneralQueryFormat
{
    //  class variables
    private StringBuilder sqlQuery;
    private final ProblemsolverTemplate templateToSave;
    private final boolean isNew;
    
    /**
     *  Checks to ensure the argument <code>templateToSave</code> is fully
     *      valid and ready to save to database
     *  @param template an object to check
     *  @return isValid a boolean describing if the object is valid for save
     */
    private boolean isArgValid ( ProblemsolverTemplate template )
    {
        if ( !(template instanceof ProblemsolverTemplate) )
            return false;
        if ( template.getProblemsolverName().length() == 0)
            return false;
        if ( template.getProblemSolverType() == 0 )
            return false;
        if ( template.getProblemsolverValue().length() == 0 )
            return false;
        
        return true;
    }

    /**
     *  Default construction of this object not allowed
     *  @throws UnsupportedOperationException
     */
    private save_problemsolver_template_query()
    {
        throw new UnsupportedOperationException("Default construction of this object not allowed.");
    }

    /**
     *  Constructs an instance of this object.
     *
     *  <p>Constructs an instance of this query by setting <code>GeneralQueryFormat.setPreparedStatement</code>.
     *  @param templateToSave an instance of ProblemsolverTemplate to save
     *  @param isNew a boolean describing if this is a new template
     *  @throws IllegalArgumentException
     */
    public save_problemsolver_template_query ( ProblemsolverTemplate templateToSave, boolean isNew )
    {
        //  initialize class variables
        if ( !this.isArgValid( templateToSave ))
            throw new IllegalArgumentException("Invalid template to save.");
        else
            this.templateToSave = templateToSave;
        this.sqlQuery = new StringBuilder();
        this.isNew = isNew;

        //  set prepared statement
        if ( this.isNew )
        {
            super.setPreparedStatement(new Object[] {
                this.templateToSave.getProblemsolverName(),
                this.templateToSave.getProblemsolverValue(),
                this.templateToSave.getProblemSolverType()
            });
        }
        else
        {
            super.setPreparedStatement(new Object[] {
                this.templateToSave.getProblemsolverName(),
                this.templateToSave.getProblemsolverValue(),
                this.templateToSave.getProblemsolverTemplateId()
            });
        }
    }

    /**
     *  Constructs and returns the prepared statement.
     *
     *  <p>This method constructs and returns the prepared statement depending
     *      on whether the <code>Problemsolver Template</code> is new.
     *  @return sqlQuery a string containing the prepared statement
     */
    @Override
    public String getPreparedStatementString()
    {
        if ( this.isNew )
        {
            this.sqlQuery.append("INSERT INTO problemsolver_template ");
            this.sqlQuery.append("(problemsolver_name, problemsolver_value, problem_solver_type) ");
            this.sqlQuery.append("VALUES ");
            this.sqlQuery.append("(?, ?, ?)");
            this.sqlQuery.append(";");
        }
        else
        {
            this.sqlQuery.append("UPDATE problemsolver_template ");
            this.sqlQuery.append("SET ");
            this.sqlQuery.append("problemsolver_name = ? ,");
            this.sqlQuery.append("problemsolver_value = ? ");
            this.sqlQuery.append("WHERE ");
            this.sqlQuery.append("problemsolver_template_id = ? ");
            this.sqlQuery.append(";");
        }
        
        return sqlQuery.toString();
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
