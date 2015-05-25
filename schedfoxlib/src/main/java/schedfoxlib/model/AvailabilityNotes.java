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
public class AvailabilityNotes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer availabilityNoteId;
    private int availabilityId;
    private Date dateEntered;
    private int enteredBy;
    private String note;

    public AvailabilityNotes() {
    }

    public AvailabilityNotes(Integer availabilityNoteId) {
        this.availabilityNoteId = availabilityNoteId;
    }

    public AvailabilityNotes(Record_Set rst) {
        try {
            this.availabilityNoteId = rst.getInt("availability_note_id");
        } catch (Exception e) {}
        try {
            this.availabilityId = rst.getInt("availability_id");
        } catch (Exception e) {}
        try {
            this.dateEntered = rst.getDate("date_entered");
        } catch (Exception e) {}
        try {
            this.enteredBy = rst.getInt("entered_by");
        } catch (Exception e) {}
        try {
            this.note = rst.getString("note");
        } catch (Exception e) {}
    }

    public Integer getAvailabilityNoteId() {
        return availabilityNoteId;
    }

    public void setAvailabilityNoteId(Integer availabilityNoteId) {
        this.availabilityNoteId = availabilityNoteId;
    }

    public int getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(int availabilityId) {
        this.availabilityId = availabilityId;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    public int getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(int enteredBy) {
        this.enteredBy = enteredBy;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (availabilityNoteId != null ? availabilityNoteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AvailabilityNotes)) {
            return false;
        }
        AvailabilityNotes other = (AvailabilityNotes) object;
        if ((this.availabilityNoteId == null && other.availabilityNoteId != null) || (this.availabilityNoteId != null && !this.availabilityNoteId.equals(other.availabilityNoteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.AvailabilityNotes[availabilityNoteId=" + availabilityNoteId + "]";
    }

}
