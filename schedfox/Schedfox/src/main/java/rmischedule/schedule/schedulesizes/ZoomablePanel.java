/*
 * ZoomablePanel.java
 *
 * Created on September 6, 2006, 12:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischedule.schedule.schedulesizes;

import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Another stupid class to help with zooming... anything on the schedule that wants to be resized on zoom
 * needs to extend this class.  The getSizeKey() method needs to return the key for the correct size in the
 * ComponentDimensions.currentSizes hashtable--if the key doesn't exist in the hashtable, bad things will happen!
 * Because the schedule uses mostly box-layouts, setting the sizes in this manner will guarantee that they will
 * be resized.  The crap inside this panel will rearrange itself according to the layout manager.  If you need
 * the stuff inside to resize as well, then it needs to extend this class too.  Also, you'll probably want to
 * override the paint method to handle font sizes and what-not.  Yes, I'm aware that this is ugly.
 *
 * @author shawn
 */
public abstract class ZoomablePanel extends JPanel implements ZoomListener {
    
    public ZoomablePanel() { ComponentDimensions.addZoomListener(this); }
    
    public Dimension getPreferredSize()   {
        Dimension d = this.getZoomSize();
        if(d != null) {
            return d;
        }
        else {
            System.out.println("Unable to find size for " + this.getSizeKey());
            return super.getPreferredSize();
        }
    }
    
    public Dimension getMinimumSize()     { return this.getPreferredSize(); }
    public Dimension getMaximumSize()     { return this.getPreferredSize(); }
    
    private final Dimension getZoomSize() { return ComponentDimensions.currentSizes.get(this.getSizeKey()); }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double scale = ComponentDimensions.getScaleValue();
        if(scale > 1.00 || scale < 1.00) {
            Graphics2D g2d = (Graphics2D)g;
            AffineTransform oldTransform = (AffineTransform)g2d.getTransform().clone();
            g2d.scale(scale, scale);
            this.paintComponentCustom(g);
            g2d.setTransform(oldTransform);
        } else {
            this.paintComponentCustom(g);
        }
    }
    
    public void zoomPerformed() { this.revalidate(); }
    public void zoomFinished()  { }
    
    public void paintComponentCustom(Graphics g) { }
    public abstract String getSizeKey();
}
