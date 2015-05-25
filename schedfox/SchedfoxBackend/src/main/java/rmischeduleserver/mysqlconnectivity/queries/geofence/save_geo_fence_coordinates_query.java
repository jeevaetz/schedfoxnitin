/*
 * employee_banned_list_query.java
 *
 * Created on January 25, 2005, 9:07 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.geofence;
import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.*;
import schedfoxlib.model.GeoFencingPoints;
/**
 *
 * @author ira
 */
public class save_geo_fence_coordinates_query extends GeneralQueryFormat {
    
    private Integer numberOfLoops = 0;
    
    /** Creates a new instance of employee_banned_list_query */
    public save_geo_fence_coordinates_query() {

    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }

    /**
     * Does this use prepared statements
     * @return boolean
     */
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM geo_fencing_points WHERE geo_fencing_id = ?;");
        for (int n = 0; n < numberOfLoops; n++) {
            sql.append("INSERT INTO ");
            sql.append("geo_fencing_points ");
            sql.append("(latitude, longitude, geo_fencing_id, point_count) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?); ");
        }
        return sql.toString();
    }

    public void update(ArrayList<GeoFencingPoints> points, Integer geoFencingId) {
        Object[] values = new Object[(points.size() * 4) + 1];
        values[0] = geoFencingId;
        for (int p = 0; p < points.size(); p++) {
            values[(p * 4) + 1] = points.get(p).getLatitude();
            values[(p * 4) + 2] = points.get(p).getLongitude();
            values[(p * 4) + 3] = points.get(p).getGeoFencingId();
            values[(p * 4) + 4] = points.get(p).getPointCount();
        }
        numberOfLoops = points.size();
        super.setPreparedStatement(values);
    }
    
}
