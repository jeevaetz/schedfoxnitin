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
public class EmailFolderQuery extends GeneralQueryFormat {

    private static final EmailFolderQuery INSTANCE = new EmailFolderQuery();

    private EmailFolderQuery() {
    }

    public static EmailFolderQuery getInstance() {
        return EmailFolderQuery.INSTANCE;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    public String toString() {
        return "select * from control_db.emailFolders;";
    }
}
