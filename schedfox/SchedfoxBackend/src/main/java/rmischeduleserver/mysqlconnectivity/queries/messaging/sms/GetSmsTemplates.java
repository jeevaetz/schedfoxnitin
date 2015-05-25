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
public class GetSmsTemplates extends GeneralQueryFormat{

    private static String  query = "SELECT * FROM message_sms_templates";

    public String toString(){
        return this.query;
    }

    @Override
    public boolean hasAccess() {
       return true;
    }
}
