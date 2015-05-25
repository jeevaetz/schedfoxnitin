/*
 * client_note_list_query.java
 *
 * Created on January 26, 2005, 2:05 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author jason.allen
 */
public class client_note_list_query extends GeneralQueryFormat{
    
    private String cid;
    
    /** Creates a new instance of client_note_list_query */
    public client_note_list_query(){
        cid = "";
    }
    
    public void update(String Cid){
        cid = Cid;
        super.setPreparedStatement(new Object[]{Cid});
    }
    
    public boolean hasAccess(){
        return true;
    }

    public String getQuery(boolean isPrepared) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("client_notes.client_notes_id        as note_id, ");
        sql.append("(" + getManagementSchema() + "." + getDriver().getTableName("user") + ".user_first_name || ");
        sql.append(getManagementSchema() + "." + getDriver().getTableName("user") + ".user_last_name) as user_name, ");
        sql.append(getManagementSchema() + "." + getDriver().getTableName("user") + ".user_id as userid, ");
        sql.append("client_notes.client_notes_date_time as note_date, ");
        sql.append("(CASE WHEN client_notes.note_type_id < 0 THEN global_note_types.note_type_name ELSE note_type.note_type_name END) as ntname, ");
        sql.append("client_notes_notes AS note, client_id, client_notes.note_type_id, read_on_checkin, client_notes.user_id ");
        sql.append("FROM client_notes ");
        sql.append("LEFT JOIN  note_type ON client_notes.note_type_id =  note_type.note_type_id ");
        sql.append("LEFT JOIN " + getManagementSchema() + "." + getDriver().getTableName("user") + " ON  client_notes.user_id = " + getManagementSchema() + "." + getDriver().getTableName("user") + ".user_id ");
        sql.append("LEFT JOIN " + getManagementSchema() + ".global_note_types ON global_note_types.note_type_id = client_notes.note_type_id ");
        sql.append("WHERE ");
        sql.append("client_notes.client_id = ");
        if (isPrepared) {
            sql.append("? ");
        } else {
            sql.append(cid).append(" ");
        }
        sql.append("ORDER BY client_notes_date_time DESC ");
        return sql.toString();
    }
    
    public String getPreparedStatementString() {
        return getQuery(true);
    }
    
    public String toString() {
        return getQuery(false);
    }
}
