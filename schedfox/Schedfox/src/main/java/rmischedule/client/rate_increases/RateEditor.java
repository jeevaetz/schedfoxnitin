/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.client.rate_increases;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import rmischedule.components.graphicalcomponents.DragAndDropContainer;

/**
 *
 * @author user
 */
public class RateEditor extends JTextPane implements DragAndDropContainer {

    public RateEditor() {
        ActionListener backspaceActionListener = this.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
        this.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), new BackwardAction(backspaceActionListener));
    }

    public void runOnDrop(Object objectToPassIn, MouseEvent evt, BufferedImage bi) {
        Point p = evt.getPoint();
        Component source = (Component)evt.getSource();

        int sourceY = source.getY();

        p.x = this.getWidth() + p.x;
        p.y = p.y + sourceY;
        Element e = this.getDocument().getDefaultRootElement();
        this.setCaretPosition(this.viewToModel(p));

        try {
            StyledDocument doc = (StyledDocument) this.getDocument();
            Style style = doc.addStyle(objectToPassIn.toString(), null);
            StyleConstants.setIcon(style, new ImageIcon(bi));

            this.getDocument().insertString(this.getCaretPosition(), objectToPassIn.toString(), style);
        } catch (Exception exe) {
            exe.printStackTrace();
        }

    }

    public void highlightMe(boolean highlightMe, Object myObj, MouseEvent evt) {
        Point p = evt.getPoint();
        Component source = (Component)evt.getSource();
        int sourceY = source.getY();

        p.x = this.getWidth() + p.x;
        p.y = p.y + sourceY;
        this.setCaretPosition(this.viewToModel(p));
    }

    private class BackwardAction extends AbstractAction {

        private ActionListener defaultActionListener;

        public BackwardAction(ActionListener actionListener) {

            defaultActionListener = actionListener;

        }

        public void actionPerformed(ActionEvent e) {

            StyledDocument doc = (StyledDocument) getDocument();
            Element el = doc.getParagraphElement(getCaretPosition());
            getCaretPosition();

            boolean removedAlready = false;
            try {
                String val = getText();
                val = val.substring(getCaretPosition() - 1, getCaretPosition());
                if (val.equals("}")) {
                    int lastPos = getText().lastIndexOf("{");
                    if (getCaretPosition() - lastPos < 18) {
                        
                        doc.remove(lastPos, getCaretPosition() - lastPos);
                        removedAlready = true;
                    }
                }

                if (!removedAlready) {
                    defaultActionListener.actionPerformed(e);
                }
            } catch (Exception exe) {
                exe.printStackTrace();
            }

        }
    }
}
