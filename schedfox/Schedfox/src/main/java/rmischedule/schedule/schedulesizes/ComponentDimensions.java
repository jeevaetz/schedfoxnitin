/*
 * ComponentDimensions.java
 *
 * Created on September 6, 2006, 7:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischedule.schedule.schedulesizes;

import java.awt.Dimension;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * A little bit of ugliness to help deal with resizing/zooming the schedule.  This is just a centralized place for
 * schedule component sizes.  If you need to add a new zoomable component to the schedule, it should extend ZoomablePanel,
 * and its getKeyString() method should refer to one of the keys found here.
 *
 * @author shawn
 */
public class ComponentDimensions {
    
    private static double zoomScale = 1.00;
    private static ArrayList<WeakReference> zoomListeners = new ArrayList();
    
    private static final Dimension[] sizes = {  new Dimension( 60, 60),         // SShift
                                                new Dimension( 50, 20),         // shiftsTotalsSize
                                                new Dimension( 60, 20),         // shiftsDaySize
                                                new Dimension(150, 30),         // employeeListHeaderSize
                                                new Dimension( 50, 30),         // printerLabelSize
                                                new Dimension( 50, 60),         // RowTotal
                                                new Dimension(150, 60),         // RowHeader
                                                new Dimension( 50, 50),         // Total_Pane
                                                new Dimension(150, 20),         // RowTotalPanel
                                                new Dimension( 60, 50),         // individualHeaderPanel
                                                new Dimension(400, 30)          // individualClientHeader
    };


    
    private static final String[] keys =    {   "SShift",
                                                "shiftsTotalsSize",
                                                "shiftsDaySize",
                                                "employeeListHeaderSize",
                                                "printerLabelSize",
                                                "RowTotal",
                                                "RowHeader",
                                                "Total_Pane",
                                                "RowTotalPanel",
                                                "individualHeaderPanel",
                                                "individualClientHeader"
    };
    
    public static final Hashtable<String, Dimension> defaultSizes = new Hashtable(keys.length);
    static {
        for(int i = 0; i < keys.length; i++) {
            defaultSizes.put(keys[i], sizes[i]);
        }
    }
    
    public static Hashtable<String, Dimension> currentSizes = new Hashtable(keys.length);
    static {
        for(int i = 0; i < keys.length; i++) {
            Dimension base = defaultSizes.get(keys[i]);
            Dimension d = new Dimension(base.width, base.height);
            currentSizes.put(keys[i], d);
        }
    }
    
    public static void addZoomListener(ZoomListener listener) {
        //WeakReference ref = new WeakReference(listener);
        //zoomListeners.add(ref);
    }
    
    public static void zoomIn() {
        if(zoomScale < 1.70) zoomScale += 0.10;
        ComponentDimensions.scaleSizes(zoomScale);
    }
    
    public static void zoomOut() {
        if(zoomScale > 0.40) zoomScale -= 0.10;
        ComponentDimensions.scaleSizes(zoomScale);        
    }
    
    private static void scaleSizes(double scaleFactor) {
        for(int i = 0; i < keys.length; i++) {
            Dimension base = defaultSizes.get(keys[i]);
            Dimension d = currentSizes.get(keys[i]);
            d.setSize(base.width * scaleFactor, base.height * scaleFactor);
        }
        fireZoomChanged();
    }
    
    private static void fireZoomChanged() {
        for(int i = 0; i < zoomListeners.size(); i++) {
            Object obj = zoomListeners.get(i).get();
            if(obj == null) {
                zoomListeners.remove(i);
                i--;
            } else if(obj instanceof ZoomListener) {
                ZoomListener zl = (ZoomListener)obj;
                zl.zoomPerformed();
            }
        }
        fireZoomFinished();
    }
    
    private static void fireZoomFinished() {
        for(WeakReference ref : zoomListeners) {
            Object obj = ref.get();
            if(obj instanceof ZoomListener) {
                ZoomListener zl = (ZoomListener)obj;
                zl.zoomFinished();
            }
        }
    }
    
    public static double getScaleValue() { return zoomScale; }
    
}
