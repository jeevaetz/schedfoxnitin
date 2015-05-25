/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.components;

import javax.swing.JComponent;
import rmischedule.xadmin.model.DynamicFieldValue;

/**
 *
 * @author user
 */
public interface DynamicFieldTypeInterface {
    public String getValue();
    public void setValue(DynamicFieldValue value);
    public JComponent getComponent();
}
