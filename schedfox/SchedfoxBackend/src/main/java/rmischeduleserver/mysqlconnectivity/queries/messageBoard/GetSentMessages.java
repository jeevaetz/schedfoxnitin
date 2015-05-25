/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.messageBoard;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class GetSentMessages extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    private String userId;
    private String schema;

    //so only the overloaded contructor can be called cause its private
    private GetSentMessages() {
    }

    public GetSentMessages(String userId, String schema) {
        this.userId = userId;
        this.schema = schema;
    }

    public String toString() {
        return "set search_path = '" + this.schema + "';" +
                "select * from internalemail where sender_id = " + this.userId + ";";

    }
}
