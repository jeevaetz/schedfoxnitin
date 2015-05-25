/*
 * ImportGenericType.java
 *
 * Created on March 1, 2006, 11:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischedule.data_import.graphicalimportClasses;
import javax.swing.*;
/**
 *
 * @author Ira Juneau
 */
public interface ImportGenericType {
    public String getTitle();
    public JPanel getComponent();
    public void doOnNext();
    public void showMe();
    public void setParent(ImportDataWindow parent);
}
