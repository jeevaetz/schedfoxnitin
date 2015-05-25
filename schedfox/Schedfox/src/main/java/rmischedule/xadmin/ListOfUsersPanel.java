/*
 * ListOfUsersPanel.java
 *
 * Created on September 5, 2005, 11:24 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package rmischedule.xadmin;
import rmischedule.components.graphicalcomponents.*;
/**
 *
 * @author Owner
 */
public class ListOfUsersPanel extends GraphicalListParent {
    
    private parentFormInterface parent;
    
    /** Creates a new instance of ListOfUsersPanel */
    public ListOfUsersPanel(parentFormInterface parentForm) {
        parent = parentForm;
    }
    
    public void functionToRunOnSelectedShift(Object objectContained, boolean selected) {
        parent.runOnClickUser(objectContained);
    }
    
}
