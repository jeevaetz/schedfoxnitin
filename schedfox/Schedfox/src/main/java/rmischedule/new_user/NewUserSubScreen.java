/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.new_user;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author user
 */
public interface NewUserSubScreen {
    public Hashtable<String, Object> getValues();
    public ArrayList<String> getValidationString();
    public Container getContainer();
}
