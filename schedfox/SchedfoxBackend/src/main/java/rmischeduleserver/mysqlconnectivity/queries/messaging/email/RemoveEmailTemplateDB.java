/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.messaging.email;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class RemoveEmailTemplateDB extends GeneralQueryFormat{

    private String templateId;
    private String queryString = "DELETE FROM message_email_templates WHERE message_email_template_id = ";
    @Override
    public boolean hasAccess() {
        return true;
    }
    public RemoveEmailTemplateDB(String templateId){
        this.templateId = templateId;
    }
    public String toString(){
        return this.queryString + this.templateId + ";";
    }

}
