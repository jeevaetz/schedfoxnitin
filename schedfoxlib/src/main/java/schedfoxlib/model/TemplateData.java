//  package declaration
package schedfoxlib.model;

//  import declarations
import schedfoxlib.model.util.Record_Set;

/**
 * This class is designed to represent the data contained within the database at
 * the schema level. The specific table is <b>templates</b>. <p>Due to the
 * number of parameters involved with creation of this object, a static
 * {@code Builder} object has been included to assist in proper construction.
 *
 *
 * @author Jeffrey N. Davis
 * @since 03/04/2011
 */
public final class TemplateData implements Comparable<TemplateData> {
    //  class variable declarations

    private int templateId;
    private int templateType;
    private int companyId;
    private int branchId;
    private String templateName;
    private String templateValue;

    public TemplateData() {
        
    }
    
    public TemplateData(Record_Set rs) {
        try {
            templateId = rs.getInt("template_id");
        } catch (Exception exe) {
        }
        try {
            templateType = rs.getInt("template_type");
        } catch (Exception exe) {
        }
        try {
            companyId = rs.getInt("company_id");
        } catch (Exception exe) {
        }
        try {
            branchId = rs.getInt("branch_id");
        } catch (Exception exe) {
        }
        try {
            templateValue = rs.getString("template_value");
            templateValue = templateValue.replaceAll("“", "\"");
            templateValue = templateValue.replaceAll("’", "\"");
        } catch (Exception exe) {
        }
        try {
            templateName = rs.getString("template_name");
        } catch (Exception exe) {
        }
    }

    /*  mutators    */
    public int getBranchId() {
        return this.branchId;
    }
    
    public int getCompanyId() {
        return this.companyId;
    }

    public int getTemplateId() {
        return this.templateId;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public int getTemplateType() {
        return this.templateType;
    }

    public String getTemplateValue() {
        return this.templateValue;
    }

    public int compareTo(TemplateData argData) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.templateName, argData.getTemplateName());
    }

    @Override
    public String toString() {
        return this.templateName;
    }

    @Override
    public boolean equals(Object obj) {
        /*  Method checks id -> type -> name -> value.  Duplicate entries will
         *      NOT be allowed.
         */
        if (!(obj instanceof TemplateData)) {
            return false;
        }

        final TemplateData other = (TemplateData) obj;

        return (this.templateId == other.templateId //    test templateID
                && this.templateType == other.templateType // test template type
                && (this.templateName == null ? other.templateName == null : this.templateName.equals(other.templateName)) // test template name
                && (this.templateValue == null ? other.templateValue == null : this.templateValue.equals(other.templateValue)) // test template value
                );
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.templateId;
        hash = 71 * hash + this.templateType;
        hash = 71 * hash + (this.templateName != null ? this.templateName.hashCode() : 0);
        hash = 71 * hash + (this.templateValue != null ? this.templateValue.hashCode() : 0);

        return hash;
    }

    /**
     * @param templateId the templateId to set
     */
    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    /**
     * @param templateType the templateType to set
     */
    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    /**
     * @param templateName the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @param templateValue the templateValue to set
     */
    public void setTemplateValue(String templateValue) {
        this.templateValue = templateValue;
    }
};
