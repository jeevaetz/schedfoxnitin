//  package declaration
package rmischeduleserver.mysqlconnectivity.queries.templates;

//  import declarations
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *  A simple query object designed to save templates to {@code <schema>.templates}.
 *      This class extends {@code GeneralQueryFormat}
 *  <p><b>NOTE:  </b> this query object uses <i>Prepared Statements</i>
 *  @author Jeffrey N. Davis
 *  @see rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat
 *  @since 03/07/2011
 */
public final class save_template_query extends GeneralQueryFormat
{
    /*  private variable declarations   */
    private StringBuilder sqlQuery;
    private final boolean isNew;
    private final int templateId;
    private final int templateType;
    private final int companyId;
    private final int branchId;
    private final String templateValue;
    private final String templateName;

    /*  Object construction */
    /**
     *  Default construction of this object not allowed
     *  @throws UnsupportedOperationException
     */
    private save_template_query()
    {
        throw new UnsupportedOperationException("Default construction of this object not allowed.");
    }

    /**
     *  This class checks the incoming arguments from the Builder to ensure a properly
     *      constructed immutable object.
     *  <p>Checks to see the following:  {@code template_type, company_id, branch_id
     *      template_value, template_name} are all valid.
     *  @param builder an instance of Builder used to create this object
     *  @return isValid
     */
    private boolean areArgsValid( Builder builder )
    {
        if( builder.isNew && ( builder.template_id != 0 ))
            return false;
        if( !builder.isNew && ( builder.template_id == 0 ))
            return false;
        if( builder.template_type == 0 )
            return false;
        if( builder.company_id == 0 )
            return false;
        if( builder.branch_id == 0 )
            return false;
        if( builder.template_value.length() == 0 )
            return false;
        if( builder.template_name.length() == 0 )
            return false;
        
        return true;
    }

    /**
     *  A static Builder object for {@code save_template_query}
     *
     *  <p>This class is a simple static Builder object; due to the large number
     *      of parameters, this allows for a far cleaner construction.
     */
    public static class Builder
    {
        //  set params
        private int template_id = 0;
        private int template_type = 0;
        private int company_id = 0;
        private int branch_id = 0;
        private String template_value = "";
        private String template_name = "";
        private boolean isNew = true;

        public Builder()    {}

        public Builder template_id( int val )
            { this.template_id = val; return this; }
        public Builder template_type( int val )
            { this.template_type = val; return this; }
        public Builder company_id( int val )
            { this.company_id = val; return this; }
        public Builder branch_id( int val )
            { this.branch_id = val; return this; }
        public Builder template_value( String val )
            { this.template_value = val; return this; }
        public Builder template_name( String val )
            { this.template_name = val; return this; }
        public Builder isNew( boolean val )
            { this.isNew = val; return this; }

        public save_template_query Build()
            { return new save_template_query( this ); }
    }

    /**
     *  Initializes this object.
     *  @param Builder an instance of the static class Builder used to
     *      construct this object
     *  @throws IllegalArgumentException if the arguments are not valid
     */
    private save_template_query( Builder builder )
    {
        if( this.areArgsValid( builder ) )
        {
            /*  set params  */
            this.sqlQuery = new StringBuilder();
            this.isNew = builder.isNew;
            this.templateId = builder.template_id;
            this.templateType = builder.template_type;
            this.companyId = builder.company_id;
            this.branchId = builder.branch_id;
            this.templateValue = builder.template_value;
            this.templateName = builder.template_name;

            /*  set prepared statement  */
            if( this.isNew )
            {
                super.setPreparedStatement( new Object[] {
                    this.templateType,
                    this.companyId,
                    this.branchId,
                    this.templateValue,
                    this.templateName
                });
            }
            else
            {
                super.setPreparedStatement( new Object[] {
                    this.templateValue,
                    this.templateName,
                    this.templateId
                });
            }
        }
        else
            throw new IllegalArgumentException("Arguments passed to this object are invalid.");
    }

    /**
     *  Constructs and returns the prepared statement.
     *
     *  <p>This method constructs and returns the prepared statement depending
     *      on {@code this.isNew}
     *  @return sqlQuery a string containing the prepared statement
     */
    @Override
    public String getPreparedStatementString()
    {
        if( this.isNew )
        {
            this.sqlQuery.append( "INSERT INTO templates ");
            this.sqlQuery.append( "VALUES (" );
            this.sqlQuery.append( "nextval('template_id_seq'), " );
            this.sqlQuery.append( "?, ?, ?, ?, ? " );
            this.sqlQuery.append( ");" );
        }
        else
        {
            this.sqlQuery.append( "UPDATE templates " );
            this.sqlQuery.append( "SET ");
            this.sqlQuery.append( "template_value = ?, " );
            this.sqlQuery.append( "template_name = ? " );
            this.sqlQuery.append( "WHERE template_id = ? " );
            this.sqlQuery.append( ";" );
        }
        
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

    @Override
    public String toString()    { return this.getPreparedStatementString(); }
};
