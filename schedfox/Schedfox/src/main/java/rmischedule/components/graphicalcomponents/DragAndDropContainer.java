/*
 * DragAndDropContainer.java
 *
 * Created on February 28, 2005, 9:41 AM
 */

package rmischedule.components.graphicalcomponents;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 *
 * @author ira
 * Interface that accepts a drop from type DragAndDropLabel...
 *
 */
public interface DragAndDropContainer {
    
    public void runOnDrop(Object objectToPassIn, MouseEvent evt, BufferedImage bi);
    public void highlightMe(boolean highlightMe, Object myObj, MouseEvent evt);
    
}
