//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.templates;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 * A simple query object designed to retrieve templates from
 * {@code <schema>.templates} via templateId. This class extends
 * {@code GeneralQueryFormat}
 * <p>
 * <b>NOTE: </b> this query object uses <i>Prepared Statements</i>
 *
 * @author Jeffrey N. Davis
 * @since 03/07/2011
 */
public class retrieve_templates_by_id_query extends GeneralQueryFormat {
    /*  private variable declarations   */

    private StringBuilder sqlQuery;


    public retrieve_templates_by_id_query(int templateType) {
        /*  Initialize class variables  */
        this.sqlQuery = new StringBuilder();

        /*  set prepared statement  */
        super.setPreparedStatement(new Object[]{
            templateType
        });
    }

    /**
     * Constructs and returns the prepared statement.
     *
     * @return sqlQuery a string containing the prepared statement
     */
    @Override
    public String getPreparedStatementString() {
        sqlQuery.append("SELECT * ");
        sqlQuery.append("FROM templates ");
        sqlQuery.append("WHERE templates.template_type = ? ");
        sqlQuery.append("ORDER BY ");
        sqlQuery.append("template_name ");

        return this.sqlQuery.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    /**
     * Describes whether this object has database access
     * <p>
     * <b>Default: </b><i>TRUE</i>
     *
     * @return hasAccess a boolean describing if this object has access to
     * database
     */
    @Override
    public boolean hasAccess() {
        return true;
    }
};
