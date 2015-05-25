/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.utility;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author ira
 */
public class WordDocumentListener implements DocumentListener {

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            String documentStr = e.getDocument().getText(0, e.getLength() - 1);
            e.getDocument().remove(0, e.getLength() - 1);
            e.getDocument().insertString(0, documentStr, null);
        } catch (Exception exe) {
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        try {
            String documentStr = e.getDocument().getText(0, e.getLength() - 1);
            e.getDocument().remove(0, e.getLength() - 1);
            e.getDocument().insertString(0, documentStr, null);
        } catch (Exception exe) {
        }
    }
}
