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
public class RemoveTemplateFromDB extends GeneralQueryFormat{
    private StringBuilder query = new StringBuilder();

    private RemoveTemplateFromDB(){}
    public RemoveTemplateFromDB(String getTemplate_db_id){
        this.query.append("DELETE FROM message_sms_templates ");
        this.query.append("WHERE message_sms_template_id="+getTemplate_db_id+";");
    }
    public String toString(){
        return this.query.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

}
