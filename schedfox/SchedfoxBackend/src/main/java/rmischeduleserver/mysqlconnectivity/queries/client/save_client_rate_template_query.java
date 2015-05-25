/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import schedfoxlib.model.ClientRateTemplates;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_client_rate_template_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(ClientRateTemplates template) {
        if (template.getClientRateTemplateId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{template.getTemplateName(),
                template.getTemplateText(), template.getActive()});
        } else {
            isInsert = false;
            super.setPreparedStatement(new Object[]{template.getTemplateName(),
                template.getTemplateText(), template.getActive(),
                template.getClientRateTemplateId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO client_rate_templates ");
            sql.append("(template_name, template_text, active)");
            sql.append("VALUES ");
            sql.append("(?, ?, ?)");
        } else {
            sql.append("UPDATE client_rate_templates ");
            sql.append("SET ");
            sql.append("template_name = ?, template_text = ?, active = ? ");
            sql.append("WHERE ");
            sql.append("client_rate_template_id = ?");
        }
        return sql.toString();
    }

}
