/*
 * get_employee_count_for_invoicing_query.java
 *
 * Created on January 18, 2006, 8:26 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class get_employee_count_for_invoicing_query extends GeneralQueryFormat {

    private String companyDb;

    /** Creates a new instance of get_employee_count_for_invoicing_query */
    public get_employee_count_for_invoicing_query() {
        myReturnString = "";
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ");
        sql.append(companyDb).append(".employee ");
        sql.append("WHERE ");
        sql.append("employee_is_deleted != 1");
        return sql.toString();
    }

    public void update(String companyDB) {
        this.companyDb = companyDB;
    }

    public boolean hasAccess() {
        return true;
    }
    
}
