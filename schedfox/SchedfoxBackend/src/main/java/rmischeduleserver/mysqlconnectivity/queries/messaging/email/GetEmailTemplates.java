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
public class GetEmailTemplates extends GeneralQueryFormat{

    private String queryString = "Select * from message_email_templates";

    public String toString() {
        return this.queryString;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
}
