/*
 * To change this template, choose Tools | Templates
 * and open the template inn the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.messaging.email;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class GetNextValMessaging extends GeneralQueryFormat {

    private static final GetNextValMessaging INSTANCE = new GetNextValMessaging();

    private GetNextValMessaging(){
        //yay private
    }
    @Override
       public boolean hasAccess() {
      return true;
    }
    public static GetNextValMessaging getInstance(){
        return INSTANCE;
    }

    public String toString() {
        return "SELECT nextval('control_db.messaging_outbound_seq') AS id FROM control_db.messaging_outbound LIMIT 1";
    }
}
