/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ClientRateTemplates implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer clientRateTemplateId;
    private String templateName;
    private String templateText;
    private Boolean active;

    public ClientRateTemplates() {
    }

    public ClientRateTemplates(Record_Set rst) {
        try {
            this.clientRateTemplateId = rst.getInt("client_rate_template_id");
        } catch (Exception e) {}
        try {
            this.templateName = rst.getString("template_name");
        } catch (Exception e) {}
        try {
            this.templateText = rst.getString("template_text");
        } catch (Exception e) {}
        try {
            this.active = rst.getBoolean("active");
        } catch (Exception e) {}
    }

    public Integer getClientRateTemplateId() {
        return clientRateTemplateId;
    }

    public void setClientRateTemplateId(Integer clientRateTemplateId) {
        this.clientRateTemplateId = clientRateTemplateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateText() {
        return templateText;
    }

    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientRateTemplateId != null ? clientRateTemplateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientRateTemplates)) {
            return false;
        }
        ClientRateTemplates other = (ClientRateTemplates) object;
        if ((this.clientRateTemplateId == null && other.clientRateTemplateId != null) || (this.clientRateTemplateId != null && !this.clientRateTemplateId.equals(other.clientRateTemplateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getTemplateName();
    }

}
