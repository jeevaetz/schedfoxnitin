/*
 * get_certifications_query.java
 *
 * Created on October 12, 2005, 8:02 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class get_certifications_query extends GeneralQueryFormat {
    
    public String certId;
    private boolean showOnlyVisible;
    
    /** Creates a new instance of get_certifications_query */
    public get_certifications_query() {
        myReturnString = new String();
        certId = "";
        showOnlyVisible = false;
    }
    
    public void update(String cert, boolean showOnlyVisible) {
        certId = cert;
        this.showOnlyVisible = showOnlyVisible;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("certification_id as certid,");
        sql.append("certification_name as certname, ");
        sql.append("certification_description as certdesc, ");
        sql.append("certification_default_renewal_time as certrenewal ");
        sql.append("FROM certifications ");
        
        StringBuffer whereClause = new StringBuffer();
        
        if (certId.length() > 0) {
            whereClause.append("certifications.certification_id = " + certId + " ");
        }
        if (showOnlyVisible) {
            if (whereClause.length() > 0) {
                whereClause.append("AND ");
            }
            whereClause.append("certifications.allow_filtering = true ");
        }

        if (whereClause.length() > 0) {
            sql.append("WHERE ");
            sql.append(whereClause.toString());
        }

        return sql.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
