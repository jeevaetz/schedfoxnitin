/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class NoteType {
    private Integer noteTypeId;
    private String noteTypeName;
    
    public NoteType() {
        
    }

    public NoteType(Record_Set rst) {
        try {
            noteTypeId = rst.getInt("note_type_id");
        } catch (Exception exe) {}
        try {
            noteTypeName = rst.getString("note_type_name");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the noteTypeId
     */
    public Integer getNoteTypeId() {
        return noteTypeId;
    }

    /**
     * @param noteTypeId the noteTypeId to set
     */
    public void setNoteTypeId(Integer noteTypeId) {
        this.noteTypeId = noteTypeId;
    }

    /**
     * @return the noteTypeName
     */
    public String getNoteTypeName() {
        return noteTypeName;
    }

    /**
     * @param noteTypeName the noteTypeName to set
     */
    public void setNoteTypeName(String noteTypeName) {
        this.noteTypeName = noteTypeName;
    }
    
    @Override
    public String toString() {
        return this.noteTypeName;
    }
}
