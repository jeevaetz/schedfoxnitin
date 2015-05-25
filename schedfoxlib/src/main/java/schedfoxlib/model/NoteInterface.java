/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.util.Date;

/**
 *
 * @author user
 */
public interface NoteInterface {

    /**
     * Primary key of table
     * @return
     */
    public Integer getPrimaryKey();

    public int getUserId();

    /**
     * Employee or Client primary key
     * @return
     */
    public int getObjectId();

    public int getNoteTypeId();

    public Date getDateEntered();

    public String getNote();
    
    public void setNote(String note);

    public Boolean getReadOnCheckin();

}
