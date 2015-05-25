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
public class ClientNotes implements Serializable, NoteInterface {
    private static final long serialVersionUID = 1L;

    private Integer clientNotesId;
    private int userId;
    private int clientId;
    private int noteTypeId;
    private Date clientNotesDateTime;
    private String clientNotesNotes;
    private Boolean readOnCheckin;

    public ClientNotes() {
    }

    public ClientNotes(Record_Set rst) {
        try {
            if (rst.hasColumn("client_notes_id")) {
                this.clientNotesId = rst.getInt("client_notes_id");
            } else {
                this.clientNotesId = rst.getInt("note_id");
            }
        } catch (Exception e) {}
        try {
            
            this.userId = rst.getInt("user_id");
        } catch (Exception e) {}
        try {
            this.clientId = rst.getInt("client_id");
        } catch (Exception e) {}
        try {
            this.noteTypeId = rst.getInt("note_type_id");
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("cient_notes_date_time")) {
                this.clientNotesDateTime = rst.getTimestamp("client_notes_date_time");
            } else {
                this.clientNotesDateTime = rst.getTimestamp("note_date");
            }
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("clientNotesNotes")) {
                this.clientNotesNotes = rst.getString("clientNotesNotes");
            } else {
                this.clientNotesNotes = rst.getString("note");
            }
        } catch (Exception e) {}
        try {
            this.readOnCheckin = rst.getBoolean("read_on_checkin");
        } catch (Exception e) {}
    }

    public Integer getClientNotesId() {
        return clientNotesId;
    }

    public void setClientNotesId(Integer clientNotesId) {
        this.clientNotesId = clientNotesId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getNoteTypeId() {
        return noteTypeId;
    }

    public void setNoteTypeId(int noteTypeId) {
        this.noteTypeId = noteTypeId;
    }

    public Date getClientNotesDateTime() {
        return clientNotesDateTime;
    }

    public void setClientNotesDateTime(Date clientNotesDateTime) {
        this.clientNotesDateTime = clientNotesDateTime;
    }

    public String getClientNotesNotes() {
        return clientNotesNotes;
    }

    public void setClientNotesNotes(String clientNotesNotes) {
        this.clientNotesNotes = clientNotesNotes;
    }

    public Boolean getReadOnCheckin() {
        return readOnCheckin;
    }

    public void setReadOnCheckin(Boolean readOnCheckin) {
        this.readOnCheckin = readOnCheckin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientNotesId != null ? clientNotesId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientNotes)) {
            return false;
        }
        ClientNotes other = (ClientNotes) object;
        if ((this.clientNotesId == null && other.clientNotesId != null) || (this.clientNotesId != null && !this.clientNotesId.equals(other.clientNotesId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ClientNotes[clientNotesId=" + clientNotesId + "]";
    }

    public Integer getPrimaryKey() {
        return this.getClientNotesId();
    }

    public int getObjectId() {
        return this.getClientId();
    }

    public Date getDateEntered() {
        return this.getClientNotesDateTime();
    }

    public String getNote() {
        return this.getClientNotesNotes();
    }
    
    public void setNote(String note) {
        this.clientNotesNotes = note;
    }

}
