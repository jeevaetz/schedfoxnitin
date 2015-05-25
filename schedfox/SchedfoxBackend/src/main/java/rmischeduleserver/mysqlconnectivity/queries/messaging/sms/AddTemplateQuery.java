/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.messaging.sms;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class AddTemplateQuery extends GeneralQueryFormat {

    StringBuilder queryString;
    String name, template;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public AddTemplateQuery(String name, String template) {
        this.name = name;
        this.template = template;
    }

    public String toString() {
        queryString = new StringBuilder();
        this.queryString.append("INSERT INTO message_sms_templates (message_sms_name,message_sms_text) ");
        this.queryString.append("VALUES ('" + this.name + "','" + this.template + "');");
        return this.queryString.toString();
    }
}
