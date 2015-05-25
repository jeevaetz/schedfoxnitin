/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.utility;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Silly little class that lets a mouse action go to the parent and not get just caught on the 
 * current level.
 * @author ira
 */
public class ParentTrickleListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
        e.getComponent().getParent().dispatchEvent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        e.getComponent().getParent().dispatchEvent(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.getComponent().getParent().dispatchEvent(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        e.getComponent().getParent().dispatchEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        e.getComponent().getParent().dispatchEvent(e);
    }
};
