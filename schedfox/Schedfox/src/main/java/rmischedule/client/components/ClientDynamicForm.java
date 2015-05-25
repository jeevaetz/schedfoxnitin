/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.client.components;

import rmischedule.components.graphicalcomponents.GenericTabbedEditForm;
import rmischedule.employee.components.EmployeeDynamicForm;

/**
 *
 * @author user
 */
public class ClientDynamicForm extends EmployeeDynamicForm {

    public ClientDynamicForm(GenericTabbedEditForm myParent) {
        super(myParent);
    }

    @Override
    protected boolean isClient() {
        return true;
    }

    @Override
    protected boolean isEmployee() {
        return false;
    }
}
