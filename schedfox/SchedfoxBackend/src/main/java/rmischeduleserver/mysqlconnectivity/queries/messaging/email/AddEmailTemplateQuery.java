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
public class AddEmailTemplateQuery extends GeneralQueryFormat {

    private String queryString;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public AddEmailTemplateQuery(String name, String subject, String msg) {
        this.queryString = "INSERT INTO message_email_templates (message_email_name,message_email_subject,message_email_text) "
                + "VALUES ('" + name + "','" + subject + "','" + msg + "')";
    }
    public String toString(){
        return this.queryString;
    }
}
