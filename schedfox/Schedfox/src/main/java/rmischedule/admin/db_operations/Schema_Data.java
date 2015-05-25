/**
 *  FileName:  Schema_Data
 *  Date Created:  11/15/2010
 *  @author Jeffrey Davis
 *  Purpose of File:  file contains a data object class for schema operations
 */

//  package declaration
package rmischedule.admin.db_operations;

//  import declarations

/**
 *  Class Name: Schema_Data
 *  Purpose of Class:  a data object class for schema operations
 */
public class Schema_Data implements Comparable<Schema_Data>
{
    //  private variable declarations
    private String schemaName;
    private String dateLastUsed;
    private String fileName;
    private String company;
    private boolean active;
    
    //  private method implementation
    /**
     *  Method Name:  determineIsActive
     *  Purpose of Method:  checks to see if a stored schema is also active on
     *      the database
     *  @return isActive -  a boolean describing whether the stored schema is
     *      active on the database
     */
    private boolean determineIsActive()
    {
        if(schemaName.length() != 0 && fileName.length() != 0)
            return true;
        else
            return false;
    }

   /**
     *  Class Name:  Builder
     *  Purpose of Class:  a public class to set paramaters emulating named
     *      parameters
     */
    public static class Builder
    {
        //  private variable declarations
        private String schemaName = "";
        private String dateLastUsed = "";
        private String fileName = "";
        private String companyName = "";
        private boolean isActive = false;
        
        /** Create a default instance of this class */
        public Builder()    {}
        /** Builder setting methods */
        /**
         *  @param  schemaName - a string representing the schema name
         *  @return this instance
         */
        public Builder schema(String schemaName)
            {this.schemaName = schemaName;  return this;}
        /**
         *  @param dateLastUsed
         *  @return this instance
         */
        public Builder date(String dateLastUsed)
            {this.dateLastUsed = dateLastUsed;  return this;}
        /**
         *  @param fileName
         *  @return this instance
         */
        public Builder file(String fileName)
            {this.fileName = fileName;  return this;}
        /**
         *  @param company
         *  @return this instance
         */
        public Builder company(String companyName)
            {this.companyName = companyName;    return this;}
        /**
         *  @param isActive
         *  @return this instance
         */
        public Builder isActive(boolean isActive)
            {this.isActive = isActive;  return this;}

        /**
         *  @return this instance, variables set, to construct new Schema_Data
         */
        public Schema_Data build()  {return new Schema_Data(this);}
    }

    //  public method implementations
    /**  Creates an instance of this class, chains to param constructor */
    public Schema_Data()  {}

    /** Creates instance of this class, using Builder class */
    public Schema_Data(Builder builder)
    {
        this.schemaName = builder.schemaName;
        this.dateLastUsed = builder.dateLastUsed;
        this.fileName = builder.fileName;
        this.company = builder.companyName;
        this.active = builder.isActive;
    }



    /**
     *  Method Name:  Schema_Data
     *  Purpose of Method:  Creates an instance of this class
     *  @param schemaName - a string representing the name of the schema
     *  @param dateLastUsed - a string representing the date the schema was last used
     *  @param fileName - a string representing the fileName for a stored schema
     *  @param company
    */
    public Schema_Data ( String schemaName, String dateLastUsed, String fileName )
    {
        try
        {
            this.schemaName = schemaName;
        }
        catch ( IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
        try
        {
            this.dateLastUsed = dateLastUsed;
        }
        catch ( IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
        try
        {
            this.fileName = fileName;
        }
        catch( IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }

        active = determineIsActive();
    }

    //  getters / setters
    /**
     *  Method Name:  getSchemaName
     *  Purpose of Method:  returns the schema name in this data object
     *  @returns schemaName -  a string representing this schema
     */
    public String getSchemaName()    {return this.schemaName;}
    /**
     *  Method Name:  getDateLastUsed
     *  Purpose of Method:  returns the date last used for the schema in this
     *      data object
     *  @returns dataLastUsed - a string representing the date the schema was
     *      used
     */
    public String getDateLastUsed()  {return this.dateLastUsed;}
    /**
     *  Method Name:  getFileName
     *  Purpose of method:  returns the file name for a stored schema
     *  @returns fileName - a string representing the file name if active
     */
    public String getFileName()      {return this.fileName;}
    /**
     *  Method Name:  isActive
     *  Purpose of Method:  returns a boolean describing if a stored schema
     *      is active on the database
     *  @returns isActive - a boolean describing if the stored schema is active
     *      on the database
     */
    public boolean isActive()        {return this.active;}
    /**
     *  Method Name:  getCompany
     *  @return company - a string representing the company
     */
    public String getCompany()       {return this.company;}

    /**
     *  Method Name:  setSchemaName
     *  Purpose of Method:  sets the schema name in this data object
     *  @param schemaName - a string representing the schema name
     */
    public void setSchemaName ( String schemaName )
    {
        try
        {
            this.schemaName = schemaName;
        }
        catch( IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     *  Method Name:  setDateLastUsed
     *  Purpose of Method:  sets the date last used in this data object
     *  @param dateLastUsed - a string representing the date the schema was last used
     */
    public void setDateLastUsed ( String dateLastUsed )
    {
        try
        {
            this.dateLastUsed = dateLastUsed;
        }
        catch( IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     *  Method Name:  setFileName
     *  Purpose of Method:  sets the file name of a stored schema
     *  @param fileName - a string representing the file name of a stored schema
     */
    public void setFileName ( String fileName)
    {
        try
        {
            this.fileName = fileName;
        }
        catch( IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     *  Method Name:  setIsActive
     *  Purpose of Method:  sets whether a stored schema is active on the
     *      database
     */
    public void setIsActive()
    {
        this.active = this.determineIsActive();
    }

    /** Method Name:  toString*/
    @Override
    public String toString() {return this.schemaName;}

    public int compareTo(Schema_Data argData)
    {
        return String.CASE_INSENSITIVE_ORDER.compare(this.schemaName, argData.getSchemaName());
    }
};
