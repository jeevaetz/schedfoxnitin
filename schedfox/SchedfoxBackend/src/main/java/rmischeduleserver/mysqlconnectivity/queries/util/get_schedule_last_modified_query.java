/*
 * get_schedule_last_modified_query.java
 *
 * Created on January 18, 2006, 9:13 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class get_schedule_last_modified_query extends GeneralQueryFormat {

    private String companyDb;
    
    /** Creates a new instance of get_schedule_last_modified_query */
    public get_schedule_last_modified_query() {
        myReturnString = "";
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT lu, \"user\".* FROM ");
        sql.append("( ");
        sql.append("(SELECT schedule_last_updated as lu, last_user_changed FROM ").append(companyDb).append(".schedule ORDER BY schedule_last_updated DESC LIMIT 1)  ");
        sql.append("UNION ");
        sql.append("(SELECT schedule_master_last_updated as lu, last_user_changed FROM ").append(companyDb).append(".schedule_master ORDER BY schedule_master_last_updated DESC LIMIT 1)  ");
        sql.append(") as data ");
        sql.append("INNER JOIN ").append(this.getManagementSchema()).append(".user ON \"user\".user_id = data.last_user_changed ");
        sql.append("ORDER BY lu DESC ");
        sql.append("LIMIT 1 ");
        return sql.toString();
    }

    public void update(String companyDB) {
        this.companyDb = companyDB;
    }

    public boolean hasAccess() {
        return true;
    }
    
}
