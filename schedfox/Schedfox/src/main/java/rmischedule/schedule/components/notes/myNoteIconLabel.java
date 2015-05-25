/*
 * myNoteIconLabel.java
 *
 * Created on February 15, 2006, 11:16 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.schedule.components.notes;
import com.creamtec.ajaxswing.AjaxSwingManager;
import javax.swing.*;
import java.awt.event.*;

import rmischeduleserver.data_connection_types.*;
import rmischedule.schedule.*;
import rmischedule.main.*;

import java.awt.*;
import java.util.ArrayList;
/**
 *
 * @author Ira Juneau
 */
public abstract class myNoteIconLabel extends JLabel {
    
    private boolean isInsideThisLabel;
    private Schedule_View_Panel myParent;
    private myNoteIconLabel thisObject;
    
    /** Creates a new instance of myNoteIconLabel */
    public myNoteIconLabel(Schedule_View_Panel svp) {
        super();
        myParent = svp;
        isInsideThisLabel = false;
        thisObject = this;
        this.addMouseListener(new getMouseEnteredExited());
    }
    
    public myNoteIconLabel(String lab, Schedule_View_Panel svp) {
        super(lab);
        myParent = svp;
        isInsideThisLabel = false;
        thisObject = this;
        this.addMouseListener(new getMouseEnteredExited());
    }
        
    public boolean isInsideThisLabel() {
        return isInsideThisLabel;
    }

        @Override
    public JToolTip createToolTip() {
    	if(AjaxSwingManager.isAjaxSwingRunning()) {
    		isInsideThisLabel = true;
    		JToolTip tooltip = new JToolTip();
			tooltip.setComponent(this);
            if (hasNote()) {
                ArrayList<NoteData> notes = getNotes();
                if (notes != null) {
                    myParent.displayNotes(notes, thisObject);
                    tooltip.add(myParent.getMyNoteDisplay());
                }
            }
            isInsideThisLabel = false;
    		return tooltip;
    	}
    	return super.createToolTip();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (hasNote()) {
            Main_Window.Note16x16.paintIcon(this, g, 1, 1);
        }
    }
    
    private class getMouseEnteredExited extends MouseAdapter {
        public void mouseEntered(MouseEvent e) {
            isInsideThisLabel = true;
            if (hasNote()) {
                ArrayList<NoteData> notes = getNotes();
                if (notes != null) {
                    myParent.displayNotes(notes, thisObject);
                }
            }
        }
        
        public void mouseExited(MouseEvent e) {
            isInsideThisLabel = false;
        }
    }
    
    /**
     * Overload for specific component to say when has Note...
     */
    public abstract boolean hasNote();
    
    /**
     * Overload for specific component to get Record_Set from query for object
     */
    public abstract ArrayList<NoteData> getNotes();
    
}
