/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class EmployeeNotes implements Serializable, NoteInterface {
    private static final long serialVersionUID = 1L;
    private Integer employeeNotesId;
    private int userId;
    private int employeeId;
    private int noteTypeId;
    private Date employeeNotesDateTime;
    private String employeeNotesNotes;

    public EmployeeNotes() {
    }

    public EmployeeNotes(Record_Set rst) {
        try {
            if (rst.hasColumn("employee_notes_id")) {
                this.employeeNotesId = rst.getInt("employee_notes_id");
            } else {
                this.employeeNotesId = rst.getInt("note_id");
            }
        } catch (Exception e) {}
        try {
            
            this.userId = rst.getInt("user_id");
        } catch (Exception e) {}
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}
        try {
            this.noteTypeId = rst.getInt("note_type_id");
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("employee_notes_date_time")) {
                this.employeeNotesDateTime = rst.getTimestamp("employee_notes_date_time");
            } else {
                this.employeeNotesDateTime = rst.getTimestamp("note_date");
            }
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("employee_notes_notes")) {
                this.employeeNotesNotes = rst.getString("employee_notes_notes");
            } else {
                this.employeeNotesNotes = rst.getString("note");
            }
        } catch (Exception e) {}
        try {
            //this.readOnCheckin = rst.getBoolean("read_on_checkin");
        } catch (Exception e) {}
    }
    
    public EmployeeNotes(Integer employeeNotesId) {
        this.employeeNotesId = employeeNotesId;
    }

    public EmployeeNotes(Integer employeeNotesId, int userId, int employeeId, int noteTypeId, Date employeeNotesDateTime, String employeeNotesNotes) {
        this.employeeNotesId = employeeNotesId;
        this.userId = userId;
        this.employeeId = employeeId;
        this.noteTypeId = noteTypeId;
        this.employeeNotesDateTime = employeeNotesDateTime;
        this.employeeNotesNotes = employeeNotesNotes;
    }

    public Integer getEmployeeNotesId() {
        return employeeNotesId;
    }

    public void setEmployeeNotesId(Integer employeeNotesId) {
        this.employeeNotesId = employeeNotesId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getNoteTypeId() {
        return noteTypeId;
    }

    public void setNoteTypeId(int noteTypeId) {
        this.noteTypeId = noteTypeId;
    }

    public Date getEmployeeNotesDateTime() {
        return employeeNotesDateTime;
    }

    public void setEmployeeNotesDateTime(Date employeeNotesDateTime) {
        this.employeeNotesDateTime = employeeNotesDateTime;
    }

    public String getEmployeeNotesNotes() {
        return employeeNotesNotes;
    }

    public void setEmployeeNotesNotes(String employeeNotesNotes) {
        this.employeeNotesNotes = employeeNotesNotes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeNotesId != null ? employeeNotesId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeNotes)) {
            return false;
        }
        EmployeeNotes other = (EmployeeNotes) object;
        if ((this.employeeNotesId == null && other.employeeNotesId != null) || (this.employeeNotesId != null && !this.employeeNotesId.equals(other.employeeNotesId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.EmployeeNotes[employeeNotesId=" + employeeNotesId + "]";
    }

    @Override
    public Integer getPrimaryKey() {
        return this.employeeNotesId;
    }

    @Override
    public int getObjectId() {
        return this.employeeId;
    }

    @Override
    public Date getDateEntered() {
        return this.employeeNotesDateTime;
    }

    @Override
    public String getNote() {
        return this.employeeNotesNotes;
    }
    
    public void setNote(String note) {
        this.employeeNotesNotes = note;
    }

    @Override
    public Boolean getReadOnCheckin() {
        return false;
    }

}
