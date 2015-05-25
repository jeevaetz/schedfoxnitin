/*
 * employee_note_query.java
 *
 * Created on January 24, 2005, 3:05 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class employee_note_query extends GeneralQueryFormat {
    
    private String EmpId;
    
    /** Creates a new instance of employee_note_query */
    public employee_note_query() {
        myReturnString = new String();
    }
    
    public void update(String empId) {
        EmpId = empId;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    /**
     *
     * @return
     */
    public String toString(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("employee_notes.employee_notes_id        as note_id, ");
        sql.append("(" + getManagementSchema() + "." + getDriver().getTableName("user") + ".user_first_name || ");
        sql.append(getManagementSchema() + "." + getDriver().getTableName("user") + ".user_last_name) as user_name, ");
        sql.append(getManagementSchema() + "." + getDriver().getTableName("user") + ".user_id as userid, ");
        sql.append("employee_notes.employee_notes_date_time as note_date, ");
        sql.append("(CASE WHEN employee_notes.note_type_id < 0 THEN global_note_types.note_type_name ELSE note_type.note_type_name END) as ntname, ");
        sql.append("employee_notes_notes AS note, employee_id, employee_notes.note_type_id, employee_notes.user_id ");
        sql.append("FROM employee_notes ");
        sql.append("LEFT JOIN  note_type ON employee_notes.note_type_id =  note_type.note_type_id ");
        sql.append("LEFT JOIN " + getManagementSchema() + "." + getDriver().getTableName("user") + " ON  employee_notes.user_id = " + getManagementSchema() + "." + getDriver().getTableName("user") + ".user_id ");
        sql.append("LEFT JOIN " + getManagementSchema() + ".global_note_types ON global_note_types.note_type_id = employee_notes.note_type_id ");
        sql.append("WHERE ");
        sql.append("employee_notes.employee_id = ").append(EmpId);
        sql.append("ORDER BY employee_notes_date_time DESC ");
        return sql.toString();
    }
    
}
