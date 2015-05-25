/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_client_query extends GeneralQueryFormat {

    private String client_id;
    private String url;
    private String username;
    private String password;

    public void update(String client_id, String url, String username, String password) {
        this.client_id = client_id;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE client SET ");
        sql.append("url = '" + url.replaceAll("'", "") + "', ");
        sql.append("cusername = '" + username.replaceAll("'", "") + "', ");
        sql.append("cpassword = '" + password.replaceAll("'", "") + "' ");
        sql.append("WHERE ");
        sql.append("client_id = " + client_id + ";");
        return sql.toString();
    }
}
