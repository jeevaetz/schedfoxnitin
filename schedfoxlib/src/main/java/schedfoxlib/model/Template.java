/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class Template {
    private Integer templateId;
    private Integer templateType;
    private Integer companyId;
    private Integer branchId;
    private String templateValue;
    private String templateName;
    
    public Template() {
        
    }
    
    public Template(Record_Set rst) {
        try {
            this.templateId = rst.getInt("template_id");
        } catch (Exception exe) {}
        try {
            this.templateType = rst.getInt("template_type");
        } catch (Exception exe) {}
        try {
            this.companyId = rst.getInt("company_id");
        } catch (Exception exe) {}
        try {
            this.branchId = rst.getInt("branch_id");
        } catch (Exception exe) {}
        try {
            this.templateValue = rst.getString("template_value");
        } catch (Exception exe) {}
        try {
            this.templateName = rst.getString("template_name");
        } catch (Exception exe) {}
    }

    /**
     * @return the templateId
     */
    public Integer getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId the templateId to set
     */
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the templateType
     */
    public Integer getTemplateType() {
        return templateType;
    }

    /**
     * @param templateType the templateType to set
     */
    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the templateValue
     */
    public String getTemplateValue() {
        return templateValue;
    }

    /**
     * @param templateValue the templateValue to set
     */
    public void setTemplateValue(String templateValue) {
        this.templateValue = templateValue;
    }

    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    @Override
    public String toString() {
        return this.templateName;
    }
}
