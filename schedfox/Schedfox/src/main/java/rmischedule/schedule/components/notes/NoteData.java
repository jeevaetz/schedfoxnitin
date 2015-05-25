/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.schedule.components.notes;

import java.util.Date;

/**
 *
 * @author user
 */
public class NoteData {
    private String userLogin;
    private Date date;
    private String noteType;
    private String note;

    /**
     * @return the userLogin
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * @param userLogin the userLogin to set
     */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the noteType
     */
    public String getNoteType() {
        return noteType;
    }

    /**
     * @param noteType the noteType to set
     */
    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }
}
