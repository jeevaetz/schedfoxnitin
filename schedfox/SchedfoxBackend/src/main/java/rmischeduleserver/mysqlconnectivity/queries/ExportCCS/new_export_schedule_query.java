/*
 * new_export_schedule_query.java
 *
 * Created on December 13, 2005, 3:28 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.ExportCCS;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.*;
/**
 *
 * @author Ira Juneau
 */
public class new_export_schedule_query extends generic_assemble_schedule_query {
    
    /** Creates a new instance of new_export_schedule_query */
    public new_export_schedule_query() {
    }
    
    public void update(String sdate, String edate) {
        super.update("", "", sdate,edate, "", "", false);
        super.setSelectedFields(super.myExportFields);
        super.ShowDeleted = false;
    }

    @Override
    public String additionalFields() {
        return ", rate_code.usked_rate_code             as urc ";
    }

    @Override
    protected String generateCompleteWhereClause() {
        StringBuilder sql = new StringBuilder();
        sql.append("LEFT JOIN rate_code ON ");
        sql.append("  rate_code.rate_code_id = assemble_schedule.rate_code_id ");
        sql.append("WHERE isdeleted = '0' AND eid != 0 ");
        return sql.toString();
    }


}
