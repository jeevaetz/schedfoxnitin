/*
 * ListOfGroupsPanel.java
 *
 * Created on September 5, 2005, 6:27 PM
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
public class ListOfGroupsPanel extends GraphicalListParent {
    
    private xGroups parent;
    
    /** Creates a new instance of ListOfGroupsPanel */
    public ListOfGroupsPanel(xGroups myParent) {
        parent = myParent;
    }
    
    public void functionToRunOnSelectedShift(Object objectContained, boolean selected) {
        //parent.runOnClickUser((xadminusers.User)objectContained);
    }
    
}
