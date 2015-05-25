/*
 * GenericListContainer.java
 *
 * Created on September 7, 2005, 10:21 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.components.graphicalcomponents;

import rmischedule.admin.newuser_alert.NewUser_Alert_Screen;

/**
 *
 * @author Ira Juneau
 */
public class GenericListContainer extends GraphicalListParent {
    
    private GenericEditForm parent;
    private NewUser_Alert_Screen alertParent;
    
    /** Creates a new instance of ListOfUsersPanel */
    public GenericListContainer(GenericEditForm parentForm) {
        parent = parentForm;
    }

    public GenericListContainer(NewUser_Alert_Screen parentForm)
    {
        this.alertParent = parentForm;
    }
    
    public void functionToRunOnSelectedShift(Object objectContained, boolean selected) {
        if(this.alertParent != null)
            this.alertParent.runOnClickUser(objectContained, selected);
        else
            parent.runOnClickUser(objectContained, selected);
    }
    
}
