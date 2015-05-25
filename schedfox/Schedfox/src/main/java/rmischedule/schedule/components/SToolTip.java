/*
 * SToolTip.java
 *
 * Created on December 15, 2004, 1:27 PM
 */

package rmischedule.schedule.components;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import rmischedule.schedule.components.*;
/**
 *
 * @author  ira
 */
public class SToolTip extends JToolTip {
    
    int charactersPerLine = 0;
    
    /** Creates a new instance of SToolTip */
    public SToolTip(int placesToShowPerLine)  {
        charactersPerLine = placesToShowPerLine;
        this.setBackground(new Color(255,255,255,190));
        
    }
    
    public void setTipText(String text) {
        if (text.length() == 0) {
            setBorder(null);
        } else {
            setBorder(new LineBorder(new Color(0,0,0), 2, true));
        }
        try {
            if (text.length() == 0) {
                super.setTipText("");
            } else {
                super.setTipText(formatToolTipText(text));
            }
        } catch (Exception e) {
            super.setTipText("");
        }
    }
    
    /**
     * Places line breaks in tooltips....
     */
    private String formatToolTipText(String input) {
        StringBuffer returnBuffer = new StringBuffer("<html>" + input + "</html>");
        return returnBuffer.toString();
    }
    
}
