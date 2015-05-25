//  package declaration
package rmischedule.templates.models;

import rmischedule.templates.controllers.TemplateController;

/**
 *  This is a simple data object designed to hold components needed to initialize
 *      the template system.  It utilizes a {@code Builder.build()} object so
 *      that optional parameters can be passed in based upon the point of entry.
 *  <p><b>Note:  </b>this class is immutable upon building.
 *  @author Jeffrey N. Davis
 *  @since 02/28/2011
 */
public final class InitializeTemplateSystem
{
    //  variable definitions
    private final String companyId;
    private final String branchId;
    private final int templateType;
    private final String incomingText;
    private final TemplateController controller;

    /**
     *  A static Builder object for {@code InitializeTemplateSystem}
     *
     *  <p>This class is a simple static Builder object, designed so that
     *      other objects declaring an instance of {@code InitializeTemplateSystem}
     *      can <i>telescope</i> parameters depending class calling the template system.
     *
     *  @author Jeffrey N. Davis
     *  @since 02/28/2011
     */
    public static class Builder
    {
        //  required parameters
        private final String companyId;
        private final String branchId;
        private final int templateType;
        private final TemplateController controller;

        //  optional parameters - initialized to default values
        private String incomingText = "";

        public Builder( String companyId, String branchId, int templateType, TemplateController controller)
        {
            this.companyId = companyId;
            this.branchId = branchId;
            this.templateType = templateType;
            this.controller = controller;
        }

        public Builder incomingText ( String val )
            { this.incomingText = val;  return this; }

        public InitializeTemplateSystem build ()
            { return new InitializeTemplateSystem( this ); }

    }

    private InitializeTemplateSystem( Builder builder )
    {
        //  initialize class paramters
        this.companyId = builder.companyId;
        this.branchId = builder.branchId;
        this.templateType = builder.templateType;
        this.incomingText = builder.incomingText;
        this.controller = builder.controller;
    }

    public String getBranchId() { return branchId; }

    public String getCompanyId() { return companyId; }

    public int getTemplateType() { return templateType; }

    public String getIncomingText() { return incomingText; }

    public TemplateController getParentTemplateController()   { return controller; }

    @Override
    public String toString()
    {
        return "InitializeTemplateSystem{" + "companyId=" + companyId + "branchId=" + branchId + "incomingText=" + incomingText + '}';
    }
};