
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
public class InsertMsgQuery extends GeneralQueryFormat {

    private StringBuilder queryString = new StringBuilder();

    @Override
    public boolean hasAccess() {
        return true;
    }
    //private to prevent non-overloaded instantiation

    private InsertMsgQuery() {
    }

    public InsertMsgQuery(String senderID, String UserId, String companyScheme, String subject, String msg) {
        subject = subject.replaceAll("'", "");
        subject = subject.replaceAll("\n", " ");
        subject = subject.replaceAll("\t", " ");
        msg = msg.replaceAll("'", "");
        msg = msg.replaceAll("\n", " ");
        msg = msg.replaceAll("\t", " ");
        this.queryString.append("set search_path = '" + companyScheme + "';");
        this.queryString.append("INSERT INTO internalemail (sender_id,reciever_id,subject,message)VALUES(");
        this.queryString.append(senderID);
        this.queryString.append("," + UserId + ",'" + subject + "','" + msg + "');");
    }

    public String toString() {
        System.out.println(this.queryString);
        return this.queryString.toString();
    }
}
