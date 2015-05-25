/*
 * GenericQuery.java
 *
 * Created on May 23, 2006, 8:25 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.util;

/**
 *
 * @author shawn
 */
public class GenericQuery extends rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat {
    
    private String queryText;
    
    /** Creates a new instance of GenereicQuery */
    public GenericQuery(String query) { this.queryText = query; }
    public GenericQuery() {}
    public boolean hasAccess() { return true; }
    
    public void setQueryText(String query) { this.queryText = query; }
    public String getQueryText() { return this.queryText; }
    
    public String toString() { return this.queryText; }
    
}
