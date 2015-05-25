/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.client.components;

import schedfoxlib.model.NoteInterface;

/**
 *
 * @author user
 */
public interface ClientNoteInterface {
    public void save();
    public void reloadData();
    public void deleteNote(NoteInterface note);
    public void saveNote(NoteInterface note);
}
