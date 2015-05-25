/*
 * ImageQuery.java
 *
 * Created on July 21, 2006, 9:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author shawn
 */
public class ImageQuery extends GeneralQueryFormat {
    
    public String baseDirectory;
    public String fileName;
    
    /** Creates a new instance of ImageQuery */
    public ImageQuery(String base, String fname) {
        this.baseDirectory = base;
        this.fileName = fname;
    }
    
    
    public boolean hasAccess() { return false; }
}
